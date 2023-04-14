package com.giyeok.sugarproto

class DefTraverser(val ast: SugarProtoAst.CompilationUnit) {
  val packageName = ast.pkgDef?.names?.joinToString(".") { it.name }

  private val requiredImports = mutableSetOf<String>()

  private val defs = mutableListOf<ProtoDef>()

  fun traverse(): TraverseResult {
    ast.defs.forEach { defWS ->
      val comments = defWS.comments.filterNotNull()
      when (val def = defWS.def) {
        is SugarProtoAst.ServiceDef ->
          traverseServiceDef(comments, def)

        is SugarProtoAst.MessageDef ->
          traverseMessageDef(comments, def)

        is SugarProtoAst.SealedDef ->
          traverseSealedDef(comments, def)

        is SugarProtoAst.EnumDef ->
          traverseEnumDef(comments, def)
      }
    }
    return TraverseResult(
      packageName,
      requiredImports + ast.imports.map { it.toValue() },
      ast.options,
      defs
    )
  }

  fun createMessageName(namingContext: NamingContext): String =
    namingContext.names.flatMap { it.split('_') }
      .joinToString("") { it.replaceFirstChar { it.uppercase() } }

  fun convertMessageDef(
    comments: List<SugarProtoAst.Comment>,
    name: String,
    members: List<SugarProtoAst.MessageMemberDefWS>,
    namingContext: NamingContext,
  ): ProtoMessageDef {
    val protoMembers = members.map { memberWS ->
      val currentNamingContext = namingContext + name
      val memberComments = memberWS.comments.filterNotNull()
      when (val member = memberWS.def) {
        is SugarProtoAst.FieldDef -> {
          val fieldDef = traverseType(member.typ, currentNamingContext + member.name, mapOf())
          ProtoMessageMember.ProtoFieldDef(
            memberComments,
            fieldDef.valueType,
            member.name.name,
            member.tag,
            member.options
          )
        }

        is SugarProtoAst.EnumDef -> {
          val nestedEnum = convertEnumDef(memberComments, member.name.name, member.members)
          ProtoMessageMember.ProtoNestedEnumDef(nestedEnum)
        }

        is SugarProtoAst.MessageDef -> {
          val nestedMessage =
            convertMessageDef(
              memberComments,
              member.name.name,
              member.members,
              currentNamingContext + name
            )
          ProtoMessageMember.ProtoNestedMessageDef(nestedMessage)
        }

        is SugarProtoAst.OptionDef -> {
          ProtoMessageMember.ProtoMessageOptionDef(memberComments, member)
        }

        is SugarProtoAst.OneOfDef -> {
          val oneofMembers = member.members.map { oneOfMemberWS ->
            val oneofComments = oneOfMemberWS.comments.filterNotNull()
            when (val oneOfMember = oneOfMemberWS.def) {
              is SugarProtoAst.FieldDef -> {
                val fieldDef = traverseType(
                  oneOfMember.typ,
                  currentNamingContext + member.name + oneOfMember.name,
                  mapOf()
                )
                ProtoMessageMember.ProtoOneOfMember.OneOfField(
                  ProtoMessageMember.ProtoFieldDef(
                    oneofComments,
                    fieldDef.valueType,
                    member.name.name,
                    oneOfMember.tag,
                    oneOfMember.options,
                  )
                )
              }

              is SugarProtoAst.OptionDef ->
                ProtoMessageMember.ProtoOneOfMember.OneOfOption(oneofComments, oneOfMember)
            }
          }
          ProtoMessageMember.ProtoOneOf(memberComments, member.name.name, oneofMembers)
        }

        is SugarProtoAst.ReservedDef ->
          ProtoMessageMember.ProtoReservedDef(memberComments, member.ranges)
      }
    }
    return ProtoMessageDef(comments, name, protoMembers)
  }

  fun convertEnumDef(
    comments: List<SugarProtoAst.Comment>,
    name: String,
    members: List<SugarProtoAst.EnumMemberDefWS>
  ): ProtoEnumDef {
    val protoMembers: List<ProtoEnumMember> = members.map { memberWS ->
      val memberComments = memberWS.comments.filterNotNull()
      when (val member = memberWS.def) {
        is SugarProtoAst.EnumFieldDef ->
          ProtoEnumMember.EnumValueDef(
            memberComments,
            member.minusTag,
            member.tag,
            member.name.name,
            member.options
          )

        is SugarProtoAst.OptionDef ->
          ProtoEnumMember.EnumOption(memberComments, member)
      }
    }
    return ProtoEnumDef(comments, name, protoMembers)
  }

  fun convertSealedDef(
    comments: List<SugarProtoAst.Comment>,
    name: String,
    members: List<SugarProtoAst.SealedMemberDefWS>
  ): ProtoSealedDef {
    val protoMembers: List<ProtoMessageMember.ProtoOneOfMember> = members.map { memberWS ->
      val memberComments = memberWS.comments.filterNotNull()
      when (val member = memberWS.def) {
        is SugarProtoAst.FieldDef -> {
          val fieldType =
            traverseType(member.typ, NamingContext(listOf(name, member.name.name)), mapOf())
          check(!fieldType.isStream) { "stream is not allowed here" }
          val fieldValueType = fieldType.valueType
          check(!fieldValueType.optional) { "optional is not allowed for oneof member" }
          check(!fieldValueType.repeated) { "repeated is not allowed for oneof member" }
          check(fieldValueType.kind != FieldKindEnum.MapKind) { "map is not allowed for oneof member" }
          ProtoMessageMember.ProtoOneOfMember.OneOfField(
            ProtoMessageMember.ProtoFieldDef(
              memberComments,
              fieldValueType,
              member.name.name,
              member.tag,
              member.options
            )
          )
        }
      }
    }
    return ProtoSealedDef(comments, name, protoMembers)
  }

  fun traverseTypeNoStream(
    type: SugarProtoAst.Type,
    namingContext: NamingContext,
    localNames: Map<String, ProtoStreamableFieldType>,
  ): ProtoFieldType =
    when (type) {
      is SugarProtoAst.MapType -> {
        val keyType = traverseTypeNoStream(type.keyType, namingContext, localNames)
        val valueType = traverseTypeNoStream(type.valueType, namingContext, localNames)
        check(!keyType.repeated) { "Key of map cannot be repeated" }
        check(!keyType.optional) { "Key of map cannot be optional" }
        check(keyType.kind == FieldKindEnum.PrimitiveKind) { "Key of map must be primitive type" }
        check(!valueType.repeated) { "Value of map cannot be repeated" }
        check(!valueType.optional) { "Value of map cannot be optional" }
        ProtoFieldType(
          FieldKindEnum.MapKind,
          false,
          false,
          TypeExpr.MapType(keyType.type, valueType.type),
        )
      }

      is SugarProtoAst.OnTheFlyMessageType -> {
        if (type.fields.isEmpty() && type.name == null) {
          requiredImports.add("google/protobuf/empty.proto")
          ProtoFieldType(FieldKindEnum.MessageKind, false, false, TypeExpr.EmptyMessage)
        } else {
          val messageName = type.name?.name ?: createMessageName(namingContext)
          // top level에 새로 들어가는 메시지이기 때문에 naming context clear
          // TODO comment
          val protoMessage = convertMessageDef(listOf(), messageName, type.fields, NamingContext())
          defs.add(protoMessage)
          ProtoFieldType(
            FieldKindEnum.MessageKind,
            false,
            false,
            TypeExpr.MessageOrEnumName(messageName)
          )
        }
      }

      is SugarProtoAst.OnTheFlySealedMessageType -> {
        val messageName = type.name?.name ?: createMessageName(namingContext)
        // TODO comment
        val protoMessage = convertSealedDef(listOf(), messageName, type.fields)
        defs.add(protoMessage)
        ProtoFieldType(
          FieldKindEnum.MessageKind,
          false,
          false,
          TypeExpr.MessageOrEnumName(messageName)
        )
      }

      is SugarProtoAst.OnTheFlyEnumType -> {
        val enumName = type.name?.name ?: createMessageName(namingContext + "enum")
        // TODO comment
        val enumMessage = convertEnumDef(listOf(), enumName, type.fields)
        defs.add(enumMessage)
        ProtoFieldType(
          FieldKindEnum.MessageKind,
          false,
          false,
          TypeExpr.MessageOrEnumName(enumName)
        )
      }

      is SugarProtoAst.OptionalType -> {
        val elemType = traverseTypeNoStream(type.elemType, namingContext, localNames)
        check(!elemType.optional) { "Optional of optional type is not supported" }
        check(!elemType.repeated) { "Optional of repeated type is not supported" }
        check(elemType.kind != FieldKindEnum.MapKind) { "Optional of map is not supported" }
        ProtoFieldType(elemType.kind, true, elemType.repeated, elemType.type)
      }

      is SugarProtoAst.PrimitiveType -> {
        ProtoFieldType(FieldKindEnum.PrimitiveKind, false, false, TypeExpr.PrimitiveType(type.typ))
      }

      is SugarProtoAst.RepeatedType -> {
        val elemType = traverseTypeNoStream(type.elemType, namingContext, localNames)
        check(!elemType.optional) { "Repeated of optional type is not supported yet" }
        check(!elemType.repeated) { "Repeated of repeated type is not supported yet" }
        ProtoFieldType(elemType.kind, false, true, elemType.type)
      }

      is SugarProtoAst.MultiName -> {
        val fullName = type.names.joinToString(".") { it.name }
        ProtoFieldType(
          FieldKindEnum.MessageKind,
          false,
          false,
          TypeExpr.MessageOrEnumName(fullName)
        )
      }

      is SugarProtoAst.SingleName -> {
        val localDef = localNames[type.name]
        if (localDef != null) {
          check(!localDef.isStream) { "Stream value should not be present here" }
          localDef.valueType
        } else {
          ProtoFieldType(
            FieldKindEnum.MessageKind,
            false,
            false,
            TypeExpr.MessageOrEnumName(type.name)
          )
        }
      }

      is SugarProtoAst.StreamType -> throw IllegalStateException("Unsupported type: $type")
    }

  fun traverseType(
    type: SugarProtoAst.Type,
    namingContext: NamingContext,
    localNames: Map<String, ProtoStreamableFieldType>,
  ): ProtoStreamableFieldType =
    when (type) {
      is SugarProtoAst.StreamType -> {
        val elemType = traverseTypeNoStream(type.elemType, namingContext, localNames)
        ProtoStreamableFieldType(true, elemType)
      }

      is SugarProtoAst.SingleName -> localNames[type.name] ?: ProtoStreamableFieldType(
        false, traverseTypeNoStream(type, namingContext, localNames)
      )

      else -> ProtoStreamableFieldType(false, traverseTypeNoStream(type, namingContext, localNames))
    }

  fun traverseServiceDef(
    comments: List<SugarProtoAst.Comment>,
    serviceDef: SugarProtoAst.ServiceDef
  ) {
    val rpcs = serviceDef.members.map { memberWS ->
      val memberComments = memberWS.comments.filterNotNull()
      when (val member = memberWS.member) {
        is SugarProtoAst.OptionDef -> {
          ServiceMember.ServiceOption(memberComments, member)
        }

        is SugarProtoAst.RpcDef -> {
          val rpc = member
          val namingContext = NamingContext(serviceDef.name, rpc.name)
          val localNames = mutableMapOf<String, ProtoStreamableFieldType>()
          rpc.wheres?.let { wheres ->
            wheres.wheres.forEach { where ->
              val evaluated = traverseType(where.typ, namingContext + where.name, localNames)
              localNames[where.name.name] = evaluated
            }
          }
          val (isInTypeStream, inType) = traverseType(
            rpc.inType,
            namingContext + "req",
            localNames
          )
          val (isOutTypeStream, outType) = traverseType(
            rpc.outType,
            namingContext + "res",
            localNames
          )

          check(inType.kind != FieldKindEnum.MapKind) { "Request type of RPC ${serviceDef.name} cannot be a map" }
          check(!inType.optional) { "Request type of RPC ${serviceDef.name} cannot be optional" }
          check(!inType.repeated) { "Request type of RPC ${serviceDef.name} cannot be repeated" }
          check(outType.kind != FieldKindEnum.MapKind) { "Response type of RPC ${serviceDef.name} cannot be a map" }
          check(!outType.optional) { "Response type of RPC ${serviceDef.name} cannot be optional" }
          check(!outType.repeated) { "Response type of RPC ${serviceDef.name} cannot be repeated" }
          ServiceMember.ProtoRpcDef(
            memberComments,
            rpc.name.name,
            isInTypeStream,
            inType.type,
            isOutTypeStream,
            outType.type,
            rpc.options,
          )
        }
      }
    }
    defs.add(ProtoServiceDef(comments, serviceDef.name.name, rpcs))
  }

  fun traverseMessageDef(
    comments: List<SugarProtoAst.Comment>,
    messageDef: SugarProtoAst.MessageDef
  ) {
    defs.add(convertMessageDef(comments, messageDef.name.name, messageDef.members, NamingContext()))
  }

  fun traverseSealedDef(comments: List<SugarProtoAst.Comment>, sealedDef: SugarProtoAst.SealedDef) {
    val messageDef = convertSealedDef(comments, sealedDef.name.name, sealedDef.members)
    defs.add(messageDef)
  }

  fun traverseEnumDef(comments: List<SugarProtoAst.Comment>, enumDef: SugarProtoAst.EnumDef) {
    val protoEnumDef = convertEnumDef(comments, enumDef.name.name, enumDef.members)
    defs.add(protoEnumDef)
  }
}
