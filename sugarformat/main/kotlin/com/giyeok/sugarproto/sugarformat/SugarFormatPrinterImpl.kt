package com.giyeok.sugarproto.sugarformat

import com.google.protobuf.*
import com.google.protobuf.Descriptors.FieldDescriptor

class SugarFormatPrinterImpl(val writer: CodeWriter) {
  fun print(message: MessageOrBuilder) {
    message.allFields.forEach { (field, value) ->
      printMessageField(field, value, false)
    }
  }

  private fun printMessageField(field: FieldDescriptor, value: Any, isListItem: Boolean) {
    when {
      field.isMapField -> TODO()
      field.isRepeated -> {
        check(value is List<*>)
        if (isListItem) {
          writer.indent {
            value.forEach { elem ->
              printMessageFieldValue(field, elem!!, true)
            }
          }
        } else {
          writer.writeLine("${field.name}:")
          value.forEach { elem ->
            printMessageFieldValue(field, elem!!, true)
          }
        }
      }

      else -> {
        printMessageFieldValue(field, value, isListItem)
      }
    }
  }

  private fun printMessageFieldValue(field: FieldDescriptor, value: Any, isListItem: Boolean) {
    when {
      field.type == FieldDescriptor.Type.MESSAGE -> {
        when (field.messageType.fullName) {
          "google.protobuf.Timestamp" -> {
            check(value is Timestamp)
            if (isListItem) {
              writer.writeLine("- ${timestampStringOf(value)}")
            } else {
              writer.writeLine("${field.name}: ${timestampStringOf(value)}")
            }
          }

          "google.protobuf.Duration" -> {
            val duration = value as Duration
            if (isListItem) {
              writer.writeLine("- ${durationStringOf(duration)}")
            } else {
              writer.writeLine("${field.name}: ${durationStringOf(duration)}")
            }
          }

          else -> {
            check(value is Message)
            val fields = value.allFields.entries.toList()
            if (fields.isNotEmpty()) {
              if (isListItem) {
                val firstField = fields.first()
                val restFields = fields.drop(1)

                printMessageField(firstField.key, firstField.value, true)
                writer.indent("  ") {
                  restFields.forEach { fieldPair ->
                    printMessageField(fieldPair.key, fieldPair.value, false)
                  }
                }
              } else {
                writer.writeLine("${field.name}:")
                writer.indent {
                  fields.forEach { fieldPair ->
                    printMessageField(fieldPair.key, fieldPair.value, false)
                  }
                }
              }
            }
          }
        }
      }

      else -> {
        val valueString = printStringOf(field.type, value)
        if (isListItem) {
          writer.writeLine("- ${field.name}: $valueString")
        } else {
          writer.writeLine("${field.name}: $valueString")
        }
      }
    }
  }
}
