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
        if (field.type != FieldDescriptor.Type.MESSAGE) {
          writer.writeLine("${field.name}:")
          for (e in value) {
            val valueLines = printStringOf(field.type, e!!)
            check(valueLines.size == 1)
            writer.writeLine("- ${valueLines.first()}")
          }
        } else {
          when (field.messageType.fullName) {
            "google.protobuf.Timestamp" -> {
              writer.writeLine("${field.name}:")
              for (e in value) {
                check(e is Timestamp)
                writer.writeLine("- ${timestampStringOf(e)}")
              }
            }

            "google.protobuf.Duration" -> {
              writer.writeLine("${field.name}:")
              for (e in value) {
                check(e is Duration)
                writer.writeLine("- ${durationStringOf(e)}")
              }
            }

            else -> {
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
              writer.writeLine("- ${field.name}: ${timestampStringOf(value)}")
            } else {
              writer.writeLine("${field.name}: ${timestampStringOf(value)}")
            }
          }

          "google.protobuf.Duration" -> {
            val duration = value as Duration
            if (isListItem) {
              writer.writeLine("- ${field.name}: ${durationStringOf(duration)}")
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
        val valueLines = printStringOf(field.type, value)
        check(valueLines.isNotEmpty())
        if (isListItem) {
          writer.writeLine("- ${field.name}: ${valueLines.first()}")
          writer.indent("    " + (0 until field.name.length).joinToString("") { " " }) {
            valueLines.drop(1).forEach { writer.writeLine(it) }
          }
        } else {
          writer.writeLine("${field.name}: ${valueLines.first()}")
          writer.indent("  " + (0 until field.name.length).joinToString("") { " " }) {
            valueLines.drop(1).forEach { writer.writeLine(it) }
          }
        }
      }
    }
  }
}
