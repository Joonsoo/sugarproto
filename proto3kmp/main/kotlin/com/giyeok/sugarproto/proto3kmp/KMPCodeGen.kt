package com.giyeok.sugarproto.proto3kmp

object KMPCodeGen {
  fun defCodeOf(type: ValueType): String = when (type) {
    BoolType -> "Boolean"
    is Int32Type -> "Int"
    is Int64Type -> "Long"
    FloatType -> "Float"
    DoubleType -> "Double"
    BytesType -> "ByteArray"
    StringType -> "String"
    is MessageType -> type.canonicalName
    is EnumType -> type.canonicalName
    is PackedRepeatedField -> TODO()
  }

  fun fieldTypeDefCodeOf(field: MessageField): String =
    when (val ftype = field.fieldType) {
      is MessageFieldType.Value -> {
        val valueType = defCodeOf(ftype.type)

        var fieldType = valueType
        if (ftype.repeated) {
          fieldType = "List<$fieldType>"
        }
        if (ftype.optional) {
          fieldType = "$fieldType?"
        }
        fieldType
      }

      is MessageFieldType.Map -> "Map<${defCodeOf(ftype.key)}, ${defCodeOf(ftype.value)}>"
    }

  data class BuilderFieldInfo(
    val builderFieldType: String,
    val initExpr: String,
    val isVal: Boolean
  )

  fun builderFieldTypeDefCodeOf(field: MessageField): BuilderFieldInfo =
    when (val ftype = field.fieldType) {
      is MessageFieldType.Value -> {
        when (ftype.type) {
          BytesType -> TODO()
          is MessageType -> TODO()
          is PackedRepeatedField -> TODO()
          BoolType -> {
            if (ftype.repeated) {
              BuilderFieldInfo("MutableList<Boolean>", "mutableListOf()", true)
            } else if (ftype.optional) {
              BuilderFieldInfo("Boolean?", "null", false)
            } else {
              BuilderFieldInfo("Boolean", "false", false)
            }
          }

          FloatType -> TODO()
          DoubleType -> TODO()
          is EnumType -> TODO()
          is Int32Type -> {
            val valueType = if (ftype.type.encoding.isUnsigned) "UInt" else "Int"
            if (ftype.repeated) {
              BuilderFieldInfo("MutableList<$valueType>", "mutableListOf()", true)
            } else if (ftype.optional) {
              BuilderFieldInfo("$valueType?", "null", false)
            } else {
              BuilderFieldInfo(valueType, if (ftype.type.encoding.isUnsigned) "0u" else "0", false)
            }
          }

          is Int64Type -> TODO()
          StringType -> {
            if (ftype.repeated) {
              BuilderFieldInfo("MutableList<String>", "mutableListOf()", true)
            } else if (ftype.optional) {
              BuilderFieldInfo("String?", "null", false)
            } else {
              BuilderFieldInfo("String", "\"\"", false)
            }
          }
        }
      }

      is MessageFieldType.Map ->
        BuilderFieldInfo(
          "MutableMap<${defCodeOf(ftype.key)}, ${defCodeOf(ftype.value)}>",
          "mutableMapOf()",
          true
        )
    }

  fun descCodeOf(type: ValueType): String = when (type) {
    BoolType -> "BoolType"
    is Int32Type -> "Int32Type()"
    is Int64Type -> "Int64Type()"
    FloatType -> "FloatType"
    DoubleType -> "DoubleType"
    BytesType -> "BytesType"
    StringType -> "StringType"
    is MessageType -> "MessageType(\"${type.pkg}\", \"${type.messageTypeName}\")"
    is EnumType -> "EnumType(\"${type.enumName}\")"
    is PackedRepeatedField -> TODO()
  }

  // returns 필요한 타입 이름들
  fun generateMessage(writer: CodeWriter, message: MessageDef) {
    writer.writeLine("data class ${message.name}(")
    writer.indent {
      for (field in message.fields) {
        writer.writeLine("val ${field.name}: ${fieldTypeDefCodeOf(field)},")
      }
    }
    writer.writeLine("): MessageType() {")
    writer.indent {
      writer.writeLine("companion object {")
      writer.indent {
        writer.writeLine("val descriptor: MessageTypeDescriptor = MessageTypeDescriptor(")
        writer.indent {
          writer.writeLine("\"${message.pkg}\",")
          writer.writeLine("\"${message.name}\",")
          writer.writeLine("listOf(")
          writer.indent {
            for (field in message.fields) {
              when (val fieldType = field.fieldType) {
                is MessageFieldType.Map -> {
                  val keyTypeDesc = descCodeOf(fieldType.key)
                  val valueTypeDesc = descCodeOf(fieldType.value)
                  writer.writeLine("MessageField.Map(${field.number}, \"${field.name}\", $keyTypeDesc, $valueTypeDesc),")
                }

                is MessageFieldType.Value -> {
                  val typeDesc = descCodeOf(fieldType.type)
                  writer.writeLine("MessageField.Value(${field.number}, \"${field.name}\", ${typeDesc}, ${fieldType.optional}, ${fieldType.repeated}),")
                }
              }
            }
          }
          writer.writeLine(")")
        }
        writer.writeLine("}")
        writer.writeLine()
        writer.writeLine("fun fromByteArray(bytes: ByteArray): ${message.name} {")
        writer.indent {
          writer.writeLine("val builder = Builder()")
          writer.writeLine("builder.setFromSerializedByteArray(bytes)")
          writer.writeLine("return builder.build()")
        }
        writer.writeLine("}")
      }
      writer.writeLine("}")
      writer.writeLine()
      writer.writeLine("data class Builder(")
      writer.indent {
        for (field in message.fields) {
          val d = builderFieldTypeDefCodeOf(field)
          val valVar = if (d.isVal) "val" else "var"
          writer.writeLine("$valVar ${field.name}: ${d.builderFieldType} = ${d.initExpr},")
        }
      }
      writer.writeLine("): MessageBuilderType<${message.name}>() {")
      writer.indent {
        writer.writeLine("override fun setFromSerializedByteArray(bytes: ByteArray) {")
        writer.indent {
          writer.writeLine("val pairs = parseBinary(bytes)")
          writer.writeLine("setFromEncodingResult(pairs)")
        }
        writer.writeLine("}")
        writer.writeLine()
        writer.writeLine("override fun setFromEncodingResult(encodingResult: MessageEncodingResult) {")
        writer.indent {
          val requiredFields = message.fields.filter {
            when (it.fieldType) {
              is MessageFieldType.Value -> !it.fieldType.optional && !it.fieldType.repeated
              is MessageFieldType.Map -> false
            }
          }
          if (requiredFields.isNotEmpty()) {
            writer.writeLine("check(encodingResult.tagNumbers.containsAll(setOf(${requiredFields.joinToString { it.number.toString() }})))")
          }

          for (field in message.fields) {
            when (val ftype = field.fieldType) {
              is MessageFieldType.Value -> {
                if (ftype.optional) {
                  writer.writeLine("val ${field.name} = encodingResult.getOptional(${field.number})")
                } else if (ftype.repeated) {
                  writer.writeLine("TODO()")
                } else {
                  writer.writeLine("val ${field.name} = encodingResult.getSingle(${field.number})")
                  when (ftype.type) {
                    StringType -> {
                      writer.writeLine("check(${field.name} is LenValue)")
                      writer.writeLine("this.${field.name} = String(${field.name}.bytes)")
                    }

                    is Int32Type -> {
                      writer.writeLine("check(${field.name} is ${ftype.type.encoding.encodingType})")
                      writer.writeLine("this.${field.name} = ${field.name}.value")
                    }

                    is Int64Type -> {
                      writer.writeLine("check(${field.name} is ${ftype.type.encoding.encodingType})")
                      writer.writeLine("this.${field.name} = ${field.name}.value")
                    }

                    BytesType -> TODO()
                    is MessageType -> TODO()
                    is PackedRepeatedField -> TODO()
                    BoolType -> TODO()
                    DoubleType -> TODO()
                    is EnumType -> TODO()
                    FloatType -> TODO()
                  }
                }
              }

              is MessageFieldType.Map -> {
                writer.writeLine("// TODO")
              }
            }
          }
        }
        writer.writeLine("}")
        writer.writeLine()
        writer.writeLine("override fun build(): ${message.name} = ${message.name}(")
        writer.indent {
          for (field in message.fields) {
            when (val ftype = field.fieldType) {
              is MessageFieldType.Value -> {
                when (ftype.type) {
                  is MessageType -> {
                    if (ftype.optional) {
                      if (ftype.repeated) {
                        writer.writeLine("${field.name}?.map { it.build() },")
                      } else {
                        writer.writeLine("${field.name}?.build(),")
                      }
                    } else {
                      if (ftype.repeated) {
                        writer.writeLine("${field.name}.map { it.build() },")
                      } else {
                        writer.writeLine("${field.name}.build(),")
                      }
                    }
                  }

                  else -> writer.writeLine("${field.name},")
                }
              }

              is MessageFieldType.Map -> {
                writer.writeLine("TODO()")
              }
            }
          }
        }
        writer.writeLine(")")
      }
      writer.writeLine("}")
      writer.writeLine()
      writer.writeLine("override fun serialize(): MessageEncodingResult {")
      writer.indent {
        writer.writeLine("val pairs = buildList<MessageEncodingPair> {")
        writer.indent {
          for (field in message.fields) {
            when (val ftype = field.fieldType) {
              is MessageFieldType.Value -> {
                when (ftype.type) {
                  BoolType -> TODO()
                  BytesType -> TODO()
                  is MessageType -> TODO()
                  is PackedRepeatedField -> TODO()
                  FloatType -> TODO()
                  DoubleType -> TODO()
                  is EnumType -> TODO()
                  is Int32Type -> {
                    val encodingType = encodeTypeOf(ftype.type.byteEncoding)
                    writer.writeLine("add(MessageEncodingPair(${field.number}, $encodingType(this.${field.name})))")
                  }

                  is Int64Type -> {
                    val encodingType = encodeTypeOf(ftype.type.byteEncoding)
                    writer.writeLine("add(MessageEncodingPair(${field.number}, $encodingType(this.${field.name})))")
                  }

                  StringType -> {
                    val enc = "${field.name}_"
                    writer.writeLine("val $enc = this.${field.name}.encodeToByteArray()")
                    writer.writeLine("add(MessageEncodingPair(${field.number}, LenValue($enc.size, $enc)))")
                  }
                }
              }

              is MessageFieldType.Map -> {
                writer.writeLine("// TODO map")
              }
            }
          }
        }
        writer.writeLine("}")
        writer.writeLine("return MessageEncodingResult(pairs)")
      }
      writer.writeLine("}")
    }
    writer.writeLine("}")
  }

  fun encodeTypeOf(typ: ProtoEncodingType): String = when (typ) {
    ProtoEncodingType.VARINT -> "VarIntValue"
    ProtoEncodingType.I32 -> "I32Value"
    ProtoEncodingType.I64 -> "I64Value"
    ProtoEncodingType.LEN -> "LenValue"
  }

  fun generateService(writer: CodeWriter, service: ServiceDef) {
    writer.writeLine("class ${service.name}Client(")
    writer.indent {
      writer.writeLine("channel: HttpClient,")
    }
    writer.writeLine("): GrpcService(channel) {")
    writer.indent {
      writer.writeLine("companion object {")
      writer.indent {
        writer.writeLine("val serviceDescriptor: ServiceDescriptor = ServiceDescriptor(")
        writer.indent {
          writer.writeLine("\"${service.pkg}\",")
          writer.writeLine("\"${service.name}\",")
          writer.writeLine("listOf(")
          writer.indent {
            for (rpc in service.rpcs) {
              writer.writeLine("ServiceRpcDescriptor(\"${rpc.name}\"),")
            }
          }
          writer.writeLine(")")
        }
        writer.writeLine(")")
      }
      writer.writeLine("}")
      writer.writeLine()
      for (rpc in service.rpcs) {
        val name = rpc.name.replaceFirstChar { it.lowercase() }
        writer.writeLine("suspend fun $name(request: ${rpc.req.canonicalName}): ${rpc.res.canonicalName} {")
        writer.indent {
          writer.writeLine("val serialized = request.serialize()")
          writer.writeLine("val size = serialized.calculateSerializedSize()")
          writer.writeLine("val bytes = ByteArray(size + 5)")
          writer.writeLine("bytes[0] = 0")
          writer.writeLine("bytes[1] = ((size shr 24) and 0xff).toByte()")
          writer.writeLine("bytes[2] = ((size shr 16) and 0xff).toByte()")
          writer.writeLine("bytes[3] = ((size shr 8) and 0xff).toByte()")
          writer.writeLine("bytes[4] = (size and 0xff).toByte()")
          writer.writeLine("serialized.writeTo(bytes, 5)")
          writer.writeLine("val reqBuilder = HttpRequestBuilder {")
          writer.writeLine("}")
          writer.writeLine("reqBuilder.setBody(bytes)")
          writer.writeLine("val response = channel.post(reqBuilder)")
          writer.writeLine("val resBytes = response.bodyAsBytes()")
          writer.writeLine("check(resBytes[0] == 0.toByte())")
          writer.writeLine("val resSize = resBytes[1].toInt() and 0xff // TODO")
          writer.writeLine("check(resBytes.size == resSize + 5)")
          writer.writeLine("val resBuilder = ExampleGeneratedMessage.Builder()")
          writer.writeLine("val pairs = parseBinary(resBytes, 5)")
          writer.writeLine("resBuilder.setFromEncodingResult(pairs)")
          writer.writeLine("return resBuilder.build()")
        }
        writer.writeLine("}")
      }
    }
    writer.writeLine("}")
  }
}
