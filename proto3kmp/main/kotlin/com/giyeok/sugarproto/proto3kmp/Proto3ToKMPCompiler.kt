package com.giyeok.sugarproto.proto3kmp

import com.giyeok.sugarproto.Proto3Ast

class Proto3ToKMPCompiler(val names: ProtoNames, val def: Proto3Ast.Proto3) {
  init {
    val pkgs = def.defs.filterIsInstance<Proto3Ast.Package>()
    check(pkgs.size <= 1)
  }

  val pkg = def.defs.filterIsInstance<Proto3Ast.Package>()
    .singleOrNull()
  val pkgName = pkg?.name?.names?.joinToString(".") { it.name }

  fun generateDefs(): CompileResult {
    val messages = mutableListOf<MessageDef>()
    val services = mutableListOf<ServiceDef>()
    val enums = mutableListOf<EnumDef>()

    for (topLevelDef in def.defs.filterIsInstance<Proto3Ast.TopLevelDef>()) {
      when (topLevelDef) {
        is Proto3Ast.EnumDef -> enums.add(compileEnum(topLevelDef))
        is Proto3Ast.Message -> messages.add(compileMessage(topLevelDef))
        is Proto3Ast.Service -> services.add(compileService(topLevelDef))
      }
    }
    return CompileResult(messages, services, enums)
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

    is Proto3Ast.MessageOrEnumType -> TODO()
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
    // TODO
    return MessageType("", typ.name.name)
  }

  fun Proto3Ast.FieldModifier?.isRepeated(): Boolean =
    this == Proto3Ast.FieldModifier.REPEATED

  fun Proto3Ast.FieldModifier?.isOptional(): Boolean =
    this == Proto3Ast.FieldModifier.OPTIONAL

  fun compileMessage(msg: Proto3Ast.Message): MessageDef {
    val fields = mutableListOf<MessageField>()
    val oneofs = mutableListOf<MessageOneofConstraint>()

    for (elem in msg.body) {
      when (elem) {
        is Proto3Ast.Field -> {
          fields.add(
            MessageField(
              elem.fieldNumber.toInt(),
              elem.name.name,
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
              elem.mapName.name,
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
    return MessageDef(pkgName ?: "", msg.name.name, fields, oneofs)
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
    return ServiceDef(pkgName ?: "", service.name.name, rpcs)
  }
}

data class CompileResult(
  val messages: List<MessageDef>,
  val services: List<ServiceDef>,
  val enums: List<EnumDef>
)
