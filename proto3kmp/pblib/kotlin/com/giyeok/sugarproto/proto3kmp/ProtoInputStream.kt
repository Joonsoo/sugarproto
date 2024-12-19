package com.giyeok.sugarproto.proto3kmp

class ProtoInputStream(val bytes: ByteArray, offset: Int = 0) {
  private var pos: Int = offset

  fun hasNext(): Boolean {
    TODO()
  }

  fun nextVarint(): Long {
    TODO()
  }

  fun next32(): Int {
    TODO()
  }

  fun next64(): Long {
    TODO()
  }

  fun nextFloat(): Float {
    TODO()
  }

  fun nextDouble(): Double {
    TODO()
  }

  fun nextBytes(length: Int): ByteArray {
    TODO()
  }
}
