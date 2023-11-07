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
  val innerIndent: String,
  val key: ItemPath,
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
  val path: List<KeyValue>,
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
val var21 = matchWS1(var20[1].first, var20[1].second)
val var22 = matchItemPath(var20[2].first, var20[2].second)
val var23 = matchItemValue(var20[4].first, var20[4].second)
val var24 = ListFieldItem(source[var20[0].first].toString() + var21.joinToString("") { it.toString() }, var22, var23, nextId(), beginGen, endGen)
return var24
}

fun matchItemPath(beginGen: Int, endGen: Int): ItemPath {
val var25 = getSequenceElems(history, 40, listOf(41,109), beginGen, endGen)
val var26 = matchKeyValue(var25[0].first, var25[0].second)
val var27 = unrollRepeat0(history, 109, 111, 7, 110, var25[1].first, var25[1].second).map { k ->
val var28 = getSequenceElems(history, 112, listOf(19,96,19,41), k.first, k.second)
val var29 = matchKeyValue(var28[3].first, var28[3].second)
var29
}
val var30 = ItemPath(listOf(var26) + var27, nextId(), beginGen, endGen)
return var30
}

fun matchListValueItem(beginGen: Int, endGen: Int): ListValueItem {
val var31 = getSequenceElems(history, 211, listOf(167,19,118), beginGen, endGen)
val var32 = matchValue(var31[2].first, var31[2].second)
val var33 = ListValueItem(var32, nextId(), beginGen, endGen)
return var33
}

fun matchValue(beginGen: Int, endGen: Int): Value {
val var34 = history[endGen].findByBeginGenOpt(119, 1, beginGen)
val var35 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var36 = history[endGen].findByBeginGenOpt(198, 1, beginGen)
check(hasSingleTrue(var34 != null, var35 != null, var36 != null))
val var37 = when {
var34 != null -> {
val var38 = matchScalarValue(beginGen, endGen)
var38
}
var35 != null -> {
val var39 = matchRepeatedValue(beginGen, endGen)
var39
}
else -> {
val var40 = matchObjectOrMapValue(beginGen, endGen)
var40
}
}
return var37
}

fun matchScalarValue(beginGen: Int, endGen: Int): ScalarValue {
val var41 = history[endGen].findByBeginGenOpt(82, 1, beginGen)
val var42 = history[endGen].findByBeginGenOpt(120, 1, beginGen)
val var43 = history[endGen].findByBeginGenOpt(130, 1, beginGen)
val var44 = history[endGen].findByBeginGenOpt(131, 1, beginGen)
val var45 = history[endGen].findByBeginGenOpt(161, 1, beginGen)
check(hasSingleTrue(var41 != null, var42 != null, var43 != null, var44 != null, var45 != null))
val var46 = when {
var41 != null -> {
val var47 = matchNumberValue(beginGen, endGen)
var47
}
var42 != null -> {
val var48 = matchStringValue(beginGen, endGen)
var48
}
var43 != null -> {
val var49 = matchNameValue(beginGen, endGen)
var49
}
var44 != null -> {
val var50 = matchDurationValue(beginGen, endGen)
var50
}
else -> {
val var51 = matchTimestampValue(beginGen, endGen)
var51
}
}
return var46
}

fun matchStringValue(beginGen: Int, endGen: Int): StringValue {
val var52 = history[endGen].findByBeginGenOpt(121, 2, beginGen)
val var53 = history[endGen].findByBeginGenOpt(126, 2, beginGen)
check(hasSingleTrue(var52 != null, var53 != null))
val var54 = when {
var52 != null -> {
val var55 = getSequenceElems(history, 121, listOf(48,122), beginGen, endGen)
val var56 = matchStringFrac(var55[0].first, var55[0].second)
val var57 = unrollRepeat0(history, 122, 124, 7, 123, var55[1].first, var55[1].second).map { k ->
val var58 = getSequenceElems(history, 125, listOf(4,48), k.first, k.second)
val var59 = matchStringFrac(var58[1].first, var58[1].second)
var59
}
val var60 = StringValue(null, listOf(var56) + var57, nextId(), beginGen, endGen)
var60
}
else -> {
val var61 = getSequenceElems(history, 126, listOf(127,48), beginGen, endGen)
val var62 = matchStringTypeAnnot(var61[0].first, var61[0].second)
val var63 = matchStringFrac(var61[1].first, var61[1].second)
val var64 = StringValue(var62, listOf(var63), nextId(), beginGen, endGen)
var64
}
}
return var54
}

fun matchStringFrac(beginGen: Int, endGen: Int): StringFrac {
val var65 = getSequenceElems(history, 49, listOf(50,51,50), beginGen, endGen)
val var66 = unrollRepeat0(history, 51, 53, 7, 52, var65[1].first, var65[1].second).map { k ->
val var67 = matchStringElem(k.first, k.second)
var67
}
val var68 = StringFrac(var66, nextId(), beginGen, endGen)
return var68
}

fun matchStringElem(beginGen: Int, endGen: Int): StringElem {
val var69 = history[endGen].findByBeginGenOpt(54, 2, beginGen)
val var70 = history[endGen].findByBeginGenOpt(57, 3, beginGen)
val var71 = history[endGen].findByBeginGenOpt(64, 4, beginGen)
val var72 = history[endGen].findByBeginGenOpt(69, 6, beginGen)
val var73 = history[endGen].findByBeginGenOpt(71, 10, beginGen)
val var74 = history[endGen].findByBeginGenOpt(74, 10, beginGen)
val var75 = history[endGen].findByBeginGenOpt(76, 1, beginGen)
check(hasSingleTrue(var69 != null, var70 != null, var71 != null, var72 != null, var73 != null, var74 != null, var75 != null))
val var76 = when {
var69 != null -> {
val var77 = getSequenceElems(history, 54, listOf(55,56), beginGen, endGen)
val var78 = EscapeCode(source[var77[1].first], nextId(), beginGen, endGen)
var78
}
var70 != null -> {
val var79 = getSequenceElems(history, 57, listOf(55,58,60), beginGen, endGen)
val var80 = matchOct(var79[1].first, var79[1].second)
val var81 = history[var79[2].second].findByBeginGenOpt(29, 1, var79[2].first)
val var82 = history[var79[2].second].findByBeginGenOpt(61, 1, var79[2].first)
check(hasSingleTrue(var81 != null, var82 != null))
val var83 = when {
var81 != null -> null
else -> {
val var84 = getSequenceElems(history, 62, listOf(58,63), var79[2].first, var79[2].second)
val var85 = history[var84[1].second].findByBeginGenOpt(29, 1, var84[1].first)
val var86 = history[var84[1].second].findByBeginGenOpt(58, 1, var84[1].first)
check(hasSingleTrue(var85 != null, var86 != null))
val var87 = when {
var85 != null -> null
else -> {
val var88 = matchOct(var84[1].first, var84[1].second)
var88
}
}
var87
}
}
val var89 = OctCode(var80.toString() + (var83?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var89
}
var71 != null -> {
val var90 = getSequenceElems(history, 64, listOf(55,65,66,68), beginGen, endGen)
val var91 = matchHex(var90[2].first, var90[2].second)
val var92 = history[var90[3].second].findByBeginGenOpt(29, 1, var90[3].first)
val var93 = history[var90[3].second].findByBeginGenOpt(66, 1, var90[3].first)
check(hasSingleTrue(var92 != null, var93 != null))
val var94 = when {
var92 != null -> null
else -> {
val var95 = matchHex(var90[3].first, var90[3].second)
var95
}
}
val var96 = HexCode(var91.toString() + (var94?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var96
}
var72 != null -> {
val var97 = getSequenceElems(history, 69, listOf(55,70,66,66,66,66), beginGen, endGen)
val var98 = matchHex(var97[2].first, var97[2].second)
val var99 = matchHex(var97[3].first, var97[3].second)
val var100 = matchHex(var97[4].first, var97[4].second)
val var101 = matchHex(var97[5].first, var97[5].second)
val var102 = Unicode(var98.toString() + var99.toString() + var100.toString() + var101.toString(), nextId(), beginGen, endGen)
var102
}
var73 != null -> {
val var103 = getSequenceElems(history, 71, listOf(55,72,73,73,73,66,66,66,66,66), beginGen, endGen)
val var104 = matchHex(var103[5].first, var103[5].second)
val var105 = matchHex(var103[6].first, var103[6].second)
val var106 = matchHex(var103[7].first, var103[7].second)
val var107 = matchHex(var103[8].first, var103[8].second)
val var108 = matchHex(var103[9].first, var103[9].second)
val var109 = Unicode(source[var103[2].first].toString() + source[var103[3].first].toString() + source[var103[4].first].toString() + var104.toString() + var105.toString() + var106.toString() + var107.toString() + var108.toString(), nextId(), beginGen, endGen)
var109
}
var74 != null -> {
val var110 = getSequenceElems(history, 74, listOf(55,72,73,73,75,73,66,66,66,66), beginGen, endGen)
val var111 = matchHex(var110[6].first, var110[6].second)
val var112 = matchHex(var110[7].first, var110[7].second)
val var113 = matchHex(var110[8].first, var110[8].second)
val var114 = matchHex(var110[9].first, var110[9].second)
val var115 = Unicode(source[var110[2].first].toString() + source[var110[3].first].toString() + source[var110[4].first].toString() + source[var110[5].first].toString() + var111.toString() + var112.toString() + var113.toString() + var114.toString(), nextId(), beginGen, endGen)
var115
}
else -> {
val var116 = unrollRepeat1(history, 77, 78, 78, 81, beginGen, endGen).map { k ->
source[k.first]
}
val var117 = PlainText(var116.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
var117
}
}
return var76
}

fun matchHex(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchOct(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchNameValue(beginGen: Int, endGen: Int): NameValue {
val var118 = matchName(beginGen, endGen)
val var119 = NameValue(var118, nextId(), beginGen, endGen)
return var119
}

fun matchName(beginGen: Int, endGen: Int): String {
val var120 = getSequenceElems(history, 43, listOf(44,45), beginGen, endGen)
val var121 = unrollRepeat0(history, 45, 47, 7, 46, var120[1].first, var120[1].second).map { k ->
source[k.first]
}
return source[var120[0].first].toString() + var121.joinToString("") { it.toString() }
}

fun matchNumberValue(beginGen: Int, endGen: Int): NumberValue {
val var122 = history[endGen].findByBeginGenOpt(83, 1, beginGen)
val var123 = history[endGen].findByBeginGenOpt(101, 1, beginGen)
val var124 = history[endGen].findByBeginGenOpt(105, 1, beginGen)
check(hasSingleTrue(var122 != null, var123 != null, var124 != null))
val var125 = when {
var122 != null -> {
val var126 = matchDecValue(beginGen, endGen)
var126
}
var123 != null -> {
val var127 = matchOctValue(beginGen, endGen)
var127
}
else -> {
val var128 = matchHexValue(beginGen, endGen)
var128
}
}
return var125
}

fun matchOctValue(beginGen: Int, endGen: Int): OctValue {
val var129 = getSequenceElems(history, 102, listOf(85,73,103), beginGen, endGen)
val var130 = history[var129[0].second].findByBeginGenOpt(29, 1, var129[0].first)
val var131 = history[var129[0].second].findByBeginGenOpt(86, 1, var129[0].first)
check(hasSingleTrue(var130 != null, var131 != null))
val var132 = when {
var130 != null -> null
else -> source[var129[0].first]
}
val var133 = unrollRepeat1(history, 103, 58, 58, 104, var129[2].first, var129[2].second).map { k ->
val var134 = matchOct(k.first, k.second)
var134
}
val var135 = OctValue(var132, var133.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var135
}

fun matchDecValue(beginGen: Int, endGen: Int): DecValue {
val var136 = getSequenceElems(history, 84, listOf(85,87,93,97), beginGen, endGen)
val var137 = history[var136[0].second].findByBeginGenOpt(29, 1, var136[0].first)
val var138 = history[var136[0].second].findByBeginGenOpt(86, 1, var136[0].first)
check(hasSingleTrue(var137 != null, var138 != null))
val var139 = when {
var137 != null -> null
else -> source[var136[0].first]
}
val var140 = matchDec(var136[1].first, var136[1].second)
val var141 = history[var136[2].second].findByBeginGenOpt(29, 1, var136[2].first)
val var142 = history[var136[2].second].findByBeginGenOpt(94, 1, var136[2].first)
check(hasSingleTrue(var141 != null, var142 != null))
val var143 = when {
var141 != null -> null
else -> {
val var144 = getSequenceElems(history, 95, listOf(96,87), var136[2].first, var136[2].second)
val var145 = matchDec(var144[1].first, var144[1].second)
var145
}
}
val var146 = history[var136[3].second].findByBeginGenOpt(29, 1, var136[3].first)
val var147 = history[var136[3].second].findByBeginGenOpt(98, 1, var136[3].first)
check(hasSingleTrue(var146 != null, var147 != null))
val var148 = when {
var146 != null -> null
else -> {
val var149 = getSequenceElems(history, 99, listOf(100,85,87), var136[3].first, var136[3].second)
val var150 = history[var149[1].second].findByBeginGenOpt(29, 1, var149[1].first)
val var151 = history[var149[1].second].findByBeginGenOpt(86, 1, var149[1].first)
check(hasSingleTrue(var150 != null, var151 != null))
val var152 = when {
var150 != null -> null
else -> source[var149[1].first]
}
val var153 = matchDec(var149[2].first, var149[2].second)
val var154 = Exponent(var152, var153, nextId(), var136[3].first, var136[3].second)
var154
}
}
val var155 = DecValue(var139, var140, var143, var148, nextId(), beginGen, endGen)
return var155
}

fun matchDec(beginGen: Int, endGen: Int): String {
val var156 = history[endGen].findByBeginGenOpt(73, 1, beginGen)
val var157 = history[endGen].findByBeginGenOpt(88, 2, beginGen)
check(hasSingleTrue(var156 != null, var157 != null))
val var158 = when {
var156 != null -> "0"
else -> {
val var159 = getSequenceElems(history, 88, listOf(89,90), beginGen, endGen)
val var160 = unrollRepeat0(history, 90, 92, 7, 91, var159[1].first, var159[1].second).map { k ->
source[k.first]
}
source[var159[0].first].toString() + var160.joinToString("") { it.toString() }
}
}
return var158
}

fun matchHexValue(beginGen: Int, endGen: Int): HexValue {
val var161 = getSequenceElems(history, 106, listOf(85,73,65,107), beginGen, endGen)
val var162 = history[var161[0].second].findByBeginGenOpt(29, 1, var161[0].first)
val var163 = history[var161[0].second].findByBeginGenOpt(86, 1, var161[0].first)
check(hasSingleTrue(var162 != null, var163 != null))
val var164 = when {
var162 != null -> null
else -> source[var161[0].first]
}
val var165 = unrollRepeat1(history, 107, 66, 66, 108, var161[3].first, var161[3].second).map { k ->
val var166 = matchHex(k.first, k.second)
var166
}
val var167 = HexValue(var164, var165.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var167
}

fun matchTimestampValue(beginGen: Int, endGen: Int): TimestampValue {
val var168 = getSequenceElems(history, 162, listOf(163,170,179), beginGen, endGen)
val var169 = matchDate(var168[0].first, var168[0].second)
val var170 = history[var168[1].second].findByBeginGenOpt(29, 1, var168[1].first)
val var171 = history[var168[1].second].findByBeginGenOpt(171, 1, var168[1].first)
check(hasSingleTrue(var170 != null, var171 != null))
val var172 = when {
var170 != null -> null
else -> {
val var173 = getSequenceElems(history, 172, listOf(173,174), var168[1].first, var168[1].second)
val var174 = matchTime(var173[1].first, var173[1].second)
var174
}
}
val var175 = history[var168[2].second].findByBeginGenOpt(29, 1, var168[2].first)
val var176 = history[var168[2].second].findByBeginGenOpt(180, 1, var168[2].first)
check(hasSingleTrue(var175 != null, var176 != null))
val var177 = when {
var175 != null -> null
else -> {
val var178 = matchTimezone(var168[2].first, var168[2].second)
var178
}
}
val var179 = TimestampValue(var169, var172, var177, nextId(), beginGen, endGen)
return var179
}

fun matchTimezone(beginGen: Int, endGen: Int): Timezone {
val var180 = history[endGen].findByBeginGenOpt(181, 1, beginGen)
val var181 = history[endGen].findByBeginGenOpt(182, 2, beginGen)
val var182 = history[endGen].findByBeginGenOpt(183, 4, beginGen)
val var183 = history[endGen].findByBeginGenOpt(184, 3, beginGen)
check(hasSingleTrue(var180 != null, var181 != null, var182 != null, var183 != null))
val var184 = when {
var180 != null -> {
val var185 = NamedTimezone("UTC", nextId(), beginGen, endGen)
var185
}
var181 != null -> {
val var186 = getSequenceElems(history, 182, listOf(86,168), beginGen, endGen)
val var187 = matchDigit2(var186[1].first, var186[1].second)
val var188 = TimeOffset(var187, "00", nextId(), beginGen, endGen)
var188
}
var182 != null -> {
val var189 = getSequenceElems(history, 183, listOf(86,168,116,168), beginGen, endGen)
val var190 = matchDigit2(var189[1].first, var189[1].second)
val var191 = matchDigit2(var189[3].first, var189[3].second)
val var192 = TimeOffset(var190, var191, nextId(), beginGen, endGen)
var192
}
else -> {
val var193 = getSequenceElems(history, 184, listOf(86,168,168), beginGen, endGen)
val var194 = matchDigit2(var193[1].first, var193[1].second)
val var195 = matchDigit2(var193[2].first, var193[2].second)
val var196 = TimeOffset(var194, var195, nextId(), beginGen, endGen)
var196
}
}
return var184
}

fun matchItemValue(beginGen: Int, endGen: Int): ItemValue {
val var197 = history[endGen].findByBeginGenOpt(114, 1, beginGen)
val var198 = history[endGen].findByBeginGenOpt(117, 3, beginGen)
check(hasSingleTrue(var197 != null, var198 != null))
val var199 = when {
var197 != null -> {
val var200 = Header(nextId(), beginGen, endGen)
var200
}
else -> {
val var201 = getSequenceElems(history, 117, listOf(116,19,118), beginGen, endGen)
val var202 = matchValue(var201[2].first, var201[2].second)
var202
}
}
return var199
}

fun matchKeyValue(beginGen: Int, endGen: Int): KeyValue {
val var203 = history[endGen].findByBeginGenOpt(42, 1, beginGen)
val var204 = history[endGen].findByBeginGenOpt(48, 1, beginGen)
val var205 = history[endGen].findByBeginGenOpt(82, 1, beginGen)
check(hasSingleTrue(var203 != null, var204 != null, var205 != null))
val var206 = when {
var203 != null -> {
val var207 = matchName(beginGen, endGen)
val var208 = NameKey(var207, nextId(), beginGen, endGen)
var208
}
var204 != null -> {
val var209 = matchStringFrac(beginGen, endGen)
var209
}
else -> {
val var210 = matchNumberValue(beginGen, endGen)
var210
}
}
return var206
}

fun matchStringTypeAnnot(beginGen: Int, endGen: Int): StringTypeAnnot {
val var211 = history[endGen].findByBeginGenOpt(128, 1, beginGen)
val var212 = history[endGen].findByBeginGenOpt(129, 1, beginGen)
check(hasSingleTrue(var211 != null, var212 != null))
val var213 = when {
var211 != null -> StringTypeAnnot.Base64
else -> StringTypeAnnot.Hex
}
return var213
}

fun matchRepeatedValue(beginGen: Int, endGen: Int): RepeatedValue {
val var214 = history[endGen].findByBeginGenOpt(186, 3, beginGen)
val var215 = history[endGen].findByBeginGenOpt(189, 7, beginGen)
check(hasSingleTrue(var214 != null, var215 != null))
val var216 = when {
var214 != null -> {
val var217 = RepeatedValue(listOf(), nextId(), beginGen, endGen)
var217
}
else -> {
val var218 = getSequenceElems(history, 189, listOf(187,4,118,190,195,4,188), beginGen, endGen)
val var219 = matchValue(var218[2].first, var218[2].second)
val var220 = unrollRepeat0(history, 190, 192, 7, 191, var218[3].first, var218[3].second).map { k ->
val var221 = getSequenceElems(history, 193, listOf(4,194,4,118), k.first, k.second)
val var222 = matchValue(var221[3].first, var221[3].second)
var222
}
val var223 = RepeatedValue(listOf(var219) + var220, nextId(), beginGen, endGen)
var223
}
}
return var216
}

fun matchDurationValue(beginGen: Int, endGen: Int): DurationValue {
val var224 = history[endGen].findByBeginGenOpt(132, 4, beginGen)
val var225 = history[endGen].findByBeginGenOpt(136, 1, beginGen)
val var226 = history[endGen].findByBeginGenOpt(159, 3, beginGen)
val var227 = history[endGen].findByBeginGenOpt(160, 2, beginGen)
check(hasSingleTrue(var224 != null, var225 != null, var226 != null, var227 != null))
val var228 = when {
var224 != null -> {
val var229 = getSequenceElems(history, 132, listOf(133,140,145,151), beginGen, endGen)
val var230 = history[var229[0].second].findByBeginGenOpt(29, 1, var229[0].first)
val var231 = history[var229[0].second].findByBeginGenOpt(134, 1, var229[0].first)
check(hasSingleTrue(var230 != null, var231 != null))
val var232 = when {
var230 != null -> null
else -> {
val var233 = getSequenceElems(history, 135, listOf(136,19), var229[0].first, var229[0].second)
val var234 = matchDays(var233[0].first, var233[0].second)
var234
}
}
val var235 = history[var229[1].second].findByBeginGenOpt(29, 1, var229[1].first)
val var236 = history[var229[1].second].findByBeginGenOpt(141, 1, var229[1].first)
check(hasSingleTrue(var235 != null, var236 != null))
val var237 = when {
var235 != null -> null
else -> {
val var238 = getSequenceElems(history, 142, listOf(143,19), var229[1].first, var229[1].second)
val var239 = matchHours(var238[0].first, var238[0].second)
var239
}
}
val var240 = history[var229[2].second].findByBeginGenOpt(29, 1, var229[2].first)
val var241 = history[var229[2].second].findByBeginGenOpt(146, 1, var229[2].first)
check(hasSingleTrue(var240 != null, var241 != null))
val var242 = when {
var240 != null -> null
else -> {
val var243 = getSequenceElems(history, 147, listOf(148,19), var229[2].first, var229[2].second)
val var244 = matchMinutes(var243[0].first, var243[0].second)
var244
}
}
val var245 = matchSeconds(var229[3].first, var229[3].second)
val var246 = DurationValue(var232, var237, var242, var245, nextId(), beginGen, endGen)
var246
}
var225 != null -> {
val var247 = matchDays(beginGen, endGen)
val var248 = DurationValue(var247, null, null, null, nextId(), beginGen, endGen)
var248
}
var226 != null -> {
val var249 = getSequenceElems(history, 159, listOf(133,140,148), beginGen, endGen)
val var250 = history[var249[0].second].findByBeginGenOpt(29, 1, var249[0].first)
val var251 = history[var249[0].second].findByBeginGenOpt(134, 1, var249[0].first)
check(hasSingleTrue(var250 != null, var251 != null))
val var252 = when {
var250 != null -> null
else -> {
val var253 = getSequenceElems(history, 135, listOf(136,19), var249[0].first, var249[0].second)
val var254 = matchDays(var253[0].first, var253[0].second)
var254
}
}
val var255 = history[var249[1].second].findByBeginGenOpt(29, 1, var249[1].first)
val var256 = history[var249[1].second].findByBeginGenOpt(141, 1, var249[1].first)
check(hasSingleTrue(var255 != null, var256 != null))
val var257 = when {
var255 != null -> null
else -> {
val var258 = getSequenceElems(history, 142, listOf(143,19), var249[1].first, var249[1].second)
val var259 = matchHours(var258[0].first, var258[0].second)
var259
}
}
val var260 = matchMinutes(var249[2].first, var249[2].second)
val var261 = DurationValue(var252, var257, var260, null, nextId(), beginGen, endGen)
var261
}
else -> {
val var262 = getSequenceElems(history, 160, listOf(133,143), beginGen, endGen)
val var263 = history[var262[0].second].findByBeginGenOpt(29, 1, var262[0].first)
val var264 = history[var262[0].second].findByBeginGenOpt(134, 1, var262[0].first)
check(hasSingleTrue(var263 != null, var264 != null))
val var265 = when {
var263 != null -> null
else -> {
val var266 = getSequenceElems(history, 135, listOf(136,19), var262[0].first, var262[0].second)
val var267 = matchDays(var266[0].first, var266[0].second)
var267
}
}
val var268 = matchHours(var262[1].first, var262[1].second)
val var269 = DurationValue(var265, var268, null, null, nextId(), beginGen, endGen)
var269
}
}
return var228
}

fun matchHours(beginGen: Int, endGen: Int): String {
val var270 = getSequenceElems(history, 144, listOf(138,19,129), beginGen, endGen)
val var271 = matchNumber(var270[0].first, var270[0].second)
return var271
}

fun matchNumber(beginGen: Int, endGen: Int): String {
val var272 = history[endGen].findByBeginGenOpt(73, 1, beginGen)
val var273 = history[endGen].findByBeginGenOpt(88, 2, beginGen)
check(hasSingleTrue(var272 != null, var273 != null))
val var274 = when {
var272 != null -> "0"
else -> {
val var275 = getSequenceElems(history, 88, listOf(89,90), beginGen, endGen)
val var276 = unrollRepeat0(history, 90, 92, 7, 91, var275[1].first, var275[1].second).map { k ->
source[k.first]
}
source[var275[0].first].toString() + var276.joinToString("") { it.toString() }
}
}
return var274
}

fun matchSingleItem(beginGen: Int, endGen: Int): SingleItem {
val var277 = getSequenceElems(history, 38, listOf(39,19,113), beginGen, endGen)
val var278 = matchItemPath(var277[0].first, var277[0].second)
val var279 = matchItemValue(var277[2].first, var277[2].second)
val var280 = SingleItem(var278, var279, nextId(), beginGen, endGen)
return var280
}

fun matchDays(beginGen: Int, endGen: Int): String {
val var281 = getSequenceElems(history, 137, listOf(138,19,139), beginGen, endGen)
val var282 = matchNumber(var281[0].first, var281[0].second)
return var282
}

fun matchDigit2(beginGen: Int, endGen: Int): String {
val var283 = getSequenceElems(history, 169, listOf(92,92), beginGen, endGen)
return source[var283[0].first].toString() + source[var283[1].first].toString()
}

fun matchDate(beginGen: Int, endGen: Int): Date {
val var284 = getSequenceElems(history, 164, listOf(165,167,168,167,168), beginGen, endGen)
val var285 = matchDigit4(var284[0].first, var284[0].second)
val var286 = matchDigit2(var284[2].first, var284[2].second)
val var287 = matchDigit2(var284[4].first, var284[4].second)
val var288 = Date(var285, var286, var287, nextId(), beginGen, endGen)
return var288
}

fun matchDigit4(beginGen: Int, endGen: Int): String {
val var289 = getSequenceElems(history, 166, listOf(92,92,92,92), beginGen, endGen)
return source[var289[0].first].toString() + source[var289[1].first].toString() + source[var289[2].first].toString() + source[var289[3].first].toString()
}

fun matchTime(beginGen: Int, endGen: Int): Time {
val var290 = history[endGen].findByBeginGenOpt(175, 6, beginGen)
val var291 = history[endGen].findByBeginGenOpt(176, 4, beginGen)
val var292 = history[endGen].findByBeginGenOpt(177, 3, beginGen)
val var293 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
check(hasSingleTrue(var290 != null, var291 != null, var292 != null, var293 != null))
val var294 = when {
var290 != null -> {
val var295 = getSequenceElems(history, 175, listOf(168,116,168,116,168,153), beginGen, endGen)
val var296 = matchDigit2(var295[0].first, var295[0].second)
val var297 = matchDigit2(var295[2].first, var295[2].second)
val var298 = matchDigit2(var295[4].first, var295[4].second)
val var299 = history[var295[5].second].findByBeginGenOpt(29, 1, var295[5].first)
val var300 = history[var295[5].second].findByBeginGenOpt(154, 1, var295[5].first)
check(hasSingleTrue(var299 != null, var300 != null))
val var301 = when {
var299 != null -> null
else -> {
val var302 = matchSecondFrac(var295[5].first, var295[5].second)
var302
}
}
val var303 = Time(var296, var297, var298, var301, nextId(), beginGen, endGen)
var303
}
var291 != null -> {
val var304 = getSequenceElems(history, 176, listOf(168,168,168,153), beginGen, endGen)
val var305 = matchDigit2(var304[0].first, var304[0].second)
val var306 = matchDigit2(var304[1].first, var304[1].second)
val var307 = matchDigit2(var304[2].first, var304[2].second)
val var308 = history[var304[3].second].findByBeginGenOpt(29, 1, var304[3].first)
val var309 = history[var304[3].second].findByBeginGenOpt(154, 1, var304[3].first)
check(hasSingleTrue(var308 != null, var309 != null))
val var310 = when {
var308 != null -> null
else -> {
val var311 = matchSecondFrac(var304[3].first, var304[3].second)
var311
}
}
val var312 = Time(var305, var306, var307, var310, nextId(), beginGen, endGen)
var312
}
var292 != null -> {
val var313 = getSequenceElems(history, 177, listOf(168,116,168), beginGen, endGen)
val var314 = matchDigit2(var313[0].first, var313[0].second)
val var315 = matchDigit2(var313[2].first, var313[2].second)
val var316 = Time(var314, var315, null, null, nextId(), beginGen, endGen)
var316
}
else -> {
val var317 = getSequenceElems(history, 178, listOf(168,168), beginGen, endGen)
val var318 = matchDigit2(var317[0].first, var317[0].second)
val var319 = matchDigit2(var317[1].first, var317[1].second)
val var320 = Time(var318, var319, null, null, nextId(), beginGen, endGen)
var320
}
}
return var294
}

fun matchObjectOrMapValue(beginGen: Int, endGen: Int): ObjectOrMapValue {
val var321 = history[endGen].findByBeginGenOpt(199, 3, beginGen)
val var322 = history[endGen].findByBeginGenOpt(202, 6, beginGen)
check(hasSingleTrue(var321 != null, var322 != null))
val var323 = when {
var321 != null -> {
val var324 = ObjectOrMapValue(listOf(), nextId(), beginGen, endGen)
var324
}
else -> {
val var325 = getSequenceElems(history, 202, listOf(200,4,203,205,4,201), beginGen, endGen)
val var326 = matchKeyValuePair(var325[2].first, var325[2].second)
val var327 = unrollRepeat0(history, 205, 207, 7, 206, var325[3].first, var325[3].second).map { k ->
val var328 = getSequenceElems(history, 208, listOf(195,4,203), k.first, k.second)
val var329 = matchKeyValuePair(var328[2].first, var328[2].second)
var329
}
val var330 = ObjectOrMapValue(listOf(var326) + var327, nextId(), beginGen, endGen)
var330
}
}
return var323
}

fun matchKeyValuePair(beginGen: Int, endGen: Int): KeyValuePair {
val var331 = getSequenceElems(history, 204, listOf(39,4,116,4,118), beginGen, endGen)
val var332 = matchItemPath(var331[0].first, var331[0].second)
val var333 = matchValue(var331[4].first, var331[4].second)
val var334 = KeyValuePair(var332, var333, nextId(), beginGen, endGen)
return var334
}

fun matchMinutes(beginGen: Int, endGen: Int): String {
val var335 = getSequenceElems(history, 149, listOf(138,19,150), beginGen, endGen)
val var336 = matchNumber(var335[0].first, var335[0].second)
return var336
}

fun matchSeconds(beginGen: Int, endGen: Int): Seconds {
val var337 = getSequenceElems(history, 152, listOf(138,153,19,158), beginGen, endGen)
val var338 = matchNumber(var337[0].first, var337[0].second)
val var339 = history[var337[1].second].findByBeginGenOpt(29, 1, var337[1].first)
val var340 = history[var337[1].second].findByBeginGenOpt(154, 1, var337[1].first)
check(hasSingleTrue(var339 != null, var340 != null))
val var341 = when {
var339 != null -> null
else -> {
val var342 = matchSecondFrac(var337[1].first, var337[1].second)
var342
}
}
val var343 = Seconds(var338, var341, nextId(), beginGen, endGen)
return var343
}

fun matchSecondFrac(beginGen: Int, endGen: Int): String {
val var344 = getSequenceElems(history, 155, listOf(96,156), beginGen, endGen)
val var345 = unrollRepeat1(history, 156, 92, 92, 157, var344[1].first, var344[1].second).map { k ->
source[k.first]
}
return var345.joinToString("") { it.toString() }
}

}
