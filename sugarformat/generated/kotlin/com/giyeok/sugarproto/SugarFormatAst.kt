package com.giyeok.sugarproto

import com.giyeok.jparser.ktlib.*

class SugarFormatAst(
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

data class HexCode(
  val code: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode

data class Header(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemValue, AstNode

sealed interface Timezone: AstNode

data class ListFieldItem(
  val name: ItemPath,
  val value: ItemValue,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ListItem, AstNode

data class NameKey(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KeyValue, AstNode

data class OctValue(
  val sgn: Char?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): NumberValue, AstNode

data class NameValue(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ScalarValue, AstNode

data class HexValue(
  val sgn: Char?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): NumberValue, AstNode

data class ListValueItem(
  val value: Value,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ListItem, AstNode

sealed interface NumberValue: KeyValue, ScalarValue, AstNode

data class Date(
  val year: String,
  val month: String,
  val day: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

sealed interface KeyValue: AstNode

data class Seconds(
  val integral: String,
  val frac: String?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

sealed interface ListItem: Item, AstNode

data class StringValue(
  val type: StringTypeAnnot?,
  val fracs: List<StringFrac>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ScalarValue, AstNode

sealed interface ScalarValue: Value, AstNode

data class StringFrac(
  val elems: List<StringElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KeyValue, AstNode

data class TimeOffset(
  val hour: String,
  val minute: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Timezone, AstNode

data class IndentItem(
  val indent: String,
  val item: Item,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class ItemPath(
  val first: KeyValue,
  val access: List<KeyValue>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class OctCode(
  val code: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode

data class RepeatedValue(
  val elems: List<Value>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode

data class Unicode(
  val code: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode

sealed interface StringElem: AstNode

sealed interface Value: ItemValue, AstNode

data class DecValue(
  val sgn: Char?,
  val integral: String,
  val frac: String?,
  val exponent: Exponent?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): NumberValue, AstNode

data class DurationValue(
  val days: String?,
  val hours: String?,
  val minutes: String?,
  val seconds: Seconds?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ScalarValue, AstNode

data class SingleItem(
  val key: ItemPath,
  val value: ItemValue,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Item, AstNode

data class KeyValuePair(
  val key: ItemPath,
  val value: Value,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class PlainText(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode

data class NamedTimezone(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Timezone, AstNode

data class TimestampValue(
  val date: Date,
  val time: Time?,
  val timezone: Timezone?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ScalarValue, AstNode

data class EscapeCode(
  val code: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode

sealed interface Item: AstNode

sealed interface ItemValue: AstNode

data class Exponent(
  val sgn: Char?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class Time(
  val hour: String,
  val minute: String,
  val second: String?,
  val secondFrac: String?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class ObjectOrMapValue(
  val pairs: List<KeyValuePair>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode
enum class StringTypeAnnot { Base64, Hex }

fun matchStart(): List<IndentItem> {
  val lastGen = source.length
  val kernel = history[lastGen].getSingle(2, 1, 0, lastGen)
  return matchItems(kernel.beginGen, kernel.endGen)
}

fun matchItems(beginGen: Int, endGen: Int): List<IndentItem> {
val var1 = getSequenceElems(history, 3, listOf(4,30), beginGen, endGen)
val var2 = unrollRepeat0(history, 30, 32, 7, 31, var1[1].first, var1[1].second).map { k ->
val var3 = getSequenceElems(history, 33, listOf(34,19,20,214), k.first, k.second)
val var4 = matchIndentItem(var3[0].first, var3[0].second)
var4
}
return var2
}

fun matchIndentItem(beginGen: Int, endGen: Int): IndentItem {
val var5 = getSequenceElems(history, 35, listOf(19,36), beginGen, endGen)
val var6 = matchWS1(var5[0].first, var5[0].second)
val var7 = matchItem(var5[1].first, var5[1].second)
val var8 = IndentItem(var6.joinToString("") { it.toString() }, var7, nextId(), beginGen, endGen)
return var8
}

fun matchWS1(beginGen: Int, endGen: Int): List<Char> {
val var9 = unrollRepeat0(history, 6, 9, 7, 8, beginGen, endGen).map { k ->
source[k.first]
}
return var9
}

fun matchItem(beginGen: Int, endGen: Int): Item {
val var10 = history[endGen].findByBeginGenOpt(37, 1, beginGen)
val var11 = history[endGen].findByBeginGenOpt(209, 1, beginGen)
check(hasSingleTrue(var10 != null, var11 != null))
val var12 = when {
var10 != null -> {
val var13 = matchSingleItem(beginGen, endGen)
var13
}
else -> {
val var14 = matchListItem(beginGen, endGen)
var14
}
}
return var12
}

fun matchListItem(beginGen: Int, endGen: Int): ListItem {
val var15 = history[endGen].findByBeginGenOpt(210, 1, beginGen)
val var16 = history[endGen].findByBeginGenOpt(212, 1, beginGen)
check(hasSingleTrue(var15 != null, var16 != null))
val var17 = when {
var15 != null -> {
val var18 = matchListValueItem(beginGen, endGen)
var18
}
else -> {
val var19 = matchListFieldItem(beginGen, endGen)
var19
}
}
return var17
}

fun matchListFieldItem(beginGen: Int, endGen: Int): ListFieldItem {
val var20 = getSequenceElems(history, 213, listOf(167,19,39,19,113), beginGen, endGen)
val var21 = matchItemPath(var20[2].first, var20[2].second)
val var22 = matchItemValue(var20[4].first, var20[4].second)
val var23 = ListFieldItem(var21, var22, nextId(), beginGen, endGen)
return var23
}

fun matchItemPath(beginGen: Int, endGen: Int): ItemPath {
val var24 = getSequenceElems(history, 40, listOf(41,109), beginGen, endGen)
val var25 = matchKeyValue(var24[0].first, var24[0].second)
val var26 = unrollRepeat0(history, 109, 111, 7, 110, var24[1].first, var24[1].second).map { k ->
val var27 = getSequenceElems(history, 112, listOf(19,96,19,41), k.first, k.second)
val var28 = matchKeyValue(var27[3].first, var27[3].second)
var28
}
val var29 = ItemPath(var25, var26, nextId(), beginGen, endGen)
return var29
}

fun matchListValueItem(beginGen: Int, endGen: Int): ListValueItem {
val var30 = getSequenceElems(history, 211, listOf(167,19,118), beginGen, endGen)
val var31 = matchValue(var30[2].first, var30[2].second)
val var32 = ListValueItem(var31, nextId(), beginGen, endGen)
return var32
}

fun matchValue(beginGen: Int, endGen: Int): Value {
val var33 = history[endGen].findByBeginGenOpt(119, 1, beginGen)
val var34 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var35 = history[endGen].findByBeginGenOpt(198, 1, beginGen)
check(hasSingleTrue(var33 != null, var34 != null, var35 != null))
val var36 = when {
var33 != null -> {
val var37 = matchScalarValue(beginGen, endGen)
var37
}
var34 != null -> {
val var38 = matchRepeatedValue(beginGen, endGen)
var38
}
else -> {
val var39 = matchObjectOrMapValue(beginGen, endGen)
var39
}
}
return var36
}

fun matchScalarValue(beginGen: Int, endGen: Int): ScalarValue {
val var40 = history[endGen].findByBeginGenOpt(82, 1, beginGen)
val var41 = history[endGen].findByBeginGenOpt(120, 1, beginGen)
val var42 = history[endGen].findByBeginGenOpt(130, 1, beginGen)
val var43 = history[endGen].findByBeginGenOpt(131, 1, beginGen)
val var44 = history[endGen].findByBeginGenOpt(161, 1, beginGen)
check(hasSingleTrue(var40 != null, var41 != null, var42 != null, var43 != null, var44 != null))
val var45 = when {
var40 != null -> {
val var46 = matchNumberValue(beginGen, endGen)
var46
}
var41 != null -> {
val var47 = matchStringValue(beginGen, endGen)
var47
}
var42 != null -> {
val var48 = matchNameValue(beginGen, endGen)
var48
}
var43 != null -> {
val var49 = matchDurationValue(beginGen, endGen)
var49
}
else -> {
val var50 = matchTimestampValue(beginGen, endGen)
var50
}
}
return var45
}

fun matchStringValue(beginGen: Int, endGen: Int): StringValue {
val var51 = history[endGen].findByBeginGenOpt(121, 2, beginGen)
val var52 = history[endGen].findByBeginGenOpt(126, 2, beginGen)
check(hasSingleTrue(var51 != null, var52 != null))
val var53 = when {
var51 != null -> {
val var54 = getSequenceElems(history, 121, listOf(48,122), beginGen, endGen)
val var55 = matchStringFrac(var54[0].first, var54[0].second)
val var56 = unrollRepeat0(history, 122, 124, 7, 123, var54[1].first, var54[1].second).map { k ->
val var57 = getSequenceElems(history, 125, listOf(4,48), k.first, k.second)
val var58 = matchStringFrac(var57[1].first, var57[1].second)
var58
}
val var59 = StringValue(null, listOf(var55) + var56, nextId(), beginGen, endGen)
var59
}
else -> {
val var60 = getSequenceElems(history, 126, listOf(127,48), beginGen, endGen)
val var61 = matchStringTypeAnnot(var60[0].first, var60[0].second)
val var62 = matchStringFrac(var60[1].first, var60[1].second)
val var63 = StringValue(var61, listOf(var62), nextId(), beginGen, endGen)
var63
}
}
return var53
}

fun matchStringFrac(beginGen: Int, endGen: Int): StringFrac {
val var64 = getSequenceElems(history, 49, listOf(50,51,50), beginGen, endGen)
val var65 = unrollRepeat0(history, 51, 53, 7, 52, var64[1].first, var64[1].second).map { k ->
val var66 = matchStringElem(k.first, k.second)
var66
}
val var67 = StringFrac(var65, nextId(), beginGen, endGen)
return var67
}

fun matchStringElem(beginGen: Int, endGen: Int): StringElem {
val var68 = history[endGen].findByBeginGenOpt(54, 2, beginGen)
val var69 = history[endGen].findByBeginGenOpt(57, 3, beginGen)
val var70 = history[endGen].findByBeginGenOpt(64, 4, beginGen)
val var71 = history[endGen].findByBeginGenOpt(69, 6, beginGen)
val var72 = history[endGen].findByBeginGenOpt(71, 10, beginGen)
val var73 = history[endGen].findByBeginGenOpt(74, 10, beginGen)
val var74 = history[endGen].findByBeginGenOpt(76, 1, beginGen)
check(hasSingleTrue(var68 != null, var69 != null, var70 != null, var71 != null, var72 != null, var73 != null, var74 != null))
val var75 = when {
var68 != null -> {
val var76 = getSequenceElems(history, 54, listOf(55,56), beginGen, endGen)
val var77 = EscapeCode(source[var76[1].first], nextId(), beginGen, endGen)
var77
}
var69 != null -> {
val var78 = getSequenceElems(history, 57, listOf(55,58,60), beginGen, endGen)
val var79 = matchOct(var78[1].first, var78[1].second)
val var80 = history[var78[2].second].findByBeginGenOpt(29, 1, var78[2].first)
val var81 = history[var78[2].second].findByBeginGenOpt(61, 1, var78[2].first)
check(hasSingleTrue(var80 != null, var81 != null))
val var82 = when {
var80 != null -> null
else -> {
val var83 = getSequenceElems(history, 62, listOf(58,63), var78[2].first, var78[2].second)
val var84 = history[var83[1].second].findByBeginGenOpt(29, 1, var83[1].first)
val var85 = history[var83[1].second].findByBeginGenOpt(58, 1, var83[1].first)
check(hasSingleTrue(var84 != null, var85 != null))
val var86 = when {
var84 != null -> null
else -> {
val var87 = matchOct(var83[1].first, var83[1].second)
var87
}
}
var86
}
}
val var88 = OctCode(var79.toString() + (var82?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var88
}
var70 != null -> {
val var89 = getSequenceElems(history, 64, listOf(55,65,66,68), beginGen, endGen)
val var90 = matchHex(var89[2].first, var89[2].second)
val var91 = history[var89[3].second].findByBeginGenOpt(29, 1, var89[3].first)
val var92 = history[var89[3].second].findByBeginGenOpt(66, 1, var89[3].first)
check(hasSingleTrue(var91 != null, var92 != null))
val var93 = when {
var91 != null -> null
else -> {
val var94 = matchHex(var89[3].first, var89[3].second)
var94
}
}
val var95 = HexCode(var90.toString() + (var93?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var95
}
var71 != null -> {
val var96 = getSequenceElems(history, 69, listOf(55,70,66,66,66,66), beginGen, endGen)
val var97 = matchHex(var96[2].first, var96[2].second)
val var98 = matchHex(var96[3].first, var96[3].second)
val var99 = matchHex(var96[4].first, var96[4].second)
val var100 = matchHex(var96[5].first, var96[5].second)
val var101 = Unicode(var97.toString() + var98.toString() + var99.toString() + var100.toString(), nextId(), beginGen, endGen)
var101
}
var72 != null -> {
val var102 = getSequenceElems(history, 71, listOf(55,72,73,73,73,66,66,66,66,66), beginGen, endGen)
val var103 = matchHex(var102[5].first, var102[5].second)
val var104 = matchHex(var102[6].first, var102[6].second)
val var105 = matchHex(var102[7].first, var102[7].second)
val var106 = matchHex(var102[8].first, var102[8].second)
val var107 = matchHex(var102[9].first, var102[9].second)
val var108 = Unicode(source[var102[2].first].toString() + source[var102[3].first].toString() + source[var102[4].first].toString() + var103.toString() + var104.toString() + var105.toString() + var106.toString() + var107.toString(), nextId(), beginGen, endGen)
var108
}
var73 != null -> {
val var109 = getSequenceElems(history, 74, listOf(55,72,73,73,75,73,66,66,66,66), beginGen, endGen)
val var110 = matchHex(var109[6].first, var109[6].second)
val var111 = matchHex(var109[7].first, var109[7].second)
val var112 = matchHex(var109[8].first, var109[8].second)
val var113 = matchHex(var109[9].first, var109[9].second)
val var114 = Unicode(source[var109[2].first].toString() + source[var109[3].first].toString() + source[var109[4].first].toString() + source[var109[5].first].toString() + var110.toString() + var111.toString() + var112.toString() + var113.toString(), nextId(), beginGen, endGen)
var114
}
else -> {
val var115 = unrollRepeat1(history, 77, 78, 78, 81, beginGen, endGen).map { k ->
source[k.first]
}
val var116 = PlainText(var115.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
var116
}
}
return var75
}

fun matchHex(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchOct(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchNameValue(beginGen: Int, endGen: Int): NameValue {
val var117 = matchName(beginGen, endGen)
val var118 = NameValue(var117, nextId(), beginGen, endGen)
return var118
}

fun matchName(beginGen: Int, endGen: Int): String {
val var119 = getSequenceElems(history, 43, listOf(44,45), beginGen, endGen)
val var120 = unrollRepeat0(history, 45, 47, 7, 46, var119[1].first, var119[1].second).map { k ->
source[k.first]
}
return source[var119[0].first].toString() + var120.joinToString("") { it.toString() }
}

fun matchNumberValue(beginGen: Int, endGen: Int): NumberValue {
val var121 = history[endGen].findByBeginGenOpt(83, 1, beginGen)
val var122 = history[endGen].findByBeginGenOpt(101, 1, beginGen)
val var123 = history[endGen].findByBeginGenOpt(105, 1, beginGen)
check(hasSingleTrue(var121 != null, var122 != null, var123 != null))
val var124 = when {
var121 != null -> {
val var125 = matchDecValue(beginGen, endGen)
var125
}
var122 != null -> {
val var126 = matchOctValue(beginGen, endGen)
var126
}
else -> {
val var127 = matchHexValue(beginGen, endGen)
var127
}
}
return var124
}

fun matchOctValue(beginGen: Int, endGen: Int): OctValue {
val var128 = getSequenceElems(history, 102, listOf(85,73,103), beginGen, endGen)
val var129 = history[var128[0].second].findByBeginGenOpt(29, 1, var128[0].first)
val var130 = history[var128[0].second].findByBeginGenOpt(86, 1, var128[0].first)
check(hasSingleTrue(var129 != null, var130 != null))
val var131 = when {
var129 != null -> null
else -> source[var128[0].first]
}
val var132 = unrollRepeat1(history, 103, 58, 58, 104, var128[2].first, var128[2].second).map { k ->
val var133 = matchOct(k.first, k.second)
var133
}
val var134 = OctValue(var131, var132.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var134
}

fun matchDecValue(beginGen: Int, endGen: Int): DecValue {
val var135 = getSequenceElems(history, 84, listOf(85,87,93,97), beginGen, endGen)
val var136 = history[var135[0].second].findByBeginGenOpt(29, 1, var135[0].first)
val var137 = history[var135[0].second].findByBeginGenOpt(86, 1, var135[0].first)
check(hasSingleTrue(var136 != null, var137 != null))
val var138 = when {
var136 != null -> null
else -> source[var135[0].first]
}
val var139 = matchDec(var135[1].first, var135[1].second)
val var140 = history[var135[2].second].findByBeginGenOpt(29, 1, var135[2].first)
val var141 = history[var135[2].second].findByBeginGenOpt(94, 1, var135[2].first)
check(hasSingleTrue(var140 != null, var141 != null))
val var142 = when {
var140 != null -> null
else -> {
val var143 = getSequenceElems(history, 95, listOf(96,87), var135[2].first, var135[2].second)
val var144 = matchDec(var143[1].first, var143[1].second)
var144
}
}
val var145 = history[var135[3].second].findByBeginGenOpt(29, 1, var135[3].first)
val var146 = history[var135[3].second].findByBeginGenOpt(98, 1, var135[3].first)
check(hasSingleTrue(var145 != null, var146 != null))
val var147 = when {
var145 != null -> null
else -> {
val var148 = getSequenceElems(history, 99, listOf(100,85,87), var135[3].first, var135[3].second)
val var149 = history[var148[1].second].findByBeginGenOpt(29, 1, var148[1].first)
val var150 = history[var148[1].second].findByBeginGenOpt(86, 1, var148[1].first)
check(hasSingleTrue(var149 != null, var150 != null))
val var151 = when {
var149 != null -> null
else -> source[var148[1].first]
}
val var152 = matchDec(var148[2].first, var148[2].second)
val var153 = Exponent(var151, var152, nextId(), var135[3].first, var135[3].second)
var153
}
}
val var154 = DecValue(var138, var139, var142, var147, nextId(), beginGen, endGen)
return var154
}

fun matchDec(beginGen: Int, endGen: Int): String {
val var155 = history[endGen].findByBeginGenOpt(73, 1, beginGen)
val var156 = history[endGen].findByBeginGenOpt(88, 2, beginGen)
check(hasSingleTrue(var155 != null, var156 != null))
val var157 = when {
var155 != null -> "0"
else -> {
val var158 = getSequenceElems(history, 88, listOf(89,90), beginGen, endGen)
val var159 = unrollRepeat0(history, 90, 92, 7, 91, var158[1].first, var158[1].second).map { k ->
source[k.first]
}
source[var158[0].first].toString() + var159.joinToString("") { it.toString() }
}
}
return var157
}

fun matchHexValue(beginGen: Int, endGen: Int): HexValue {
val var160 = getSequenceElems(history, 106, listOf(85,73,65,107), beginGen, endGen)
val var161 = history[var160[0].second].findByBeginGenOpt(29, 1, var160[0].first)
val var162 = history[var160[0].second].findByBeginGenOpt(86, 1, var160[0].first)
check(hasSingleTrue(var161 != null, var162 != null))
val var163 = when {
var161 != null -> null
else -> source[var160[0].first]
}
val var164 = unrollRepeat1(history, 107, 66, 66, 108, var160[3].first, var160[3].second).map { k ->
val var165 = matchHex(k.first, k.second)
var165
}
val var166 = HexValue(var163, var164.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var166
}

fun matchTimestampValue(beginGen: Int, endGen: Int): TimestampValue {
val var167 = getSequenceElems(history, 162, listOf(163,170,179), beginGen, endGen)
val var168 = matchDate(var167[0].first, var167[0].second)
val var169 = history[var167[1].second].findByBeginGenOpt(29, 1, var167[1].first)
val var170 = history[var167[1].second].findByBeginGenOpt(171, 1, var167[1].first)
check(hasSingleTrue(var169 != null, var170 != null))
val var171 = when {
var169 != null -> null
else -> {
val var172 = getSequenceElems(history, 172, listOf(173,174), var167[1].first, var167[1].second)
val var173 = matchTime(var172[1].first, var172[1].second)
var173
}
}
val var174 = history[var167[2].second].findByBeginGenOpt(29, 1, var167[2].first)
val var175 = history[var167[2].second].findByBeginGenOpt(180, 1, var167[2].first)
check(hasSingleTrue(var174 != null, var175 != null))
val var176 = when {
var174 != null -> null
else -> {
val var177 = matchTimezone(var167[2].first, var167[2].second)
var177
}
}
val var178 = TimestampValue(var168, var171, var176, nextId(), beginGen, endGen)
return var178
}

fun matchTimezone(beginGen: Int, endGen: Int): Timezone {
val var179 = history[endGen].findByBeginGenOpt(181, 1, beginGen)
val var180 = history[endGen].findByBeginGenOpt(182, 2, beginGen)
val var181 = history[endGen].findByBeginGenOpt(183, 4, beginGen)
val var182 = history[endGen].findByBeginGenOpt(184, 3, beginGen)
check(hasSingleTrue(var179 != null, var180 != null, var181 != null, var182 != null))
val var183 = when {
var179 != null -> {
val var184 = NamedTimezone("UTC", nextId(), beginGen, endGen)
var184
}
var180 != null -> {
val var185 = getSequenceElems(history, 182, listOf(86,168), beginGen, endGen)
val var186 = matchDigit2(var185[1].first, var185[1].second)
val var187 = TimeOffset(var186, "00", nextId(), beginGen, endGen)
var187
}
var181 != null -> {
val var188 = getSequenceElems(history, 183, listOf(86,168,116,168), beginGen, endGen)
val var189 = matchDigit2(var188[1].first, var188[1].second)
val var190 = matchDigit2(var188[3].first, var188[3].second)
val var191 = TimeOffset(var189, var190, nextId(), beginGen, endGen)
var191
}
else -> {
val var192 = getSequenceElems(history, 184, listOf(86,168,168), beginGen, endGen)
val var193 = matchDigit2(var192[1].first, var192[1].second)
val var194 = matchDigit2(var192[2].first, var192[2].second)
val var195 = TimeOffset(var193, var194, nextId(), beginGen, endGen)
var195
}
}
return var183
}

fun matchItemValue(beginGen: Int, endGen: Int): ItemValue {
val var196 = history[endGen].findByBeginGenOpt(114, 1, beginGen)
val var197 = history[endGen].findByBeginGenOpt(117, 3, beginGen)
check(hasSingleTrue(var196 != null, var197 != null))
val var198 = when {
var196 != null -> {
val var199 = Header(nextId(), beginGen, endGen)
var199
}
else -> {
val var200 = getSequenceElems(history, 117, listOf(116,19,118), beginGen, endGen)
val var201 = matchValue(var200[2].first, var200[2].second)
var201
}
}
return var198
}

fun matchKeyValue(beginGen: Int, endGen: Int): KeyValue {
val var202 = history[endGen].findByBeginGenOpt(42, 1, beginGen)
val var203 = history[endGen].findByBeginGenOpt(48, 1, beginGen)
val var204 = history[endGen].findByBeginGenOpt(82, 1, beginGen)
check(hasSingleTrue(var202 != null, var203 != null, var204 != null))
val var205 = when {
var202 != null -> {
val var206 = matchName(beginGen, endGen)
val var207 = NameKey(var206, nextId(), beginGen, endGen)
var207
}
var203 != null -> {
val var208 = matchStringFrac(beginGen, endGen)
var208
}
else -> {
val var209 = matchNumberValue(beginGen, endGen)
var209
}
}
return var205
}

fun matchStringTypeAnnot(beginGen: Int, endGen: Int): StringTypeAnnot {
val var210 = history[endGen].findByBeginGenOpt(128, 1, beginGen)
val var211 = history[endGen].findByBeginGenOpt(129, 1, beginGen)
check(hasSingleTrue(var210 != null, var211 != null))
val var212 = when {
var210 != null -> StringTypeAnnot.Base64
else -> StringTypeAnnot.Hex
}
return var212
}

fun matchRepeatedValue(beginGen: Int, endGen: Int): RepeatedValue {
val var213 = history[endGen].findByBeginGenOpt(186, 3, beginGen)
val var214 = history[endGen].findByBeginGenOpt(189, 7, beginGen)
check(hasSingleTrue(var213 != null, var214 != null))
val var215 = when {
var213 != null -> {
val var216 = RepeatedValue(listOf(), nextId(), beginGen, endGen)
var216
}
else -> {
val var217 = getSequenceElems(history, 189, listOf(187,4,118,190,195,4,188), beginGen, endGen)
val var218 = matchValue(var217[2].first, var217[2].second)
val var219 = unrollRepeat0(history, 190, 192, 7, 191, var217[3].first, var217[3].second).map { k ->
val var220 = getSequenceElems(history, 193, listOf(4,194,4,118), k.first, k.second)
val var221 = matchValue(var220[3].first, var220[3].second)
var221
}
val var222 = RepeatedValue(listOf(var218) + var219, nextId(), beginGen, endGen)
var222
}
}
return var215
}

fun matchDurationValue(beginGen: Int, endGen: Int): DurationValue {
val var223 = history[endGen].findByBeginGenOpt(132, 4, beginGen)
val var224 = history[endGen].findByBeginGenOpt(136, 1, beginGen)
val var225 = history[endGen].findByBeginGenOpt(159, 3, beginGen)
val var226 = history[endGen].findByBeginGenOpt(160, 2, beginGen)
check(hasSingleTrue(var223 != null, var224 != null, var225 != null, var226 != null))
val var227 = when {
var223 != null -> {
val var228 = getSequenceElems(history, 132, listOf(133,140,145,151), beginGen, endGen)
val var229 = history[var228[0].second].findByBeginGenOpt(29, 1, var228[0].first)
val var230 = history[var228[0].second].findByBeginGenOpt(134, 1, var228[0].first)
check(hasSingleTrue(var229 != null, var230 != null))
val var231 = when {
var229 != null -> null
else -> {
val var232 = getSequenceElems(history, 135, listOf(136,19), var228[0].first, var228[0].second)
val var233 = matchDays(var232[0].first, var232[0].second)
var233
}
}
val var234 = history[var228[1].second].findByBeginGenOpt(29, 1, var228[1].first)
val var235 = history[var228[1].second].findByBeginGenOpt(141, 1, var228[1].first)
check(hasSingleTrue(var234 != null, var235 != null))
val var236 = when {
var234 != null -> null
else -> {
val var237 = getSequenceElems(history, 142, listOf(143,19), var228[1].first, var228[1].second)
val var238 = matchHours(var237[0].first, var237[0].second)
var238
}
}
val var239 = history[var228[2].second].findByBeginGenOpt(29, 1, var228[2].first)
val var240 = history[var228[2].second].findByBeginGenOpt(146, 1, var228[2].first)
check(hasSingleTrue(var239 != null, var240 != null))
val var241 = when {
var239 != null -> null
else -> {
val var242 = getSequenceElems(history, 147, listOf(148,19), var228[2].first, var228[2].second)
val var243 = matchMinutes(var242[0].first, var242[0].second)
var243
}
}
val var244 = matchSeconds(var228[3].first, var228[3].second)
val var245 = DurationValue(var231, var236, var241, var244, nextId(), beginGen, endGen)
var245
}
var224 != null -> {
val var246 = matchDays(beginGen, endGen)
val var247 = DurationValue(var246, null, null, null, nextId(), beginGen, endGen)
var247
}
var225 != null -> {
val var248 = getSequenceElems(history, 159, listOf(133,140,148), beginGen, endGen)
val var249 = history[var248[0].second].findByBeginGenOpt(29, 1, var248[0].first)
val var250 = history[var248[0].second].findByBeginGenOpt(134, 1, var248[0].first)
check(hasSingleTrue(var249 != null, var250 != null))
val var251 = when {
var249 != null -> null
else -> {
val var252 = getSequenceElems(history, 135, listOf(136,19), var248[0].first, var248[0].second)
val var253 = matchDays(var252[0].first, var252[0].second)
var253
}
}
val var254 = history[var248[1].second].findByBeginGenOpt(29, 1, var248[1].first)
val var255 = history[var248[1].second].findByBeginGenOpt(141, 1, var248[1].first)
check(hasSingleTrue(var254 != null, var255 != null))
val var256 = when {
var254 != null -> null
else -> {
val var257 = getSequenceElems(history, 142, listOf(143,19), var248[1].first, var248[1].second)
val var258 = matchHours(var257[0].first, var257[0].second)
var258
}
}
val var259 = matchMinutes(var248[2].first, var248[2].second)
val var260 = DurationValue(var251, var256, var259, null, nextId(), beginGen, endGen)
var260
}
else -> {
val var261 = getSequenceElems(history, 160, listOf(133,143), beginGen, endGen)
val var262 = history[var261[0].second].findByBeginGenOpt(29, 1, var261[0].first)
val var263 = history[var261[0].second].findByBeginGenOpt(134, 1, var261[0].first)
check(hasSingleTrue(var262 != null, var263 != null))
val var264 = when {
var262 != null -> null
else -> {
val var265 = getSequenceElems(history, 135, listOf(136,19), var261[0].first, var261[0].second)
val var266 = matchDays(var265[0].first, var265[0].second)
var266
}
}
val var267 = matchHours(var261[1].first, var261[1].second)
val var268 = DurationValue(var264, var267, null, null, nextId(), beginGen, endGen)
var268
}
}
return var227
}

fun matchHours(beginGen: Int, endGen: Int): String {
val var269 = getSequenceElems(history, 144, listOf(138,19,129), beginGen, endGen)
val var270 = matchNumber(var269[0].first, var269[0].second)
return var270
}

fun matchNumber(beginGen: Int, endGen: Int): String {
val var271 = history[endGen].findByBeginGenOpt(73, 1, beginGen)
val var272 = history[endGen].findByBeginGenOpt(88, 2, beginGen)
check(hasSingleTrue(var271 != null, var272 != null))
val var273 = when {
var271 != null -> "0"
else -> {
val var274 = getSequenceElems(history, 88, listOf(89,90), beginGen, endGen)
val var275 = unrollRepeat0(history, 90, 92, 7, 91, var274[1].first, var274[1].second).map { k ->
source[k.first]
}
source[var274[0].first].toString() + var275.joinToString("") { it.toString() }
}
}
return var273
}

fun matchSingleItem(beginGen: Int, endGen: Int): SingleItem {
val var276 = getSequenceElems(history, 38, listOf(39,19,113), beginGen, endGen)
val var277 = matchItemPath(var276[0].first, var276[0].second)
val var278 = matchItemValue(var276[2].first, var276[2].second)
val var279 = SingleItem(var277, var278, nextId(), beginGen, endGen)
return var279
}

fun matchDays(beginGen: Int, endGen: Int): String {
val var280 = getSequenceElems(history, 137, listOf(138,19,139), beginGen, endGen)
val var281 = matchNumber(var280[0].first, var280[0].second)
return var281
}

fun matchDigit2(beginGen: Int, endGen: Int): String {
val var282 = getSequenceElems(history, 169, listOf(92,92), beginGen, endGen)
return source[var282[0].first].toString() + source[var282[1].first].toString()
}

fun matchDate(beginGen: Int, endGen: Int): Date {
val var283 = getSequenceElems(history, 164, listOf(165,167,168,167,168), beginGen, endGen)
val var284 = matchDigit4(var283[0].first, var283[0].second)
val var285 = matchDigit2(var283[2].first, var283[2].second)
val var286 = matchDigit2(var283[4].first, var283[4].second)
val var287 = Date(var284, var285, var286, nextId(), beginGen, endGen)
return var287
}

fun matchDigit4(beginGen: Int, endGen: Int): String {
val var288 = getSequenceElems(history, 166, listOf(92,92,92,92), beginGen, endGen)
return source[var288[0].first].toString() + source[var288[1].first].toString() + source[var288[2].first].toString() + source[var288[3].first].toString()
}

fun matchTime(beginGen: Int, endGen: Int): Time {
val var289 = history[endGen].findByBeginGenOpt(175, 6, beginGen)
val var290 = history[endGen].findByBeginGenOpt(176, 4, beginGen)
val var291 = history[endGen].findByBeginGenOpt(177, 3, beginGen)
val var292 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
check(hasSingleTrue(var289 != null, var290 != null, var291 != null, var292 != null))
val var293 = when {
var289 != null -> {
val var294 = getSequenceElems(history, 175, listOf(168,116,168,116,168,153), beginGen, endGen)
val var295 = matchDigit2(var294[0].first, var294[0].second)
val var296 = matchDigit2(var294[2].first, var294[2].second)
val var297 = matchDigit2(var294[4].first, var294[4].second)
val var298 = history[var294[5].second].findByBeginGenOpt(29, 1, var294[5].first)
val var299 = history[var294[5].second].findByBeginGenOpt(154, 1, var294[5].first)
check(hasSingleTrue(var298 != null, var299 != null))
val var300 = when {
var298 != null -> null
else -> {
val var301 = matchSecondFrac(var294[5].first, var294[5].second)
var301
}
}
val var302 = Time(var295, var296, var297, var300, nextId(), beginGen, endGen)
var302
}
var290 != null -> {
val var303 = getSequenceElems(history, 176, listOf(168,168,168,153), beginGen, endGen)
val var304 = matchDigit2(var303[0].first, var303[0].second)
val var305 = matchDigit2(var303[1].first, var303[1].second)
val var306 = matchDigit2(var303[2].first, var303[2].second)
val var307 = history[var303[3].second].findByBeginGenOpt(29, 1, var303[3].first)
val var308 = history[var303[3].second].findByBeginGenOpt(154, 1, var303[3].first)
check(hasSingleTrue(var307 != null, var308 != null))
val var309 = when {
var307 != null -> null
else -> {
val var310 = matchSecondFrac(var303[3].first, var303[3].second)
var310
}
}
val var311 = Time(var304, var305, var306, var309, nextId(), beginGen, endGen)
var311
}
var291 != null -> {
val var312 = getSequenceElems(history, 177, listOf(168,116,168), beginGen, endGen)
val var313 = matchDigit2(var312[0].first, var312[0].second)
val var314 = matchDigit2(var312[2].first, var312[2].second)
val var315 = Time(var313, var314, null, null, nextId(), beginGen, endGen)
var315
}
else -> {
val var316 = getSequenceElems(history, 178, listOf(168,168), beginGen, endGen)
val var317 = matchDigit2(var316[0].first, var316[0].second)
val var318 = matchDigit2(var316[1].first, var316[1].second)
val var319 = Time(var317, var318, null, null, nextId(), beginGen, endGen)
var319
}
}
return var293
}

fun matchObjectOrMapValue(beginGen: Int, endGen: Int): ObjectOrMapValue {
val var320 = history[endGen].findByBeginGenOpt(199, 3, beginGen)
val var321 = history[endGen].findByBeginGenOpt(202, 6, beginGen)
check(hasSingleTrue(var320 != null, var321 != null))
val var322 = when {
var320 != null -> {
val var323 = ObjectOrMapValue(listOf(), nextId(), beginGen, endGen)
var323
}
else -> {
val var324 = getSequenceElems(history, 202, listOf(200,4,203,205,4,201), beginGen, endGen)
val var325 = matchKeyValuePair(var324[2].first, var324[2].second)
val var326 = unrollRepeat0(history, 205, 207, 7, 206, var324[3].first, var324[3].second).map { k ->
val var327 = getSequenceElems(history, 208, listOf(195,4,203), k.first, k.second)
val var328 = matchKeyValuePair(var327[2].first, var327[2].second)
var328
}
val var329 = ObjectOrMapValue(listOf(var325) + var326, nextId(), beginGen, endGen)
var329
}
}
return var322
}

fun matchKeyValuePair(beginGen: Int, endGen: Int): KeyValuePair {
val var330 = getSequenceElems(history, 204, listOf(39,4,116,4,118), beginGen, endGen)
val var331 = matchItemPath(var330[0].first, var330[0].second)
val var332 = matchValue(var330[4].first, var330[4].second)
val var333 = KeyValuePair(var331, var332, nextId(), beginGen, endGen)
return var333
}

fun matchMinutes(beginGen: Int, endGen: Int): String {
val var334 = getSequenceElems(history, 149, listOf(138,19,150), beginGen, endGen)
val var335 = matchNumber(var334[0].first, var334[0].second)
return var335
}

fun matchSeconds(beginGen: Int, endGen: Int): Seconds {
val var336 = getSequenceElems(history, 152, listOf(138,153,19,158), beginGen, endGen)
val var337 = matchNumber(var336[0].first, var336[0].second)
val var338 = history[var336[1].second].findByBeginGenOpt(29, 1, var336[1].first)
val var339 = history[var336[1].second].findByBeginGenOpt(154, 1, var336[1].first)
check(hasSingleTrue(var338 != null, var339 != null))
val var340 = when {
var338 != null -> null
else -> {
val var341 = matchSecondFrac(var336[1].first, var336[1].second)
var341
}
}
val var342 = Seconds(var337, var340, nextId(), beginGen, endGen)
return var342
}

fun matchSecondFrac(beginGen: Int, endGen: Int): String {
val var343 = getSequenceElems(history, 155, listOf(96,156), beginGen, endGen)
val var344 = unrollRepeat1(history, 156, 92, 92, 157, var343[1].first, var343[1].second).map { k ->
source[k.first]
}
return var344.joinToString("") { it.toString() }
}

}
