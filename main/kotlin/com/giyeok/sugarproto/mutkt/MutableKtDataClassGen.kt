package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.toValueString

// Kotlin mutable data class defs -> kotlin mutable data class definition source codes
class MutableKtDataClassGen(
  val defs: KtDefs,
  val packageName: String? = null,
  val imports: List<String> = listOf(),
  val protoOuterClassName: String = "",
  val gdxMode: Boolean = false,
  val builder: StringBuilder = StringBuilder(),
) {
  val tsGen = TypeStringGen(gdxMode)

  var indentLevel = 0

  fun append(s: String) {
    builder.append(s)
  }

  fun addLine(s: String) {
    addIndent()
    builder.append(s)
    builder.append("\n")
  }

  fun addLine() {
    builder.append("\n")
  }

  fun addIndent() {
    repeat(indentLevel) {
      append("  ")
    }
  }

  fun indentString(): String =
    (0 until indentLevel).map { "  " }.joinToString("")

  fun indent(body: () -> Unit) {
    indentLevel += 1
    body()
    indentLevel -= 1
  }

  fun addComments(comments: List<SugarProtoAst.Comment>) {
    comments.forEach { comment ->
      when (comment) {
        is SugarProtoAst.LineComment -> {
          addLine("//${comment.content}")
        }

        is SugarProtoAst.BlockComment -> TODO()
      }
    }
  }

  fun addField(fieldDef: KtFieldDef, inherited: Boolean) {
    addComments(fieldDef.comments)
    val modifier = if (inherited) "override " else ""
    val (useVar, typeStr, _) = tsGen.fromType(fieldDef.type)
    val valVar = if (useVar) "var" else "val"
    addLine("$modifier$valVar ${fieldDef.name.classFieldName}: $typeStr,")
  }

  fun addAbstractField(fieldDef: KtFieldDef) {
    addComments(fieldDef.comments)
    val (useVar, typeStr, _) = tsGen.fromType(fieldDef.type)
    val valVar = if (useVar) "var" else "val"
    addLine("abstract $valVar ${fieldDef.name.classFieldName}: $typeStr")
  }

  fun addDataClassDef(def: KtDataClassDef) {
    addComments(def.comments)
    val className = def.name.className
    addLine("data class $className(")
    indent {
      def.inheritedFields.forEach { field ->
        addField(field, true)
      }
      def.uniqueFields.forEach { field ->
        addField(field, false)
      }
    }

    val sealedSuper = defs.sealedSupers[def.name]
    if (sealedSuper == null) {
      addLine(") {")
    } else {
      addLine("): ${sealedSuper.className} {")
    }

    val protoClassName = if (sealedSuper == null) {
      "$protoOuterClassName${def.name.className}"
    } else {
      "proto: $protoOuterClassName${sealedSuper.className}"
    }

    indent {
      addLine("companion object {")
      indent {
        addLine("val defaultValue = $className(")
        indent {
          def.allFields.forEach { field ->
            val typeString = tsGen.fromType(field.type)
            addLine("${field.name.classFieldName} = ${typeString.defaultValue},")
          }
        }
        addLine(")")
        addLine()
        val params = def.inheritedFields.map { field ->
          val type = tsGen.fromType(field.type)
          "${field.name.classFieldName}: ${type.typeString}"
        } + "proto: $protoClassName"
        addLine("fun fromProto(${params.joinToString(", ")}): $className {")
        val postProcessors = mutableListOf<ProtoPostProcessorExpr>()
        indent {
          addLine("val instance = $className(")
          indent {
            def.inheritedFields.forEach { field ->
              addLine("${field.name.classFieldName} = ${field.name.classFieldName},")
            }
            def.uniqueFields.forEach { field ->
              val conversion = ProtoConversionExpr.fromField(field, tsGen)
              val initExpr = conversion.initExpr.expr("proto")
              if (initExpr.size == 1) {
                addLine("${field.name.classFieldName} = ${initExpr.first()},")
              } else {
                addLine("${field.name.classFieldName} = ${initExpr.first()}")
                indent {
                  initExpr.drop(1).forEachIndexed { idx, line ->
                    if (idx + 1 == initExpr.size - 1) {
                      addLine("$line,")
                    } else {
                      addLine(line)
                    }
                  }
                }
              }
              conversion.initPostProcessorExpr?.let { postProcessors.add(it) }
            }
          }
          addLine(")")
          postProcessors.forEach { postProcessor ->
            postProcessor.expr(this, "proto", "instance")
          }
          addLine("return instance")
        }
        addLine("}")
      }
      addLine("}")
      addLine()
      addLine("fun toProto(builder: $protoClassName.Builder) {")
      indent {
        // inheritedFields는 이미 밖에서 셋팅돼서 들어옴
        def.uniqueFields.forEach { field ->
          val conversionExpr = ProtoConversionExpr.fromField(field, tsGen)
          conversionExpr.toProtoExpr.expr(this, "this", "builder")
        }
      }
      addLine("}")
    }
    addLine("}")
  }

  fun addEnumClassDef(def: KtEnumClassDef) {
    addComments(def.comments)
    val enumName = def.name.enumName
    addLine("enum class $enumName(val tag: Int) {")
    indent {
      def.values.forEachIndexed { idx, value ->
        val tagValue =
          if (value.minusTag) "-${value.tag.toValueString()}" else value.tag.toValueString()
        if (idx + 1 != def.values.size) {
          addLine("${value.name.enumClassFieldName}($tagValue),")
        } else {
          addLine("${value.name.enumClassFieldName}($tagValue);")
        }
      }

      val protoTypeName = "$protoOuterClassName$enumName"
      addLine()
      addLine("companion object {")
      indent {
        addLine("val defaultValue = $enumName.${def.values.first().name.enumClassFieldName}")
        addLine()
        addLine("fun fromProto(proto: $protoTypeName): $enumName =")
        addLine("  values().find { it.tag == proto.number }!!")
      }
      addLine("}")
      addLine()
      addLine("fun toProto(): $protoTypeName =")
      addLine("  $protoTypeName.forNumber(tag)")

      addComments(def.trailingComments)
    }
    addLine("}")
  }

  fun addSealedClassDef(def: KtSealedClassDef) {
    addComments(def.comments)
    val className = def.name.className
    addLine("sealed class $className {")
    indent {
      def.commonFields.forEach { field ->
        addAbstractField(field)
      }
      addLine()
      def.subTypes.forEach { subType ->
        when (subType) {
          is KtSealedSubType.DedicatedMessage -> {
            // do nothing - 알아서 필요한 타입이 생성되어 있음
          }

          is KtSealedSubType.EmptySub -> {
            if (def.commonFields.isEmpty()) {
              addLine("object ${subType.fieldName.className}: $className()")
              addLine()
            } else {
              addLine("data class ${subType.fieldName.className}(")
              indent {
                def.commonFields.forEach { field ->
                  addField(field, true)
                }
              }
              addLine("): $className()")
              addLine()
            }
          }

          is KtSealedSubType.SingleSub -> {
            addLine("data class ${subType.fieldName.className}(")
            indent {
              def.commonFields.forEach { field ->
                addField(field, true)
              }
              addField(subType.fieldDef, false)
            }
            addLine("): $className()")
            addLine()
          }
        }
      }
      addLine("companion object {")
      val protoTypeName = "$protoOuterClassName$className"
      indent {
        addLine("fun fromProto(proto: $protoTypeName): $className {")
        indent {
          val postProcessors = mutableListOf<ProtoPostProcessorExpr>()
          def.commonFields.forEach { field ->
            val expr = ProtoConversionExpr.fromField(field, tsGen)
            val initExpr = expr.initExpr.expr("proto")
            check(initExpr.size == 1)
            addLine("val ${field.name.classFieldName} = ${initExpr.first()}")
            expr.initPostProcessorExpr?.let { postProcessors.add(it) }
          }
          addLine("val instance = when(proto.${def.name.classFieldName}Case) {")
          indent {
            val commonFields = def.commonFields.map { it.name.classFieldName }
            def.subTypes.forEach { subType ->
              addLine("${subType.fieldName.capitalSnakeCase()} ->")
              indent {
                when (subType) {
                  is KtSealedSubType.DedicatedMessage -> {
                    addLine("${subType.typeName}.fromProto(${(commonFields + "proto").joinToString(", ")})")
                  }

                  is KtSealedSubType.EmptySub -> {
                    if (def.commonFields.isEmpty()) {
                      addLine(subType.fieldName.className)
                    } else {
                      addLine("${subType.fieldName.className}(${commonFields.joinToString(", ")})")
                    }
                  }

                  is KtSealedSubType.SingleSub -> {
                    val conversionExpr = ProtoConversionExpr.fromField(subType.fieldDef, tsGen)
                    val initExpr = conversionExpr.initExpr.expr("proto")
                    check(initExpr.size == 1)
                    // post process?
                    check(conversionExpr.initPostProcessorExpr == null)
                    val params = commonFields + initExpr.first()
                    addLine("${subType.fieldName.className}(${params.joinToString(", ")})")
                  }
                }
              }
            }
            addLine("else -> TODO()")
          }
          addLine(")")
          postProcessors.forEach { postProcessor ->
            postProcessor.expr(this, "proto", "instance")
          }
          addLine("return instance")
        }
        addLine("}")
      }
      addLine("}")
      addLine()
      addLine("fun toProto(builder: ${protoTypeName}.Builder) {")
      indent {
        def.commonFields.forEach { field ->
          val conversionExpr = ProtoConversionExpr.fromField(field, tsGen)
          conversionExpr.toProtoExpr.expr(this, "this", "builder")
        }
      }
      addLine("}")
      addComments(def.trailingComments)
    }
    addLine("}")
  }

  fun generate(): String {
    addComments(defs.comments)

    if (packageName != null) {
      addLine("package $packageName")
      addLine()
    }

    imports.forEach { import ->
      addLine("import $import")
    }
    if (imports.isNotEmpty()) {
      addLine()
    }

    defs.defs.forEach { def ->
      when (def) {
        is KtDataClassDef -> addDataClassDef(def)
        is KtEnumClassDef -> addEnumClassDef(def)
        is KtSealedClassDef -> addSealedClassDef(def)
      }
      addLine()
    }

    addComments(defs.trailingComments)
    return builder.toString()
  }
}
