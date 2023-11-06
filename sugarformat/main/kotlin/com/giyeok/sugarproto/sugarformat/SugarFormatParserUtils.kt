package com.giyeok.sugarproto.sugarformat

import com.giyeok.sugarproto.SugarFormatAst
import com.google.protobuf.Descriptors
import com.google.protobuf.Message
import java.lang.StringBuilder

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

fun SugarFormatAst.ScalarValue.toProtoValue(type: Descriptors.FieldDescriptor.Type): Any =
  when (type) {
    Descriptors.FieldDescriptor.Type.DOUBLE -> when (this) {
      is SugarFormatAst.DecValue ->
        doubleValueFrom(this.sgn, this.integral, this.frac, this.exponent)

      is SugarFormatAst.StringValue -> stringValueFrom(this).toDouble()
      else -> throw IllegalStateException()
    }

    Descriptors.FieldDescriptor.Type.FLOAT -> when (this) {
      is SugarFormatAst.DecValue ->
        doubleValueFrom(this.sgn, this.integral, this.frac, this.exponent).toFloat()

      is SugarFormatAst.StringValue -> stringValueFrom(this).toFloat()
      else -> throw IllegalStateException()
    }

    Descriptors.FieldDescriptor.Type.INT32 -> when (this) {
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

    Descriptors.FieldDescriptor.Type.INT64 -> when (this) {
      is SugarFormatAst.DecValue -> TODO()
      is SugarFormatAst.HexValue -> TODO()
      is SugarFormatAst.OctValue -> TODO()
      is SugarFormatAst.StringValue -> TODO()
      else -> throw IllegalStateException()
    }

    Descriptors.FieldDescriptor.Type.UINT64 -> TODO()
    Descriptors.FieldDescriptor.Type.FIXED64 -> TODO()
    Descriptors.FieldDescriptor.Type.FIXED32 -> TODO()
    Descriptors.FieldDescriptor.Type.BOOL -> TODO()
    Descriptors.FieldDescriptor.Type.STRING -> when (this) {
      is SugarFormatAst.StringValue -> stringValueFrom(this)
      is SugarFormatAst.NameValue -> this.value
      is SugarFormatAst.DurationValue -> TODO()
      is SugarFormatAst.DecValue -> TODO()
      is SugarFormatAst.HexValue -> TODO()
      is SugarFormatAst.OctValue -> TODO()
      is SugarFormatAst.TimestampValue -> TODO()
    }

    Descriptors.FieldDescriptor.Type.GROUP -> TODO()
    Descriptors.FieldDescriptor.Type.MESSAGE -> TODO()
    Descriptors.FieldDescriptor.Type.BYTES -> TODO()
    Descriptors.FieldDescriptor.Type.UINT32 -> TODO()
    Descriptors.FieldDescriptor.Type.ENUM -> TODO()
    Descriptors.FieldDescriptor.Type.SFIXED32 -> TODO()
    Descriptors.FieldDescriptor.Type.SFIXED64 -> TODO()
    Descriptors.FieldDescriptor.Type.SINT32 -> TODO()
    Descriptors.FieldDescriptor.Type.SINT64 -> TODO()
  }

private fun doubleValueFrom(
  sgn: Char?,
  integral: String,
  frac: String?,
  exponent: SugarFormatAst.Exponent?
): Double {
  TODO("Not yet implemented")
}

private fun stringValueFrom(value: SugarFormatAst.StringValue): String = when (value.type) {
  SugarFormatAst.StringTypeAnnot.Base64 -> TODO()
  SugarFormatAst.StringTypeAnnot.Hex -> TODO()
  null -> {
    val stringBuilder = StringBuilder()
    value.fracs.forEach { frac ->
      frac.elems.forEach { elem ->
        when (elem) {
          is SugarFormatAst.PlainText -> stringBuilder.append(elem.value)
          is SugarFormatAst.EscapeCode -> TODO()
          is SugarFormatAst.HexCode -> TODO()
          is SugarFormatAst.OctCode -> TODO()
          is SugarFormatAst.Unicode -> TODO()
        }
      }
    }
    stringBuilder.toString()
  }
}