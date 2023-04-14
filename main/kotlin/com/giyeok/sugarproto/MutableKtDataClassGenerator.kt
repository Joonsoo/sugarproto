package com.giyeok.sugarproto

class MutableKtDataClassGenerator(
  val packageName: String?,
  val options: List<SugarProtoAst.OptionDef>,
  val defs: List<ProtoDef>,
  val imports: List<String>,
  val protoOuterClassName: String,
  val gdxMode: Boolean,
) {
  fun appendComments(
    builder: StringBuilder,
    comments: List<SugarProtoAst.Comment>,
    indent: String
  ) {
    comments.forEach { comment ->
      when (comment) {
        is SugarProtoAst.LineComment -> {
          builder.append(indent)
          builder.append("// ")
          builder.append(comment.content.trim())
          builder.append("\n")
        }

        is SugarProtoAst.BlockComment -> TODO()
      }
    }
  }

  fun TypeExpr.isInt() =
    (this is TypeExpr.PrimitiveType) && (this.typ == SugarProtoAst.PrimitiveTypeEnum.INT32)

  fun kotlinTypeOf(typ: TypeExpr): String = when (typ) {
    TypeExpr.EmptyMessage -> "Empty"
    is TypeExpr.MapType -> {
      if (gdxMode) {
        // TODO 타입 추가
        if (typ.keyType.isInt()) {
          if (typ.valueType.isInt()) {
            "IntIntMap"
          } else {
            "IntMap<${kotlinTypeOf(typ.valueType)}>"
          }
        } else {
          "ObjectMap<${kotlinTypeOf(typ.keyType)}, ${kotlinTypeOf(typ.valueType)}>"
        }
      } else {
        "MutableMap<${kotlinTypeOf(typ.keyType)}, ${kotlinTypeOf(typ.valueType)}>"
      }
    }

    is TypeExpr.MessageOrEnumName -> typ.name
    is TypeExpr.PrimitiveType -> when (typ.typ) {
      SugarProtoAst.PrimitiveTypeEnum.BOOL -> "Boolean"
      SugarProtoAst.PrimitiveTypeEnum.BYTES -> "ByteString"
      SugarProtoAst.PrimitiveTypeEnum.DOUBLE -> "Double"
      SugarProtoAst.PrimitiveTypeEnum.FIXED32 -> TODO()
      SugarProtoAst.PrimitiveTypeEnum.FIXED64 -> TODO()
      SugarProtoAst.PrimitiveTypeEnum.FLOAT -> "Float"
      SugarProtoAst.PrimitiveTypeEnum.INT32 -> "Int"
      SugarProtoAst.PrimitiveTypeEnum.INT64 -> "Long"
      SugarProtoAst.PrimitiveTypeEnum.SFIXED32 -> TODO()
      SugarProtoAst.PrimitiveTypeEnum.SFIXED64 -> TODO()
      SugarProtoAst.PrimitiveTypeEnum.SINT32 -> TODO()
      SugarProtoAst.PrimitiveTypeEnum.SINT64 -> TODO()
      SugarProtoAst.PrimitiveTypeEnum.STRING -> "String"
      SugarProtoAst.PrimitiveTypeEnum.UINT32 -> TODO()
      SugarProtoAst.PrimitiveTypeEnum.UINT64 -> TODO()
    }
  }

  fun String.withOptional(optional: Boolean) = if (optional) "$this?" else this

  fun gdxArrayType(elemType: TypeExpr): String =
    if (elemType.isInt()) {
      "IntArray"
    } else {
      "GdxArray<${kotlinTypeOf(elemType)}>"
    }

  fun generateFieldType(fieldType: ProtoFieldType): String =
    if (fieldType.repeated) {
      if (gdxMode) {
        // TODO libgdx의 BooleanArray, LongArray 등 추가
        gdxArrayType(fieldType.type).withOptional(fieldType.optional)
      } else {
        "MutableList<${kotlinTypeOf(fieldType.type)}>".withOptional(fieldType.optional)
      }
    } else {
      kotlinTypeOf(fieldType.type).withOptional(fieldType.optional)
    }

  fun memberShouldBeVar(typ: ProtoFieldType): Boolean {
    if (!typ.repeated && typ.type is TypeExpr.PrimitiveType) {
      return true
    }
    return false
  }

  fun capitalSnakeCase(name: String): String {
    val words = name.split('_')
    return words.joinToString("_") { it.uppercase() }
  }

  fun capitalCamelCase(name: String): String {
    val words = name.split('_')
    return words.joinToString("") { word -> word.replaceFirstChar { it.uppercase() } }
  }

  fun camelCase(name: String): String {
    val words = name.split('_')
    val firstWord = words.first()
    val restWords = words.drop(1)
    return firstWord.lowercase() + (restWords.joinToString("") { word -> word.replaceFirstChar { it.uppercase() } })
  }

  fun generate(): String {
    val builder = StringBuilder()

    if (packageName != null) {
      builder.append("package $packageName\n\n")
    }

    imports.forEach {
      builder.append("import $it\n")
    }
    if (imports.isNotEmpty()) {
      builder.append("\n")
    }

    defs.forEach { def ->
      when (def) {
        is ProtoEnumDef -> {
          appendComments(builder, def.comments, "")
          builder.append("enum class ${def.name}(val tag: Int) {\n")
          def.members.forEachIndexed { idx, member ->
            when (member) {
              is ProtoEnumMember.EnumOption -> {
                // ignore
              }

              is ProtoEnumMember.EnumValueDef -> {
                val tag =
                  if (member.minusTag) "-${member.tag.toValueString()}" else member.tag.toValueString()
                builder.append("  ${capitalSnakeCase(member.name)}($tag)")

                if (idx + 1 == def.members.size) {
                  builder.append(";\n")
                } else {
                  builder.append(",\n")
                }
              }
            }
          }
          builder.append("  companion object {\n")
          builder.append("    fun fromProto(proto: $protoOuterClassName${def.name}): ${def.name} {\n")
          builder.append("      TODO()\n")
          builder.append("    }\n")
          builder.append("    fun toProto(): $protoOuterClassName${def.name} {\n")
          builder.append("      TODO()\n")
          builder.append("    }\n")
          builder.append("  }\n")
          builder.append("}\n\n")
        }

        is ProtoMessageDef -> {
          appendComments(builder, def.comments, "")
          val className = capitalCamelCase(def.name)
          builder.append("data class $className(\n")
          def.members.forEach { member ->
            when (member) {
              is ProtoMessageMember.ProtoFieldDef -> {
                appendComments(builder, member.comments, "  ")
                val varVal = if (memberShouldBeVar(member.type)) "var" else "val"
                builder.append("  $varVal ${camelCase(member.name)}: ")
                builder.append(generateFieldType(member.type))
                builder.append(",\n")
              }

              is ProtoMessageMember.ProtoMessageOptionDef -> {
                // ignore
              }

              is ProtoMessageMember.ProtoNestedEnumDef -> TODO()
              is ProtoMessageMember.ProtoNestedMessageDef -> TODO()
              is ProtoMessageMember.ProtoOneOf -> TODO()
              is ProtoMessageMember.ProtoReservedDef -> {
                // ignore
              }
            }
          }
          builder.append(") {\n")
          builder.append("  companion object {\n")
          builder.append("    fun fromProto(proto: ${protoOuterClassName}$className): $className {\n")
          builder.append("      val instance = $className(\n")
          val postProcessors = mutableListOf<List<String>>()
          def.members.forEach { member ->
            when (member) {
              is ProtoMessageMember.ProtoFieldDef -> {
                val memberName = camelCase(member.name)
                builder.append("        $memberName = ")
                val processor = when (member.type.type) {
                  is TypeExpr.PrimitiveType ->
                    if (member.type.repeated) {
                      if (gdxMode) {
                        postProcessors.add(
                          listOf(
                            "proto.${memberName}List.forEach { elem ->",
                            "  instance.$memberName.add(elem)",
                            "}",
                          )
                        )
                        "${gdxArrayType(member.type.type)}(proto.${memberName}Count)"
                      } else {
                        "proto.${memberName}List.toMutableList()"
                      }
                    } else {
                      "proto.$memberName"
                    }

                  is TypeExpr.MessageOrEnumName ->
                    if (member.type.repeated) {
                      if (gdxMode) {
                        postProcessors.add(
                          listOf(
                            "proto.${memberName}List.forEach { elem ->",
                            "  instance.$memberName.add(${capitalCamelCase(member.type.type.name)}.fromProto(elem))",
                            "}",
                          )
                        )
                        "GdxArray(proto.${memberName}Count)"
                      } else {
                        "proto.${memberName}List.map { ${capitalCamelCase(member.type.type.name)}.fromProto(it) }.toMutableList()"
                      }
                    } else {
                      "${capitalCamelCase(member.type.type.name)}.fromProto(proto.$memberName)"
                    }

                  TypeExpr.EmptyMessage -> TODO()
                  is TypeExpr.MapType -> {
                    if (gdxMode) {
                      val valueProcessor = when (val valType = member.type.type.valueType) {
                        TypeExpr.EmptyMessage -> TODO()
                        is TypeExpr.MapType -> TODO()
                        is TypeExpr.MessageOrEnumName -> "${capitalCamelCase(valType.name)}.fromProto(value)"
                        is TypeExpr.PrimitiveType -> "value"
                      }
                      postProcessors.add(
                        listOf(
                          "proto.${memberName}Map.forEach { (key, value) ->",
                          "  instance.$memberName.put(key, $valueProcessor)",
                          "}",
                        )
                      )
                      "${kotlinTypeOf(member.type.type)}()"
                    } else {
                      // proto.tileChunkRowsMap.map { it.key to it.value }.toMap().toMutableMap()
                      TODO()
                    }
                  }
                }
                if (member.type.optional) {
                  builder.append("if (!proto.has${capitalCamelCase(member.name)}()) null else $processor")
                } else {
                  builder.append(processor)
                }
                builder.append(",\n")
              }

              is ProtoMessageMember.ProtoMessageOptionDef,
              is ProtoMessageMember.ProtoNestedEnumDef,
              is ProtoMessageMember.ProtoNestedMessageDef,
              is ProtoMessageMember.ProtoReservedDef -> {
                // ignore
              }

              is ProtoMessageMember.ProtoOneOf -> TODO()
            }
          }
          builder.append("      )\n")
          postProcessors.forEach { postProcessor ->
            postProcessor.forEach { line ->
              builder.append("      $line\n")
            }
          }
          builder.append("      return instance\n")
          builder.append("    }\n")
          builder.append("  }\n")
          builder.append("  fun toProto(builder: ${protoOuterClassName}$className.Builder) {\n")
          builder.append("  }\n")
          builder.append("}\n\n")
        }

        is ProtoSealedDef -> {
          val sealedName = capitalCamelCase(def.name)
          builder.append("sealed class $sealedName {\n")
          builder.append("  companion object {\n")
          builder.append("    fun fromProto(proto: $protoOuterClassName${def.name}): $sealedName {\n")
          builder.append("      TODO()\n")
          builder.append("    }\n")
          builder.append("  }\n")
          builder.append("  fun toProto(builder: ${protoOuterClassName}${def.name}.Builder) {\n")
          builder.append("  }\n")
          builder.append("}\n\n")

          def.oneofMembers.forEach { member ->
            when (member) {
              is ProtoMessageMember.ProtoOneOfMember.OneOfOption -> {
                // ignore
              }

              is ProtoMessageMember.ProtoOneOfMember.OneOfField -> {
                appendComments(builder, member.field.comments, "")
                val subType = member.field.type
                if (subType.type == TypeExpr.EmptyMessage) {
                  builder.append("object ${capitalCamelCase(member.field.name)}: $sealedName()\n")
                } else {
                  builder.append("data class ${capitalCamelCase(member.field.name)}(")
                  val varVal = if (memberShouldBeVar(subType)) "var" else "val"
                  builder.append("$varVal ${camelCase(member.field.name)}: ")
                  builder.append(generateFieldType(subType))
                  builder.append("): $sealedName()\n")
                }
              }
            }
          }
        }

        is ProtoServiceDef -> TODO()
      }
    }

    return builder.toString()
  }
}
