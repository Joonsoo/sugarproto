package com.giyeok.sugarproto.proto3kmp

import com.giyeok.sugarproto.proto3kmp.*
import io.ktor.client.*

class Proto3KmpTestServiceClient(
  channel: HttpClient,
  serverHost: String,
  serverPort: Int,
): GeneratedGrpcService(channel, serverHost, serverPort) {
  companion object {
    val serviceDescriptor: ServiceDescriptor = ServiceDescriptor(
      "com.giyeok.sugarproto.proto3kmp.generated",
      "Proto3KmpTestService",
      listOf(
        ServiceRpcDescriptor("MyProtocol", false, "MyProtocolReq", false, "MyProtocolRes"),
      )
    )
  }

  suspend fun myProtocol(request: MyProtocolReq): MyProtocolRes {
    val resPairs = unaryRequest("com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestService/MyProtocol", request.serialize())
    val resBuilder = MyProtocolRes.Builder()
    resBuilder.setFromEncodingResult(resPairs)
    return resBuilder.build()
  }
}

data class MyProtocolReq(
  val name: String,
  val age: Int,
  val address: String?,
  val family: List<String>,
  val map: Map<String, String>,
  val plainTs: Timestamp,
  val optionalTs: Timestamp?,
  val repeatedTs: List<Timestamp>,
): GeneratedMessage() {
  companion object {
    val descriptor: MessageTypeDescriptor = MessageTypeDescriptor(
      "com.giyeok.sugarproto.proto3kmp.generated",
      "MyProtocolReq",
      listOf(
        MessageField(1, "name", MessageFieldType.Value(StringType, false, false)),
        MessageField(2, "age", MessageFieldType.Value(Int32Type(Int32Encoding.INT32), false, false)),
        MessageField(3, "address", MessageFieldType.Value(StringType, true, false)),
        MessageField(4, "family", MessageFieldType.Value(StringType, false, true)),
        MessageField(5, "map", MessageFieldType.Map(StringType, StringType)),
        MessageField(6, "plainTs", MessageFieldType.Value(MessageType("Timestamp"), false, false)),
        MessageField(7, "optionalTs", MessageFieldType.Value(MessageType("Timestamp"), true, false)),
        MessageField(8, "repeatedTs", MessageFieldType.Value(MessageType("Timestamp"), false, true)),
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
    val plainTs: Timestamp.Builder = Timestamp.Builder(),
    var optionalTs: Timestamp.Builder? = null,
    val repeatedTs: MutableList<Timestamp.Builder> = mutableListOf(),
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
        val builder = Timestamp.Builder()
        builder.setFromSerializedByteArray(optionalTs.bytes)
        this.optionalTs = builder
      }
      this.repeatedTs.clear()
      encodingResult.getRepeated(8).forEach {
        check(it is LenValue)
        val builder = Timestamp.Builder()
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
data class MyProtocolRes(
  val address: String,
): GeneratedMessage() {
  companion object {
    val descriptor: MessageTypeDescriptor = MessageTypeDescriptor(
      "com.giyeok.sugarproto.proto3kmp.generated",
      "MyProtocolRes",
      listOf(
        MessageField(1, "address", MessageFieldType.Value(StringType, false, false)),
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
data class Timestamp(
  val seconds: Long,
  val nanos: Int,
): GeneratedMessage() {
  companion object {
    val descriptor: MessageTypeDescriptor = MessageTypeDescriptor(
      "com.giyeok.sugarproto.proto3kmp.generated",
      "Timestamp",
      listOf(
        MessageField(1, "seconds", MessageFieldType.Value(Int64Type(Int64Encoding.INT64), false, false)),
        MessageField(2, "nanos", MessageFieldType.Value(Int32Type(Int32Encoding.INT32), false, false)),
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
