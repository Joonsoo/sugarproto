package com.giyeok.sugarproto.sugarformat

import com.giyeok.sugarproto.SugarFormatAst
import com.google.protobuf.*
import com.google.protobuf.Descriptors.FieldDescriptor
import com.google.protobuf.Message.Builder

object SugarFormat {
  fun print(message: MessageOrBuilder): String {
    val writer = CodeWriter()
    Printer(writer).print(message)
    return writer.toString()
  }

  class Printer(val writer: CodeWriter) {
    fun printField(field: FieldDescriptor, value: Any) {

    }

    fun print(message: MessageOrBuilder) {
      message.allFields.forEach { (field, value) ->
        when {
          field.isMapField -> TODO()

          field.isRepeated -> {
            writer.writeLine("${field.name}:")
            if (field.type == Descriptors.FieldDescriptor.Type.MESSAGE) {
              when (field.messageType.fullName) {
                "google.protobuf.Timestamp" -> {
                  val coll = value as List<*>
                  coll.forEach { elemValue ->
                    check(elemValue is Timestamp)
                    writer.writeLine("- ${timestampStringOf(elemValue)}")
                  }
                }

                "google.protobuf.Duration" -> {
                  val coll = value as List<*>
                  coll.forEach { elemValue ->
                    val duration = elemValue as Duration
                    writer.writeLine("- ${durationStringOf(duration)}")
                  }
                }

                else -> {
                  writer.indent {
                    val coll = value as List<*>
                    coll.forEach { elemValue ->
                      check(elemValue is Message)
                      val entries = elemValue.allFields.entries.toList()
                      // TODO elemValue의 첫번쨰 필드는 - 뒤에
                      //   나머지 필드는 두칸 띄워서
                      writer.writeLine("- ${entries.first()}")
                      entries.drop(1).forEach { entry ->
                        writer.writeLine("  $entry")
                      }
                    }
                  }
                }
              }
            } else {
              check(value is Collection<*>)
              value.forEach { elem ->
                writer.writeLine("- ${printStringOf(field.type, elem!!)}")
              }
            }
          }

          else -> {
            if (field.type == Descriptors.FieldDescriptor.Type.MESSAGE) {
              when (field.messageType.fullName) {
                "google.protobuf.Timestamp" -> {
                  val ts = value as Timestamp
                  writer.writeLine("${field.name}: ${timestampStringOf(ts)}")
                }

                "google.protobuf.Duration" -> {
                  val duration = value as Duration
                  writer.writeLine("${field.name}: ${durationStringOf(duration)}")
                }

                else -> {
                  writer.writeLine("${field.name}:")
                  writer.indent {
                    check(value is Message)
                    print(value)
                  }
                }
              }
            } else {
              val valueString = printStringOf(field.type, value)
              writer.writeLine("${field.name}: $valueString")
            }
          }
        }
      }
    }
  }

  fun <T: Message.Builder> merge(sufText: String, builder: T): T {
    val parsed = SugarFormatParser.parse(sufText)
    Parser(ItemStructure(parsed)).merge(builder)
    return builder
  }

  class Parser(val struct: ItemStructure) {
    fun merge(builder: Builder) {
      val siblings = struct.all.siblingsOfFirst()
      siblings.forEach { rootField ->
        mergeField(BuilderSetter.MessageBuilder(builder), rootField)
      }
    }

    fun setScalarValue(bs: BuilderSetter, value: SugarFormatAst.ScalarValue) {
      when (value) {
        is SugarFormatAst.NameValue -> {
          check(bs is BuilderSetter.ScalarSetter)
          bs.setScalarValue(value)
        }

        is SugarFormatAst.StringValue -> {
          check(bs is BuilderSetter.ScalarSetter)
          bs.setScalarValue(value)
        }

        is SugarFormatAst.DecValue -> TODO()
        is SugarFormatAst.HexValue -> TODO()
        is SugarFormatAst.OctValue -> TODO()

        is SugarFormatAst.TimestampValue -> {
          check(bs !is BuilderSetter.RepeatedBuilder && bs !is BuilderSetter.MapBuilder)
          when (bs) {
            is BuilderSetter.ScalarSetter -> {
              // TODO bs의 타입에 따라 string 등으로 변환해서 넣기
              TODO()
            }

            is BuilderSetter.MessageBuilder -> {
              check(bs.builder.descriptorForType.fullName == "google.protobuf.Timestamp")
              val seconds = bs.builder.descriptorForType.findFieldByName("seconds")
              val nanos = bs.builder.descriptorForType.findFieldByName("nanos")
              val timestamp = value.toProtoValue()
              bs.builder.setField(seconds, timestamp.seconds)
              bs.builder.setField(nanos, timestamp.nanos)
            }

            is BuilderSetter.RepeatedBuilder, is BuilderSetter.MapBuilder ->
              throw IllegalStateException()
          }
        }

        is SugarFormatAst.DurationValue -> {
          when (bs) {
            is BuilderSetter.ScalarSetter -> {
              // TODO bs의 타입에 따라 string 등으로 변환해서 넣기
              TODO()
            }

            is BuilderSetter.MessageBuilder -> {
              check(bs.builder.descriptorForType.fullName == "google.protobuf.Duration")
              val seconds = bs.builder.descriptorForType.findFieldByName("seconds")
              val nanos = bs.builder.descriptorForType.findFieldByName("nanos")
              val duration = value.toProtoValue()
              bs.builder.setField(seconds, duration.seconds)
              bs.builder.setField(nanos, duration.nanos)
            }

            is BuilderSetter.RepeatedBuilder, is BuilderSetter.MapBuilder ->
              throw IllegalStateException()
          }
        }
      }
    }

    private fun mergeField(bs: BuilderSetter, field: ItemStructure.Range) {
      val head = field.head
      check(head is SugarFormatAst.SingleItem)
      val path = head.key
      when {
        field.isSingular -> {
          when (val value = head.value) {
            is SugarFormatAst.ScalarValue -> {
              val setter = BuilderSetter.lookup(bs, path)
              setScalarValue(setter, value)
            }

            is SugarFormatAst.RepeatedValue -> {
              val setter = BuilderSetter.lookup(bs, path)
              check(setter is BuilderSetter.RepeatedBuilder)
              setter.setRepeatedValues(value)
            }

            is SugarFormatAst.ObjectOrMapValue -> {
              // setObjectOrMapValues(bs, head.key.first, head.key.access, value)
              TODO()
            }

            is SugarFormatAst.Header -> throw IllegalStateException()
          }
        }

        field.isRepeated -> {
          check(head.value is SugarFormatAst.Header)
          val children = field.listChildrenOfFirst()
          val setter = BuilderSetter.lookup(bs, head.key)
          check(setter is BuilderSetter.RepeatedBuilder)
          when (setter) {
            is BuilderSetter.ScalarRepeatedBuilder -> {
              children.forEach { child ->
                val item = child.head
                check(item is SugarFormatAst.ListValueItem)
                setter.addRepeatedValue(item.value)
              }
            }

            is BuilderSetter.MessageRepeatedBuilder -> {
              children.forEach { child ->
                mergeField(setter.addValueBuilder(), child)
              }
            }
          }
        }

        else -> {
          check(head.value is SugarFormatAst.Header)
          // scalar value setter
          // message value builder
          // scalar list value builder
          // message list value builder
          // scalar map value builder
          // message map value builder
          when (val setter = BuilderSetter.lookup(bs, head.key)) {
            is BuilderSetter.ScalarSetter -> throw IllegalStateException()
            is BuilderSetter.MessageBuilder -> TODO()
            is BuilderSetter.MessageMapBuilder -> TODO()
            is BuilderSetter.MessageRepeatedBuilder -> TODO()
            is BuilderSetter.ScalarMapBuilder -> TODO()
            is BuilderSetter.ScalarRepeatedBuilder -> TODO()
          }
        }
      }
    }

    private fun setRepeatedValues(
      builder: Message.Builder,
      first: SugarFormatAst.KeyValue,
      rest: List<SugarFormatAst.KeyValue>,
      value: SugarFormatAst.RepeatedValue
    ) {
      TODO()
    }

    private fun setObjectOrMapValues(
      builder: Message.Builder,
      first: SugarFormatAst.KeyValue,
      rest: List<SugarFormatAst.KeyValue>,
      value: SugarFormatAst.ObjectOrMapValue
    ) {
      TODO()
    }

    fun test() {
      val fields = struct.all.siblingsOfFirst()
      println(fields)
      val children = fields.first().childrenOfFirst()
      println(children)
      val listChildren = fields.last().listChildrenOfFirst()
      println(listChildren)
    }
  }
}

