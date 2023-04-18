package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.proto.AtomicType
import com.giyeok.sugarproto.proto.ValueType

class ProtoConversionExprGen(
  val tsGen: TypeStringGen,
  val protoOuterClassName: String,
) {
  fun fromField(fieldDef: KtFieldDef): ProtoConversionExpr {
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

      is AtomicType.MessageRefType ->
        fromProtoPattern(fieldDef.type.refName, fieldDef.name.classFieldName)

      is AtomicType.EnumRefType ->
        ProtoConversionExpr(
          ProtoGetterExpr.FromProto(fieldDef.type.refName, ProtoGetterExpr.SimpleGetterExpr(fieldDef.name.classFieldName)),
          null,
          ProtoSetterExpr.ToProtoSimpleSet(fieldDef.name.classFieldName),
        )

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

          is AtomicType.MessageRefType ->
            ProtoGetterExpr.FromProto(fieldDef.type.valueType.refName, ProtoGetterExpr.Expr)

          is AtomicType.EnumRefType ->
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

          is AtomicType.EnumRefType ->
            ProtoSetterExpr.PutToProtoElem(
              "put${fieldDef.name.capitalClassFieldName}",
              fieldDef.name.classFieldName
            )

          is AtomicType.MessageRefType ->
            ProtoSetterExpr.PutElemDelegate(
              "put${fieldDef.name.capitalClassFieldName}",
              protoOuterClassName + fieldDef.type.valueType.refName,
            )
        }

        ProtoConversionExpr(
          ProtoGetterExpr.Const(tsGen.fromType(fieldDef.type).defaultValue),
          ProtoPostProcessorExpr.ForEachMap(
            fieldDef.name.classFieldName + "Map",
            fieldDef.name.classFieldName,
            valueBody,
          ),
          ProtoSetterExpr.ForEach(fieldDef.name.classFieldName, setter),
        )
      }

      is ValueType.OptionalType -> {
        when (fieldDef.type.elemType) {
          AtomicType.EmptyType -> TODO()

          is AtomicType.MessageRefType ->
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

          is AtomicType.EnumRefType -> TODO()

          is AtomicType.PrimitiveType -> TODO()
        }
      }

      is ValueType.RepeatedType -> {
        val getter: ProtoPostProcessorExpr = when (fieldDef.type.elemType) {
          AtomicType.EmptyType -> TODO()

          is AtomicType.PrimitiveType ->
            ProtoPostProcessorExpr.Add(fieldDef.name.classFieldName)

          is AtomicType.MessageRefType ->
            ProtoPostProcessorExpr.AddFromProto(
              fieldDef.name.classFieldName,
              fieldDef.type.elemType.refName
            )

          is AtomicType.EnumRefType ->
            ProtoPostProcessorExpr.AddFromProto(
              fieldDef.name.classFieldName,
              fieldDef.type.elemType.refName,
            )
        }

        val setter: ProtoSetterExpr = when (fieldDef.type.elemType) {
          AtomicType.EmptyType -> TODO()

          is AtomicType.MessageRefType ->
            ProtoSetterExpr.AddElemDelegate("add" + fieldDef.name.capitalClassFieldName + "Builder")

          is AtomicType.PrimitiveType ->
            ProtoSetterExpr.AddElem("add" + fieldDef.name.classFieldName)

          is AtomicType.EnumRefType ->
            ProtoSetterExpr.AddToProtoElem("add" + fieldDef.name.classFieldName)
        }

        ProtoConversionExpr(
          ProtoGetterExpr.Const(tsGen.fromType(fieldDef.type).defaultValue),
          ProtoPostProcessorExpr.ForEachList(
            fieldDef.name.classFieldName + "List",
            getter
          ),
          ProtoSetterExpr.ForEach(
            fieldDef.name.classFieldName,
            setter,
          ),
        )
      }
    }
  }
}

data class ProtoConversionExpr(
  val initExpr: ProtoGetterExpr,
  val initPostProcessorExpr: ProtoPostProcessorExpr?,
  val toProtoExpr: ProtoSetterExpr,
)

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

  data class ForEachList(val inputFieldName: String, val body: ProtoPostProcessorExpr):
    ProtoPostProcessorExpr() {
    override fun expr(gen: MutableKtDataClassGen, input: String, instanceExpr: String) {
      gen.addLine("$input.$inputFieldName.forEach { elem ->")
      gen.indent {
        body.expr(gen, "elem", instanceExpr)
      }
      gen.addLine("}")
    }
  }

  data class ForEachMap(
    val inputFieldName: String,
    val fieldName: String,
    val valueBody: ProtoGetterExpr
  ): ProtoPostProcessorExpr() {
    override fun expr(gen: MutableKtDataClassGen, input: String, instanceExpr: String) {
      gen.addLine("$input.$inputFieldName.forEach { entry ->")
      val valueBodyLines = valueBody.expr("entry.value")
      gen.indent {
        gen.addLine("$instanceExpr.$fieldName.put(entry.key, ${valueBodyLines.first()})")
      }
      gen.addLine("}")
    }
  }

  data class Add(val fieldName: String): ProtoPostProcessorExpr() {
    override fun expr(gen: MutableKtDataClassGen, input: String, instanceExpr: String) {
      gen.addLine("$instanceExpr.$fieldName.add($input)")
    }
  }

  data class AddFromProto(val fieldName: String, val typ: String): ProtoPostProcessorExpr() {
    override fun expr(gen: MutableKtDataClassGen, input: String, instanceExpr: String) {
      gen.addLine("$instanceExpr.$fieldName.add($typ.fromProto($input))")
    }
  }
}

sealed class ProtoSetterExpr {
  abstract fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String)

  data class SimpleSet(val fieldName: String): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$fieldName = $inputExpr.$fieldName")
    }
  }

  data class ToProtoSimpleSet(val fieldName: String): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$fieldName = $inputExpr.$fieldName.toProto()")
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

  data class AddToProtoElem(val addFuncName: String): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$addFuncName($inputExpr.toProto())")
    }
  }

  data class PutElem(val putFuncName: String, val thisFieldName: String): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$putFuncName($inputExpr.$thisFieldName)")
    }
  }

  data class PutToProtoElem(val putFuncName: String, val thisFieldName: String): ProtoSetterExpr() {
    override fun expr(gen: MutableKtDataClassGen, inputExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$putFuncName($inputExpr.$thisFieldName.toProto())")
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
      gen.addLine("$inputExpr.value.toProto(valueBuilder)")
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
