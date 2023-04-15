package com.giyeok.sugarproto

class ProtoDefGenerator(
  val packageName: String?,
  val imports: Set<String>,
  val options: List<SugarProtoAst.OptionDef>,
  val defs: List<ProtoDef>,
) {
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
    builder.append(field.type.type.toProtoTypeString())
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
          generateComments(builder, member.comments, "$indent  ")
          generateOption(builder, member.optionDef, "$indent  ")
        }

        is ProtoMessageMember.ProtoReservedDef -> {
          generateComments(builder, member.comments, "$indent  ")
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

    if (packageName != null) {
      builder.append("package $packageName;\n\n")
    }

    imports.forEach { import ->
      // TODO escape
      builder.append("import \"$import\";\n")
    }
    if (imports.isNotEmpty()) {
      builder.append("\n")
    }

    options.forEach { option ->
      generateOption(builder, option, "")
    }
    if (options.isNotEmpty()) {
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
                val inType =
                  if (rpc.isInTypeStream) "stream ${rpc.inType.toProtoTypeString()}" else rpc.inType.toProtoTypeString()
                val outType =
                  if (rpc.isOutTypeStream) "stream ${rpc.outType.toProtoTypeString()}" else rpc.outType.toProtoTypeString()
                builder.append("  rpc ${rpc.name.replaceFirstChar { it.uppercase() }}($inType) returns ($outType);\n")
              }
            }
          }
          builder.append("}\n")
        }

        is ProtoSealedDef -> {
          generateMessageDef(
            builder, ProtoMessageDef(
              def.comments,
              def.name,
              listOf(ProtoMessageMember.ProtoOneOf(listOf(), def.name, def.sealedMembers))
            ),
            ""
          )
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
