package com.giyeok.sugarproto.proto

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.toValue
import com.giyeok.sugarproto.toValueString

// Proto Defs -> Proto schema text
class ProtoGen(val builder: StringBuilder = StringBuilder()) {
  var indentLevel = 0

  fun append(s: String) {
    builder.append(s)
  }

  fun addLine(s: String) {
    addIndent()
    builder.append(s)
    builder.append("\n")
  }

  fun addLine() {
    builder.append("\n")
  }

  fun addIndent() {
    repeat(indentLevel) {
      append("  ")
    }
  }

  fun indentString(): String =
    (0 until indentLevel).map { "  " }.joinToString("")

  fun indent(body: () -> Unit) {
    indentLevel += 1
    body()
    indentLevel -= 1
  }

  fun addComments(comments: List<SugarProtoAst.Comment>) {
    comments.forEach { comment ->
      when (comment) {
        is SugarProtoAst.LineComment -> {
          addLine("//${comment.content}")
        }

        is SugarProtoAst.BlockComment -> TODO()
      }
    }
  }

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

  fun SugarProtoAst.FieldOption.toProtoString(): String =
    "${this.name.toProtoString()} = ${this.value.toProtoString()}"

  fun addOption(option: OptionDef) {
    addComments(option.comments)
    addLine("option ${option.optionDef.name.toProtoString()} = ${option.optionDef.value.toProtoString()};")
  }

  fun protoString(value: String): String =
    // TODO escape
    "\"$value\""

  fun generateFieldOptions(options: SugarProtoAst.FieldOptions?): String =
    if (options == null) "" else {
      if (options.options.isEmpty()) {
        // 이런 경우가 있나 근데?
        " []"
      } else if (options.options.size == 1) {
        " [${options.options.first().toProtoString()}]"
      } else {
        val indent = indentString()
        " [\n" + options.options.joinToString(",\n") { option ->
          "$indent  ${option.toProtoString()}"
        } + "]"
      }
    }

  fun addEnumDef(def: ProtoEnumDef) {
    addComments(def.comments)
    addLine("enum ${def.name.enumName} {")
    indent {
      def.members.forEach { member ->
        when (member) {
          is ProtoEnumMember.EnumOption -> addOption(member.option)
          is ProtoEnumMember.EnumField -> {
            addComments(member.comments)
            val tag =
              if (member.minusTag) "-${member.tag.toValueString()}" else member.tag.toValueString()
            val optionStr = generateFieldOptions(member.options)
            addLine("${member.name.enumFieldName} = $tag$optionStr;")
          }
        }
      }
      addComments(def.trailingComments)
    }
    addLine("}")
  }

  fun ProtoType.toProtoString(): String = when (this) {
    is ProtoType.StreamType -> "stream ${valueType.toProtoString()}"
    is ValueType -> this.toProtoString()
  }

  fun ValueType.toProtoString(): String = when (this) {
    is AtomicType.UnknownName -> this.name
    is AtomicType.MessageOrSealedType -> {
      when (val source = this.source) {
        is AtomicType.TypeSource.External ->
          (source.protoPkg + this.name.messageName).joinToString(".")

        else -> this.name.messageName
      }
    }

    is AtomicType.EnumType -> this.name.enumName
    is AtomicType.PrimitiveType -> when (this.type) {
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

    AtomicType.EmptyType -> "google.protobuf.Empty"
    is ValueType.MapType -> "map<${keyType.toProtoString()}, ${valueType.toProtoString()}>"
    is ValueType.OptionalType -> "optional ${elemType.toProtoString()}"
    is ValueType.RepeatedType -> "repeated ${elemType.toProtoString()}"
    is ValueType.SetType -> "repeated ${elemType.toProtoString()}"
  }

  fun addField(member: ProtoMessageMember.MessageField) {
    addComments(member.comments)
    val typeStr = member.type.toProtoString()
    val tag = member.tag.toValueString()
    val optionStr = generateFieldOptions(member.options)
    addLine("$typeStr ${member.name.messageFieldName} = $tag$optionStr;")
  }

  fun addMessageDef(def: ProtoMessageDef) {
    addComments(def.comments)
    addLine("message ${def.name.messageName} {")
    indent {
      def.members.forEach { member ->
        when (member) {
          is ProtoMessageMember.MessageOption -> addOption(member.option)
          is ProtoMessageMember.MessageField -> addField(member)
          is ProtoMessageMember.NestedEnum -> addEnumDef(member.enumDef)
          is ProtoMessageMember.NestedMessage -> addMessageDef(member.messageDef)
          is ProtoMessageMember.OneOf -> {
            addComments(member.comments)
            addLine("oneof ${member.name.messageFieldName} {")
            indent {
              member.members.forEach { oneof ->
                when (oneof) {
                  is ProtoOneOfMember.OneOfOption -> addOption(oneof.option)
                  is ProtoOneOfMember.OneOfField -> addField(oneof.field)
                }
              }
            }
            addLine("}")
          }

          is ProtoMessageMember.Reserved -> {
            addComments(member.comments)
            val reserved = member.ranges.joinToString(", ") { reserve ->
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
            addLine("reserved $reserved;")
          }
        }
      }
      addComments(def.trailingComments)
    }
    addLine("}")
  }

  fun addSealedDef(def: ProtoSealedDef) {
    val sealedFields = def.sealedFields.map { field -> ProtoOneOfMember.OneOfField(field) }
    addMessageDef(
      ProtoMessageDef(
        def.comments,
        def.name,
        def.commonFields + ProtoMessageMember.OneOf(listOf(), def.name, sealedFields),
        def.trailingComments
      )
    )
  }

  fun addServiceDef(def: ProtoServiceDef) {
    addComments(def.comments)
    addLine("service ${def.name.serviceName} {")
    indent {
      def.members.forEach { member ->
        when (member) {
          is ProtoServiceMember.ServiceOption -> addOption(member.option)
          is ProtoServiceMember.ServiceRpc -> {
            addComments(member.comments)
            val inTypeStr = member.inType.toProtoString()
            val outTypeStr = member.outType.toProtoString()
            val optionStr = generateFieldOptions(member.options)
            addLine("rpc ${member.name.rpcName}($inTypeStr) returns ($outTypeStr)$optionStr;")
          }
        }
      }
      addComments(def.trailingComments)
    }
    addLine("}")
  }

  fun generate(defs: ProtoDefs): String {
    addComments(defs.comments)

    addLine("syntax = \"proto3\";")
    addLine()

    if (defs.packageName != null) {
      addLine("package ${defs.packageName};")
      addLine()
    }

    if (defs.emptyRequired && defs.imports.all { it.import != "google/protobuf/empty.proto" }) {
      addLine("import \"google/protobuf/empty.proto\";")
    }
    defs.imports.forEach { import ->
      addComments(import.comments)
      addLine("import ${protoString(import.import)};")
    }
    if (defs.imports.isNotEmpty()) {
      addLine()
    }

    defs.options.forEach { option ->
      addOption(option)
    }
    if (defs.options.isNotEmpty()) {
      addLine()
    }

    defs.defs.forEach { def ->
      when (def) {
        is ProtoEnumDef -> addEnumDef(def)
        is ProtoMessageDef -> addMessageDef(def)
        is ProtoSealedDef -> addSealedDef(def)
        is ProtoServiceDef -> addServiceDef(def)
      }
      addLine()
    }

    addComments(defs.trailingComments)

    return builder.toString()
  }
}
