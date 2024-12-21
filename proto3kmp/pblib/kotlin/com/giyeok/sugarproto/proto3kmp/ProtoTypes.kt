package com.giyeok.sugarproto.proto3kmp

// ID Name    Used For
// 0  VARINT  int32, int64, uint32, uint64, sint32, sint64, bool, enum
// 1  I64     fixed64, sfixed64, double
// 2  LEN     string, bytes, embedded messages, packed repeated fields
// 3  SGROUP  group start (deprecated)
// 4  EGROUP  group end (deprecated)
// 5  I32     fixed32, sfixed32, float

enum class ProtoEncodingType(val code: Int) {
  VARINT(0),
  I64(1),
  LEN(2),
  I32(5),
}

sealed class ValueType
sealed class ScalarType: ValueType() {
  abstract val byteEncoding: ProtoEncodingType
}

data class Int32Type(val encoding: Int32Encoding): ScalarType() {
  override val byteEncoding: ProtoEncodingType
    get() = encoding.encodingType
}

data class Int64Type(val encoding: Int64Encoding): ScalarType() {
  override val byteEncoding: ProtoEncodingType
    get() = encoding.encodingType
}

data object BoolType: ScalarType() {
  override val byteEncoding: ProtoEncodingType get() = ProtoEncodingType.VARINT
}

data class EnumType(val enumName: String): ScalarType() {
  val canonicalName get() = "$enumName"
  override val byteEncoding: ProtoEncodingType get() = ProtoEncodingType.VARINT
}

data object DoubleType: ScalarType() {
  override val byteEncoding: ProtoEncodingType get() = ProtoEncodingType.I64
}

data object StringType: ValueType()
data object BytesType: ValueType()
data class MessageType(val canonicalName: String): ValueType()

data class PackedRepeatedField(val elemType: ScalarType): ValueType()

data object FloatType: ScalarType() {
  override val byteEncoding: ProtoEncodingType get() = ProtoEncodingType.I32
}

enum class Int32Encoding(val encodingType: ProtoEncodingType, val isUnsigned: Boolean) {
  INT32(ProtoEncodingType.VARINT, false),
  UINT32(ProtoEncodingType.VARINT, true),
  SINT32(ProtoEncodingType.VARINT, false),
  FIXED32(ProtoEncodingType.I32, false),
  SFIXED32(ProtoEncodingType.I32, false),
}

enum class Int64Encoding(val encodingType: ProtoEncodingType, val isUnsigned: Boolean) {
  INT64(ProtoEncodingType.VARINT, false),
  UINT64(ProtoEncodingType.VARINT, true),
  SINT64(ProtoEncodingType.VARINT, false),
  FIXED64(ProtoEncodingType.I64, false),
  SFIXED64(ProtoEncodingType.I64, false)
}
