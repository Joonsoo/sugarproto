package com.giyeok.sugarproto.proto3kmp.compiler

import com.giyeok.sugarproto.proto3kmp.*

data class MessageDef(
  val javaPkg: String,
  val protoPkg: String,
  val name: String,
  val fields: List<MessageField>,
  val oneofConstraints: List<MessageOneofConstraint>,
) {
  val requiredTypes
    get() = fields.flatMap { field ->
      when (val ftype = field.fieldType) {
        is MessageFieldType.Map -> ftype.key.requiredTypes() + ftype.value.requiredTypes()
        is MessageFieldType.Value -> ftype.type.requiredTypes()
      }
    }

  val canonicalJavaName get() = if (javaPkg.isEmpty()) name else "$javaPkg.$name"
}

fun ValueType.requiredTypes(): Set<String> = when (this) {
  BoolType, is Int32Type, is Int64Type,
  FloatType, DoubleType, BytesType, StringType -> setOf()

  is MessageType -> setOf(this.canonicalName)
  is PackedRepeatedField -> this.elemType.requiredTypes()
  is EnumType -> setOf(this.canonicalName)
}


data class MessageField(
  val number: Int,
  val name: String,
  val fieldType: MessageFieldType,
)

sealed class MessageFieldType {
  data class Value(val type: ValueType, val repeated: Boolean, val optional: Boolean):
    MessageFieldType()

  data class Map(val key: ValueType, val value: ValueType): MessageFieldType()
}

data class MessageOneofConstraint(
  val name: String,
  val fields: List<String>,
)

data class ServiceDef(
  val javaPkg: String,
  val protoPkg: String,
  val name: String,
  val rpcs: List<RpcDef>,
) {
  val canonicalName: String get() = if (javaPkg.isEmpty()) name else "$javaPkg.$name"
}

data class RpcDef(
  val name: String,
  val req: MessageType,
  val streamReq: Boolean,
  val res: MessageType,
  val streamRes: Boolean
)

data class EnumDef(
  val javaPkg: String,
  val name: String,
) {
  val canonicalJavaName get() = if (javaPkg.isEmpty()) name else "$javaPkg.$name"
}
