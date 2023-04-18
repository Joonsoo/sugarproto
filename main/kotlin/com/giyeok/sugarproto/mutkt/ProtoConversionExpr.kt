package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.proto.AtomicType
import com.giyeok.sugarproto.proto.ValueType

data class ProtoConversionExpr(
  val initExpr: ProtoGetterExpr,
  val initPostProcessorExpr: ProtoPostProcessorExpr?,
  val toProtoExpr: ProtoSetterExpr,
) {
  companion object {
    fun fromField(fieldDef: KtFieldDef, tsGen: TypeStringGen): ProtoConversionExpr {
      fun fromProtoPattern(typeName: String, fieldName: String): ProtoConversionExpr =
        ProtoConversionExpr(
          ProtoGetterExpr.FromProto(
            typeName,
            ProtoGetterExpr.SimpleGetterExpr(fieldName)
          ),
          null,
          ProtoSetterExpr.Delegate(fieldName, fieldName + "Builder", false),
        )

      return when (fieldDef.type) {
        AtomicType.EmptyType -> throw IllegalStateException("Unsupported? maybe?")

        is AtomicType.UnknownName ->
          throw IllegalStateException("Unsupported? maybe? ${fieldDef.type.name}")

        is AtomicType.NameRefType ->
          fromProtoPattern(fieldDef.type.refName, fieldDef.name.classFieldName)

        is AtomicType.PrimitiveType ->
          // progress = proto.progress
          ProtoConversionExpr(
            ProtoGetterExpr.SimpleGetterExpr(fieldDef.name.classFieldName),
            null,
            ProtoSetterExpr.SimpleSet(fieldDef.name.classFieldName),
          )

        is ValueType.MapType -> {
          val valueBody = when (fieldDef.type.valueType) {
            AtomicType.EmptyType -> TODO()

            is AtomicType.PrimitiveType ->
              ProtoGetterExpr.SimpleGetterExpr(fieldDef.name.classFieldName)

            is AtomicType.NameRefType ->
              ProtoGetterExpr.FromProto(
                fieldDef.type.valueType.refName,
                ProtoGetterExpr.SimpleGetterExpr(fieldDef.name.classFieldName)
              )
          }

          val setter: ProtoSetterExpr = when (fieldDef.type.valueType) {
            AtomicType.EmptyType -> TODO()

            is AtomicType.PrimitiveType ->
              ProtoSetterExpr.PutElem(
                "put${fieldDef.name.capitalClassFieldName}",
                fieldDef.name.classFieldName
              )

            is AtomicType.NameRefType ->
              ProtoSetterExpr.PutElemDelegate(
                "put${fieldDef.name.capitalClassFieldName}",
                fieldDef.type.valueType.refName,
              )
          }

          ProtoConversionExpr(
            ProtoGetterExpr.Const(tsGen.fromType(fieldDef.type).defaultValue),
            ProtoPostProcessorExpr.ForEach(
              fieldDef.name.classFieldName + "Map",
              ProtoPostProcessorExpr.Put(fieldDef.name.classFieldName, valueBody)
            ),
            ProtoSetterExpr.ForEach(fieldDef.name.classFieldName, setter),
          )
        }

        is ValueType.OptionalType -> {
          when (fieldDef.type.elemType) {
            AtomicType.EmptyType -> TODO()
            is AtomicType.NameRefType ->
              // water = if (!proto.hasWater()) null else TileWater.fromProto(proto.water)
              ProtoConversionExpr(
                ProtoGetterExpr.IfHas(
                  "has${fieldDef.name.capitalClassFieldName}", ProtoGetterExpr.FromProto(
                    fieldDef.type.elemType.refName,
                    ProtoGetterExpr.SimpleGetterExpr(fieldDef.name.classFieldName)
                  )
                ),
                null,
                ProtoSetterExpr.Delegate(
                  fieldDef.name.classFieldName,
                  fieldDef.name.classFieldName + "Builder",
                  true
                ),
              )

            is AtomicType.PrimitiveType -> TODO()
          }
        }

        is ValueType.RepeatedType -> {
          val elemAdder: ProtoSetterExpr = when (fieldDef.type.elemType) {
            AtomicType.EmptyType -> TODO()

            is AtomicType.NameRefType ->
              ProtoSetterExpr.AddElemDelegate("add" + fieldDef.name.capitalClassFieldName + "Builder")

            is AtomicType.PrimitiveType -> ProtoSetterExpr.AddElem(fieldDef.name.classFieldName)
          }

          ProtoConversionExpr(
            ProtoGetterExpr.Const(tsGen.fromType(fieldDef.type).defaultValue),
            ProtoPostProcessorExpr.ForEach(
              fieldDef.name.classFieldName + "List",
              ProtoPostProcessorExpr.Add(fieldDef.name.classFieldName)
            ),
            ProtoSetterExpr.ForEach(
              fieldDef.name.classFieldName,
              elemAdder,
            ),
          )
        }
      }
    }
  }
}

sealed class ProtoGetterExpr {
  abstract fun expr(expr: String): List<String>

  data class Const(val const: String): ProtoGetterExpr() {
    override fun expr(expr: String): List<String> = listOf(const)
  }

  object Expr: ProtoGetterExpr() {
    override fun expr(expr: String): List<String> = listOf(expr)
  }

  data class SimpleGetterExpr(val fieldName: String): ProtoGetterExpr() {
    override fun expr(expr: String): List<String> =
      listOf("$expr.$fieldName")
  }

  data class FromProto(val typeName: String, val src: ProtoGetterExpr): ProtoGetterExpr() {
    override fun expr(expr: String): List<String> {
      val targetExpr = src.expr(expr)
      check(targetExpr.size == 1)
      return listOf("$typeName.fromProto(${targetExpr.first()})")
    }
  }

  data class When(val target: ProtoGetterExpr, val cases: List<Pair<String, ProtoGetterExpr>>):
    ProtoGetterExpr() {
    override fun expr(expr: String): List<String> {
      val targetExpr = target.expr(expr)
      // 여러개인 경우는.. 생기려나 모르곘네
      check(targetExpr.size == 1)
      val list = mutableListOf("when (${targetExpr.first()}) {")
      cases.forEach { (case, body) ->
        list.add("$case ->")
        list.addAll(body.expr(expr).map { "  $it" })
      }
      list.add("}")
      return list
    }
  }

  data class IfHas(val hasFieldFuncName: String, val body: ProtoGetterExpr): ProtoGetterExpr() {
    override fun expr(expr: String): List<String> {
      val targetExpr = body.expr(expr)
      check(targetExpr.size == 1)
      return listOf("if (!$expr.$hasFieldFuncName()) null else ${targetExpr.first()}")
    }
  }
}

sealed class ProtoPostProcessorExpr {
  abstract fun expr(gen: MutableKtDataClassGen, input: String, instanceExpr: String)

  data class ForEach(val inputFieldName: String, val body: ProtoPostProcessorExpr):
    ProtoPostProcessorExpr() {
    override fun expr(gen: MutableKtDataClassGen, input: String, instanceExpr: String) {
      gen.addLine("$input.$inputFieldName.forEach { elem ->")
      gen.indent {
        body.expr(gen, "elem", instanceExpr)
      }
      gen.addLine("}")
    }
  }

  data class Add(val fieldName: String): ProtoPostProcessorExpr() {
    override fun expr(gen: MutableKtDataClassGen, input: String, instanceExpr: String) {
      gen.addLine("$instanceExpr.$fieldName.add($input)")
    }
  }

  data class Put(val fieldName: String, val valueBody: ProtoGetterExpr):
    ProtoPostProcessorExpr() {
    override fun expr(gen: MutableKtDataClassGen, input: String, instanceExpr: String) {
      val valueBodyLines = valueBody.expr("$input.value")
      check(valueBodyLines.size == 1)

      gen.addLine("$instanceExpr.$fieldName.put($input.key, ${valueBodyLines.first()})")
    }
  }
}

sealed class ProtoSetterExpr {
  abstract fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String)

  data class SimpleSet(val fieldName: String): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      gen.addLine("$inputExpr.$fieldName = $builderExpr.$fieldName")
    }
  }

  data class Delegate(
    val thisFieldName: String,
    val builderFieldName: String,
    val optional: Boolean
  ): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      val opt = if (optional) "?" else ""
      gen.addLine("$inputExpr.$thisFieldName$opt.toProto($builderExpr.$builderFieldName)")
    }
  }

  data class AddElem(val addFuncName: String): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$addFuncName($inputExpr)")
    }
  }

  data class PutElem(val putFuncName: String, val thisFieldName: String): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$putFuncName($inputExpr.$thisFieldName)")
    }
  }

  data class AddElemDelegate(val addBuilderName: String): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      gen.addLine("$inputExpr.toProto($builderExpr.$addBuilderName())")
    }
  }

  data class PutElemDelegate(val putFuncName: String, val protoTypeName: String):
    ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      // val valueBuilder = WorldProto.WaterMass.newBuilder()
      // entry.value.toProto(valueBuilder)
      // builder.putWaterMasses(entry.key, valueBuilder.build())
      gen.addLine("val valueBuilder = $protoTypeName.newBuilder()")
      gen.addLine("$inputExpr.value.toProto(valuBuilder)")
      gen.addLine("$builderExpr.$putFuncName($inputExpr.key, valueBuilder.build())")
    }
  }

  data class ForEach(
    val thisFieldName: String,
    val body: ProtoSetterExpr,
  ): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      gen.addLine("$inputExpr.$thisFieldName.forEach { elem ->")
      gen.indent {
        body.expr(gen, "elem", builderExpr)
      }
      gen.addLine("}")
    }
  }
}
