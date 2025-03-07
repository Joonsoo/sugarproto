package com.giyeok.sugarproto.sugarformat

import com.giyeok.sugarproto.SugarFormatAst
import com.google.protobuf.ByteString
import com.google.protobuf.Descriptors
import com.google.protobuf.Descriptors.FieldDescriptor
import com.google.protobuf.Duration
import com.google.protobuf.Message
import com.google.protobuf.Timestamp
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

fun setFieldValue(
  builder: Message.Builder,
  first: SugarFormatAst.KeyValue,
  rest: List<SugarFormatAst.KeyValue>,
  value: SugarFormatAst.ScalarValue
) {
  if (rest.isNotEmpty()) {
    TODO()
  } else {
    val fieldDesc = when (first) {
      is SugarFormatAst.NameKey ->
        builder.descriptorForType.findFieldByName(first.name)

      is SugarFormatAst.DecValue -> TODO()
      is SugarFormatAst.HexValue -> TODO()
      is SugarFormatAst.OctValue -> TODO()
      is SugarFormatAst.StringFrac -> TODO()
    }
    checkNotNull(fieldDesc)
    builder.setField(fieldDesc, value.toProtoValue(fieldDesc.type))
  }
}

fun SugarFormatAst.ScalarValue.toProtoValue(type: FieldDescriptor.Type): Any =
  when (type) {
    FieldDescriptor.Type.DOUBLE -> when (this) {
      is SugarFormatAst.DecValue ->
        doubleValueFrom(this.sgn, this.integral, this.frac, this.exponent)

      is SugarFormatAst.StringValue -> stringValueFrom(this).toDouble()
      else -> throw IllegalStateException()
    }

    FieldDescriptor.Type.FLOAT -> when (this) {
      is SugarFormatAst.DecValue ->
        doubleValueFrom(this.sgn, this.integral, this.frac, this.exponent).toFloat()

      is SugarFormatAst.StringValue -> stringValueFrom(this).toFloat()
      else -> throw IllegalStateException()
    }

    FieldDescriptor.Type.INT32 -> when (this) {
      is SugarFormatAst.DecValue -> {
        check(this.frac == null)
        check(this.exponent == null)
        // TODO sgn
        this.integral.toInt()
      }

      is SugarFormatAst.HexValue ->
        // TODO sgn
        this.value.toInt(16)

      is SugarFormatAst.OctValue ->
        // TODO sgn
        this.value.toInt(8)

      is SugarFormatAst.StringValue -> stringValueFrom(this).toInt()
      else -> throw IllegalStateException()
    }

    FieldDescriptor.Type.INT64 -> when (this) {
      is SugarFormatAst.DecValue -> TODO()
      is SugarFormatAst.HexValue -> TODO()
      is SugarFormatAst.OctValue -> TODO()
      is SugarFormatAst.StringValue -> TODO()
      else -> throw IllegalStateException()
    }

    FieldDescriptor.Type.UINT64 -> TODO()
    FieldDescriptor.Type.FIXED64 -> TODO()
    FieldDescriptor.Type.FIXED32 -> TODO()
    FieldDescriptor.Type.BOOL -> TODO()
    FieldDescriptor.Type.STRING -> when (this) {
      is SugarFormatAst.StringValue -> stringValueFrom(this)
      is SugarFormatAst.NameValue -> this.value
      is SugarFormatAst.DurationValue -> TODO()
      is SugarFormatAst.DecValue -> TODO()
      is SugarFormatAst.HexValue -> TODO()
      is SugarFormatAst.OctValue -> TODO()
      is SugarFormatAst.TimestampValue -> TODO()
    }

    FieldDescriptor.Type.GROUP -> TODO()
    FieldDescriptor.Type.MESSAGE -> TODO()
    FieldDescriptor.Type.BYTES -> TODO()
    FieldDescriptor.Type.UINT32 -> TODO()
    FieldDescriptor.Type.ENUM -> TODO()
    FieldDescriptor.Type.SFIXED32 -> TODO()
    FieldDescriptor.Type.SFIXED64 -> TODO()
    FieldDescriptor.Type.SINT32 -> TODO()
    FieldDescriptor.Type.SINT64 -> TODO()
  }

private fun doubleValueFrom(
  sgn: Char?,
  integral: String,
  frac: String?,
  exponent: SugarFormatAst.Exponent?
): Double {
  TODO("Not yet implemented")
}

fun SugarFormatAst.StringFrac.appendToStringBuilder(stringBuilder: StringBuilder) {
  this.elems.forEach { elem ->
    when (elem) {
      is SugarFormatAst.PlainText -> stringBuilder.append(elem.value)
      is SugarFormatAst.EscapeCode -> {
        val chr = when (elem.code) {
          'n' -> '\n'
          else -> TODO()
        }
        stringBuilder.append(chr)
      }

      is SugarFormatAst.HexCode -> TODO()
      is SugarFormatAst.OctCode -> TODO()
      is SugarFormatAst.Unicode -> TODO()
    }
  }
}

fun SugarFormatAst.StringFrac.toStringValue(): String {
  val stringBuilder = StringBuilder()
  this.appendToStringBuilder(stringBuilder)
  return stringBuilder.toString()
}

fun stringValueFrom(value: SugarFormatAst.StringValue): String = when (value.type) {
  SugarFormatAst.StringTypeAnnot.Base64 -> {
    TODO()
  }

  SugarFormatAst.StringTypeAnnot.Hex -> {
    TODO()
  }

  null -> {
    val stringBuilder = StringBuilder()
    value.fracs.forEach { frac ->
      frac.appendToStringBuilder(stringBuilder)
    }
    stringBuilder.toString()
  }
}

fun bytesValueFrom(value: SugarFormatAst.StringValue): ByteString = when (value.type) {
  SugarFormatAst.StringTypeAnnot.Base64 -> {
    check(value.fracs.size == 1)
    ByteString.copyFrom(Base64.getDecoder().decode(value.fracs.first().toStringValue()))
  }

  SugarFormatAst.StringTypeAnnot.Hex -> {
    check(value.fracs.size == 1)
    ByteString.fromHex(value.fracs.first().toStringValue())
  }

  null -> {
    ByteString.copyFromUtf8(stringValueFrom(value))
  }
}

fun SugarFormatAst.TimestampValue.toProtoValue(): Timestamp {
  val cal = Calendar.getInstance()
  cal.timeZone = TimeZone.getTimeZone("UTC")
  cal.set(
    this.date.year.toInt(), this.date.month.toInt() - 1, this.date.day.toInt(),
    this.time?.hour?.toInt() ?: 0, this.time?.minute?.toInt() ?: 0, this.time?.second?.toInt() ?: 0
  )
  val timestamp = Timestamp.newBuilder()
  timestamp.seconds = cal.toInstant().epochSecond
  this.time?.secondFrac?.let { secondFrac ->
    timestamp.nanos = secondFrac.padEnd(9, '0').toInt()
  }
  return timestamp.build()
}

fun SugarFormatAst.DurationValue.toProtoValue(): Duration {
  val duration = Duration.newBuilder()
  duration.seconds = (this.days?.toLong() ?: 0) * (24 * 3600) +
    (this.hours?.toInt() ?: 0) * 3600 +
    (this.minutes?.toInt() ?: 0) * 60 +
    (this.seconds?.integral?.toInt() ?: 0)
  this.seconds?.frac?.let { frac ->
    duration.nanos = frac.padEnd(9, '0').toInt()
  }
  return duration.build()
}

fun String.canOmitQuotes(): Boolean {
  if (this == "true" || this == "false") {
    return false
  }
  if (!this.all { chr -> chr in '0'..'9' || chr in 'a'..'z' || chr in 'A'..'Z' || chr == '_' }) {
    return false
  }
  if (this.first() in '0'..'9') {
    return false
  }
  return true
}

fun String.escape(): String = this.replace("\n", "\\n")

fun printStringOf(type: FieldDescriptor.Type, value: Any): List<String> = when (type) {
  Descriptors.FieldDescriptor.Type.STRING -> {
    value as String
    if (value.canOmitQuotes()) listOf(value) else {
      val lines = value.split('\n')
      (lines.dropLast(1).map { "$it\n" } + lines.last()).map { "\"${it.escape()}\"" }
    }
  }

  Descriptors.FieldDescriptor.Type.BYTES -> {
    check(value is ByteString)
    // TODO utf8로 바꿀 수 있으면 일반 스트링으로
    val valueBytes = value.toByteArray()
    val base64 = Base64.getEncoder().withoutPadding().encodeToString(valueBytes)
    listOf("b\"${base64}\"")
  }

  Descriptors.FieldDescriptor.Type.INT32, Descriptors.FieldDescriptor.Type.INT64,
  Descriptors.FieldDescriptor.Type.ENUM -> listOf("$value")

  Descriptors.FieldDescriptor.Type.FLOAT,
  Descriptors.FieldDescriptor.Type.DOUBLE -> listOf("$value")

  Descriptors.FieldDescriptor.Type.UINT32,
  Descriptors.FieldDescriptor.Type.UINT64,
  Descriptors.FieldDescriptor.Type.SFIXED32,
  Descriptors.FieldDescriptor.Type.SFIXED64,
  Descriptors.FieldDescriptor.Type.SINT32,
  Descriptors.FieldDescriptor.Type.SINT64,
  Descriptors.FieldDescriptor.Type.FIXED32,
  Descriptors.FieldDescriptor.Type.FIXED64 -> listOf("$value")

  Descriptors.FieldDescriptor.Type.BOOL -> listOf("$value")

  Descriptors.FieldDescriptor.Type.GROUP,
  Descriptors.FieldDescriptor.Type.MESSAGE -> TODO()
}

fun timestampStringOf(ts: Timestamp): String {
  val instant = Instant.ofEpochSecond(ts.seconds, ts.nanos.toLong())
  return instant.atOffset(ZoneOffset.UTC).toString()
}

fun durationStringOf(dur: Duration): String {
  var leftSeconds = dur.seconds
  val builder = StringBuilder()
  val days = leftSeconds / (24 * 3600)
  if (days > 0) {
    builder.append("${days}d")
    leftSeconds -= 24 * 3600 * days
  }
  val hours = leftSeconds / 3600
  if (hours > 0) {
    builder.append("${hours}h")
    leftSeconds -= 3600 * hours
  }
  val mins = leftSeconds / 60
  if (mins > 0) {
    builder.append("${mins}m")
    leftSeconds -= 60 * mins
  }
  if (leftSeconds > 0) {
    builder.append(leftSeconds)
    if (dur.nanos == 0) {
      builder.append("s")
    }
  } else if (dur.nanos != 0) {
    builder.append("0")
  }
  if (dur.nanos != 0) {
    val fracVal = dur.nanos.toString().padStart(9, '0').trimEnd { it == '0' }
    builder.append(".")
    builder.append(fracVal)
    builder.append("s")
  }
  return builder.toString()
}