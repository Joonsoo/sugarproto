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

data class ListFieldItem(
  val name: String,
  val value: ItemValue,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ListItem, AstNode

data class StringFrac(
  val elems: List<StringElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KeyValue, AstNode

data class IndentItem(
  val indent: String,
  val item: Item,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class NameKey(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Key, AstNode

data class EnumValue(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode

data class ListValueItem(
  val value: Value,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ListItem, AstNode

data class RepeatedHeader(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemValue, AstNode

data class SingleItem(
  val key: String,
  val value: ItemValue,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Item, AstNode

sealed interface NumberValue: KeyValue, Value, AstNode

data class ObjectHeader(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemValue, AstNode

sealed interface ItemValue: AstNode

sealed interface KeyValue: Key, AstNode

sealed interface Key: AstNode

data class StringValue(
  val type: StringTypeAnnot?,
  val fracs: List<StringFrac>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode

sealed interface ListItem: Item, AstNode

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

data class MapItem(
  val key: KeyValue,
  val value: ItemValue,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Item, AstNode

data class OctValue(
  val sgn: Char?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): NumberValue, AstNode

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
  val exponent: String?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): NumberValue, AstNode

data class ObjectValue(
  val pairs: List<KeyValuePair>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode

data class MapHeader(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemValue, AstNode

data class HexValue(
  val sgn: Char?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): NumberValue, AstNode

data class KeyValuePair(
  val key: Key,
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

data class EscapeCode(
  val code: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode

sealed interface Item: AstNode
enum class StringTypeAnnot { Base64, Duration, Hex, Timestamp }

fun matchStart(): List<IndentItem> {
  val lastGen = source.length
  val kernel = history[lastGen].getSingle(2, 1, 0, lastGen)
  return matchItems(kernel.beginGen, kernel.endGen)
}

fun matchItems(beginGen: Int, endGen: Int): List<IndentItem> {
val var1 = getSequenceElems(history, 3, listOf(4,9), beginGen, endGen)
val var2 = unrollRepeat0(history, 9, 11, 6, 10, var1[1].first, var1[1].second).map { k ->
val var3 = getSequenceElems(history, 12, listOf(13,15,148,158), k.first, k.second)
val var4 = matchIndentItem(var3[0].first, var3[0].second)
var4
}
return var2
}

fun matchIndentItem(beginGen: Int, endGen: Int): IndentItem {
val var5 = getSequenceElems(history, 14, listOf(15,19), beginGen, endGen)
val var6 = matchWS1(var5[0].first, var5[0].second)
val var7 = matchItem(var5[1].first, var5[1].second)
val var8 = IndentItem(var6.joinToString("") { it.toString() }, var7, nextId(), beginGen, endGen)
return var8
}

fun matchWS1(beginGen: Int, endGen: Int): List<Char> {
val var9 = unrollRepeat0(history, 16, 18, 6, 17, beginGen, endGen).map { k ->
source[k.first]
}
return var9
}

fun matchItem(beginGen: Int, endGen: Int): Item {
val var10 = history[endGen].findByBeginGenOpt(20, 1, beginGen)
val var11 = history[endGen].findByBeginGenOpt(140, 1, beginGen)
val var12 = history[endGen].findByBeginGenOpt(146, 1, beginGen)
check(hasSingleTrue(var10 != null, var11 != null, var12 != null))
val var13 = when {
var10 != null -> {
val var14 = matchSingleItem(beginGen, endGen)
var14
}
var11 != null -> {
val var15 = matchListItem(beginGen, endGen)
var15
}
else -> {
val var16 = matchMapItem(beginGen, endGen)
var16
}
}
return var13
}

fun matchListItem(beginGen: Int, endGen: Int): ListItem {
val var17 = history[endGen].findByBeginGenOpt(141, 1, beginGen)
val var18 = history[endGen].findByBeginGenOpt(144, 1, beginGen)
check(hasSingleTrue(var17 != null, var18 != null))
val var19 = when {
var17 != null -> {
val var20 = matchListValueItem(beginGen, endGen)
var20
}
else -> {
val var21 = matchListFieldItem(beginGen, endGen)
var21
}
}
return var19
}

fun matchListFieldItem(beginGen: Int, endGen: Int): ListFieldItem {
val var22 = getSequenceElems(history, 145, listOf(143,15,22,15,28), beginGen, endGen)
val var23 = matchName(var22[2].first, var22[2].second)
val var24 = matchItemValue(var22[4].first, var22[4].second)
val var25 = ListFieldItem(var23, var24, nextId(), beginGen, endGen)
return var25
}

fun matchName(beginGen: Int, endGen: Int): String {
val var26 = getSequenceElems(history, 23, listOf(24,25), beginGen, endGen)
val var27 = unrollRepeat0(history, 25, 27, 6, 26, var26[1].first, var26[1].second).map { k ->
source[k.first]
}
return source[var26[0].first].toString() + var27.joinToString("") { it.toString() }
}

fun matchMapItem(beginGen: Int, endGen: Int): MapItem {
val var28 = getSequenceElems(history, 147, listOf(135,15,28), beginGen, endGen)
val var29 = matchKeyValue(var28[0].first, var28[0].second)
val var30 = matchItemValue(var28[2].first, var28[2].second)
val var31 = MapItem(var29, var30, nextId(), beginGen, endGen)
return var31
}

fun matchListValueItem(beginGen: Int, endGen: Int): ListValueItem {
val var32 = getSequenceElems(history, 142, listOf(143,15,41), beginGen, endGen)
val var33 = matchValue(var32[2].first, var32[2].second)
val var34 = ListValueItem(var33, nextId(), beginGen, endGen)
return var34
}

fun matchValue(beginGen: Int, endGen: Int): Value {
val var35 = history[endGen].findByBeginGenOpt(42, 1, beginGen)
val var36 = history[endGen].findByBeginGenOpt(90, 1, beginGen)
val var37 = history[endGen].findByBeginGenOpt(117, 1, beginGen)
val var38 = history[endGen].findByBeginGenOpt(118, 1, beginGen)
val var39 = history[endGen].findByBeginGenOpt(129, 1, beginGen)
check(hasSingleTrue(var35 != null, var36 != null, var37 != null, var38 != null, var39 != null))
val var40 = when {
var35 != null -> {
val var41 = matchStringValue(beginGen, endGen)
var41
}
var36 != null -> {
val var42 = matchNumberValue(beginGen, endGen)
var42
}
var37 != null -> {
val var43 = matchEnumValue(beginGen, endGen)
var43
}
var38 != null -> {
val var44 = matchRepeatedValue(beginGen, endGen)
var44
}
else -> {
val var45 = matchObjectValue(beginGen, endGen)
var45
}
}
return var40
}

fun matchStringValue(beginGen: Int, endGen: Int): StringValue {
val var46 = history[endGen].findByBeginGenOpt(43, 2, beginGen)
val var47 = history[endGen].findByBeginGenOpt(84, 2, beginGen)
check(hasSingleTrue(var46 != null, var47 != null))
val var48 = when {
var46 != null -> {
val var49 = getSequenceElems(history, 43, listOf(44,80), beginGen, endGen)
val var50 = matchStringFrac(var49[0].first, var49[0].second)
val var51 = unrollRepeat0(history, 80, 82, 6, 81, var49[1].first, var49[1].second).map { k ->
val var52 = getSequenceElems(history, 83, listOf(4,44), k.first, k.second)
val var53 = matchStringFrac(var52[1].first, var52[1].second)
var53
}
val var54 = StringValue(null, listOf(var50) + var51, nextId(), beginGen, endGen)
var54
}
else -> {
val var55 = getSequenceElems(history, 84, listOf(85,44), beginGen, endGen)
val var56 = matchStringTypeAnnot(var55[0].first, var55[0].second)
val var57 = matchStringFrac(var55[1].first, var55[1].second)
val var58 = StringValue(var56, listOf(var57), nextId(), beginGen, endGen)
var58
}
}
return var48
}

fun matchStringFrac(beginGen: Int, endGen: Int): StringFrac {
val var59 = getSequenceElems(history, 45, listOf(46,47,46), beginGen, endGen)
val var60 = unrollRepeat0(history, 47, 49, 6, 48, var59[1].first, var59[1].second).map { k ->
val var61 = matchStringElem(k.first, k.second)
var61
}
val var62 = StringFrac(var60, nextId(), beginGen, endGen)
return var62
}

fun matchStringElem(beginGen: Int, endGen: Int): StringElem {
val var63 = history[endGen].findByBeginGenOpt(50, 2, beginGen)
val var64 = history[endGen].findByBeginGenOpt(53, 3, beginGen)
val var65 = history[endGen].findByBeginGenOpt(61, 4, beginGen)
val var66 = history[endGen].findByBeginGenOpt(66, 6, beginGen)
val var67 = history[endGen].findByBeginGenOpt(68, 10, beginGen)
val var68 = history[endGen].findByBeginGenOpt(71, 10, beginGen)
val var69 = history[endGen].findByBeginGenOpt(73, 1, beginGen)
check(hasSingleTrue(var63 != null, var64 != null, var65 != null, var66 != null, var67 != null, var68 != null, var69 != null))
val var70 = when {
var63 != null -> {
val var71 = getSequenceElems(history, 50, listOf(51,52), beginGen, endGen)
val var72 = EscapeCode(source[var71[1].first], nextId(), beginGen, endGen)
var72
}
var64 != null -> {
val var73 = getSequenceElems(history, 53, listOf(51,54,56), beginGen, endGen)
val var74 = matchOct(var73[1].first, var73[1].second)
val var75 = history[var73[2].second].findByBeginGenOpt(57, 1, var73[2].first)
val var76 = history[var73[2].second].findByBeginGenOpt(60, 1, var73[2].first)
check(hasSingleTrue(var75 != null, var76 != null))
val var77 = when {
var75 != null -> {
val var78 = getSequenceElems(history, 58, listOf(54,59), var73[2].first, var73[2].second)
val var79 = history[var78[1].second].findByBeginGenOpt(54, 1, var78[1].first)
val var80 = history[var78[1].second].findByBeginGenOpt(60, 1, var78[1].first)
check(hasSingleTrue(var79 != null, var80 != null))
val var81 = when {
var79 != null -> {
val var82 = matchOct(var78[1].first, var78[1].second)
var82
}
else -> null
}
var81
}
else -> null
}
val var83 = OctCode(var74.toString() + (var77?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var83
}
var65 != null -> {
val var84 = getSequenceElems(history, 61, listOf(51,62,63,65), beginGen, endGen)
val var85 = matchHex(var84[2].first, var84[2].second)
val var86 = history[var84[3].second].findByBeginGenOpt(60, 1, var84[3].first)
val var87 = history[var84[3].second].findByBeginGenOpt(63, 1, var84[3].first)
check(hasSingleTrue(var86 != null, var87 != null))
val var88 = when {
var86 != null -> null
else -> {
val var89 = matchHex(var84[3].first, var84[3].second)
var89
}
}
val var90 = HexCode(var85.toString() + (var88?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var90
}
var66 != null -> {
val var91 = getSequenceElems(history, 66, listOf(51,67,63,63,63,63), beginGen, endGen)
val var92 = matchHex(var91[2].first, var91[2].second)
val var93 = matchHex(var91[3].first, var91[3].second)
val var94 = matchHex(var91[4].first, var91[4].second)
val var95 = matchHex(var91[5].first, var91[5].second)
val var96 = Unicode(var92.toString() + var93.toString() + var94.toString() + var95.toString(), nextId(), beginGen, endGen)
var96
}
var67 != null -> {
val var97 = getSequenceElems(history, 68, listOf(51,69,70,70,70,63,63,63,63,63), beginGen, endGen)
val var98 = matchHex(var97[5].first, var97[5].second)
val var99 = matchHex(var97[6].first, var97[6].second)
val var100 = matchHex(var97[7].first, var97[7].second)
val var101 = matchHex(var97[8].first, var97[8].second)
val var102 = matchHex(var97[9].first, var97[9].second)
val var103 = Unicode(source[var97[2].first].toString() + source[var97[3].first].toString() + source[var97[4].first].toString() + var98.toString() + var99.toString() + var100.toString() + var101.toString() + var102.toString(), nextId(), beginGen, endGen)
var103
}
var68 != null -> {
val var104 = getSequenceElems(history, 71, listOf(51,69,70,70,72,70,63,63,63,63), beginGen, endGen)
val var105 = matchHex(var104[6].first, var104[6].second)
val var106 = matchHex(var104[7].first, var104[7].second)
val var107 = matchHex(var104[8].first, var104[8].second)
val var108 = matchHex(var104[9].first, var104[9].second)
val var109 = Unicode(source[var104[2].first].toString() + source[var104[3].first].toString() + source[var104[4].first].toString() + source[var104[5].first].toString() + var105.toString() + var106.toString() + var107.toString() + var108.toString(), nextId(), beginGen, endGen)
var109
}
else -> {
val var110 = unrollRepeat1(history, 74, 75, 75, 79, beginGen, endGen).map { k ->
source[k.first]
}
val var111 = PlainText(var110.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
var111
}
}
return var70
}

fun matchHex(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchOct(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchObjectValue(beginGen: Int, endGen: Int): ObjectValue {
val var112 = history[endGen].findByBeginGenOpt(130, 3, beginGen)
val var113 = history[endGen].findByBeginGenOpt(131, 6, beginGen)
check(hasSingleTrue(var112 != null, var113 != null))
val var114 = when {
var112 != null -> {
val var115 = ObjectValue(listOf(), nextId(), beginGen, endGen)
var115
}
else -> {
val var116 = getSequenceElems(history, 131, listOf(38,4,132,136,4,39), beginGen, endGen)
val var117 = matchKeyValuePair(var116[2].first, var116[2].second)
val var118 = unrollRepeat0(history, 136, 138, 6, 137, var116[3].first, var116[3].second).map { k ->
val var119 = getSequenceElems(history, 139, listOf(126,4,132), k.first, k.second)
val var120 = matchKeyValuePair(var119[2].first, var119[2].second)
var120
}
val var121 = ObjectValue(listOf(var117) + var118, nextId(), beginGen, endGen)
var121
}
}
return var114
}

fun matchEnumValue(beginGen: Int, endGen: Int): EnumValue {
val var122 = matchName(beginGen, endGen)
val var123 = EnumValue(var122, nextId(), beginGen, endGen)
return var123
}

fun matchSingleItem(beginGen: Int, endGen: Int): SingleItem {
val var124 = getSequenceElems(history, 21, listOf(22,15,28), beginGen, endGen)
val var125 = matchName(var124[0].first, var124[0].second)
val var126 = matchItemValue(var124[2].first, var124[2].second)
val var127 = SingleItem(var125, var126, nextId(), beginGen, endGen)
return var127
}

fun matchKeyValuePair(beginGen: Int, endGen: Int): KeyValuePair {
val var128 = getSequenceElems(history, 133, listOf(134,4,31,4,41), beginGen, endGen)
val var129 = matchKey(var128[0].first, var128[0].second)
val var130 = matchValue(var128[4].first, var128[4].second)
val var131 = KeyValuePair(var129, var130, nextId(), beginGen, endGen)
return var131
}

fun matchNumberValue(beginGen: Int, endGen: Int): NumberValue {
val var132 = history[endGen].findByBeginGenOpt(91, 1, beginGen)
val var133 = history[endGen].findByBeginGenOpt(109, 1, beginGen)
val var134 = history[endGen].findByBeginGenOpt(113, 1, beginGen)
check(hasSingleTrue(var132 != null, var133 != null, var134 != null))
val var135 = when {
var132 != null -> {
val var136 = matchDecValue(beginGen, endGen)
var136
}
var133 != null -> {
val var137 = matchOctValue(beginGen, endGen)
var137
}
else -> {
val var138 = matchHexValue(beginGen, endGen)
var138
}
}
return var135
}

fun matchOctValue(beginGen: Int, endGen: Int): OctValue {
val var139 = getSequenceElems(history, 110, listOf(93,70,111), beginGen, endGen)
val var140 = history[var139[0].second].findByBeginGenOpt(60, 1, var139[0].first)
val var141 = history[var139[0].second].findByBeginGenOpt(94, 1, var139[0].first)
check(hasSingleTrue(var140 != null, var141 != null))
val var142 = when {
var140 != null -> null
else -> source[var139[0].first]
}
val var143 = unrollRepeat1(history, 111, 54, 54, 112, var139[2].first, var139[2].second).map { k ->
val var144 = matchOct(k.first, k.second)
var144
}
val var145 = OctValue(var142, var143.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var145
}

fun matchDecValue(beginGen: Int, endGen: Int): DecValue {
val var146 = getSequenceElems(history, 92, listOf(93,95,101,105), beginGen, endGen)
val var147 = history[var146[0].second].findByBeginGenOpt(60, 1, var146[0].first)
val var148 = history[var146[0].second].findByBeginGenOpt(94, 1, var146[0].first)
check(hasSingleTrue(var147 != null, var148 != null))
val var149 = when {
var147 != null -> null
else -> source[var146[0].first]
}
val var150 = matchDec(var146[1].first, var146[1].second)
val var151 = history[var146[2].second].findByBeginGenOpt(60, 1, var146[2].first)
val var152 = history[var146[2].second].findByBeginGenOpt(102, 1, var146[2].first)
check(hasSingleTrue(var151 != null, var152 != null))
val var153 = when {
var151 != null -> null
else -> {
val var154 = getSequenceElems(history, 103, listOf(104,95), var146[2].first, var146[2].second)
val var155 = matchDec(var154[1].first, var154[1].second)
var155
}
}
val var156 = history[var146[3].second].findByBeginGenOpt(60, 1, var146[3].first)
val var157 = history[var146[3].second].findByBeginGenOpt(106, 1, var146[3].first)
check(hasSingleTrue(var156 != null, var157 != null))
val var158 = when {
var156 != null -> null
else -> {
val var159 = getSequenceElems(history, 107, listOf(108,93,95), var146[3].first, var146[3].second)
val var160 = matchDec(var159[2].first, var159[2].second)
var160
}
}
val var161 = DecValue(var149, var150, var153, var158, nextId(), beginGen, endGen)
return var161
}

fun matchDec(beginGen: Int, endGen: Int): String {
val var162 = history[endGen].findByBeginGenOpt(70, 1, beginGen)
val var163 = history[endGen].findByBeginGenOpt(96, 2, beginGen)
check(hasSingleTrue(var162 != null, var163 != null))
val var164 = when {
var162 != null -> "0"
else -> {
val var165 = getSequenceElems(history, 96, listOf(97,98), beginGen, endGen)
val var166 = unrollRepeat0(history, 98, 100, 6, 99, var165[1].first, var165[1].second).map { k ->
source[k.first]
}
source[var165[0].first].toString() + var166.joinToString("") { it.toString() }
}
}
return var164
}

fun matchHexValue(beginGen: Int, endGen: Int): HexValue {
val var167 = getSequenceElems(history, 114, listOf(93,70,62,115), beginGen, endGen)
val var168 = history[var167[0].second].findByBeginGenOpt(60, 1, var167[0].first)
val var169 = history[var167[0].second].findByBeginGenOpt(94, 1, var167[0].first)
check(hasSingleTrue(var168 != null, var169 != null))
val var170 = when {
var168 != null -> null
else -> source[var167[0].first]
}
val var171 = unrollRepeat1(history, 115, 63, 63, 116, var167[3].first, var167[3].second).map { k ->
val var172 = matchHex(k.first, k.second)
var172
}
val var173 = HexValue(var170, var171.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var173
}

fun matchItemValue(beginGen: Int, endGen: Int): ItemValue {
val var174 = history[endGen].findByBeginGenOpt(29, 1, beginGen)
val var175 = history[endGen].findByBeginGenOpt(32, 1, beginGen)
val var176 = history[endGen].findByBeginGenOpt(36, 1, beginGen)
val var177 = history[endGen].findByBeginGenOpt(40, 3, beginGen)
check(hasSingleTrue(var174 != null, var175 != null, var176 != null, var177 != null))
val var178 = when {
var174 != null -> {
val var179 = ObjectHeader(nextId(), beginGen, endGen)
var179
}
var175 != null -> {
val var180 = RepeatedHeader(nextId(), beginGen, endGen)
var180
}
var176 != null -> {
val var181 = MapHeader(nextId(), beginGen, endGen)
var181
}
else -> {
val var182 = getSequenceElems(history, 40, listOf(31,15,41), beginGen, endGen)
val var183 = matchValue(var182[2].first, var182[2].second)
var183
}
}
return var178
}

fun matchKeyValue(beginGen: Int, endGen: Int): KeyValue {
val var184 = history[endGen].findByBeginGenOpt(44, 1, beginGen)
val var185 = history[endGen].findByBeginGenOpt(90, 1, beginGen)
check(hasSingleTrue(var184 != null, var185 != null))
val var186 = when {
var184 != null -> {
val var187 = matchStringFrac(beginGen, endGen)
var187
}
else -> {
val var188 = matchNumberValue(beginGen, endGen)
var188
}
}
return var186
}

fun matchKey(beginGen: Int, endGen: Int): Key {
val var189 = history[endGen].findByBeginGenOpt(22, 1, beginGen)
val var190 = history[endGen].findByBeginGenOpt(135, 1, beginGen)
check(hasSingleTrue(var189 != null, var190 != null))
val var191 = when {
var189 != null -> {
val var192 = matchName(beginGen, endGen)
val var193 = NameKey(var192, nextId(), beginGen, endGen)
var193
}
else -> {
val var194 = matchKeyValue(beginGen, endGen)
var194
}
}
return var191
}

fun matchStringTypeAnnot(beginGen: Int, endGen: Int): StringTypeAnnot {
val var195 = history[endGen].findByBeginGenOpt(86, 1, beginGen)
val var196 = history[endGen].findByBeginGenOpt(87, 1, beginGen)
val var197 = history[endGen].findByBeginGenOpt(88, 1, beginGen)
val var198 = history[endGen].findByBeginGenOpt(89, 1, beginGen)
check(hasSingleTrue(var195 != null, var196 != null, var197 != null, var198 != null))
val var199 = when {
var195 != null -> StringTypeAnnot.Base64
var196 != null -> StringTypeAnnot.Hex
var197 != null -> StringTypeAnnot.Timestamp
else -> StringTypeAnnot.Duration
}
return var199
}

fun matchRepeatedValue(beginGen: Int, endGen: Int): RepeatedValue {
val var200 = history[endGen].findByBeginGenOpt(119, 3, beginGen)
val var201 = history[endGen].findByBeginGenOpt(120, 7, beginGen)
check(hasSingleTrue(var200 != null, var201 != null))
val var202 = when {
var200 != null -> {
val var203 = RepeatedValue(listOf(), nextId(), beginGen, endGen)
var203
}
else -> {
val var204 = getSequenceElems(history, 120, listOf(34,4,41,121,126,4,35), beginGen, endGen)
val var205 = matchValue(var204[2].first, var204[2].second)
val var206 = unrollRepeat0(history, 121, 123, 6, 122, var204[3].first, var204[3].second).map { k ->
val var207 = getSequenceElems(history, 124, listOf(4,125,4,41), k.first, k.second)
val var208 = matchValue(var207[3].first, var207[3].second)
var208
}
val var209 = RepeatedValue(listOf(var205) + var206, nextId(), beginGen, endGen)
var209
}
}
return var202
}

}
