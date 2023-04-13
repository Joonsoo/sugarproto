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
  val options: FieldOptions?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ServiceMember, AstNode

sealed interface ReservedItem: AstNode

data class Max(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ReservedRangeEnd, AstNode

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

data class OnTheFlyEnumType(
  val name: Ident?,
  val fields: List<EnumMemberDef>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

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
  val members: List<ServiceMember>,
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

data class Inf(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AbstractFloatLiteral, AstNode

sealed interface EnumMemberDef: AstNode

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

data class EnumFieldDef(
  val minusTag: Boolean,
  val tag: IntLiteral,
  val name: Ident,
  val options: FieldOptions?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): EnumMemberDef, AstNode

sealed interface Constant: AstNode

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

sealed interface ReservedRangeEnd: AstNode

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

sealed interface ServiceMember: AstNode

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
): EnumMemberDef, MessageMemberDef, OneOfMemberDef, ServiceMember, AstNode

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

data class OnTheFlyMessageType(
  val name: Ident?,
  val fields: List<MessageMemberDef>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

data class ReservedDef(
  val ranges: List<ReservedItem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageMemberDef, AstNode

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

data class PlainChar(
  val value: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

data class EnumDef(
  val name: Ident,
  val members: List<EnumMemberDef>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageMemberDef, TopLevelDef, AstNode

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

data class ReservedRange(
  val reservedStart: IntLiteral,
  val reservedEnd: ReservedRangeEnd?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ReservedItem, AstNode

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

sealed interface IntLiteral: Constant, ReservedRangeEnd, AstNode

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
): ReservedItem, AstNode

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
val var212 = history[endGen].findByBeginGenOpt(353, 1, beginGen)
val var213 = history[endGen].findByBeginGenOpt(382, 1, beginGen)
val var214 = history[endGen].findByBeginGenOpt(471, 1, beginGen)
check(hasSingleTrue(var211 != null, var212 != null, var213 != null, var214 != null))
val var215 = when {
var211 != null -> {
val var216 = matchServiceDef(beginGen, endGen)
var216
}
var212 != null -> {
val var217 = matchEnumDef(beginGen, endGen)
var217
}
var213 != null -> {
val var218 = matchMessageDef(beginGen, endGen)
var218
}
else -> {
val var219 = matchSealedDef(beginGen, endGen)
var219
}
}
return var215
}

fun matchServiceDef(beginGen: Int, endGen: Int): ServiceDef {
val var220 = getSequenceElems(history, 230, listOf(231,7,62,7,236,237,7,381), beginGen, endGen)
val var221 = matchIdent(var220[2].first, var220[2].second)
val var222 = unrollRepeat0(history, 237, 239, 9, 238, var220[5].first, var220[5].second).map { k ->
val var223 = getSequenceElems(history, 240, listOf(7,241), k.first, k.second)
val var224 = matchServiceMember(var223[1].first, var223[1].second)
var224
}
val var225 = ServiceDef(var221, var222, nextId(), beginGen, endGen)
return var225
}

fun matchServiceMember(beginGen: Int, endGen: Int): ServiceMember {
val var226 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var227 = history[endGen].findByBeginGenOpt(242, 1, beginGen)
check(hasSingleTrue(var226 != null, var227 != null))
val var228 = when {
var226 != null -> {
val var229 = matchOptionDef(beginGen, endGen)
var229
}
else -> {
val var230 = matchRpcDef(beginGen, endGen)
var230
}
}
return var228
}

fun matchRpcDef(beginGen: Int, endGen: Int): RpcDef {
val var231 = getSequenceElems(history, 243, listOf(244,7,62,7,248,7,249,7,452,7,249,454,337), beginGen, endGen)
val var232 = matchIdent(var231[2].first, var231[2].second)
val var233 = matchType(var231[6].first, var231[6].second)
val var234 = matchType(var231[10].first, var231[10].second)
val var235 = history[var231[11].second].findByBeginGenOpt(82, 1, var231[11].first)
val var236 = history[var231[11].second].findByBeginGenOpt(455, 1, var231[11].first)
check(hasSingleTrue(var235 != null, var236 != null))
val var237 = when {
var235 != null -> null
else -> {
val var238 = getSequenceElems(history, 456, listOf(7,457,7,463), var231[11].first, var231[11].second)
val var239 = matchRpcTypeWheres(var238[3].first, var238[3].second)
var239
}
}
val var240 = history[var231[12].second].findByBeginGenOpt(82, 1, var231[12].first)
val var241 = history[var231[12].second].findByBeginGenOpt(338, 1, var231[12].first)
check(hasSingleTrue(var240 != null, var241 != null))
val var242 = when {
var240 != null -> null
else -> {
val var243 = getSequenceElems(history, 339, listOf(7,340), var231[12].first, var231[12].second)
val var244 = matchFieldOptions(var243[1].first, var243[1].second)
var244
}
}
val var245 = RpcDef(var232, var233, var234, var237, var242, nextId(), beginGen, endGen)
return var245
}

fun matchMessageDef(beginGen: Int, endGen: Int): MessageDef {
val var246 = getSequenceElems(history, 383, listOf(384,7,62,7,236,329,7,381), beginGen, endGen)
val var247 = matchIdent(var246[2].first, var246[2].second)
val var249 = history[var246[5].second].findByBeginGenOpt(82, 1, var246[5].first)
val var250 = history[var246[5].second].findByBeginGenOpt(330, 1, var246[5].first)
check(hasSingleTrue(var249 != null, var250 != null))
val var251 = when {
var249 != null -> null
else -> {
val var252 = getSequenceElems(history, 331, listOf(7,332), var246[5].first, var246[5].second)
val var253 = matchMessageMembers(var252[1].first, var252[1].second)
var253
}
}
val var248 = var251
val var254 = MessageDef(var247, (var248 ?: listOf()), nextId(), beginGen, endGen)
return var254
}

fun matchMessageMembers(beginGen: Int, endGen: Int): List<MessageMemberDef> {
val var255 = getSequenceElems(history, 333, listOf(334,424), beginGen, endGen)
val var256 = matchMessageMemberDef(var255[0].first, var255[0].second)
val var257 = unrollRepeat0(history, 424, 426, 9, 425, var255[1].first, var255[1].second).map { k ->
val var258 = getSequenceElems(history, 427, listOf(372,334), k.first, k.second)
val var259 = matchMessageMemberDef(var258[1].first, var258[1].second)
var259
}
return listOf(var256) + var257
}

fun matchEnumDef(beginGen: Int, endGen: Int): EnumDef {
val var260 = getSequenceElems(history, 354, listOf(355,7,62,7,236,357,7,381), beginGen, endGen)
val var261 = matchIdent(var260[2].first, var260[2].second)
val var263 = history[var260[5].second].findByBeginGenOpt(82, 1, var260[5].first)
val var264 = history[var260[5].second].findByBeginGenOpt(358, 1, var260[5].first)
check(hasSingleTrue(var263 != null, var264 != null))
val var265 = when {
var263 != null -> null
else -> {
val var266 = getSequenceElems(history, 359, listOf(7,360), var260[5].first, var260[5].second)
val var267 = matchEnumMembers(var266[1].first, var266[1].second)
var267
}
}
val var262 = var265
val var268 = EnumDef(var261, (var262 ?: listOf()), nextId(), beginGen, endGen)
return var268
}

fun matchEnumMembers(beginGen: Int, endGen: Int): List<EnumMemberDef> {
val var269 = getSequenceElems(history, 361, listOf(362,368), beginGen, endGen)
val var270 = matchEnumMemberDef(var269[0].first, var269[0].second)
val var271 = unrollRepeat0(history, 368, 370, 9, 369, var269[1].first, var269[1].second).map { k ->
val var272 = getSequenceElems(history, 371, listOf(372,362), k.first, k.second)
val var273 = matchEnumMemberDef(var272[1].first, var272[1].second)
var273
}
return listOf(var270) + var271
}

fun matchEnumMemberDef(beginGen: Int, endGen: Int): EnumMemberDef {
val var274 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var275 = history[endGen].findByBeginGenOpt(363, 1, beginGen)
check(hasSingleTrue(var274 != null, var275 != null))
val var276 = when {
var274 != null -> {
val var277 = matchOptionDef(beginGen, endGen)
var277
}
else -> {
val var278 = matchEnumFieldDef(beginGen, endGen)
var278
}
}
return var276
}

fun matchEnumFieldDef(beginGen: Int, endGen: Int): EnumFieldDef {
val var279 = getSequenceElems(history, 364, listOf(365,185,7,62,337), beginGen, endGen)
val var280 = history[var279[0].second].findByBeginGenOpt(82, 1, var279[0].first)
val var281 = history[var279[0].second].findByBeginGenOpt(366, 1, var279[0].first)
check(hasSingleTrue(var280 != null, var281 != null))
val var282 = when {
var280 != null -> null
else -> {
val var283 = getSequenceElems(history, 367, listOf(184,7), var279[0].first, var279[0].second)
source[var283[0].first]
}
}
val var284 = matchIntLiteral(var279[1].first, var279[1].second)
val var285 = matchIdent(var279[3].first, var279[3].second)
val var286 = history[var279[4].second].findByBeginGenOpt(82, 1, var279[4].first)
val var287 = history[var279[4].second].findByBeginGenOpt(338, 1, var279[4].first)
check(hasSingleTrue(var286 != null, var287 != null))
val var288 = when {
var286 != null -> null
else -> {
val var289 = getSequenceElems(history, 339, listOf(7,340), var279[4].first, var279[4].second)
val var290 = matchFieldOptions(var289[1].first, var289[1].second)
var290
}
}
val var291 = EnumFieldDef(var282 != null, var284, var285, var288, nextId(), beginGen, endGen)
return var291
}

fun matchSealedDef(beginGen: Int, endGen: Int): SealedDef {
val var292 = getSequenceElems(history, 472, listOf(430,7,62,7,236,437,7,381), beginGen, endGen)
val var293 = matchIdent(var292[2].first, var292[2].second)
val var294 = unrollRepeat0(history, 437, 439, 9, 438, var292[5].first, var292[5].second).map { k ->
val var295 = getSequenceElems(history, 440, listOf(7,441), k.first, k.second)
val var296 = matchSealedMemberDef(var295[1].first, var295[1].second)
var296
}
val var297 = SealedDef(var293, var294, nextId(), beginGen, endGen)
return var297
}

fun matchSealedMemberDef(beginGen: Int, endGen: Int): SealedMemberDef {
val var298 = matchFieldDef(beginGen, endGen)
return var298
}

fun matchFieldDef(beginGen: Int, endGen: Int): FieldDef {
val var299 = getSequenceElems(history, 336, listOf(185,7,62,7,248,7,249,337), beginGen, endGen)
val var300 = matchIntLiteral(var299[0].first, var299[0].second)
val var301 = matchIdent(var299[2].first, var299[2].second)
val var302 = matchType(var299[6].first, var299[6].second)
val var303 = history[var299[7].second].findByBeginGenOpt(82, 1, var299[7].first)
val var304 = history[var299[7].second].findByBeginGenOpt(338, 1, var299[7].first)
check(hasSingleTrue(var303 != null, var304 != null))
val var305 = when {
var303 != null -> null
else -> {
val var306 = getSequenceElems(history, 339, listOf(7,340), var299[7].first, var299[7].second)
val var307 = matchFieldOptions(var306[1].first, var306[1].second)
var307
}
}
val var308 = FieldDef(var300, var301, var302, var305, nextId(), beginGen, endGen)
return var308
}

fun matchLetter(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchType(beginGen: Int, endGen: Int): Type {
val var309 = history[endGen].findByBeginGenOpt(250, 1, beginGen)
val var310 = history[endGen].findByBeginGenOpt(290, 1, beginGen)
val var311 = history[endGen].findByBeginGenOpt(298, 1, beginGen)
val var312 = history[endGen].findByBeginGenOpt(304, 1, beginGen)
val var313 = history[endGen].findByBeginGenOpt(311, 1, beginGen)
val var314 = history[endGen].findByBeginGenOpt(317, 1, beginGen)
val var315 = history[endGen].findByBeginGenOpt(428, 1, beginGen)
val var316 = history[endGen].findByBeginGenOpt(442, 1, beginGen)
val var317 = history[endGen].findByBeginGenOpt(449, 1, beginGen)
check(hasSingleTrue(var309 != null, var310 != null, var311 != null, var312 != null, var313 != null, var314 != null, var315 != null, var316 != null, var317 != null))
val var318 = when {
var309 != null -> {
val var319 = matchPrimitiveType(beginGen, endGen)
val var320 = PrimitiveType(var319, nextId(), beginGen, endGen)
var320
}
var310 != null -> {
val var321 = matchRepeatedType(beginGen, endGen)
var321
}
var311 != null -> {
val var322 = matchOptionalType(beginGen, endGen)
var322
}
var312 != null -> {
val var323 = matchMapType(beginGen, endGen)
var323
}
var313 != null -> {
val var324 = matchStreamType(beginGen, endGen)
var324
}
var314 != null -> {
val var325 = matchOnTheFlyMessageType(beginGen, endGen)
var325
}
var315 != null -> {
val var326 = matchOnTheFlySealedMessageType(beginGen, endGen)
var326
}
var316 != null -> {
val var327 = matchOnTheFlyEnumType(beginGen, endGen)
var327
}
else -> {
val var328 = matchTypeName(beginGen, endGen)
var328
}
}
return var318
}

fun matchTypeName(beginGen: Int, endGen: Int): TypeName {
val var329 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var330 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var331 = history[endGen].findByBeginGenOpt(450, 1, beginGen)
check(hasSingleTrue(var329 != null, var330 != null, var331 != null))
val var332 = when {
var329 != null -> {
val var333 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var334 = matchIdentName(var333[1].first, var333[1].second)
val var335 = SingleName(var334, nextId(), beginGen, endGen)
var335
}
var330 != null -> {
val var336 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var337 = matchIdent(var336[0].first, var336[0].second)
val var338 = unrollRepeat1(history, 179, 79, 79, 180, var336[1].first, var336[1].second).map { k ->
val var339 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var340 = matchIdent(var339[3].first, var339[3].second)
var340
}
val var341 = MultiName(listOf(var337) + var338, nextId(), beginGen, endGen)
var341
}
else -> {
val var342 = matchIdentName(beginGen, endGen)
val var343 = SingleName(var342, nextId(), beginGen, endGen)
var343
}
}
return var332
}

fun matchOnTheFlyMessageType(beginGen: Int, endGen: Int): OnTheFlyMessageType {
val var344 = getSequenceElems(history, 318, listOf(319,236,329,7,381), beginGen, endGen)
val var345 = history[var344[0].second].findByBeginGenOpt(82, 1, var344[0].first)
val var346 = history[var344[0].second].findByBeginGenOpt(320, 1, var344[0].first)
check(hasSingleTrue(var345 != null, var346 != null))
val var347 = when {
var345 != null -> null
else -> {
val var348 = getSequenceElems(history, 321, listOf(322,7), var344[0].first, var344[0].second)
val var349 = matchIdentNoSealedEnum(var348[0].first, var348[0].second)
var349
}
}
val var351 = history[var344[2].second].findByBeginGenOpt(82, 1, var344[2].first)
val var352 = history[var344[2].second].findByBeginGenOpt(330, 1, var344[2].first)
check(hasSingleTrue(var351 != null, var352 != null))
val var353 = when {
var351 != null -> null
else -> {
val var354 = getSequenceElems(history, 331, listOf(7,332), var344[2].first, var344[2].second)
val var355 = matchMessageMembers(var354[1].first, var354[1].second)
var355
}
}
val var350 = var353
val var356 = OnTheFlyMessageType(var347, (var350 ?: listOf()), nextId(), beginGen, endGen)
return var356
}

fun matchIdentNoSealedEnum(beginGen: Int, endGen: Int): Ident {
val var357 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var358 = history[endGen].findByBeginGenOpt(323, 1, beginGen)
check(hasSingleTrue(var357 != null, var358 != null))
val var359 = when {
var357 != null -> {
val var360 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var361 = matchIdentName(var360[1].first, var360[1].second)
val var362 = Ident(var361, nextId(), beginGen, endGen)
var362
}
else -> {
val var363 = matchIdentName(beginGen, endGen)
val var364 = Ident(var363, nextId(), beginGen, endGen)
var364
}
}
return var359
}

fun matchOnTheFlyEnumType(beginGen: Int, endGen: Int): OnTheFlyEnumType {
val var365 = getSequenceElems(history, 443, listOf(355,444,7,236,357,7,381), beginGen, endGen)
val var366 = history[var365[1].second].findByBeginGenOpt(82, 1, var365[1].first)
val var367 = history[var365[1].second].findByBeginGenOpt(445, 1, var365[1].first)
check(hasSingleTrue(var366 != null, var367 != null))
val var368 = when {
var366 != null -> null
else -> {
val var369 = getSequenceElems(history, 446, listOf(7,447), var365[1].first, var365[1].second)
val var370 = matchIdentNoEnum(var369[1].first, var369[1].second)
var370
}
}
val var372 = history[var365[4].second].findByBeginGenOpt(82, 1, var365[4].first)
val var373 = history[var365[4].second].findByBeginGenOpt(358, 1, var365[4].first)
check(hasSingleTrue(var372 != null, var373 != null))
val var374 = when {
var372 != null -> null
else -> {
val var375 = getSequenceElems(history, 359, listOf(7,360), var365[4].first, var365[4].second)
val var376 = matchEnumMembers(var375[1].first, var375[1].second)
var376
}
}
val var371 = var374
val var377 = OnTheFlyEnumType(var368, (var371 ?: listOf()), nextId(), beginGen, endGen)
return var377
}

fun matchIdentNoEnum(beginGen: Int, endGen: Int): Ident {
val var378 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var379 = history[endGen].findByBeginGenOpt(448, 1, beginGen)
check(hasSingleTrue(var378 != null, var379 != null))
val var380 = when {
var378 != null -> {
val var381 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var382 = matchIdentName(var381[1].first, var381[1].second)
val var383 = Ident(var382, nextId(), beginGen, endGen)
var383
}
else -> {
val var384 = matchIdentName(beginGen, endGen)
val var385 = Ident(var384, nextId(), beginGen, endGen)
var385
}
}
return var380
}

fun matchMapType(beginGen: Int, endGen: Int): MapType {
val var386 = getSequenceElems(history, 305, listOf(306,7,296,7,249,7,310,7,249,7,297), beginGen, endGen)
val var387 = matchType(var386[4].first, var386[4].second)
val var388 = matchType(var386[8].first, var386[8].second)
val var389 = MapType(var387, var388, nextId(), beginGen, endGen)
return var389
}

fun matchRepeatedType(beginGen: Int, endGen: Int): RepeatedType {
val var390 = getSequenceElems(history, 291, listOf(292,7,296,7,249,7,297), beginGen, endGen)
val var391 = matchType(var390[4].first, var390[4].second)
val var392 = RepeatedType(var391, nextId(), beginGen, endGen)
return var392
}

fun matchStreamType(beginGen: Int, endGen: Int): StreamType {
val var393 = getSequenceElems(history, 312, listOf(313,7,296,7,249,7,297), beginGen, endGen)
val var394 = matchType(var393[4].first, var393[4].second)
val var395 = StreamType(var394, nextId(), beginGen, endGen)
return var395
}

fun matchRpcTypeWheres(beginGen: Int, endGen: Int): RpcTypeWheres {
val var396 = getSequenceElems(history, 464, listOf(465,467), beginGen, endGen)
val var397 = matchRpcTypeWhere(var396[0].first, var396[0].second)
val var398 = unrollRepeat0(history, 467, 469, 9, 468, var396[1].first, var396[1].second).map { k ->
val var399 = getSequenceElems(history, 470, listOf(7,310,7,465), k.first, k.second)
val var400 = matchRpcTypeWhere(var399[3].first, var399[3].second)
var400
}
val var401 = RpcTypeWheres(listOf(var397) + var398, nextId(), beginGen, endGen)
return var401
}

fun matchRpcTypeWhere(beginGen: Int, endGen: Int): RpcTypeWhere {
val var402 = getSequenceElems(history, 466, listOf(62,7,167,7,249), beginGen, endGen)
val var403 = matchIdent(var402[0].first, var402[0].second)
val var404 = matchType(var402[4].first, var402[4].second)
val var405 = RpcTypeWhere(var403, var404, nextId(), beginGen, endGen)
return var405
}

fun matchOnTheFlySealedMessageType(beginGen: Int, endGen: Int): OnTheFlySealedMessageType {
val var406 = getSequenceElems(history, 429, listOf(430,432,7,236,437,7,381), beginGen, endGen)
val var407 = history[var406[1].second].findByBeginGenOpt(82, 1, var406[1].first)
val var408 = history[var406[1].second].findByBeginGenOpt(433, 1, var406[1].first)
check(hasSingleTrue(var407 != null, var408 != null))
val var409 = when {
var407 != null -> null
else -> {
val var410 = getSequenceElems(history, 434, listOf(7,435), var406[1].first, var406[1].second)
val var411 = matchIdentNoSealed(var410[1].first, var410[1].second)
var411
}
}
val var412 = unrollRepeat0(history, 437, 439, 9, 438, var406[4].first, var406[4].second).map { k ->
val var413 = getSequenceElems(history, 440, listOf(7,441), k.first, k.second)
val var414 = matchSealedMemberDef(var413[1].first, var413[1].second)
var414
}
val var415 = OnTheFlySealedMessageType(var409, var412, nextId(), beginGen, endGen)
return var415
}

fun matchIdentNoSealed(beginGen: Int, endGen: Int): Ident {
val var416 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var417 = history[endGen].findByBeginGenOpt(436, 1, beginGen)
check(hasSingleTrue(var416 != null, var417 != null))
val var418 = when {
var416 != null -> {
val var419 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var420 = matchIdentName(var419[1].first, var419[1].second)
val var421 = Ident(var420, nextId(), beginGen, endGen)
var421
}
else -> {
val var422 = matchIdentName(beginGen, endGen)
val var423 = Ident(var422, nextId(), beginGen, endGen)
var423
}
}
return var418
}

fun matchFieldOptions(beginGen: Int, endGen: Int): FieldOptions {
val var425 = getSequenceElems(history, 341, listOf(342,343,7,352), beginGen, endGen)
val var426 = history[var425[1].second].findByBeginGenOpt(82, 1, var425[1].first)
val var427 = history[var425[1].second].findByBeginGenOpt(344, 1, var425[1].first)
check(hasSingleTrue(var426 != null, var427 != null))
val var428 = when {
var426 != null -> null
else -> {
val var429 = getSequenceElems(history, 345, listOf(7,346,348), var425[1].first, var425[1].second)
val var430 = matchFieldOption(var429[1].first, var429[1].second)
val var431 = unrollRepeat0(history, 348, 350, 9, 349, var429[2].first, var429[2].second).map { k ->
val var432 = getSequenceElems(history, 351, listOf(7,310,7,346), k.first, k.second)
val var433 = matchFieldOption(var432[3].first, var432[3].second)
var433
}
listOf(var430) + var431
}
}
val var424 = var428
val var434 = FieldOptions((var424 ?: listOf()), nextId(), beginGen, endGen)
return var434
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var435 = getSequenceElems(history, 347, listOf(160,7,167,7,168), beginGen, endGen)
val var436 = matchOptionName(var435[0].first, var435[0].second)
val var437 = matchConstant(var435[4].first, var435[4].second)
val var438 = FieldOption(var436, var437, nextId(), beginGen, endGen)
return var438
}

fun matchMessageMemberDef(beginGen: Int, endGen: Int): MessageMemberDef {
val var439 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var440 = history[endGen].findByBeginGenOpt(335, 1, beginGen)
val var441 = history[endGen].findByBeginGenOpt(353, 1, beginGen)
val var442 = history[endGen].findByBeginGenOpt(382, 1, beginGen)
val var443 = history[endGen].findByBeginGenOpt(388, 1, beginGen)
val var444 = history[endGen].findByBeginGenOpt(399, 1, beginGen)
check(hasSingleTrue(var439 != null, var440 != null, var441 != null, var442 != null, var443 != null, var444 != null))
val var445 = when {
var439 != null -> {
val var446 = matchOptionDef(beginGen, endGen)
var446
}
var440 != null -> {
val var447 = matchFieldDef(beginGen, endGen)
var447
}
var441 != null -> {
val var448 = matchEnumDef(beginGen, endGen)
var448
}
var442 != null -> {
val var449 = matchMessageDef(beginGen, endGen)
var449
}
var443 != null -> {
val var450 = matchOneOfDef(beginGen, endGen)
var450
}
else -> {
val var451 = matchReservedDef(beginGen, endGen)
var451
}
}
return var445
}

fun matchOneOfDef(beginGen: Int, endGen: Int): OneOfDef {
val var452 = getSequenceElems(history, 389, listOf(390,7,62,7,236,394,7,381), beginGen, endGen)
val var453 = matchIdent(var452[2].first, var452[2].second)
val var454 = unrollRepeat0(history, 394, 396, 9, 395, var452[5].first, var452[5].second).map { k ->
val var455 = getSequenceElems(history, 397, listOf(7,398), k.first, k.second)
val var456 = matchOneOfMemberDef(var455[1].first, var455[1].second)
var456
}
val var457 = OneOfDef(var453, var454, nextId(), beginGen, endGen)
return var457
}

fun matchReservedDef(beginGen: Int, endGen: Int): ReservedDef {
val var458 = getSequenceElems(history, 400, listOf(401,7,165,7,405,417,421,7,166), beginGen, endGen)
val var459 = matchReservedItem(var458[4].first, var458[4].second)
val var460 = unrollRepeat0(history, 417, 419, 9, 418, var458[5].first, var458[5].second).map { k ->
val var461 = getSequenceElems(history, 420, listOf(7,310,7,405), k.first, k.second)
val var462 = matchReservedItem(var461[3].first, var461[3].second)
var462
}
val var463 = ReservedDef(listOf(var459) + var460, nextId(), beginGen, endGen)
return var463
}

fun matchReservedItem(beginGen: Int, endGen: Int): ReservedItem {
val var464 = history[endGen].findByBeginGenOpt(62, 1, beginGen)
val var465 = history[endGen].findByBeginGenOpt(406, 1, beginGen)
check(hasSingleTrue(var464 != null, var465 != null))
val var466 = when {
var464 != null -> {
val var467 = matchIdent(beginGen, endGen)
var467
}
else -> {
val var468 = matchReservedRange(beginGen, endGen)
var468
}
}
return var466
}

fun matchOneOfMemberDef(beginGen: Int, endGen: Int): OneOfMemberDef {
val var469 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var470 = history[endGen].findByBeginGenOpt(335, 1, beginGen)
check(hasSingleTrue(var469 != null, var470 != null))
val var471 = when {
var469 != null -> {
val var472 = matchOptionDef(beginGen, endGen)
var472
}
else -> {
val var473 = matchFieldDef(beginGen, endGen)
var473
}
}
return var471
}

fun matchReservedRange(beginGen: Int, endGen: Int): ReservedRange {
val var474 = getSequenceElems(history, 407, listOf(185,408), beginGen, endGen)
val var475 = matchIntLiteral(var474[0].first, var474[0].second)
val var476 = history[var474[1].second].findByBeginGenOpt(82, 1, var474[1].first)
val var477 = history[var474[1].second].findByBeginGenOpt(409, 1, var474[1].first)
check(hasSingleTrue(var476 != null, var477 != null))
val var478 = when {
var476 != null -> null
else -> {
val var479 = getSequenceElems(history, 410, listOf(7,411,7,413), var474[1].first, var474[1].second)
val var480 = matchReservedRangeEnd(var479[3].first, var479[3].second)
var480
}
}
val var481 = ReservedRange(var475, var478, nextId(), beginGen, endGen)
return var481
}

fun matchReservedRangeEnd(beginGen: Int, endGen: Int): ReservedRangeEnd {
val var482 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var483 = history[endGen].findByBeginGenOpt(414, 1, beginGen)
check(hasSingleTrue(var482 != null, var483 != null))
val var484 = when {
var482 != null -> {
val var485 = matchIntLiteral(beginGen, endGen)
var485
}
else -> {
val var486 = Max(nextId(), beginGen, endGen)
var486
}
}
return var484
}

fun matchPrimitiveType(beginGen: Int, endGen: Int): PrimitiveTypeEnum {
val var487 = history[endGen].findByBeginGenOpt(252, 1, beginGen)
val var488 = history[endGen].findByBeginGenOpt(256, 1, beginGen)
val var489 = history[endGen].findByBeginGenOpt(258, 1, beginGen)
val var490 = history[endGen].findByBeginGenOpt(262, 1, beginGen)
val var491 = history[endGen].findByBeginGenOpt(266, 1, beginGen)
val var492 = history[endGen].findByBeginGenOpt(268, 1, beginGen)
val var493 = history[endGen].findByBeginGenOpt(270, 1, beginGen)
val var494 = history[endGen].findByBeginGenOpt(272, 1, beginGen)
val var495 = history[endGen].findByBeginGenOpt(274, 1, beginGen)
val var496 = history[endGen].findByBeginGenOpt(277, 1, beginGen)
val var497 = history[endGen].findByBeginGenOpt(279, 1, beginGen)
val var498 = history[endGen].findByBeginGenOpt(281, 1, beginGen)
val var499 = history[endGen].findByBeginGenOpt(283, 1, beginGen)
val var500 = history[endGen].findByBeginGenOpt(285, 1, beginGen)
val var501 = history[endGen].findByBeginGenOpt(287, 1, beginGen)
check(hasSingleTrue(var487 != null, var488 != null, var489 != null, var490 != null, var491 != null, var492 != null, var493 != null, var494 != null, var495 != null, var496 != null, var497 != null, var498 != null, var499 != null, var500 != null, var501 != null))
val var502 = when {
var487 != null -> PrimitiveTypeEnum.DOUBLE
var488 != null -> PrimitiveTypeEnum.FLOAT
var489 != null -> PrimitiveTypeEnum.INT32
var490 != null -> PrimitiveTypeEnum.INT64
var491 != null -> PrimitiveTypeEnum.UINT32
var492 != null -> PrimitiveTypeEnum.UINT64
var493 != null -> PrimitiveTypeEnum.SINT32
var494 != null -> PrimitiveTypeEnum.SINT64
var495 != null -> PrimitiveTypeEnum.FIXED32
var496 != null -> PrimitiveTypeEnum.FIXED64
var497 != null -> PrimitiveTypeEnum.SFIXED32
var498 != null -> PrimitiveTypeEnum.SFIXED64
var499 != null -> PrimitiveTypeEnum.BOOL
var500 != null -> PrimitiveTypeEnum.STRING
else -> PrimitiveTypeEnum.BYTES
}
return var502
}

fun matchOptionalType(beginGen: Int, endGen: Int): OptionalType {
val var503 = getSequenceElems(history, 299, listOf(300,7,296,7,249,7,297), beginGen, endGen)
val var504 = matchType(var503[4].first, var503[4].second)
val var505 = OptionalType(var504, nextId(), beginGen, endGen)
return var505
}

}
