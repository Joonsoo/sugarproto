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
          is MessageType -> {
            val bld = ftype.type.canonicalName + ".Builder"
            if (ftype.repeated) {
              BuilderFieldInfo("MutableList<$bld>", "mutableListOf()", true)
            } else if (ftype.optional) {
              BuilderFieldInfo("$bld?", "null", false)
            } else {
              BuilderFieldInfo(bld, "$bld()", true)
            }
          }

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

          is Int64Type -> {
            val valueType = if (ftype.type.encoding.isUnsigned) "ULong" else "Long"
            if (ftype.repeated) {
              BuilderFieldInfo("MutableList<$valueType>", "mutableListOf()", true)
            } else if (ftype.optional) {
              BuilderFieldInfo("$valueType?", "null", false)
            } else {
              BuilderFieldInfo(valueType, if (ftype.type.encoding.isUnsigned) "0u" else "0", false)
            }
          }

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

  fun descCodeOf(writer: CodeWriter, type: ValueType): String = when (type) {
    BoolType -> "BoolType"
    is Int32Type -> {
      writer.addImport("com.giyeok.sugarproto.proto3kmp.Int32Type")
      writer.addImport("com.giyeok.sugarproto.proto3kmp.Int32Encoding")
      "Int32Type(Int32Encoding.${type.encoding})"
    }

    is Int64Type -> {
      writer.addImport("com.giyeok.sugarproto.proto3kmp.Int64Type")
      writer.addImport("com.giyeok.sugarproto.proto3kmp.Int64Encoding")
      "Int64Type(Int64Encoding.${type.encoding})"
    }

    FloatType -> "FloatType"
    DoubleType -> "DoubleType"
    BytesType -> "BytesType"
    StringType -> {
      writer.addImport("com.giyeok.sugarproto.proto3kmp.StringType")
      "StringType"
    }

    is MessageType -> {
      writer.addImport("com.giyeok.sugarproto.proto3kmp.MessageType")
      "MessageType(\"${type.canonicalName}\")"
    }

    is EnumType -> "EnumType(\"${type.enumName}\")"
    is PackedRepeatedField -> TODO()
  }

  fun ktValueEncodeType(encoding: ProtoEncodingType): String = when (encoding) {
    ProtoEncodingType.VARINT -> "VarIntValue"
    ProtoEncodingType.LEN -> "LenValue"
    ProtoEncodingType.I64 -> "I64Value"
    ProtoEncodingType.I32 -> "I32Value"
  }

  // returns 필요한 타입 이름들
  fun generateMessage(writer: CodeWriter, message: MessageDef) {
    writer.writeLine("data class ${message.name}(")
    writer.indent {
      for (field in message.fields) {
        writer.writeLine("val ${field.name}: ${fieldTypeDefCodeOf(field)},")
      }
    }
    writer.writeLine("): GeneratedMessage() {")
    writer.addImport("com.giyeok.sugarproto.proto3kmp.GeneratedMessage")
    writer.indent {
      writer.writeLine("companion object {")
      writer.indent {
        writer.writeLine("val descriptor: MessageTypeDescriptor = MessageTypeDescriptor(")
        writer.addImport("com.giyeok.sugarproto.proto3kmp.MessageTypeDescriptor")
        writer.indent {
          writer.writeLine("\"${message.pkg}\",")
          writer.writeLine("\"${message.name}\",")
          writer.writeLine("listOf(")
          writer.addImport("com.giyeok.sugarproto.proto3kmp.MessageField")
          writer.addImport("com.giyeok.sugarproto.proto3kmp.MessageFieldType")
          writer.indent {
            for (field in message.fields) {
              when (val fieldType = field.fieldType) {
                is MessageFieldType.Map -> {
                  val keyTypeDesc = descCodeOf(writer, fieldType.key)
                  val valueTypeDesc = descCodeOf(writer, fieldType.value)
                  writer.writeLine("MessageField(${field.number}, \"${field.name}\", MessageFieldType.Map($keyTypeDesc, $valueTypeDesc)),")
                }

                is MessageFieldType.Value -> {
                  val typeDesc = descCodeOf(writer, fieldType.type)
                  writer.writeLine("MessageField(${field.number}, \"${field.name}\", MessageFieldType.Value(${typeDesc}, ${fieldType.optional}, ${fieldType.repeated})),")
                }
              }
            }
          }
          writer.writeLine(")")
        }
        writer.writeLine(")")
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
      writer.writeLine("): GeneratedMessageBuilder<${message.name}>() {")
      writer.addImport("com.giyeok.sugarproto.proto3kmp.GeneratedMessageBuilder")
      writer.indent {
        writer.writeLine("override fun setFromSerializedByteArray(bytes: ByteArray) {")
        writer.indent {
          writer.writeLine("val pairs = parseBinary(bytes)")
          writer.addImport("com.giyeok.sugarproto.proto3kmp.parseBinary")
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

          fun generateConvertCode(typ: ValueType, valueName: String): String {
            when (typ) {
              StringType -> {
                writer.addImport("com.giyeok.sugarproto.proto3kmp.LenValue")
                writer.writeLine("check($valueName is LenValue)")
                return "String(${valueName}.bytes)"
              }

              is Int32Type -> {
                val ktValueType = ktValueEncodeType(typ.encoding.encodingType)
                val valueConvertMethod = when (typ.encoding) {
                  Int32Encoding.INT32 -> ".toInt()"
                  Int32Encoding.UINT32 -> ".toUInt()"
                  Int32Encoding.SINT32 -> ".toInt()"
                  Int32Encoding.FIXED32 -> TODO()
                  Int32Encoding.SFIXED32 -> TODO()
                }
                writer.writeLine("check($valueName is $ktValueType)")
                return "$valueName.value$valueConvertMethod"
              }

              is Int64Type -> {
                val ktValueType = ktValueEncodeType(typ.encoding.encodingType)
                val valueConvertMethod = when (typ.encoding) {
                  Int64Encoding.INT64 -> ""
                  Int64Encoding.UINT64 -> ".toULong()"
                  Int64Encoding.SINT64 -> ""
                  Int64Encoding.FIXED64 -> TODO()
                  Int64Encoding.SFIXED64 -> TODO()
                }
                writer.writeLine("check($valueName is $ktValueType)")
                return "$valueName.value$valueConvertMethod"
              }

              BytesType -> TODO()
              is MessageType -> throw AssertionError()

              is PackedRepeatedField -> TODO()
              BoolType -> TODO()
              DoubleType -> TODO()
              is EnumType -> TODO()
              FloatType -> TODO()
            }
          }

          for (field in message.fields) {
            when (val ftype = field.fieldType) {
              is MessageFieldType.Value -> {
                if (ftype.type is MessageType) {
                  if (ftype.optional) {
                    writer.writeLine("val ${field.name} = encodingResult.getOptional(${field.number})")
                    writer.writeLine("${field.name}?.let {")
                    writer.indent {
                      writer.writeLine("check(${field.name} is LenValue)")
                      writer.writeLine("val builder = ${ftype.type.canonicalName}.Builder()")
                      writer.writeLine("builder.setFromSerializedByteArray(${field.name}.bytes)")
                      writer.writeLine("this.${field.name} = builder")
                    }
                    writer.writeLine("}")
                  } else if (ftype.repeated) {
                    writer.writeLine("this.${field.name}.clear()")
                    writer.writeLine("encodingResult.getRepeated(${field.number}).forEach {")
                    writer.indent {
                      writer.writeLine("check(it is LenValue)")
                      writer.writeLine("val builder = ${ftype.type.canonicalName}.Builder()")
                      writer.writeLine("builder.setFromSerializedByteArray(it.bytes)")
                      writer.writeLine("this.${field.name}.add(builder)")
                    }
                    writer.writeLine("}")
                  } else {
                    writer.writeLine("val ${field.name} = encodingResult.getSingle(${field.number})")
                    writer.writeLine("check(${field.name} is LenValue)")
                    writer.writeLine("this.${field.name}.setFromSerializedByteArray(${field.name}.bytes)")
                  }
                } else {
                  if (ftype.optional) {
                    writer.writeLine("val ${field.name} = encodingResult.getOptional(${field.number})")
                    writer.writeLine("this.${field.name} = ${field.name}?.let {")
                    writer.indent {
                      val expr = generateConvertCode(ftype.type, "it")
                      writer.writeLine(expr)
                    }
                    writer.writeLine("}")
                  } else if (ftype.repeated) {
                    writer.writeLine("this.${field.name}.clear()")
                    writer.writeLine("encodingResult.getRepeated(${field.number}).forEach {")
                    writer.indent {
                      val expr = generateConvertCode(ftype.type, "it")
                      writer.writeLine("this.${field.name}.add($expr)")
                    }
                    writer.writeLine("}")
                  } else {
                    writer.writeLine("val ${field.name} = encodingResult.getSingle(${field.number})")
                    val expr = generateConvertCode(ftype.type, field.name)
                    writer.writeLine("this.${field.name} = $expr")
                  }
                }
              }

              is MessageFieldType.Map -> {
                writer.writeLine("this.${field.name}.clear()")
                writer.writeLine("encodingResult.getRepeated(${field.number}).forEach { kvPair ->")
                writer.indent {
                  writer.writeLine("check(kvPair is LenValue)")
                  writer.writeLine("val parsedKvPair = parseBinary(kvPair.bytes)")
                  writer.writeLine("val rawKey = parsedKvPair.getSingle(1)")
                  val keyExpr = generateConvertCode(ftype.key, "rawKey")
                  writer.writeLine("val rawValue = parsedKvPair.getSingle(2)")
                  val valueExpr = generateConvertCode(ftype.key, "rawValue")
                  writer.writeLine("this.${field.name}[$keyExpr] = $valueExpr")
                }
                writer.writeLine("}")
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

                  else -> {
                    if (ftype.optional) {
                      if (ftype.repeated) {
                        writer.writeLine("${field.name}?.toList(),")
                      } else {
                        writer.writeLine("${field.name},")
                      }
                    } else {
                      if (ftype.repeated) {
                        writer.writeLine("${field.name}.toList(),")
                      } else {
                        writer.writeLine("${field.name},")
                      }
                    }
                  }
                }
              }

              is MessageFieldType.Map -> {
                writer.writeLine("${field.name}.toMap(),")
              }
            }
          }
        }
        writer.writeLine(")")
      }
      writer.writeLine("}")
      writer.writeLine()
      writer.addImport("com.giyeok.sugarproto.proto3kmp.MessageEncodingResult")
      writer.writeLine("override fun serialize(): MessageEncodingResult {")
      writer.indent {
        writer.writeLine("val msg = this")
        writer.addImport("com.giyeok.sugarproto.proto3kmp.MessageEncodingPair")
        writer.writeLine("val pairs = buildList {")
        writer.indent {
          fun generateAddPairCode(typ: ValueType, number: Int, value: String) {
            when (typ) {
              BoolType -> {
                writer.writeLine("add(MessageEncodingPair($number, VarIntValue(if ($value) 1 else 0)))")
              }

              BytesType -> {
                writer.writeLine("add(MessageEncodingPair($number, LenValue($value.size, $value)))")
              }

              is MessageType -> {
                writer.writeLine("$value.serializeToByteArray().let { arr ->")
                writer.indent {
                  writer.writeLine("add(MessageEncodingPair($number, LenValue(arr.size, arr)))")
                }
                writer.writeLine("}")
              }

              is PackedRepeatedField -> TODO()
              FloatType -> TODO()
              DoubleType -> TODO()
              is EnumType -> TODO()
              is Int32Type -> {
                val encodingType = encodeTypeOf(writer, typ.byteEncoding)
                writer.writeLine("add(MessageEncodingPair($number, $encodingType($value)))")
              }

              is Int64Type -> {
                val encodingType = encodeTypeOf(writer, typ.byteEncoding)
                writer.writeLine("add(MessageEncodingPair($number, $encodingType($value)))")
              }

              StringType -> {
                // TODO repeated, optional
                writer.writeLine("$value.encodeToByteArray().let { value ->")
                writer.indent {
                  writer.writeLine("add(MessageEncodingPair($number, LenValue(value.size, value)))")
                }
                writer.writeLine("}")
              }
            }
          }

          for (field in message.fields) {
            when (val ftype = field.fieldType) {
              is MessageFieldType.Value -> {
                if (ftype.repeated) {
                  writer.writeLine("msg.${field.name}.forEach { value ->")
                  writer.indent {
                    generateAddPairCode(ftype.type, field.number, "value")
                  }
                  writer.writeLine("}")
                } else if (ftype.optional) {
                  writer.writeLine("msg.${field.name}?.let { value ->")
                  writer.indent {
                    generateAddPairCode(ftype.type, field.number, "value")
                  }
                  writer.writeLine("}")
                } else {
                  generateAddPairCode(ftype.type, field.number, "msg.${field.name}")
                }
              }

              is MessageFieldType.Map -> {
                writer.writeLine("msg.${field.name}.forEach { (key, value) ->")
                writer.indent {
                  writer.writeLine("val kvPair = buildList {")
                  writer.indent {
                    generateAddPairCode(ftype.key, 1, "key")
                    generateAddPairCode(ftype.key, 2, "value")
                  }
                  writer.writeLine("}")
                  writer.writeLine("val kvPairEncoded = MessageEncodingResult(kvPair).toByteArray()")
                  writer.writeLine("add(MessageEncodingPair(${field.number}, LenValue(kvPairEncoded.size, kvPairEncoded)))")
                }
                writer.writeLine("}")
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

  fun encodeTypeOf(writer: CodeWriter, typ: ProtoEncodingType): String = when (typ) {
    ProtoEncodingType.VARINT -> {
      writer.addImport("com.giyeok.sugarproto.proto3kmp.VarIntValue")
      "VarIntValue"
    }

    ProtoEncodingType.I32 -> {
      writer.addImport("com.giyeok.sugarproto.proto3kmp.I32Value")
      "I32Value"
    }

    ProtoEncodingType.I64 -> {
      writer.addImport("com.giyeok.sugarproto.proto3kmp.I64Value")
      "I64Value"
    }

    ProtoEncodingType.LEN -> {
      writer.addImport("com.giyeok.sugarproto.proto3kmp.LenValue")
      "LenValue"
    }
  }

  fun generateService(writer: CodeWriter, service: ServiceDef) {
    writer.writeLine("class ${service.name}Client(")
    writer.indent {
      writer.addImport("io.ktor.client.HttpClient")
      writer.writeLine("channel: HttpClient,")
      writer.writeLine("serverHost: String,")
      writer.writeLine("serverPort: Int,")
    }
    writer.writeLine("): GeneratedGrpcService(channel, serverHost, serverPort) {")
    writer.addImport("com.giyeok.sugarproto.proto3kmp.GeneratedGrpcService")
    writer.indent {
      writer.writeLine("companion object {")
      writer.indent {
        writer.writeLine("val serviceDescriptor: ServiceDescriptor = ServiceDescriptor(")
        writer.addImport("com.giyeok.sugarproto.proto3kmp.ServiceDescriptor")
        writer.indent {
          writer.writeLine("\"${service.pkg}\",")
          writer.writeLine("\"${service.name}\",")
          writer.writeLine("listOf(")
          writer.addImport("com.giyeok.sugarproto.proto3kmp.ServiceRpcDescriptor")
          writer.indent {
            for (rpc in service.rpcs) {
              writer.writeLine("ServiceRpcDescriptor(\"${rpc.name}\", ${rpc.streamReq}, \"${rpc.req.canonicalName}\", ${rpc.streamRes}, \"${rpc.res.canonicalName}\"),")
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
          writer.writeLine("val resPairs = unaryRequest(\"${service.canonicalName}/${rpc.name}\", request.serialize())")
          writer.writeLine("val resBuilder = ${rpc.res.canonicalName}.Builder()")
          writer.writeLine("resBuilder.setFromEncodingResult(resPairs)")
          writer.writeLine("return resBuilder.build()")
        }
        writer.writeLine("}")
      }
    }
    writer.writeLine("}")
  }
}
