package com.giyeok.sugarproto.proto

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.name.SemanticName

sealed class ProtoType {
  data class StreamType(val valueType: ValueType): ProtoType()
}

sealed class ValueType: ProtoType() {
  data class RepeatedType(val elemType: AtomicType): ValueType()
  data class OptionalType(val elemType: AtomicType): ValueType()
  data class MapType(val keyType: AtomicType.PrimitiveType, val valueType: AtomicType): ValueType()
}

sealed class AtomicType: ValueType() {
  object EmptyType: AtomicType()

  data class PrimitiveType(val type: SugarProtoAst.PrimitiveTypeEnum): AtomicType()

  sealed class NameRefType: AtomicType() {
    abstract val refName: String
  }

  // 이름은 항상 루트 scope에서부터 시작하는 canonical name으로
  data class UnknownName(val name: String): NameRefType() {
    override val refName: String get() = name
  }

  data class MessageName(val name: String): NameRefType() {
    override val refName: String get() = name
  }

  data class EnumName(val name: String): NameRefType() {
    override val refName: String get() = name
  }

  data class SealedName(val name: String): NameRefType() {
    override val refName: String get() = name
  }

  // generated name은 별도로 처리
  // 역시 항상 루트 scope에서 시작하는 canonical name
  data class GeneratedMessageName(val name: SemanticName): NameRefType() {
    override val refName: String get() = name.messageName
  }

  data class GeneratedEnumName(val name: SemanticName): NameRefType() {
    override val refName: String get() = name.enumName
  }

  data class GeneratedSealedName(val name: SemanticName): NameRefType() {
    override val refName: String get() = name.messageName
  }
}
