package com.giyeok.sugarproto.sugarformat

import com.giyeok.sugarproto.SugarFormatAst
import com.giyeok.sugarproto.sugarformat.ParsedValueBuilder.Companion.messageValueBuilderFor
import com.google.protobuf.ByteString
import com.google.protobuf.Descriptors.Descriptor
import com.google.protobuf.Descriptors.FieldDescriptor
import com.google.protobuf.Duration
import com.google.protobuf.Message
import com.google.protobuf.Timestamp

class SugarFormatParserImpl(val st: ItemStructure) {
  fun parse(messageTypeDesc: Descriptor): ParsedValueBuilder.MessageValueBuilder {
    val builder = messageValueBuilderFor(messageTypeDesc)
    // TODO builder는 timestamp/duration builder일 수도 있다
    check(builder is ParsedValueBuilder.MessageValueBuilder)
    parseMessage(builder, st.all.siblingsOfFirst())
    return builder
  }

  fun parseMessage(
    builder: ParsedValueBuilder.MessageValueBuilder,
    fields: List<ItemStructure.Range>
  ) {
    fields.forEach { sibling ->
      val headItem = when (val head = sibling.head) {
        is SugarFormatAst.SingleItem -> head
        is SugarFormatAst.ListFieldItem -> head.item
        is SugarFormatAst.ListValueItem -> throw IllegalStateException()
      }


      val fieldBuilder = builder.followPath(headItem.key.path)
      when (val value = headItem.value) {
        is SugarFormatAst.Header -> {
          when (fieldBuilder) {
            is ParsedValueBuilder.MessageValueBuilder -> {
              parseMessage(fieldBuilder, sibling.childrenOfFirst())
            }

            is ParsedValueBuilder.RepeatedValueBuilder -> {
              parseRepeated(fieldBuilder, sibling.listChildrenOfFirst())
            }

            is ParsedValueBuilder.MapValueBuilder -> TODO()
            is ParsedValueBuilder.SingularValueBuilder -> TODO()
            is ParsedValueBuilder.TimestampValueBuilder -> TODO()
            is ParsedValueBuilder.DurationValueBuilder -> TODO()
          }
        }

        is SugarFormatAst.Value -> {
          setFieldValue(fieldBuilder, value)
        }
      }
    }
  }

  fun parseRepeated(
    builder: ParsedValueBuilder.RepeatedValueBuilder,
    elems: List<ItemStructure.Range>
  ) {
    elems.forEach { elem ->
      val elemBuilder = builder.addElemBuilder()
      when (val head = elem.head) {
        is SugarFormatAst.SingleItem -> {
          throw IllegalStateException()
        }

        is SugarFormatAst.ListFieldItem -> {
          check(elemBuilder is ParsedValueBuilder.MessageValueBuilder)
          // sibling.head보다는 indent가 들어가 있는 것들 찾기
          val elemFields = elem.siblingsOfFirstListField()
          parseMessage(elemBuilder, elemFields)
        }

        is SugarFormatAst.ListValueItem ->
          setFieldValue(elemBuilder, head.value)
      }
    }
  }

  fun setFieldValue(fieldBuilder: ParsedValueBuilder, value: SugarFormatAst.Value) {
    when (fieldBuilder) {
      is ParsedValueBuilder.SingularValueBuilder -> {
        when (value) {
          is SugarFormatAst.StringValue -> {
            // TODO fieldBuilder가 string이 아니어도 적당히 바꿔서 넣어주기
            when (fieldBuilder.fieldDesc.type) {
              FieldDescriptor.Type.STRING -> {
                fieldBuilder.value = stringValueFrom(value)
              }

              FieldDescriptor.Type.BYTES -> {
                fieldBuilder.value = bytesValueFrom(value)
              }

              FieldDescriptor.Type.DOUBLE -> TODO()
              FieldDescriptor.Type.FLOAT -> TODO()
              FieldDescriptor.Type.INT64 -> TODO()
              FieldDescriptor.Type.UINT64 -> TODO()
              FieldDescriptor.Type.INT32 -> TODO()
              FieldDescriptor.Type.FIXED64 -> TODO()
              FieldDescriptor.Type.FIXED32 -> TODO()
              FieldDescriptor.Type.BOOL -> TODO()
              FieldDescriptor.Type.GROUP -> TODO()
              FieldDescriptor.Type.MESSAGE -> TODO()
              FieldDescriptor.Type.UINT32 -> TODO()
              FieldDescriptor.Type.ENUM -> TODO()
              FieldDescriptor.Type.SFIXED32 -> TODO()
              FieldDescriptor.Type.SFIXED64 -> TODO()
              FieldDescriptor.Type.SINT32 -> TODO()
              FieldDescriptor.Type.SINT64 -> TODO()
            }
          }

          is SugarFormatAst.NameValue -> {
            when (fieldBuilder.fieldDesc.type) {
              FieldDescriptor.Type.STRING -> {
                fieldBuilder.value = value.value
              }

              FieldDescriptor.Type.ENUM -> {
                val enumType = fieldBuilder.fieldDesc.enumType
                val enumValue = enumType.values.find { it.name == value.value }
                checkNotNull(enumValue) { "Invalid enum value: ${value.value}" }
                fieldBuilder.value = enumValue
              }

              FieldDescriptor.Type.DOUBLE -> TODO()
              FieldDescriptor.Type.FLOAT -> TODO()
              FieldDescriptor.Type.INT64 -> TODO()
              FieldDescriptor.Type.UINT64 -> TODO()
              FieldDescriptor.Type.INT32 -> TODO()
              FieldDescriptor.Type.FIXED64 -> TODO()
              FieldDescriptor.Type.FIXED32 -> TODO()
              FieldDescriptor.Type.BOOL -> TODO()
              FieldDescriptor.Type.GROUP -> TODO()
              FieldDescriptor.Type.MESSAGE -> TODO()
              FieldDescriptor.Type.BYTES -> TODO()
              FieldDescriptor.Type.UINT32 -> TODO()
              FieldDescriptor.Type.SFIXED32 -> TODO()
              FieldDescriptor.Type.SFIXED64 -> TODO()
              FieldDescriptor.Type.SINT32 -> TODO()
              FieldDescriptor.Type.SINT64 -> TODO()
            }
          }

          is SugarFormatAst.ObjectOrMapValue -> TODO()
          is SugarFormatAst.RepeatedValue -> TODO()
          is SugarFormatAst.DurationValue -> TODO()
          is SugarFormatAst.DecValue -> {
            when (fieldBuilder.fieldDesc.type) {
              FieldDescriptor.Type.INT32 -> {
                // TODO sgn
                check(value.frac == null && value.exponent == null)
                fieldBuilder.value = value.integral.toInt()
              }

              FieldDescriptor.Type.INT64 -> {
                // TODO sgn
                check(value.frac == null && value.exponent == null)
                fieldBuilder.value = value.integral.toLong()
              }

              FieldDescriptor.Type.DOUBLE -> TODO()
              FieldDescriptor.Type.FLOAT -> TODO()
              FieldDescriptor.Type.UINT64 -> TODO()
              FieldDescriptor.Type.FIXED64 -> TODO()
              FieldDescriptor.Type.FIXED32 -> TODO()
              FieldDescriptor.Type.BOOL -> TODO()
              FieldDescriptor.Type.STRING -> TODO()
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
          }

          is SugarFormatAst.HexValue -> TODO()
          is SugarFormatAst.OctValue -> TODO()
          is SugarFormatAst.TimestampValue -> TODO()
        }
      }

      is ParsedValueBuilder.MapValueBuilder -> TODO()
      is ParsedValueBuilder.MessageValueBuilder -> {
        when (value) {
          is SugarFormatAst.ObjectOrMapValue -> {
            for ((key, fieldValue) in value.pairs) {
              setFieldValue(fieldBuilder.followPath(key.path), fieldValue)
            }
          }

          is SugarFormatAst.RepeatedValue -> TODO()
          is SugarFormatAst.NameValue -> TODO()
          is SugarFormatAst.DecValue -> TODO()
          is SugarFormatAst.HexValue -> TODO()
          is SugarFormatAst.OctValue -> TODO()
          is SugarFormatAst.StringValue -> TODO()
          is SugarFormatAst.TimestampValue -> TODO()
          is SugarFormatAst.DurationValue -> TODO()
        }
      }

      is ParsedValueBuilder.RepeatedValueBuilder -> {
        when (value) {
          is SugarFormatAst.ObjectOrMapValue -> TODO()
          is SugarFormatAst.RepeatedValue -> {
            value.elems.forEach { elem ->
              setFieldValue(fieldBuilder.addElemBuilder(), elem)
            }
          }

          is SugarFormatAst.DurationValue -> TODO()
          is SugarFormatAst.NameValue -> TODO()
          is SugarFormatAst.DecValue -> TODO()
          is SugarFormatAst.HexValue -> TODO()
          is SugarFormatAst.OctValue -> TODO()
          is SugarFormatAst.StringValue -> TODO()
          is SugarFormatAst.TimestampValue -> TODO()
        }
      }

      is ParsedValueBuilder.TimestampValueBuilder -> {
        when (value) {
          is SugarFormatAst.TimestampValue -> {
            fieldBuilder.builder.mergeFrom(value.toProtoValue())
          }

          else -> TODO()
        }
      }

      is ParsedValueBuilder.DurationValueBuilder -> {
        when (value) {
          is SugarFormatAst.DurationValue -> {
            fieldBuilder.builder.mergeFrom(value.toProtoValue())
          }

          else -> TODO()
        }
      }
    }
  }

  private fun parse(range: ItemStructure.Range, messageTypeDesc: Descriptor) {
    TODO()
  }
}

sealed class ParsingBuilder {
  class MessageBuilder(val typeDesc: Descriptor): ParsingBuilder()
}

sealed class ParsedValueBuilder {
  abstract fun followPath(path: List<SugarFormatAst.KeyValue>): ParsedValueBuilder

  companion object {
    fun messageValueBuilderFor(type: Descriptor): ParsedValueBuilder =
      when (type.fullName) {
        "google.protobuf.Timestamp" -> TimestampValueBuilder(Timestamp.newBuilder())
        "google.protobuf.Duration" -> DurationValueBuilder(Duration.newBuilder())
        else -> MessageValueBuilder(type, mutableMapOf())
      }
  }

  data class MessageValueBuilder(
    val messageTypeDesc: Descriptor,
    val fields: MutableMap<String, Pair<FieldDescriptor, ParsedValueBuilder>>
  ): ParsedValueBuilder() {
    override fun followPath(path: List<SugarFormatAst.KeyValue>): ParsedValueBuilder {
      check(path.isNotEmpty())
      val following = when (val first = path.first()) {
        is SugarFormatAst.NameKey -> {
          val field = messageTypeDesc.findFieldByName(first.name)
          checkNotNull(field) { "Field name not found: ${first.name}" }
          val fieldBuilder = when {
            field.isMapField -> TODO()
            field.isRepeated -> RepeatedValueBuilder(field, mutableListOf())
            else ->
              if (field.type == FieldDescriptor.Type.MESSAGE) {
                messageValueBuilderFor(field.messageType)
              } else {
                SingularValueBuilder(field, null)
              }
          }
          check(first.name !in fields) { "Duplicate name: ${first.name}" }
          fields[first.name] = field to fieldBuilder
          fieldBuilder
        }

        is SugarFormatAst.DecValue -> TODO()
        is SugarFormatAst.HexValue -> TODO()
        is SugarFormatAst.OctValue -> TODO()
        is SugarFormatAst.StringFrac -> TODO()
      }
      return if (path.size == 1) following else following.followPath(path.drop(1))
    }

    fun Message.Builder.addRepeatedBuilderForField(field: FieldDescriptor): Message.Builder {
      val emptyValue = this.newBuilderForField(field).build()
      val index = this.getRepeatedFieldCount(field)
      this.addRepeatedField(field, emptyValue)
      return getRepeatedFieldBuilder(field, index)
    }

    fun mergeTo(builder: Message.Builder) {
      check(builder.descriptorForType == messageTypeDesc)
      fields.forEach { (_, pair) ->
        val (field, parsed) = pair
        when (parsed) {
          is SingularValueBuilder -> {
            parsed.setTo(builder, field)
          }

          is MessageValueBuilder -> {
            parsed.mergeTo(builder.getFieldBuilder(field))
          }

          is RepeatedValueBuilder -> {
            if (parsed.elemTypeDesc.type == FieldDescriptor.Type.MESSAGE) {
              when (parsed.elemTypeDesc.messageType.fullName) {
                "google.protobuf.Timestamp" ->
                  parsed.elems.forEach { elem ->
                    check(elem is TimestampValueBuilder)
                    elem.setTo(builder.addRepeatedBuilderForField(field))
                  }

                "google.protobuf.Duration" ->
                  parsed.elems.forEach { elem ->
                    check(elem is DurationValueBuilder)
                    elem.setTo(builder.addRepeatedBuilderForField(field))
                  }

                else -> {
                  parsed.elems.forEach { elem ->
                    check(elem is MessageValueBuilder)
                    val elemBuilder = builder.addRepeatedBuilderForField(field)
                    elem.mergeTo(elemBuilder)
                  }
                }
              }
            } else {
              parsed.elems.forEach { elem ->
                check(elem is SingularValueBuilder)
                elem.addRepeatedTo(builder, parsed.elemTypeDesc)
              }
            }
          }

          is MapValueBuilder -> TODO()

          is TimestampValueBuilder -> {
            parsed.setTo(builder.getFieldBuilder(field))
          }

          is DurationValueBuilder -> {
            parsed.setTo(builder.getFieldBuilder(field))
          }
        }
      }
    }
  }

  data class RepeatedValueBuilder(
    val elemTypeDesc: FieldDescriptor,
    val elems: MutableList<ParsedValueBuilder>
  ): ParsedValueBuilder() {
    override fun followPath(path: List<SugarFormatAst.KeyValue>): ParsedValueBuilder {
      throw IllegalStateException()
    }

    fun addElemBuilder(): ParsedValueBuilder {
      val builder = if (elemTypeDesc.type == FieldDescriptor.Type.MESSAGE) {
        messageValueBuilderFor(elemTypeDesc.messageType)
      } else {
        SingularValueBuilder(elemTypeDesc, null)
      }
      elems.add(builder)
      return builder
    }
  }

  data class MapValueBuilder(
    val elems: MutableList<Pair<ParsedValueBuilder, ParsedValueBuilder>>
  ): ParsedValueBuilder() {
    override fun followPath(path: List<SugarFormatAst.KeyValue>): ParsedValueBuilder {
      TODO()
    }
  }

  data class SingularValueBuilder(
    val fieldDesc: FieldDescriptor,
    var value: Any?
  ): ParsedValueBuilder() {
    override fun followPath(path: List<SugarFormatAst.KeyValue>): ParsedValueBuilder {
      throw IllegalStateException()
    }

    fun setTo(builder: Message.Builder, field: FieldDescriptor) {
      checkNotNull(value)
      builder.setField(field, value!!)
    }

    fun addRepeatedTo(builder: Message.Builder, field: FieldDescriptor) {
      builder.addRepeatedField(field, value!!)
    }
  }

  data class TimestampValueBuilder(val builder: Timestamp.Builder): ParsedValueBuilder() {
    override fun followPath(path: List<SugarFormatAst.KeyValue>): ParsedValueBuilder {
      TODO("Not yet implemented")
    }

    fun setTo(builder: Message.Builder) {
      builder.mergeFrom(this.builder.build())
    }
  }

  data class DurationValueBuilder(val builder: Duration.Builder): ParsedValueBuilder() {
    override fun followPath(path: List<SugarFormatAst.KeyValue>): ParsedValueBuilder {
      TODO("Not yet implemented")
    }

    fun setTo(builder: Message.Builder) {
      builder.mergeFrom(this.builder.build())
    }
  }
}
