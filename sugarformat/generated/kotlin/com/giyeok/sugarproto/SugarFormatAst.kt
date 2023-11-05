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

data class Timezone(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class ListFieldItem(
  val name: ItemPath,
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
): ItemPathFirst, KeyValue, AstNode

data class NameKey(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KeyValue, AstNode

data class NamePath(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemPathFirst, AstNode

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
): Value, AstNode

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

sealed interface NumberValue: ItemPathFirst, KeyValue, Value, AstNode

sealed interface ItemPathAccess: AstNode

sealed interface KeyValue: AstNode

data class StringValue(
  val type: StringTypeAnnot?,
  val fracs: List<StringFrac>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode

sealed interface ListItem: Item, AstNode

data class IndentItem(
  val indent: String,
  val item: Item,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class ItemPath(
  val first: ItemPathFirst,
  val access: List<ItemPathAccess>,
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
  val exponent: String?,
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
): Value, AstNode

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

sealed interface ItemPathFirst: AstNode

data class Date(
  val year: String,
  val month: String,
  val day: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class TimestampValue(
  val date: Date,
  val time: Time?,
  val timezone: Timezone,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode

data class EscapeCode(
  val code: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StringElem, AstNode

sealed interface Item: AstNode

sealed interface ItemValue: AstNode

data class KeyAccess(
  val value: KeyValue,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemPathAccess, AstNode

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

data class Seconds(
  val integral: String,
  val frac: String?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class MemberAccess(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemPathAccess, AstNode
enum class StringTypeAnnot { Base64, Hex }

fun matchStart(): List<IndentItem> {
  val lastGen = source.length
  val kernel = history[lastGen].getSingle(2, 1, 0, lastGen)
  return matchItems(kernel.beginGen, kernel.endGen)
}

fun matchItems(beginGen: Int, endGen: Int): List<IndentItem> {
val var1 = getSequenceElems(history, 3, listOf(4,30), beginGen, endGen)
val var2 = unrollRepeat0(history, 30, 32, 7, 31, var1[1].first, var1[1].second).map { k ->
val var3 = getSequenceElems(history, 33, listOf(34,19,20,213), k.first, k.second)
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
val var11 = history[endGen].findByBeginGenOpt(208, 1, beginGen)
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
val var15 = history[endGen].findByBeginGenOpt(209, 1, beginGen)
val var16 = history[endGen].findByBeginGenOpt(211, 1, beginGen)
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
val var20 = getSequenceElems(history, 212, listOf(194,19,39,19,119), beginGen, endGen)
val var21 = matchItemPath(var20[2].first, var20[2].second)
val var22 = matchItemValue(var20[4].first, var20[4].second)
val var23 = ListFieldItem(var21, var22, nextId(), beginGen, endGen)
return var23
}

fun matchItemPath(beginGen: Int, endGen: Int): ItemPath {
val var24 = getSequenceElems(history, 40, listOf(41,109), beginGen, endGen)
val var25 = matchItemPathFirst(var24[0].first, var24[0].second)
val var26 = unrollRepeat0(history, 109, 111, 7, 110, var24[1].first, var24[1].second).map { k ->
val var27 = getSequenceElems(history, 112, listOf(19,113), k.first, k.second)
val var28 = matchItemPathAccess(var27[1].first, var27[1].second)
var28
}
val var29 = ItemPath(var25, var26, nextId(), beginGen, endGen)
return var29
}

fun matchListValueItem(beginGen: Int, endGen: Int): ListValueItem {
val var30 = getSequenceElems(history, 210, listOf(194,19,124), beginGen, endGen)
val var31 = matchValue(var30[2].first, var30[2].second)
val var32 = ListValueItem(var31, nextId(), beginGen, endGen)
return var32
}

fun matchValue(beginGen: Int, endGen: Int): Value {
val var33 = history[endGen].findByBeginGenOpt(82, 1, beginGen)
val var34 = history[endGen].findByBeginGenOpt(125, 1, beginGen)
val var35 = history[endGen].findByBeginGenOpt(135, 1, beginGen)
val var36 = history[endGen].findByBeginGenOpt(136, 1, beginGen)
val var37 = history[endGen].findByBeginGenOpt(147, 1, beginGen)
val var38 = history[endGen].findByBeginGenOpt(158, 1, beginGen)
val var39 = history[endGen].findByBeginGenOpt(188, 1, beginGen)
check(hasSingleTrue(var33 != null, var34 != null, var35 != null, var36 != null, var37 != null, var38 != null, var39 != null))
val var40 = when {
var33 != null -> {
val var41 = matchNumberValue(beginGen, endGen)
var41
}
var34 != null -> {
val var42 = matchStringValue(beginGen, endGen)
var42
}
var35 != null -> {
val var43 = matchNameValue(beginGen, endGen)
var43
}
var36 != null -> {
val var44 = matchRepeatedValue(beginGen, endGen)
var44
}
var37 != null -> {
val var45 = matchObjectOrMapValue(beginGen, endGen)
var45
}
var38 != null -> {
val var46 = matchDurationValue(beginGen, endGen)
var46
}
else -> {
val var47 = matchTimestampValue(beginGen, endGen)
var47
}
}
return var40
}

fun matchStringValue(beginGen: Int, endGen: Int): StringValue {
val var48 = history[endGen].findByBeginGenOpt(126, 2, beginGen)
val var49 = history[endGen].findByBeginGenOpt(131, 2, beginGen)
check(hasSingleTrue(var48 != null, var49 != null))
val var50 = when {
var48 != null -> {
val var51 = getSequenceElems(history, 126, listOf(48,127), beginGen, endGen)
val var52 = matchStringFrac(var51[0].first, var51[0].second)
val var53 = unrollRepeat0(history, 127, 129, 7, 128, var51[1].first, var51[1].second).map { k ->
val var54 = getSequenceElems(history, 130, listOf(4,48), k.first, k.second)
val var55 = matchStringFrac(var54[1].first, var54[1].second)
var55
}
val var56 = StringValue(null, listOf(var52) + var53, nextId(), beginGen, endGen)
var56
}
else -> {
val var57 = getSequenceElems(history, 131, listOf(132,48), beginGen, endGen)
val var58 = matchStringTypeAnnot(var57[0].first, var57[0].second)
val var59 = matchStringFrac(var57[1].first, var57[1].second)
val var60 = StringValue(var58, listOf(var59), nextId(), beginGen, endGen)
var60
}
}
return var50
}

fun matchStringFrac(beginGen: Int, endGen: Int): StringFrac {
val var61 = getSequenceElems(history, 49, listOf(50,51,50), beginGen, endGen)
val var62 = unrollRepeat0(history, 51, 53, 7, 52, var61[1].first, var61[1].second).map { k ->
val var63 = matchStringElem(k.first, k.second)
var63
}
val var64 = StringFrac(var62, nextId(), beginGen, endGen)
return var64
}

fun matchStringElem(beginGen: Int, endGen: Int): StringElem {
val var65 = history[endGen].findByBeginGenOpt(54, 2, beginGen)
val var66 = history[endGen].findByBeginGenOpt(57, 3, beginGen)
val var67 = history[endGen].findByBeginGenOpt(64, 4, beginGen)
val var68 = history[endGen].findByBeginGenOpt(69, 6, beginGen)
val var69 = history[endGen].findByBeginGenOpt(71, 10, beginGen)
val var70 = history[endGen].findByBeginGenOpt(74, 10, beginGen)
val var71 = history[endGen].findByBeginGenOpt(76, 1, beginGen)
check(hasSingleTrue(var65 != null, var66 != null, var67 != null, var68 != null, var69 != null, var70 != null, var71 != null))
val var72 = when {
var65 != null -> {
val var73 = getSequenceElems(history, 54, listOf(55,56), beginGen, endGen)
val var74 = EscapeCode(source[var73[1].first], nextId(), beginGen, endGen)
var74
}
var66 != null -> {
val var75 = getSequenceElems(history, 57, listOf(55,58,60), beginGen, endGen)
val var76 = matchOct(var75[1].first, var75[1].second)
val var77 = history[var75[2].second].findByBeginGenOpt(29, 1, var75[2].first)
val var78 = history[var75[2].second].findByBeginGenOpt(61, 1, var75[2].first)
check(hasSingleTrue(var77 != null, var78 != null))
val var79 = when {
var77 != null -> null
else -> {
val var80 = getSequenceElems(history, 62, listOf(58,63), var75[2].first, var75[2].second)
val var81 = history[var80[1].second].findByBeginGenOpt(29, 1, var80[1].first)
val var82 = history[var80[1].second].findByBeginGenOpt(58, 1, var80[1].first)
check(hasSingleTrue(var81 != null, var82 != null))
val var83 = when {
var81 != null -> null
else -> {
val var84 = matchOct(var80[1].first, var80[1].second)
var84
}
}
var83
}
}
val var85 = OctCode(var76.toString() + (var79?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var85
}
var67 != null -> {
val var86 = getSequenceElems(history, 64, listOf(55,65,66,68), beginGen, endGen)
val var87 = matchHex(var86[2].first, var86[2].second)
val var88 = history[var86[3].second].findByBeginGenOpt(29, 1, var86[3].first)
val var89 = history[var86[3].second].findByBeginGenOpt(66, 1, var86[3].first)
check(hasSingleTrue(var88 != null, var89 != null))
val var90 = when {
var88 != null -> null
else -> {
val var91 = matchHex(var86[3].first, var86[3].second)
var91
}
}
val var92 = HexCode(var87.toString() + (var90?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var92
}
var68 != null -> {
val var93 = getSequenceElems(history, 69, listOf(55,70,66,66,66,66), beginGen, endGen)
val var94 = matchHex(var93[2].first, var93[2].second)
val var95 = matchHex(var93[3].first, var93[3].second)
val var96 = matchHex(var93[4].first, var93[4].second)
val var97 = matchHex(var93[5].first, var93[5].second)
val var98 = Unicode(var94.toString() + var95.toString() + var96.toString() + var97.toString(), nextId(), beginGen, endGen)
var98
}
var69 != null -> {
val var99 = getSequenceElems(history, 71, listOf(55,72,73,73,73,66,66,66,66,66), beginGen, endGen)
val var100 = matchHex(var99[5].first, var99[5].second)
val var101 = matchHex(var99[6].first, var99[6].second)
val var102 = matchHex(var99[7].first, var99[7].second)
val var103 = matchHex(var99[8].first, var99[8].second)
val var104 = matchHex(var99[9].first, var99[9].second)
val var105 = Unicode(source[var99[2].first].toString() + source[var99[3].first].toString() + source[var99[4].first].toString() + var100.toString() + var101.toString() + var102.toString() + var103.toString() + var104.toString(), nextId(), beginGen, endGen)
var105
}
var70 != null -> {
val var106 = getSequenceElems(history, 74, listOf(55,72,73,73,75,73,66,66,66,66), beginGen, endGen)
val var107 = matchHex(var106[6].first, var106[6].second)
val var108 = matchHex(var106[7].first, var106[7].second)
val var109 = matchHex(var106[8].first, var106[8].second)
val var110 = matchHex(var106[9].first, var106[9].second)
val var111 = Unicode(source[var106[2].first].toString() + source[var106[3].first].toString() + source[var106[4].first].toString() + source[var106[5].first].toString() + var107.toString() + var108.toString() + var109.toString() + var110.toString(), nextId(), beginGen, endGen)
var111
}
else -> {
val var112 = unrollRepeat1(history, 77, 78, 78, 81, beginGen, endGen).map { k ->
source[k.first]
}
val var113 = PlainText(var112.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
var113
}
}
return var72
}

fun matchHex(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchOct(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchNameValue(beginGen: Int, endGen: Int): NameValue {
val var114 = matchName(beginGen, endGen)
val var115 = NameValue(var114, nextId(), beginGen, endGen)
return var115
}

fun matchName(beginGen: Int, endGen: Int): String {
val var116 = getSequenceElems(history, 43, listOf(44,45), beginGen, endGen)
val var117 = unrollRepeat0(history, 45, 47, 7, 46, var116[1].first, var116[1].second).map { k ->
source[k.first]
}
return source[var116[0].first].toString() + var117.joinToString("") { it.toString() }
}

fun matchNumberValue(beginGen: Int, endGen: Int): NumberValue {
val var118 = history[endGen].findByBeginGenOpt(83, 1, beginGen)
val var119 = history[endGen].findByBeginGenOpt(101, 1, beginGen)
val var120 = history[endGen].findByBeginGenOpt(105, 1, beginGen)
check(hasSingleTrue(var118 != null, var119 != null, var120 != null))
val var121 = when {
var118 != null -> {
val var122 = matchDecValue(beginGen, endGen)
var122
}
var119 != null -> {
val var123 = matchOctValue(beginGen, endGen)
var123
}
else -> {
val var124 = matchHexValue(beginGen, endGen)
var124
}
}
return var121
}

fun matchOctValue(beginGen: Int, endGen: Int): OctValue {
val var125 = getSequenceElems(history, 102, listOf(85,73,103), beginGen, endGen)
val var126 = history[var125[0].second].findByBeginGenOpt(29, 1, var125[0].first)
val var127 = history[var125[0].second].findByBeginGenOpt(86, 1, var125[0].first)
check(hasSingleTrue(var126 != null, var127 != null))
val var128 = when {
var126 != null -> null
else -> source[var125[0].first]
}
val var129 = unrollRepeat1(history, 103, 58, 58, 104, var125[2].first, var125[2].second).map { k ->
val var130 = matchOct(k.first, k.second)
var130
}
val var131 = OctValue(var128, var129.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var131
}

fun matchDecValue(beginGen: Int, endGen: Int): DecValue {
val var132 = getSequenceElems(history, 84, listOf(85,87,93,97), beginGen, endGen)
val var133 = history[var132[0].second].findByBeginGenOpt(29, 1, var132[0].first)
val var134 = history[var132[0].second].findByBeginGenOpt(86, 1, var132[0].first)
check(hasSingleTrue(var133 != null, var134 != null))
val var135 = when {
var133 != null -> null
else -> source[var132[0].first]
}
val var136 = matchDec(var132[1].first, var132[1].second)
val var137 = history[var132[2].second].findByBeginGenOpt(29, 1, var132[2].first)
val var138 = history[var132[2].second].findByBeginGenOpt(94, 1, var132[2].first)
check(hasSingleTrue(var137 != null, var138 != null))
val var139 = when {
var137 != null -> null
else -> {
val var140 = getSequenceElems(history, 95, listOf(96,87), var132[2].first, var132[2].second)
val var141 = matchDec(var140[1].first, var140[1].second)
var141
}
}
val var142 = history[var132[3].second].findByBeginGenOpt(29, 1, var132[3].first)
val var143 = history[var132[3].second].findByBeginGenOpt(98, 1, var132[3].first)
check(hasSingleTrue(var142 != null, var143 != null))
val var144 = when {
var142 != null -> null
else -> {
val var145 = getSequenceElems(history, 99, listOf(100,85,87), var132[3].first, var132[3].second)
val var146 = matchDec(var145[2].first, var145[2].second)
var146
}
}
val var147 = DecValue(var135, var136, var139, var144, nextId(), beginGen, endGen)
return var147
}

fun matchDec(beginGen: Int, endGen: Int): String {
val var148 = history[endGen].findByBeginGenOpt(73, 1, beginGen)
val var149 = history[endGen].findByBeginGenOpt(88, 2, beginGen)
check(hasSingleTrue(var148 != null, var149 != null))
val var150 = when {
var148 != null -> "0"
else -> {
val var151 = getSequenceElems(history, 88, listOf(89,90), beginGen, endGen)
val var152 = unrollRepeat0(history, 90, 92, 7, 91, var151[1].first, var151[1].second).map { k ->
source[k.first]
}
source[var151[0].first].toString() + var152.joinToString("") { it.toString() }
}
}
return var150
}

fun matchHexValue(beginGen: Int, endGen: Int): HexValue {
val var153 = getSequenceElems(history, 106, listOf(85,73,65,107), beginGen, endGen)
val var154 = history[var153[0].second].findByBeginGenOpt(29, 1, var153[0].first)
val var155 = history[var153[0].second].findByBeginGenOpt(86, 1, var153[0].first)
check(hasSingleTrue(var154 != null, var155 != null))
val var156 = when {
var154 != null -> null
else -> source[var153[0].first]
}
val var157 = unrollRepeat1(history, 107, 66, 66, 108, var153[3].first, var153[3].second).map { k ->
val var158 = matchHex(k.first, k.second)
var158
}
val var159 = HexValue(var156, var157.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var159
}

fun matchItemPathFirst(beginGen: Int, endGen: Int): ItemPathFirst {
val var160 = history[endGen].findByBeginGenOpt(42, 1, beginGen)
val var161 = history[endGen].findByBeginGenOpt(48, 1, beginGen)
val var162 = history[endGen].findByBeginGenOpt(82, 1, beginGen)
check(hasSingleTrue(var160 != null, var161 != null, var162 != null))
val var163 = when {
var160 != null -> {
val var164 = matchName(beginGen, endGen)
val var165 = NamePath(var164, nextId(), beginGen, endGen)
var165
}
var161 != null -> {
val var166 = matchStringFrac(beginGen, endGen)
var166
}
else -> {
val var167 = matchNumberValue(beginGen, endGen)
var167
}
}
return var163
}

fun matchTimestampValue(beginGen: Int, endGen: Int): TimestampValue {
val var168 = getSequenceElems(history, 189, listOf(190,197,206), beginGen, endGen)
val var169 = matchDate(var168[0].first, var168[0].second)
val var170 = history[var168[1].second].findByBeginGenOpt(29, 1, var168[1].first)
val var171 = history[var168[1].second].findByBeginGenOpt(198, 1, var168[1].first)
check(hasSingleTrue(var170 != null, var171 != null))
val var172 = when {
var170 != null -> null
else -> {
val var173 = getSequenceElems(history, 199, listOf(200,201), var168[1].first, var168[1].second)
val var174 = matchTime(var173[1].first, var173[1].second)
var174
}
}
val var175 = matchTimezone(var168[2].first, var168[2].second)
val var176 = TimestampValue(var169, var172, var175, nextId(), beginGen, endGen)
return var176
}

fun matchTimezone(beginGen: Int, endGen: Int): Timezone {
val var177 = Timezone("UTC", nextId(), beginGen, endGen)
return var177
}

fun matchDate(beginGen: Int, endGen: Int): Date {
val var178 = getSequenceElems(history, 191, listOf(192,194,195,194,195), beginGen, endGen)
val var179 = matchDigit4(var178[0].first, var178[0].second)
val var180 = matchDigit2(var178[2].first, var178[2].second)
val var181 = matchDigit2(var178[4].first, var178[4].second)
val var182 = Date(var179, var180, var181, nextId(), beginGen, endGen)
return var182
}

fun matchDigit2(beginGen: Int, endGen: Int): String {
val var183 = getSequenceElems(history, 196, listOf(92,92), beginGen, endGen)
return source[var183[0].first].toString() + source[var183[1].first].toString()
}

fun matchItemPathAccess(beginGen: Int, endGen: Int): ItemPathAccess {
val var184 = history[endGen].findByBeginGenOpt(114, 3, beginGen)
val var185 = history[endGen].findByBeginGenOpt(115, 5, beginGen)
check(hasSingleTrue(var184 != null, var185 != null))
val var186 = when {
var184 != null -> {
val var187 = getSequenceElems(history, 114, listOf(96,19,42), beginGen, endGen)
val var188 = matchName(var187[2].first, var187[2].second)
val var189 = MemberAccess(var188, nextId(), beginGen, endGen)
var189
}
else -> {
val var190 = getSequenceElems(history, 115, listOf(116,19,117,19,118), beginGen, endGen)
val var191 = matchKeyValue(var190[2].first, var190[2].second)
val var192 = KeyAccess(var191, nextId(), beginGen, endGen)
var192
}
}
return var186
}

fun matchItemValue(beginGen: Int, endGen: Int): ItemValue {
val var193 = history[endGen].findByBeginGenOpt(120, 1, beginGen)
val var194 = history[endGen].findByBeginGenOpt(123, 3, beginGen)
check(hasSingleTrue(var193 != null, var194 != null))
val var195 = when {
var193 != null -> {
val var196 = Header(nextId(), beginGen, endGen)
var196
}
else -> {
val var197 = getSequenceElems(history, 123, listOf(122,19,124), beginGen, endGen)
val var198 = matchValue(var197[2].first, var197[2].second)
var198
}
}
return var195
}

fun matchKeyValue(beginGen: Int, endGen: Int): KeyValue {
val var199 = history[endGen].findByBeginGenOpt(42, 1, beginGen)
val var200 = history[endGen].findByBeginGenOpt(48, 1, beginGen)
val var201 = history[endGen].findByBeginGenOpt(82, 1, beginGen)
check(hasSingleTrue(var199 != null, var200 != null, var201 != null))
val var202 = when {
var199 != null -> {
val var203 = matchName(beginGen, endGen)
val var204 = NameKey(var203, nextId(), beginGen, endGen)
var204
}
var200 != null -> {
val var205 = matchStringFrac(beginGen, endGen)
var205
}
else -> {
val var206 = matchNumberValue(beginGen, endGen)
var206
}
}
return var202
}

fun matchStringTypeAnnot(beginGen: Int, endGen: Int): StringTypeAnnot {
val var207 = history[endGen].findByBeginGenOpt(133, 1, beginGen)
val var208 = history[endGen].findByBeginGenOpt(134, 1, beginGen)
check(hasSingleTrue(var207 != null, var208 != null))
val var209 = when {
var207 != null -> StringTypeAnnot.Base64
else -> StringTypeAnnot.Hex
}
return var209
}

fun matchRepeatedValue(beginGen: Int, endGen: Int): RepeatedValue {
val var210 = history[endGen].findByBeginGenOpt(137, 3, beginGen)
val var211 = history[endGen].findByBeginGenOpt(138, 7, beginGen)
check(hasSingleTrue(var210 != null, var211 != null))
val var212 = when {
var210 != null -> {
val var213 = RepeatedValue(listOf(), nextId(), beginGen, endGen)
var213
}
else -> {
val var214 = getSequenceElems(history, 138, listOf(116,4,124,139,144,4,118), beginGen, endGen)
val var215 = matchValue(var214[2].first, var214[2].second)
val var216 = unrollRepeat0(history, 139, 141, 7, 140, var214[3].first, var214[3].second).map { k ->
val var217 = getSequenceElems(history, 142, listOf(4,143,4,124), k.first, k.second)
val var218 = matchValue(var217[3].first, var217[3].second)
var218
}
val var219 = RepeatedValue(listOf(var215) + var216, nextId(), beginGen, endGen)
var219
}
}
return var212
}

fun matchDurationValue(beginGen: Int, endGen: Int): DurationValue {
val var220 = history[endGen].findByBeginGenOpt(159, 4, beginGen)
val var221 = history[endGen].findByBeginGenOpt(163, 1, beginGen)
val var222 = history[endGen].findByBeginGenOpt(186, 3, beginGen)
val var223 = history[endGen].findByBeginGenOpt(187, 2, beginGen)
check(hasSingleTrue(var220 != null, var221 != null, var222 != null, var223 != null))
val var224 = when {
var220 != null -> {
val var225 = getSequenceElems(history, 159, listOf(160,167,172,178), beginGen, endGen)
val var226 = history[var225[0].second].findByBeginGenOpt(29, 1, var225[0].first)
val var227 = history[var225[0].second].findByBeginGenOpt(161, 1, var225[0].first)
check(hasSingleTrue(var226 != null, var227 != null))
val var228 = when {
var226 != null -> null
else -> {
val var229 = getSequenceElems(history, 162, listOf(163,19), var225[0].first, var225[0].second)
val var230 = matchDays(var229[0].first, var229[0].second)
var230
}
}
val var231 = history[var225[1].second].findByBeginGenOpt(29, 1, var225[1].first)
val var232 = history[var225[1].second].findByBeginGenOpt(168, 1, var225[1].first)
check(hasSingleTrue(var231 != null, var232 != null))
val var233 = when {
var231 != null -> null
else -> {
val var234 = getSequenceElems(history, 169, listOf(170,19), var225[1].first, var225[1].second)
val var235 = matchHours(var234[0].first, var234[0].second)
var235
}
}
val var236 = history[var225[2].second].findByBeginGenOpt(29, 1, var225[2].first)
val var237 = history[var225[2].second].findByBeginGenOpt(173, 1, var225[2].first)
check(hasSingleTrue(var236 != null, var237 != null))
val var238 = when {
var236 != null -> null
else -> {
val var239 = getSequenceElems(history, 174, listOf(175,19), var225[2].first, var225[2].second)
val var240 = matchMinutes(var239[0].first, var239[0].second)
var240
}
}
val var241 = matchSeconds(var225[3].first, var225[3].second)
val var242 = DurationValue(var228, var233, var238, var241, nextId(), beginGen, endGen)
var242
}
var221 != null -> {
val var243 = matchDays(beginGen, endGen)
val var244 = DurationValue(var243, null, null, null, nextId(), beginGen, endGen)
var244
}
var222 != null -> {
val var245 = getSequenceElems(history, 186, listOf(160,167,175), beginGen, endGen)
val var246 = history[var245[0].second].findByBeginGenOpt(29, 1, var245[0].first)
val var247 = history[var245[0].second].findByBeginGenOpt(161, 1, var245[0].first)
check(hasSingleTrue(var246 != null, var247 != null))
val var248 = when {
var246 != null -> null
else -> {
val var249 = getSequenceElems(history, 162, listOf(163,19), var245[0].first, var245[0].second)
val var250 = matchDays(var249[0].first, var249[0].second)
var250
}
}
val var251 = history[var245[1].second].findByBeginGenOpt(29, 1, var245[1].first)
val var252 = history[var245[1].second].findByBeginGenOpt(168, 1, var245[1].first)
check(hasSingleTrue(var251 != null, var252 != null))
val var253 = when {
var251 != null -> null
else -> {
val var254 = getSequenceElems(history, 169, listOf(170,19), var245[1].first, var245[1].second)
val var255 = matchHours(var254[0].first, var254[0].second)
var255
}
}
val var256 = matchMinutes(var245[2].first, var245[2].second)
val var257 = DurationValue(var248, var253, var256, null, nextId(), beginGen, endGen)
var257
}
else -> {
val var258 = getSequenceElems(history, 187, listOf(160,170), beginGen, endGen)
val var259 = history[var258[0].second].findByBeginGenOpt(29, 1, var258[0].first)
val var260 = history[var258[0].second].findByBeginGenOpt(161, 1, var258[0].first)
check(hasSingleTrue(var259 != null, var260 != null))
val var261 = when {
var259 != null -> null
else -> {
val var262 = getSequenceElems(history, 162, listOf(163,19), var258[0].first, var258[0].second)
val var263 = matchDays(var262[0].first, var262[0].second)
var263
}
}
val var264 = matchHours(var258[1].first, var258[1].second)
val var265 = DurationValue(var261, var264, null, null, nextId(), beginGen, endGen)
var265
}
}
return var224
}

fun matchHours(beginGen: Int, endGen: Int): String {
val var266 = getSequenceElems(history, 171, listOf(165,19,134), beginGen, endGen)
val var267 = matchNumber(var266[0].first, var266[0].second)
return var267
}

fun matchNumber(beginGen: Int, endGen: Int): String {
val var268 = history[endGen].findByBeginGenOpt(73, 1, beginGen)
val var269 = history[endGen].findByBeginGenOpt(88, 2, beginGen)
check(hasSingleTrue(var268 != null, var269 != null))
val var270 = when {
var268 != null -> "0"
else -> {
val var271 = getSequenceElems(history, 88, listOf(89,90), beginGen, endGen)
val var272 = unrollRepeat0(history, 90, 92, 7, 91, var271[1].first, var271[1].second).map { k ->
source[k.first]
}
source[var271[0].first].toString() + var272.joinToString("") { it.toString() }
}
}
return var270
}

fun matchDigit4(beginGen: Int, endGen: Int): String {
val var273 = getSequenceElems(history, 193, listOf(92,92,92,92), beginGen, endGen)
return source[var273[0].first].toString() + source[var273[1].first].toString() + source[var273[2].first].toString() + source[var273[3].first].toString()
}

fun matchSingleItem(beginGen: Int, endGen: Int): SingleItem {
val var274 = getSequenceElems(history, 38, listOf(39,19,119), beginGen, endGen)
val var275 = matchItemPath(var274[0].first, var274[0].second)
val var276 = matchItemValue(var274[2].first, var274[2].second)
val var277 = SingleItem(var275, var276, nextId(), beginGen, endGen)
return var277
}

fun matchDays(beginGen: Int, endGen: Int): String {
val var278 = getSequenceElems(history, 164, listOf(165,19,166), beginGen, endGen)
val var279 = matchNumber(var278[0].first, var278[0].second)
return var279
}

fun matchTime(beginGen: Int, endGen: Int): Time {
val var280 = history[endGen].findByBeginGenOpt(202, 6, beginGen)
val var281 = history[endGen].findByBeginGenOpt(203, 4, beginGen)
val var282 = history[endGen].findByBeginGenOpt(204, 3, beginGen)
val var283 = history[endGen].findByBeginGenOpt(205, 2, beginGen)
check(hasSingleTrue(var280 != null, var281 != null, var282 != null, var283 != null))
val var284 = when {
var280 != null -> {
val var285 = getSequenceElems(history, 202, listOf(195,122,195,122,195,180), beginGen, endGen)
val var286 = matchDigit2(var285[0].first, var285[0].second)
val var287 = matchDigit2(var285[2].first, var285[2].second)
val var288 = matchDigit2(var285[4].first, var285[4].second)
val var289 = history[var285[5].second].findByBeginGenOpt(29, 1, var285[5].first)
val var290 = history[var285[5].second].findByBeginGenOpt(181, 1, var285[5].first)
check(hasSingleTrue(var289 != null, var290 != null))
val var291 = when {
var289 != null -> null
else -> {
val var292 = matchSecondFrac(var285[5].first, var285[5].second)
var292
}
}
val var293 = Time(var286, var287, var288, var291, nextId(), beginGen, endGen)
var293
}
var281 != null -> {
val var294 = getSequenceElems(history, 203, listOf(195,195,195,180), beginGen, endGen)
val var295 = matchDigit2(var294[0].first, var294[0].second)
val var296 = matchDigit2(var294[1].first, var294[1].second)
val var297 = matchDigit2(var294[2].first, var294[2].second)
val var298 = history[var294[3].second].findByBeginGenOpt(29, 1, var294[3].first)
val var299 = history[var294[3].second].findByBeginGenOpt(181, 1, var294[3].first)
check(hasSingleTrue(var298 != null, var299 != null))
val var300 = when {
var298 != null -> null
else -> {
val var301 = matchSecondFrac(var294[3].first, var294[3].second)
var301
}
}
val var302 = Time(var295, var296, var297, var300, nextId(), beginGen, endGen)
var302
}
var282 != null -> {
val var303 = getSequenceElems(history, 204, listOf(195,122,195), beginGen, endGen)
val var304 = matchDigit2(var303[0].first, var303[0].second)
val var305 = matchDigit2(var303[2].first, var303[2].second)
val var306 = Time(var304, var305, null, null, nextId(), beginGen, endGen)
var306
}
else -> {
val var307 = getSequenceElems(history, 205, listOf(195,195), beginGen, endGen)
val var308 = matchDigit2(var307[0].first, var307[0].second)
val var309 = matchDigit2(var307[1].first, var307[1].second)
val var310 = Time(var308, var309, null, null, nextId(), beginGen, endGen)
var310
}
}
return var284
}

fun matchObjectOrMapValue(beginGen: Int, endGen: Int): ObjectOrMapValue {
val var311 = history[endGen].findByBeginGenOpt(148, 3, beginGen)
val var312 = history[endGen].findByBeginGenOpt(151, 6, beginGen)
check(hasSingleTrue(var311 != null, var312 != null))
val var313 = when {
var311 != null -> {
val var314 = ObjectOrMapValue(listOf(), nextId(), beginGen, endGen)
var314
}
else -> {
val var315 = getSequenceElems(history, 151, listOf(149,4,152,154,4,150), beginGen, endGen)
val var316 = matchKeyValuePair(var315[2].first, var315[2].second)
val var317 = unrollRepeat0(history, 154, 156, 7, 155, var315[3].first, var315[3].second).map { k ->
val var318 = getSequenceElems(history, 157, listOf(144,4,152), k.first, k.second)
val var319 = matchKeyValuePair(var318[2].first, var318[2].second)
var319
}
val var320 = ObjectOrMapValue(listOf(var316) + var317, nextId(), beginGen, endGen)
var320
}
}
return var313
}

fun matchKeyValuePair(beginGen: Int, endGen: Int): KeyValuePair {
val var321 = getSequenceElems(history, 153, listOf(39,4,122,4,124), beginGen, endGen)
val var322 = matchItemPath(var321[0].first, var321[0].second)
val var323 = matchValue(var321[4].first, var321[4].second)
val var324 = KeyValuePair(var322, var323, nextId(), beginGen, endGen)
return var324
}

fun matchMinutes(beginGen: Int, endGen: Int): String {
val var325 = getSequenceElems(history, 176, listOf(165,19,177), beginGen, endGen)
val var326 = matchNumber(var325[0].first, var325[0].second)
return var326
}

fun matchSeconds(beginGen: Int, endGen: Int): Seconds {
val var327 = getSequenceElems(history, 179, listOf(165,180,19,185), beginGen, endGen)
val var328 = matchNumber(var327[0].first, var327[0].second)
val var329 = history[var327[1].second].findByBeginGenOpt(29, 1, var327[1].first)
val var330 = history[var327[1].second].findByBeginGenOpt(181, 1, var327[1].first)
check(hasSingleTrue(var329 != null, var330 != null))
val var331 = when {
var329 != null -> null
else -> {
val var332 = matchSecondFrac(var327[1].first, var327[1].second)
var332
}
}
val var333 = Seconds(var328, var331, nextId(), beginGen, endGen)
return var333
}

fun matchSecondFrac(beginGen: Int, endGen: Int): String {
val var334 = getSequenceElems(history, 182, listOf(96,183), beginGen, endGen)
val var335 = unrollRepeat1(history, 183, 92, 92, 184, var334[1].first, var334[1].second).map { k ->
source[k.first]
}
return var335.joinToString("") { it.toString() }
}

}
