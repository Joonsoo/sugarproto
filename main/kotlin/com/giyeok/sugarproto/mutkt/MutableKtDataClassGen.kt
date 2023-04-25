package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.proto.ValueType
import com.giyeok.sugarproto.toValueString

// Kotlin mutable data class defs -> kotlin mutable data class definition source codes
class MutableKtDataClassGen(
  val defs: KtDefs,
  val imports: List<String> = listOf(),
  val gdxMode: Boolean = false,
  val builder: StringBuilder = StringBuilder(),
) {
  val protoOuterClassName = defs.protoJavaOuterClassName + "."

  val tsGen = TypeStringGen(gdxMode)
  val fieldExprGen = FieldExprGen(tsGen, protoOuterClassName)

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

  fun indent(body: () -> Unit) {
    indentLevel += 1
    body()
    indentLevel -= 1
  }

  fun companion(body: () -> Unit) {
    addLine("companion object {")
    indent {
      body()
    }
    addLine("}")
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
    val valVar = if (useVar && !fieldDef.useVal) "var" else "val"
    addLine("$modifier$valVar ${fieldDef.name.classFieldName}: $typeStr,")
  }

  fun addAbstractField(fieldDef: KtFieldDef) {
    addComments(fieldDef.comments)
    val (useVar, typeStr, _) = tsGen.fromType(fieldDef.type)
    val valVar = if (useVar && !fieldDef.useVal) "var" else "val"
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
      addLine("): ${sealedSuper.superClassName.className}() {")
    }

    val fromProtoClassName = "$protoOuterClassName${def.name.className}"
    val toProtoClassName = if (sealedSuper == null) {
      fromProtoClassName
    } else {
      "$protoOuterClassName${sealedSuper.superClassName.className}"
    }

    indent {
      companion {
        addLine("fun create(")
        indent {
          def.allFields.forEach { field ->
            val typeString = tsGen.fromType(field.type)
            addLine("${field.name.classFieldName}: ${typeString.typeString} = ${typeString.defaultValue},")
          }
        }
        addLine(") = $className(")
        indent {
          def.allFields.forEach { field ->
            addLine("${field.name.classFieldName},")
          }
        }
        addLine(")")
        addLine()
        val params = def.inheritedFields.map { field ->
          val type = tsGen.fromType(field.type)
          "${field.name.classFieldName}: ${type.typeString}"
        } + "proto: $fromProtoClassName"
        addLine("fun fromProto(${params.joinToString(", ")}): $className {")
        val postProcessors = mutableListOf<FromProtoPostProcessExpr>()
        indent {
          addLine("val instance = $className(")
          indent {
            def.inheritedFields.forEach { field ->
              addLine("${field.name.classFieldName} = ${field.name.classFieldName},")
            }
            def.uniqueFields.forEach { field ->
              val conversion =
                fieldExprGen.fromField(field, "proto.${field.name.classFieldName}Count")
              val initExpr = conversion.fromProtoExpr.expr("proto")
              addLine("${field.name.classFieldName} = $initExpr,")
              conversion.fromProtoPostProcessExpr?.let { postProcessors.add(it) }
            }
          }
          addLine(")")
          postProcessors.forEach { postProcessor ->
            postProcessor.generate(this, "proto", "instance")
          }
          addLine("return instance")
        }
        addLine("}")
      }
      addLine()
      val indexedFields = def.allFields.filter { it.type is ValueType.IndexedType }
      if (indexedFields.isNotEmpty()) {
        indexedFields.forEach { field ->
          val fieldType = field.type as ValueType.IndexedType
          val ts = tsGen.fromType(fieldType.elemType)
          addLine("fun add${field.name.capitalClassFieldName}(value: ${ts.typeString}) {")
          indent {
            val keyExpr = fieldType.keyExpr.genKeyExpr("value")
            addLine("this.${field.name.classFieldName}.put($keyExpr, value)")
          }
          addLine("}")
          addLine()
        }
      }
      addLine("fun deepClone(): $className {")
      indent {
        val postProcessors = mutableListOf<DeepClonePostProcessExpr>()
        addLine("val clone = $className(")
        indent {
          def.allFields.forEach { field ->
            val pc = fieldExprGen.fromField(field, "this.${field.name.classFieldName}.size")
            addLine("${field.name.classFieldName} = ${pc.deepCloneExpr.expr("this")},")
            pc.deepClonePostProcessExpr?.let { postProcessors.add(it) }
          }
        }
        addLine(")")
        postProcessors.forEach { postProcessor ->
          postProcessor.generate(this, "this", "clone")
        }
      }
      addLine("}")
      addLine()
      addLine("fun copyFrom(other: $className) {")
      indent {
        def.allFields.forEach { field ->
          if (field.useVal) {
            addLine("check(this.${field.name.classFieldName} == other.${field.name.classFieldName})")
          } else {
            val pc = fieldExprGen.fromField(field)
            pc.copyFromExpr.generate(this, "this", "other")
          }
        }
      }
      addLine("}")
      addLine()
      if (sealedSuper != null) {
        addLine("override fun toProto(builder: $toProtoClassName.Builder) {")
        indent {
          def.inheritedFields.forEach { field ->
            val conversionExpr = fieldExprGen.fromField(field)
            conversionExpr.toProtoExpr.generate(this, "this", "builder")
          }
          addLine("val subBuilder = builder.${sealedSuper.fieldName.classFieldName}Builder")
          def.uniqueFields.forEach { field ->
            val conversionExpr = fieldExprGen.fromField(field)
            conversionExpr.toProtoExpr.generate(this, "this", "subBuilder")
          }
        }
      } else {
        addLine("fun toProto(builder: $toProtoClassName.Builder) {")
        check(def.inheritedFields.isEmpty())
        indent {
          def.uniqueFields.forEach { field ->
            val conversionExpr = fieldExprGen.fromField(field)
            conversionExpr.toProtoExpr.generate(this, "this", "builder")
          }
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
      companion {
        addLine("val defaultValue = $enumName.${def.values.first().name.enumClassFieldName}")
        addLine()
        addLine("fun fromProto(proto: $protoTypeName): $enumName =")
        addLine("  values().find { it.tag == proto.number }!!")
      }
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
    val protoTypeName = "$protoOuterClassName$className"
    addLine("sealed class $className {")
    indent {
      def.commonFields.forEach { field ->
        addAbstractField(field)
      }
      if (def.commonFields.isNotEmpty()) {
        addLine()
      }

      addLine("abstract fun toProto(builder: $protoTypeName.Builder)")
      addLine()

      var defaultValue = ""

      def.subTypes.forEachIndexed { idx, subType ->
        when (subType) {
          is KtSealedSubType.DedicatedMessage -> {
            // do nothing - 알아서 필요한 타입이 생성되어 있음
            if (idx == 0) {
              defaultValue = "${subType.typeName.className}.create()"
            }
          }

          is KtSealedSubType.EmptySub -> {
            if (def.commonFields.isEmpty()) {
              if (idx == 0) {
                defaultValue = subType.fieldName.className
              }
              addLine("object ${subType.fieldName.className}: $className() {")
              indent {
                addLine("override fun toProto(builder: $protoTypeName.Builder) {")
                indent {
                  addLine("builder.${subType.fieldName.classFieldName}Builder")
                }
                addLine("}")
              }
              addLine("}")
              addLine()
            } else {
              if (idx == 0) {
                defaultValue = "${subType.fieldName.className}.create()"
              }
              addLine("data class ${subType.fieldName.className}(")
              indent {
                def.commonFields.forEach { field ->
                  addField(field, true)
                }
              }
              addLine("): $className() {")
              indent {
                companion {
                  addLine("fun create(")
                  indent {
                    def.commonFields.forEach { field ->
                      val ts = tsGen.fromType(field.type)
                      addLine("${field.name.classFieldName}: ${ts.typeString} = ${ts.defaultValue},")
                    }
                  }
                  addLine(") = ${subType.fieldName.className}(")
                  indent {
                    def.commonFields.forEach { field ->
                      addLine("${field.name.classFieldName},")
                    }
                  }
                  addLine(")")
                }
                addLine()
                addLine("override fun toProto(builder: $protoTypeName.Builder) {")
                indent {
                  def.commonFields.forEach { field ->
                    val pc = fieldExprGen.fromField(field)
                    pc.toProtoExpr.generate(this, "this", "builder")
                  }
                  addLine("builder.${subType.fieldName.classFieldName}Builder")
                }
                addLine("}")
              }
              addLine("}")
              addLine()
            }
          }

          is KtSealedSubType.SingleSub -> {
            if (idx == 0) {
              defaultValue = "${subType.fieldName.className}.create()"
            }
            addLine("data class ${subType.fieldName.className}(")
            indent {
              def.commonFields.forEach { field ->
                addField(field, true)
              }
              addField(subType.fieldDef, false)
            }
            addLine("): $className() {")
            indent {
              companion {
                addLine("fun create() = ${subType.fieldName.className}(")
                indent {
                  def.commonFields.forEach { field ->
                    val ts = tsGen.fromType(field.type)
                    addLine("${field.name.classFieldName} = ${ts.defaultValue},")
                  }
                  val ts = tsGen.fromType(subType.fieldDef.type)
                  addLine("${subType.fieldDef.name.classFieldName} = ${ts.defaultValue},")
                }
                addLine(")")
              }
              addLine()
              addLine("override fun toProto(builder: $protoTypeName.Builder) {")
              indent {
                def.commonFields.forEach { field ->
                  val pc = fieldExprGen.fromField(field)
                  pc.toProtoExpr.generate(this, "this", "builder")
                }
                val pc = fieldExprGen.fromField(subType.fieldDef)
                pc.toProtoExpr.generate(this, "this", "builder")
              }
              addLine("}")
            }
            addLine("}")
            addLine()
          }
        }
      }
      companion {
        addLine("fun create() = $defaultValue")
        addLine()
        addLine("fun fromProto(proto: $protoTypeName): $className {")
        indent {
          val postProcessors = mutableListOf<FromProtoPostProcessExpr>()
          def.commonFields.forEach { field ->
            val expr = fieldExprGen.fromField(field)
            val initExpr = expr.fromProtoExpr.expr("proto")
            addLine("val ${field.name.classFieldName} = $initExpr")
            expr.fromProtoPostProcessExpr?.let { postProcessors.add(it) }
          }
          addLine("val instance = when(proto.${def.name.classFieldName}Case) {")
          indent {
            val commonFields = def.commonFields.map { it.name.classFieldName }
            def.subTypes.forEach { subType ->
              addLine("$protoTypeName.${def.name.className}Case.${subType.fieldName.capitalSnakeCase()} ->")
              indent {
                when (subType) {
                  is KtSealedSubType.DedicatedMessage -> {
                    addLine(
                      "${subType.typeName.className}.fromProto(${
                        (commonFields + "proto.${subType.fieldName.classFieldName}").joinToString(", ")
                      })"
                    )
                  }

                  is KtSealedSubType.EmptySub -> {
                    if (def.commonFields.isEmpty()) {
                      addLine(subType.fieldName.className)
                    } else {
                      addLine("${subType.fieldName.className}(${commonFields.joinToString(", ")})")
                    }
                  }

                  is KtSealedSubType.SingleSub -> {
                    val conversionExpr = fieldExprGen.fromField(subType.fieldDef)
                    val initExpr = conversionExpr.fromProtoExpr.expr("proto")
                    // post process?
                    check(conversionExpr.fromProtoPostProcessExpr == null)
                    val params = commonFields + initExpr
                    addLine("${subType.fieldName.className}(${params.joinToString(", ")})")
                  }
                }
              }
            }
            addLine("else -> TODO()")
          }
          addLine("}")
          postProcessors.forEach { postProcessor ->
            postProcessor.generate(this, "proto", "instance")
          }
          addLine("return instance")
        }
        addLine("}")
      }
      addLine()
      addComments(def.trailingComments)
    }
    addLine("}")
  }

  fun generate(): String {
    addComments(defs.comments)

    if (defs.kotlinPackageName != null) {
      addLine("package ${defs.kotlinPackageName}")
      addLine()
    }

    if (gdxMode) {
      addLine("import com.badlogic.gdx.utils.Array as GdxArray")
      addLine("import com.badlogic.gdx.utils.IntArray as GdxIntArray")
      addLine("import com.badlogic.gdx.utils.LongArray as GdxLongArray")
      addLine("import com.badlogic.gdx.utils.FloatArray as GdxFloatArray")
      addLine("import com.badlogic.gdx.utils.IntIntMap")
      addLine("import com.badlogic.gdx.utils.IntFloatMap")
      addLine("import com.badlogic.gdx.utils.IntMap")
      addLine("import com.badlogic.gdx.utils.LongMap")
      addLine("import com.badlogic.gdx.utils.ObjectIntMap")
      addLine("import com.badlogic.gdx.utils.ObjectLongMap")
      addLine("import com.badlogic.gdx.utils.ObjectFloatMap")
      addLine("import com.badlogic.gdx.utils.ObjectMap")
      addLine("import com.badlogic.gdx.utils.IntSet as GdxIntSet")
      addLine("import com.badlogic.gdx.utils.ObjectSet")
      addLine("import com.giyeok.msspgame.libgdx.forEach")
    }
    (imports + defs.kotlinImports).toList().sorted().forEach { import ->
      addLine("import $import")
    }
    if (gdxMode || (imports + defs.kotlinImports).isNotEmpty()) {
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
