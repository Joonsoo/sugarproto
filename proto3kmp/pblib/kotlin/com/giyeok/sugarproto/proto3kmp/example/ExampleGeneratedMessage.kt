package com.giyeok.sugarproto.proto3kmp.example

import com.giyeok.sugarproto.proto3kmp.*

data class ExampleGeneratedMessage(
  val field1: String,
  val field2: Long,
  val field3: ExampleGeneratedMessage?,
): GeneratedMessage() {
  companion object {
    val descriptor: MessageTypeDescriptor =
      MessageTypeDescriptor(
        "abc", "ExampleGenerated", listOf(
          MessageField.Value(1, "field1", StringType),
          MessageField.Value(2, "field2", Int64Type(Int64Encoding.INT64)),
          MessageField.Value(3, "field3", MessageType("abc.ExampleGenerated")),
        )
      )

    fun fromByteArray(bytes: ByteArray): ExampleGeneratedMessage {
      val builder = Builder()
      builder.setFromSerializedByteArray(bytes)
      return builder.build()
    }
  }

  data class Builder(
    var field1: String = "",
    var field2: Long = 0L,
    var field3: Builder? = null,
  ): GeneratedMessageBuilder<ExampleGeneratedMessage>() {
    override fun setFromSerializedByteArray(bytes: ByteArray) {
      val pairs = parseBinary(bytes)
      setFromEncodingResult(pairs)
    }

    override fun setFromEncodingResult(encodingResult: MessageEncodingResult) {
      check(encodingResult.tagNumbers.containsAll(setOf(1, 2, 3)))

      val field1 = encodingResult.getSingle(1)
      check(field1 is LenValue)
      this.field1 = String(field1.bytes)

      val field2 = encodingResult.getSingle(2)
      check(field2 is VarIntValue)
      this.field2 = field2.value

      val field3 = encodingResult.getOptional(3)
      if (field3 == null) {
        this.field3 = null
      } else {
        check(field3 is LenValue)
        this.field3 = Builder().also {
          this.setFromSerializedByteArray(field3.bytes)
        }
      }
    }

    override fun build(): ExampleGeneratedMessage =
      ExampleGeneratedMessage(field1, field2, field3?.build())
  }

  override fun serialize(): MessageEncodingResult {
    val pairs = mutableListOf<MessageEncodingPair>()
    val field1Enc = field1.encodeToByteArray()
    pairs.add(MessageEncodingPair(1, LenValue(field1Enc.size, field1Enc)))
    pairs.add(MessageEncodingPair(2, VarIntValue(field2)))
    field3?.let {
      val field3Enc = field3.serializeToByteArray()
      pairs.add(MessageEncodingPair(3, LenValue(field3Enc.size, field3Enc)))
    }
    return MessageEncodingResult(pairs.toList())
  }
}
