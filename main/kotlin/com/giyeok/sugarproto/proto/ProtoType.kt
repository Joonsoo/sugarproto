package com.giyeok.sugarproto.proto

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.name.SemanticName

sealed class ProtoType {
  data class StreamType(val valueType: ValueType): ProtoType()
}

sealed class ValueType: ProtoType() {
  data class RepeatedType(val elemType: AtomicType): ValueType()
  data class SetType(val elemType: AtomicType): ValueType()
  data class OptionalType(val elemType: AtomicType): ValueType()
  data class MapType(val keyType: AtomicType.PrimitiveType, val valueType: AtomicType): ValueType()
}

sealed class AtomicType: ValueType() {
  object EmptyType: AtomicType()

  data class PrimitiveType(val type: SugarProtoAst.PrimitiveTypeEnum): AtomicType()

  // 이름은 항상 루트 scope에서부터 시작하는 canonical name으로
  data class UnknownName(val name: String): AtomicType()

  sealed class MessageOrSealedType: AtomicType() {
    abstract val name: SemanticName
    abstract val source: TypeSource
  }

  data class MessageType(override val name: SemanticName, override val source: TypeSource):
    MessageOrSealedType()

  data class SealedType(override val name: SemanticName, override val source: TypeSource):
    MessageOrSealedType()

  data class EnumType(val name: SemanticName, val source: TypeSource): AtomicType()

  sealed class TypeSource {
    object UserDefined: TypeSource()

    object Generated: TypeSource()

    data class External(val protoPkg: List<String>): TypeSource()
  }
}
