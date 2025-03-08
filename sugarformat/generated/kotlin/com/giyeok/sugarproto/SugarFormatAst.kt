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
    fun toShortString(): String
  }

data class HexCode(
  val code: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode {
  override fun toShortString(): String = "HexCode(code=${code})"
}

data class Header(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemValue, AstNode {
  override fun toShortString(): String = "Header()"
}

sealed interface Timezone: AstNode

data class ListFieldItem(
  val innerIndent: String,
  val item: SingleItem,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ListItem, AstNode {
  override fun toShortString(): String = "ListFieldItem(innerIndent=${innerIndent}, item=${item})"
}

data class NameKey(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KeyValue, AstNode {
  override fun toShortString(): String = "NameKey(name=${name})"
}

data class OctValue(
  val sgn: Char?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): NumberValue, AstNode {
  override fun toShortString(): String = "OctValue(sgn=${sgn}, value=${value})"
}

data class NameValue(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ScalarValue, AstNode {
  override fun toShortString(): String = "NameValue(value=${value})"
}

data class HexValue(
  val sgn: Char?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): NumberValue, AstNode {
  override fun toShortString(): String = "HexValue(sgn=${sgn}, value=${value})"
}

data class ListValueItem(
  val value: Value,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ListItem, AstNode {
  override fun toShortString(): String = "ListValueItem(value=${value})"
}

sealed interface NumberValue: KeyValue, ScalarValue, AstNode

data class Date(
  val year: String,
  val month: String,
  val day: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "Date(year=${year}, month=${month}, day=${day})"
}

sealed interface KeyValue: AstNode

data class Seconds(
  val integral: String,
  val frac: String?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "Seconds(integral=${integral}, frac=${frac})"
}

sealed interface ListItem: Item, AstNode

data class StringValue(
  val type: StringTypeAnnot?,
  val fracs: List<StringFrac>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ScalarValue, AstNode {
  override fun toShortString(): String = "StringValue(type=${type}, fracs=${fracs})"
}

sealed interface ScalarValue: Value, AstNode

data class StringFrac(
  val elems: List<StringElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KeyValue, AstNode {
  override fun toShortString(): String = "StringFrac(elems=${elems})"
}

data class TimeOffset(
  val hour: String,
  val minute: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Timezone, AstNode {
  override fun toShortString(): String = "TimeOffset(hour=${hour}, minute=${minute})"
}

data class IndentItem(
  val indent: String,
  val item: Item,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "IndentItem(indent=${indent}, item=${item})"
}

data class ItemPath(
  val path: List<KeyValue>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "ItemPath(path=${path})"
}

data class OctCode(
  val code: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode {
  override fun toShortString(): String = "OctCode(code=${code})"
}

data class RepeatedValue(
  val elems: List<Value>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode {
  override fun toShortString(): String = "RepeatedValue(elems=${elems})"
}

data class Unicode(
  val code: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode {
  override fun toShortString(): String = "Unicode(code=${code})"
}

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
): NumberValue, AstNode {
  override fun toShortString(): String = "DecValue(sgn=${sgn}, integral=${integral}, frac=${frac}, exponent=${exponent})"
}

data class DurationValue(
  val days: String?,
  val hours: String?,
  val minutes: String?,
  val seconds: Seconds?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ScalarValue, AstNode {
  override fun toShortString(): String = "DurationValue(days=${days}, hours=${hours}, minutes=${minutes}, seconds=${seconds})"
}

data class SingleItem(
  val key: ItemPath,
  val value: ItemValue,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Item, AstNode {
  override fun toShortString(): String = "SingleItem(key=${key}, value=${value})"
}

data class KeyValuePair(
  val key: ItemPath,
  val value: Value,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "KeyValuePair(key=${key}, value=${value})"
}

data class PlainText(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode {
  override fun toShortString(): String = "PlainText(value=${value})"
}

data class NamedTimezone(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Timezone, AstNode {
  override fun toShortString(): String = "NamedTimezone(name=${name})"
}

data class TimestampValue(
  val date: Date,
  val time: Time?,
  val timezone: Timezone?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ScalarValue, AstNode {
  override fun toShortString(): String = "TimestampValue(date=${date}, time=${time}, timezone=${timezone})"
}

data class EscapeCode(
  val code: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode {
  override fun toShortString(): String = "EscapeCode(code=${code})"
}

sealed interface Item: AstNode

sealed interface ItemValue: AstNode

data class Exponent(
  val sgn: Char?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "Exponent(sgn=${sgn}, value=${value})"
}

data class Time(
  val hour: String,
  val minute: String,
  val second: String?,
  val secondFrac: String?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "Time(hour=${hour}, minute=${minute}, second=${second}, secondFrac=${secondFrac})"
}

data class ObjectOrMapValue(
  val pairs: List<KeyValuePair>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode {
  override fun toShortString(): String = "ObjectOrMapValue(pairs=${pairs})"
}
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
val var20 = getSequenceElems(history, 213, listOf(167,19,37), beginGen, endGen)
val var21 = matchWS1(var20[1].first, var20[1].second)
val var22 = matchSingleItem(var20[2].first, var20[2].second)
val var23 = ListFieldItem(source[var20[0].first].toString() + var21.joinToString("") { it.toString() }, var22, nextId(), beginGen, endGen)
return var23
}

fun matchListValueItem(beginGen: Int, endGen: Int): ListValueItem {
val var24 = getSequenceElems(history, 211, listOf(167,19,118), beginGen, endGen)
val var25 = matchValue(var24[2].first, var24[2].second)
val var26 = ListValueItem(var25, nextId(), beginGen, endGen)
return var26
}

fun matchValue(beginGen: Int, endGen: Int): Value {
val var27 = history[endGen].findByBeginGenOpt(119, 1, beginGen)
val var28 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var29 = history[endGen].findByBeginGenOpt(198, 1, beginGen)
check(hasSingleTrue(var27 != null, var28 != null, var29 != null))
val var30 = when {
var27 != null -> {
val var31 = matchScalarValue(beginGen, endGen)
var31
}
var28 != null -> {
val var32 = matchRepeatedValue(beginGen, endGen)
var32
}
else -> {
val var33 = matchObjectOrMapValue(beginGen, endGen)
var33
}
}
return var30
}

fun matchScalarValue(beginGen: Int, endGen: Int): ScalarValue {
val var34 = history[endGen].findByBeginGenOpt(82, 1, beginGen)
val var35 = history[endGen].findByBeginGenOpt(120, 1, beginGen)
val var36 = history[endGen].findByBeginGenOpt(130, 1, beginGen)
val var37 = history[endGen].findByBeginGenOpt(131, 1, beginGen)
val var38 = history[endGen].findByBeginGenOpt(161, 1, beginGen)
check(hasSingleTrue(var34 != null, var35 != null, var36 != null, var37 != null, var38 != null))
val var39 = when {
var34 != null -> {
val var40 = matchNumberValue(beginGen, endGen)
var40
}
var35 != null -> {
val var41 = matchStringValue(beginGen, endGen)
var41
}
var36 != null -> {
val var42 = matchNameValue(beginGen, endGen)
var42
}
var37 != null -> {
val var43 = matchDurationValue(beginGen, endGen)
var43
}
else -> {
val var44 = matchTimestampValue(beginGen, endGen)
var44
}
}
return var39
}

fun matchStringValue(beginGen: Int, endGen: Int): StringValue {
val var45 = history[endGen].findByBeginGenOpt(121, 2, beginGen)
val var46 = history[endGen].findByBeginGenOpt(126, 2, beginGen)
check(hasSingleTrue(var45 != null, var46 != null))
val var47 = when {
var45 != null -> {
val var48 = getSequenceElems(history, 121, listOf(48,122), beginGen, endGen)
val var49 = matchStringFrac(var48[0].first, var48[0].second)
val var50 = unrollRepeat0(history, 122, 124, 7, 123, var48[1].first, var48[1].second).map { k ->
val var51 = getSequenceElems(history, 125, listOf(4,48), k.first, k.second)
val var52 = matchStringFrac(var51[1].first, var51[1].second)
var52
}
val var53 = StringValue(null, listOf(var49) + var50, nextId(), beginGen, endGen)
var53
}
else -> {
val var54 = getSequenceElems(history, 126, listOf(127,48), beginGen, endGen)
val var55 = matchStringTypeAnnot(var54[0].first, var54[0].second)
val var56 = matchStringFrac(var54[1].first, var54[1].second)
val var57 = StringValue(var55, listOf(var56), nextId(), beginGen, endGen)
var57
}
}
return var47
}

fun matchStringFrac(beginGen: Int, endGen: Int): StringFrac {
val var58 = getSequenceElems(history, 49, listOf(50,51,50), beginGen, endGen)
val var59 = unrollRepeat0(history, 51, 53, 7, 52, var58[1].first, var58[1].second).map { k ->
val var60 = matchStringElem(k.first, k.second)
var60
}
val var61 = StringFrac(var59, nextId(), beginGen, endGen)
return var61
}

fun matchStringElem(beginGen: Int, endGen: Int): StringElem {
val var62 = history[endGen].findByBeginGenOpt(54, 2, beginGen)
val var63 = history[endGen].findByBeginGenOpt(57, 3, beginGen)
val var64 = history[endGen].findByBeginGenOpt(64, 4, beginGen)
val var65 = history[endGen].findByBeginGenOpt(69, 6, beginGen)
val var66 = history[endGen].findByBeginGenOpt(71, 10, beginGen)
val var67 = history[endGen].findByBeginGenOpt(74, 10, beginGen)
val var68 = history[endGen].findByBeginGenOpt(76, 1, beginGen)
check(hasSingleTrue(var62 != null, var63 != null, var64 != null, var65 != null, var66 != null, var67 != null, var68 != null))
val var69 = when {
var62 != null -> {
val var70 = getSequenceElems(history, 54, listOf(55,56), beginGen, endGen)
val var71 = EscapeCode(source[var70[1].first], nextId(), beginGen, endGen)
var71
}
var63 != null -> {
val var72 = getSequenceElems(history, 57, listOf(55,58,60), beginGen, endGen)
val var73 = matchOct(var72[1].first, var72[1].second)
val var74 = history[var72[2].second].findByBeginGenOpt(29, 1, var72[2].first)
val var75 = history[var72[2].second].findByBeginGenOpt(61, 1, var72[2].first)
check(hasSingleTrue(var74 != null, var75 != null))
val var76 = when {
var74 != null -> null
else -> {
val var77 = getSequenceElems(history, 62, listOf(58,63), var72[2].first, var72[2].second)
val var78 = history[var77[1].second].findByBeginGenOpt(29, 1, var77[1].first)
val var79 = history[var77[1].second].findByBeginGenOpt(58, 1, var77[1].first)
check(hasSingleTrue(var78 != null, var79 != null))
val var80 = when {
var78 != null -> null
else -> {
val var81 = matchOct(var77[1].first, var77[1].second)
var81
}
}
var80
}
}
val var82 = OctCode(var73.toString() + (var76?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var82
}
var64 != null -> {
val var83 = getSequenceElems(history, 64, listOf(55,65,66,68), beginGen, endGen)
val var84 = matchHex(var83[2].first, var83[2].second)
val var85 = history[var83[3].second].findByBeginGenOpt(29, 1, var83[3].first)
val var86 = history[var83[3].second].findByBeginGenOpt(66, 1, var83[3].first)
check(hasSingleTrue(var85 != null, var86 != null))
val var87 = when {
var85 != null -> null
else -> {
val var88 = matchHex(var83[3].first, var83[3].second)
var88
}
}
val var89 = HexCode(var84.toString() + (var87?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var89
}
var65 != null -> {
val var90 = getSequenceElems(history, 69, listOf(55,70,66,66,66,66), beginGen, endGen)
val var91 = matchHex(var90[2].first, var90[2].second)
val var92 = matchHex(var90[3].first, var90[3].second)
val var93 = matchHex(var90[4].first, var90[4].second)
val var94 = matchHex(var90[5].first, var90[5].second)
val var95 = Unicode(var91.toString() + var92.toString() + var93.toString() + var94.toString(), nextId(), beginGen, endGen)
var95
}
var66 != null -> {
val var96 = getSequenceElems(history, 71, listOf(55,72,73,73,73,66,66,66,66,66), beginGen, endGen)
val var97 = matchHex(var96[5].first, var96[5].second)
val var98 = matchHex(var96[6].first, var96[6].second)
val var99 = matchHex(var96[7].first, var96[7].second)
val var100 = matchHex(var96[8].first, var96[8].second)
val var101 = matchHex(var96[9].first, var96[9].second)
val var102 = Unicode(source[var96[2].first].toString() + source[var96[3].first].toString() + source[var96[4].first].toString() + var97.toString() + var98.toString() + var99.toString() + var100.toString() + var101.toString(), nextId(), beginGen, endGen)
var102
}
var67 != null -> {
val var103 = getSequenceElems(history, 74, listOf(55,72,73,73,75,73,66,66,66,66), beginGen, endGen)
val var104 = matchHex(var103[6].first, var103[6].second)
val var105 = matchHex(var103[7].first, var103[7].second)
val var106 = matchHex(var103[8].first, var103[8].second)
val var107 = matchHex(var103[9].first, var103[9].second)
val var108 = Unicode(source[var103[2].first].toString() + source[var103[3].first].toString() + source[var103[4].first].toString() + source[var103[5].first].toString() + var104.toString() + var105.toString() + var106.toString() + var107.toString(), nextId(), beginGen, endGen)
var108
}
else -> {
val var109 = unrollRepeat1(history, 77, 78, 78, 81, beginGen, endGen).map { k ->
source[k.first]
}
val var110 = PlainText(var109.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
var110
}
}
return var69
}

fun matchHex(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchOct(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchNameValue(beginGen: Int, endGen: Int): NameValue {
val var111 = matchName(beginGen, endGen)
val var112 = NameValue(var111, nextId(), beginGen, endGen)
return var112
}

fun matchName(beginGen: Int, endGen: Int): String {
val var113 = getSequenceElems(history, 43, listOf(44,45), beginGen, endGen)
val var114 = unrollRepeat0(history, 45, 47, 7, 46, var113[1].first, var113[1].second).map { k ->
source[k.first]
}
return source[var113[0].first].toString() + var114.joinToString("") { it.toString() }
}

fun matchNumberValue(beginGen: Int, endGen: Int): NumberValue {
val var115 = history[endGen].findByBeginGenOpt(83, 1, beginGen)
val var116 = history[endGen].findByBeginGenOpt(101, 1, beginGen)
val var117 = history[endGen].findByBeginGenOpt(105, 1, beginGen)
check(hasSingleTrue(var115 != null, var116 != null, var117 != null))
val var118 = when {
var115 != null -> {
val var119 = matchDecValue(beginGen, endGen)
var119
}
var116 != null -> {
val var120 = matchOctValue(beginGen, endGen)
var120
}
else -> {
val var121 = matchHexValue(beginGen, endGen)
var121
}
}
return var118
}

fun matchOctValue(beginGen: Int, endGen: Int): OctValue {
val var122 = getSequenceElems(history, 102, listOf(85,73,103), beginGen, endGen)
val var123 = history[var122[0].second].findByBeginGenOpt(29, 1, var122[0].first)
val var124 = history[var122[0].second].findByBeginGenOpt(86, 1, var122[0].first)
check(hasSingleTrue(var123 != null, var124 != null))
val var125 = when {
var123 != null -> null
else -> source[var122[0].first]
}
val var126 = unrollRepeat1(history, 103, 58, 58, 104, var122[2].first, var122[2].second).map { k ->
val var127 = matchOct(k.first, k.second)
var127
}
val var128 = OctValue(var125, var126.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var128
}

fun matchDecValue(beginGen: Int, endGen: Int): DecValue {
val var129 = getSequenceElems(history, 84, listOf(85,87,93,97), beginGen, endGen)
val var130 = history[var129[0].second].findByBeginGenOpt(29, 1, var129[0].first)
val var131 = history[var129[0].second].findByBeginGenOpt(86, 1, var129[0].first)
check(hasSingleTrue(var130 != null, var131 != null))
val var132 = when {
var130 != null -> null
else -> source[var129[0].first]
}
val var133 = matchDec(var129[1].first, var129[1].second)
val var134 = history[var129[2].second].findByBeginGenOpt(29, 1, var129[2].first)
val var135 = history[var129[2].second].findByBeginGenOpt(94, 1, var129[2].first)
check(hasSingleTrue(var134 != null, var135 != null))
val var136 = when {
var134 != null -> null
else -> {
val var137 = getSequenceElems(history, 95, listOf(96,87), var129[2].first, var129[2].second)
val var138 = matchDec(var137[1].first, var137[1].second)
var138
}
}
val var139 = history[var129[3].second].findByBeginGenOpt(29, 1, var129[3].first)
val var140 = history[var129[3].second].findByBeginGenOpt(98, 1, var129[3].first)
check(hasSingleTrue(var139 != null, var140 != null))
val var141 = when {
var139 != null -> null
else -> {
val var142 = getSequenceElems(history, 99, listOf(100,85,87), var129[3].first, var129[3].second)
val var143 = history[var142[1].second].findByBeginGenOpt(29, 1, var142[1].first)
val var144 = history[var142[1].second].findByBeginGenOpt(86, 1, var142[1].first)
check(hasSingleTrue(var143 != null, var144 != null))
val var145 = when {
var143 != null -> null
else -> source[var142[1].first]
}
val var146 = matchDec(var142[2].first, var142[2].second)
val var147 = Exponent(var145, var146, nextId(), var129[3].first, var129[3].second)
var147
}
}
val var148 = DecValue(var132, var133, var136, var141, nextId(), beginGen, endGen)
return var148
}

fun matchDec(beginGen: Int, endGen: Int): String {
val var149 = history[endGen].findByBeginGenOpt(73, 1, beginGen)
val var150 = history[endGen].findByBeginGenOpt(88, 2, beginGen)
check(hasSingleTrue(var149 != null, var150 != null))
val var151 = when {
var149 != null -> "0"
else -> {
val var152 = getSequenceElems(history, 88, listOf(89,90), beginGen, endGen)
val var153 = unrollRepeat0(history, 90, 92, 7, 91, var152[1].first, var152[1].second).map { k ->
source[k.first]
}
source[var152[0].first].toString() + var153.joinToString("") { it.toString() }
}
}
return var151
}

fun matchHexValue(beginGen: Int, endGen: Int): HexValue {
val var154 = getSequenceElems(history, 106, listOf(85,73,65,107), beginGen, endGen)
val var155 = history[var154[0].second].findByBeginGenOpt(29, 1, var154[0].first)
val var156 = history[var154[0].second].findByBeginGenOpt(86, 1, var154[0].first)
check(hasSingleTrue(var155 != null, var156 != null))
val var157 = when {
var155 != null -> null
else -> source[var154[0].first]
}
val var158 = unrollRepeat1(history, 107, 66, 66, 108, var154[3].first, var154[3].second).map { k ->
val var159 = matchHex(k.first, k.second)
var159
}
val var160 = HexValue(var157, var158.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var160
}

fun matchTimestampValue(beginGen: Int, endGen: Int): TimestampValue {
val var161 = getSequenceElems(history, 162, listOf(163,170,179), beginGen, endGen)
val var162 = matchDate(var161[0].first, var161[0].second)
val var163 = history[var161[1].second].findByBeginGenOpt(29, 1, var161[1].first)
val var164 = history[var161[1].second].findByBeginGenOpt(171, 1, var161[1].first)
check(hasSingleTrue(var163 != null, var164 != null))
val var165 = when {
var163 != null -> null
else -> {
val var166 = getSequenceElems(history, 172, listOf(173,174), var161[1].first, var161[1].second)
val var167 = matchTime(var166[1].first, var166[1].second)
var167
}
}
val var168 = history[var161[2].second].findByBeginGenOpt(29, 1, var161[2].first)
val var169 = history[var161[2].second].findByBeginGenOpt(180, 1, var161[2].first)
check(hasSingleTrue(var168 != null, var169 != null))
val var170 = when {
var168 != null -> null
else -> {
val var171 = matchTimezone(var161[2].first, var161[2].second)
var171
}
}
val var172 = TimestampValue(var162, var165, var170, nextId(), beginGen, endGen)
return var172
}

fun matchTimezone(beginGen: Int, endGen: Int): Timezone {
val var173 = history[endGen].findByBeginGenOpt(181, 1, beginGen)
val var174 = history[endGen].findByBeginGenOpt(182, 2, beginGen)
val var175 = history[endGen].findByBeginGenOpt(183, 4, beginGen)
val var176 = history[endGen].findByBeginGenOpt(184, 3, beginGen)
check(hasSingleTrue(var173 != null, var174 != null, var175 != null, var176 != null))
val var177 = when {
var173 != null -> {
val var178 = NamedTimezone("UTC", nextId(), beginGen, endGen)
var178
}
var174 != null -> {
val var179 = getSequenceElems(history, 182, listOf(86,168), beginGen, endGen)
val var180 = matchDigit2(var179[1].first, var179[1].second)
val var181 = TimeOffset(var180, "00", nextId(), beginGen, endGen)
var181
}
var175 != null -> {
val var182 = getSequenceElems(history, 183, listOf(86,168,116,168), beginGen, endGen)
val var183 = matchDigit2(var182[1].first, var182[1].second)
val var184 = matchDigit2(var182[3].first, var182[3].second)
val var185 = TimeOffset(var183, var184, nextId(), beginGen, endGen)
var185
}
else -> {
val var186 = getSequenceElems(history, 184, listOf(86,168,168), beginGen, endGen)
val var187 = matchDigit2(var186[1].first, var186[1].second)
val var188 = matchDigit2(var186[2].first, var186[2].second)
val var189 = TimeOffset(var187, var188, nextId(), beginGen, endGen)
var189
}
}
return var177
}

fun matchStringTypeAnnot(beginGen: Int, endGen: Int): StringTypeAnnot {
val var190 = history[endGen].findByBeginGenOpt(128, 1, beginGen)
val var191 = history[endGen].findByBeginGenOpt(129, 1, beginGen)
check(hasSingleTrue(var190 != null, var191 != null))
val var192 = when {
var190 != null -> StringTypeAnnot.Base64
else -> StringTypeAnnot.Hex
}
return var192
}

fun matchRepeatedValue(beginGen: Int, endGen: Int): RepeatedValue {
val var193 = history[endGen].findByBeginGenOpt(186, 3, beginGen)
val var194 = history[endGen].findByBeginGenOpt(189, 7, beginGen)
check(hasSingleTrue(var193 != null, var194 != null))
val var195 = when {
var193 != null -> {
val var196 = RepeatedValue(listOf(), nextId(), beginGen, endGen)
var196
}
else -> {
val var197 = getSequenceElems(history, 189, listOf(187,4,118,190,195,4,188), beginGen, endGen)
val var198 = matchValue(var197[2].first, var197[2].second)
val var199 = unrollRepeat0(history, 190, 192, 7, 191, var197[3].first, var197[3].second).map { k ->
val var200 = getSequenceElems(history, 193, listOf(4,194,4,118), k.first, k.second)
val var201 = matchValue(var200[3].first, var200[3].second)
var201
}
val var202 = RepeatedValue(listOf(var198) + var199, nextId(), beginGen, endGen)
var202
}
}
return var195
}

fun matchDurationValue(beginGen: Int, endGen: Int): DurationValue {
val var203 = history[endGen].findByBeginGenOpt(132, 4, beginGen)
val var204 = history[endGen].findByBeginGenOpt(136, 1, beginGen)
val var205 = history[endGen].findByBeginGenOpt(159, 3, beginGen)
val var206 = history[endGen].findByBeginGenOpt(160, 2, beginGen)
check(hasSingleTrue(var203 != null, var204 != null, var205 != null, var206 != null))
val var207 = when {
var203 != null -> {
val var208 = getSequenceElems(history, 132, listOf(133,140,145,151), beginGen, endGen)
val var209 = history[var208[0].second].findByBeginGenOpt(29, 1, var208[0].first)
val var210 = history[var208[0].second].findByBeginGenOpt(134, 1, var208[0].first)
check(hasSingleTrue(var209 != null, var210 != null))
val var211 = when {
var209 != null -> null
else -> {
val var212 = getSequenceElems(history, 135, listOf(136,19), var208[0].first, var208[0].second)
val var213 = matchDays(var212[0].first, var212[0].second)
var213
}
}
val var214 = history[var208[1].second].findByBeginGenOpt(29, 1, var208[1].first)
val var215 = history[var208[1].second].findByBeginGenOpt(141, 1, var208[1].first)
check(hasSingleTrue(var214 != null, var215 != null))
val var216 = when {
var214 != null -> null
else -> {
val var217 = getSequenceElems(history, 142, listOf(143,19), var208[1].first, var208[1].second)
val var218 = matchHours(var217[0].first, var217[0].second)
var218
}
}
val var219 = history[var208[2].second].findByBeginGenOpt(29, 1, var208[2].first)
val var220 = history[var208[2].second].findByBeginGenOpt(146, 1, var208[2].first)
check(hasSingleTrue(var219 != null, var220 != null))
val var221 = when {
var219 != null -> null
else -> {
val var222 = getSequenceElems(history, 147, listOf(148,19), var208[2].first, var208[2].second)
val var223 = matchMinutes(var222[0].first, var222[0].second)
var223
}
}
val var224 = matchSeconds(var208[3].first, var208[3].second)
val var225 = DurationValue(var211, var216, var221, var224, nextId(), beginGen, endGen)
var225
}
var204 != null -> {
val var226 = matchDays(beginGen, endGen)
val var227 = DurationValue(var226, null, null, null, nextId(), beginGen, endGen)
var227
}
var205 != null -> {
val var228 = getSequenceElems(history, 159, listOf(133,140,148), beginGen, endGen)
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
val var239 = matchMinutes(var228[2].first, var228[2].second)
val var240 = DurationValue(var231, var236, var239, null, nextId(), beginGen, endGen)
var240
}
else -> {
val var241 = getSequenceElems(history, 160, listOf(133,143), beginGen, endGen)
val var242 = history[var241[0].second].findByBeginGenOpt(29, 1, var241[0].first)
val var243 = history[var241[0].second].findByBeginGenOpt(134, 1, var241[0].first)
check(hasSingleTrue(var242 != null, var243 != null))
val var244 = when {
var242 != null -> null
else -> {
val var245 = getSequenceElems(history, 135, listOf(136,19), var241[0].first, var241[0].second)
val var246 = matchDays(var245[0].first, var245[0].second)
var246
}
}
val var247 = matchHours(var241[1].first, var241[1].second)
val var248 = DurationValue(var244, var247, null, null, nextId(), beginGen, endGen)
var248
}
}
return var207
}

fun matchHours(beginGen: Int, endGen: Int): String {
val var249 = getSequenceElems(history, 144, listOf(138,19,129), beginGen, endGen)
val var250 = matchNumber(var249[0].first, var249[0].second)
return var250
}

fun matchNumber(beginGen: Int, endGen: Int): String {
val var251 = history[endGen].findByBeginGenOpt(73, 1, beginGen)
val var252 = history[endGen].findByBeginGenOpt(88, 2, beginGen)
check(hasSingleTrue(var251 != null, var252 != null))
val var253 = when {
var251 != null -> "0"
else -> {
val var254 = getSequenceElems(history, 88, listOf(89,90), beginGen, endGen)
val var255 = unrollRepeat0(history, 90, 92, 7, 91, var254[1].first, var254[1].second).map { k ->
source[k.first]
}
source[var254[0].first].toString() + var255.joinToString("") { it.toString() }
}
}
return var253
}

fun matchSingleItem(beginGen: Int, endGen: Int): SingleItem {
val var256 = getSequenceElems(history, 38, listOf(39,19,113), beginGen, endGen)
val var257 = matchItemPath(var256[0].first, var256[0].second)
val var258 = matchItemValue(var256[2].first, var256[2].second)
val var259 = SingleItem(var257, var258, nextId(), beginGen, endGen)
return var259
}

fun matchItemPath(beginGen: Int, endGen: Int): ItemPath {
val var260 = getSequenceElems(history, 40, listOf(41,109), beginGen, endGen)
val var261 = matchKeyValue(var260[0].first, var260[0].second)
val var262 = unrollRepeat0(history, 109, 111, 7, 110, var260[1].first, var260[1].second).map { k ->
val var263 = getSequenceElems(history, 112, listOf(19,96,19,41), k.first, k.second)
val var264 = matchKeyValue(var263[3].first, var263[3].second)
var264
}
val var265 = ItemPath(listOf(var261) + var262, nextId(), beginGen, endGen)
return var265
}

fun matchDays(beginGen: Int, endGen: Int): String {
val var266 = getSequenceElems(history, 137, listOf(138,19,139), beginGen, endGen)
val var267 = matchNumber(var266[0].first, var266[0].second)
return var267
}

fun matchItemValue(beginGen: Int, endGen: Int): ItemValue {
val var268 = history[endGen].findByBeginGenOpt(114, 1, beginGen)
val var269 = history[endGen].findByBeginGenOpt(117, 3, beginGen)
check(hasSingleTrue(var268 != null, var269 != null))
val var270 = when {
var268 != null -> {
val var271 = Header(nextId(), beginGen, endGen)
var271
}
else -> {
val var272 = getSequenceElems(history, 117, listOf(116,19,118), beginGen, endGen)
val var273 = matchValue(var272[2].first, var272[2].second)
var273
}
}
return var270
}

fun matchKeyValue(beginGen: Int, endGen: Int): KeyValue {
val var274 = history[endGen].findByBeginGenOpt(42, 1, beginGen)
val var275 = history[endGen].findByBeginGenOpt(48, 1, beginGen)
val var276 = history[endGen].findByBeginGenOpt(82, 1, beginGen)
check(hasSingleTrue(var274 != null, var275 != null, var276 != null))
val var277 = when {
var274 != null -> {
val var278 = matchName(beginGen, endGen)
val var279 = NameKey(var278, nextId(), beginGen, endGen)
var279
}
var275 != null -> {
val var280 = matchStringFrac(beginGen, endGen)
var280
}
else -> {
val var281 = matchNumberValue(beginGen, endGen)
var281
}
}
return var277
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
