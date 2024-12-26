package com.giyeok.sugarproto.proto3kmp.generated

import com.giyeok.sugarproto.proto3kmp.GeneratedMessage
import com.giyeok.sugarproto.proto3kmp.GeneratedMessageBuilder
import com.giyeok.sugarproto.proto3kmp.LenValue
import com.giyeok.sugarproto.proto3kmp.MessageEncodingPair
import com.giyeok.sugarproto.proto3kmp.MessageEncodingResult
import com.giyeok.sugarproto.proto3kmp.MessageField
import com.giyeok.sugarproto.proto3kmp.MessageTypeDescriptor
import com.giyeok.sugarproto.proto3kmp.StringType
import com.giyeok.sugarproto.proto3kmp.parseBinary

data class MyProtocolRes(
  val address: String,
): GeneratedMessage() {
  companion object {
    val descriptor: MessageTypeDescriptor = MessageTypeDescriptor(
      "com.giyeok.sugarproto.proto3kmp.generated",
      "MyProtocolRes",
      listOf(
        MessageField.Value(1, "address", StringType, false, false),
      )
    )

    fun fromByteArray(bytes: ByteArray): MyProtocolRes {
      val builder = Builder()
      builder.setFromSerializedByteArray(bytes)
      return builder.build()
    }
  }

  data class Builder(
    var address: String = "",
  ): GeneratedMessageBuilder<MyProtocolRes>() {
    override fun setFromSerializedByteArray(bytes: ByteArray) {
      val pairs = parseBinary(bytes)
      setFromEncodingResult(pairs)
    }

    override fun setFromEncodingResult(encodingResult: MessageEncodingResult) {
      check(encodingResult.tagNumbers.containsAll(setOf(1)))
      val address = encodingResult.getSingle(1)
      check(address is LenValue)
      this.address = String(address.bytes)
    }

    override fun build(): MyProtocolRes = MyProtocolRes(
      address,
    )
  }

  override fun serialize(): MessageEncodingResult {
    val msg = this
    val pairs = buildList {
      msg.address.encodeToByteArray().let { value ->
        add(MessageEncodingPair(1, LenValue(value.size, value)))
      }
    }
    return MessageEncodingResult(pairs)
  }
}
