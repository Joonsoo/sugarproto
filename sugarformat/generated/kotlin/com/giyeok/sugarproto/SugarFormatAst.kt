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
  val fracs: List<StringFrac>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Value, AstNode

data class StringFrac(
  val elems: List<StringElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Key, AstNode

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

data class RepeatedHeader(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemValue, AstNode

sealed interface NumberValue: Key, Value, AstNode

data class ObjectHeader(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ItemValue, AstNode

sealed interface ItemValue: AstNode

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

data class Item(
  val indent: String,
  val itemHead: String,
  val key: Key,
  val value: ItemValue,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode


fun matchStart(): List<Item> {
  val lastGen = source.length
  val kernel = history[lastGen].getSingle(2, 1, 0, lastGen)
  return matchItems(kernel.beginGen, kernel.endGen)
}

fun matchItems(beginGen: Int, endGen: Int): List<Item> {
val var1 = getSequenceElems(history, 3, listOf(4,9), beginGen, endGen)
val var2 = unrollRepeat0(history, 9, 11, 6, 10, var1[1].first, var1[1].second).map { k ->
val var3 = getSequenceElems(history, 12, listOf(13,15,134), k.first, k.second)
val var4 = matchItem(var3[0].first, var3[0].second)
var4
}
return var2
}

fun matchItem(beginGen: Int, endGen: Int): Item {
val var5 = getSequenceElems(history, 14, listOf(15,19,24,15,93), beginGen, endGen)
val var6 = matchWS1(var5[0].first, var5[0].second)
val var7 = history[var5[1].second].findByBeginGenOpt(20, 1, var5[1].first)
val var8 = history[var5[1].second].findByBeginGenOpt(23, 1, var5[1].first)
check(hasSingleTrue(var7 != null, var8 != null))
val var9 = when {
var7 != null -> {
val var10 = getSequenceElems(history, 21, listOf(22,15), var5[1].first, var5[1].second)
val var11 = matchWS1(var10[1].first, var10[1].second)
var11
}
else -> null
}
val var12 = matchKey(var5[2].first, var5[2].second)
val var13 = matchItemValue(var5[4].first, var5[4].second)
val var14 = Item(var6.joinToString("") { it.toString() }, (var9?.let { it.joinToString("") { it.toString() } } ?: ""), var12, var13, nextId(), beginGen, endGen)
return var14
}

fun matchWS1(beginGen: Int, endGen: Int): List<Char> {
val var15 = unrollRepeat0(history, 16, 18, 6, 17, beginGen, endGen).map { k ->
source[k.first]
}
return var15
}

fun matchItemValue(beginGen: Int, endGen: Int): ItemValue {
val var16 = history[endGen].findByBeginGenOpt(94, 1, beginGen)
val var17 = history[endGen].findByBeginGenOpt(97, 1, beginGen)
val var18 = history[endGen].findByBeginGenOpt(101, 1, beginGen)
val var19 = history[endGen].findByBeginGenOpt(105, 3, beginGen)
check(hasSingleTrue(var16 != null, var17 != null, var18 != null, var19 != null))
val var20 = when {
var16 != null -> {
val var21 = ObjectHeader(nextId(), beginGen, endGen)
var21
}
var17 != null -> {
val var22 = RepeatedHeader(nextId(), beginGen, endGen)
var22
}
var18 != null -> {
val var23 = MapHeader(nextId(), beginGen, endGen)
var23
}
else -> {
val var24 = getSequenceElems(history, 105, listOf(96,15,106), beginGen, endGen)
val var25 = matchValue(var24[2].first, var24[2].second)
var25
}
}
return var20
}

fun matchValue(beginGen: Int, endGen: Int): Value {
val var26 = history[endGen].findByBeginGenOpt(66, 1, beginGen)
val var27 = history[endGen].findByBeginGenOpt(107, 1, beginGen)
val var28 = history[endGen].findByBeginGenOpt(113, 1, beginGen)
val var29 = history[endGen].findByBeginGenOpt(114, 1, beginGen)
val var30 = history[endGen].findByBeginGenOpt(125, 1, beginGen)
check(hasSingleTrue(var26 != null, var27 != null, var28 != null, var29 != null, var30 != null))
val var31 = when {
var26 != null -> {
val var32 = matchNumberValue(beginGen, endGen)
var32
}
var27 != null -> {
val var33 = matchStringValue(beginGen, endGen)
var33
}
var28 != null -> {
val var34 = matchEnumValue(beginGen, endGen)
var34
}
var29 != null -> {
val var35 = matchRepeatedValue(beginGen, endGen)
var35
}
else -> {
val var36 = matchObjectValue(beginGen, endGen)
var36
}
}
return var31
}

fun matchStringValue(beginGen: Int, endGen: Int): StringValue {
val var37 = getSequenceElems(history, 108, listOf(31,109), beginGen, endGen)
val var38 = matchStringFrac(var37[0].first, var37[0].second)
val var39 = unrollRepeat0(history, 109, 111, 6, 110, var37[1].first, var37[1].second).map { k ->
val var40 = getSequenceElems(history, 112, listOf(4,31), k.first, k.second)
val var41 = matchStringFrac(var40[1].first, var40[1].second)
var41
}
val var42 = StringValue(listOf(var38) + var39, nextId(), beginGen, endGen)
return var42
}

fun matchStringFrac(beginGen: Int, endGen: Int): StringFrac {
val var43 = getSequenceElems(history, 32, listOf(33,34,33), beginGen, endGen)
val var44 = unrollRepeat0(history, 34, 36, 6, 35, var43[1].first, var43[1].second).map { k ->
val var45 = matchStringElem(k.first, k.second)
var45
}
val var46 = StringFrac(var44, nextId(), beginGen, endGen)
return var46
}

fun matchRepeatedValue(beginGen: Int, endGen: Int): RepeatedValue {
val var47 = history[endGen].findByBeginGenOpt(115, 3, beginGen)
val var48 = history[endGen].findByBeginGenOpt(116, 7, beginGen)
check(hasSingleTrue(var47 != null, var48 != null))
val var49 = when {
var47 != null -> {
val var50 = RepeatedValue(listOf(), nextId(), beginGen, endGen)
var50
}
else -> {
val var51 = getSequenceElems(history, 116, listOf(99,4,106,117,122,4,100), beginGen, endGen)
val var52 = matchValue(var51[2].first, var51[2].second)
val var53 = unrollRepeat0(history, 117, 119, 6, 118, var51[3].first, var51[3].second).map { k ->
val var54 = getSequenceElems(history, 120, listOf(4,121,4,106), k.first, k.second)
val var55 = matchValue(var54[3].first, var54[3].second)
var55
}
val var56 = RepeatedValue(listOf(var52) + var53, nextId(), beginGen, endGen)
var56
}
}
return var49
}

fun matchStringElem(beginGen: Int, endGen: Int): StringElem {
val var57 = history[endGen].findByBeginGenOpt(37, 2, beginGen)
val var58 = history[endGen].findByBeginGenOpt(40, 3, beginGen)
val var59 = history[endGen].findByBeginGenOpt(47, 4, beginGen)
val var60 = history[endGen].findByBeginGenOpt(52, 6, beginGen)
val var61 = history[endGen].findByBeginGenOpt(54, 10, beginGen)
val var62 = history[endGen].findByBeginGenOpt(57, 10, beginGen)
val var63 = history[endGen].findByBeginGenOpt(59, 1, beginGen)
check(hasSingleTrue(var57 != null, var58 != null, var59 != null, var60 != null, var61 != null, var62 != null, var63 != null))
val var64 = when {
var57 != null -> {
val var65 = getSequenceElems(history, 37, listOf(38,39), beginGen, endGen)
val var66 = EscapeCode(source[var65[1].first], nextId(), beginGen, endGen)
var66
}
var58 != null -> {
val var67 = getSequenceElems(history, 40, listOf(38,41,43), beginGen, endGen)
val var68 = matchOct(var67[1].first, var67[1].second)
val var69 = history[var67[2].second].findByBeginGenOpt(23, 1, var67[2].first)
val var70 = history[var67[2].second].findByBeginGenOpt(44, 1, var67[2].first)
check(hasSingleTrue(var69 != null, var70 != null))
val var71 = when {
var69 != null -> null
else -> {
val var72 = getSequenceElems(history, 45, listOf(41,46), var67[2].first, var67[2].second)
val var73 = history[var72[1].second].findByBeginGenOpt(23, 1, var72[1].first)
val var74 = history[var72[1].second].findByBeginGenOpt(41, 1, var72[1].first)
check(hasSingleTrue(var73 != null, var74 != null))
val var75 = when {
var73 != null -> null
else -> {
val var76 = matchOct(var72[1].first, var72[1].second)
var76
}
}
var75
}
}
val var77 = OctCode(var68.toString() + (var71?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var77
}
var59 != null -> {
val var78 = getSequenceElems(history, 47, listOf(38,48,49,51), beginGen, endGen)
val var79 = matchHex(var78[2].first, var78[2].second)
val var80 = history[var78[3].second].findByBeginGenOpt(23, 1, var78[3].first)
val var81 = history[var78[3].second].findByBeginGenOpt(49, 1, var78[3].first)
check(hasSingleTrue(var80 != null, var81 != null))
val var82 = when {
var80 != null -> null
else -> {
val var83 = matchHex(var78[3].first, var78[3].second)
var83
}
}
val var84 = HexCode(var79.toString() + (var82?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
var84
}
var60 != null -> {
val var85 = getSequenceElems(history, 52, listOf(38,53,49,49,49,49), beginGen, endGen)
val var86 = matchHex(var85[2].first, var85[2].second)
val var87 = matchHex(var85[3].first, var85[3].second)
val var88 = matchHex(var85[4].first, var85[4].second)
val var89 = matchHex(var85[5].first, var85[5].second)
val var90 = Unicode(var86.toString() + var87.toString() + var88.toString() + var89.toString(), nextId(), beginGen, endGen)
var90
}
var61 != null -> {
val var91 = getSequenceElems(history, 54, listOf(38,55,56,56,56,49,49,49,49,49), beginGen, endGen)
val var92 = matchHex(var91[5].first, var91[5].second)
val var93 = matchHex(var91[6].first, var91[6].second)
val var94 = matchHex(var91[7].first, var91[7].second)
val var95 = matchHex(var91[8].first, var91[8].second)
val var96 = matchHex(var91[9].first, var91[9].second)
val var97 = Unicode(source[var91[2].first].toString() + source[var91[3].first].toString() + source[var91[4].first].toString() + var92.toString() + var93.toString() + var94.toString() + var95.toString() + var96.toString(), nextId(), beginGen, endGen)
var97
}
var62 != null -> {
val var98 = getSequenceElems(history, 57, listOf(38,55,56,56,58,56,49,49,49,49), beginGen, endGen)
val var99 = matchHex(var98[6].first, var98[6].second)
val var100 = matchHex(var98[7].first, var98[7].second)
val var101 = matchHex(var98[8].first, var98[8].second)
val var102 = matchHex(var98[9].first, var98[9].second)
val var103 = Unicode(source[var98[2].first].toString() + source[var98[3].first].toString() + source[var98[4].first].toString() + source[var98[5].first].toString() + var99.toString() + var100.toString() + var101.toString() + var102.toString(), nextId(), beginGen, endGen)
var103
}
else -> {
val var104 = unrollRepeat1(history, 60, 61, 61, 65, beginGen, endGen).map { k ->
source[k.first]
}
val var105 = PlainText(var104.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
var105
}
}
return var64
}

fun matchHex(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchOct(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchObjectValue(beginGen: Int, endGen: Int): ObjectValue {
val var106 = history[endGen].findByBeginGenOpt(126, 3, beginGen)
val var107 = history[endGen].findByBeginGenOpt(127, 6, beginGen)
check(hasSingleTrue(var106 != null, var107 != null))
val var108 = when {
var106 != null -> {
val var109 = ObjectValue(listOf(), nextId(), beginGen, endGen)
var109
}
else -> {
val var110 = getSequenceElems(history, 127, listOf(103,4,128,130,4,104), beginGen, endGen)
val var111 = matchKeyValuePair(var110[2].first, var110[2].second)
val var112 = unrollRepeat0(history, 130, 132, 6, 131, var110[3].first, var110[3].second).map { k ->
val var113 = getSequenceElems(history, 133, listOf(122,4,128), k.first, k.second)
val var114 = matchKeyValuePair(var113[2].first, var113[2].second)
var114
}
val var115 = ObjectValue(listOf(var111) + var112, nextId(), beginGen, endGen)
var115
}
}
return var108
}

fun matchEnumValue(beginGen: Int, endGen: Int): EnumValue {
val var116 = matchName(beginGen, endGen)
val var117 = EnumValue(var116, nextId(), beginGen, endGen)
return var117
}

fun matchName(beginGen: Int, endGen: Int): String {
val var118 = getSequenceElems(history, 26, listOf(27,28), beginGen, endGen)
val var119 = unrollRepeat0(history, 28, 30, 6, 29, var118[1].first, var118[1].second).map { k ->
source[k.first]
}
return source[var118[0].first].toString() + var119.joinToString("") { it.toString() }
}

fun matchKeyValuePair(beginGen: Int, endGen: Int): KeyValuePair {
val var120 = getSequenceElems(history, 129, listOf(24,4,96,4,106), beginGen, endGen)
val var121 = matchKey(var120[0].first, var120[0].second)
val var122 = matchValue(var120[4].first, var120[4].second)
val var123 = KeyValuePair(var121, var122, nextId(), beginGen, endGen)
return var123
}

fun matchNumberValue(beginGen: Int, endGen: Int): NumberValue {
val var124 = history[endGen].findByBeginGenOpt(67, 1, beginGen)
val var125 = history[endGen].findByBeginGenOpt(85, 1, beginGen)
val var126 = history[endGen].findByBeginGenOpt(89, 1, beginGen)
check(hasSingleTrue(var124 != null, var125 != null, var126 != null))
val var127 = when {
var124 != null -> {
val var128 = matchDecValue(beginGen, endGen)
var128
}
var125 != null -> {
val var129 = matchOctValue(beginGen, endGen)
var129
}
else -> {
val var130 = matchHexValue(beginGen, endGen)
var130
}
}
return var127
}

fun matchOctValue(beginGen: Int, endGen: Int): OctValue {
val var131 = getSequenceElems(history, 86, listOf(69,56,87), beginGen, endGen)
val var132 = history[var131[0].second].findByBeginGenOpt(23, 1, var131[0].first)
val var133 = history[var131[0].second].findByBeginGenOpt(70, 1, var131[0].first)
check(hasSingleTrue(var132 != null, var133 != null))
val var134 = when {
var132 != null -> null
else -> source[var131[0].first]
}
val var135 = unrollRepeat1(history, 87, 41, 41, 88, var131[2].first, var131[2].second).map { k ->
val var136 = matchOct(k.first, k.second)
var136
}
val var137 = OctValue(var134, var135.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var137
}

fun matchDecValue(beginGen: Int, endGen: Int): DecValue {
val var138 = getSequenceElems(history, 68, listOf(69,71,77,81), beginGen, endGen)
val var139 = history[var138[0].second].findByBeginGenOpt(23, 1, var138[0].first)
val var140 = history[var138[0].second].findByBeginGenOpt(70, 1, var138[0].first)
check(hasSingleTrue(var139 != null, var140 != null))
val var141 = when {
var139 != null -> null
else -> source[var138[0].first]
}
val var142 = matchDec(var138[1].first, var138[1].second)
val var143 = history[var138[2].second].findByBeginGenOpt(23, 1, var138[2].first)
val var144 = history[var138[2].second].findByBeginGenOpt(78, 1, var138[2].first)
check(hasSingleTrue(var143 != null, var144 != null))
val var145 = when {
var143 != null -> null
else -> {
val var146 = getSequenceElems(history, 79, listOf(80,71), var138[2].first, var138[2].second)
val var147 = matchDec(var146[1].first, var146[1].second)
var147
}
}
val var148 = history[var138[3].second].findByBeginGenOpt(23, 1, var138[3].first)
val var149 = history[var138[3].second].findByBeginGenOpt(82, 1, var138[3].first)
check(hasSingleTrue(var148 != null, var149 != null))
val var150 = when {
var148 != null -> null
else -> {
val var151 = getSequenceElems(history, 83, listOf(84,69,71), var138[3].first, var138[3].second)
val var152 = matchDec(var151[2].first, var151[2].second)
var152
}
}
val var153 = DecValue(var141, var142, var145, var150, nextId(), beginGen, endGen)
return var153
}

fun matchDec(beginGen: Int, endGen: Int): String {
val var154 = history[endGen].findByBeginGenOpt(56, 1, beginGen)
val var155 = history[endGen].findByBeginGenOpt(72, 2, beginGen)
check(hasSingleTrue(var154 != null, var155 != null))
val var156 = when {
var154 != null -> "0"
else -> {
val var157 = getSequenceElems(history, 72, listOf(73,74), beginGen, endGen)
val var158 = unrollRepeat0(history, 74, 76, 6, 75, var157[1].first, var157[1].second).map { k ->
source[k.first]
}
source[var157[0].first].toString() + var158.joinToString("") { it.toString() }
}
}
return var156
}

fun matchHexValue(beginGen: Int, endGen: Int): HexValue {
val var159 = getSequenceElems(history, 90, listOf(69,56,48,91), beginGen, endGen)
val var160 = history[var159[0].second].findByBeginGenOpt(23, 1, var159[0].first)
val var161 = history[var159[0].second].findByBeginGenOpt(70, 1, var159[0].first)
check(hasSingleTrue(var160 != null, var161 != null))
val var162 = when {
var160 != null -> null
else -> source[var159[0].first]
}
val var163 = unrollRepeat1(history, 91, 49, 49, 92, var159[3].first, var159[3].second).map { k ->
val var164 = matchHex(k.first, k.second)
var164
}
val var165 = HexValue(var162, var163.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var165
}

fun matchKey(beginGen: Int, endGen: Int): Key {
val var166 = history[endGen].findByBeginGenOpt(25, 1, beginGen)
val var167 = history[endGen].findByBeginGenOpt(31, 1, beginGen)
val var168 = history[endGen].findByBeginGenOpt(66, 1, beginGen)
check(hasSingleTrue(var166 != null, var167 != null, var168 != null))
val var169 = when {
var166 != null -> {
val var170 = matchName(beginGen, endGen)
val var171 = NameKey(var170, nextId(), beginGen, endGen)
var171
}
var167 != null -> {
val var172 = matchStringFrac(beginGen, endGen)
var172
}
else -> {
val var173 = matchNumberValue(beginGen, endGen)
var173
}
}
return var169
}

}
