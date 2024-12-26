package com.giyeok.sugarproto.proto3kmp.generated

import com.giyeok.sugarproto.proto3kmp.GeneratedMessage
import com.giyeok.sugarproto.proto3kmp.GeneratedMessageBuilder
import com.giyeok.sugarproto.proto3kmp.Int32Encoding
import com.giyeok.sugarproto.proto3kmp.Int32Type
import com.giyeok.sugarproto.proto3kmp.LenValue
import com.giyeok.sugarproto.proto3kmp.MessageEncodingPair
import com.giyeok.sugarproto.proto3kmp.MessageEncodingResult
import com.giyeok.sugarproto.proto3kmp.MessageField
import com.giyeok.sugarproto.proto3kmp.MessageType
import com.giyeok.sugarproto.proto3kmp.MessageTypeDescriptor
import com.giyeok.sugarproto.proto3kmp.StringType
import com.giyeok.sugarproto.proto3kmp.VarIntValue
import com.giyeok.sugarproto.proto3kmp.parseBinary

data class MyProtocolReq(
  val name: String,
  val age: Int,
  val address: String?,
  val family: List<String>,
  val map: Map<String, String>,
  val plainTs: com.google.protobuf.Timestamp,
  val optionalTs: com.google.protobuf.Timestamp?,
  val repeatedTs: List<com.google.protobuf.Timestamp>,
): GeneratedMessage() {
  companion object {
    val descriptor: MessageTypeDescriptor = MessageTypeDescriptor(
      "com.giyeok.sugarproto.proto3kmp.generated",
      "MyProtocolReq",
      listOf(
        MessageField.Value(1, "name", StringType, false, false),
        MessageField.Value(2, "age", Int32Type(Int32Encoding.INT32), false, false),
        MessageField.Value(3, "address", StringType, false, true),
        MessageField.Value(4, "family", StringType, true, false),
        MessageField.Map(5, "map", StringType, StringType),
        MessageField.Value(6, "plainTs", MessageType("google.protobuf.Timestamp"), false, false),
        MessageField.Value(7, "optionalTs", MessageType("google.protobuf.Timestamp"), false, true),
        MessageField.Value(8, "repeatedTs", MessageType("google.protobuf.Timestamp"), true, false),
      )
    )

    fun fromByteArray(bytes: ByteArray): MyProtocolReq {
      val builder = Builder()
      builder.setFromSerializedByteArray(bytes)
      return builder.build()
    }
  }

  data class Builder(
    var name: String = "",
    var age: Int = 0,
    var address: String? = null,
    val family: MutableList<String> = mutableListOf(),
    val map: MutableMap<String, String> = mutableMapOf(),
    val plainTs: com.google.protobuf.Timestamp.Builder = com.google.protobuf.Timestamp.Builder(),
    var optionalTs: com.google.protobuf.Timestamp.Builder? = null,
    val repeatedTs: MutableList<com.google.protobuf.Timestamp.Builder> = mutableListOf(),
  ): GeneratedMessageBuilder<MyProtocolReq>() {
    override fun setFromSerializedByteArray(bytes: ByteArray) {
      val pairs = parseBinary(bytes)
      setFromEncodingResult(pairs)
    }

    override fun setFromEncodingResult(encodingResult: MessageEncodingResult) {
      check(encodingResult.tagNumbers.containsAll(setOf(1, 2, 6)))
      val name = encodingResult.getSingle(1)
      check(name is LenValue)
      this.name = String(name.bytes)
      val age = encodingResult.getSingle(2)
      check(age is VarIntValue)
      this.age = age.value.toInt()
      val address = encodingResult.getOptional(3)
      this.address = address?.let {
        check(it is LenValue)
        String(it.bytes)
      }
      this.family.clear()
      encodingResult.getRepeated(4).forEach {
        check(it is LenValue)
        this.family.add(String(it.bytes))
      }
      this.map.clear()
      encodingResult.getRepeated(5).forEach { kvPair ->
        check(kvPair is LenValue)
        val parsedKvPair = parseBinary(kvPair.bytes)
        val rawKey = parsedKvPair.getSingle(1)
        check(rawKey is LenValue)
        val rawValue = parsedKvPair.getSingle(2)
        check(rawValue is LenValue)
        this.map[String(rawKey.bytes)] = String(rawValue.bytes)
      }
      val plainTs = encodingResult.getSingle(6)
      check(plainTs is LenValue)
      this.plainTs.setFromSerializedByteArray(plainTs.bytes)
      val optionalTs = encodingResult.getOptional(7)
      optionalTs?.let {
        check(optionalTs is LenValue)
        val builder = com.google.protobuf.Timestamp.Builder()
        builder.setFromSerializedByteArray(optionalTs.bytes)
        this.optionalTs = builder
      }
      this.repeatedTs.clear()
      encodingResult.getRepeated(8).forEach {
        check(it is LenValue)
        val builder = com.google.protobuf.Timestamp.Builder()
        builder.setFromSerializedByteArray(it.bytes)
        this.repeatedTs.add(builder)
      }
    }

    override fun build(): MyProtocolReq = MyProtocolReq(
      name,
      age,
      address,
      family.toList(),
      map.toMap(),
      plainTs.build(),
      optionalTs?.build(),
      repeatedTs.map { it.build() },
    )
  }

  override fun serialize(): MessageEncodingResult {
    val msg = this
    val pairs = buildList {
      msg.name.encodeToByteArray().let { value ->
        add(MessageEncodingPair(1, LenValue(value.size, value)))
      }
      add(MessageEncodingPair(2, VarIntValue(msg.age)))
      msg.address?.let { value ->
        value.encodeToByteArray().let { value ->
          add(MessageEncodingPair(3, LenValue(value.size, value)))
        }
      }
      msg.family.forEach { value ->
        value.encodeToByteArray().let { value ->
          add(MessageEncodingPair(4, LenValue(value.size, value)))
        }
      }
      msg.map.forEach { (key, value) ->
        val kvPair = buildList {
          key.encodeToByteArray().let { value ->
            add(MessageEncodingPair(1, LenValue(value.size, value)))
          }
          value.encodeToByteArray().let { value ->
            add(MessageEncodingPair(2, LenValue(value.size, value)))
          }
        }
        val kvPairEncoded = MessageEncodingResult(kvPair).toByteArray()
        add(MessageEncodingPair(5, LenValue(kvPairEncoded.size, kvPairEncoded)))
      }
      msg.plainTs.serializeToByteArray().let { arr ->
        add(MessageEncodingPair(6, LenValue(arr.size, arr)))
      }
      msg.optionalTs?.let { value ->
        value.serializeToByteArray().let { arr ->
          add(MessageEncodingPair(7, LenValue(arr.size, arr)))
        }
      }
      msg.repeatedTs.forEach { value ->
        value.serializeToByteArray().let { arr ->
          add(MessageEncodingPair(8, LenValue(arr.size, arr)))
        }
      }
    }
    return MessageEncodingResult(pairs)
  }
}
