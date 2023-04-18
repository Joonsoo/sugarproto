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

data class CommonFieldDef(
  val field: FieldDef,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): SealedMemberDef, AstNode

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
val var236 = history[endGen].findByBeginGenOpt(347, 1, beginGen)
val var237 = history[endGen].findByBeginGenOpt(374, 1, beginGen)
val var238 = history[endGen].findByBeginGenOpt(475, 1, beginGen)
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
val var244 = getSequenceElems(history, 230, listOf(231,7,62,7,236,237,7,373), beginGen, endGen)
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
val var258 = getSequenceElems(history, 243, listOf(244,7,62,7,248,7,249,7,456,7,249,458,331), beginGen, endGen)
val var259 = matchIdent(var258[2].first, var258[2].second)
val var260 = matchType(var258[6].first, var258[6].second)
val var261 = matchType(var258[10].first, var258[10].second)
val var262 = history[var258[11].second].findByBeginGenOpt(82, 1, var258[11].first)
val var263 = history[var258[11].second].findByBeginGenOpt(459, 1, var258[11].first)
check(hasSingleTrue(var262 != null, var263 != null))
val var264 = when {
var262 != null -> null
else -> {
val var265 = getSequenceElems(history, 460, listOf(7,461,7,467), var258[11].first, var258[11].second)
val var266 = matchRpcTypeWheres(var265[3].first, var265[3].second)
var266
}
}
val var267 = history[var258[12].second].findByBeginGenOpt(82, 1, var258[12].first)
val var268 = history[var258[12].second].findByBeginGenOpt(332, 1, var258[12].first)
check(hasSingleTrue(var267 != null, var268 != null))
val var269 = when {
var267 != null -> null
else -> {
val var270 = getSequenceElems(history, 333, listOf(7,334), var258[12].first, var258[12].second)
val var271 = matchFieldOptions(var270[1].first, var270[1].second)
var271
}
}
val var272 = RpcDef(var259, var260, var261, var264, var269, nextId(), beginGen, endGen)
return var272
}

fun matchMessageDef(beginGen: Int, endGen: Int): MessageDef {
val var273 = getSequenceElems(history, 375, listOf(376,7,62,7,236,325,7,373), beginGen, endGen)
val var274 = matchIdent(var273[2].first, var273[2].second)
val var276 = history[var273[5].second].findByBeginGenOpt(82, 1, var273[5].first)
val var277 = history[var273[5].second].findByBeginGenOpt(326, 1, var273[5].first)
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
val var281 = getSequenceElems(history, 327, listOf(7,328,419), beginGen, endGen)
val var282 = matchWS(var281[0].first, var281[0].second)
val var283 = matchMessageMemberDef(var281[1].first, var281[1].second)
val var284 = MessageMemberDefWS(var282, var283, nextId(), beginGen, endGen)
val var285 = unrollRepeat0(history, 419, 421, 9, 420, var281[2].first, var281[2].second).map { k ->
val var286 = getSequenceElems(history, 422, listOf(364,328), k.first, k.second)
val var287 = history[var286[0].second].findByBeginGenOpt(365, 1, var286[0].first)
val var288 = history[var286[0].second].findByBeginGenOpt(367, 1, var286[0].first)
check(hasSingleTrue(var287 != null, var288 != null))
val var289 = when {
var287 != null -> {
val var290 = getSequenceElems(history, 366, listOf(7,307,7), var286[0].first, var286[0].second)
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
val var295 = getSequenceElems(history, 368, listOf(369,372,7), beginGen, endGen)
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
val var301 = getSequenceElems(history, 348, listOf(349,7,62,7,236,351,7,373), beginGen, endGen)
val var302 = matchIdent(var301[2].first, var301[2].second)
val var304 = history[var301[5].second].findByBeginGenOpt(82, 1, var301[5].first)
val var305 = history[var301[5].second].findByBeginGenOpt(352, 1, var301[5].first)
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
val var309 = getSequenceElems(history, 353, listOf(7,354,360), beginGen, endGen)
val var310 = matchWS(var309[0].first, var309[0].second)
val var311 = matchEnumMemberDef(var309[1].first, var309[1].second)
val var312 = EnumMemberDefWS(var310, var311, nextId(), beginGen, endGen)
val var313 = unrollRepeat0(history, 360, 362, 9, 361, var309[2].first, var309[2].second).map { k ->
val var314 = getSequenceElems(history, 363, listOf(364,354), k.first, k.second)
val var315 = history[var314[0].second].findByBeginGenOpt(365, 1, var314[0].first)
val var316 = history[var314[0].second].findByBeginGenOpt(367, 1, var314[0].first)
check(hasSingleTrue(var315 != null, var316 != null))
val var317 = when {
var315 != null -> {
val var318 = getSequenceElems(history, 366, listOf(7,307,7), var314[0].first, var314[0].second)
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
val var324 = history[endGen].findByBeginGenOpt(355, 1, beginGen)
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
val var328 = getSequenceElems(history, 356, listOf(357,185,7,62,331), beginGen, endGen)
val var329 = history[var328[0].second].findByBeginGenOpt(82, 1, var328[0].first)
val var330 = history[var328[0].second].findByBeginGenOpt(358, 1, var328[0].first)
check(hasSingleTrue(var329 != null, var330 != null))
val var331 = when {
var329 != null -> null
else -> {
val var332 = getSequenceElems(history, 359, listOf(184,7), var328[0].first, var328[0].second)
source[var332[0].first]
}
}
val var333 = matchIntLiteral(var328[1].first, var328[1].second)
val var334 = matchIdent(var328[3].first, var328[3].second)
val var335 = history[var328[4].second].findByBeginGenOpt(82, 1, var328[4].first)
val var336 = history[var328[4].second].findByBeginGenOpt(332, 1, var328[4].first)
check(hasSingleTrue(var335 != null, var336 != null))
val var337 = when {
var335 != null -> null
else -> {
val var338 = getSequenceElems(history, 333, listOf(7,334), var328[4].first, var328[4].second)
val var339 = matchFieldOptions(var338[1].first, var338[1].second)
var339
}
}
val var340 = EnumFieldDef(var331 != null, var333, var334, var337, nextId(), beginGen, endGen)
return var340
}

fun matchSealedDef(beginGen: Int, endGen: Int): SealedDef {
val var341 = getSequenceElems(history, 476, listOf(425,7,62,7,236,432,7,373), beginGen, endGen)
val var342 = matchIdent(var341[2].first, var341[2].second)
val var344 = history[var341[5].second].findByBeginGenOpt(82, 1, var341[5].first)
val var345 = history[var341[5].second].findByBeginGenOpt(433, 1, var341[5].first)
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
val var349 = getSequenceElems(history, 434, listOf(7,435,442), beginGen, endGen)
val var350 = matchWS(var349[0].first, var349[0].second)
val var351 = matchSealedMemberDef(var349[1].first, var349[1].second)
val var352 = SealedMemberDefWS(var350, var351, nextId(), beginGen, endGen)
val var353 = unrollRepeat0(history, 442, 444, 9, 443, var349[2].first, var349[2].second).map { k ->
val var354 = getSequenceElems(history, 445, listOf(364,435), k.first, k.second)
val var355 = history[var354[0].second].findByBeginGenOpt(365, 1, var354[0].first)
val var356 = history[var354[0].second].findByBeginGenOpt(367, 1, var354[0].first)
check(hasSingleTrue(var355 != null, var356 != null))
val var357 = when {
var355 != null -> {
val var358 = getSequenceElems(history, 366, listOf(7,307,7), var354[0].first, var354[0].second)
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
val var363 = history[endGen].findByBeginGenOpt(329, 1, beginGen)
val var364 = history[endGen].findByBeginGenOpt(436, 1, beginGen)
check(hasSingleTrue(var363 != null, var364 != null))
val var365 = when {
var363 != null -> {
val var366 = matchFieldDef(beginGen, endGen)
var366
}
else -> {
val var367 = matchCommonFieldDef(beginGen, endGen)
var367
}
}
return var365
}

fun matchCommonFieldDef(beginGen: Int, endGen: Int): CommonFieldDef {
val var368 = getSequenceElems(history, 437, listOf(438,7,329), beginGen, endGen)
val var369 = matchFieldDef(var368[2].first, var368[2].second)
val var370 = CommonFieldDef(var369, nextId(), beginGen, endGen)
return var370
}

fun matchFieldDef(beginGen: Int, endGen: Int): FieldDef {
val var371 = getSequenceElems(history, 330, listOf(185,7,62,7,248,7,249,331), beginGen, endGen)
val var372 = matchIntLiteral(var371[0].first, var371[0].second)
val var373 = matchIdent(var371[2].first, var371[2].second)
val var374 = matchType(var371[6].first, var371[6].second)
val var375 = history[var371[7].second].findByBeginGenOpt(82, 1, var371[7].first)
val var376 = history[var371[7].second].findByBeginGenOpt(332, 1, var371[7].first)
check(hasSingleTrue(var375 != null, var376 != null))
val var377 = when {
var375 != null -> null
else -> {
val var378 = getSequenceElems(history, 333, listOf(7,334), var371[7].first, var371[7].second)
val var379 = matchFieldOptions(var378[1].first, var378[1].second)
var379
}
}
val var380 = FieldDef(var372, var373, var374, var377, nextId(), beginGen, endGen)
return var380
}

fun matchLetter(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchType(beginGen: Int, endGen: Int): Type {
val var381 = history[endGen].findByBeginGenOpt(250, 1, beginGen)
val var382 = history[endGen].findByBeginGenOpt(290, 7, beginGen)
val var383 = history[endGen].findByBeginGenOpt(297, 7, beginGen)
val var384 = history[endGen].findByBeginGenOpt(302, 11, beginGen)
val var385 = history[endGen].findByBeginGenOpt(308, 7, beginGen)
val var386 = history[endGen].findByBeginGenOpt(313, 1, beginGen)
val var387 = history[endGen].findByBeginGenOpt(423, 1, beginGen)
val var388 = history[endGen].findByBeginGenOpt(446, 1, beginGen)
val var389 = history[endGen].findByBeginGenOpt(453, 1, beginGen)
check(hasSingleTrue(var381 != null, var382 != null, var383 != null, var384 != null, var385 != null, var386 != null, var387 != null, var388 != null, var389 != null))
val var390 = when {
var381 != null -> {
val var391 = matchPrimitiveType(beginGen, endGen)
val var392 = PrimitiveType(var391, nextId(), beginGen, endGen)
var392
}
var382 != null -> {
val var393 = getSequenceElems(history, 290, listOf(291,7,295,7,249,7,296), beginGen, endGen)
val var394 = matchType(var393[4].first, var393[4].second)
val var395 = RepeatedType(var394, nextId(), beginGen, endGen)
var395
}
var383 != null -> {
val var396 = getSequenceElems(history, 297, listOf(298,7,295,7,249,7,296), beginGen, endGen)
val var397 = matchType(var396[4].first, var396[4].second)
val var398 = OptionalType(var397, nextId(), beginGen, endGen)
var398
}
var384 != null -> {
val var399 = getSequenceElems(history, 302, listOf(303,7,295,7,249,7,307,7,249,7,296), beginGen, endGen)
val var400 = matchType(var399[4].first, var399[4].second)
val var401 = matchType(var399[8].first, var399[8].second)
val var402 = MapType(var400, var401, nextId(), beginGen, endGen)
var402
}
var385 != null -> {
val var403 = getSequenceElems(history, 308, listOf(309,7,295,7,249,7,296), beginGen, endGen)
val var404 = matchType(var403[4].first, var403[4].second)
val var405 = StreamType(var404, nextId(), beginGen, endGen)
var405
}
var386 != null -> {
val var406 = matchOnTheFlyMessageType(beginGen, endGen)
var406
}
var387 != null -> {
val var407 = matchOnTheFlySealedMessageType(beginGen, endGen)
var407
}
var388 != null -> {
val var408 = matchOnTheFlyEnumType(beginGen, endGen)
var408
}
else -> {
val var409 = matchTypeName(beginGen, endGen)
var409
}
}
return var390
}

fun matchTypeName(beginGen: Int, endGen: Int): TypeName {
val var410 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var411 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var412 = history[endGen].findByBeginGenOpt(454, 1, beginGen)
check(hasSingleTrue(var410 != null, var411 != null, var412 != null))
val var413 = when {
var410 != null -> {
val var414 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var415 = matchIdentName(var414[1].first, var414[1].second)
val var416 = SingleName(var415, nextId(), beginGen, endGen)
var416
}
var411 != null -> {
val var417 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var418 = matchIdent(var417[0].first, var417[0].second)
val var419 = unrollRepeat1(history, 179, 79, 79, 180, var417[1].first, var417[1].second).map { k ->
val var420 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var421 = matchIdent(var420[3].first, var420[3].second)
var421
}
val var422 = MultiName(listOf(var418) + var419, nextId(), beginGen, endGen)
var422
}
else -> {
val var423 = matchIdentName(beginGen, endGen)
val var424 = SingleName(var423, nextId(), beginGen, endGen)
var424
}
}
return var413
}

fun matchOnTheFlyMessageType(beginGen: Int, endGen: Int): OnTheFlyMessageType {
val var425 = getSequenceElems(history, 314, listOf(315,236,325,7,373), beginGen, endGen)
val var426 = history[var425[0].second].findByBeginGenOpt(82, 1, var425[0].first)
val var427 = history[var425[0].second].findByBeginGenOpt(316, 1, var425[0].first)
check(hasSingleTrue(var426 != null, var427 != null))
val var428 = when {
var426 != null -> null
else -> {
val var429 = getSequenceElems(history, 317, listOf(318,7), var425[0].first, var425[0].second)
val var430 = matchIdentNoSealedEnum(var429[0].first, var429[0].second)
var430
}
}
val var432 = history[var425[2].second].findByBeginGenOpt(82, 1, var425[2].first)
val var433 = history[var425[2].second].findByBeginGenOpt(326, 1, var425[2].first)
check(hasSingleTrue(var432 != null, var433 != null))
val var434 = when {
var432 != null -> null
else -> {
val var435 = matchMessageMembersWS(var425[2].first, var425[2].second)
var435
}
}
val var431 = var434
val var436 = OnTheFlyMessageType(var428, (var431 ?: listOf()), nextId(), beginGen, endGen)
return var436
}

fun matchIdentNoSealedEnum(beginGen: Int, endGen: Int): Ident {
val var437 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var438 = history[endGen].findByBeginGenOpt(319, 1, beginGen)
check(hasSingleTrue(var437 != null, var438 != null))
val var439 = when {
var437 != null -> {
val var440 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var441 = matchIdentName(var440[1].first, var440[1].second)
val var442 = Ident(var441, nextId(), beginGen, endGen)
var442
}
else -> {
val var443 = matchIdentName(beginGen, endGen)
val var444 = Ident(var443, nextId(), beginGen, endGen)
var444
}
}
return var439
}

fun matchOnTheFlyEnumType(beginGen: Int, endGen: Int): OnTheFlyEnumType {
val var445 = getSequenceElems(history, 447, listOf(349,448,7,236,351,7,373), beginGen, endGen)
val var446 = history[var445[1].second].findByBeginGenOpt(82, 1, var445[1].first)
val var447 = history[var445[1].second].findByBeginGenOpt(449, 1, var445[1].first)
check(hasSingleTrue(var446 != null, var447 != null))
val var448 = when {
var446 != null -> null
else -> {
val var449 = getSequenceElems(history, 450, listOf(7,451), var445[1].first, var445[1].second)
val var450 = matchIdentNoEnum(var449[1].first, var449[1].second)
var450
}
}
val var452 = history[var445[4].second].findByBeginGenOpt(82, 1, var445[4].first)
val var453 = history[var445[4].second].findByBeginGenOpt(352, 1, var445[4].first)
check(hasSingleTrue(var452 != null, var453 != null))
val var454 = when {
var452 != null -> null
else -> {
val var455 = matchEnumMembersWS(var445[4].first, var445[4].second)
var455
}
}
val var451 = var454
val var456 = OnTheFlyEnumType(var448, (var451 ?: listOf()), nextId(), beginGen, endGen)
return var456
}

fun matchIdentNoEnum(beginGen: Int, endGen: Int): Ident {
val var457 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var458 = history[endGen].findByBeginGenOpt(452, 1, beginGen)
check(hasSingleTrue(var457 != null, var458 != null))
val var459 = when {
var457 != null -> {
val var460 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var461 = matchIdentName(var460[1].first, var460[1].second)
val var462 = Ident(var461, nextId(), beginGen, endGen)
var462
}
else -> {
val var463 = matchIdentName(beginGen, endGen)
val var464 = Ident(var463, nextId(), beginGen, endGen)
var464
}
}
return var459
}

fun matchPrimitiveType(beginGen: Int, endGen: Int): PrimitiveTypeEnum {
val var465 = history[endGen].findByBeginGenOpt(252, 1, beginGen)
val var466 = history[endGen].findByBeginGenOpt(256, 1, beginGen)
val var467 = history[endGen].findByBeginGenOpt(258, 1, beginGen)
val var468 = history[endGen].findByBeginGenOpt(262, 1, beginGen)
val var469 = history[endGen].findByBeginGenOpt(266, 1, beginGen)
val var470 = history[endGen].findByBeginGenOpt(268, 1, beginGen)
val var471 = history[endGen].findByBeginGenOpt(270, 1, beginGen)
val var472 = history[endGen].findByBeginGenOpt(272, 1, beginGen)
val var473 = history[endGen].findByBeginGenOpt(274, 1, beginGen)
val var474 = history[endGen].findByBeginGenOpt(277, 1, beginGen)
val var475 = history[endGen].findByBeginGenOpt(279, 1, beginGen)
val var476 = history[endGen].findByBeginGenOpt(281, 1, beginGen)
val var477 = history[endGen].findByBeginGenOpt(283, 1, beginGen)
val var478 = history[endGen].findByBeginGenOpt(285, 1, beginGen)
val var479 = history[endGen].findByBeginGenOpt(287, 1, beginGen)
check(hasSingleTrue(var465 != null, var466 != null, var467 != null, var468 != null, var469 != null, var470 != null, var471 != null, var472 != null, var473 != null, var474 != null, var475 != null, var476 != null, var477 != null, var478 != null, var479 != null))
val var480 = when {
var465 != null -> PrimitiveTypeEnum.DOUBLE
var466 != null -> PrimitiveTypeEnum.FLOAT
var467 != null -> PrimitiveTypeEnum.INT32
var468 != null -> PrimitiveTypeEnum.INT64
var469 != null -> PrimitiveTypeEnum.UINT32
var470 != null -> PrimitiveTypeEnum.UINT64
var471 != null -> PrimitiveTypeEnum.SINT32
var472 != null -> PrimitiveTypeEnum.SINT64
var473 != null -> PrimitiveTypeEnum.FIXED32
var474 != null -> PrimitiveTypeEnum.FIXED64
var475 != null -> PrimitiveTypeEnum.SFIXED32
var476 != null -> PrimitiveTypeEnum.SFIXED64
var477 != null -> PrimitiveTypeEnum.BOOL
var478 != null -> PrimitiveTypeEnum.STRING
else -> PrimitiveTypeEnum.BYTES
}
return var480
}

fun matchRpcTypeWheres(beginGen: Int, endGen: Int): RpcTypeWheres {
val var481 = getSequenceElems(history, 468, listOf(469,471), beginGen, endGen)
val var482 = matchRpcTypeWhere(var481[0].first, var481[0].second)
val var483 = unrollRepeat0(history, 471, 473, 9, 472, var481[1].first, var481[1].second).map { k ->
val var484 = getSequenceElems(history, 474, listOf(7,307,7,469), k.first, k.second)
val var485 = matchRpcTypeWhere(var484[3].first, var484[3].second)
var485
}
val var486 = RpcTypeWheres(listOf(var482) + var483, nextId(), beginGen, endGen)
return var486
}

fun matchRpcTypeWhere(beginGen: Int, endGen: Int): RpcTypeWhere {
val var487 = getSequenceElems(history, 470, listOf(62,7,167,7,249), beginGen, endGen)
val var488 = matchIdent(var487[0].first, var487[0].second)
val var489 = matchType(var487[4].first, var487[4].second)
val var490 = RpcTypeWhere(var488, var489, nextId(), beginGen, endGen)
return var490
}

fun matchOnTheFlySealedMessageType(beginGen: Int, endGen: Int): OnTheFlySealedMessageType {
val var491 = getSequenceElems(history, 424, listOf(425,427,7,236,432,7,373), beginGen, endGen)
val var492 = history[var491[1].second].findByBeginGenOpt(82, 1, var491[1].first)
val var493 = history[var491[1].second].findByBeginGenOpt(428, 1, var491[1].first)
check(hasSingleTrue(var492 != null, var493 != null))
val var494 = when {
var492 != null -> null
else -> {
val var495 = getSequenceElems(history, 429, listOf(7,430), var491[1].first, var491[1].second)
val var496 = matchIdentNoSealed(var495[1].first, var495[1].second)
var496
}
}
val var498 = history[var491[4].second].findByBeginGenOpt(82, 1, var491[4].first)
val var499 = history[var491[4].second].findByBeginGenOpt(433, 1, var491[4].first)
check(hasSingleTrue(var498 != null, var499 != null))
val var500 = when {
var498 != null -> null
else -> {
val var501 = matchSealedMembersWS(var491[4].first, var491[4].second)
var501
}
}
val var497 = var500
val var502 = OnTheFlySealedMessageType(var494, (var497 ?: listOf()), nextId(), beginGen, endGen)
return var502
}

fun matchIdentNoSealed(beginGen: Int, endGen: Int): Ident {
val var503 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var504 = history[endGen].findByBeginGenOpt(431, 1, beginGen)
check(hasSingleTrue(var503 != null, var504 != null))
val var505 = when {
var503 != null -> {
val var506 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var507 = matchIdentName(var506[1].first, var506[1].second)
val var508 = Ident(var507, nextId(), beginGen, endGen)
var508
}
else -> {
val var509 = matchIdentName(beginGen, endGen)
val var510 = Ident(var509, nextId(), beginGen, endGen)
var510
}
}
return var505
}

fun matchFieldOptions(beginGen: Int, endGen: Int): FieldOptions {
val var512 = getSequenceElems(history, 335, listOf(336,337,7,346), beginGen, endGen)
val var513 = history[var512[1].second].findByBeginGenOpt(82, 1, var512[1].first)
val var514 = history[var512[1].second].findByBeginGenOpt(338, 1, var512[1].first)
check(hasSingleTrue(var513 != null, var514 != null))
val var515 = when {
var513 != null -> null
else -> {
val var516 = getSequenceElems(history, 339, listOf(7,340,342), var512[1].first, var512[1].second)
val var517 = matchFieldOption(var516[1].first, var516[1].second)
val var518 = unrollRepeat0(history, 342, 344, 9, 343, var516[2].first, var516[2].second).map { k ->
val var519 = getSequenceElems(history, 345, listOf(7,307,7,340), k.first, k.second)
val var520 = matchFieldOption(var519[3].first, var519[3].second)
var520
}
listOf(var517) + var518
}
}
val var511 = var515
val var521 = FieldOptions((var511 ?: listOf()), nextId(), beginGen, endGen)
return var521
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var522 = getSequenceElems(history, 341, listOf(160,7,167,7,168), beginGen, endGen)
val var523 = matchOptionName(var522[0].first, var522[0].second)
val var524 = matchConstant(var522[4].first, var522[4].second)
val var525 = FieldOption(var523, var524, nextId(), beginGen, endGen)
return var525
}

fun matchMessageMemberDef(beginGen: Int, endGen: Int): MessageMemberDef {
val var526 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var527 = history[endGen].findByBeginGenOpt(329, 1, beginGen)
val var528 = history[endGen].findByBeginGenOpt(347, 1, beginGen)
val var529 = history[endGen].findByBeginGenOpt(374, 1, beginGen)
val var530 = history[endGen].findByBeginGenOpt(380, 1, beginGen)
val var531 = history[endGen].findByBeginGenOpt(394, 1, beginGen)
check(hasSingleTrue(var526 != null, var527 != null, var528 != null, var529 != null, var530 != null, var531 != null))
val var532 = when {
var526 != null -> {
val var533 = matchOptionDef(beginGen, endGen)
var533
}
var527 != null -> {
val var534 = matchFieldDef(beginGen, endGen)
var534
}
var528 != null -> {
val var535 = matchEnumDef(beginGen, endGen)
var535
}
var529 != null -> {
val var536 = matchMessageDef(beginGen, endGen)
var536
}
var530 != null -> {
val var537 = matchOneOfDef(beginGen, endGen)
var537
}
else -> {
val var538 = matchReservedDef(beginGen, endGen)
var538
}
}
return var532
}

fun matchOneOfDef(beginGen: Int, endGen: Int): OneOfDef {
val var539 = getSequenceElems(history, 381, listOf(382,7,62,7,236,386,7,373), beginGen, endGen)
val var540 = matchIdent(var539[2].first, var539[2].second)
val var542 = history[var539[5].second].findByBeginGenOpt(82, 1, var539[5].first)
val var543 = history[var539[5].second].findByBeginGenOpt(387, 1, var539[5].first)
check(hasSingleTrue(var542 != null, var543 != null))
val var544 = when {
var542 != null -> null
else -> {
val var545 = matchOneOfMembersWS(var539[5].first, var539[5].second)
var545
}
}
val var541 = var544
val var546 = OneOfDef(var540, (var541 ?: listOf()), nextId(), beginGen, endGen)
return var546
}

fun matchOneOfMembersWS(beginGen: Int, endGen: Int): List<OneOfMembersDefWS> {
val var547 = getSequenceElems(history, 388, listOf(7,389,390), beginGen, endGen)
val var548 = matchWS(var547[0].first, var547[0].second)
val var549 = matchOneOfMemberDef(var547[1].first, var547[1].second)
val var550 = OneOfMembersDefWS(var548, var549, nextId(), beginGen, endGen)
val var551 = unrollRepeat0(history, 390, 392, 9, 391, var547[2].first, var547[2].second).map { k ->
val var552 = getSequenceElems(history, 393, listOf(364,389), k.first, k.second)
val var553 = history[var552[0].second].findByBeginGenOpt(365, 1, var552[0].first)
val var554 = history[var552[0].second].findByBeginGenOpt(367, 1, var552[0].first)
check(hasSingleTrue(var553 != null, var554 != null))
val var555 = when {
var553 != null -> {
val var556 = getSequenceElems(history, 366, listOf(7,307,7), var552[0].first, var552[0].second)
val var557 = matchWS(var556[2].first, var556[2].second)
var557
}
else -> {
val var558 = matchWSNL(var552[0].first, var552[0].second)
var558
}
}
val var559 = matchOneOfMemberDef(var552[1].first, var552[1].second)
val var560 = OneOfMembersDefWS(var555, var559, nextId(), k.first, k.second)
var560
}
return listOf(var550) + var551
}

fun matchReservedDef(beginGen: Int, endGen: Int): ReservedDef {
val var561 = getSequenceElems(history, 395, listOf(396,7,165,7,400,412,416,7,166), beginGen, endGen)
val var562 = matchReservedItem(var561[4].first, var561[4].second)
val var563 = unrollRepeat0(history, 412, 414, 9, 413, var561[5].first, var561[5].second).map { k ->
val var564 = getSequenceElems(history, 415, listOf(7,307,7,400), k.first, k.second)
val var565 = matchReservedItem(var564[3].first, var564[3].second)
var565
}
val var566 = ReservedDef(listOf(var562) + var563, nextId(), beginGen, endGen)
return var566
}

fun matchReservedItem(beginGen: Int, endGen: Int): ReservedItem {
val var567 = history[endGen].findByBeginGenOpt(62, 1, beginGen)
val var568 = history[endGen].findByBeginGenOpt(401, 1, beginGen)
check(hasSingleTrue(var567 != null, var568 != null))
val var569 = when {
var567 != null -> {
val var570 = matchIdent(beginGen, endGen)
var570
}
else -> {
val var571 = matchReservedRange(beginGen, endGen)
var571
}
}
return var569
}

fun matchOneOfMemberDef(beginGen: Int, endGen: Int): OneOfMemberDef {
val var572 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var573 = history[endGen].findByBeginGenOpt(329, 1, beginGen)
check(hasSingleTrue(var572 != null, var573 != null))
val var574 = when {
var572 != null -> {
val var575 = matchOptionDef(beginGen, endGen)
var575
}
else -> {
val var576 = matchFieldDef(beginGen, endGen)
var576
}
}
return var574
}

fun matchReservedRange(beginGen: Int, endGen: Int): ReservedRange {
val var577 = getSequenceElems(history, 402, listOf(185,403), beginGen, endGen)
val var578 = matchIntLiteral(var577[0].first, var577[0].second)
val var579 = history[var577[1].second].findByBeginGenOpt(82, 1, var577[1].first)
val var580 = history[var577[1].second].findByBeginGenOpt(404, 1, var577[1].first)
check(hasSingleTrue(var579 != null, var580 != null))
val var581 = when {
var579 != null -> null
else -> {
val var582 = getSequenceElems(history, 405, listOf(7,406,7,408), var577[1].first, var577[1].second)
val var583 = matchReservedRangeEnd(var582[3].first, var582[3].second)
var583
}
}
val var584 = ReservedRange(var578, var581, nextId(), beginGen, endGen)
return var584
}

fun matchReservedRangeEnd(beginGen: Int, endGen: Int): ReservedRangeEnd {
val var585 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var586 = history[endGen].findByBeginGenOpt(409, 1, beginGen)
check(hasSingleTrue(var585 != null, var586 != null))
val var587 = when {
var585 != null -> {
val var588 = matchIntLiteral(beginGen, endGen)
var588
}
else -> {
val var589 = Max(nextId(), beginGen, endGen)
var589
}
}
return var587
}

}
