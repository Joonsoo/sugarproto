package com.giyeok.sugarproto.proto

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.name.SemanticName

sealed class ProtoType {
  data class StreamType(val valueType: ValueType): ProtoType()
  data class NoStreamType(val valueType: ValueType): ProtoType()
}

sealed class ValueType {
  data class RepeatedType(val elemType: AtomicType): ValueType()
  data class OptionalType(val elemType: AtomicType): ValueType()
  data class MapType(val keyType: AtomicType.PrimitiveType, val valueType: AtomicType): ValueType()
}

sealed class AtomicType {
  data class PrimitiveType(val type: SugarProtoAst.PrimitiveTypeEnum): AtomicType()

  // 이름은 항상 루트 scope에서부터 시작하는 canonical name으로
  data class Name(val name: String): AtomicType()

  // generated name은 별도로 처리
  // 역시 항상 루트 scope에서 시작하는 canonical name
  data class GeneratedMessageName(val name: SemanticName): AtomicType()
  data class GeneratedEnumName(val name: SemanticName): AtomicType()
  data class GeneratedSealedName(val name: SemanticName): AtomicType()
}
