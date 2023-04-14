package com.giyeok.sugarproto

class ProtoDefGenerator(val ast: SugarProtoAst.CompilationUnit) {
  val packageName = ast.pkgDef?.names?.joinToString(".")

  private val requiredImports = mutableSetOf<String>()

  private val defs = mutableListOf<ProtoDef>()

  fun traverse() {
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
  ): ProtoMessageDef {
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
    return ProtoMessageDef(
      comments,
      name,
      listOf(ProtoMessageMember.ProtoOneOf(listOf(), name, protoMembers))
    )
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
          "map<${keyType.type}, ${valueType.type}>",
        )
      }

      is SugarProtoAst.OnTheFlyMessageType -> {
        if (type.fields.isEmpty() && type.name == null) {
          requiredImports.add("google/protobuf/empty.proto")
          ProtoFieldType(FieldKindEnum.MessageKind, false, false, "google.protobuf.Empty")
        } else {
          val messageName = type.name?.name ?: createMessageName(namingContext)
          // top level에 새로 들어가는 메시지이기 때문에 naming context clear
          // TODO comment
          val protoMessage = convertMessageDef(listOf(), messageName, type.fields, NamingContext())
          defs.add(protoMessage)
          ProtoFieldType(FieldKindEnum.MessageKind, false, false, messageName)
        }
      }

      is SugarProtoAst.OnTheFlySealedMessageType -> {
        val messageName = type.name?.name ?: createMessageName(namingContext)
        // TODO comment
        val protoMessage = convertSealedDef(listOf(), messageName, type.fields)
        defs.add(protoMessage)
        ProtoFieldType(FieldKindEnum.MessageKind, false, false, messageName)
      }

      is SugarProtoAst.OnTheFlyEnumType -> {
        val enumName = type.name?.name ?: createMessageName(namingContext + "enum")
        // TODO comment
        val enumMessage = convertEnumDef(listOf(), enumName, type.fields)
        defs.add(enumMessage)
        ProtoFieldType(FieldKindEnum.MessageKind, false, false, enumName)
      }

      is SugarProtoAst.OptionalType -> {
        val elemType = traverseTypeNoStream(type.elemType, namingContext, localNames)
        check(!elemType.optional) { "Optional of optional type is not supported" }
        check(!elemType.repeated) { "Optional of repeated type is not supported" }
        check(elemType.kind != FieldKindEnum.MapKind) { "Optional of map is not supported" }
        ProtoFieldType(elemType.kind, true, elemType.repeated, elemType.type)
      }

      is SugarProtoAst.PrimitiveType -> {
        val typeName = when (type.typ) {
          SugarProtoAst.PrimitiveTypeEnum.BOOL -> "bool"
          SugarProtoAst.PrimitiveTypeEnum.BYTES -> "bytes"
          SugarProtoAst.PrimitiveTypeEnum.DOUBLE -> "double"
          SugarProtoAst.PrimitiveTypeEnum.FIXED32 -> "fixed32"
          SugarProtoAst.PrimitiveTypeEnum.FIXED64 -> "fixed64"
          SugarProtoAst.PrimitiveTypeEnum.FLOAT -> "float"
          SugarProtoAst.PrimitiveTypeEnum.INT32 -> "int32"
          SugarProtoAst.PrimitiveTypeEnum.INT64 -> "int64"
          SugarProtoAst.PrimitiveTypeEnum.SFIXED32 -> "sfixed32"
          SugarProtoAst.PrimitiveTypeEnum.SFIXED64 -> "sfixed64"
          SugarProtoAst.PrimitiveTypeEnum.SINT32 -> "sint32"
          SugarProtoAst.PrimitiveTypeEnum.SINT64 -> "sint64"
          SugarProtoAst.PrimitiveTypeEnum.STRING -> "string"
          SugarProtoAst.PrimitiveTypeEnum.UINT32 -> "uint32"
          SugarProtoAst.PrimitiveTypeEnum.UINT64 -> "uint64"
        }
        ProtoFieldType(FieldKindEnum.PrimitiveKind, false, false, typeName)
      }

      is SugarProtoAst.RepeatedType -> {
        val elemType = traverseTypeNoStream(type.elemType, namingContext, localNames)
        check(!elemType.optional) { "Repeated of optional type is not supported yet" }
        check(!elemType.repeated) { "Repeated of repeated type is not supported yet" }
        ProtoFieldType(elemType.kind, false, true, elemType.type)
      }

      is SugarProtoAst.MultiName -> {
        val fullName = type.names.joinToString(".") { it.name }
        ProtoFieldType(FieldKindEnum.MessageKind, false, false, fullName)
      }

      is SugarProtoAst.SingleName -> {
        val localDef = localNames[type.name]
        if (localDef != null) {
          check(!localDef.isStream) { "Stream value should not be present here" }
          localDef.valueType
        } else {
          ProtoFieldType(FieldKindEnum.MessageKind, false, false, type.name)
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
    val messageDef = convertSealedDef(comments, sealedDef.name.name, sealedDef.members ?: listOf())
    defs.add(messageDef)
  }

  fun traverseEnumDef(comments: List<SugarProtoAst.Comment>, enumDef: SugarProtoAst.EnumDef) {
    val protoEnumDef = convertEnumDef(comments, enumDef.name.name, enumDef.members)
    defs.add(protoEnumDef)
  }

  fun SugarProtoAst.StringLiteral.toValue(): String {
    val builder = StringBuilder()
    this.singles.forEach { single ->
      single.value.forEach { charValue ->
        when (charValue) {
          is SugarProtoAst.PlainChar -> builder.append(charValue.value)
          is SugarProtoAst.CharEscape -> TODO()
          is SugarProtoAst.HexEscape -> TODO()
          is SugarProtoAst.OctEscape -> TODO()
          is SugarProtoAst.UnicodeEscape -> TODO()
          is SugarProtoAst.UnicodeLongEscape -> TODO()
        }
      }
    }
    return builder.toString()
  }

  fun SugarProtoAst.FieldOption.toProtoString(): String =
    "${this.name.toProtoString()} = ${this.value.toProtoString()}"

  fun SugarProtoAst.OptionName.toProtoString(): String {
    val baseName = if (this.name.names.size == 1) {
      this.name.names.first().name
    } else {
      this.name.names.joinToString(".") { it.name }
    }
    return if (this.trailings.isNotEmpty()) {
      "$baseName." + this.trailings.joinToString(".") { it.name }
    } else {
      baseName
    }
  }

  fun SugarProtoAst.Constant.toProtoString(): String = when (this) {
    is SugarProtoAst.FloatLiteral -> {
      val expPart = this.exp?.let { exp -> "e${exp.sign ?: ""}${exp.exp}" } ?: ""
      (this.intPart ?: "") + ".${this.fracPart ?: ""}" + expPart
    }

    is SugarProtoAst.Inf -> "inf"
    is SugarProtoAst.Nan -> "nan"
    is SugarProtoAst.BoolLiteral -> when (this.value) {
      SugarProtoAst.BoolValueEnum.TRUE -> "true"
      SugarProtoAst.BoolValueEnum.FALSE -> "false"
    }

    is SugarProtoAst.FullIdent -> TODO()
    is SugarProtoAst.DecimalLiteral -> TODO()
    is SugarProtoAst.HexLiteral -> TODO()
    is SugarProtoAst.OctalLiteral -> TODO()
    is SugarProtoAst.ZeroIntLiteral -> TODO()
    is SugarProtoAst.StringLiteral ->
      // TODO escape
      "\"${this.toValue()}\""
  }

  fun generateFieldDef(
    builder: StringBuilder,
    field: ProtoMessageMember.ProtoFieldDef,
    indent: String
  ) {
    generateComments(builder, field.comments, "$indent  ")
    builder.append(indent)
    builder.append("  ")
    if (field.type.optional) {
      builder.append("optional ")
    }
    if (field.type.repeated) {
      builder.append("repeated ")
    }
    builder.append(field.type.type)
    builder.append(" ")
    builder.append(field.name)
    builder.append(" = ")
    builder.append(field.tag.toValueString())
    field.options?.let { options ->
      generateOptions(builder, options, indent)
    }
    builder.append(";\n")
  }

  fun generateOptions(builder: StringBuilder, options: SugarProtoAst.FieldOptions, indent: String) {
    if (options.options.isEmpty()) {
      // 이런 경우가 있나 근데?
      builder.append(" []")
    } else if (options.options.size == 1) {
      builder.append(" [")
      builder.append(options.options.first().toProtoString())
      builder.append("]")
    } else {
      builder.append(" [\n")
      options.options.forEach { option ->
        builder.append("$indent    ${option.toProtoString()},\n")
      }
      builder.append("$indent  ]")
    }
  }

  fun SugarProtoAst.IntLiteral.toValueString(): String = when (this) {
    is SugarProtoAst.ZeroIntLiteral -> "0"
    is SugarProtoAst.DecimalLiteral -> this.value
    is SugarProtoAst.HexLiteral -> "0x${this.value}"
    is SugarProtoAst.OctalLiteral -> "0${this.value}"
  }

  fun generateComments(
    builder: StringBuilder,
    comments: List<SugarProtoAst.Comment>,
    indent: String
  ) {
    comments.forEach { comment ->
      when (comment) {
        is SugarProtoAst.LineComment -> {
          builder.append(indent)
          builder.append("// ")
          builder.append(comment.content.trim())
          builder.append("\n")
        }

        is SugarProtoAst.BlockComment -> TODO()
      }
    }
  }

  fun generateOption(
    builder: java.lang.StringBuilder,
    option: SugarProtoAst.OptionDef,
    indent: String
  ) {
    builder.append(indent)
    builder.append("option ")
    builder.append(option.name.toProtoString())
    builder.append(" = ")
    builder.append(option.value.toProtoString())
    builder.append(";\n")
  }

  fun generateMessageDef(builder: StringBuilder, messageDef: ProtoMessageDef, indent: String) {
    generateComments(builder, messageDef.comments, indent)
    builder.append("${indent}message ${messageDef.name} {\n")
    messageDef.members.forEach { member ->
      when (member) {
        is ProtoMessageMember.ProtoFieldDef -> generateFieldDef(builder, member, indent)

        is ProtoMessageMember.ProtoNestedMessageDef ->
          generateMessageDef(builder, member.message, "$indent  ")

        is ProtoMessageMember.ProtoNestedEnumDef ->
          generateEnumDef(builder, member.enum, "$indent  ")

        is ProtoMessageMember.ProtoOneOf -> {
          generateComments(builder, member.comments, "$indent  ")
          builder.append(indent)
          builder.append("  oneof ${member.name} {\n")
          member.members.forEach { oneofMember ->
            when (oneofMember) {
              is ProtoMessageMember.ProtoOneOfMember.OneOfField ->
                generateFieldDef(builder, oneofMember.field, "$indent  ")

              is ProtoMessageMember.ProtoOneOfMember.OneOfOption -> {
                generateComments(builder, oneofMember.comments, "$indent  ")
                generateOption(builder, oneofMember.option, "$indent    ")
              }
            }
          }
          builder.append("$indent  }\n")
        }

        is ProtoMessageMember.ProtoMessageOptionDef -> {
          generateComments(builder, member.comments, indent)
          builder.append(indent)
          generateOption(builder, member.optionDef, "$indent  ")
        }

        is ProtoMessageMember.ProtoReservedDef -> {
          generateComments(builder, member.comments, indent)
          builder.append(indent)
          builder.append("  reserved ")
          val reserves = member.reserved.map { reserve ->
            when (reserve) {
              is SugarProtoAst.Ident ->
                // TODO name escape
                "\"${reserve.name}\""

              is SugarProtoAst.ReservedRange -> {
                val trailing = reserve.reservedEnd?.let {
                  when (it) {
                    is SugarProtoAst.Max -> " to max"
                    is SugarProtoAst.IntLiteral -> " to ${it.toValueString()}"
                  }
                } ?: ""

                reserve.reservedStart.toValueString() + trailing
              }
            }
          }
          builder.append(reserves.joinToString(", "))
          builder.append(";\n")
        }
      }
    }
    builder.append("$indent}\n")
  }

  fun generateEnumDef(builder: StringBuilder, enumDef: ProtoEnumDef, indent: String) {
    generateComments(builder, enumDef.comments, indent)
    builder.append("${indent}enum ${enumDef.name} {\n")
    enumDef.members.forEach { member ->
      when (member) {
        is ProtoEnumMember.EnumOption -> {
          generateComments(builder, member.comments, indent)
          generateOption(builder, member.optionDef, "$indent  ")
        }

        is ProtoEnumMember.EnumValueDef -> {
          generateComments(builder, member.comments, "$indent  ")
          builder.append(indent)
          builder.append("  ")
          builder.append(member.name)
          builder.append(" = ")
          if (member.minusTag) {
            builder.append("-")
          }
          builder.append(member.tag.toValueString())
          member.options?.let { options ->
            generateOptions(builder, options, indent)
          }
          builder.append(";\n")
        }
      }
    }
    builder.append("$indent}\n")
  }

  fun generate(): String {
    val builder = StringBuilder()

    builder.append("syntax = \"proto3\";\n\n")

    if (ast.pkgDef != null) {
      builder.append("package ${ast.pkgDef!!.names.joinToString(".") { it.name }};\n\n")
    }

    val imports = requiredImports + ast.imports.map { it.toValue() }
    imports.forEach { import ->
      // TODO escape
      builder.append("import \"$import\";\n")
    }
    if (imports.isNotEmpty()) {
      builder.append("\n")
    }

    ast.options.forEach { option ->
      generateOption(builder, option, "")
    }
    if (ast.options.isNotEmpty()) {
      builder.append("\n")
    }

    defs.forEach { def ->
      when (def) {
        is ProtoMessageDef -> {
          generateMessageDef(builder, def, "")
        }

        is ProtoServiceDef -> {
          generateComments(builder, def.comments, "")
          builder.append("service ${def.name} {\n")
          def.members.forEach { member ->
            when (member) {
              is ServiceMember.ServiceOption -> {
                generateComments(builder, member.comments, "  ")
                generateOption(builder, member.optionDef, "  ")
              }

              is ServiceMember.ProtoRpcDef -> {
                generateComments(builder, member.comments, "  ")
                val rpc = member
                val inType = if (rpc.isInTypeStream) "stream ${rpc.inType}" else rpc.inType
                val outType = if (rpc.isOutTypeStream) "stream ${rpc.outType}" else rpc.outType
                builder.append("  rpc ${rpc.name.replaceFirstChar { it.uppercase() }}($inType) returns ($outType);\n")
              }
            }
          }
          builder.append("}\n")
        }

        is ProtoEnumDef -> {
          generateEnumDef(builder, def, "")
        }
      }
      builder.append("\n")
    }
    return builder.toString()
  }
}
