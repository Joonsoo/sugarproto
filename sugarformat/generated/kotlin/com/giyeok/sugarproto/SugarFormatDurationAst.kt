package com.giyeok.sugarproto

import com.giyeok.jparser.ktlib.*

class SugarFormatDurationAst(
  val source: String,
  val history: List<KernelSet>,
  val idIssuer: IdIssuer = IdIssuerImpl(0)
) {
  private fun nextId(): Int = idIssuer.nextId()

  sealed interface AstNode {
    val nodeId: Int
    val start: Int
    val end: Int
  }

data class Duration(
  val days: String?,
  val hours: String?,
  val minutes: String?,
  val seconds: Seconds?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class Seconds(
  val integral: String,
  val frac: String?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode


fun matchStart(): Duration {
  val lastGen = source.length
  val kernel = history[lastGen].getSingle(2, 1, 0, lastGen)
  return matchDuration(kernel.beginGen, kernel.endGen)
}

fun matchDuration(beginGen: Int, endGen: Int): Duration {
val var1 = history[endGen].findByBeginGenOpt(3, 4, beginGen)
val var2 = history[endGen].findByBeginGenOpt(7, 1, beginGen)
val var3 = history[endGen].findByBeginGenOpt(44, 3, beginGen)
val var4 = history[endGen].findByBeginGenOpt(45, 2, beginGen)
check(hasSingleTrue(var1 != null, var2 != null, var3 != null, var4 != null))
val var5 = when {
var1 != null -> {
val var6 = getSequenceElems(history, 3, listOf(4,23,29,35), beginGen, endGen)
val var7 = history[var6[0].second].findByBeginGenOpt(5, 1, var6[0].first)
val var8 = history[var6[0].second].findByBeginGenOpt(22, 1, var6[0].first)
check(hasSingleTrue(var7 != null, var8 != null))
val var9 = when {
var7 != null -> {
val var10 = getSequenceElems(history, 6, listOf(7,17), var6[0].first, var6[0].second)
val var11 = matchDays(var10[0].first, var10[0].second)
var11
}
else -> null
}
val var12 = history[var6[1].second].findByBeginGenOpt(22, 1, var6[1].first)
val var13 = history[var6[1].second].findByBeginGenOpt(24, 1, var6[1].first)
check(hasSingleTrue(var12 != null, var13 != null))
val var14 = when {
var12 != null -> null
else -> {
val var15 = getSequenceElems(history, 25, listOf(26,17), var6[1].first, var6[1].second)
val var16 = matchHours(var15[0].first, var15[0].second)
var16
}
}
val var17 = history[var6[2].second].findByBeginGenOpt(22, 1, var6[2].first)
val var18 = history[var6[2].second].findByBeginGenOpt(30, 1, var6[2].first)
check(hasSingleTrue(var17 != null, var18 != null))
val var19 = when {
var17 != null -> null
else -> {
val var20 = getSequenceElems(history, 31, listOf(32,17), var6[2].first, var6[2].second)
val var21 = matchMinutes(var20[0].first, var20[0].second)
var21
}
}
val var22 = matchSeconds(var6[3].first, var6[3].second)
val var23 = Duration(var9, var14, var19, var22, nextId(), beginGen, endGen)
var23
}
var2 != null -> {
val var24 = matchDays(beginGen, endGen)
val var25 = Duration(var24, null, null, null, nextId(), beginGen, endGen)
var25
}
var3 != null -> {
val var26 = getSequenceElems(history, 44, listOf(4,23,32), beginGen, endGen)
val var27 = history[var26[0].second].findByBeginGenOpt(5, 1, var26[0].first)
val var28 = history[var26[0].second].findByBeginGenOpt(22, 1, var26[0].first)
check(hasSingleTrue(var27 != null, var28 != null))
val var29 = when {
var27 != null -> {
val var30 = getSequenceElems(history, 6, listOf(7,17), var26[0].first, var26[0].second)
val var31 = matchDays(var30[0].first, var30[0].second)
var31
}
else -> null
}
val var32 = history[var26[1].second].findByBeginGenOpt(22, 1, var26[1].first)
val var33 = history[var26[1].second].findByBeginGenOpt(24, 1, var26[1].first)
check(hasSingleTrue(var32 != null, var33 != null))
val var34 = when {
var32 != null -> null
else -> {
val var35 = getSequenceElems(history, 25, listOf(26,17), var26[1].first, var26[1].second)
val var36 = matchHours(var35[0].first, var35[0].second)
var36
}
}
val var37 = matchMinutes(var26[2].first, var26[2].second)
val var38 = Duration(var29, var34, var37, null, nextId(), beginGen, endGen)
var38
}
else -> {
val var39 = getSequenceElems(history, 45, listOf(4,26), beginGen, endGen)
val var40 = history[var39[0].second].findByBeginGenOpt(5, 1, var39[0].first)
val var41 = history[var39[0].second].findByBeginGenOpt(22, 1, var39[0].first)
check(hasSingleTrue(var40 != null, var41 != null))
val var42 = when {
var40 != null -> {
val var43 = getSequenceElems(history, 6, listOf(7,17), var39[0].first, var39[0].second)
val var44 = matchDays(var43[0].first, var43[0].second)
var44
}
else -> null
}
val var45 = matchHours(var39[1].first, var39[1].second)
val var46 = Duration(var42, var45, null, null, nextId(), beginGen, endGen)
var46
}
}
return var5
}

fun matchDays(beginGen: Int, endGen: Int): String {
val var47 = getSequenceElems(history, 8, listOf(9,17,21), beginGen, endGen)
val var48 = matchNumber(var47[0].first, var47[0].second)
return var48
}

fun matchNumber(beginGen: Int, endGen: Int): String {
val var49 = history[endGen].findByBeginGenOpt(10, 1, beginGen)
val var50 = history[endGen].findByBeginGenOpt(11, 2, beginGen)
check(hasSingleTrue(var49 != null, var50 != null))
val var51 = when {
var49 != null -> "0"
else -> {
val var52 = getSequenceElems(history, 11, listOf(12,13), beginGen, endGen)
val var53 = unrollRepeat0(history, 13, 16, 14, 15, var52[1].first, var52[1].second).map { k ->
source[k.first]
}
source[var52[0].first].toString() + var53.joinToString("") { it.toString() }
}
}
return var51
}

fun matchHours(beginGen: Int, endGen: Int): String {
val var54 = getSequenceElems(history, 27, listOf(9,17,28), beginGen, endGen)
val var55 = matchNumber(var54[0].first, var54[0].second)
return var55
}

fun matchMinutes(beginGen: Int, endGen: Int): String {
val var56 = getSequenceElems(history, 33, listOf(9,17,34), beginGen, endGen)
val var57 = matchNumber(var56[0].first, var56[0].second)
return var57
}

fun matchSeconds(beginGen: Int, endGen: Int): Seconds {
val var58 = getSequenceElems(history, 36, listOf(9,37,17,43), beginGen, endGen)
val var59 = matchNumber(var58[0].first, var58[0].second)
val var60 = history[var58[1].second].findByBeginGenOpt(22, 1, var58[1].first)
val var61 = history[var58[1].second].findByBeginGenOpt(38, 1, var58[1].first)
check(hasSingleTrue(var60 != null, var61 != null))
val var62 = when {
var60 != null -> null
else -> {
val var63 = getSequenceElems(history, 39, listOf(40,41), var58[1].first, var58[1].second)
val var64 = unrollRepeat1(history, 41, 16, 16, 42, var63[1].first, var63[1].second).map { k ->
source[k.first]
}
var64.joinToString("") { it.toString() }
}
}
val var65 = Seconds(var59, var62, nextId(), beginGen, endGen)
return var65
}

}
