package com.giyeok.sugarproto.proto3kmp

class ProtoInputStream(val bytes: ByteArray, offset: Int = 0) {
  private var pos: Int = offset

  fun hasNext(): Boolean = pos < bytes.size

  fun nextVarint(): Long {
    var value = 0L
    var byte = bytes[pos++].toLong()
    value = value or (byte and 0x7fL)
    var offset = 0
    while (byte and 0x80L != 0L) {
      byte = bytes[pos++].toLong()
      offset += 1
      value = value or ((byte and 0x7fL) shl (offset * 7))
    }
    return value
  }

  fun next64(): Long {
    var value = 0L
    repeat(8) { i ->
      value = value or ((bytes[pos++].toLong() and 0xff) shl (i * 8))
    }
    return value
  }

  fun next32(): Int {
    var value = 0
    repeat(8) { i ->
      value = value or ((bytes[pos++].toInt() and 0xff) shl (i * 8))
    }
    return value
  }

  fun nextFloat(): Float {
    return Float.fromBits(next32())
  }

  fun nextDouble(): Double {
    return Double.fromBits(next64())
  }

  fun nextBytes(length: Int): ByteArray {
    val copy = bytes.copyOfRange(pos, pos + length)
    pos += length
    return copy
  }
}
