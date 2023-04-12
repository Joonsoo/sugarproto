package com.giyeok.sugarproto

import com.giyeok.jparser.ktparser.mgroup2.MilestoneGroupParserLoader

object SugarProtoParser {
  val parser = MilestoneGroupParserLoader.loadParserFromResource("/sugarproto-mg2-parserdata.pb")

  fun parse(sourceText: String): SugarProtoAst.CompilationUnit {
    val parseResult = parser.parse(sourceText)
    return SugarProtoAst(sourceText, parser.kernelsHistory(parseResult)).matchStart()
  }
}
