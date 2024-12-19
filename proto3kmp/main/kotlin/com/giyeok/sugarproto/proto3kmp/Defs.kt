package com.giyeok.sugarproto.proto3kmp

data class MessageDef(
  val pkg: String,
  val name: String,
  val fields: List<MessageField>,
  val oneofConstraints: List<MessageOneofConstraint>,
)

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
  val pkg: String,
  val name: String,
  val rpcs: List<RpcDef>,
)

data class RpcDef(
  val name: String,
  val req: MessageType,
  val streamReq: Boolean,
  val res: MessageType,
  val streamRes: Boolean
)

data class EnumDef(
  val name: String,
)
