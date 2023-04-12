package com.giyeok.sugarproto

import com.giyeok.jparser.ktlib.*

class SugarProtoAst(
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

data class FullIdent(
  val names: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode

data class RpcDef(
  val name: Ident,
  val inType: Type,
  val outType: Type,
  val wheres: RpcTypeWheres?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class OnTheFlyMessageType(
  val name: Ident?,
  val fields: List<MessageMemberDef>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

data class UnicodeLongEscape(
  val code: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

data class DecimalLiteral(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLiteral, AstNode

data class PlainChar(
  val value: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

sealed interface OneOfMemberDef: AstNode

data class OctalLiteral(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLiteral, AstNode

data class RpcTypeWhere(
  val name: Ident,
  val typ: Type,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class ServiceDef(
  val name: Ident,
  val rpcs: List<RpcDef>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): TopLevelDef, AstNode

data class StringLiteralSingle(
  val value: List<CharValue>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

sealed interface Constant: AstNode

data class Inf(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AbstractFloatLiteral, AstNode

data class MultiName(
  val names: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): TypeName, AstNode

data class SingleName(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): TypeName, AstNode

data class Nan(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AbstractFloatLiteral, AstNode

data class ZeroIntLiteral(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLiteral, AstNode

sealed interface TypeName: Type, AstNode

data class OneOfDef(
  val name: Ident,
  val members: List<OneOfMemberDef>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageMemberDef, AstNode

data class FieldOption(
  val name: OptionName,
  val value: Constant,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class FloatLiteral(
  val intPart: String?,
  val fracPart: String?,
  val exp: Exponent?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AbstractFloatLiteral, AstNode

data class OptionDef(
  val name: OptionName,
  val value: Constant,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): OneOfMemberDef, AstNode

data class CompilationUnit(
  val pkgDef: FullIdent?,
  val imports: List<StringLiteral>,
  val options: List<OptionDef>,
  val defs: List<TopLevelDef>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class BoolLiteral(
  val value: BoolValueEnum,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode

data class CharEscape(
  val code: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

data class RepeatedType(
  val elemType: Type,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

data class StringLiteral(
  val singles: List<StringLiteralSingle>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode

data class StreamType(
  val elemType: Type,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

data class OptionName(
  val name: FullIdent,
  val trailings: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class MessageDef(
  val name: Ident,
  val members: List<MessageMemberDef>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageMemberDef, TopLevelDef, AstNode

sealed interface AbstractFloatLiteral: Constant, AstNode

sealed interface CharValue: AstNode

sealed interface SealedMemberDef: AstNode

data class RpcTypeWheres(
  val wheres: List<RpcTypeWhere>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class OnTheFlySealedMessageType(
  val name: Ident?,
  val fields: List<SealedMemberDef>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

data class FieldOptions(
  val options: List<FieldOption>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

sealed interface MessageMemberDef: AstNode

data class PrimitiveType(
  val typ: PrimitiveTypeEnum,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

data class OptionalType(
  val elemType: Type,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

data class Exponent(
  val sign: Char?,
  val exp: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class HexLiteral(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLiteral, AstNode

data class SealedDef(
  val name: Ident,
  val members: List<SealedMemberDef>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): TopLevelDef, AstNode

data class FieldDef(
  val tag: IntLiteral,
  val name: Ident,
  val typ: Type,
  val options: FieldOptions?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageMemberDef, OneOfMemberDef, SealedMemberDef, AstNode

sealed interface IntLiteral: Constant, AstNode

data class OctEscape(
  val code: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

data class UnicodeEscape(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

data class Ident(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

sealed interface TopLevelDef: AstNode

data class HexEscape(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

sealed interface Type: AstNode

data class MapType(
  val keyType: Type,
  val valueType: Type,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode
enum class BoolValueEnum { FALSE, TRUE }
enum class PrimitiveTypeEnum { BOOL, BYTES, DOUBLE, FIXED32, FIXED64, FLOAT, INT32, INT64, SFIXED32, SFIXED64, SINT32, SINT64, STRING, UINT32, UINT64 }

fun matchStart(): CompilationUnit {
  val lastGen = source.length
  val kernel = history[lastGen].getSingle(2, 1, 0, lastGen)
  return matchCompilationUnit(kernel.beginGen, kernel.endGen)
}

fun matchCompilationUnit(beginGen: Int, endGen: Int): CompilationUnit {
val var1 = getSequenceElems(history, 3, listOf(4,83,149,224,7), beginGen, endGen)
val var2 = history[var1[0].second].findByBeginGenOpt(5, 1, var1[0].first)
val var3 = history[var1[0].second].findByBeginGenOpt(82, 1, var1[0].first)
check(hasSingleTrue(var2 != null, var3 != null))
val var4 = when {
var2 != null -> {
val var5 = getSequenceElems(history, 6, listOf(7,43), var1[0].first, var1[0].second)
val var6 = matchPackageDef(var5[1].first, var5[1].second)
var6
}
else -> null
}
val var7 = unrollRepeat0(history, 83, 85, 9, 84, var1[1].first, var1[1].second).map { k ->
val var8 = getSequenceElems(history, 86, listOf(7,87), k.first, k.second)
val var9 = matchImportDef(var8[1].first, var8[1].second)
var9
}
val var10 = unrollRepeat0(history, 149, 151, 9, 150, var1[2].first, var1[2].second).map { k ->
val var11 = getSequenceElems(history, 152, listOf(7,153), k.first, k.second)
val var12 = matchOptionDef(var11[1].first, var11[1].second)
var12
}
val var13 = unrollRepeat0(history, 224, 226, 9, 225, var1[3].first, var1[3].second).map { k ->
val var14 = getSequenceElems(history, 227, listOf(7,228), k.first, k.second)
val var15 = matchTopLevelDef(var14[1].first, var14[1].second)
var15
}
val var16 = CompilationUnit(var4, var7, var10, var13, nextId(), beginGen, endGen)
return var16
}

fun matchPackageDef(beginGen: Int, endGen: Int): FullIdent {
val var17 = getSequenceElems(history, 44, listOf(45,7,60), beginGen, endGen)
val var18 = matchFullIdent(var17[2].first, var17[2].second)
return var18
}

fun matchFullIdent(beginGen: Int, endGen: Int): FullIdent {
val var19 = getSequenceElems(history, 61, listOf(62,77), beginGen, endGen)
val var20 = matchIdent(var19[0].first, var19[0].second)
val var21 = unrollRepeat0(history, 77, 79, 9, 78, var19[1].first, var19[1].second).map { k ->
val var22 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var23 = matchIdent(var22[3].first, var22[3].second)
var23
}
val var24 = FullIdent(listOf(var20) + var21, nextId(), beginGen, endGen)
return var24
}

fun matchImportDef(beginGen: Int, endGen: Int): StringLiteral {
val var25 = getSequenceElems(history, 88, listOf(89,7,98), beginGen, endGen)
val var26 = matchStringLiteral(var25[2].first, var25[2].second)
return var26
}

fun matchOptionDef(beginGen: Int, endGen: Int): OptionDef {
val var27 = getSequenceElems(history, 154, listOf(155,7,160,7,167,7,168), beginGen, endGen)
val var28 = matchOptionName(var27[2].first, var27[2].second)
val var29 = matchConstant(var27[6].first, var27[6].second)
val var30 = OptionDef(var28, var29, nextId(), beginGen, endGen)
return var30
}

fun matchConstant(beginGen: Int, endGen: Int): Constant {
val var31 = history[endGen].findByBeginGenOpt(98, 1, beginGen)
val var32 = history[endGen].findByBeginGenOpt(169, 1, beginGen)
val var33 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var34 = history[endGen].findByBeginGenOpt(181, 3, beginGen)
val var35 = history[endGen].findByBeginGenOpt(201, 3, beginGen)
val var36 = history[endGen].findByBeginGenOpt(222, 1, beginGen)
check(hasSingleTrue(var31 != null, var32 != null, var33 != null, var34 != null, var35 != null, var36 != null))
val var37 = when {
var31 != null -> {
val var38 = matchStringLiteral(beginGen, endGen)
var38
}
var32 != null -> {
val var39 = matchIdent(beginGen, endGen)
val var40 = FullIdent(listOf(var39), nextId(), beginGen, endGen)
var40
}
var33 != null -> {
val var41 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var42 = matchIdent(var41[0].first, var41[0].second)
val var43 = unrollRepeat1(history, 179, 79, 79, 180, var41[1].first, var41[1].second).map { k ->
val var44 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var45 = matchIdent(var44[3].first, var44[3].second)
var45
}
val var46 = FullIdent(listOf(var42) + var43, nextId(), beginGen, endGen)
var46
}
var34 != null -> {
val var47 = getSequenceElems(history, 181, listOf(182,7,185), beginGen, endGen)
val var48 = matchIntLiteral(var47[2].first, var47[2].second)
var48
}
var35 != null -> {
val var49 = getSequenceElems(history, 201, listOf(182,7,202), beginGen, endGen)
val var50 = matchFloatLiteral(var49[2].first, var49[2].second)
var50
}
else -> {
val var51 = matchBoolLiteral(beginGen, endGen)
var51
}
}
return var37
}

fun matchFloatLiteral(beginGen: Int, endGen: Int): AbstractFloatLiteral {
val var52 = history[endGen].findByBeginGenOpt(203, 4, beginGen)
val var53 = history[endGen].findByBeginGenOpt(214, 2, beginGen)
val var54 = history[endGen].findByBeginGenOpt(215, 3, beginGen)
val var55 = history[endGen].findByBeginGenOpt(216, 1, beginGen)
val var56 = history[endGen].findByBeginGenOpt(219, 1, beginGen)
check(hasSingleTrue(var52 != null, var53 != null, var54 != null, var55 != null, var56 != null))
val var57 = when {
var52 != null -> {
val var58 = getSequenceElems(history, 203, listOf(204,81,207,208), beginGen, endGen)
val var59 = matchDecimals(var58[0].first, var58[0].second)
val var60 = history[var58[2].second].findByBeginGenOpt(82, 1, var58[2].first)
val var61 = history[var58[2].second].findByBeginGenOpt(204, 1, var58[2].first)
check(hasSingleTrue(var60 != null, var61 != null))
val var62 = when {
var60 != null -> null
else -> {
val var63 = matchDecimals(var58[2].first, var58[2].second)
var63
}
}
val var64 = history[var58[3].second].findByBeginGenOpt(82, 1, var58[3].first)
val var65 = history[var58[3].second].findByBeginGenOpt(209, 1, var58[3].first)
check(hasSingleTrue(var64 != null, var65 != null))
val var66 = when {
var64 != null -> null
else -> {
val var67 = matchExponent(var58[3].first, var58[3].second)
var67
}
}
val var68 = FloatLiteral(var59, var62, var66, nextId(), beginGen, endGen)
var68
}
var53 != null -> {
val var69 = getSequenceElems(history, 214, listOf(204,209), beginGen, endGen)
val var70 = matchDecimals(var69[0].first, var69[0].second)
val var71 = matchExponent(var69[1].first, var69[1].second)
val var72 = FloatLiteral(var70, null, var71, nextId(), beginGen, endGen)
var72
}
var54 != null -> {
val var73 = getSequenceElems(history, 215, listOf(81,204,208), beginGen, endGen)
val var74 = matchDecimals(var73[1].first, var73[1].second)
val var75 = history[var73[2].second].findByBeginGenOpt(82, 1, var73[2].first)
val var76 = history[var73[2].second].findByBeginGenOpt(209, 1, var73[2].first)
check(hasSingleTrue(var75 != null, var76 != null))
val var77 = when {
var75 != null -> null
else -> {
val var78 = matchExponent(var73[2].first, var73[2].second)
var78
}
}
val var79 = FloatLiteral(null, var74, var77, nextId(), beginGen, endGen)
var79
}
var55 != null -> {
val var80 = Inf(nextId(), beginGen, endGen)
var80
}
else -> {
val var81 = Nan(nextId(), beginGen, endGen)
var81
}
}
return var57
}

fun matchBoolLiteral(beginGen: Int, endGen: Int): BoolLiteral {
val var82 = history[endGen].findByBeginGenOpt(171, 1, beginGen)
val var83 = history[endGen].findByBeginGenOpt(173, 1, beginGen)
check(hasSingleTrue(var82 != null, var83 != null))
val var84 = when {
var82 != null -> BoolValueEnum.TRUE
else -> BoolValueEnum.FALSE
}
val var85 = BoolLiteral(var84, nextId(), beginGen, endGen)
return var85
}

fun matchStringLiteral(beginGen: Int, endGen: Int): StringLiteral {
val var86 = getSequenceElems(history, 99, listOf(100,145), beginGen, endGen)
val var87 = matchStringLiteralSingle(var86[0].first, var86[0].second)
val var88 = unrollRepeat0(history, 145, 147, 9, 146, var86[1].first, var86[1].second).map { k ->
val var89 = getSequenceElems(history, 148, listOf(7,100), k.first, k.second)
val var90 = matchStringLiteralSingle(var89[1].first, var89[1].second)
var90
}
val var91 = StringLiteral(listOf(var87) + var88, nextId(), beginGen, endGen)
return var91
}

fun matchStringLiteralSingle(beginGen: Int, endGen: Int): StringLiteralSingle {
val var92 = history[endGen].findByBeginGenOpt(101, 3, beginGen)
val var93 = history[endGen].findByBeginGenOpt(143, 3, beginGen)
check(hasSingleTrue(var92 != null, var93 != null))
val var94 = when {
var92 != null -> {
val var95 = getSequenceElems(history, 101, listOf(102,103,102), beginGen, endGen)
val var96 = unrollRepeat0(history, 103, 105, 9, 104, var95[1].first, var95[1].second).map { k ->
val var97 = matchCharValue(k.first, k.second)
var97
}
val var98 = StringLiteralSingle(var96, nextId(), beginGen, endGen)
var98
}
else -> {
val var99 = getSequenceElems(history, 143, listOf(144,103,144), beginGen, endGen)
val var100 = unrollRepeat0(history, 103, 105, 9, 104, var99[1].first, var99[1].second).map { k ->
val var101 = matchCharValue(k.first, k.second)
var101
}
val var102 = StringLiteralSingle(var100, nextId(), beginGen, endGen)
var102
}
}
return var94
}

fun matchOptionName(beginGen: Int, endGen: Int): OptionName {
val var103 = getSequenceElems(history, 161, listOf(162,77), beginGen, endGen)
val var104 = history[var103[0].second].findByBeginGenOpt(62, 1, var103[0].first)
val var105 = history[var103[0].second].findByBeginGenOpt(163, 1, var103[0].first)
check(hasSingleTrue(var104 != null, var105 != null))
val var106 = when {
var104 != null -> {
val var107 = matchIdent(var103[0].first, var103[0].second)
val var108 = FullIdent(listOf(var107), nextId(), var103[0].first, var103[0].second)
var108
}
else -> {
val var109 = getSequenceElems(history, 164, listOf(165,7,60,7,166), var103[0].first, var103[0].second)
val var110 = matchFullIdent(var109[2].first, var109[2].second)
var110
}
}
val var111 = unrollRepeat0(history, 77, 79, 9, 78, var103[1].first, var103[1].second).map { k ->
val var112 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var113 = matchIdent(var112[3].first, var112[3].second)
var113
}
val var114 = OptionName(var106, var111, nextId(), beginGen, endGen)
return var114
}

fun matchDecimals(beginGen: Int, endGen: Int): String {
val var115 = unrollRepeat1(history, 205, 72, 72, 206, beginGen, endGen).map { k ->
val var116 = matchDecimalDigit(k.first, k.second)
var116
}
return var115.joinToString("") { it.toString() }
}

fun matchDecimalDigit(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchCharValue(beginGen: Int, endGen: Int): CharValue {
val var117 = history[endGen].findByBeginGenOpt(106, 1, beginGen)
val var118 = history[endGen].findByBeginGenOpt(109, 1, beginGen)
val var119 = history[endGen].findByBeginGenOpt(117, 1, beginGen)
val var120 = history[endGen].findByBeginGenOpt(124, 1, beginGen)
val var121 = history[endGen].findByBeginGenOpt(127, 1, beginGen)
val var122 = history[endGen].findByBeginGenOpt(130, 1, beginGen)
check(hasSingleTrue(var117 != null, var118 != null, var119 != null, var120 != null, var121 != null, var122 != null))
val var123 = when {
var117 != null -> {
val var124 = matchPlainChar(beginGen, endGen)
var124
}
var118 != null -> {
val var125 = matchHexEscape(beginGen, endGen)
var125
}
var119 != null -> {
val var126 = matchOctEscape(beginGen, endGen)
var126
}
var120 != null -> {
val var127 = matchCharEscape(beginGen, endGen)
var127
}
var121 != null -> {
val var128 = matchUnicodeEscape(beginGen, endGen)
var128
}
else -> {
val var129 = matchUnicodeLongEscape(beginGen, endGen)
var129
}
}
return var123
}

fun matchCharEscape(beginGen: Int, endGen: Int): CharEscape {
val var130 = getSequenceElems(history, 125, listOf(111,126), beginGen, endGen)
val var131 = CharEscape(source[var130[1].first], nextId(), beginGen, endGen)
return var131
}

fun matchUnicodeLongEscape(beginGen: Int, endGen: Int): UnicodeLongEscape {
val var132 = getSequenceElems(history, 131, listOf(111,129,132), beginGen, endGen)
val var133 = history[var132[2].second].findByBeginGenOpt(133, 1, var132[2].first)
val var134 = history[var132[2].second].findByBeginGenOpt(138, 1, var132[2].first)
check(hasSingleTrue(var133 != null, var134 != null))
val var135 = when {
var133 != null -> {
val var136 = getSequenceElems(history, 134, listOf(135,114,114,114,114,114), var132[2].first, var132[2].second)
val var137 = matchHexDigit(var136[1].first, var136[1].second)
val var138 = matchHexDigit(var136[2].first, var136[2].second)
val var139 = matchHexDigit(var136[3].first, var136[3].second)
val var140 = matchHexDigit(var136[4].first, var136[4].second)
val var141 = matchHexDigit(var136[5].first, var136[5].second)
val var142 = UnicodeLongEscape("000" + var137.toString() + var138.toString() + var139.toString() + var140.toString() + var141.toString(), nextId(), var132[2].first, var132[2].second)
var142
}
else -> {
val var143 = getSequenceElems(history, 139, listOf(140,114,114,114,114), var132[2].first, var132[2].second)
val var144 = matchHexDigit(var143[1].first, var143[1].second)
val var145 = matchHexDigit(var143[2].first, var143[2].second)
val var146 = matchHexDigit(var143[3].first, var143[3].second)
val var147 = matchHexDigit(var143[4].first, var143[4].second)
val var148 = UnicodeLongEscape("0010" + var144.toString() + var145.toString() + var146.toString() + var147.toString(), nextId(), var132[2].first, var132[2].second)
var148
}
}
return var135
}

fun matchPlainChar(beginGen: Int, endGen: Int): PlainChar {
val var149 = PlainChar(source[beginGen], nextId(), beginGen, endGen)
return var149
}

fun matchHexDigit(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchExponent(beginGen: Int, endGen: Int): Exponent {
val var150 = getSequenceElems(history, 210, listOf(211,212,204), beginGen, endGen)
val var151 = history[var150[1].second].findByBeginGenOpt(82, 1, var150[1].first)
val var152 = history[var150[1].second].findByBeginGenOpt(213, 1, var150[1].first)
check(hasSingleTrue(var151 != null, var152 != null))
val var153 = when {
var151 != null -> null
else -> source[var150[1].first]
}
val var154 = matchDecimals(var150[2].first, var150[2].second)
val var155 = Exponent(var153, var154, nextId(), beginGen, endGen)
return var155
}

fun matchHexEscape(beginGen: Int, endGen: Int): HexEscape {
val var156 = getSequenceElems(history, 110, listOf(111,112,113), beginGen, endGen)
val var157 = unrollRepeat1(history, 113, 114, 114, 116, var156[2].first, var156[2].second).map { k ->
val var158 = matchHexDigit(k.first, k.second)
var158
}
val var159 = HexEscape(var157.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var159
}

fun matchIntLiteral(beginGen: Int, endGen: Int): IntLiteral {
val var160 = history[endGen].findByBeginGenOpt(188, 1, beginGen)
val var161 = history[endGen].findByBeginGenOpt(190, 1, beginGen)
val var162 = history[endGen].findByBeginGenOpt(195, 1, beginGen)
val var163 = history[endGen].findByBeginGenOpt(199, 1, beginGen)
check(hasSingleTrue(var160 != null, var161 != null, var162 != null, var163 != null))
val var164 = when {
var160 != null -> {
val var165 = ZeroIntLiteral(nextId(), beginGen, endGen)
var165
}
var161 != null -> {
val var166 = matchDecimalLiteral(beginGen, endGen)
var166
}
var162 != null -> {
val var167 = matchOctalLiteral(beginGen, endGen)
var167
}
else -> {
val var168 = matchHexLiteral(beginGen, endGen)
var168
}
}
return var164
}

fun matchDecimalLiteral(beginGen: Int, endGen: Int): DecimalLiteral {
val var169 = getSequenceElems(history, 191, listOf(192,193), beginGen, endGen)
val var170 = unrollRepeat0(history, 193, 73, 9, 194, var169[1].first, var169[1].second).map { k ->
source[k.first]
}
val var171 = DecimalLiteral(source[var169[0].first].toString() + var170.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var171
}

fun matchOctalLiteral(beginGen: Int, endGen: Int): OctalLiteral {
val var172 = getSequenceElems(history, 196, listOf(137,197), beginGen, endGen)
val var173 = unrollRepeat1(history, 197, 119, 119, 198, var172[1].first, var172[1].second).map { k ->
source[k.first]
}
val var174 = OctalLiteral(var173.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var174
}

fun matchHexLiteral(beginGen: Int, endGen: Int): HexLiteral {
val var175 = getSequenceElems(history, 200, listOf(137,112,113), beginGen, endGen)
val var176 = unrollRepeat1(history, 113, 114, 114, 116, var175[2].first, var175[2].second).map { k ->
val var177 = matchHexDigit(k.first, k.second)
var177
}
val var178 = HexLiteral(var176.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var178
}

fun matchOctEscape(beginGen: Int, endGen: Int): OctEscape {
val var179 = getSequenceElems(history, 118, listOf(111,119,120), beginGen, endGen)
val var180 = history[var179[2].second].findByBeginGenOpt(82, 1, var179[2].first)
val var181 = history[var179[2].second].findByBeginGenOpt(121, 1, var179[2].first)
check(hasSingleTrue(var180 != null, var181 != null))
val var182 = when {
var180 != null -> null
else -> {
val var183 = getSequenceElems(history, 122, listOf(119,123), var179[2].first, var179[2].second)
val var184 = history[var183[1].second].findByBeginGenOpt(82, 1, var183[1].first)
val var185 = history[var183[1].second].findByBeginGenOpt(119, 1, var183[1].first)
check(hasSingleTrue(var184 != null, var185 != null))
val var186 = when {
var184 != null -> null
else -> source[var183[1].first]
}
var186
}
}
val var187 = OctEscape(source[var179[1].first].toString() + (var182?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
return var187
}

fun matchUnicodeEscape(beginGen: Int, endGen: Int): UnicodeEscape {
val var188 = getSequenceElems(history, 128, listOf(111,129,114,114,114,114), beginGen, endGen)
val var189 = matchHexDigit(var188[2].first, var188[2].second)
val var190 = matchHexDigit(var188[3].first, var188[3].second)
val var191 = matchHexDigit(var188[4].first, var188[4].second)
val var192 = matchHexDigit(var188[5].first, var188[5].second)
val var193 = UnicodeEscape(var189.toString() + var190.toString() + var191.toString() + var192.toString(), nextId(), beginGen, endGen)
return var193
}

fun matchIdent(beginGen: Int, endGen: Int): Ident {
val var194 = history[endGen].findByBeginGenOpt(63, 1, beginGen)
val var195 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
check(hasSingleTrue(var194 != null, var195 != null))
val var196 = when {
var194 != null -> {
val var197 = matchIdentName(beginGen, endGen)
val var198 = Ident(var197, nextId(), beginGen, endGen)
var198
}
else -> {
val var199 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var200 = matchIdentName(var199[1].first, var199[1].second)
val var201 = Ident(var200, nextId(), beginGen, endGen)
var201
}
}
return var196
}

fun matchIdentName(beginGen: Int, endGen: Int): String {
val var202 = getSequenceElems(history, 66, listOf(67,69), beginGen, endGen)
val var203 = matchLetter(var202[0].first, var202[0].second)
val var204 = unrollRepeat0(history, 69, 71, 9, 70, var202[1].first, var202[1].second).map { k ->
val var205 = history[k.second].findByBeginGenOpt(67, 1, k.first)
val var206 = history[k.second].findByBeginGenOpt(72, 1, k.first)
val var207 = history[k.second].findByBeginGenOpt(74, 1, k.first)
check(hasSingleTrue(var205 != null, var206 != null, var207 != null))
val var208 = when {
var205 != null -> {
val var209 = matchLetter(k.first, k.second)
var209
}
var206 != null -> {
val var210 = matchDecimalDigit(k.first, k.second)
var210
}
else -> source[k.first]
}
var208
}
return var203.toString() + var204.joinToString("") { it.toString() }
}

fun matchTopLevelDef(beginGen: Int, endGen: Int): TopLevelDef {
val var211 = history[endGen].findByBeginGenOpt(229, 1, beginGen)
val var212 = history[endGen].findByBeginGenOpt(360, 1, beginGen)
val var213 = history[endGen].findByBeginGenOpt(400, 1, beginGen)
check(hasSingleTrue(var211 != null, var212 != null, var213 != null))
val var214 = when {
var211 != null -> {
val var215 = matchServiceDef(beginGen, endGen)
var215
}
var212 != null -> {
val var216 = matchMessageDef(beginGen, endGen)
var216
}
else -> {
val var217 = matchSealedDef(beginGen, endGen)
var217
}
}
return var214
}

fun matchServiceDef(beginGen: Int, endGen: Int): ServiceDef {
val var218 = getSequenceElems(history, 230, listOf(231,7,62,7,236,237,7,359), beginGen, endGen)
val var219 = matchIdent(var218[2].first, var218[2].second)
val var220 = unrollRepeat0(history, 237, 239, 9, 238, var218[5].first, var218[5].second).map { k ->
val var221 = getSequenceElems(history, 240, listOf(7,241), k.first, k.second)
val var222 = matchRpcDef(var221[1].first, var221[1].second)
var222
}
val var223 = ServiceDef(var219, var220, nextId(), beginGen, endGen)
return var223
}

fun matchRpcDef(beginGen: Int, endGen: Int): RpcDef {
val var224 = getSequenceElems(history, 242, listOf(243,7,62,7,247,7,248,7,381,7,248,383), beginGen, endGen)
val var225 = matchIdent(var224[2].first, var224[2].second)
val var226 = matchType(var224[6].first, var224[6].second)
val var227 = matchType(var224[10].first, var224[10].second)
val var228 = history[var224[11].second].findByBeginGenOpt(82, 1, var224[11].first)
val var229 = history[var224[11].second].findByBeginGenOpt(384, 1, var224[11].first)
check(hasSingleTrue(var228 != null, var229 != null))
val var230 = when {
var228 != null -> null
else -> {
val var231 = getSequenceElems(history, 385, listOf(7,386,7,392), var224[11].first, var224[11].second)
val var232 = matchRpcTypeWheres(var231[3].first, var231[3].second)
var232
}
}
val var233 = RpcDef(var225, var226, var227, var230, nextId(), beginGen, endGen)
return var233
}

fun matchMessageDef(beginGen: Int, endGen: Int): MessageDef {
val var234 = getSequenceElems(history, 361, listOf(362,7,62,7,236,325,7,359), beginGen, endGen)
val var235 = matchIdent(var234[2].first, var234[2].second)
val var236 = unrollRepeat0(history, 325, 327, 9, 326, var234[5].first, var234[5].second).map { k ->
val var237 = getSequenceElems(history, 328, listOf(7,329), k.first, k.second)
val var238 = matchMessageMemberDef(var237[1].first, var237[1].second)
var238
}
val var239 = MessageDef(var235, var236, nextId(), beginGen, endGen)
return var239
}

fun matchSealedDef(beginGen: Int, endGen: Int): SealedDef {
val var240 = getSequenceElems(history, 401, listOf(368,7,62,7,236,373,7,359), beginGen, endGen)
val var241 = matchIdent(var240[2].first, var240[2].second)
val var242 = unrollRepeat0(history, 373, 375, 9, 374, var240[5].first, var240[5].second).map { k ->
val var243 = getSequenceElems(history, 376, listOf(7,377), k.first, k.second)
val var244 = matchSealedMemberDef(var243[1].first, var243[1].second)
var244
}
val var245 = SealedDef(var241, var242, nextId(), beginGen, endGen)
return var245
}

fun matchSealedMemberDef(beginGen: Int, endGen: Int): SealedMemberDef {
val var246 = matchFieldDef(beginGen, endGen)
return var246
}

fun matchFieldDef(beginGen: Int, endGen: Int): FieldDef {
val var247 = getSequenceElems(history, 331, listOf(185,7,62,7,247,7,248,332), beginGen, endGen)
val var248 = matchIntLiteral(var247[0].first, var247[0].second)
val var249 = matchIdent(var247[2].first, var247[2].second)
val var250 = matchType(var247[6].first, var247[6].second)
val var251 = history[var247[7].second].findByBeginGenOpt(82, 1, var247[7].first)
val var252 = history[var247[7].second].findByBeginGenOpt(333, 1, var247[7].first)
check(hasSingleTrue(var251 != null, var252 != null))
val var253 = when {
var251 != null -> null
else -> {
val var254 = getSequenceElems(history, 334, listOf(7,335), var247[7].first, var247[7].second)
val var255 = matchFieldOptions(var254[1].first, var254[1].second)
var255
}
}
val var256 = FieldDef(var248, var249, var250, var253, nextId(), beginGen, endGen)
return var256
}

fun matchLetter(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchType(beginGen: Int, endGen: Int): Type {
val var257 = history[endGen].findByBeginGenOpt(249, 1, beginGen)
val var258 = history[endGen].findByBeginGenOpt(289, 1, beginGen)
val var259 = history[endGen].findByBeginGenOpt(297, 1, beginGen)
val var260 = history[endGen].findByBeginGenOpt(303, 1, beginGen)
val var261 = history[endGen].findByBeginGenOpt(310, 1, beginGen)
val var262 = history[endGen].findByBeginGenOpt(316, 1, beginGen)
val var263 = history[endGen].findByBeginGenOpt(366, 1, beginGen)
val var264 = history[endGen].findByBeginGenOpt(378, 1, beginGen)
check(hasSingleTrue(var257 != null, var258 != null, var259 != null, var260 != null, var261 != null, var262 != null, var263 != null, var264 != null))
val var265 = when {
var257 != null -> {
val var266 = matchPrimitiveType(beginGen, endGen)
val var267 = PrimitiveType(var266, nextId(), beginGen, endGen)
var267
}
var258 != null -> {
val var268 = matchRepeatedType(beginGen, endGen)
var268
}
var259 != null -> {
val var269 = matchOptionalType(beginGen, endGen)
var269
}
var260 != null -> {
val var270 = matchMapType(beginGen, endGen)
var270
}
var261 != null -> {
val var271 = matchStreamType(beginGen, endGen)
var271
}
var262 != null -> {
val var272 = matchOnTheFlyMessageType(beginGen, endGen)
var272
}
var263 != null -> {
val var273 = matchOnTheFlySealedMessageType(beginGen, endGen)
var273
}
else -> {
val var274 = matchTypeName(beginGen, endGen)
var274
}
}
return var265
}

fun matchTypeName(beginGen: Int, endGen: Int): TypeName {
val var275 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var276 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var277 = history[endGen].findByBeginGenOpt(379, 1, beginGen)
check(hasSingleTrue(var275 != null, var276 != null, var277 != null))
val var278 = when {
var275 != null -> {
val var279 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var280 = matchIdentName(var279[1].first, var279[1].second)
val var281 = SingleName(var280, nextId(), beginGen, endGen)
var281
}
var276 != null -> {
val var282 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var283 = matchIdent(var282[0].first, var282[0].second)
val var284 = unrollRepeat1(history, 179, 79, 79, 180, var282[1].first, var282[1].second).map { k ->
val var285 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var286 = matchIdent(var285[3].first, var285[3].second)
var286
}
val var287 = MultiName(listOf(var283) + var284, nextId(), beginGen, endGen)
var287
}
else -> {
val var288 = matchIdentName(beginGen, endGen)
val var289 = SingleName(var288, nextId(), beginGen, endGen)
var289
}
}
return var278
}

fun matchOnTheFlyMessageType(beginGen: Int, endGen: Int): OnTheFlyMessageType {
val var290 = getSequenceElems(history, 317, listOf(318,236,325,7,359), beginGen, endGen)
val var291 = history[var290[0].second].findByBeginGenOpt(82, 1, var290[0].first)
val var292 = history[var290[0].second].findByBeginGenOpt(319, 1, var290[0].first)
check(hasSingleTrue(var291 != null, var292 != null))
val var293 = when {
var291 != null -> null
else -> {
val var294 = getSequenceElems(history, 320, listOf(321,7), var290[0].first, var290[0].second)
val var295 = matchIdentNoSealed(var294[0].first, var294[0].second)
var295
}
}
val var296 = unrollRepeat0(history, 325, 327, 9, 326, var290[2].first, var290[2].second).map { k ->
val var297 = getSequenceElems(history, 328, listOf(7,329), k.first, k.second)
val var298 = matchMessageMemberDef(var297[1].first, var297[1].second)
var298
}
val var299 = OnTheFlyMessageType(var293, var296, nextId(), beginGen, endGen)
return var299
}

fun matchMapType(beginGen: Int, endGen: Int): MapType {
val var300 = getSequenceElems(history, 304, listOf(305,7,295,7,248,7,309,7,248,7,296), beginGen, endGen)
val var301 = matchType(var300[4].first, var300[4].second)
val var302 = matchType(var300[8].first, var300[8].second)
val var303 = MapType(var301, var302, nextId(), beginGen, endGen)
return var303
}

fun matchRepeatedType(beginGen: Int, endGen: Int): RepeatedType {
val var304 = getSequenceElems(history, 290, listOf(291,7,295,7,248,7,296), beginGen, endGen)
val var305 = matchType(var304[4].first, var304[4].second)
val var306 = RepeatedType(var305, nextId(), beginGen, endGen)
return var306
}

fun matchIdentNoSealed(beginGen: Int, endGen: Int): Ident {
val var307 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var308 = history[endGen].findByBeginGenOpt(322, 1, beginGen)
check(hasSingleTrue(var307 != null, var308 != null))
val var309 = when {
var307 != null -> {
val var310 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var311 = matchIdentName(var310[1].first, var310[1].second)
val var312 = Ident(var311, nextId(), beginGen, endGen)
var312
}
else -> {
val var313 = matchIdentName(beginGen, endGen)
val var314 = Ident(var313, nextId(), beginGen, endGen)
var314
}
}
return var309
}

fun matchStreamType(beginGen: Int, endGen: Int): StreamType {
val var315 = getSequenceElems(history, 311, listOf(312,7,295,7,248,7,296), beginGen, endGen)
val var316 = matchType(var315[4].first, var315[4].second)
val var317 = StreamType(var316, nextId(), beginGen, endGen)
return var317
}

fun matchRpcTypeWheres(beginGen: Int, endGen: Int): RpcTypeWheres {
val var318 = getSequenceElems(history, 393, listOf(394,396), beginGen, endGen)
val var319 = matchRpcTypeWhere(var318[0].first, var318[0].second)
val var320 = unrollRepeat0(history, 396, 398, 9, 397, var318[1].first, var318[1].second).map { k ->
val var321 = getSequenceElems(history, 399, listOf(7,309,7,394), k.first, k.second)
val var322 = matchRpcTypeWhere(var321[3].first, var321[3].second)
var322
}
val var323 = RpcTypeWheres(listOf(var319) + var320, nextId(), beginGen, endGen)
return var323
}

fun matchRpcTypeWhere(beginGen: Int, endGen: Int): RpcTypeWhere {
val var324 = getSequenceElems(history, 395, listOf(62,7,167,7,248), beginGen, endGen)
val var325 = matchIdent(var324[0].first, var324[0].second)
val var326 = matchType(var324[4].first, var324[4].second)
val var327 = RpcTypeWhere(var325, var326, nextId(), beginGen, endGen)
return var327
}

fun matchOnTheFlySealedMessageType(beginGen: Int, endGen: Int): OnTheFlySealedMessageType {
val var328 = getSequenceElems(history, 367, listOf(368,370,7,236,373,7,359), beginGen, endGen)
val var329 = history[var328[1].second].findByBeginGenOpt(82, 1, var328[1].first)
val var330 = history[var328[1].second].findByBeginGenOpt(371, 1, var328[1].first)
check(hasSingleTrue(var329 != null, var330 != null))
val var331 = when {
var329 != null -> null
else -> {
val var332 = getSequenceElems(history, 372, listOf(7,321), var328[1].first, var328[1].second)
val var333 = matchIdentNoSealed(var332[1].first, var332[1].second)
var333
}
}
val var334 = unrollRepeat0(history, 373, 375, 9, 374, var328[4].first, var328[4].second).map { k ->
val var335 = getSequenceElems(history, 376, listOf(7,377), k.first, k.second)
val var336 = matchSealedMemberDef(var335[1].first, var335[1].second)
var336
}
val var337 = OnTheFlySealedMessageType(var331, var334, nextId(), beginGen, endGen)
return var337
}

fun matchFieldOptions(beginGen: Int, endGen: Int): FieldOptions {
val var339 = getSequenceElems(history, 336, listOf(337,338,7,347), beginGen, endGen)
val var340 = history[var339[1].second].findByBeginGenOpt(82, 1, var339[1].first)
val var341 = history[var339[1].second].findByBeginGenOpt(339, 1, var339[1].first)
check(hasSingleTrue(var340 != null, var341 != null))
val var342 = when {
var340 != null -> null
else -> {
val var343 = getSequenceElems(history, 340, listOf(7,341,343), var339[1].first, var339[1].second)
val var344 = matchFieldOption(var343[1].first, var343[1].second)
val var345 = unrollRepeat0(history, 343, 345, 9, 344, var343[2].first, var343[2].second).map { k ->
val var346 = getSequenceElems(history, 346, listOf(7,309,7,341), k.first, k.second)
val var347 = matchFieldOption(var346[3].first, var346[3].second)
var347
}
listOf(var344) + var345
}
}
val var338 = var342
val var348 = FieldOptions((var338 ?: listOf()), nextId(), beginGen, endGen)
return var348
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var349 = getSequenceElems(history, 342, listOf(160,7,167,7,168), beginGen, endGen)
val var350 = matchOptionName(var349[0].first, var349[0].second)
val var351 = matchConstant(var349[4].first, var349[4].second)
val var352 = FieldOption(var350, var351, nextId(), beginGen, endGen)
return var352
}

fun matchMessageMemberDef(beginGen: Int, endGen: Int): MessageMemberDef {
val var353 = history[endGen].findByBeginGenOpt(330, 1, beginGen)
val var354 = history[endGen].findByBeginGenOpt(348, 1, beginGen)
val var355 = history[endGen].findByBeginGenOpt(360, 1, beginGen)
check(hasSingleTrue(var353 != null, var354 != null, var355 != null))
val var356 = when {
var353 != null -> {
val var357 = matchFieldDef(beginGen, endGen)
var357
}
var354 != null -> {
val var358 = matchOneOfDef(beginGen, endGen)
var358
}
else -> {
val var359 = matchMessageDef(beginGen, endGen)
var359
}
}
return var356
}

fun matchOneOfDef(beginGen: Int, endGen: Int): OneOfDef {
val var360 = getSequenceElems(history, 349, listOf(350,7,62,7,236,354,7,359), beginGen, endGen)
val var361 = matchIdent(var360[2].first, var360[2].second)
val var362 = unrollRepeat0(history, 354, 356, 9, 355, var360[5].first, var360[5].second).map { k ->
val var363 = getSequenceElems(history, 357, listOf(7,358), k.first, k.second)
val var364 = matchOneOfMemberDef(var363[1].first, var363[1].second)
var364
}
val var365 = OneOfDef(var361, var362, nextId(), beginGen, endGen)
return var365
}

fun matchOneOfMemberDef(beginGen: Int, endGen: Int): OneOfMemberDef {
val var366 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var367 = history[endGen].findByBeginGenOpt(330, 1, beginGen)
check(hasSingleTrue(var366 != null, var367 != null))
val var368 = when {
var366 != null -> {
val var369 = matchOptionDef(beginGen, endGen)
var369
}
else -> {
val var370 = matchFieldDef(beginGen, endGen)
var370
}
}
return var368
}

fun matchPrimitiveType(beginGen: Int, endGen: Int): PrimitiveTypeEnum {
val var371 = history[endGen].findByBeginGenOpt(251, 1, beginGen)
val var372 = history[endGen].findByBeginGenOpt(255, 1, beginGen)
val var373 = history[endGen].findByBeginGenOpt(257, 1, beginGen)
val var374 = history[endGen].findByBeginGenOpt(261, 1, beginGen)
val var375 = history[endGen].findByBeginGenOpt(265, 1, beginGen)
val var376 = history[endGen].findByBeginGenOpt(267, 1, beginGen)
val var377 = history[endGen].findByBeginGenOpt(269, 1, beginGen)
val var378 = history[endGen].findByBeginGenOpt(271, 1, beginGen)
val var379 = history[endGen].findByBeginGenOpt(273, 1, beginGen)
val var380 = history[endGen].findByBeginGenOpt(276, 1, beginGen)
val var381 = history[endGen].findByBeginGenOpt(278, 1, beginGen)
val var382 = history[endGen].findByBeginGenOpt(280, 1, beginGen)
val var383 = history[endGen].findByBeginGenOpt(282, 1, beginGen)
val var384 = history[endGen].findByBeginGenOpt(284, 1, beginGen)
val var385 = history[endGen].findByBeginGenOpt(286, 1, beginGen)
check(hasSingleTrue(var371 != null, var372 != null, var373 != null, var374 != null, var375 != null, var376 != null, var377 != null, var378 != null, var379 != null, var380 != null, var381 != null, var382 != null, var383 != null, var384 != null, var385 != null))
val var386 = when {
var371 != null -> PrimitiveTypeEnum.DOUBLE
var372 != null -> PrimitiveTypeEnum.FLOAT
var373 != null -> PrimitiveTypeEnum.INT32
var374 != null -> PrimitiveTypeEnum.INT64
var375 != null -> PrimitiveTypeEnum.UINT32
var376 != null -> PrimitiveTypeEnum.UINT64
var377 != null -> PrimitiveTypeEnum.SINT32
var378 != null -> PrimitiveTypeEnum.SINT64
var379 != null -> PrimitiveTypeEnum.FIXED32
var380 != null -> PrimitiveTypeEnum.FIXED64
var381 != null -> PrimitiveTypeEnum.SFIXED32
var382 != null -> PrimitiveTypeEnum.SFIXED64
var383 != null -> PrimitiveTypeEnum.BOOL
var384 != null -> PrimitiveTypeEnum.STRING
else -> PrimitiveTypeEnum.BYTES
}
return var386
}

fun matchOptionalType(beginGen: Int, endGen: Int): OptionalType {
val var387 = getSequenceElems(history, 298, listOf(299,7,295,7,248,7,296), beginGen, endGen)
val var388 = matchType(var387[4].first, var387[4].second)
val var389 = OptionalType(var388, nextId(), beginGen, endGen)
return var389
}

}
