package com.giyeok.sugarproto.proto

import com.giyeok.sugarproto.NamingContext
import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.name.SemanticName
import com.giyeok.sugarproto.toValue

// AST -> ProtoMessageDef
// on the fly message/enum들은 모두 top level에 정의한다
// 특별히 nested로 명시한 경우에만 nested message/enum으로 변환
class ProtoDefTraverser(val ast: SugarProtoAst.CompilationUnit) {
  private val packageName = ast.pkgDef?.names?.joinToString(".") { it.name }

  private var emptyRequired = false

  private val defs = mutableListOf<ProtoDef>()

  private val sealedSupers = mutableMapOf<SemanticName, SuperName>()

  private val nameLookup = mutableMapOf<String, AtomicType>()

  private val kotlinImports = mutableSetOf<String>()
  private var kotlinPackageName: String? = null

  init {
    fun addEnumName(name: String) {
      nameLookup[name] =
        AtomicType.EnumType(SemanticName.enumName(name), AtomicType.TypeSource.UserDefined)
    }

    fun addMessageName(name: String) {
      nameLookup[name] =
        AtomicType.MessageType(SemanticName.messageName(name), AtomicType.TypeSource.UserDefined)
    }

    fun addSealedName(name: String) {
      nameLookup[name] =
        AtomicType.SealedType(SemanticName.messageName(name), AtomicType.TypeSource.UserDefined)
    }

    fun traverseMessageDef(members: List<SugarProtoAst.MessageMemberDefWS>, name: String) {
      members.forEach { memberWS ->
        when (val member = memberWS.def) {
          is SugarProtoAst.EnumDef -> addEnumName("$name.${member.name.name}")
          is SugarProtoAst.FieldDef -> {}
          is SugarProtoAst.MessageDef -> {
            addMessageName("$name.${member.name.name}")
            traverseMessageDef(member.members, "$name.${member.name.name}")
          }

          is SugarProtoAst.OneOfDef -> {}
          is SugarProtoAst.OptionDef -> {}
          is SugarProtoAst.ReservedDef -> {}
        }
      }
    }

    ast.kotlinOptions?.let { kotlinOptions ->
      kotlinOptions.options.forEach { option ->
        when (option) {
          is SugarProtoAst.KotlinPackage -> {
            if (kotlinPackageName != null) {
              throw IllegalStateException("duplicate definition of kotlin package name")
            }
            kotlinPackageName = option.name.names.joinToString(".") { it.name }
          }

          is SugarProtoAst.KotlinFromOtherPackage -> {
            val protoPkg = option.protoPkg.names.map { it.name }
            val kotlinPkg = option.kotlinPkg?.names?.map { it.name } ?: protoPkg
            option.uses.forEach { use ->
              val useNames = use.name.names.map { it.name }
              check(useNames.size == 1)
              kotlinImports.add((kotlinPkg + useNames).joinToString("."))
              when (use.kind) {
                SugarProtoAst.TypeKind.MESSAGE ->
                  nameLookup[useNames.joinToString(".")] = AtomicType.MessageType(
                    SemanticName.messageName(useNames.last()),
                    AtomicType.TypeSource.External(protoPkg)
                  )

                SugarProtoAst.TypeKind.SEALED ->
                  nameLookup[useNames.joinToString(".")] = AtomicType.MessageType(
                    SemanticName.messageName(useNames.last()),
                    AtomicType.TypeSource.External(protoPkg)
                  )

                SugarProtoAst.TypeKind.ENUM -> nameLookup[useNames.joinToString(".")] =
                  AtomicType.MessageType(
                    SemanticName.messageName(useNames.last()),
                    AtomicType.TypeSource.External(protoPkg)
                  )
              }
            }
          }
        }
      }
    }

    ast.defs.forEach { defWS ->
      when (val def = defWS.def) {
        is SugarProtoAst.EnumDef ->
          addEnumName(def.name.name)

        is SugarProtoAst.MessageDef -> {
          addMessageName(def.name.name)
          traverseMessageDef(def.members, def.name.name)
        }

        is SugarProtoAst.SealedDef ->
          addSealedName(def.name.name)

        is SugarProtoAst.ServiceDef -> {
          // do nothing
        }
      }
    }
  }

  fun lookupName(name: String): AtomicType =
    nameLookup[name] ?: AtomicType.UnknownName(name)

  fun traverseEnum(
    comments: List<SugarProtoAst.Comment>,
    def: SugarProtoAst.EnumDef
  ): ProtoEnumDef {
    return traverseEnum(
      comments,
      SemanticName.enumName(def.name),
      def.members
    )
  }

  fun traverseEnum(
    comments: List<SugarProtoAst.Comment>,
    name: SemanticName,
    members: List<SugarProtoAst.EnumMemberDefWS>,
  ): ProtoEnumDef {
    return ProtoEnumDef(
      comments,
      name,
      members.map { traverseEnumMember(it) },
      listOf()
    )
  }

  fun traverseEnumMember(ast: SugarProtoAst.EnumMemberDefWS): ProtoEnumMember {
    val comments = ast.comments.filterNotNull()
    return when (val def = ast.def) {
      is SugarProtoAst.EnumFieldDef ->
        ProtoEnumMember.EnumField(
          comments,
          SemanticName.enumMember(def.name),
          def.minusTag,
          def.tag,
          def.options
        )

      is SugarProtoAst.OptionDef ->
        ProtoEnumMember.EnumOption(OptionDef(comments, def))
    }
  }

  fun traverseMessage(
    comments: List<SugarProtoAst.Comment>,
    name: SemanticName,
    members: List<SugarProtoAst.MessageMemberDefWS>,
    namingContext: NamingContext,
    defs: MutableList<ProtoDef>,
  ): ProtoMessageDef {
    return ProtoMessageDef(
      comments,
      name,
      members.map {
        traverseMessageMember(it.comments.filterNotNull(), it.def, namingContext + name, defs)
      },
      listOf()
    )
  }

  fun traverseMessage(
    comments: List<SugarProtoAst.Comment>,
    ast: SugarProtoAst.MessageDef,
    namingContext: NamingContext,
    defs: MutableList<ProtoDef>,
  ): ProtoMessageDef {
    return traverseMessage(
      comments,
      SemanticName.messageName(ast.name),
      ast.members,
      namingContext,
      defs
    )
  }

  fun traverseOneOfMember(
    ast: SugarProtoAst.OneOfMembersDefWS,
    namingContext: NamingContext,
    defs: MutableList<ProtoDef>
  ): ProtoOneOfMember {
    val comments = ast.comments.filterNotNull()
    return when (val def = ast.def) {
      is SugarProtoAst.OptionDef -> ProtoOneOfMember.OneOfOption(OptionDef(comments, def))
      is SugarProtoAst.FieldDef ->
        ProtoOneOfMember.OneOfField(traverseMessageField(comments, def, namingContext, defs))
    }
  }

  fun traverseType(
    type: SugarProtoAst.Type,
    namingContext: NamingContext,
    localNames: Map<String, ProtoType>,
    defs: MutableList<ProtoDef>
  ): ProtoType {
    return when (type) {
      is SugarProtoAst.MapType -> {
        val keyType = traverseType(type.keyType, namingContext, localNames, defs)
        val valueType = traverseType(type.valueType, namingContext, localNames, defs)
        if (keyType !is AtomicType.PrimitiveType) {
          throw IllegalStateException("$type is not valid")
        } else if (valueType !is AtomicType) {
          throw IllegalStateException("$type is not valid")
        } else {
          ValueType.MapType(keyType, valueType)
        }
      }

      is SugarProtoAst.OnTheFlyEnumType -> {
        val enumName = type.name?.name?.let { SemanticName.enumName(it) }
          ?: namingContext.enumName()
        val generatedEnum = traverseEnum(listOf(), enumName, type.fields)
        defs.add(generatedEnum)
        AtomicType.EnumType(generatedEnum.name, AtomicType.TypeSource.Generated)
      }

      is SugarProtoAst.OnTheFlyMessageType -> {
        if (type.name == null && type.fields.isEmpty()) {
          emptyRequired = true
          AtomicType.EmptyType
        } else {
          val messageName = type.name?.name?.let { SemanticName.messageName(it) }
            ?: namingContext.messageName()
          val subDefs = mutableListOf<ProtoDef>()
          val generatedMessage =
            traverseMessage(listOf(), messageName, type.fields, NamingContext(messageName), subDefs)
          defs.add(generatedMessage)
          defs.addAll(subDefs)
          AtomicType.MessageType(generatedMessage.name, AtomicType.TypeSource.Generated)
        }
      }

      is SugarProtoAst.OnTheFlySealedMessageType -> {
        val sealedName = type.name?.name?.let { SemanticName.messageName(it) }
          ?: namingContext.messageName()
        val newNamingContext = if (type.name != null) NamingContext() else namingContext
        val subDefs = mutableListOf<ProtoDef>()
        val generatedSealed =
          traverseSealed(listOf(), sealedName, type.fields, newNamingContext, subDefs)
        defs.add(generatedSealed)
        defs.addAll(subDefs)
        AtomicType.SealedType(generatedSealed.name, AtomicType.TypeSource.Generated)
      }

      is SugarProtoAst.OptionalType -> {
        val elemType = traverseType(type.elemType, namingContext, localNames, defs)
        if (elemType !is AtomicType) {
          throw IllegalStateException("$type is not valid")
        } else {
          ValueType.OptionalType(elemType)
        }
      }

      is SugarProtoAst.PrimitiveType ->
        AtomicType.PrimitiveType(type.typ)

      is SugarProtoAst.RepeatedType -> {
        val elemType = traverseType(type.elemType, namingContext, localNames, defs)
        if (elemType !is AtomicType) {
          throw IllegalStateException("$type is not valid")
        } else {
          ValueType.RepeatedType(elemType)
        }
      }

      is SugarProtoAst.SetType -> {
        val elemType = traverseType(type.elemType, namingContext, localNames, defs)
        if (elemType !is AtomicType) {
          throw IllegalStateException("$type is not valid")
        } else {
          ValueType.SetType(elemType)
        }
      }

      is SugarProtoAst.StreamType -> {
        val valueType = traverseType(type.elemType, namingContext, localNames, defs)
        if (valueType !is ValueType) {
          throw IllegalStateException("$type is not valid")
        } else {
          ProtoType.StreamType(valueType)
        }
      }

      is SugarProtoAst.MultiName ->
        lookupName(type.names.joinToString(".") { it.name })

      is SugarProtoAst.SingleName -> {
        localNames[type.name] ?: lookupName(type.name)
      }
    }
  }

  fun traverseMessageField(
    comments: List<SugarProtoAst.Comment>,
    def: SugarProtoAst.FieldDef,
    namingContext: NamingContext,
    defs: MutableList<ProtoDef>
  ): ProtoMessageMember.MessageField {
    val fieldName = SemanticName.messageMember(def.name)
    val fieldType = traverseType(def.typ, namingContext + fieldName, mapOf(), defs)

    check(fieldType !is ProtoType.StreamType)

    return ProtoMessageMember.MessageField(
      comments,
      fieldName,
      fieldType as ValueType,
      def.tag,
      def.options
    )
  }

  fun traverseMessageMember(
    comments: List<SugarProtoAst.Comment>,
    def: SugarProtoAst.MessageMemberDef,
    namingContext: NamingContext,
    defs: MutableList<ProtoDef>
  ): ProtoMessageMember {
    return when (def) {
      is SugarProtoAst.EnumDef ->
        ProtoMessageMember.NestedEnum(traverseEnum(comments, def))

      is SugarProtoAst.FieldDef ->
        traverseMessageField(comments, def, namingContext, defs)

      is SugarProtoAst.MessageDef ->
        ProtoMessageMember.NestedMessage(traverseMessage(comments, def, namingContext, defs))

      is SugarProtoAst.OneOfDef ->
        ProtoMessageMember.OneOf(
          comments,
          SemanticName.messageMember(def.name),
          def.members.map { traverseOneOfMember(it, namingContext, defs) })

      is SugarProtoAst.OptionDef ->
        ProtoMessageMember.MessageOption(OptionDef(comments, def))

      is SugarProtoAst.ReservedDef ->
        ProtoMessageMember.Reserved(comments, def.ranges)
    }
  }

  fun traverseSealed(
    comments: List<SugarProtoAst.Comment>,
    def: SugarProtoAst.SealedDef,
    namingContext: NamingContext,
    defs: MutableList<ProtoDef>
  ): ProtoSealedDef {
    return traverseSealed(
      comments,
      SemanticName.messageName(def.name),
      def.members,
      namingContext,
      defs
    )
  }

  fun traverseSealed(
    comments: List<SugarProtoAst.Comment>,
    name: SemanticName,
    members: List<SugarProtoAst.SealedMemberDefWS>,
    namingContext: NamingContext,
    defs: MutableList<ProtoDef>
  ): ProtoSealedDef {
    val subDefs = mutableListOf<ProtoDef>()

    val commonFields: List<ProtoMessageMember.MessageField> =
      members.mapNotNull { memberWS ->
        when (val member = memberWS.def) {
          is SugarProtoAst.CommonFieldDef -> {
            traverseMessageField(
              memberWS.comments.filterNotNull(),
              member.field,
              namingContext + name,
              subDefs
            )
          }

          is SugarProtoAst.FieldDef -> null
        }
      }

    val sealedFields: List<ProtoMessageMember.MessageField> =
      members.mapNotNull { memberWS ->
        when (val member = memberWS.def) {
          is SugarProtoAst.CommonFieldDef -> null
          is SugarProtoAst.FieldDef -> {
            val field =
              traverseMessageField(
                memberWS.comments.filterNotNull(),
                member,
                namingContext + name,
                subDefs
              )
            if (field.type is AtomicType.MessageType && field.type.source == AtomicType.TypeSource.Generated) {
              sealedSupers[field.type.name] = SuperName(name, field.name)
            }
            field
          }
        }
      }

    defs.addAll(subDefs)
    return ProtoSealedDef(
      comments,
      name,
      commonFields,
      sealedFields,
      listOf(),
    )
  }

  fun traverseService(
    comments: List<SugarProtoAst.Comment>,
    def: SugarProtoAst.ServiceDef,
    namingContext: NamingContext,
    defs: MutableList<ProtoDef>
  ): ProtoServiceDef {
    val name = SemanticName.serviceName(def.name)
    val members = def.members.map { memberWS ->
      val memberComments = memberWS.comments.filterNotNull()
      when (val member = memberWS.member) {
        is SugarProtoAst.OptionDef ->
          ProtoServiceMember.ServiceOption(OptionDef(memberComments, member))

        is SugarProtoAst.RpcDef -> {
          val rpcName = SemanticName.rpcName(member.name)
          val fieldNamingContext = namingContext + name + rpcName
          val localNames = member.wheres?.let { wheres ->
            wheres.wheres.associate { where ->
              where.name.name to traverseType(where.typ, fieldNamingContext, mapOf(), defs)
            }
          } ?: mapOf()
          val inType = traverseType(member.inType, fieldNamingContext + "req", localNames, defs)
          val outType = traverseType(member.outType, fieldNamingContext + "res", localNames, defs)
          ProtoServiceMember.ServiceRpc(
            memberComments,
            rpcName,
            inType,
            outType,
            member.options
          )
        }
      }
    }
    return ProtoServiceDef(comments, name, members, listOf())
  }

  fun traverse(): ProtoDefs {
    ast.defs.forEach { defWS ->
      val comments = defWS.comments.filterNotNull()

      when (val def = defWS.def) {
        is SugarProtoAst.EnumDef ->
          defs.add(traverseEnum(comments, def))

        is SugarProtoAst.MessageDef -> {
          val subDefs = mutableListOf<ProtoDef>()
          // TODO naming context
          defs.add(traverseMessage(comments, def, NamingContext(), subDefs))
          defs.addAll(subDefs)
        }

        is SugarProtoAst.SealedDef -> {
          val subDefs = mutableListOf<ProtoDef>()
          defs.add(traverseSealed(comments, def, NamingContext(), subDefs))
          defs.addAll(subDefs)
        }

        is SugarProtoAst.ServiceDef -> {
          val subDefs = mutableListOf<ProtoDef>()
          defs.add(traverseService(comments, def, NamingContext(), subDefs))
          defs.addAll(subDefs)
        }
      }
    }
    return ProtoDefs(
      listOf(),
      packageName,
      ast.imports.map { ImportDef(listOf(), it.toValue()) },
      emptyRequired,
      ast.options.map { OptionDef(listOf(), it) },
      defs,
      sealedSupers,
      listOf(),
      kotlinPackageName,
      kotlinImports
    )
  }
}
