package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.proto.AtomicType
import com.giyeok.sugarproto.proto.ValueType

class ProtoConversionExprGen(
  val tsGen: TypeStringGen,
  val protoOuterClassName: String,
) {
  // repeated<atomic>:
  // fromProto(proto): <fieldName> = GdxArray(proto.<fieldName>Count)
  // post: proto.<fieldName>List.forEach { elem ->
  // prim:   instance.<fieldName>.add(elem)
  // enum:   instance.<fieldName>.add(<TypeName>.fromProto(elem))
  // msgs:   instance.<fieldName>.add(<TypeName>.fromProto(elem))
  //       }
  // toProto(builder): this.<fieldName>List.forEach { elem ->
  // prim:               builder.add<FieldName>(elem)
  // enum:               builder.add<FieldName>(elem.toProto())
  // msgs:               <TypeName>.toProto(builder.add<FieldName>Builder)
  //                   }

  // optional<atomic>:
  // fromProto(proto): <fieldName> = if (!proto.has<FieldName>()) null else <<fromProto(elem)>>
  // toProto(builder):
  // prim: this.<fieldName>?.let { value -> builder.<fieldName> = value } ?: builder.clear<FieldName>()
  // enum: this.<fieldName>?.let { value -> builder.<fieldName> = value.toProto() } ?: builder.clear<FieldName>()
  // msgs: this.<fieldName>?.toProto(builder.<fieldName>Builder) ?: builder.clear<FieldName>()

  // map<prim, atomic>:
  // fromProto(proto): <fieldName> = GdxIntMap(proto.<fieldName>Count)
  // post: proto.<fieldName>Map.forEach { entry ->
  // prim:   instance.<fieldName>.put(entry.key, entry.value)
  // enum:   instance.<fieldName>.put(entry.key, <TypeName>.fromProto(entry.value))
  // msgs:   instance.<fieldName>.put(entry.key, <TypeName>.fromProto(entry.value))
  //       }
  // toProto(builder): this.<fieldName>Map.forEach { entry ->
  // prim:               builder.put<FieldName>(entry.key, entry.value)
  // enum:               builder.put<FieldName>(entry.key, entry.value.toProto())
  // msgs:               val valueBuilder = <ProtoTypeName>.newBuilder()
  //                     entry.value.toProto(valueBuilder)
  //                     builder.put<FieldName>(entry.key, valueBuilder.build())
  //                   }

  // atomic = prim, enum, message/sealed, empty
  // prim:
  // fromProto(proto): <fieldName> = proto.<fieldName>
  // toProto(builder): builder.<fieldName> = this.<fieldName>
  // enum:
  // fromProto(proto): <fieldName> = <TypeName>.fromProto(proto.<fieldName>)
  // toProto(builder): builder.<fieldName> = this.<fieldName>.toProto()
  // message/sealed:
  // fromProto(proto): <fieldName> = <TypeName>.fromProto(proto.<fieldName>)
  // toProto(builder): this.<fieldName>.toProto(builder.<fieldName>Builder)
  // empty: unsupported

  fun fromField(fieldDef: KtFieldDef, collectionSizeHint: String? = null): ProtoConversionExpr {
    fun fromProtoPattern(typeName: String, fieldName: String): ProtoConversionExpr =
      ProtoConversionExpr(
        FromProtoExpr.FromProto(
          typeName,
          FromProtoExpr.SimpleGetterExpr(fieldName)
        ),
        null,
        ToProtoExpr.Delegate(fieldName, fieldName + "Builder", false),
      )

    return when (fieldDef.type) {
      AtomicType.EmptyType -> throw IllegalStateException("Unsupported? maybe?")

      is AtomicType.UnknownName ->
        throw IllegalStateException("Unsupported? maybe? ${fieldDef.type.name}")

      is AtomicType.MessageRefType ->
        fromProtoPattern(fieldDef.type.refName, fieldDef.name.classFieldName)

      is AtomicType.EnumRefType ->
        ProtoConversionExpr(
          FromProtoExpr.FromProto(
            fieldDef.type.refName,
            FromProtoExpr.SimpleGetterExpr(fieldDef.name.classFieldName)
          ),
          null,
          ToProtoExpr.ToProtoSimpleSet(fieldDef.name.classFieldName),
        )

      is AtomicType.PrimitiveType ->
        // progress = proto.progress
        ProtoConversionExpr(
          FromProtoExpr.SimpleGetterExpr(fieldDef.name.classFieldName),
          null,
          ToProtoExpr.SimpleSet(fieldDef.name.classFieldName),
        )

      is ValueType.MapType -> {
        val valueBody = when (fieldDef.type.valueType) {
          AtomicType.EmptyType -> TODO()

          is AtomicType.PrimitiveType ->
            FromProtoExpr.SimpleGetterExpr(fieldDef.name.classFieldName)

          is AtomicType.MessageRefType ->
            FromProtoExpr.FromProto(fieldDef.type.valueType.refName, FromProtoExpr.Expr)

          is AtomicType.EnumRefType ->
            FromProtoExpr.FromProto(
              fieldDef.type.valueType.refName,
              FromProtoExpr.SimpleGetterExpr(fieldDef.name.classFieldName)
            )
        }

        val setter: ToProtoExpr = when (fieldDef.type.valueType) {
          AtomicType.EmptyType -> TODO()

          is AtomicType.PrimitiveType ->
            ToProtoExpr.PutElem(
              "put${fieldDef.name.capitalClassFieldName}",
              fieldDef.name.classFieldName
            )

          is AtomicType.EnumRefType ->
            ToProtoExpr.PutToProtoElem(
              "put${fieldDef.name.capitalClassFieldName}",
              fieldDef.name.classFieldName
            )

          is AtomicType.MessageRefType ->
            ToProtoExpr.PutElemDelegate(
              "put${fieldDef.name.capitalClassFieldName}",
              protoOuterClassName + fieldDef.type.valueType.refName,
            )
        }

        ProtoConversionExpr(
          FromProtoExpr.Const(tsGen.fromType(fieldDef.type, collectionSizeHint).defaultValue),
          FromProtoPostProcessExpr.ForEachMap(
            fieldDef.name.classFieldName + "Map",
            fieldDef.name.classFieldName,
            valueBody,
          ),
          ToProtoExpr.ForEach(fieldDef.name.classFieldName, setter),
        )
      }

      is ValueType.OptionalType -> {
        when (fieldDef.type.elemType) {
          AtomicType.EmptyType -> TODO()

          is AtomicType.MessageRefType ->
            // water = if (!proto.hasWater()) null else TileWater.fromProto(proto.water)
            ProtoConversionExpr(
              FromProtoExpr.IfHas(
                "has${fieldDef.name.capitalClassFieldName}", FromProtoExpr.FromProto(
                  fieldDef.type.elemType.refName,
                  FromProtoExpr.SimpleGetterExpr(fieldDef.name.classFieldName)
                )
              ),
              null,
              ToProtoExpr.Delegate(
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
        val getter: FromProtoPostProcessExpr = when (fieldDef.type.elemType) {
          AtomicType.EmptyType -> TODO()

          is AtomicType.PrimitiveType ->
            FromProtoPostProcessExpr.Add(fieldDef.name.classFieldName)

          is AtomicType.MessageRefType ->
            FromProtoPostProcessExpr.AddFromProto(
              fieldDef.name.classFieldName,
              fieldDef.type.elemType.refName
            )

          is AtomicType.EnumRefType ->
            FromProtoPostProcessExpr.AddFromProto(
              fieldDef.name.classFieldName,
              fieldDef.type.elemType.refName,
            )
        }

        val setter: ToProtoExpr = when (fieldDef.type.elemType) {
          AtomicType.EmptyType -> TODO()

          is AtomicType.MessageRefType ->
            ToProtoExpr.AddElemDelegate("add" + fieldDef.name.capitalClassFieldName + "Builder")

          is AtomicType.PrimitiveType ->
            ToProtoExpr.AddElem("add" + fieldDef.name.capitalClassFieldName)

          is AtomicType.EnumRefType ->
            ToProtoExpr.AddToProtoElem("add" + fieldDef.name.capitalClassFieldName)
        }

        ProtoConversionExpr(
          FromProtoExpr.Const(tsGen.fromType(fieldDef.type, collectionSizeHint).defaultValue),
          FromProtoPostProcessExpr.ForEachList(
            fieldDef.name.classFieldName + "List",
            getter
          ),
          ToProtoExpr.ForEach(
            fieldDef.name.classFieldName,
            setter,
          ),
        )
      }
    }
  }
}

data class ProtoConversionExpr(
  val fromProtoExpr: FromProtoExpr,
  val fromProtoPostProcessExpr: FromProtoPostProcessExpr?,
  val toProtoExpr: ToProtoExpr,
)

sealed class FromProtoExpr {
  abstract fun expr(protoExpr: String): String

  data class Const(val const: String): FromProtoExpr() {
    override fun expr(protoExpr: String): String = const
  }

  object Expr: FromProtoExpr() {
    override fun expr(protoExpr: String): String = protoExpr
  }

  data class SimpleGetterExpr(val fieldName: String): FromProtoExpr() {
    override fun expr(protoExpr: String): String =
      "$protoExpr.$fieldName"
  }

  data class FromProto(val typeName: String, val src: FromProtoExpr): FromProtoExpr() {
    override fun expr(protoExpr: String): String {
      val targetExpr = src.expr(protoExpr)
      return "$typeName.fromProto($targetExpr)"
    }
  }

  data class IfHas(val hasFieldFuncName: String, val body: FromProtoExpr): FromProtoExpr() {
    override fun expr(protoExpr: String): String {
      val targetExpr = body.expr(protoExpr)
      return "if (!$protoExpr.$hasFieldFuncName()) null else $targetExpr"
    }
  }
}

sealed class FromProtoPostProcessExpr {
  abstract fun generate(gen: MutableKtDataClassGen, protoExpr: String, instanceExpr: String)

  data class ForEachList(val inputFieldName: String, val body: FromProtoPostProcessExpr):
    FromProtoPostProcessExpr() {
    override fun generate(gen: MutableKtDataClassGen, protoExpr: String, instanceExpr: String) {
      gen.addLine("$protoExpr.$inputFieldName.forEach { elem ->")
      gen.indent {
        body.generate(gen, "elem", instanceExpr)
      }
      gen.addLine("}")
    }
  }

  data class ForEachMap(
    val inputFieldName: String,
    val fieldName: String,
    val valueBody: FromProtoExpr
  ): FromProtoPostProcessExpr() {
    override fun generate(gen: MutableKtDataClassGen, protoExpr: String, instanceExpr: String) {
      gen.addLine("$protoExpr.$inputFieldName.forEach { entry ->")
      val valueBodyLines = valueBody.expr("entry.value")
      gen.indent {
        gen.addLine("$instanceExpr.$fieldName.put(entry.key, ${valueBodyLines.first()})")
      }
      gen.addLine("}")
    }
  }

  data class Add(val fieldName: String): FromProtoPostProcessExpr() {
    override fun generate(gen: MutableKtDataClassGen, protoExpr: String, instanceExpr: String) {
      gen.addLine("$instanceExpr.$fieldName.add($protoExpr)")
    }
  }

  data class AddFromProto(val fieldName: String, val typ: String): FromProtoPostProcessExpr() {
    override fun generate(gen: MutableKtDataClassGen, protoExpr: String, instanceExpr: String) {
      gen.addLine("$instanceExpr.$fieldName.add($typ.fromProto($protoExpr))")
    }
  }
}

sealed class ToProtoExpr {
  abstract fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String)

  data class SimpleSet(val fieldName: String): ToProtoExpr() {
    override fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$fieldName = $thisExpr.$fieldName")
    }
  }

  data class ToProtoSimpleSet(val fieldName: String): ToProtoExpr() {
    override fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$fieldName = $thisExpr.$fieldName.toProto()")
    }
  }

  data class Delegate(
    val thisFieldName: String,
    val builderFieldName: String,
    val optional: Boolean
  ): ToProtoExpr() {
    override fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      val opt = if (optional) "?" else ""
      gen.addLine("$thisExpr.$thisFieldName$opt.toProto($builderExpr.$builderFieldName)")
    }
  }

  data class AddElem(val addFuncName: String): ToProtoExpr() {
    override fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$addFuncName($thisExpr)")
    }
  }

  data class AddToProtoElem(val addFuncName: String): ToProtoExpr() {
    override fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$addFuncName($thisExpr.toProto())")
    }
  }

  data class PutElem(val putFuncName: String, val thisFieldName: String): ToProtoExpr() {
    override fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$putFuncName($thisExpr.$thisFieldName)")
    }
  }

  data class PutToProtoElem(val putFuncName: String, val thisFieldName: String): ToProtoExpr() {
    override fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.$putFuncName($thisExpr.$thisFieldName.toProto())")
    }
  }

  data class AddElemDelegate(val addBuilderName: String): ToProtoExpr() {
    override fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$thisExpr.toProto($builderExpr.$addBuilderName())")
    }
  }

  data class PutElemDelegate(val putFuncName: String, val protoTypeName: String):
    ToProtoExpr() {
    override fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      // val valueBuilder = WorldProto.WaterMass.newBuilder()
      // entry.value.toProto(valueBuilder)
      // builder.putWaterMasses(entry.key, valueBuilder.build())
      gen.addLine("val valueBuilder = $protoTypeName.newBuilder()")
      gen.addLine("$thisExpr.value.toProto(valueBuilder)")
      gen.addLine("$builderExpr.$putFuncName($thisExpr.key, valueBuilder.build())")
    }
  }

  data class ForEach(
    val thisFieldName: String,
    val body: ToProtoExpr,
  ): ToProtoExpr() {
    override fun expr(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$thisExpr.$thisFieldName.forEach { elem ->")
      gen.indent {
        body.expr(gen, "elem", builderExpr)
      }
      gen.addLine("}")
    }
  }
}
