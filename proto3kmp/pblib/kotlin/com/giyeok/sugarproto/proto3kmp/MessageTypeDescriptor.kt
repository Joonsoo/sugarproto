package com.giyeok.sugarproto.proto3kmp

abstract class GeneratedMessage {
  abstract fun serialize(): MessageEncodingResult

  fun serializeToByteArray(): ByteArray =
    serialize().toByteArray()
}

abstract class GeneratedMessageBuilder<T: GeneratedMessage> {
  abstract fun setFromSerializedByteArray(bytes: ByteArray)
  abstract fun setFromEncodingResult(encodingResult: MessageEncodingResult)
  abstract fun build(): T
}

data class MessageTypeDescriptor(
  val protoPkg: String,
  val name: String,
  val fields: List<MessageField>,
)

sealed class MessageField {
  data class Value(
    val tagNumber: Int,
    val name: String,
    val type: ValueType,
    val isRepeated: Boolean,
    val isOptional: Boolean
  ): MessageField()

  data class Map(
    val tagNumber: Int,
    val name: String,
    val keyType: ValueType,
    val valueType: ValueType
  ): MessageField()
}