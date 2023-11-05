package com.giyeok.sugarproto.sugarformat

import com.giyeok.jparser.ktlib.ParsingErrorKt
import com.giyeok.jparser.ktparser.mgroup2.MilestoneGroupParserLoader
import com.giyeok.sugarproto.SugarFormatAst
import com.giyeok.sugarproto.SugarFormatDurationAst
import com.google.protobuf.Duration

object SugarFormatParser {
  val parser = MilestoneGroupParserLoader.loadParserFromResource("/sugarformat-mg2-parserdata.pb")
  val durationParser =
    MilestoneGroupParserLoader.loadParserFromResource("/duration-mg2-parserdata.pb")

  fun parse(sourceText: String): List<SugarFormatAst.IndentItem> {
    val parseResult = parser.parse(sourceText)
    return SugarFormatAst(sourceText, parser.kernelsHistory(parseResult)).matchStart()
  }

  fun parseDuration(duration: String): SugarFormatDurationAst.Duration {
    val parseResult = durationParser.parse(duration)
    return SugarFormatDurationAst(duration, durationParser.kernelsHistory(parseResult)).matchStart()
  }

  fun convertDuration(duration: String): Duration {
    val parsed = try {
      parseDuration(duration)
    } catch (e: ParsingErrorKt) {
      throw IllegalStateException("Invalid duration format: parse failed at ${e.location}")
    }
    val seconds = (parsed.days?.toLong() ?: 0) * 24 * 3600 +
      (parsed.hours?.toLong() ?: 0) * 3600 +
      (parsed.minutes?.toLong() ?: 0) * 60 +
      (parsed.seconds?.integral?.toLong() ?: 0)

    val builder = Duration.newBuilder()
    builder.seconds = seconds

    parsed.seconds?.let { s ->
      s.frac?.let { frac ->
        if (frac.length > 9) {
          throw IllegalStateException("Invalid duration format: invalid nanos")
        }
        val nanos = frac.padEnd(9, '0').toInt()
        builder.nanos = nanos
      }
    }

    return builder.build()
  }
}
