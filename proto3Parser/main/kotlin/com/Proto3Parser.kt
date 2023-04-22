package com.giyeok.sugarproto

import com.giyeok.jparser.ktparser.mgroup2.MilestoneGroupParserLoader

object Proto3Parser {
  val parser = MilestoneGroupParserLoader.loadParserFromResource("/proto3-mg2-parserdata.pb")

  fun parse(sourceText: String): Proto3Ast.Proto3 {
    val parseResult = parser.parse(sourceText)
    return Proto3Ast(sourceText, parser.kernelsHistory(parseResult)).matchStart()
  }
}
