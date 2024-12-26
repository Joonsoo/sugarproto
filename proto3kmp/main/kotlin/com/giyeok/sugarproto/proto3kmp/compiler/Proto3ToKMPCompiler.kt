package com.giyeok.sugarproto.proto3kmp.compiler

import com.giyeok.sugarproto.Proto3Ast
import com.giyeok.sugarproto.proto3kmp.*

class Proto3ToKMPCompiler(val names: ProtoNames, val def: Proto3Ast.Proto3) {
  init {
    val pkgs = def.defs.filterIsInstance<Proto3Ast.Package>()
    check(pkgs.size <= 1)
  }

  val protoPkg = def.defs.filterIsInstance<Proto3Ast.Package>()
    .singleOrNull()
  val protoPkgName = protoPkg?.name?.names?.joinToString(".") { it.name }

  val javaPkg = def.defs.filterIsInstance<Proto3Ast.OptionDef>()
    .singleOrNull { it.name.scope.let { it is Proto3Ast.Ident && it.name == "java_package" } }
    ?.value?.asStringValue() ?: protoPkgName

  private fun Proto3Ast.Constant.asStringValue(): String = when (this) {
    is Proto3Ast.BoolConstant -> TODO()
    is Proto3Ast.FloatConstant -> TODO()
    is Proto3Ast.FullIdent -> TODO()
    is Proto3Ast.IntConstant -> TODO()
    is Proto3Ast.StringConstant -> when (val lit = this.value) {
      is Proto3Ast.DoubleQuoteStrLit -> lit.value.joinToString("") { c ->
        when (c) {
          is Proto3Ast.Character -> c.value.toString()
          is Proto3Ast.CharEscape -> TODO()
          is Proto3Ast.HexEscape -> TODO()
          is Proto3Ast.OctalEscape -> TODO()
        }
      }

      is Proto3Ast.SingleQuoteStrLit -> TODO()
    }
  }

  private fun nameWithPkg(name: String): String =
    if (protoPkg == null) name else "$protoPkgName.$name"

  fun generateDefs(): FileCompileResult {
    val services = mutableListOf<ServiceDef>()
    val messages = mutableMapOf<String, MessageDef>()
    val enums = mutableMapOf<String, EnumDef>()

    for (topLevelDef in def.defs.filterIsInstance<Proto3Ast.TopLevelDef>()) {
      when (topLevelDef) {
        is Proto3Ast.Service -> services.add(compileService(topLevelDef))
        is Proto3Ast.Message ->
          messages[nameWithPkg(topLevelDef.name.name)] = compileMessage(topLevelDef)

        is Proto3Ast.EnumDef -> enums[nameWithPkg(topLevelDef.name.name)] = compileEnum(topLevelDef)
      }
    }
    return FileCompileResult(services, messages, enums)
  }

  fun compileEnum(enum: Proto3Ast.EnumDef): EnumDef {
    TODO()
  }

  fun Proto3Ast.IntLit.toInt(): Int = when (this) {
    is Proto3Ast.DecimalLit -> this.value.toInt()
    is Proto3Ast.HexLit -> this.value.toInt(16)
    is Proto3Ast.OctalLit -> this.value.toInt(8)
  }

  fun compileType(typ: Proto3Ast.Type): ValueType = when (typ) {
    is Proto3Ast.BuiltinType -> when (typ.typ) {
      Proto3Ast.BuiltinTypeEnum.BOOL -> BoolType
      Proto3Ast.BuiltinTypeEnum.BYTES -> BytesType
      Proto3Ast.BuiltinTypeEnum.FLOAT -> FloatType
      Proto3Ast.BuiltinTypeEnum.DOUBLE -> DoubleType
      Proto3Ast.BuiltinTypeEnum.FIXED32 -> Int32Type(Int32Encoding.FIXED32)
      Proto3Ast.BuiltinTypeEnum.FIXED64 -> Int64Type(Int64Encoding.FIXED64)
      Proto3Ast.BuiltinTypeEnum.INT32 -> Int32Type(Int32Encoding.INT32)
      Proto3Ast.BuiltinTypeEnum.INT64 -> Int64Type(Int64Encoding.INT64)
      Proto3Ast.BuiltinTypeEnum.SFIXED32 -> Int32Type(Int32Encoding.SFIXED32)
      Proto3Ast.BuiltinTypeEnum.SFIXED64 -> Int64Type(Int64Encoding.SFIXED64)
      Proto3Ast.BuiltinTypeEnum.SINT32 -> Int32Type(Int32Encoding.SINT32)
      Proto3Ast.BuiltinTypeEnum.SINT64 -> Int64Type(Int64Encoding.SINT64)
      Proto3Ast.BuiltinTypeEnum.STRING -> StringType
      Proto3Ast.BuiltinTypeEnum.UINT32 -> Int32Type(Int32Encoding.UINT32)
      Proto3Ast.BuiltinTypeEnum.UINT64 -> Int64Type(Int64Encoding.UINT64)
    }

    is Proto3Ast.MessageOrEnumType -> {
      val canonicalName = (typ.name.parent + typ.name.name).joinToString(".") { it.name }
      if (canonicalName in names.messages) {
        MessageType(canonicalName)
      } else if (canonicalName in names.enums) {
        EnumType(canonicalName)
      } else {
        throw IllegalStateException("Type not found: $canonicalName")
      }
    }
  }

  fun compileType(typ: Proto3Ast.MapKeyType): ValueType = when (typ) {
    Proto3Ast.MapKeyType.BOOL -> BoolType
    Proto3Ast.MapKeyType.FIXED32 -> Int32Type(Int32Encoding.FIXED32)
    Proto3Ast.MapKeyType.FIXED64 -> Int64Type(Int64Encoding.FIXED64)
    Proto3Ast.MapKeyType.INT32 -> Int32Type(Int32Encoding.INT32)
    Proto3Ast.MapKeyType.INT64 -> Int64Type(Int64Encoding.INT64)
    Proto3Ast.MapKeyType.SFIXED32 -> Int32Type(Int32Encoding.SFIXED32)
    Proto3Ast.MapKeyType.SFIXED64 -> Int64Type(Int64Encoding.SFIXED64)
    Proto3Ast.MapKeyType.SINT32 -> Int32Type(Int32Encoding.SINT32)
    Proto3Ast.MapKeyType.SINT64 -> Int64Type(Int64Encoding.SINT64)
    Proto3Ast.MapKeyType.STRING -> StringType
    Proto3Ast.MapKeyType.UINT32 -> Int32Type(Int32Encoding.UINT32)
    Proto3Ast.MapKeyType.UINT64 -> Int64Type(Int64Encoding.UINT64)
  }

  fun compileType(typ: Proto3Ast.MessageType): MessageType {
    val canonicalName = if (typ.parent.isEmpty()) {
      if (protoPkgName == null) {
        typ.name.name
      } else {
        "$protoPkgName.${typ.name.name}"
      }
    } else {
      (typ.parent.map { it.name } + typ.name.name).joinToString(".")
    }
    check(canonicalName in names.messages)
    return MessageType(canonicalName)
  }

  fun Proto3Ast.FieldModifier?.isRepeated(): Boolean =
    this == Proto3Ast.FieldModifier.REPEATED

  fun Proto3Ast.FieldModifier?.isOptional(): Boolean =
    this == Proto3Ast.FieldModifier.OPTIONAL

  fun fieldNameToCamelCase(name: String): String {
    val words = name.split("_")
    return (words.take(1) +
      words.drop(1).map { word -> word.replaceFirstChar { it.uppercase() } })
      .joinToString("")
  }

  fun compileMessage(msg: Proto3Ast.Message): MessageDef {
    val fields = mutableListOf<MessageField>()
    val oneofs = mutableListOf<MessageOneofConstraint>()

    for (elem in msg.body) {
      when (elem) {
        is Proto3Ast.Field -> {
          fields.add(
            MessageField(
              elem.fieldNumber.toInt(),
              fieldNameToCamelCase(elem.name.name),
              MessageFieldType.Value(
                compileType(elem.typ),
                elem.modifier.isRepeated(),
                elem.modifier.isOptional()
              )
            )
          )
        }

        is Proto3Ast.MapField -> {
          fields.add(
            MessageField(
              elem.number.toInt(),
              fieldNameToCamelCase(elem.mapName.name),
              MessageFieldType.Map(compileType(elem.keyType), compileType(elem.valueType))
            )
          )
        }

        is Proto3Ast.OneofDef -> {
          val constraint = mutableListOf<String>()
          for (oneof in elem.elems) {
            when (oneof) {
              is Proto3Ast.EmptyStatement -> {}
              is Proto3Ast.OneofField -> {
                fields.add(
                  MessageField(
                    oneof.number.toInt(),
                    oneof.name.name,
                    MessageFieldType.Value(compileType(oneof.typ), false, false)
                  )
                )
                constraint.add(oneof.name.name)
              }

              is Proto3Ast.OptionDef -> TODO()
            }
          }
        }

        is Proto3Ast.EmptyStatement -> {}
        is Proto3Ast.EnumDef -> TODO()
        is Proto3Ast.Message -> TODO()
        is Proto3Ast.OptionDef -> TODO()
        is Proto3Ast.Reserved -> TODO()
      }
    }
    return MessageDef(javaPkg ?: "", protoPkgName ?: "", msg.name.name, fields, oneofs)
  }

  fun compileService(service: Proto3Ast.Service): ServiceDef {
    val rpcs = mutableListOf<RpcDef>()
    for (elem in service.body) {
      when (elem) {
        is Proto3Ast.Rpc -> {
          rpcs.add(
            RpcDef(
              elem.name.name,
              compileType(elem.inputType),
              elem.isInputStream,
              compileType(elem.outputType),
              elem.isOutputStream
            )
          )
        }

        is Proto3Ast.EmptyStatement -> {}
        is Proto3Ast.OptionDef -> TODO()
      }
    }
    return ServiceDef(javaPkg ?: "", protoPkgName ?: "", service.name.name, rpcs)
  }
}

data class FileCompileResult(
  val services: List<ServiceDef>,
  val messages: Map<String, MessageDef>,
  val enums: Map<String, EnumDef>,
) {
  val requiredTypes: Set<String> get() = messages.values.flatMap { it.requiredTypes }.toSet()
}
