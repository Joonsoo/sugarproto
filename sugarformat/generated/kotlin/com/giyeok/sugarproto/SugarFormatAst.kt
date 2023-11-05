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

data class StringValue(
  val type: StringTypeAnnot?,
  val fracs: List<StringFrac>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode

data class ListFieldItem(
  val indent: String,
  val name: String,
  val value: ItemValue,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Item, AstNode

data class StringFrac(
  val elems: List<StringElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KeyValue, AstNode

data class NameKey(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Key, AstNode

data class OctValue(
  val sgn: Char?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): NumberValue, AstNode

data class EnumValue(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode

data class ListValueItem(
  val indent: String,
  val value: Value,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Item, AstNode

data class RepeatedHeader(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemValue, AstNode

data class SingleItem(
  val indent: String,
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

sealed interface ItemValue: Item, AstNode

sealed interface KeyValue: Key, AstNode

sealed interface Key: AstNode

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

fun matchStart(): List<Item> {
  val lastGen = source.length
  val kernel = history[lastGen].getSingle(2, 1, 0, lastGen)
  return matchItems(kernel.beginGen, kernel.endGen)
}

fun matchItems(beginGen: Int, endGen: Int): List<Item> {
val var1 = getSequenceElems(history, 3, listOf(4,9), beginGen, endGen)
val var2 = unrollRepeat0(history, 9, 11, 6, 10, var1[1].first, var1[1].second).map { k ->
val var3 = getSequenceElems(history, 12, listOf(13,16,145,155), k.first, k.second)
val var4 = matchItem(var3[0].first, var3[0].second)
var4
}
return var2
}

fun matchItem(beginGen: Int, endGen: Int): Item {
val var5 = history[endGen].findByBeginGenOpt(14, 1, beginGen)
val var6 = history[endGen].findByBeginGenOpt(138, 1, beginGen)
val var7 = history[endGen].findByBeginGenOpt(141, 1, beginGen)
val var8 = history[endGen].findByBeginGenOpt(143, 1, beginGen)
check(hasSingleTrue(var5 != null, var6 != null, var7 != null, var8 != null))
val var9 = when {
var5 != null -> {
val var10 = matchSingleItem(beginGen, endGen)
var10
}
var6 != null -> {
val var11 = matchListValueItem(beginGen, endGen)
var11
}
var7 != null -> {
val var12 = matchListFieldItem(beginGen, endGen)
var12
}
else -> {
val var13 = matchMapItem(beginGen, endGen)
var13
}
}
return var9
}

fun matchListFieldItem(beginGen: Int, endGen: Int): ListFieldItem {
val var14 = getSequenceElems(history, 142, listOf(16,140,16,20,16,26), beginGen, endGen)
val var15 = matchWS1(var14[0].first, var14[0].second)
val var16 = matchName(var14[3].first, var14[3].second)
val var17 = matchItemValue(var14[5].first, var14[5].second)
val var18 = ListFieldItem(var15.joinToString("") { it.toString() }, var16, var17, nextId(), beginGen, endGen)
return var18
}

fun matchName(beginGen: Int, endGen: Int): String {
val var19 = getSequenceElems(history, 21, listOf(22,23), beginGen, endGen)
val var20 = unrollRepeat0(history, 23, 25, 6, 24, var19[1].first, var19[1].second).map { k ->
source[k.first]
}
return source[var19[0].first].toString() + var20.joinToString("") { it.toString() }
}

fun matchWS1(beginGen: Int, endGen: Int): List<Char> {
val var21 = unrollRepeat0(history, 17, 19, 6, 18, beginGen, endGen).map { k ->
source[k.first]
}
return var21
}

fun matchMapItem(beginGen: Int, endGen: Int): ItemValue {
val var22 = getSequenceElems(history, 144, listOf(16,133,16,26), beginGen, endGen)
val var23 = matchItemValue(var22[3].first, var22[3].second)
return var23
}

fun matchListValueItem(beginGen: Int, endGen: Int): ListValueItem {
val var24 = getSequenceElems(history, 139, listOf(16,140,16,39), beginGen, endGen)
val var25 = matchWS1(var24[0].first, var24[0].second)
val var26 = matchValue(var24[3].first, var24[3].second)
val var27 = ListValueItem(var25.joinToString("") { it.toString() }, var26, nextId(), beginGen, endGen)
return var27
}

fun matchValue(beginGen: Int, endGen: Int): Value {
val var28 = history[endGen].findByBeginGenOpt(40, 1, beginGen)
val var29 = history[endGen].findByBeginGenOpt(88, 1, beginGen)
val var30 = history[endGen].findByBeginGenOpt(115, 1, beginGen)
val var31 = history[endGen].findByBeginGenOpt(116, 1, beginGen)
val var32 = history[endGen].findByBeginGenOpt(127, 1, beginGen)
check(hasSingleTrue(var28 != null, var29 != null, var30 != null, var31 != null, var32 != null))
val var33 = when {
var28 != null -> {
val var34 = matchStringValue(beginGen, endGen)
var34
}
var29 != null -> {
val var35 = matchNumberValue(beginGen, endGen)
var35
}
var30 != null -> {
val var36 = matchEnumValue(beginGen, endGen)
var36
}
var31 != null -> {
val var37 = matchRepeatedValue(beginGen, endGen)
var37
}
else -> {
val var38 = matchObjectValue(beginGen, endGen)
var38
}
}
return var33
}

fun matchStringValue(beginGen: Int, endGen: Int): StringValue {
val var39 = history[endGen].findByBeginGenOpt(41, 2, beginGen)
val var40 = history[endGen].findByBeginGenOpt(82, 2, beginGen)
check(hasSingleTrue(var39 != null, var40 != null))
val var41 = when {
var39 != null -> {
val var42 = getSequenceElems(history, 41, listOf(42,78), beginGen, endGen)
val var43 = matchStringFrac(var42[0].first, var42[0].second)
val var44 = unrollRepeat0(history, 78, 80, 6, 79, var42[1].first, var42[1].second).map { k ->
val var45 = getSequenceElems(history, 81, listOf(4,42), k.first, k.second)
val var46 = matchStringFrac(var45[1].first, var45[1].second)
var46
}
val var47 = StringValue(null, listOf(var43) + var44, nextId(), beginGen, endGen)
var47
}
else -> {
val var48 = getSequenceElems(history, 82, listOf(83,42), beginGen, endGen)
val var49 = matchStringTypeAnnot(var48[0].first, var48[0].second)
val var50 = matchStringFrac(var48[1].first, var48[1].second)
val var51 = StringValue(var49, listOf(var50), nextId(), beginGen, endGen)
var51
}
}
return var41
}

fun matchStringFrac(beginGen: Int, endGen: Int): StringFrac {
val var52 = getSequenceElems(history, 43, listOf(44,45,44), beginGen, endGen)
val var53 = unrollRepeat0(history, 45, 47, 6, 46, var52[1].first, var52[1].second).map { k ->
val var54 = matchStringElem(k.first, k.second)
var54
}
val var55 = StringFrac(var53, nextId(), beginGen, endGen)
return var55
}

fun matchStringElem(beginGen: Int, endGen: Int): StringElem {
val var56 = history[endGen].findByBeginGenOpt(48, 2, beginGen)
val var57 = history[endGen].findByBeginGenOpt(51, 3, beginGen)
val var58 = history[endGen].findByBeginGenOpt(59, 4, beginGen)
val var59 = history[endGen].findByBeginGenOpt(64, 6, beginGen)
val var60 = history[endGen].findByBeginGenOpt(66, 10, beginGen)
val var61 = history[endGen].findByBeginGenOpt(69, 10, beginGen)
val var62 = history[endGen].findByBeginGenOpt(71, 1, beginGen)
check(hasSingleTrue(var56 != null, var57 != null, var58 != null, var59 != null, var60 != null, var61 != null, var62 != null))
val var63 = when {
var56 != null -> {
val var64 = getSequenceElems(history, 48, listOf(49,50), beginGen, endGen)
val var65 = EscapeCode(source[var64[1].first], nextId(), beginGen, endGen)
var65
}
var57 != null -> {
val var66 = getSequenceElems(history, 51, listOf(49,52,54), beginGen, endGen)
val var67 = matchOct(var66[1].first, var66[1].second)
val var68 = history[var66[2].second].findByBeginGenOpt(55, 1, var66[2].first)
val var69 = history[var66[2].second].findByBeginGenOpt(58, 1, var66[2].first)
check(hasSingleTrue(var68 != null, var69 != null))
val var70 = when {
var68 != null -> {
val var71 = getSequenceElems(history, 56, listOf(52,57), var66[2].first, var66[2].second)
val var72 = history[var71[1].second].findByBeginGenOpt(52, 1, var71[1].first)
val var73 = history[var71[1].second].findByBeginGenOpt(58, 1, var71[1].first)
check(hasSingleTrue(var72 != null, var73 != null))
val var74 = when {
var72 != null -> {
val var75 = matchOct(var71[1].first, var71[1].second)
var75
}
else -> null
}
var74
}
else -> null
}
val var76 = OctCode(var67.toString() + (var70?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var76
}
var58 != null -> {
val var77 = getSequenceElems(history, 59, listOf(49,60,61,63), beginGen, endGen)
val var78 = matchHex(var77[2].first, var77[2].second)
val var79 = history[var77[3].second].findByBeginGenOpt(58, 1, var77[3].first)
val var80 = history[var77[3].second].findByBeginGenOpt(61, 1, var77[3].first)
check(hasSingleTrue(var79 != null, var80 != null))
val var81 = when {
var79 != null -> null
else -> {
val var82 = matchHex(var77[3].first, var77[3].second)
var82
}
}
val var83 = HexCode(var78.toString() + (var81?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var83
}
var59 != null -> {
val var84 = getSequenceElems(history, 64, listOf(49,65,61,61,61,61), beginGen, endGen)
val var85 = matchHex(var84[2].first, var84[2].second)
val var86 = matchHex(var84[3].first, var84[3].second)
val var87 = matchHex(var84[4].first, var84[4].second)
val var88 = matchHex(var84[5].first, var84[5].second)
val var89 = Unicode(var85.toString() + var86.toString() + var87.toString() + var88.toString(), nextId(), beginGen, endGen)
var89
}
var60 != null -> {
val var90 = getSequenceElems(history, 66, listOf(49,67,68,68,68,61,61,61,61,61), beginGen, endGen)
val var91 = matchHex(var90[5].first, var90[5].second)
val var92 = matchHex(var90[6].first, var90[6].second)
val var93 = matchHex(var90[7].first, var90[7].second)
val var94 = matchHex(var90[8].first, var90[8].second)
val var95 = matchHex(var90[9].first, var90[9].second)
val var96 = Unicode(source[var90[2].first].toString() + source[var90[3].first].toString() + source[var90[4].first].toString() + var91.toString() + var92.toString() + var93.toString() + var94.toString() + var95.toString(), nextId(), beginGen, endGen)
var96
}
var61 != null -> {
val var97 = getSequenceElems(history, 69, listOf(49,67,68,68,70,68,61,61,61,61), beginGen, endGen)
val var98 = matchHex(var97[6].first, var97[6].second)
val var99 = matchHex(var97[7].first, var97[7].second)
val var100 = matchHex(var97[8].first, var97[8].second)
val var101 = matchHex(var97[9].first, var97[9].second)
val var102 = Unicode(source[var97[2].first].toString() + source[var97[3].first].toString() + source[var97[4].first].toString() + source[var97[5].first].toString() + var98.toString() + var99.toString() + var100.toString() + var101.toString(), nextId(), beginGen, endGen)
var102
}
else -> {
val var103 = unrollRepeat1(history, 72, 73, 73, 77, beginGen, endGen).map { k ->
source[k.first]
}
val var104 = PlainText(var103.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
var104
}
}
return var63
}

fun matchHex(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchOct(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchObjectValue(beginGen: Int, endGen: Int): ObjectValue {
val var105 = history[endGen].findByBeginGenOpt(128, 3, beginGen)
val var106 = history[endGen].findByBeginGenOpt(129, 6, beginGen)
check(hasSingleTrue(var105 != null, var106 != null))
val var107 = when {
var105 != null -> {
val var108 = ObjectValue(listOf(), nextId(), beginGen, endGen)
var108
}
else -> {
val var109 = getSequenceElems(history, 129, listOf(36,4,130,134,4,37), beginGen, endGen)
val var110 = matchKeyValuePair(var109[2].first, var109[2].second)
val var111 = unrollRepeat0(history, 134, 136, 6, 135, var109[3].first, var109[3].second).map { k ->
val var112 = getSequenceElems(history, 137, listOf(124,4,130), k.first, k.second)
val var113 = matchKeyValuePair(var112[2].first, var112[2].second)
var113
}
val var114 = ObjectValue(listOf(var110) + var111, nextId(), beginGen, endGen)
var114
}
}
return var107
}

fun matchEnumValue(beginGen: Int, endGen: Int): EnumValue {
val var115 = matchName(beginGen, endGen)
val var116 = EnumValue(var115, nextId(), beginGen, endGen)
return var116
}

fun matchSingleItem(beginGen: Int, endGen: Int): SingleItem {
val var117 = getSequenceElems(history, 15, listOf(16,20,16,26), beginGen, endGen)
val var118 = matchWS1(var117[0].first, var117[0].second)
val var119 = matchName(var117[1].first, var117[1].second)
val var120 = matchItemValue(var117[3].first, var117[3].second)
val var121 = SingleItem(var118.joinToString("") { it.toString() }, var119, var120, nextId(), beginGen, endGen)
return var121
}

fun matchKeyValuePair(beginGen: Int, endGen: Int): KeyValuePair {
val var122 = getSequenceElems(history, 131, listOf(132,4,29,4,39), beginGen, endGen)
val var123 = matchKey(var122[0].first, var122[0].second)
val var124 = matchValue(var122[4].first, var122[4].second)
val var125 = KeyValuePair(var123, var124, nextId(), beginGen, endGen)
return var125
}

fun matchNumberValue(beginGen: Int, endGen: Int): NumberValue {
val var126 = history[endGen].findByBeginGenOpt(89, 1, beginGen)
val var127 = history[endGen].findByBeginGenOpt(107, 1, beginGen)
val var128 = history[endGen].findByBeginGenOpt(111, 1, beginGen)
check(hasSingleTrue(var126 != null, var127 != null, var128 != null))
val var129 = when {
var126 != null -> {
val var130 = matchDecValue(beginGen, endGen)
var130
}
var127 != null -> {
val var131 = matchOctValue(beginGen, endGen)
var131
}
else -> {
val var132 = matchHexValue(beginGen, endGen)
var132
}
}
return var129
}

fun matchOctValue(beginGen: Int, endGen: Int): OctValue {
val var133 = getSequenceElems(history, 108, listOf(91,68,109), beginGen, endGen)
val var134 = history[var133[0].second].findByBeginGenOpt(58, 1, var133[0].first)
val var135 = history[var133[0].second].findByBeginGenOpt(92, 1, var133[0].first)
check(hasSingleTrue(var134 != null, var135 != null))
val var136 = when {
var134 != null -> null
else -> source[var133[0].first]
}
val var137 = unrollRepeat1(history, 109, 52, 52, 110, var133[2].first, var133[2].second).map { k ->
val var138 = matchOct(k.first, k.second)
var138
}
val var139 = OctValue(var136, var137.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var139
}

fun matchDecValue(beginGen: Int, endGen: Int): DecValue {
val var140 = getSequenceElems(history, 90, listOf(91,93,99,103), beginGen, endGen)
val var141 = history[var140[0].second].findByBeginGenOpt(58, 1, var140[0].first)
val var142 = history[var140[0].second].findByBeginGenOpt(92, 1, var140[0].first)
check(hasSingleTrue(var141 != null, var142 != null))
val var143 = when {
var141 != null -> null
else -> source[var140[0].first]
}
val var144 = matchDec(var140[1].first, var140[1].second)
val var145 = history[var140[2].second].findByBeginGenOpt(58, 1, var140[2].first)
val var146 = history[var140[2].second].findByBeginGenOpt(100, 1, var140[2].first)
check(hasSingleTrue(var145 != null, var146 != null))
val var147 = when {
var145 != null -> null
else -> {
val var148 = getSequenceElems(history, 101, listOf(102,93), var140[2].first, var140[2].second)
val var149 = matchDec(var148[1].first, var148[1].second)
var149
}
}
val var150 = history[var140[3].second].findByBeginGenOpt(58, 1, var140[3].first)
val var151 = history[var140[3].second].findByBeginGenOpt(104, 1, var140[3].first)
check(hasSingleTrue(var150 != null, var151 != null))
val var152 = when {
var150 != null -> null
else -> {
val var153 = getSequenceElems(history, 105, listOf(106,91,93), var140[3].first, var140[3].second)
val var154 = matchDec(var153[2].first, var153[2].second)
var154
}
}
val var155 = DecValue(var143, var144, var147, var152, nextId(), beginGen, endGen)
return var155
}

fun matchDec(beginGen: Int, endGen: Int): String {
val var156 = history[endGen].findByBeginGenOpt(68, 1, beginGen)
val var157 = history[endGen].findByBeginGenOpt(94, 2, beginGen)
check(hasSingleTrue(var156 != null, var157 != null))
val var158 = when {
var156 != null -> "0"
else -> {
val var159 = getSequenceElems(history, 94, listOf(95,96), beginGen, endGen)
val var160 = unrollRepeat0(history, 96, 98, 6, 97, var159[1].first, var159[1].second).map { k ->
source[k.first]
}
source[var159[0].first].toString() + var160.joinToString("") { it.toString() }
}
}
return var158
}

fun matchHexValue(beginGen: Int, endGen: Int): HexValue {
val var161 = getSequenceElems(history, 112, listOf(91,68,60,113), beginGen, endGen)
val var162 = history[var161[0].second].findByBeginGenOpt(58, 1, var161[0].first)
val var163 = history[var161[0].second].findByBeginGenOpt(92, 1, var161[0].first)
check(hasSingleTrue(var162 != null, var163 != null))
val var164 = when {
var162 != null -> null
else -> source[var161[0].first]
}
val var165 = unrollRepeat1(history, 113, 61, 61, 114, var161[3].first, var161[3].second).map { k ->
val var166 = matchHex(k.first, k.second)
var166
}
val var167 = HexValue(var164, var165.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var167
}

fun matchItemValue(beginGen: Int, endGen: Int): ItemValue {
val var168 = history[endGen].findByBeginGenOpt(27, 1, beginGen)
val var169 = history[endGen].findByBeginGenOpt(30, 1, beginGen)
val var170 = history[endGen].findByBeginGenOpt(34, 1, beginGen)
val var171 = history[endGen].findByBeginGenOpt(38, 3, beginGen)
check(hasSingleTrue(var168 != null, var169 != null, var170 != null, var171 != null))
val var172 = when {
var168 != null -> {
val var173 = ObjectHeader(nextId(), beginGen, endGen)
var173
}
var169 != null -> {
val var174 = RepeatedHeader(nextId(), beginGen, endGen)
var174
}
var170 != null -> {
val var175 = MapHeader(nextId(), beginGen, endGen)
var175
}
else -> {
val var176 = getSequenceElems(history, 38, listOf(29,16,39), beginGen, endGen)
val var177 = matchValue(var176[2].first, var176[2].second)
var177
}
}
return var172
}

fun matchKey(beginGen: Int, endGen: Int): Key {
val var178 = history[endGen].findByBeginGenOpt(20, 1, beginGen)
val var179 = history[endGen].findByBeginGenOpt(133, 1, beginGen)
check(hasSingleTrue(var178 != null, var179 != null))
val var180 = when {
var178 != null -> {
val var181 = matchName(beginGen, endGen)
val var182 = NameKey(var181, nextId(), beginGen, endGen)
var182
}
else -> {
val var183 = matchKeyValue(beginGen, endGen)
var183
}
}
return var180
}

fun matchKeyValue(beginGen: Int, endGen: Int): KeyValue {
val var184 = history[endGen].findByBeginGenOpt(42, 1, beginGen)
val var185 = history[endGen].findByBeginGenOpt(88, 1, beginGen)
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

fun matchStringTypeAnnot(beginGen: Int, endGen: Int): StringTypeAnnot {
val var189 = history[endGen].findByBeginGenOpt(84, 1, beginGen)
val var190 = history[endGen].findByBeginGenOpt(85, 1, beginGen)
val var191 = history[endGen].findByBeginGenOpt(86, 1, beginGen)
val var192 = history[endGen].findByBeginGenOpt(87, 1, beginGen)
check(hasSingleTrue(var189 != null, var190 != null, var191 != null, var192 != null))
val var193 = when {
var189 != null -> StringTypeAnnot.Base64
var190 != null -> StringTypeAnnot.Hex
var191 != null -> StringTypeAnnot.Timestamp
else -> StringTypeAnnot.Duration
}
return var193
}

fun matchRepeatedValue(beginGen: Int, endGen: Int): RepeatedValue {
val var194 = history[endGen].findByBeginGenOpt(117, 3, beginGen)
val var195 = history[endGen].findByBeginGenOpt(118, 7, beginGen)
check(hasSingleTrue(var194 != null, var195 != null))
val var196 = when {
var194 != null -> {
val var197 = RepeatedValue(listOf(), nextId(), beginGen, endGen)
var197
}
else -> {
val var198 = getSequenceElems(history, 118, listOf(32,4,39,119,124,4,33), beginGen, endGen)
val var199 = matchValue(var198[2].first, var198[2].second)
val var200 = unrollRepeat0(history, 119, 121, 6, 120, var198[3].first, var198[3].second).map { k ->
val var201 = getSequenceElems(history, 122, listOf(4,123,4,39), k.first, k.second)
val var202 = matchValue(var201[3].first, var201[3].second)
var202
}
val var203 = RepeatedValue(listOf(var199) + var200, nextId(), beginGen, endGen)
var203
}
}
return var196
}

}
