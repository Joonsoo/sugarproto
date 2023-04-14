package com.giyeok.sugarproto

class TraverseResult(
  val packageName: String?,
  val imports: Set<String>,
  val options: List<SugarProtoAst.OptionDef>,
  val defs: List<ProtoDef>,
)
