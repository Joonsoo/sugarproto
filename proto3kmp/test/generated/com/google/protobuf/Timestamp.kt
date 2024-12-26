package com.google.protobuf

import com.giyeok.sugarproto.proto3kmp.GeneratedMessage
import com.giyeok.sugarproto.proto3kmp.GeneratedMessageBuilder
import com.giyeok.sugarproto.proto3kmp.Int32Encoding
import com.giyeok.sugarproto.proto3kmp.Int32Type
import com.giyeok.sugarproto.proto3kmp.Int64Encoding
import com.giyeok.sugarproto.proto3kmp.Int64Type
import com.giyeok.sugarproto.proto3kmp.MessageEncodingPair
import com.giyeok.sugarproto.proto3kmp.MessageEncodingResult
import com.giyeok.sugarproto.proto3kmp.MessageField
import com.giyeok.sugarproto.proto3kmp.MessageTypeDescriptor
import com.giyeok.sugarproto.proto3kmp.VarIntValue
import com.giyeok.sugarproto.proto3kmp.parseBinary

data class Timestamp(
  val seconds: Long,
  val nanos: Int,
): GeneratedMessage() {
  companion object {
    val descriptor: MessageTypeDescriptor = MessageTypeDescriptor(
      "google.protobuf",
      "Timestamp",
      listOf(
        MessageField.Value(1, "seconds", Int64Type(Int64Encoding.INT64), false, false),
        MessageField.Value(2, "nanos", Int32Type(Int32Encoding.INT32), false, false),
      )
    )

    fun fromByteArray(bytes: ByteArray): Timestamp {
      val builder = Builder()
      builder.setFromSerializedByteArray(bytes)
      return builder.build()
    }
  }

  data class Builder(
    var seconds: Long = 0,
    var nanos: Int = 0,
  ): GeneratedMessageBuilder<Timestamp>() {
    override fun setFromSerializedByteArray(bytes: ByteArray) {
      val pairs = parseBinary(bytes)
      setFromEncodingResult(pairs)
    }

    override fun setFromEncodingResult(encodingResult: MessageEncodingResult) {
      check(encodingResult.tagNumbers.containsAll(setOf(1, 2)))
      val seconds = encodingResult.getSingle(1)
      check(seconds is VarIntValue)
      this.seconds = seconds.value
      val nanos = encodingResult.getSingle(2)
      check(nanos is VarIntValue)
      this.nanos = nanos.value.toInt()
    }

    override fun build(): Timestamp = Timestamp(
      seconds,
      nanos,
    )
  }

  override fun serialize(): MessageEncodingResult {
    val msg = this
    val pairs = buildList {
      add(MessageEncodingPair(1, VarIntValue(msg.seconds)))
      add(MessageEncodingPair(2, VarIntValue(msg.nanos)))
    }
    return MessageEncodingResult(pairs)
  }
}
