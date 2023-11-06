package com.giyeok.sugarproto.sugarformat

import com.giyeok.sugarproto.SugarFormatAst
import com.google.protobuf.Descriptors.FieldDescriptor
import com.google.protobuf.Message
import com.google.protobuf.TextFormat
import com.google.protobuf.util.JsonFormat

sealed interface BuilderSetter {
  companion object {
    fun lookup(bs: BuilderSetter, path: SugarFormatAst.ItemPath): BuilderSetter =
      // lookup(bs, path.first, path.access)
      TODO()

    fun lookup(
      bs: BuilderSetter,
      first: SugarFormatAst.KeyValue,
      access: List<SugarFormatAst.KeyValue>
    ): BuilderSetter = when (bs) {
      is ScalarSetter -> throw IllegalStateException()
      is MessageBuilder -> {
        val builder = bs.builder
        val typeDesc = builder.descriptorForType
        if (access.isNotEmpty()) {
          TODO()
        } else {
          when (first) {
            is SugarFormatAst.NameKey -> {
              val field = typeDesc.findFieldByName(first.name)
              check(field != null) { "Field not found: ${first.name}" }
              when {
                field.isRepeated ->
                  if (field.type == FieldDescriptor.Type.MESSAGE) {
                    MessageRepeatedBuilder(builder, field)
                  } else {
                    ScalarRepeatedBuilder(builder, field)
                  }

                field.isMapField -> {
                  // value type에 따라 MessageMapBuilder or ScalarMapBuilder
                  TODO()
                }

                else ->
                  if (field.type == FieldDescriptor.Type.MESSAGE) {
                    MessageBuilder(builder.getFieldBuilder(field))
                  } else {
                    ScalarFieldSetter(builder, field)
                  }
              }
            }

            is SugarFormatAst.DecValue -> TODO()
            is SugarFormatAst.HexValue -> TODO()
            is SugarFormatAst.OctValue -> TODO()
            is SugarFormatAst.StringFrac -> TODO()
          }
        }
      }

      else -> TODO()
    }
  }

  sealed interface ScalarSetter: BuilderSetter {
    fun setScalarValue(value: SugarFormatAst.ItemValue)
  }

  data class ScalarFieldSetter(
    val builder: Message.Builder,
    val field: FieldDescriptor,
  ): ScalarSetter {
    override fun setScalarValue(value: SugarFormatAst.ItemValue) {
      check(value is SugarFormatAst.ScalarValue)
      builder.setField(field, value.toProtoValue(field.type))
    }
  }

  data class MessageBuilder(
    val builder: Message.Builder,
  ): BuilderSetter

  sealed interface RepeatedBuilder: BuilderSetter {
    fun addRepeatedValue(value: SugarFormatAst.ItemValue)

    fun setRepeatedValues(values: SugarFormatAst.RepeatedValue) {
      values.elems.forEach { value ->
        addRepeatedValue(value)
      }
    }
  }

  data class ScalarRepeatedBuilder(
    val builder: Message.Builder,
    val field: FieldDescriptor,
  ): ScalarSetter, RepeatedBuilder {
    override fun setScalarValue(value: SugarFormatAst.ItemValue) {
      check(value is SugarFormatAst.ScalarValue)
      builder.addRepeatedField(field, value.toProtoValue(field.type))
    }

    override fun addRepeatedValue(value: SugarFormatAst.ItemValue) {
      check(value is SugarFormatAst.ScalarValue)
      builder.addRepeatedField(field, value.toProtoValue(field.type))
    }
  }

  data class MessageRepeatedBuilder(
    val builder: Message.Builder,
    val field: FieldDescriptor,
  ): RepeatedBuilder {
    override fun addRepeatedValue(value: SugarFormatAst.ItemValue) {
      // builder.addRepeatedField(field, value)
      TODO()
    }

    fun addValueBuilder(): MessageBuilder {
      // TODO
      val emptyValue = builder.newBuilderForField(field)
      val index = builder.getRepeatedFieldCount(field)
      builder.addRepeatedField(field, emptyValue.build())
      val valueBuilder = builder.getRepeatedFieldBuilder(field, index)
      // MessageBuilder(builder.getRepeatedFieldBuilder(field, builder.getRepeatedFieldCount(field)))
      return MessageBuilder(valueBuilder)
    }
  }

  sealed interface MapBuilder: BuilderSetter

  data class ScalarMapBuilder(
    val builder: Message.Builder,
    val field: FieldDescriptor
  ): MapBuilder {
    fun put(key: Any, value: Any) {
      // builder.addRepeatedField(field)
    }
  }

  data class MessageMapBuilder(
    val builder: Message.Builder,
    val field: FieldDescriptor
  ): MapBuilder {
    fun put(key: Any, value: Any) {
      // builder.addRepeatedField(field)
    }
  }
}
