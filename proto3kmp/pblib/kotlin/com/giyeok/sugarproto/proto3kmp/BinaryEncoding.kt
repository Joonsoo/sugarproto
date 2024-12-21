package com.giyeok.sugarproto.proto3kmp

fun parseBinary(bytes: ByteArray): MessageEncodingResult = parseBinary(bytes, 0)

fun parseBinary(bytes: ByteArray, offset: Int): MessageEncodingResult {
  val stream = ProtoInputStream(bytes, offset)
  val pairs = mutableListOf<MessageEncodingPair>()
  while (stream.hasNext()) {
    val tlv = stream.nextVarint()
    val fieldNumber = tlv shr 3
    check(fieldNumber >= 0 && fieldNumber < Int.MAX_VALUE)
    val wireType = tlv and 7
    val value = when (wireType.toInt()) {
      0 -> VarIntValue(stream.nextVarint())
      1 -> I64Value(stream.next64())
      2 -> {
        val len = stream.nextVarint()
        check(len >= 0 && len < Int.MAX_VALUE)
        val bytes = stream.nextBytes(len.toInt())
        LenValue(len.toInt(), bytes)
      }

      5 -> I32Value(stream.next32())
      else -> throw IllegalArgumentException("Failed to parse proto binary")
    }
    pairs.add(MessageEncodingPair(fieldNumber.toInt(), value))
  }
  return MessageEncodingResult(pairs)
}

data class MessageEncodingResult(
  val pairs: List<MessageEncodingPair>
) {
  val tagNumbers: Set<Int> get() = pairs.map { it.tagNumber }.toSet()

  private val grouped = pairs.groupBy { it.tagNumber }

  fun getSingle(tagNumber: Int): Value =
    grouped[tagNumber]?.lastOrNull()?.value ?: throw IllegalArgumentException()

  fun getOptional(tagNumber: Int): Value? =
    grouped[tagNumber]?.lastOrNull()?.value

  fun getRepeated(tagNumber: Int): List<Value> =
    grouped[tagNumber]?.map { it.value } ?: listOf()

  fun calculateSerializedSize(): Int {
    var size = 0
    for ((tagNumber, value) in pairs) {
      size += lengthOfEncodedVarInt(tagNumber shl 3)
      when (value) {
        is VarIntValue -> size += lengthOfEncodedVarInt(value.value)
        is I64Value -> size += 8
        is LenValue -> size += lengthOfEncodedVarInt(value.length) + value.length
        is I32Value -> size += 4
      }
    }
    return size
  }

  fun writeTo(bytes: ByteArray, offset: Int) {

  }

  fun toByteArray(): ByteArray {
    val array = ByteArray(calculateSerializedSize())
    val writer = ProtoOutputStream(array)
    for (pair in pairs) {
      val typeCode = when (pair.value) {
        is VarIntValue -> 0
        is I64Value -> 1
        is LenValue -> 2
        is I32Value -> 5
      }
      writer.writeVarInt((pair.tagNumber.toLong() shl 3) or typeCode.toLong())

      when (pair.value) {
        is VarIntValue -> writer.writeVarInt(pair.value.value)
        is I64Value -> writer.write64(pair.value.value)
        is LenValue -> {
          writer.writeVarInt(pair.value.length.toLong())
          writer.writeBytes(pair.value.bytes)
        }

        is I32Value -> writer.write32(pair.value.value)
      }
    }
    // check if writer reached to the end of array
    return array
  }
}

data class MessageEncodingPair(
  val tagNumber: Int,
  val value: Value
)

sealed class Value
data class VarIntValue(val value: Long): Value() {
  constructor(value: Int): this(value.toLong())
}

data class I64Value(val value: Long): Value()
data class LenValue(val length: Int, val bytes: ByteArray): Value()
data class I32Value(val value: Int): Value()
