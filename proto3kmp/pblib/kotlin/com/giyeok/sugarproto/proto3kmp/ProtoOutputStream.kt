package com.giyeok.sugarproto.proto3kmp

class ProtoOutputStream(val array: ByteArray) {
  var pos = 0

  fun writeVarInt(value: Long) {
    var v = value.toULong()
    val nextBits = v and 0x7fu
    v = v shr 7
    val nextByte = if (v > 0u) nextBits or 0x80u else nextBits
    array[pos++] = nextByte.toByte()
    while (v > 0u) {
      val nextBits = v and 0x7fu
      v = v shr 7
      val nextByte = if (v > 0u) nextBits or 0x80u else nextBits
      array[pos++] = nextByte.toByte()
    }
  }

  fun write64(value: Long) {
    var v = value
    repeat(8) {
      array[pos++] = (v and 0xff).toByte()
      v = v shr 8
    }
  }

  fun write32(value: Int) {
    var v = value
    repeat(4) {
      array[pos++] = (v and 0xff).toByte()
      v = v shr 8
    }
  }

  fun writeBytes(bytes: ByteArray) {
    bytes.copyInto(array, pos)
    pos += bytes.size
  }
}

fun lengthOfEncodedVarInt(value: Long): Int {
  var ulong = value.toULong()
  var bytes = 1
  ulong = ulong shr 7
  while (ulong > 0u) {
    bytes += 1
    ulong = ulong shr 7
  }
  return bytes
}

fun lengthOfEncodedVarInt(value: Int): Int = lengthOfEncodedVarInt(value.toLong())
