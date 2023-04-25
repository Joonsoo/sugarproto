package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.name.SemanticName
import com.giyeok.sugarproto.proto.AtomicType
import com.giyeok.sugarproto.proto.ValueType

class FieldExprGen(
  val tsGen: TypeStringGen,
  val protoOuterClassName: String,
) {
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

  // optional<atomic>:
  // fromProto(proto): <fieldName> = if (!proto.has<FieldName>()) null else <<fromProto(elem)>>
  // toProto(builder):
  // prim: this.<fieldName>?.let { value -> builder.<fieldName> = value } ?: builder.clear<FieldName>()
  // enum: this.<fieldName>?.let { value -> builder.<fieldName> = value.toProto() } ?: builder.clear<FieldName>()
  // msgs: this.<fieldName>?.toProto(builder.<fieldName>Builder) ?: builder.clear<FieldName>()

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
  // msgs:               elem.toProto(builder.add<FieldName>Builder())
  //                   }

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

  fun fromField(fieldDef: KtFieldDef, collectionSizeHint: String? = null): FieldExprs {
    return when (fieldDef.type) {
      AtomicType.EmptyType, is AtomicType.UnknownName ->
        throw IllegalStateException("Unsupported? maybe? ${fieldDef.type}")

      is AtomicType.PrimitiveType ->
        FieldExprs(
          FromProtoExpr.PrimFromProto(fieldDef.name),
          null,
          ToProtoExpr.PrimToProto(fieldDef.name),
          DeepCloneExpr.PlainDeepClone(fieldDef.name),
          null,
          CopyFromExpr.PlainCopyFrom(fieldDef.name),
        )

      is AtomicType.EnumType ->
        FieldExprs(
          FromProtoExpr.EnumFromProto(fieldDef.name, fieldDef.type.name),
          null,
          ToProtoExpr.EnumToProto(fieldDef.name),
          DeepCloneExpr.PlainDeepClone(fieldDef.name),
          null,
          CopyFromExpr.PlainCopyFrom(fieldDef.name),
        )

      is AtomicType.MessageOrSealedType ->
        FieldExprs(
          FromProtoExpr.MessageFromProto(fieldDef.name, fieldDef.type.name),
          null,
          ToProtoExpr.MessageToProto(fieldDef.name),
          DeepCloneExpr.MessageDeepClone(fieldDef.name),
          null,
          CopyFromExpr.MessageCopyFrom(fieldDef.name)
        )

      is ValueType.OptionalType -> {
        when (val elemType = fieldDef.type.elemType) {
          AtomicType.EmptyType, is AtomicType.UnknownName -> TODO()

          is AtomicType.PrimitiveType ->
            FieldExprs(
              FromProtoExpr.OptionalFromProto(
                fieldDef.name,
                FromProtoExpr.PrimFromProto(fieldDef.name)
              ),
              null,
              ToProtoExpr.PrimOptionalToProto(fieldDef.name),
              DeepCloneExpr.Optional(fieldDef.name, false),
              null,
              CopyFromExpr.PlainCopyFrom(fieldDef.name),
            )

          is AtomicType.EnumType ->
            FieldExprs(
              FromProtoExpr.OptionalFromProto(
                fieldDef.name,
                FromProtoExpr.EnumFromProto(fieldDef.name, elemType.name)
              ),
              null,
              ToProtoExpr.EnumOptionalToProto(fieldDef.name),
              DeepCloneExpr.Optional(fieldDef.name, false),
              null,
              CopyFromExpr.PlainCopyFrom(fieldDef.name),
            )

          is AtomicType.MessageOrSealedType ->
            FieldExprs(
              FromProtoExpr.OptionalFromProto(
                fieldDef.name,
                FromProtoExpr.MessageFromProto(fieldDef.name, elemType.name)
              ),
              null,
              ToProtoExpr.MessageOptionalToProto(fieldDef.name),
              DeepCloneExpr.Optional(fieldDef.name, true),
              null,
              CopyFromExpr.MessageOptionalCopyFrom(fieldDef.name),
            )
        }
      }

      is ValueType.MapType -> {
        val ts = tsGen.fromType(fieldDef.type, collectionSizeHint)
        when (val elemType = fieldDef.type.valueType) {
          AtomicType.EmptyType, is AtomicType.UnknownName -> TODO()

          is AtomicType.PrimitiveType ->
            FieldExprs(
              FromProtoExpr.Const(ts.defaultValue),
              FromProtoPostProcessExpr.ForEachMap(
                fieldDef.name,
                FromProtoPostProcessExpr.PrimValueGetterExpr,
              ),
              ToProtoExpr.ForEachMap(fieldDef.name, ElemType.PRIM, ""),
              DeepCloneExpr.Const(ts.defaultValue),
              DeepClonePostProcessExpr.ForEachMap(fieldDef.name, false),
              CopyFromExpr.ForEachMap(fieldDef.name, false),
            )

          is AtomicType.EnumType ->
            FieldExprs(
              FromProtoExpr.Const(ts.defaultValue),
              FromProtoPostProcessExpr.ForEachMap(
                fieldDef.name,
                FromProtoPostProcessExpr.EnumValueGetterExpr(elemType.name),
              ),
              ToProtoExpr.ForEachMap(fieldDef.name, ElemType.ENUM, ""),
              DeepCloneExpr.Const(ts.defaultValue),
              DeepClonePostProcessExpr.ForEachMap(fieldDef.name, false),
              CopyFromExpr.ForEachMap(fieldDef.name, false),
            )

          is AtomicType.MessageOrSealedType -> {
            val protoTypeName = "$protoOuterClassName${elemType.name.className}"
            FieldExprs(
              FromProtoExpr.Const(ts.defaultValue),
              FromProtoPostProcessExpr.ForEachMap(
                fieldDef.name,
                FromProtoPostProcessExpr.MessageValueGetterExpr(elemType.name),
              ),
              ToProtoExpr.ForEachMap(fieldDef.name, ElemType.MESSAGE, protoTypeName),
              DeepCloneExpr.Const(ts.defaultValue),
              DeepClonePostProcessExpr.ForEachMap(fieldDef.name, true),
              CopyFromExpr.ForEachMap(fieldDef.name, true),
            )
          }
        }
      }

      is ValueType.RepeatedType, is ValueType.SetType -> {
        val ts = tsGen.fromType(fieldDef.type, collectionSizeHint)
        val elemType = when (fieldDef.type) {
          is ValueType.RepeatedType -> fieldDef.type.elemType
          is ValueType.SetType -> fieldDef.type.elemType
          else -> TODO()
        }
        when (elemType) {
          AtomicType.EmptyType, is AtomicType.UnknownName -> TODO()

          is AtomicType.PrimitiveType ->
            FieldExprs(
              FromProtoExpr.Const(ts.defaultValue),
              FromProtoPostProcessExpr.ForEachRepeated(
                fieldDef.name,
                FromProtoPostProcessExpr.PrimValueGetterExpr
              ),
              ToProtoExpr.ForEachRepeated(fieldDef.name, ElemType.PRIM, ""),
              DeepCloneExpr.Const(ts.defaultValue),
              DeepClonePostProcessExpr.ForEachRepeated(fieldDef.name, false),
              CopyFromExpr.ForEachRepeated(fieldDef.name, false),
            )

          is AtomicType.EnumType ->
            FieldExprs(
              FromProtoExpr.Const(ts.defaultValue),
              FromProtoPostProcessExpr.ForEachRepeated(
                fieldDef.name,
                FromProtoPostProcessExpr.EnumValueGetterExpr(elemType.name)
              ),
              ToProtoExpr.ForEachRepeated(fieldDef.name, ElemType.ENUM, ""),
              DeepCloneExpr.Const(ts.defaultValue),
              DeepClonePostProcessExpr.ForEachRepeated(fieldDef.name, false),
              CopyFromExpr.ForEachRepeated(fieldDef.name, false),
            )

          is AtomicType.MessageOrSealedType -> {
            val protoTypeName = "$protoOuterClassName${elemType.name.className}"
            FieldExprs(
              FromProtoExpr.Const(ts.defaultValue),
              FromProtoPostProcessExpr.ForEachRepeated(
                fieldDef.name,
                FromProtoPostProcessExpr.MessageValueGetterExpr(elemType.name)
              ),
              ToProtoExpr.ForEachRepeated(fieldDef.name, ElemType.MESSAGE, protoTypeName),
              DeepCloneExpr.Const(ts.defaultValue),
              DeepClonePostProcessExpr.ForEachRepeated(fieldDef.name, true),
              CopyFromExpr.ForEachRepeated(fieldDef.name, true),
            )
          }
        }
      }

      is ValueType.IndexedType -> {
        val ts = tsGen.fromType(fieldDef.type, collectionSizeHint)
        when (val elemType = fieldDef.type.elemType) {
          AtomicType.EmptyType, is AtomicType.UnknownName -> TODO()

          // indexed에서 elem이 primitive이거나 enum인 경우를 지원할 필요가 있을까?
          is AtomicType.PrimitiveType -> TODO()
          is AtomicType.EnumType -> TODO()

          is AtomicType.MessageOrSealedType -> {
            val protoTypeName = "$protoOuterClassName${elemType.name.className}"
            FieldExprs(
              FromProtoExpr.Const(ts.defaultValue),
              FromProtoPostProcessExpr.ForEachIndexed(
                fieldDef.name,
                fieldDef.type.keyExpr,
                FromProtoPostProcessExpr.MessageValueGetterExpr(elemType.name)
              ),
              ToProtoExpr.ForEachIndexed(fieldDef.name, ElemType.MESSAGE, protoTypeName),
              DeepCloneExpr.Const(ts.defaultValue),
              DeepClonePostProcessExpr.ForEachIndexed(fieldDef.name),
              CopyFromExpr.ForEachIndexed(fieldDef.name),
            )
          }
        }
      }
    }
  }
}

data class FieldExprs(
  val fromProtoExpr: FromProtoExpr,
  val fromProtoPostProcessExpr: FromProtoPostProcessExpr?,
  val toProtoExpr: ToProtoExpr,
  val deepCloneExpr: DeepCloneExpr,
  val deepClonePostProcessExpr: DeepClonePostProcessExpr?,
  val copyFromExpr: CopyFromExpr,
)

sealed class FromProtoExpr {
  abstract fun expr(protoExpr: String): String

  sealed class AtomicFromProtoExpr : FromProtoExpr()

  data class PrimFromProto(val fieldName: SemanticName) : AtomicFromProtoExpr() {
    override fun expr(protoExpr: String): String =
      "$protoExpr.${fieldName.classFieldName}"
  }

  data class EnumFromProto(val fieldName: SemanticName, val enumTypeName: SemanticName) :
    AtomicFromProtoExpr() {
    override fun expr(protoExpr: String): String =
      "${enumTypeName.enumClassName}.fromProto($protoExpr.${fieldName.classFieldName})"
  }

  data class MessageFromProto(val fieldName: SemanticName, val messageTypeName: SemanticName) :
    AtomicFromProtoExpr() {
    override fun expr(protoExpr: String): String =
      "${messageTypeName.className}.fromProto($protoExpr.${fieldName.classFieldName})"
  }

  data class OptionalFromProto(
    val fieldName: SemanticName,
    val fromProtoExpr: AtomicFromProtoExpr
  ) :
    FromProtoExpr() {
    override fun expr(protoExpr: String): String {
      val fromProto = fromProtoExpr.expr(protoExpr)
      return "if (!$protoExpr.has${fieldName.capitalClassFieldName}()) null else $fromProto"
    }
  }

  data class Const(val const: String) : FromProtoExpr() {
    override fun expr(protoExpr: String): String = const
  }
}

sealed class FromProtoPostProcessExpr {
  abstract fun generate(gen: MutableKtDataClassGen, protoExpr: String, instanceExpr: String)

  sealed class AtomicValueGetterExpr {
    abstract fun expr(elemExpr: String): String
  }

  object PrimValueGetterExpr : AtomicValueGetterExpr() {
    override fun expr(elemExpr: String): String = elemExpr
  }

  data class EnumValueGetterExpr(val typeName: SemanticName) : AtomicValueGetterExpr() {
    override fun expr(elemExpr: String): String = "${typeName.enumClassName}.fromProto($elemExpr)"
  }

  data class MessageValueGetterExpr(val typeName: SemanticName) : AtomicValueGetterExpr() {
    override fun expr(elemExpr: String): String = "${typeName.className}.fromProto($elemExpr)"
  }

  data class ForEachRepeated(val fieldName: SemanticName, val valueGetter: AtomicValueGetterExpr) :
    FromProtoPostProcessExpr() {
    override fun generate(gen: MutableKtDataClassGen, protoExpr: String, instanceExpr: String) {
      gen.addLine("$protoExpr.${fieldName.classFieldName}List.forEach { elem ->")
      gen.indent {
        val getter = valueGetter.expr("elem")
        gen.addLine("$instanceExpr.${fieldName.classFieldName}.add($getter)")
      }
      gen.addLine("}")
    }
  }

  data class ForEachMap(val fieldName: SemanticName, val valueGetter: AtomicValueGetterExpr) :
    FromProtoPostProcessExpr() {
    override fun generate(gen: MutableKtDataClassGen, protoExpr: String, instanceExpr: String) {
      gen.addLine("$protoExpr.${fieldName.classFieldName}Map.forEach { entry ->")
      gen.indent {
        val getter = valueGetter.expr("entry.value")
        gen.addLine("$instanceExpr.${fieldName.classFieldName}.put(entry.key, $getter)")
      }
      gen.addLine("}")
    }
  }

  data class ForEachIndexed(
    val fieldName: SemanticName,
    val keyExpr: SugarProtoAst.KeyExpr,
    val valueGetter: AtomicValueGetterExpr
  ) : FromProtoPostProcessExpr() {
    override fun generate(gen: MutableKtDataClassGen, protoExpr: String, instanceExpr: String) {
      gen.addLine("$protoExpr.${fieldName.classFieldName}List.forEach { elem ->")
      gen.indent {
        val getter = valueGetter.expr("elem")
        gen.addLine("val elemInstance = $getter")
        val keyExpr = keyExpr.genKeyExpr("elemInstance")
        gen.addLine("$instanceExpr.${fieldName.classFieldName}.put($keyExpr, elemInstance)")
      }
      gen.addLine("}")
    }
  }
}

sealed class ToProtoExpr {
  abstract fun generate(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String)

  data class PrimToProto(val fieldName: SemanticName) : ToProtoExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.${fieldName.classFieldName} = $thisExpr.${fieldName.classFieldName}")
    }
  }

  data class EnumToProto(val fieldName: SemanticName) : ToProtoExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$builderExpr.${fieldName.classFieldName} = $thisExpr.${fieldName.classFieldName}.toProto()")
    }
  }

  data class MessageToProto(val fieldName: SemanticName) : ToProtoExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$thisExpr.${fieldName.classFieldName}.toProto($builderExpr.${fieldName.classFieldName}Builder)")
    }
  }

  data class PrimOptionalToProto(val fieldName: SemanticName) : ToProtoExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$thisExpr.${fieldName.classFieldName}?.let { value -> $builderExpr.${fieldName.classFieldName} = value } ?: $builderExpr.clear${fieldName.capitalClassFieldName}()")
    }
  }

  data class EnumOptionalToProto(val fieldName: SemanticName) : ToProtoExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$thisExpr.${fieldName.classFieldName}?.let { value -> $builderExpr.${fieldName.classFieldName} = value.toProto() } ?: $builderExpr.clear${fieldName.capitalClassFieldName}()")
    }
  }

  data class MessageOptionalToProto(val fieldName: SemanticName) : ToProtoExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$thisExpr.${fieldName.classFieldName}?.toProto($builderExpr.${fieldName.classFieldName}Builder) ?: $builderExpr.clear${fieldName.capitalClassFieldName}()")
    }
  }

  data class ForEachRepeated(
    val fieldName: SemanticName,
    val elemType: ElemType,
    val messageProtoName: String
  ) : ToProtoExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$thisExpr.${fieldName.classFieldName}.forEach { elem ->")
      gen.indent {
        when (elemType) {
          ElemType.PRIM ->
            gen.addLine("$builderExpr.add${fieldName.capitalClassFieldName}(elem)")

          ElemType.ENUM ->
            gen.addLine("$builderExpr.add${fieldName.capitalClassFieldName}(elem.toProto())")

          ElemType.MESSAGE ->
            gen.addLine("elem.toProto($builderExpr.add${fieldName.capitalClassFieldName}Builder())")
        }
      }
      gen.addLine("}")
    }
  }

  data class ForEachIndexed(
    val fieldName: SemanticName,
    val elemType: ElemType,
    val messageProtoName: String
  ) : ToProtoExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$thisExpr.${fieldName.classFieldName}.forEach { entry ->")
      gen.indent {
        when (elemType) {
          ElemType.PRIM ->
            gen.addLine("$builderExpr.add${fieldName.capitalClassFieldName}(entry.value)")

          ElemType.ENUM ->
            gen.addLine("$builderExpr.add${fieldName.capitalClassFieldName}(entry.value.toProto())")

          ElemType.MESSAGE ->
            gen.addLine("entry.value.toProto($builderExpr.add${fieldName.capitalClassFieldName}Builder())")
        }
      }
      gen.addLine("}")
    }
  }

  data class ForEachMap(
    val fieldName: SemanticName,
    val elemType: ElemType,
    val messageProtoClassName: String
  ) : ToProtoExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, builderExpr: String) {
      gen.addLine("$thisExpr.${fieldName.classFieldName}.forEach { entry ->")
      gen.indent {
        when (elemType) {
          ElemType.PRIM ->
            gen.addLine("$builderExpr.put${fieldName.capitalClassFieldName}(entry.key, entry.value)")

          ElemType.ENUM ->
            gen.addLine("$builderExpr.put${fieldName.capitalClassFieldName}(entry.key, entry.value.toProto())")

          ElemType.MESSAGE -> {
            gen.addLine("val valueBuilder = $messageProtoClassName.newBuilder()")
            gen.addLine("entry.value.toProto(valueBuilder)")
            gen.addLine("$builderExpr.put${fieldName.capitalClassFieldName}(entry.key, valueBuilder.build())")
          }
        }
      }
      gen.addLine("}")
    }
  }
}

enum class ElemType {
  PRIM, ENUM, MESSAGE
}

sealed class DeepCloneExpr {
  // this의 필드를 other의 필드로 initialize하는 코드
  abstract fun expr(thisExpr: String): String

  data class PlainDeepClone(val fieldName: SemanticName) : DeepCloneExpr() {
    override fun expr(thisExpr: String): String = "$thisExpr.${fieldName.classFieldName}"
  }

  data class MessageDeepClone(val fieldName: SemanticName) :
    DeepCloneExpr() {
    override fun expr(thisExpr: String): String =
      "$thisExpr.${fieldName.classFieldName}.deepClone()"
  }

  data class Optional(val fieldName: SemanticName, val isMessage: Boolean) :
    DeepCloneExpr() {
    override fun expr(thisExpr: String): String = if (isMessage) {
      "$thisExpr.${fieldName.classFieldName}?.deepClone()"
    } else {
      "$thisExpr.${fieldName.classFieldName}"
    }
  }

  data class Const(val const: String) : DeepCloneExpr() {
    override fun expr(thisExpr: String): String = const
  }
}

sealed class DeepClonePostProcessExpr {
  abstract fun generate(gen: MutableKtDataClassGen, thisExpr: String, cloneExpr: String)

  data class ForEachMap(val fieldName: SemanticName, val isMessage: Boolean) :
    DeepClonePostProcessExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, cloneExpr: String) {
      val ktFieldName = fieldName.classFieldName
      gen.addLine("$thisExpr.$ktFieldName.forEach { entry ->")
      gen.indent {
        if (isMessage) {
          gen.addLine("$cloneExpr.$ktFieldName.put(entry.key, entry.value.deepClone())")
        } else {
          gen.addLine("$cloneExpr.$ktFieldName.put(entry.key, entry.value)")
        }
      }
      gen.addLine("}")
    }
  }

  data class ForEachRepeated(val fieldName: SemanticName, val isMessage: Boolean) :
    DeepClonePostProcessExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, cloneExpr: String) {
      val ktFieldName = fieldName.classFieldName
      gen.addLine("$thisExpr.$ktFieldName.forEach { elem ->")
      gen.indent {
        gen.addLine("$cloneExpr.$ktFieldName.add(elem${if (isMessage) ".deepClone()" else ""})")
      }
      gen.addLine("}")
    }
  }

  data class ForEachIndexed(val fieldName: SemanticName) : DeepClonePostProcessExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, cloneExpr: String) {
      val ktFieldName = fieldName.classFieldName
      // gen.addLine("$cloneExpr.$ktFieldName.clear()")
      gen.addLine("$thisExpr.$ktFieldName.forEach { entry ->")
      gen.indent {
        gen.addLine("$cloneExpr.add${fieldName.capitalClassFieldName}(entry.value)")
      }
      gen.addLine("}")
    }
  }
}

sealed class CopyFromExpr {
  // other의 필드를 this의 필드로 복사하는 코드
  abstract fun generate(gen: MutableKtDataClassGen, thisExpr: String, otherExpr: String)

  data class PlainCopyFrom(val fieldName: SemanticName) : CopyFromExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, otherExpr: String) {
      val ktFieldName = fieldName.classFieldName
      gen.addLine("$thisExpr.$ktFieldName = $otherExpr.$ktFieldName")
    }
  }

  data class MessageCopyFrom(val fieldName: SemanticName) : CopyFromExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, otherExpr: String) {
      val ktFieldName = fieldName.classFieldName
      gen.addLine("$thisExpr.$ktFieldName.copyFrom($otherExpr.$ktFieldName)")
    }
  }

  data class MessageOptionalCopyFrom(val fieldName: SemanticName) : CopyFromExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, otherExpr: String) {
      val ktFieldName = fieldName.classFieldName
      //    other.ltTilePoint.let { otherField ->
      //      if (otherField == null) {
      //        this.ltTilePoint = null
      //      } else {
      //        this.ltTilePoint.let { thisField ->
      //          if (thisField == null) {
      //            this.ltTilePoint = otherField.deepClone()
      //          } else {
      //            thisField.copyFrom(otherField)
      //          }
      //        }
      //      }
      //    }
      gen.addLine("$otherExpr.$ktFieldName.let { otherField ->")
      gen.indent {
        gen.addLine("if (otherField == null) {")
        gen.indent {
          gen.addLine("$thisExpr.$ktFieldName = null")
        }
        gen.addLine("} else {")
        gen.indent {
          gen.addLine("$thisExpr.$ktFieldName.let { thisField ->")
          gen.indent {
            gen.addLine("if (thisField == null) {")
            gen.indent {
              gen.addLine("$thisExpr.$ktFieldName = otherField.deepClone()")
            }
            gen.addLine("} else {")
            gen.indent {
              gen.addLine("thisField.copyFrom(otherField)")
            }
            gen.addLine("}")
          }
          gen.addLine("}")
        }
        gen.addLine("}")
      }
      gen.addLine("}")
    }
  }

  data class ForEachMap(val fieldName: SemanticName, val isMessage: Boolean) : CopyFromExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, otherExpr: String) {
      val ktFieldName = fieldName.classFieldName
      gen.addLine("$thisExpr.$ktFieldName.clear()")
      gen.addLine("$otherExpr.$ktFieldName.forEach { entry ->")
      gen.indent {
        gen.addLine("$thisExpr.$ktFieldName.put(entry.key, entry.value${if (isMessage) ".deepClone()" else ""})")
      }
      gen.addLine("}")
    }
  }

  data class ForEachRepeated(val fieldName: SemanticName, val isMessage: Boolean) : CopyFromExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, otherExpr: String) {
      val ktFieldName = fieldName.classFieldName
      gen.addLine("$thisExpr.$ktFieldName.clear()")
      gen.addLine("$otherExpr.$ktFieldName.forEach { elem ->")
      gen.indent {
        gen.addLine("$thisExpr.$ktFieldName.add(elem${if (isMessage) ".deepClone()" else ""})")
      }
      gen.addLine("}")
    }
  }

  data class ForEachIndexed(val fieldName: SemanticName) : CopyFromExpr() {
    override fun generate(gen: MutableKtDataClassGen, thisExpr: String, otherExpr: String) {
      val ktFieldName = fieldName.classFieldName
      gen.addLine("$thisExpr.$ktFieldName.clear()")
      gen.addLine("$otherExpr.$ktFieldName.forEach { entry ->")
      gen.indent {
        gen.addLine("$thisExpr.add${fieldName.capitalClassFieldName}(entry.value)")
      }
      gen.addLine("}")
    }
  }
}
