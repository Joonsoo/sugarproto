package com.giyeok.sugarproto.proto3ts

import com.giyeok.sugarproto.Proto3Ast
import com.giyeok.sugarproto.Proto3Parser

class Proto3TypeScriptCompiler {
  private fun nameToCamelCase(name: String): String {
    val tokens = name.split('_')
    return (tokens.take(1) + tokens.drop(1)
      .map { it.replaceFirstChar { c -> c.uppercase() } }).joinToString("")
  }

  fun typeStringOf(type: Proto3Ast.Type): String = when (type) {
    is Proto3Ast.BuiltinType -> when (type.typ) {
      Proto3Ast.BuiltinTypeEnum.BOOL -> "boolean"
      Proto3Ast.BuiltinTypeEnum.BYTES -> "Uint8Array"
      Proto3Ast.BuiltinTypeEnum.FLOAT -> "number"
      Proto3Ast.BuiltinTypeEnum.DOUBLE -> "number"
      Proto3Ast.BuiltinTypeEnum.FIXED32 -> TODO()
      Proto3Ast.BuiltinTypeEnum.FIXED64 -> TODO()
      Proto3Ast.BuiltinTypeEnum.INT32 -> "int32"
      Proto3Ast.BuiltinTypeEnum.INT64 -> "int64"
      Proto3Ast.BuiltinTypeEnum.SFIXED32 -> TODO()
      Proto3Ast.BuiltinTypeEnum.SFIXED64 -> TODO()
      Proto3Ast.BuiltinTypeEnum.SINT32 -> TODO()
      Proto3Ast.BuiltinTypeEnum.SINT64 -> TODO()
      Proto3Ast.BuiltinTypeEnum.STRING -> "string"
      Proto3Ast.BuiltinTypeEnum.UINT32 -> TODO()
      Proto3Ast.BuiltinTypeEnum.UINT64 -> TODO()
    }

    is Proto3Ast.MessageOrEnumType -> {
      type.name.name.name
    }
  }

  fun typeStringOf(mapKeyType: Proto3Ast.MapKeyType): String = when (mapKeyType) {
    Proto3Ast.MapKeyType.BOOL -> "boolean"
    Proto3Ast.MapKeyType.FIXED32 -> TODO()
    Proto3Ast.MapKeyType.FIXED64 -> TODO()
    Proto3Ast.MapKeyType.INT32 -> "int32"
    Proto3Ast.MapKeyType.INT64 -> "int64"
    Proto3Ast.MapKeyType.SFIXED32 -> TODO()
    Proto3Ast.MapKeyType.SFIXED64 -> TODO()
    Proto3Ast.MapKeyType.SINT32 -> TODO()
    Proto3Ast.MapKeyType.SINT64 -> TODO()
    Proto3Ast.MapKeyType.STRING -> "string"
    Proto3Ast.MapKeyType.UINT32 -> TODO()
    Proto3Ast.MapKeyType.UINT64 -> TODO()
  }

  fun generateMessageClass(writer: CodeWriter, message: Proto3Ast.Message) {
    writer.writeLine("class ${message.name.name} {")
    writer.indent {
      message.body.forEach { elem ->
        when (elem) {
          is Proto3Ast.EmptyStatement -> {
            // do nothing
          }

          is Proto3Ast.EnumDef -> {
            TODO()
          }

          is Proto3Ast.Field -> {
            val elemName = nameToCamelCase(elem.name.name)
            val typeString = typeStringOf(elem.typ)

            when (elem.modifier) {
              Proto3Ast.FieldModifier.OPTIONAL ->
                writer.writeLine("$elemName?: $typeString;")

              Proto3Ast.FieldModifier.REPEATED ->
                writer.writeLine("$elemName: $typeString[];")

              null ->
                writer.writeLine("$elemName: $typeString;")
            }
          }

          is Proto3Ast.MapField -> {
            val keyType = typeStringOf(elem.keyType)
            val valueType = typeStringOf(elem.valueType)
            writer.writeLine("${nameToCamelCase(elem.mapName.name)}: Map<$keyType, $valueType>;")
          }

          is Proto3Ast.Message -> {
            generateMessageClass(writer, elem)
          }

          is Proto3Ast.OneofDef -> {
            writer.writeLine("// oneof")
          }

          is Proto3Ast.OptionDef -> TODO()
          is Proto3Ast.Reserved -> TODO()
        }
      }
    }
    writer.writeLine("}")
  }

  fun generateEnum(writer: CodeWriter, enum: Proto3Ast.EnumDef) {
    writer.writeLine("enum ${enum.name.name} {")
    writer.indent {
      enum.body.forEach { elem ->
        when (elem) {
          is Proto3Ast.EmptyStatement -> {
            // do nothing
          }

          is Proto3Ast.EnumFieldDef -> {
            val value = when (val valueString = elem.value) {
              is Proto3Ast.DecimalLit -> valueString.value
              is Proto3Ast.HexLit -> "0x${valueString.value}"
              is Proto3Ast.OctalLit -> TODO()
            }
            writer.writeLine("${elem.name.name} = $value,")
          }

          is Proto3Ast.OptionDef -> TODO()
        }
      }
    }
    writer.writeLine("}")
  }

  fun main(sourceCode: String) {
    val writer = CodeWriter()
    val x = Proto3Parser.parse(sourceCode)
    x.defs.forEach { def ->
      when (def) {
        is Proto3Ast.EmptyStatement -> {
          // do nothing
        }

        is Proto3Ast.Import -> {
          // TODO
        }

        is Proto3Ast.OptionDef -> {
          // TODO
        }

        is Proto3Ast.Package -> {
          // TODO
        }

        is Proto3Ast.EnumDef -> {
          generateEnum(writer, def)
        }

        is Proto3Ast.Message -> {
          generateMessageClass(writer, def)
        }

        is Proto3Ast.Service -> {
          // TODO
        }
      }
    }

    println(writer.toString())
  }
}
