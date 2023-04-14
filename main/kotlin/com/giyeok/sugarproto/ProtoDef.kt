package com.giyeok.sugarproto

sealed class ProtoDef

data class ProtoMessageDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: String,
  val members: List<ProtoMessageMember>,
): ProtoDef()

data class ProtoStreamableFieldType(
  val isStream: Boolean,
  val valueType: ProtoFieldType,
)

data class ProtoFieldType(
  val kind: FieldKindEnum,
  val optional: Boolean,
  val repeated: Boolean,
  val type: TypeExpr,
)

sealed class TypeExpr {
  fun toProtoTypeString(): String = when (this) {
    EmptyMessage -> "google.protobuf.Empty"
    is MessageOrEnumName -> this.name
    is PrimitiveType -> when (this.typ) {
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

    is MapType -> "map<${this.keyType.toProtoTypeString()}, ${this.valueType.toProtoTypeString()}>"
  }

  object EmptyMessage: TypeExpr()
  data class MessageOrEnumName(val name: String): TypeExpr()
  data class MapType(val keyType: TypeExpr, val valueType: TypeExpr): TypeExpr()
  data class PrimitiveType(val typ: SugarProtoAst.PrimitiveTypeEnum): TypeExpr()
}

enum class FieldKindEnum {
  PrimitiveKind,

  // message or enum
  MessageKind,

  MapKind,
}

sealed class ProtoMessageMember {
  data class ProtoFieldDef(
    val comments: List<SugarProtoAst.Comment>,
    val type: ProtoFieldType,
    val name: String,
    val tag: SugarProtoAst.IntLiteral,
    val options: SugarProtoAst.FieldOptions?,
  ): ProtoMessageMember()

  data class ProtoOneOf(
    val comments: List<SugarProtoAst.Comment>,
    val name: String,
    val members: List<ProtoOneOfMember>,
  ): ProtoMessageMember()

  sealed class ProtoOneOfMember {
    data class OneOfField(val field: ProtoFieldDef): ProtoOneOfMember()
    data class OneOfOption(
      val comments: List<SugarProtoAst.Comment>,
      val option: SugarProtoAst.OptionDef
    ): ProtoOneOfMember()
  }

  data class ProtoNestedEnumDef(
    val enum: ProtoEnumDef
  ): ProtoMessageMember()

  data class ProtoNestedMessageDef(
    val message: ProtoMessageDef
  ): ProtoMessageMember()

  data class ProtoMessageOptionDef(
    val comments: List<SugarProtoAst.Comment>,
    val optionDef: SugarProtoAst.OptionDef
  ): ProtoMessageMember()

  data class ProtoReservedDef(
    val comments: List<SugarProtoAst.Comment>,
    val reserved: List<SugarProtoAst.ReservedItem>
  ): ProtoMessageMember()
}

data class ProtoSealedDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: String,
  val oneofMembers: List<ProtoMessageMember.ProtoOneOfMember>
): ProtoDef()

data class ProtoEnumDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: String,
  val members: List<ProtoEnumMember>,
): ProtoDef()

sealed class ProtoEnumMember {
  data class EnumOption(
    val comments: List<SugarProtoAst.Comment>,
    val optionDef: SugarProtoAst.OptionDef
  ): ProtoEnumMember()

  data class EnumValueDef(
    val comments: List<SugarProtoAst.Comment>,
    val minusTag: Boolean,
    val tag: SugarProtoAst.IntLiteral,
    val name: String,
    val options: SugarProtoAst.FieldOptions?,
  ): ProtoEnumMember()
}

data class ProtoServiceDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: String,
  val members: List<ServiceMember>,
): ProtoDef()

sealed class ServiceMember {
  data class ServiceOption(
    val comments: List<SugarProtoAst.Comment>,
    val optionDef: SugarProtoAst.OptionDef
  ): ServiceMember()

  data class ProtoRpcDef(
    val comments: List<SugarProtoAst.Comment>,
    val name: String,
    val isInTypeStream: Boolean,
    val inType: TypeExpr,
    val isOutTypeStream: Boolean,
    val outType: TypeExpr,
    val options: SugarProtoAst.FieldOptions?,
  ): ServiceMember()
}
