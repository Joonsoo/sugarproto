package com.giyeok.sugarproto.sugarformat

import com.google.protobuf.Descriptors.FieldDescriptor
import com.google.protobuf.Duration
import com.google.protobuf.Message
import com.google.protobuf.MessageOrBuilder
import com.google.protobuf.Timestamp

// 중간 단계로 list<string> 을 많이 만들어서 비효율적일 것.
class SugarFormatPrinterImpl2(
  private val writer: CodeWriter,
  val explicitEmptyList: Boolean = true,
  val explicitEmptyMessage: Boolean = true,
  val joinSingleFieldMessage: Boolean = true,
) {
  fun print(message: MessageOrBuilder) {
    val lines = printMessage(message)
    for (line in lines) {
      writer.writeLine(line)
    }
  }

  private fun joinSingleFieldMessages(
    fieldName: String,
    field: FieldDescriptor,
    value: Any
  ): List<String> {
    val memberFieldName = "$fieldName.${field.name}"
    if (isSimplePrintableValue(field)) {
      return printMessageField(memberFieldName, field, value)
    }
    if (value is MessageOrBuilder) {
      if (value.allFields.size == 1) {
        val (memberField, memberValue) = value.allFields.entries.single()
        return joinSingleFieldMessages("$fieldName.${memberField.name}", memberField, memberValue)
      }
    }
    return printMessageField(memberFieldName, field, value)
  }

  private fun printMessage(message: MessageOrBuilder): List<String> {
    val lines = mutableListOf<String>()
    message.allFields.forEach { (field, value) ->
      lines.addAll(printMessageField(field.name, field, value))
    }
    return lines
  }

  private fun isSimplePrintableValue(field: FieldDescriptor) =
    (field.type != FieldDescriptor.Type.MESSAGE) ||
      (field.messageType.fullName in setOf(
        "google.protobuf.Timestamp",
        "google.protobuf.Duration"
      ))

  private fun printSimpleValue(field: FieldDescriptor, value: Any): String =
    if (field.type == FieldDescriptor.Type.MESSAGE) {
      when (field.messageType.fullName) {
        "google.protobuf.Timestamp" -> {
          check(value is Timestamp)
          timestampStringOf(value)
        }

        "google.protobuf.Duration" -> {
          val duration = value as Duration
          durationStringOf(duration)
        }

        else -> TODO()
      }
    } else {
      val lines = printStringOf(field.type, value)
      check(lines.size == 1)
      lines.single()
    }

  private fun printMessageField(
    fieldName: String,
    field: FieldDescriptor,
    value: Any
  ): List<String> = when {
    field.isMapField -> TODO()
    field.isRepeated -> {
      check(value is List<*>)
      if (value.isEmpty()) {
        if (explicitEmptyList) {
          listOf("$fieldName: []")
        } else {
          listOf()
        }
      } else {
        val lines = mutableListOf<String>()
        if (isSimplePrintableValue(field)) {
          lines.add("$fieldName:")
          for (e in value) {
            lines.add("- ${printSimpleValue(field, e!!)}")
          }
        } else {
          lines.add("$fieldName:")
          for (elem in value) {
            check(elem is MessageOrBuilder)
            val elemLines = printMessage(elem)
            if (elemLines.isNotEmpty()) {
              lines.add("- ${elemLines.first()}")
              lines.addAll(elemLines.drop(1).map { "  $it" })
            }
          }
        }
        lines
      }
    }

    field.type == FieldDescriptor.Type.MESSAGE -> {
      when (field.messageType.fullName) {
        "google.protobuf.Timestamp" -> {
          check(value is Timestamp)
          listOf("$fieldName: ${timestampStringOf(value)}")
        }

        "google.protobuf.Duration" -> {
          val duration = value as Duration
          listOf("$fieldName: ${durationStringOf(duration)}")
        }

        else -> {
          check(value is Message)
          if (value.allFields.isEmpty()) {
            if (explicitEmptyMessage) {
              listOf("$fieldName: {}")
            } else {
              listOf()
            }
          } else if (joinSingleFieldMessage && value.allFields.size == 1) {
            val (memberField, memberValue) = value.allFields.entries.single()
            joinSingleFieldMessages(field.name, memberField, memberValue)
          } else {
            listOf("$fieldName:") + (printMessage(value).map { "  $it" })
          }
        }
      }
    }

    else -> {
      val valueLines = printStringOf(field.type, value)
      check(valueLines.isNotEmpty())
      val lines = mutableListOf<String>()
      lines.add("$fieldName: ${valueLines.first()}")
      val indent = "  " + fieldName.indices.joinToString("") { " " }
      valueLines.drop(1).forEach { lines.add("$indent$it") }
      lines
    }
  }
}
