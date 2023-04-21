package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.proto.AtomicType
import com.giyeok.sugarproto.proto.ValueType

class TypeStringGen(val gdxMode: Boolean) {
  private val requiredCollections = mutableSetOf<String>()

  fun ValueType.isInt() = when (this) {
    is AtomicType.PrimitiveType -> when (this.type) {
      SugarProtoAst.PrimitiveTypeEnum.INT32,
      SugarProtoAst.PrimitiveTypeEnum.SINT32,
      SugarProtoAst.PrimitiveTypeEnum.UINT32 -> true

      else -> false
    }

    else -> false
  }

  fun ValueType.isLong() = when (this) {
    is AtomicType.PrimitiveType -> when (this.type) {
      SugarProtoAst.PrimitiveTypeEnum.INT64,
      SugarProtoAst.PrimitiveTypeEnum.SINT64,
      SugarProtoAst.PrimitiveTypeEnum.UINT64 -> true

      else -> false
    }

    else -> false
  }

  fun ValueType.isFloat() = when (this) {
    is AtomicType.PrimitiveType -> when (this.type) {
      SugarProtoAst.PrimitiveTypeEnum.FLOAT -> true
      else -> false
    }

    else -> false
  }

  fun ValueType.isDouble() = when (this) {
    is AtomicType.PrimitiveType -> when (this.type) {
      SugarProtoAst.PrimitiveTypeEnum.DOUBLE -> true
      else -> false
    }

    else -> false
  }

  fun mapType(keyType: ValueType, valueType: ValueType, collectionSizeHint: String?): TypeString =
    if (gdxMode) {
      if (keyType.isInt()) {
        if (valueType.isInt()) {
          TypeString(false, "IntIntMap", "IntIntMap(${collectionSizeHint ?: ""})")
        } else if (valueType.isFloat()) {
          TypeString(false, "IntFloatMap", "IntFloatMap(${collectionSizeHint ?: ""})")
        } else {
          val s = fromType(valueType)
          TypeString(false, "IntMap<${s.typeString}>", "IntMap(${collectionSizeHint ?: ""})")
        }
      } else if (keyType.isLong()) {
        val s = fromType(valueType)
        TypeString(false, "LongMap<${s.typeString}>", "LongMap(${collectionSizeHint ?: ""})")
      } else {
        val key = fromType(keyType)
        if (valueType.isInt()) {
          TypeString(
            false,
            "ObjectIntMap<${key.typeString}>",
            "ObjectIntMap(${collectionSizeHint ?: ""})"
          )
        } else if (valueType.isLong()) {
          TypeString(
            false,
            "ObjectLongMap<${key.typeString}>",
            "ObjectLongMap(${collectionSizeHint ?: ""})"
          )
        } else if (valueType.isFloat()) {
          TypeString(
            false,
            "ObjectFloatMap<${key.typeString}>",
            "ObjectFloatMap(${collectionSizeHint ?: ""})"
          )
        } else {
          val v = fromType(valueType)
          TypeString(
            false,
            "ObjectMap<${key.typeString}, ${v.typeString}>",
            "ObjectMap(${collectionSizeHint ?: ""})"
          )
        }
      }
    } else {
      val key = fromType(keyType)
      val value = fromType(valueType)
      TypeString(
        false,
        "MutableMap<${key.typeString}, ${value.typeString}>",
        "mutableMapOf(${if (collectionSizeHint != null) "/* $collectionSizeHint */" else ""})"
      )
    }

  fun fromType(typ: ValueType, collectionSizeHint: String? = null): TypeString =
    when (typ) {
      AtomicType.EmptyType -> TODO()
      is AtomicType.UnknownName ->
        throw IllegalStateException("Unknown name: ${typ.name}")

      is AtomicType.EnumType -> {
        val enumName = typ.name.enumName
        TypeString(true, enumName, "$enumName.defaultValue")
      }

      is AtomicType.MessageType -> {
        val className = typ.name.className
        TypeString(false, className, "$className.create()")
      }

      is AtomicType.SealedType -> {
        val className = typ.name.className
        TypeString(true, className, "$className.create()")
      }

      is AtomicType.PrimitiveType -> when (typ.type) {
        SugarProtoAst.PrimitiveTypeEnum.BOOL -> TypeString(true, "Boolean", "false")
        SugarProtoAst.PrimitiveTypeEnum.BYTES -> TODO()
        SugarProtoAst.PrimitiveTypeEnum.DOUBLE -> TypeString(true, "Double", "0.0")
        SugarProtoAst.PrimitiveTypeEnum.FIXED32 -> TODO()
        SugarProtoAst.PrimitiveTypeEnum.FIXED64 -> TODO()
        SugarProtoAst.PrimitiveTypeEnum.FLOAT -> TypeString(true, "Float", "0f")
        SugarProtoAst.PrimitiveTypeEnum.INT32 -> TypeString(true, "Int", "0")
        SugarProtoAst.PrimitiveTypeEnum.INT64 -> TypeString(true, "Long", "0L")
        SugarProtoAst.PrimitiveTypeEnum.SFIXED32 -> TODO()
        SugarProtoAst.PrimitiveTypeEnum.SFIXED64 -> TODO()
        SugarProtoAst.PrimitiveTypeEnum.SINT32 -> TypeString(true, "Int", "0")
        SugarProtoAst.PrimitiveTypeEnum.SINT64 -> TypeString(true, "Long", "0L")
        SugarProtoAst.PrimitiveTypeEnum.STRING -> TypeString(true, "String", "\"\"")
        SugarProtoAst.PrimitiveTypeEnum.UINT32 -> TODO()
        SugarProtoAst.PrimitiveTypeEnum.UINT64 -> TODO()
      }

      is ValueType.OptionalType -> {
        val s = fromType(typ.elemType)
        TypeString(true, s.typeString + "?", "null")
      }

      is ValueType.RepeatedType -> {
        if (gdxMode) {
          if (typ.elemType.isInt()) {
            TypeString(false, "GdxIntArray", "GdxIntArray(${collectionSizeHint ?: ""})")
          } else if (typ.elemType.isLong()) {
            TypeString(false, "GdxLongArray", "GdxLongArray(${collectionSizeHint ?: ""})")
          } else if (typ.elemType.isFloat()) {
            TypeString(false, "GdxFloatArray", "GdxFloatArray(${collectionSizeHint ?: ""})")
          } else {
            val s = fromType(typ.elemType)
            TypeString(false, "GdxArray<${s.typeString}>", "GdxArray(${collectionSizeHint ?: ""})")
          }
        } else {
          val s = fromType(typ.elemType)
          TypeString(
            false,
            "MutableList<${s.typeString}>",
            "mutableListOf(${if (collectionSizeHint != null) "/* $collectionSizeHint */" else ""})"
          )
        }
      }

      is ValueType.SetType -> {
        if (gdxMode) {
          if (typ.elemType.isInt()) {
            TypeString(false, "GdxIntSet", "GdxIntSet(${collectionSizeHint ?: ""})")
          } else {
            val s = fromType(typ.elemType)
            TypeString(
              false,
              "ObjectSet<${s.typeString}>",
              "ObjectSet(${collectionSizeHint ?: ""})"
            )
          }
        } else {
          val s = fromType(typ.elemType)
          TypeString(
            false,
            "MutableSet<${s.typeString}>",
            "mutableSetOf(${if (collectionSizeHint != null) "/* $collectionSizeHint */" else ""})"
          )
        }
      }

      is ValueType.IndexedType ->
        mapType(typ.keyType, typ.elemType, collectionSizeHint)

      is ValueType.MapType ->
        mapType(typ.keyType, typ.valueType, collectionSizeHint)
    }
}

data class TypeString(
  val useVar: Boolean,
  val typeString: String,
  val defaultValue: String,
)
