package com.giyeok.sugarproto.proto3kmp

class ProtoOutputStream(val array: ByteArray) {
  fun writeVarint(value: Long) {

  }

  fun write64(value: Long) {

  }

  fun write32(value: Int) {

  }

  fun writeBytes(bytes: ByteArray) {

  }
}

fun lengthOfEncodedVarint(value: Long): Int {
  TODO()
}

fun lengthOfEncodedVarint(value: Int): Int = lengthOfEncodedVarint(value.toLong())
