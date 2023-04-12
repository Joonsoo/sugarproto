package com.giyeok.sugarproto

sealed class ProtoDef

data class ProtoMessageDef(
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
  val type: String,
)

enum class FieldKindEnum {
  PrimitiveKind,

  // message or enum
  MessageKind,

  MapKind,
}

sealed class ProtoMessageMember {
  data class ProtoFieldDef(
    val type: ProtoFieldType,
    val name: String,
    val tag: SugarProtoAst.IntLiteral,
    val options: SugarProtoAst.FieldOptions?,
  ): ProtoMessageMember()

  data class ProtoOneOf(
    val name: String,
    val members: List<ProtoOneOfMember>,
  ): ProtoMessageMember()

  sealed class ProtoOneOfMember {
    data class OneOfField(val field: ProtoFieldDef): ProtoOneOfMember()
    data class OneOfOption(val option: SugarProtoAst.OptionDef): ProtoOneOfMember()
  }

  data class ProtoNestedMessageDef(
    val message: ProtoMessageDef
  ): ProtoMessageMember()
}

data class ProtoServiceDef(
  val name: String,
  val rpcs: List<ProtoRpcDef>,
): ProtoDef()

data class ProtoRpcDef(
  val name: String,
  val isInTypeStream: Boolean,
  val inType: String,
  val isOutTypeStream: Boolean,
  val outType: String,
)
