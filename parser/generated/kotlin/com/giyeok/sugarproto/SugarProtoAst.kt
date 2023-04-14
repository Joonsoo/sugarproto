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

data class DecimalLiteral(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLiteral, AstNode

data class OnTheFlyEnumType(
  val name: Ident?,
  val fields: List<EnumMemberDefWS>,
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
  val members: List<ServiceMemberWS>,
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

data class LineComment(
  val content: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Comment, AstNode

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

data class ServiceMemberWS(
  val comments: List<Comment?>,
  val member: ServiceMember,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

sealed interface TypeName: Type, AstNode

sealed interface ReservedRangeEnd: AstNode

data class OneOfDef(
  val name: Ident,
  val members: List<OneOfMembersDefWS>,
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

data class SealedMemberDefWS(
  val comments: List<Comment?>,
  val def: SealedMemberDef,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class EnumMemberDefWS(
  val comments: List<Comment?>,
  val def: EnumMemberDef,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

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
  val defs: List<TopLevelDefWS>,
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
  val fields: List<MessageMemberDefWS>,
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

data class Max(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ReservedRangeEnd, AstNode

data class BlockComment(
  val content: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Comment, AstNode

data class OptionName(
  val name: FullIdent,
  val trailings: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

sealed interface AbstractFloatLiteral: Constant, AstNode

data class MessageMemberDefWS(
  val comments: List<Comment?>,
  val def: MessageMemberDef,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class MessageDef(
  val name: Ident,
  val members: List<MessageMemberDefWS>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageMemberDef, TopLevelDef, AstNode

data class TopLevelDefWS(
  val comments: List<Comment?>,
  val def: TopLevelDef,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class UnicodeLongEscape(
  val code: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

data class PlainChar(
  val value: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

data class EnumDef(
  val name: Ident,
  val members: List<EnumMemberDefWS>,
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
  val fields: List<SealedMemberDefWS>,
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
  val members: List<SealedMemberDefWS>,
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

data class OneOfMembersDefWS(
  val comments: List<Comment?>,
  val def: OneOfMemberDef,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

sealed interface Comment: AstNode

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
val var14 = matchTopLevelDefWS(k.first, k.second)
var14
}
val var15 = CompilationUnit(var4, var7, var10, var13, nextId(), beginGen, endGen)
return var15
}

fun matchPackageDef(beginGen: Int, endGen: Int): FullIdent {
val var16 = getSequenceElems(history, 44, listOf(45,7,60), beginGen, endGen)
val var17 = matchFullIdent(var16[2].first, var16[2].second)
return var17
}

fun matchFullIdent(beginGen: Int, endGen: Int): FullIdent {
val var18 = getSequenceElems(history, 61, listOf(62,77), beginGen, endGen)
val var19 = matchIdent(var18[0].first, var18[0].second)
val var20 = unrollRepeat0(history, 77, 79, 9, 78, var18[1].first, var18[1].second).map { k ->
val var21 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var22 = matchIdent(var21[3].first, var21[3].second)
var22
}
val var23 = FullIdent(listOf(var19) + var20, nextId(), beginGen, endGen)
return var23
}

fun matchImportDef(beginGen: Int, endGen: Int): StringLiteral {
val var24 = getSequenceElems(history, 88, listOf(89,7,98), beginGen, endGen)
val var25 = matchStringLiteral(var24[2].first, var24[2].second)
return var25
}

fun matchOptionDef(beginGen: Int, endGen: Int): OptionDef {
val var26 = getSequenceElems(history, 154, listOf(155,7,160,7,167,7,168), beginGen, endGen)
val var27 = matchOptionName(var26[2].first, var26[2].second)
val var28 = matchConstant(var26[6].first, var26[6].second)
val var29 = OptionDef(var27, var28, nextId(), beginGen, endGen)
return var29
}

fun matchConstant(beginGen: Int, endGen: Int): Constant {
val var30 = history[endGen].findByBeginGenOpt(98, 1, beginGen)
val var31 = history[endGen].findByBeginGenOpt(169, 1, beginGen)
val var32 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var33 = history[endGen].findByBeginGenOpt(181, 3, beginGen)
val var34 = history[endGen].findByBeginGenOpt(201, 3, beginGen)
val var35 = history[endGen].findByBeginGenOpt(222, 1, beginGen)
check(hasSingleTrue(var30 != null, var31 != null, var32 != null, var33 != null, var34 != null, var35 != null))
val var36 = when {
var30 != null -> {
val var37 = matchStringLiteral(beginGen, endGen)
var37
}
var31 != null -> {
val var38 = matchIdent(beginGen, endGen)
val var39 = FullIdent(listOf(var38), nextId(), beginGen, endGen)
var39
}
var32 != null -> {
val var40 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var41 = matchIdent(var40[0].first, var40[0].second)
val var42 = unrollRepeat1(history, 179, 79, 79, 180, var40[1].first, var40[1].second).map { k ->
val var43 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var44 = matchIdent(var43[3].first, var43[3].second)
var44
}
val var45 = FullIdent(listOf(var41) + var42, nextId(), beginGen, endGen)
var45
}
var33 != null -> {
val var46 = getSequenceElems(history, 181, listOf(182,7,185), beginGen, endGen)
val var47 = matchIntLiteral(var46[2].first, var46[2].second)
var47
}
var34 != null -> {
val var48 = getSequenceElems(history, 201, listOf(182,7,202), beginGen, endGen)
val var49 = matchFloatLiteral(var48[2].first, var48[2].second)
var49
}
else -> {
val var50 = matchBoolLiteral(beginGen, endGen)
var50
}
}
return var36
}

fun matchFloatLiteral(beginGen: Int, endGen: Int): AbstractFloatLiteral {
val var51 = history[endGen].findByBeginGenOpt(203, 4, beginGen)
val var52 = history[endGen].findByBeginGenOpt(214, 2, beginGen)
val var53 = history[endGen].findByBeginGenOpt(215, 3, beginGen)
val var54 = history[endGen].findByBeginGenOpt(216, 1, beginGen)
val var55 = history[endGen].findByBeginGenOpt(219, 1, beginGen)
check(hasSingleTrue(var51 != null, var52 != null, var53 != null, var54 != null, var55 != null))
val var56 = when {
var51 != null -> {
val var57 = getSequenceElems(history, 203, listOf(204,81,207,208), beginGen, endGen)
val var58 = matchDecimals(var57[0].first, var57[0].second)
val var59 = history[var57[2].second].findByBeginGenOpt(82, 1, var57[2].first)
val var60 = history[var57[2].second].findByBeginGenOpt(204, 1, var57[2].first)
check(hasSingleTrue(var59 != null, var60 != null))
val var61 = when {
var59 != null -> null
else -> {
val var62 = matchDecimals(var57[2].first, var57[2].second)
var62
}
}
val var63 = history[var57[3].second].findByBeginGenOpt(82, 1, var57[3].first)
val var64 = history[var57[3].second].findByBeginGenOpt(209, 1, var57[3].first)
check(hasSingleTrue(var63 != null, var64 != null))
val var65 = when {
var63 != null -> null
else -> {
val var66 = matchExponent(var57[3].first, var57[3].second)
var66
}
}
val var67 = FloatLiteral(var58, var61, var65, nextId(), beginGen, endGen)
var67
}
var52 != null -> {
val var68 = getSequenceElems(history, 214, listOf(204,209), beginGen, endGen)
val var69 = matchDecimals(var68[0].first, var68[0].second)
val var70 = matchExponent(var68[1].first, var68[1].second)
val var71 = FloatLiteral(var69, null, var70, nextId(), beginGen, endGen)
var71
}
var53 != null -> {
val var72 = getSequenceElems(history, 215, listOf(81,204,208), beginGen, endGen)
val var73 = matchDecimals(var72[1].first, var72[1].second)
val var74 = history[var72[2].second].findByBeginGenOpt(82, 1, var72[2].first)
val var75 = history[var72[2].second].findByBeginGenOpt(209, 1, var72[2].first)
check(hasSingleTrue(var74 != null, var75 != null))
val var76 = when {
var74 != null -> null
else -> {
val var77 = matchExponent(var72[2].first, var72[2].second)
var77
}
}
val var78 = FloatLiteral(null, var73, var76, nextId(), beginGen, endGen)
var78
}
var54 != null -> {
val var79 = Inf(nextId(), beginGen, endGen)
var79
}
else -> {
val var80 = Nan(nextId(), beginGen, endGen)
var80
}
}
return var56
}

fun matchBoolLiteral(beginGen: Int, endGen: Int): BoolLiteral {
val var81 = history[endGen].findByBeginGenOpt(171, 1, beginGen)
val var82 = history[endGen].findByBeginGenOpt(173, 1, beginGen)
check(hasSingleTrue(var81 != null, var82 != null))
val var83 = when {
var81 != null -> BoolValueEnum.TRUE
else -> BoolValueEnum.FALSE
}
val var84 = BoolLiteral(var83, nextId(), beginGen, endGen)
return var84
}

fun matchStringLiteral(beginGen: Int, endGen: Int): StringLiteral {
val var85 = getSequenceElems(history, 99, listOf(100,145), beginGen, endGen)
val var86 = matchStringLiteralSingle(var85[0].first, var85[0].second)
val var87 = unrollRepeat0(history, 145, 147, 9, 146, var85[1].first, var85[1].second).map { k ->
val var88 = getSequenceElems(history, 148, listOf(7,100), k.first, k.second)
val var89 = matchStringLiteralSingle(var88[1].first, var88[1].second)
var89
}
val var90 = StringLiteral(listOf(var86) + var87, nextId(), beginGen, endGen)
return var90
}

fun matchStringLiteralSingle(beginGen: Int, endGen: Int): StringLiteralSingle {
val var91 = history[endGen].findByBeginGenOpt(101, 3, beginGen)
val var92 = history[endGen].findByBeginGenOpt(143, 3, beginGen)
check(hasSingleTrue(var91 != null, var92 != null))
val var93 = when {
var91 != null -> {
val var94 = getSequenceElems(history, 101, listOf(102,103,102), beginGen, endGen)
val var95 = unrollRepeat0(history, 103, 105, 9, 104, var94[1].first, var94[1].second).map { k ->
val var96 = matchCharValue(k.first, k.second)
var96
}
val var97 = StringLiteralSingle(var95, nextId(), beginGen, endGen)
var97
}
else -> {
val var98 = getSequenceElems(history, 143, listOf(144,103,144), beginGen, endGen)
val var99 = unrollRepeat0(history, 103, 105, 9, 104, var98[1].first, var98[1].second).map { k ->
val var100 = matchCharValue(k.first, k.second)
var100
}
val var101 = StringLiteralSingle(var99, nextId(), beginGen, endGen)
var101
}
}
return var93
}

fun matchOptionName(beginGen: Int, endGen: Int): OptionName {
val var102 = getSequenceElems(history, 161, listOf(162,77), beginGen, endGen)
val var103 = history[var102[0].second].findByBeginGenOpt(62, 1, var102[0].first)
val var104 = history[var102[0].second].findByBeginGenOpt(163, 1, var102[0].first)
check(hasSingleTrue(var103 != null, var104 != null))
val var105 = when {
var103 != null -> {
val var106 = matchIdent(var102[0].first, var102[0].second)
val var107 = FullIdent(listOf(var106), nextId(), var102[0].first, var102[0].second)
var107
}
else -> {
val var108 = getSequenceElems(history, 164, listOf(165,7,60,7,166), var102[0].first, var102[0].second)
val var109 = matchFullIdent(var108[2].first, var108[2].second)
var109
}
}
val var110 = unrollRepeat0(history, 77, 79, 9, 78, var102[1].first, var102[1].second).map { k ->
val var111 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var112 = matchIdent(var111[3].first, var111[3].second)
var112
}
val var113 = OptionName(var105, var110, nextId(), beginGen, endGen)
return var113
}

fun matchTopLevelDefWS(beginGen: Int, endGen: Int): TopLevelDefWS {
val var114 = getSequenceElems(history, 227, listOf(7,228), beginGen, endGen)
val var115 = matchWS(var114[0].first, var114[0].second)
val var116 = matchTopLevelDef(var114[1].first, var114[1].second)
val var117 = TopLevelDefWS(var115, var116, nextId(), beginGen, endGen)
return var117
}

fun matchWS(beginGen: Int, endGen: Int): List<Comment?> {
val var118 = unrollRepeat0(history, 8, 11, 9, 10, beginGen, endGen).map { k ->
val var119 = history[k.second].findByBeginGenOpt(12, 1, k.first)
val var120 = history[k.second].findByBeginGenOpt(13, 1, k.first)
check(hasSingleTrue(var119 != null, var120 != null))
val var121 = when {
var119 != null -> null
else -> {
val var122 = matchComment(k.first, k.second)
var122
}
}
var121
}
return var118
}

fun matchDecimals(beginGen: Int, endGen: Int): String {
val var123 = unrollRepeat1(history, 205, 72, 72, 206, beginGen, endGen).map { k ->
val var124 = matchDecimalDigit(k.first, k.second)
var124
}
return var123.joinToString("") { it.toString() }
}

fun matchDecimalDigit(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchCharValue(beginGen: Int, endGen: Int): CharValue {
val var125 = history[endGen].findByBeginGenOpt(106, 1, beginGen)
val var126 = history[endGen].findByBeginGenOpt(109, 1, beginGen)
val var127 = history[endGen].findByBeginGenOpt(117, 1, beginGen)
val var128 = history[endGen].findByBeginGenOpt(124, 1, beginGen)
val var129 = history[endGen].findByBeginGenOpt(127, 1, beginGen)
val var130 = history[endGen].findByBeginGenOpt(130, 1, beginGen)
check(hasSingleTrue(var125 != null, var126 != null, var127 != null, var128 != null, var129 != null, var130 != null))
val var131 = when {
var125 != null -> {
val var132 = matchPlainChar(beginGen, endGen)
var132
}
var126 != null -> {
val var133 = matchHexEscape(beginGen, endGen)
var133
}
var127 != null -> {
val var134 = matchOctEscape(beginGen, endGen)
var134
}
var128 != null -> {
val var135 = matchCharEscape(beginGen, endGen)
var135
}
var129 != null -> {
val var136 = matchUnicodeEscape(beginGen, endGen)
var136
}
else -> {
val var137 = matchUnicodeLongEscape(beginGen, endGen)
var137
}
}
return var131
}

fun matchCharEscape(beginGen: Int, endGen: Int): CharEscape {
val var138 = getSequenceElems(history, 125, listOf(111,126), beginGen, endGen)
val var139 = CharEscape(source[var138[1].first], nextId(), beginGen, endGen)
return var139
}

fun matchUnicodeLongEscape(beginGen: Int, endGen: Int): UnicodeLongEscape {
val var140 = getSequenceElems(history, 131, listOf(111,129,132), beginGen, endGen)
val var141 = history[var140[2].second].findByBeginGenOpt(133, 1, var140[2].first)
val var142 = history[var140[2].second].findByBeginGenOpt(138, 1, var140[2].first)
check(hasSingleTrue(var141 != null, var142 != null))
val var143 = when {
var141 != null -> {
val var144 = getSequenceElems(history, 134, listOf(135,114,114,114,114,114), var140[2].first, var140[2].second)
val var145 = matchHexDigit(var144[1].first, var144[1].second)
val var146 = matchHexDigit(var144[2].first, var144[2].second)
val var147 = matchHexDigit(var144[3].first, var144[3].second)
val var148 = matchHexDigit(var144[4].first, var144[4].second)
val var149 = matchHexDigit(var144[5].first, var144[5].second)
val var150 = UnicodeLongEscape("000" + var145.toString() + var146.toString() + var147.toString() + var148.toString() + var149.toString(), nextId(), var140[2].first, var140[2].second)
var150
}
else -> {
val var151 = getSequenceElems(history, 139, listOf(140,114,114,114,114), var140[2].first, var140[2].second)
val var152 = matchHexDigit(var151[1].first, var151[1].second)
val var153 = matchHexDigit(var151[2].first, var151[2].second)
val var154 = matchHexDigit(var151[3].first, var151[3].second)
val var155 = matchHexDigit(var151[4].first, var151[4].second)
val var156 = UnicodeLongEscape("0010" + var152.toString() + var153.toString() + var154.toString() + var155.toString(), nextId(), var140[2].first, var140[2].second)
var156
}
}
return var143
}

fun matchPlainChar(beginGen: Int, endGen: Int): PlainChar {
val var157 = PlainChar(source[beginGen], nextId(), beginGen, endGen)
return var157
}

fun matchHexDigit(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchExponent(beginGen: Int, endGen: Int): Exponent {
val var158 = getSequenceElems(history, 210, listOf(211,212,204), beginGen, endGen)
val var159 = history[var158[1].second].findByBeginGenOpt(82, 1, var158[1].first)
val var160 = history[var158[1].second].findByBeginGenOpt(213, 1, var158[1].first)
check(hasSingleTrue(var159 != null, var160 != null))
val var161 = when {
var159 != null -> null
else -> source[var158[1].first]
}
val var162 = matchDecimals(var158[2].first, var158[2].second)
val var163 = Exponent(var161, var162, nextId(), beginGen, endGen)
return var163
}

fun matchHexEscape(beginGen: Int, endGen: Int): HexEscape {
val var164 = getSequenceElems(history, 110, listOf(111,112,113), beginGen, endGen)
val var165 = unrollRepeat1(history, 113, 114, 114, 116, var164[2].first, var164[2].second).map { k ->
val var166 = matchHexDigit(k.first, k.second)
var166
}
val var167 = HexEscape(var165.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var167
}

fun matchComment(beginGen: Int, endGen: Int): Comment {
val var168 = history[endGen].findByBeginGenOpt(14, 1, beginGen)
val var169 = history[endGen].findByBeginGenOpt(28, 1, beginGen)
check(hasSingleTrue(var168 != null, var169 != null))
val var170 = when {
var168 != null -> {
val var171 = matchLineComment(beginGen, endGen)
var171
}
else -> {
val var172 = matchBlockComment(beginGen, endGen)
var172
}
}
return var170
}

fun matchLineComment(beginGen: Int, endGen: Int): LineComment {
val var173 = getSequenceElems(history, 15, listOf(16,19,25), beginGen, endGen)
val var174 = unrollRepeat0(history, 19, 21, 9, 20, var173[1].first, var173[1].second).map { k ->
source[k.first]
}
val var175 = LineComment(var174.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var175
}

fun matchBlockComment(beginGen: Int, endGen: Int): BlockComment {
val var176 = history[endGen].findByBeginGenOpt(29, 4, beginGen)
val var177 = history[endGen].findByBeginGenOpt(41, 1, beginGen)
check(hasSingleTrue(var176 != null, var177 != null))
val var178 = when {
var176 != null -> {
val var179 = getSequenceElems(history, 29, listOf(30,33,23,39), beginGen, endGen)
val var180 = unrollRepeat0(history, 33, 35, 9, 34, var179[1].first, var179[1].second).map { k ->
val var181 = getSequenceElems(history, 36, listOf(23,37), k.first, k.second)
source[var181[0].first]
}
val var182 = BlockComment(var180.joinToString("") { it.toString() } + source[var179[2].first].toString(), nextId(), beginGen, endGen)
var182
}
else -> {
val var183 = BlockComment("", nextId(), beginGen, endGen)
var183
}
}
return var178
}

fun matchIntLiteral(beginGen: Int, endGen: Int): IntLiteral {
val var184 = history[endGen].findByBeginGenOpt(188, 1, beginGen)
val var185 = history[endGen].findByBeginGenOpt(190, 1, beginGen)
val var186 = history[endGen].findByBeginGenOpt(195, 1, beginGen)
val var187 = history[endGen].findByBeginGenOpt(199, 1, beginGen)
check(hasSingleTrue(var184 != null, var185 != null, var186 != null, var187 != null))
val var188 = when {
var184 != null -> {
val var189 = ZeroIntLiteral(nextId(), beginGen, endGen)
var189
}
var185 != null -> {
val var190 = matchDecimalLiteral(beginGen, endGen)
var190
}
var186 != null -> {
val var191 = matchOctalLiteral(beginGen, endGen)
var191
}
else -> {
val var192 = matchHexLiteral(beginGen, endGen)
var192
}
}
return var188
}

fun matchDecimalLiteral(beginGen: Int, endGen: Int): DecimalLiteral {
val var193 = getSequenceElems(history, 191, listOf(192,193), beginGen, endGen)
val var194 = unrollRepeat0(history, 193, 73, 9, 194, var193[1].first, var193[1].second).map { k ->
source[k.first]
}
val var195 = DecimalLiteral(source[var193[0].first].toString() + var194.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var195
}

fun matchOctalLiteral(beginGen: Int, endGen: Int): OctalLiteral {
val var196 = getSequenceElems(history, 196, listOf(137,197), beginGen, endGen)
val var197 = unrollRepeat1(history, 197, 119, 119, 198, var196[1].first, var196[1].second).map { k ->
source[k.first]
}
val var198 = OctalLiteral(var197.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var198
}

fun matchHexLiteral(beginGen: Int, endGen: Int): HexLiteral {
val var199 = getSequenceElems(history, 200, listOf(137,112,113), beginGen, endGen)
val var200 = unrollRepeat1(history, 113, 114, 114, 116, var199[2].first, var199[2].second).map { k ->
val var201 = matchHexDigit(k.first, k.second)
var201
}
val var202 = HexLiteral(var200.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var202
}

fun matchOctEscape(beginGen: Int, endGen: Int): OctEscape {
val var203 = getSequenceElems(history, 118, listOf(111,119,120), beginGen, endGen)
val var204 = history[var203[2].second].findByBeginGenOpt(82, 1, var203[2].first)
val var205 = history[var203[2].second].findByBeginGenOpt(121, 1, var203[2].first)
check(hasSingleTrue(var204 != null, var205 != null))
val var206 = when {
var204 != null -> null
else -> {
val var207 = getSequenceElems(history, 122, listOf(119,123), var203[2].first, var203[2].second)
val var208 = history[var207[1].second].findByBeginGenOpt(82, 1, var207[1].first)
val var209 = history[var207[1].second].findByBeginGenOpt(119, 1, var207[1].first)
check(hasSingleTrue(var208 != null, var209 != null))
val var210 = when {
var208 != null -> null
else -> source[var207[1].first]
}
var210
}
}
val var211 = OctEscape(source[var203[1].first].toString() + (var206?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
return var211
}

fun matchUnicodeEscape(beginGen: Int, endGen: Int): UnicodeEscape {
val var212 = getSequenceElems(history, 128, listOf(111,129,114,114,114,114), beginGen, endGen)
val var213 = matchHexDigit(var212[2].first, var212[2].second)
val var214 = matchHexDigit(var212[3].first, var212[3].second)
val var215 = matchHexDigit(var212[4].first, var212[4].second)
val var216 = matchHexDigit(var212[5].first, var212[5].second)
val var217 = UnicodeEscape(var213.toString() + var214.toString() + var215.toString() + var216.toString(), nextId(), beginGen, endGen)
return var217
}

fun matchIdent(beginGen: Int, endGen: Int): Ident {
val var218 = history[endGen].findByBeginGenOpt(63, 1, beginGen)
val var219 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
check(hasSingleTrue(var218 != null, var219 != null))
val var220 = when {
var218 != null -> {
val var221 = matchIdentName(beginGen, endGen)
val var222 = Ident(var221, nextId(), beginGen, endGen)
var222
}
else -> {
val var223 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var224 = matchIdentName(var223[1].first, var223[1].second)
val var225 = Ident(var224, nextId(), beginGen, endGen)
var225
}
}
return var220
}

fun matchIdentName(beginGen: Int, endGen: Int): String {
val var226 = getSequenceElems(history, 66, listOf(67,69), beginGen, endGen)
val var227 = matchLetter(var226[0].first, var226[0].second)
val var228 = unrollRepeat0(history, 69, 71, 9, 70, var226[1].first, var226[1].second).map { k ->
val var229 = history[k.second].findByBeginGenOpt(67, 1, k.first)
val var230 = history[k.second].findByBeginGenOpt(72, 1, k.first)
val var231 = history[k.second].findByBeginGenOpt(74, 1, k.first)
check(hasSingleTrue(var229 != null, var230 != null, var231 != null))
val var232 = when {
var229 != null -> {
val var233 = matchLetter(k.first, k.second)
var233
}
var230 != null -> {
val var234 = matchDecimalDigit(k.first, k.second)
var234
}
else -> source[k.first]
}
var232
}
return var227.toString() + var228.joinToString("") { it.toString() }
}

fun matchTopLevelDef(beginGen: Int, endGen: Int): TopLevelDef {
val var235 = history[endGen].findByBeginGenOpt(229, 1, beginGen)
val var236 = history[endGen].findByBeginGenOpt(351, 1, beginGen)
val var237 = history[endGen].findByBeginGenOpt(378, 1, beginGen)
val var238 = history[endGen].findByBeginGenOpt(473, 1, beginGen)
check(hasSingleTrue(var235 != null, var236 != null, var237 != null, var238 != null))
val var239 = when {
var235 != null -> {
val var240 = matchServiceDef(beginGen, endGen)
var240
}
var236 != null -> {
val var241 = matchEnumDef(beginGen, endGen)
var241
}
var237 != null -> {
val var242 = matchMessageDef(beginGen, endGen)
var242
}
else -> {
val var243 = matchSealedDef(beginGen, endGen)
var243
}
}
return var239
}

fun matchServiceDef(beginGen: Int, endGen: Int): ServiceDef {
val var244 = getSequenceElems(history, 230, listOf(231,7,62,7,236,237,7,377), beginGen, endGen)
val var245 = matchIdent(var244[2].first, var244[2].second)
val var246 = unrollRepeat0(history, 237, 239, 9, 238, var244[5].first, var244[5].second).map { k ->
val var247 = matchServiceMemberWS(k.first, k.second)
var247
}
val var248 = ServiceDef(var245, var246, nextId(), beginGen, endGen)
return var248
}

fun matchServiceMemberWS(beginGen: Int, endGen: Int): ServiceMemberWS {
val var249 = getSequenceElems(history, 240, listOf(7,241), beginGen, endGen)
val var250 = matchWS(var249[0].first, var249[0].second)
val var251 = matchServiceMember(var249[1].first, var249[1].second)
val var252 = ServiceMemberWS(var250, var251, nextId(), beginGen, endGen)
return var252
}

fun matchServiceMember(beginGen: Int, endGen: Int): ServiceMember {
val var253 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var254 = history[endGen].findByBeginGenOpt(242, 1, beginGen)
check(hasSingleTrue(var253 != null, var254 != null))
val var255 = when {
var253 != null -> {
val var256 = matchOptionDef(beginGen, endGen)
var256
}
else -> {
val var257 = matchRpcDef(beginGen, endGen)
var257
}
}
return var255
}

fun matchRpcDef(beginGen: Int, endGen: Int): RpcDef {
val var258 = getSequenceElems(history, 243, listOf(244,7,62,7,248,7,249,7,454,7,249,456,335), beginGen, endGen)
val var259 = matchIdent(var258[2].first, var258[2].second)
val var260 = matchType(var258[6].first, var258[6].second)
val var261 = matchType(var258[10].first, var258[10].second)
val var262 = history[var258[11].second].findByBeginGenOpt(82, 1, var258[11].first)
val var263 = history[var258[11].second].findByBeginGenOpt(457, 1, var258[11].first)
check(hasSingleTrue(var262 != null, var263 != null))
val var264 = when {
var262 != null -> null
else -> {
val var265 = getSequenceElems(history, 458, listOf(7,459,7,465), var258[11].first, var258[11].second)
val var266 = matchRpcTypeWheres(var265[3].first, var265[3].second)
var266
}
}
val var267 = history[var258[12].second].findByBeginGenOpt(82, 1, var258[12].first)
val var268 = history[var258[12].second].findByBeginGenOpt(336, 1, var258[12].first)
check(hasSingleTrue(var267 != null, var268 != null))
val var269 = when {
var267 != null -> null
else -> {
val var270 = getSequenceElems(history, 337, listOf(7,338), var258[12].first, var258[12].second)
val var271 = matchFieldOptions(var270[1].first, var270[1].second)
var271
}
}
val var272 = RpcDef(var259, var260, var261, var264, var269, nextId(), beginGen, endGen)
return var272
}

fun matchMessageDef(beginGen: Int, endGen: Int): MessageDef {
val var273 = getSequenceElems(history, 379, listOf(380,7,62,7,236,329,7,377), beginGen, endGen)
val var274 = matchIdent(var273[2].first, var273[2].second)
val var276 = history[var273[5].second].findByBeginGenOpt(82, 1, var273[5].first)
val var277 = history[var273[5].second].findByBeginGenOpt(330, 1, var273[5].first)
check(hasSingleTrue(var276 != null, var277 != null))
val var278 = when {
var276 != null -> null
else -> {
val var279 = matchMessageMembersWS(var273[5].first, var273[5].second)
var279
}
}
val var275 = var278
val var280 = MessageDef(var274, (var275 ?: listOf()), nextId(), beginGen, endGen)
return var280
}

fun matchMessageMembersWS(beginGen: Int, endGen: Int): List<MessageMemberDefWS> {
val var281 = getSequenceElems(history, 331, listOf(7,332,423), beginGen, endGen)
val var282 = matchWS(var281[0].first, var281[0].second)
val var283 = matchMessageMemberDef(var281[1].first, var281[1].second)
val var284 = MessageMemberDefWS(var282, var283, nextId(), beginGen, endGen)
val var285 = unrollRepeat0(history, 423, 425, 9, 424, var281[2].first, var281[2].second).map { k ->
val var286 = getSequenceElems(history, 426, listOf(368,332), k.first, k.second)
val var287 = history[var286[0].second].findByBeginGenOpt(369, 1, var286[0].first)
val var288 = history[var286[0].second].findByBeginGenOpt(371, 1, var286[0].first)
check(hasSingleTrue(var287 != null, var288 != null))
val var289 = when {
var287 != null -> {
val var290 = getSequenceElems(history, 370, listOf(7,310,7), var286[0].first, var286[0].second)
val var291 = matchWS(var290[2].first, var290[2].second)
var291
}
else -> {
val var292 = matchWSNL(var286[0].first, var286[0].second)
var292
}
}
val var293 = matchMessageMemberDef(var286[1].first, var286[1].second)
val var294 = MessageMemberDefWS(var289, var293, nextId(), k.first, k.second)
var294
}
return listOf(var284) + var285
}

fun matchWSNL(beginGen: Int, endGen: Int): List<Comment?> {
val var295 = getSequenceElems(history, 372, listOf(373,376,7), beginGen, endGen)
val var296 = history[var295[1].second].findByBeginGenOpt(14, 1, var295[1].first)
val var297 = history[var295[1].second].findByBeginGenOpt(24, 1, var295[1].first)
check(hasSingleTrue(var296 != null, var297 != null))
val var298 = when {
var296 != null -> {
val var299 = matchLineComment(var295[1].first, var295[1].second)
var299
}
else -> null
}
val var300 = matchWS(var295[2].first, var295[2].second)
return listOf(var298) + var300
}

fun matchEnumDef(beginGen: Int, endGen: Int): EnumDef {
val var301 = getSequenceElems(history, 352, listOf(353,7,62,7,236,355,7,377), beginGen, endGen)
val var302 = matchIdent(var301[2].first, var301[2].second)
val var304 = history[var301[5].second].findByBeginGenOpt(82, 1, var301[5].first)
val var305 = history[var301[5].second].findByBeginGenOpt(356, 1, var301[5].first)
check(hasSingleTrue(var304 != null, var305 != null))
val var306 = when {
var304 != null -> null
else -> {
val var307 = matchEnumMembersWS(var301[5].first, var301[5].second)
var307
}
}
val var303 = var306
val var308 = EnumDef(var302, (var303 ?: listOf()), nextId(), beginGen, endGen)
return var308
}

fun matchEnumMembersWS(beginGen: Int, endGen: Int): List<EnumMemberDefWS> {
val var309 = getSequenceElems(history, 357, listOf(7,358,364), beginGen, endGen)
val var310 = matchWS(var309[0].first, var309[0].second)
val var311 = matchEnumMemberDef(var309[1].first, var309[1].second)
val var312 = EnumMemberDefWS(var310, var311, nextId(), beginGen, endGen)
val var313 = unrollRepeat0(history, 364, 366, 9, 365, var309[2].first, var309[2].second).map { k ->
val var314 = getSequenceElems(history, 367, listOf(368,358), k.first, k.second)
val var315 = history[var314[0].second].findByBeginGenOpt(369, 1, var314[0].first)
val var316 = history[var314[0].second].findByBeginGenOpt(371, 1, var314[0].first)
check(hasSingleTrue(var315 != null, var316 != null))
val var317 = when {
var315 != null -> {
val var318 = getSequenceElems(history, 370, listOf(7,310,7), var314[0].first, var314[0].second)
val var319 = matchWS(var318[2].first, var318[2].second)
var319
}
else -> {
val var320 = matchWSNL(var314[0].first, var314[0].second)
var320
}
}
val var321 = matchEnumMemberDef(var314[1].first, var314[1].second)
val var322 = EnumMemberDefWS(var317, var321, nextId(), k.first, k.second)
var322
}
return listOf(var312) + var313
}

fun matchEnumMemberDef(beginGen: Int, endGen: Int): EnumMemberDef {
val var323 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var324 = history[endGen].findByBeginGenOpt(359, 1, beginGen)
check(hasSingleTrue(var323 != null, var324 != null))
val var325 = when {
var323 != null -> {
val var326 = matchOptionDef(beginGen, endGen)
var326
}
else -> {
val var327 = matchEnumFieldDef(beginGen, endGen)
var327
}
}
return var325
}

fun matchEnumFieldDef(beginGen: Int, endGen: Int): EnumFieldDef {
val var328 = getSequenceElems(history, 360, listOf(361,185,7,62,335), beginGen, endGen)
val var329 = history[var328[0].second].findByBeginGenOpt(82, 1, var328[0].first)
val var330 = history[var328[0].second].findByBeginGenOpt(362, 1, var328[0].first)
check(hasSingleTrue(var329 != null, var330 != null))
val var331 = when {
var329 != null -> null
else -> {
val var332 = getSequenceElems(history, 363, listOf(184,7), var328[0].first, var328[0].second)
source[var332[0].first]
}
}
val var333 = matchIntLiteral(var328[1].first, var328[1].second)
val var334 = matchIdent(var328[3].first, var328[3].second)
val var335 = history[var328[4].second].findByBeginGenOpt(82, 1, var328[4].first)
val var336 = history[var328[4].second].findByBeginGenOpt(336, 1, var328[4].first)
check(hasSingleTrue(var335 != null, var336 != null))
val var337 = when {
var335 != null -> null
else -> {
val var338 = getSequenceElems(history, 337, listOf(7,338), var328[4].first, var328[4].second)
val var339 = matchFieldOptions(var338[1].first, var338[1].second)
var339
}
}
val var340 = EnumFieldDef(var331 != null, var333, var334, var337, nextId(), beginGen, endGen)
return var340
}

fun matchSealedDef(beginGen: Int, endGen: Int): SealedDef {
val var341 = getSequenceElems(history, 474, listOf(429,7,62,7,236,436,7,377), beginGen, endGen)
val var342 = matchIdent(var341[2].first, var341[2].second)
val var344 = history[var341[5].second].findByBeginGenOpt(82, 1, var341[5].first)
val var345 = history[var341[5].second].findByBeginGenOpt(437, 1, var341[5].first)
check(hasSingleTrue(var344 != null, var345 != null))
val var346 = when {
var344 != null -> null
else -> {
val var347 = matchSealedMembersWS(var341[5].first, var341[5].second)
var347
}
}
val var343 = var346
val var348 = SealedDef(var342, (var343 ?: listOf()), nextId(), beginGen, endGen)
return var348
}

fun matchSealedMembersWS(beginGen: Int, endGen: Int): List<SealedMemberDefWS> {
val var349 = getSequenceElems(history, 438, listOf(7,439,440), beginGen, endGen)
val var350 = matchWS(var349[0].first, var349[0].second)
val var351 = matchSealedMemberDef(var349[1].first, var349[1].second)
val var352 = SealedMemberDefWS(var350, var351, nextId(), beginGen, endGen)
val var353 = unrollRepeat0(history, 440, 442, 9, 441, var349[2].first, var349[2].second).map { k ->
val var354 = getSequenceElems(history, 443, listOf(368,439), k.first, k.second)
val var355 = history[var354[0].second].findByBeginGenOpt(369, 1, var354[0].first)
val var356 = history[var354[0].second].findByBeginGenOpt(371, 1, var354[0].first)
check(hasSingleTrue(var355 != null, var356 != null))
val var357 = when {
var355 != null -> {
val var358 = getSequenceElems(history, 370, listOf(7,310,7), var354[0].first, var354[0].second)
val var359 = matchWS(var358[2].first, var358[2].second)
var359
}
else -> {
val var360 = matchWSNL(var354[0].first, var354[0].second)
var360
}
}
val var361 = matchSealedMemberDef(var354[1].first, var354[1].second)
val var362 = SealedMemberDefWS(var357, var361, nextId(), k.first, k.second)
var362
}
return listOf(var352) + var353
}

fun matchSealedMemberDef(beginGen: Int, endGen: Int): SealedMemberDef {
val var363 = matchFieldDef(beginGen, endGen)
return var363
}

fun matchFieldDef(beginGen: Int, endGen: Int): FieldDef {
val var364 = getSequenceElems(history, 334, listOf(185,7,62,7,248,7,249,335), beginGen, endGen)
val var365 = matchIntLiteral(var364[0].first, var364[0].second)
val var366 = matchIdent(var364[2].first, var364[2].second)
val var367 = matchType(var364[6].first, var364[6].second)
val var368 = history[var364[7].second].findByBeginGenOpt(82, 1, var364[7].first)
val var369 = history[var364[7].second].findByBeginGenOpt(336, 1, var364[7].first)
check(hasSingleTrue(var368 != null, var369 != null))
val var370 = when {
var368 != null -> null
else -> {
val var371 = getSequenceElems(history, 337, listOf(7,338), var364[7].first, var364[7].second)
val var372 = matchFieldOptions(var371[1].first, var371[1].second)
var372
}
}
val var373 = FieldDef(var365, var366, var367, var370, nextId(), beginGen, endGen)
return var373
}

fun matchLetter(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchType(beginGen: Int, endGen: Int): Type {
val var374 = history[endGen].findByBeginGenOpt(250, 1, beginGen)
val var375 = history[endGen].findByBeginGenOpt(290, 1, beginGen)
val var376 = history[endGen].findByBeginGenOpt(298, 1, beginGen)
val var377 = history[endGen].findByBeginGenOpt(304, 1, beginGen)
val var378 = history[endGen].findByBeginGenOpt(311, 1, beginGen)
val var379 = history[endGen].findByBeginGenOpt(317, 1, beginGen)
val var380 = history[endGen].findByBeginGenOpt(427, 1, beginGen)
val var381 = history[endGen].findByBeginGenOpt(444, 1, beginGen)
val var382 = history[endGen].findByBeginGenOpt(451, 1, beginGen)
check(hasSingleTrue(var374 != null, var375 != null, var376 != null, var377 != null, var378 != null, var379 != null, var380 != null, var381 != null, var382 != null))
val var383 = when {
var374 != null -> {
val var384 = matchPrimitiveType(beginGen, endGen)
val var385 = PrimitiveType(var384, nextId(), beginGen, endGen)
var385
}
var375 != null -> {
val var386 = matchRepeatedType(beginGen, endGen)
var386
}
var376 != null -> {
val var387 = matchOptionalType(beginGen, endGen)
var387
}
var377 != null -> {
val var388 = matchMapType(beginGen, endGen)
var388
}
var378 != null -> {
val var389 = matchStreamType(beginGen, endGen)
var389
}
var379 != null -> {
val var390 = matchOnTheFlyMessageType(beginGen, endGen)
var390
}
var380 != null -> {
val var391 = matchOnTheFlySealedMessageType(beginGen, endGen)
var391
}
var381 != null -> {
val var392 = matchOnTheFlyEnumType(beginGen, endGen)
var392
}
else -> {
val var393 = matchTypeName(beginGen, endGen)
var393
}
}
return var383
}

fun matchTypeName(beginGen: Int, endGen: Int): TypeName {
val var394 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var395 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var396 = history[endGen].findByBeginGenOpt(452, 1, beginGen)
check(hasSingleTrue(var394 != null, var395 != null, var396 != null))
val var397 = when {
var394 != null -> {
val var398 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var399 = matchIdentName(var398[1].first, var398[1].second)
val var400 = SingleName(var399, nextId(), beginGen, endGen)
var400
}
var395 != null -> {
val var401 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var402 = matchIdent(var401[0].first, var401[0].second)
val var403 = unrollRepeat1(history, 179, 79, 79, 180, var401[1].first, var401[1].second).map { k ->
val var404 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var405 = matchIdent(var404[3].first, var404[3].second)
var405
}
val var406 = MultiName(listOf(var402) + var403, nextId(), beginGen, endGen)
var406
}
else -> {
val var407 = matchIdentName(beginGen, endGen)
val var408 = SingleName(var407, nextId(), beginGen, endGen)
var408
}
}
return var397
}

fun matchOnTheFlyMessageType(beginGen: Int, endGen: Int): OnTheFlyMessageType {
val var409 = getSequenceElems(history, 318, listOf(319,236,329,7,377), beginGen, endGen)
val var410 = history[var409[0].second].findByBeginGenOpt(82, 1, var409[0].first)
val var411 = history[var409[0].second].findByBeginGenOpt(320, 1, var409[0].first)
check(hasSingleTrue(var410 != null, var411 != null))
val var412 = when {
var410 != null -> null
else -> {
val var413 = getSequenceElems(history, 321, listOf(322,7), var409[0].first, var409[0].second)
val var414 = matchIdentNoSealedEnum(var413[0].first, var413[0].second)
var414
}
}
val var416 = history[var409[2].second].findByBeginGenOpt(82, 1, var409[2].first)
val var417 = history[var409[2].second].findByBeginGenOpt(330, 1, var409[2].first)
check(hasSingleTrue(var416 != null, var417 != null))
val var418 = when {
var416 != null -> null
else -> {
val var419 = matchMessageMembersWS(var409[2].first, var409[2].second)
var419
}
}
val var415 = var418
val var420 = OnTheFlyMessageType(var412, (var415 ?: listOf()), nextId(), beginGen, endGen)
return var420
}

fun matchIdentNoSealedEnum(beginGen: Int, endGen: Int): Ident {
val var421 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var422 = history[endGen].findByBeginGenOpt(323, 1, beginGen)
check(hasSingleTrue(var421 != null, var422 != null))
val var423 = when {
var421 != null -> {
val var424 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var425 = matchIdentName(var424[1].first, var424[1].second)
val var426 = Ident(var425, nextId(), beginGen, endGen)
var426
}
else -> {
val var427 = matchIdentName(beginGen, endGen)
val var428 = Ident(var427, nextId(), beginGen, endGen)
var428
}
}
return var423
}

fun matchOnTheFlyEnumType(beginGen: Int, endGen: Int): OnTheFlyEnumType {
val var429 = getSequenceElems(history, 445, listOf(353,446,7,236,355,7,377), beginGen, endGen)
val var430 = history[var429[1].second].findByBeginGenOpt(82, 1, var429[1].first)
val var431 = history[var429[1].second].findByBeginGenOpt(447, 1, var429[1].first)
check(hasSingleTrue(var430 != null, var431 != null))
val var432 = when {
var430 != null -> null
else -> {
val var433 = getSequenceElems(history, 448, listOf(7,449), var429[1].first, var429[1].second)
val var434 = matchIdentNoEnum(var433[1].first, var433[1].second)
var434
}
}
val var436 = history[var429[4].second].findByBeginGenOpt(82, 1, var429[4].first)
val var437 = history[var429[4].second].findByBeginGenOpt(356, 1, var429[4].first)
check(hasSingleTrue(var436 != null, var437 != null))
val var438 = when {
var436 != null -> null
else -> {
val var439 = matchEnumMembersWS(var429[4].first, var429[4].second)
var439
}
}
val var435 = var438
val var440 = OnTheFlyEnumType(var432, (var435 ?: listOf()), nextId(), beginGen, endGen)
return var440
}

fun matchIdentNoEnum(beginGen: Int, endGen: Int): Ident {
val var441 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var442 = history[endGen].findByBeginGenOpt(450, 1, beginGen)
check(hasSingleTrue(var441 != null, var442 != null))
val var443 = when {
var441 != null -> {
val var444 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var445 = matchIdentName(var444[1].first, var444[1].second)
val var446 = Ident(var445, nextId(), beginGen, endGen)
var446
}
else -> {
val var447 = matchIdentName(beginGen, endGen)
val var448 = Ident(var447, nextId(), beginGen, endGen)
var448
}
}
return var443
}

fun matchMapType(beginGen: Int, endGen: Int): MapType {
val var449 = getSequenceElems(history, 305, listOf(306,7,296,7,249,7,310,7,249,7,297), beginGen, endGen)
val var450 = matchType(var449[4].first, var449[4].second)
val var451 = matchType(var449[8].first, var449[8].second)
val var452 = MapType(var450, var451, nextId(), beginGen, endGen)
return var452
}

fun matchRepeatedType(beginGen: Int, endGen: Int): RepeatedType {
val var453 = getSequenceElems(history, 291, listOf(292,7,296,7,249,7,297), beginGen, endGen)
val var454 = matchType(var453[4].first, var453[4].second)
val var455 = RepeatedType(var454, nextId(), beginGen, endGen)
return var455
}

fun matchStreamType(beginGen: Int, endGen: Int): StreamType {
val var456 = getSequenceElems(history, 312, listOf(313,7,296,7,249,7,297), beginGen, endGen)
val var457 = matchType(var456[4].first, var456[4].second)
val var458 = StreamType(var457, nextId(), beginGen, endGen)
return var458
}

fun matchRpcTypeWheres(beginGen: Int, endGen: Int): RpcTypeWheres {
val var459 = getSequenceElems(history, 466, listOf(467,469), beginGen, endGen)
val var460 = matchRpcTypeWhere(var459[0].first, var459[0].second)
val var461 = unrollRepeat0(history, 469, 471, 9, 470, var459[1].first, var459[1].second).map { k ->
val var462 = getSequenceElems(history, 472, listOf(7,310,7,467), k.first, k.second)
val var463 = matchRpcTypeWhere(var462[3].first, var462[3].second)
var463
}
val var464 = RpcTypeWheres(listOf(var460) + var461, nextId(), beginGen, endGen)
return var464
}

fun matchRpcTypeWhere(beginGen: Int, endGen: Int): RpcTypeWhere {
val var465 = getSequenceElems(history, 468, listOf(62,7,167,7,249), beginGen, endGen)
val var466 = matchIdent(var465[0].first, var465[0].second)
val var467 = matchType(var465[4].first, var465[4].second)
val var468 = RpcTypeWhere(var466, var467, nextId(), beginGen, endGen)
return var468
}

fun matchOnTheFlySealedMessageType(beginGen: Int, endGen: Int): OnTheFlySealedMessageType {
val var469 = getSequenceElems(history, 428, listOf(429,431,7,236,436,7,377), beginGen, endGen)
val var470 = history[var469[1].second].findByBeginGenOpt(82, 1, var469[1].first)
val var471 = history[var469[1].second].findByBeginGenOpt(432, 1, var469[1].first)
check(hasSingleTrue(var470 != null, var471 != null))
val var472 = when {
var470 != null -> null
else -> {
val var473 = getSequenceElems(history, 433, listOf(7,434), var469[1].first, var469[1].second)
val var474 = matchIdentNoSealed(var473[1].first, var473[1].second)
var474
}
}
val var476 = history[var469[4].second].findByBeginGenOpt(82, 1, var469[4].first)
val var477 = history[var469[4].second].findByBeginGenOpt(437, 1, var469[4].first)
check(hasSingleTrue(var476 != null, var477 != null))
val var478 = when {
var476 != null -> null
else -> {
val var479 = matchSealedMembersWS(var469[4].first, var469[4].second)
var479
}
}
val var475 = var478
val var480 = OnTheFlySealedMessageType(var472, (var475 ?: listOf()), nextId(), beginGen, endGen)
return var480
}

fun matchIdentNoSealed(beginGen: Int, endGen: Int): Ident {
val var481 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var482 = history[endGen].findByBeginGenOpt(435, 1, beginGen)
check(hasSingleTrue(var481 != null, var482 != null))
val var483 = when {
var481 != null -> {
val var484 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var485 = matchIdentName(var484[1].first, var484[1].second)
val var486 = Ident(var485, nextId(), beginGen, endGen)
var486
}
else -> {
val var487 = matchIdentName(beginGen, endGen)
val var488 = Ident(var487, nextId(), beginGen, endGen)
var488
}
}
return var483
}

fun matchFieldOptions(beginGen: Int, endGen: Int): FieldOptions {
val var490 = getSequenceElems(history, 339, listOf(340,341,7,350), beginGen, endGen)
val var491 = history[var490[1].second].findByBeginGenOpt(82, 1, var490[1].first)
val var492 = history[var490[1].second].findByBeginGenOpt(342, 1, var490[1].first)
check(hasSingleTrue(var491 != null, var492 != null))
val var493 = when {
var491 != null -> null
else -> {
val var494 = getSequenceElems(history, 343, listOf(7,344,346), var490[1].first, var490[1].second)
val var495 = matchFieldOption(var494[1].first, var494[1].second)
val var496 = unrollRepeat0(history, 346, 348, 9, 347, var494[2].first, var494[2].second).map { k ->
val var497 = getSequenceElems(history, 349, listOf(7,310,7,344), k.first, k.second)
val var498 = matchFieldOption(var497[3].first, var497[3].second)
var498
}
listOf(var495) + var496
}
}
val var489 = var493
val var499 = FieldOptions((var489 ?: listOf()), nextId(), beginGen, endGen)
return var499
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var500 = getSequenceElems(history, 345, listOf(160,7,167,7,168), beginGen, endGen)
val var501 = matchOptionName(var500[0].first, var500[0].second)
val var502 = matchConstant(var500[4].first, var500[4].second)
val var503 = FieldOption(var501, var502, nextId(), beginGen, endGen)
return var503
}

fun matchMessageMemberDef(beginGen: Int, endGen: Int): MessageMemberDef {
val var504 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var505 = history[endGen].findByBeginGenOpt(333, 1, beginGen)
val var506 = history[endGen].findByBeginGenOpt(351, 1, beginGen)
val var507 = history[endGen].findByBeginGenOpt(378, 1, beginGen)
val var508 = history[endGen].findByBeginGenOpt(384, 1, beginGen)
val var509 = history[endGen].findByBeginGenOpt(398, 1, beginGen)
check(hasSingleTrue(var504 != null, var505 != null, var506 != null, var507 != null, var508 != null, var509 != null))
val var510 = when {
var504 != null -> {
val var511 = matchOptionDef(beginGen, endGen)
var511
}
var505 != null -> {
val var512 = matchFieldDef(beginGen, endGen)
var512
}
var506 != null -> {
val var513 = matchEnumDef(beginGen, endGen)
var513
}
var507 != null -> {
val var514 = matchMessageDef(beginGen, endGen)
var514
}
var508 != null -> {
val var515 = matchOneOfDef(beginGen, endGen)
var515
}
else -> {
val var516 = matchReservedDef(beginGen, endGen)
var516
}
}
return var510
}

fun matchOneOfDef(beginGen: Int, endGen: Int): OneOfDef {
val var517 = getSequenceElems(history, 385, listOf(386,7,62,7,236,390,7,377), beginGen, endGen)
val var518 = matchIdent(var517[2].first, var517[2].second)
val var520 = history[var517[5].second].findByBeginGenOpt(82, 1, var517[5].first)
val var521 = history[var517[5].second].findByBeginGenOpt(391, 1, var517[5].first)
check(hasSingleTrue(var520 != null, var521 != null))
val var522 = when {
var520 != null -> null
else -> {
val var523 = matchOneOfMembersWS(var517[5].first, var517[5].second)
var523
}
}
val var519 = var522
val var524 = OneOfDef(var518, (var519 ?: listOf()), nextId(), beginGen, endGen)
return var524
}

fun matchOneOfMembersWS(beginGen: Int, endGen: Int): List<OneOfMembersDefWS> {
val var525 = getSequenceElems(history, 392, listOf(7,393,394), beginGen, endGen)
val var526 = matchWS(var525[0].first, var525[0].second)
val var527 = matchOneOfMemberDef(var525[1].first, var525[1].second)
val var528 = OneOfMembersDefWS(var526, var527, nextId(), beginGen, endGen)
val var529 = unrollRepeat0(history, 394, 396, 9, 395, var525[2].first, var525[2].second).map { k ->
val var530 = getSequenceElems(history, 397, listOf(368,393), k.first, k.second)
val var531 = history[var530[0].second].findByBeginGenOpt(369, 1, var530[0].first)
val var532 = history[var530[0].second].findByBeginGenOpt(371, 1, var530[0].first)
check(hasSingleTrue(var531 != null, var532 != null))
val var533 = when {
var531 != null -> {
val var534 = getSequenceElems(history, 370, listOf(7,310,7), var530[0].first, var530[0].second)
val var535 = matchWS(var534[2].first, var534[2].second)
var535
}
else -> {
val var536 = matchWSNL(var530[0].first, var530[0].second)
var536
}
}
val var537 = matchOneOfMemberDef(var530[1].first, var530[1].second)
val var538 = OneOfMembersDefWS(var533, var537, nextId(), k.first, k.second)
var538
}
return listOf(var528) + var529
}

fun matchReservedDef(beginGen: Int, endGen: Int): ReservedDef {
val var539 = getSequenceElems(history, 399, listOf(400,7,165,7,404,416,420,7,166), beginGen, endGen)
val var540 = matchReservedItem(var539[4].first, var539[4].second)
val var541 = unrollRepeat0(history, 416, 418, 9, 417, var539[5].first, var539[5].second).map { k ->
val var542 = getSequenceElems(history, 419, listOf(7,310,7,404), k.first, k.second)
val var543 = matchReservedItem(var542[3].first, var542[3].second)
var543
}
val var544 = ReservedDef(listOf(var540) + var541, nextId(), beginGen, endGen)
return var544
}

fun matchReservedItem(beginGen: Int, endGen: Int): ReservedItem {
val var545 = history[endGen].findByBeginGenOpt(62, 1, beginGen)
val var546 = history[endGen].findByBeginGenOpt(405, 1, beginGen)
check(hasSingleTrue(var545 != null, var546 != null))
val var547 = when {
var545 != null -> {
val var548 = matchIdent(beginGen, endGen)
var548
}
else -> {
val var549 = matchReservedRange(beginGen, endGen)
var549
}
}
return var547
}

fun matchOneOfMemberDef(beginGen: Int, endGen: Int): OneOfMemberDef {
val var550 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var551 = history[endGen].findByBeginGenOpt(333, 1, beginGen)
check(hasSingleTrue(var550 != null, var551 != null))
val var552 = when {
var550 != null -> {
val var553 = matchOptionDef(beginGen, endGen)
var553
}
else -> {
val var554 = matchFieldDef(beginGen, endGen)
var554
}
}
return var552
}

fun matchReservedRange(beginGen: Int, endGen: Int): ReservedRange {
val var555 = getSequenceElems(history, 406, listOf(185,407), beginGen, endGen)
val var556 = matchIntLiteral(var555[0].first, var555[0].second)
val var557 = history[var555[1].second].findByBeginGenOpt(82, 1, var555[1].first)
val var558 = history[var555[1].second].findByBeginGenOpt(408, 1, var555[1].first)
check(hasSingleTrue(var557 != null, var558 != null))
val var559 = when {
var557 != null -> null
else -> {
val var560 = getSequenceElems(history, 409, listOf(7,410,7,412), var555[1].first, var555[1].second)
val var561 = matchReservedRangeEnd(var560[3].first, var560[3].second)
var561
}
}
val var562 = ReservedRange(var556, var559, nextId(), beginGen, endGen)
return var562
}

fun matchReservedRangeEnd(beginGen: Int, endGen: Int): ReservedRangeEnd {
val var563 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var564 = history[endGen].findByBeginGenOpt(413, 1, beginGen)
check(hasSingleTrue(var563 != null, var564 != null))
val var565 = when {
var563 != null -> {
val var566 = matchIntLiteral(beginGen, endGen)
var566
}
else -> {
val var567 = Max(nextId(), beginGen, endGen)
var567
}
}
return var565
}

fun matchPrimitiveType(beginGen: Int, endGen: Int): PrimitiveTypeEnum {
val var568 = history[endGen].findByBeginGenOpt(252, 1, beginGen)
val var569 = history[endGen].findByBeginGenOpt(256, 1, beginGen)
val var570 = history[endGen].findByBeginGenOpt(258, 1, beginGen)
val var571 = history[endGen].findByBeginGenOpt(262, 1, beginGen)
val var572 = history[endGen].findByBeginGenOpt(266, 1, beginGen)
val var573 = history[endGen].findByBeginGenOpt(268, 1, beginGen)
val var574 = history[endGen].findByBeginGenOpt(270, 1, beginGen)
val var575 = history[endGen].findByBeginGenOpt(272, 1, beginGen)
val var576 = history[endGen].findByBeginGenOpt(274, 1, beginGen)
val var577 = history[endGen].findByBeginGenOpt(277, 1, beginGen)
val var578 = history[endGen].findByBeginGenOpt(279, 1, beginGen)
val var579 = history[endGen].findByBeginGenOpt(281, 1, beginGen)
val var580 = history[endGen].findByBeginGenOpt(283, 1, beginGen)
val var581 = history[endGen].findByBeginGenOpt(285, 1, beginGen)
val var582 = history[endGen].findByBeginGenOpt(287, 1, beginGen)
check(hasSingleTrue(var568 != null, var569 != null, var570 != null, var571 != null, var572 != null, var573 != null, var574 != null, var575 != null, var576 != null, var577 != null, var578 != null, var579 != null, var580 != null, var581 != null, var582 != null))
val var583 = when {
var568 != null -> PrimitiveTypeEnum.DOUBLE
var569 != null -> PrimitiveTypeEnum.FLOAT
var570 != null -> PrimitiveTypeEnum.INT32
var571 != null -> PrimitiveTypeEnum.INT64
var572 != null -> PrimitiveTypeEnum.UINT32
var573 != null -> PrimitiveTypeEnum.UINT64
var574 != null -> PrimitiveTypeEnum.SINT32
var575 != null -> PrimitiveTypeEnum.SINT64
var576 != null -> PrimitiveTypeEnum.FIXED32
var577 != null -> PrimitiveTypeEnum.FIXED64
var578 != null -> PrimitiveTypeEnum.SFIXED32
var579 != null -> PrimitiveTypeEnum.SFIXED64
var580 != null -> PrimitiveTypeEnum.BOOL
var581 != null -> PrimitiveTypeEnum.STRING
else -> PrimitiveTypeEnum.BYTES
}
return var583
}

fun matchOptionalType(beginGen: Int, endGen: Int): OptionalType {
val var584 = getSequenceElems(history, 299, listOf(300,7,296,7,249,7,297), beginGen, endGen)
val var585 = matchType(var584[4].first, var584[4].second)
val var586 = OptionalType(var585, nextId(), beginGen, endGen)
return var586
}

}
