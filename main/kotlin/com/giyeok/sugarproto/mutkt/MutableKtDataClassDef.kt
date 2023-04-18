package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.name.SemanticName
import com.giyeok.sugarproto.proto.ValueType

data class MutableKtDataClassDefs(
  val defs: List<MutableKtDataClassDef>,
)

data class MutableKtDataClassDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: SemanticName,
  val superClass: SemanticName?,
  val inheritedFields: List<MutableKtDataClassField>,
  val uniqueFields: List<MutableKtDataClassField>,
)

data class MutableKtDataClassField(
  val comments: List<SugarProtoAst.Comment>,
  // name에서 kotlin 이름과 proto 이름 생성 가능
  val name: SemanticName,
  val type: ValueType,
)
