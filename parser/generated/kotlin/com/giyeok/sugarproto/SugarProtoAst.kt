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

data class FullIdent(
  val names: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode

data class SetType(
  val elemType: Type,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

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
val var236 = history[endGen].findByBeginGenOpt(352, 1, beginGen)
val var237 = history[endGen].findByBeginGenOpt(379, 1, beginGen)
val var238 = history[endGen].findByBeginGenOpt(480, 1, beginGen)
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
val var244 = getSequenceElems(history, 230, listOf(231,7,62,7,236,237,7,378), beginGen, endGen)
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
val var258 = getSequenceElems(history, 243, listOf(244,7,62,7,248,7,249,7,461,7,249,463,336), beginGen, endGen)
val var259 = matchIdent(var258[2].first, var258[2].second)
val var260 = matchType(var258[6].first, var258[6].second)
val var261 = matchType(var258[10].first, var258[10].second)
val var262 = history[var258[11].second].findByBeginGenOpt(82, 1, var258[11].first)
val var263 = history[var258[11].second].findByBeginGenOpt(464, 1, var258[11].first)
check(hasSingleTrue(var262 != null, var263 != null))
val var264 = when {
var262 != null -> null
else -> {
val var265 = getSequenceElems(history, 465, listOf(7,466,7,472), var258[11].first, var258[11].second)
val var266 = matchRpcTypeWheres(var265[3].first, var265[3].second)
var266
}
}
val var267 = history[var258[12].second].findByBeginGenOpt(82, 1, var258[12].first)
val var268 = history[var258[12].second].findByBeginGenOpt(337, 1, var258[12].first)
check(hasSingleTrue(var267 != null, var268 != null))
val var269 = when {
var267 != null -> null
else -> {
val var270 = getSequenceElems(history, 338, listOf(7,339), var258[12].first, var258[12].second)
val var271 = matchFieldOptions(var270[1].first, var270[1].second)
var271
}
}
val var272 = RpcDef(var259, var260, var261, var264, var269, nextId(), beginGen, endGen)
return var272
}

fun matchMessageDef(beginGen: Int, endGen: Int): MessageDef {
val var273 = getSequenceElems(history, 380, listOf(381,7,62,7,236,330,7,378), beginGen, endGen)
val var274 = matchIdent(var273[2].first, var273[2].second)
val var276 = history[var273[5].second].findByBeginGenOpt(82, 1, var273[5].first)
val var277 = history[var273[5].second].findByBeginGenOpt(331, 1, var273[5].first)
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
val var281 = getSequenceElems(history, 332, listOf(7,333,424), beginGen, endGen)
val var282 = matchWS(var281[0].first, var281[0].second)
val var283 = matchMessageMemberDef(var281[1].first, var281[1].second)
val var284 = MessageMemberDefWS(var282, var283, nextId(), beginGen, endGen)
val var285 = unrollRepeat0(history, 424, 426, 9, 425, var281[2].first, var281[2].second).map { k ->
val var286 = getSequenceElems(history, 427, listOf(369,333), k.first, k.second)
val var287 = history[var286[0].second].findByBeginGenOpt(370, 1, var286[0].first)
val var288 = history[var286[0].second].findByBeginGenOpt(372, 1, var286[0].first)
check(hasSingleTrue(var287 != null, var288 != null))
val var289 = when {
var287 != null -> {
val var290 = getSequenceElems(history, 371, listOf(7,307,7), var286[0].first, var286[0].second)
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
val var295 = getSequenceElems(history, 373, listOf(374,377,7), beginGen, endGen)
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
val var301 = getSequenceElems(history, 353, listOf(354,7,62,7,236,356,7,378), beginGen, endGen)
val var302 = matchIdent(var301[2].first, var301[2].second)
val var304 = history[var301[5].second].findByBeginGenOpt(82, 1, var301[5].first)
val var305 = history[var301[5].second].findByBeginGenOpt(357, 1, var301[5].first)
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
val var309 = getSequenceElems(history, 358, listOf(7,359,365), beginGen, endGen)
val var310 = matchWS(var309[0].first, var309[0].second)
val var311 = matchEnumMemberDef(var309[1].first, var309[1].second)
val var312 = EnumMemberDefWS(var310, var311, nextId(), beginGen, endGen)
val var313 = unrollRepeat0(history, 365, 367, 9, 366, var309[2].first, var309[2].second).map { k ->
val var314 = getSequenceElems(history, 368, listOf(369,359), k.first, k.second)
val var315 = history[var314[0].second].findByBeginGenOpt(370, 1, var314[0].first)
val var316 = history[var314[0].second].findByBeginGenOpt(372, 1, var314[0].first)
check(hasSingleTrue(var315 != null, var316 != null))
val var317 = when {
var315 != null -> {
val var318 = getSequenceElems(history, 371, listOf(7,307,7), var314[0].first, var314[0].second)
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
val var324 = history[endGen].findByBeginGenOpt(360, 1, beginGen)
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
val var328 = getSequenceElems(history, 361, listOf(362,185,7,62,336), beginGen, endGen)
val var329 = history[var328[0].second].findByBeginGenOpt(82, 1, var328[0].first)
val var330 = history[var328[0].second].findByBeginGenOpt(363, 1, var328[0].first)
check(hasSingleTrue(var329 != null, var330 != null))
val var331 = when {
var329 != null -> null
else -> {
val var332 = getSequenceElems(history, 364, listOf(184,7), var328[0].first, var328[0].second)
source[var332[0].first]
}
}
val var333 = matchIntLiteral(var328[1].first, var328[1].second)
val var334 = matchIdent(var328[3].first, var328[3].second)
val var335 = history[var328[4].second].findByBeginGenOpt(82, 1, var328[4].first)
val var336 = history[var328[4].second].findByBeginGenOpt(337, 1, var328[4].first)
check(hasSingleTrue(var335 != null, var336 != null))
val var337 = when {
var335 != null -> null
else -> {
val var338 = getSequenceElems(history, 338, listOf(7,339), var328[4].first, var328[4].second)
val var339 = matchFieldOptions(var338[1].first, var338[1].second)
var339
}
}
val var340 = EnumFieldDef(var331 != null, var333, var334, var337, nextId(), beginGen, endGen)
return var340
}

fun matchSealedDef(beginGen: Int, endGen: Int): SealedDef {
val var341 = getSequenceElems(history, 481, listOf(430,7,62,7,236,437,7,378), beginGen, endGen)
val var342 = matchIdent(var341[2].first, var341[2].second)
val var344 = history[var341[5].second].findByBeginGenOpt(82, 1, var341[5].first)
val var345 = history[var341[5].second].findByBeginGenOpt(438, 1, var341[5].first)
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
val var349 = getSequenceElems(history, 439, listOf(7,440,447), beginGen, endGen)
val var350 = matchWS(var349[0].first, var349[0].second)
val var351 = matchSealedMemberDef(var349[1].first, var349[1].second)
val var352 = SealedMemberDefWS(var350, var351, nextId(), beginGen, endGen)
val var353 = unrollRepeat0(history, 447, 449, 9, 448, var349[2].first, var349[2].second).map { k ->
val var354 = getSequenceElems(history, 450, listOf(369,440), k.first, k.second)
val var355 = history[var354[0].second].findByBeginGenOpt(370, 1, var354[0].first)
val var356 = history[var354[0].second].findByBeginGenOpt(372, 1, var354[0].first)
check(hasSingleTrue(var355 != null, var356 != null))
val var357 = when {
var355 != null -> {
val var358 = getSequenceElems(history, 371, listOf(7,307,7), var354[0].first, var354[0].second)
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
val var363 = history[endGen].findByBeginGenOpt(334, 1, beginGen)
val var364 = history[endGen].findByBeginGenOpt(441, 1, beginGen)
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
val var368 = getSequenceElems(history, 442, listOf(443,7,334), beginGen, endGen)
val var369 = matchFieldDef(var368[2].first, var368[2].second)
val var370 = CommonFieldDef(var369, nextId(), beginGen, endGen)
return var370
}

fun matchFieldDef(beginGen: Int, endGen: Int): FieldDef {
val var371 = getSequenceElems(history, 335, listOf(185,7,62,7,248,7,249,336), beginGen, endGen)
val var372 = matchIntLiteral(var371[0].first, var371[0].second)
val var373 = matchIdent(var371[2].first, var371[2].second)
val var374 = matchType(var371[6].first, var371[6].second)
val var375 = history[var371[7].second].findByBeginGenOpt(82, 1, var371[7].first)
val var376 = history[var371[7].second].findByBeginGenOpt(337, 1, var371[7].first)
check(hasSingleTrue(var375 != null, var376 != null))
val var377 = when {
var375 != null -> null
else -> {
val var378 = getSequenceElems(history, 338, listOf(7,339), var371[7].first, var371[7].second)
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
val var386 = history[endGen].findByBeginGenOpt(313, 7, beginGen)
val var387 = history[endGen].findByBeginGenOpt(318, 1, beginGen)
val var388 = history[endGen].findByBeginGenOpt(428, 1, beginGen)
val var389 = history[endGen].findByBeginGenOpt(451, 1, beginGen)
val var390 = history[endGen].findByBeginGenOpt(458, 1, beginGen)
check(hasSingleTrue(var381 != null, var382 != null, var383 != null, var384 != null, var385 != null, var386 != null, var387 != null, var388 != null, var389 != null, var390 != null))
val var391 = when {
var381 != null -> {
val var392 = matchPrimitiveType(beginGen, endGen)
val var393 = PrimitiveType(var392, nextId(), beginGen, endGen)
var393
}
var382 != null -> {
val var394 = getSequenceElems(history, 290, listOf(291,7,295,7,249,7,296), beginGen, endGen)
val var395 = matchType(var394[4].first, var394[4].second)
val var396 = RepeatedType(var395, nextId(), beginGen, endGen)
var396
}
var383 != null -> {
val var397 = getSequenceElems(history, 297, listOf(298,7,295,7,249,7,296), beginGen, endGen)
val var398 = matchType(var397[4].first, var397[4].second)
val var399 = OptionalType(var398, nextId(), beginGen, endGen)
var399
}
var384 != null -> {
val var400 = getSequenceElems(history, 302, listOf(303,7,295,7,249,7,307,7,249,7,296), beginGen, endGen)
val var401 = matchType(var400[4].first, var400[4].second)
val var402 = matchType(var400[8].first, var400[8].second)
val var403 = MapType(var401, var402, nextId(), beginGen, endGen)
var403
}
var385 != null -> {
val var404 = getSequenceElems(history, 308, listOf(309,7,295,7,249,7,296), beginGen, endGen)
val var405 = matchType(var404[4].first, var404[4].second)
val var406 = SetType(var405, nextId(), beginGen, endGen)
var406
}
var386 != null -> {
val var407 = getSequenceElems(history, 313, listOf(314,7,295,7,249,7,296), beginGen, endGen)
val var408 = matchType(var407[4].first, var407[4].second)
val var409 = StreamType(var408, nextId(), beginGen, endGen)
var409
}
var387 != null -> {
val var410 = matchOnTheFlyMessageType(beginGen, endGen)
var410
}
var388 != null -> {
val var411 = matchOnTheFlySealedMessageType(beginGen, endGen)
var411
}
var389 != null -> {
val var412 = matchOnTheFlyEnumType(beginGen, endGen)
var412
}
else -> {
val var413 = matchTypeName(beginGen, endGen)
var413
}
}
return var391
}

fun matchTypeName(beginGen: Int, endGen: Int): TypeName {
val var414 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var415 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var416 = history[endGen].findByBeginGenOpt(459, 1, beginGen)
check(hasSingleTrue(var414 != null, var415 != null, var416 != null))
val var417 = when {
var414 != null -> {
val var418 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var419 = matchIdentName(var418[1].first, var418[1].second)
val var420 = SingleName(var419, nextId(), beginGen, endGen)
var420
}
var415 != null -> {
val var421 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var422 = matchIdent(var421[0].first, var421[0].second)
val var423 = unrollRepeat1(history, 179, 79, 79, 180, var421[1].first, var421[1].second).map { k ->
val var424 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var425 = matchIdent(var424[3].first, var424[3].second)
var425
}
val var426 = MultiName(listOf(var422) + var423, nextId(), beginGen, endGen)
var426
}
else -> {
val var427 = matchIdentName(beginGen, endGen)
val var428 = SingleName(var427, nextId(), beginGen, endGen)
var428
}
}
return var417
}

fun matchOnTheFlyMessageType(beginGen: Int, endGen: Int): OnTheFlyMessageType {
val var429 = getSequenceElems(history, 319, listOf(320,236,330,7,378), beginGen, endGen)
val var430 = history[var429[0].second].findByBeginGenOpt(82, 1, var429[0].first)
val var431 = history[var429[0].second].findByBeginGenOpt(321, 1, var429[0].first)
check(hasSingleTrue(var430 != null, var431 != null))
val var432 = when {
var430 != null -> null
else -> {
val var433 = getSequenceElems(history, 322, listOf(323,7), var429[0].first, var429[0].second)
val var434 = matchIdentNoSealedEnum(var433[0].first, var433[0].second)
var434
}
}
val var436 = history[var429[2].second].findByBeginGenOpt(82, 1, var429[2].first)
val var437 = history[var429[2].second].findByBeginGenOpt(331, 1, var429[2].first)
check(hasSingleTrue(var436 != null, var437 != null))
val var438 = when {
var436 != null -> null
else -> {
val var439 = matchMessageMembersWS(var429[2].first, var429[2].second)
var439
}
}
val var435 = var438
val var440 = OnTheFlyMessageType(var432, (var435 ?: listOf()), nextId(), beginGen, endGen)
return var440
}

fun matchIdentNoSealedEnum(beginGen: Int, endGen: Int): Ident {
val var441 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var442 = history[endGen].findByBeginGenOpt(324, 1, beginGen)
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

fun matchOnTheFlyEnumType(beginGen: Int, endGen: Int): OnTheFlyEnumType {
val var449 = getSequenceElems(history, 452, listOf(354,453,7,236,356,7,378), beginGen, endGen)
val var450 = history[var449[1].second].findByBeginGenOpt(82, 1, var449[1].first)
val var451 = history[var449[1].second].findByBeginGenOpt(454, 1, var449[1].first)
check(hasSingleTrue(var450 != null, var451 != null))
val var452 = when {
var450 != null -> null
else -> {
val var453 = getSequenceElems(history, 455, listOf(7,456), var449[1].first, var449[1].second)
val var454 = matchIdentNoEnum(var453[1].first, var453[1].second)
var454
}
}
val var456 = history[var449[4].second].findByBeginGenOpt(82, 1, var449[4].first)
val var457 = history[var449[4].second].findByBeginGenOpt(357, 1, var449[4].first)
check(hasSingleTrue(var456 != null, var457 != null))
val var458 = when {
var456 != null -> null
else -> {
val var459 = matchEnumMembersWS(var449[4].first, var449[4].second)
var459
}
}
val var455 = var458
val var460 = OnTheFlyEnumType(var452, (var455 ?: listOf()), nextId(), beginGen, endGen)
return var460
}

fun matchIdentNoEnum(beginGen: Int, endGen: Int): Ident {
val var461 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var462 = history[endGen].findByBeginGenOpt(457, 1, beginGen)
check(hasSingleTrue(var461 != null, var462 != null))
val var463 = when {
var461 != null -> {
val var464 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var465 = matchIdentName(var464[1].first, var464[1].second)
val var466 = Ident(var465, nextId(), beginGen, endGen)
var466
}
else -> {
val var467 = matchIdentName(beginGen, endGen)
val var468 = Ident(var467, nextId(), beginGen, endGen)
var468
}
}
return var463
}

fun matchPrimitiveType(beginGen: Int, endGen: Int): PrimitiveTypeEnum {
val var469 = history[endGen].findByBeginGenOpt(252, 1, beginGen)
val var470 = history[endGen].findByBeginGenOpt(256, 1, beginGen)
val var471 = history[endGen].findByBeginGenOpt(258, 1, beginGen)
val var472 = history[endGen].findByBeginGenOpt(262, 1, beginGen)
val var473 = history[endGen].findByBeginGenOpt(266, 1, beginGen)
val var474 = history[endGen].findByBeginGenOpt(268, 1, beginGen)
val var475 = history[endGen].findByBeginGenOpt(270, 1, beginGen)
val var476 = history[endGen].findByBeginGenOpt(272, 1, beginGen)
val var477 = history[endGen].findByBeginGenOpt(274, 1, beginGen)
val var478 = history[endGen].findByBeginGenOpt(277, 1, beginGen)
val var479 = history[endGen].findByBeginGenOpt(279, 1, beginGen)
val var480 = history[endGen].findByBeginGenOpt(281, 1, beginGen)
val var481 = history[endGen].findByBeginGenOpt(283, 1, beginGen)
val var482 = history[endGen].findByBeginGenOpt(285, 1, beginGen)
val var483 = history[endGen].findByBeginGenOpt(287, 1, beginGen)
check(hasSingleTrue(var469 != null, var470 != null, var471 != null, var472 != null, var473 != null, var474 != null, var475 != null, var476 != null, var477 != null, var478 != null, var479 != null, var480 != null, var481 != null, var482 != null, var483 != null))
val var484 = when {
var469 != null -> PrimitiveTypeEnum.DOUBLE
var470 != null -> PrimitiveTypeEnum.FLOAT
var471 != null -> PrimitiveTypeEnum.INT32
var472 != null -> PrimitiveTypeEnum.INT64
var473 != null -> PrimitiveTypeEnum.UINT32
var474 != null -> PrimitiveTypeEnum.UINT64
var475 != null -> PrimitiveTypeEnum.SINT32
var476 != null -> PrimitiveTypeEnum.SINT64
var477 != null -> PrimitiveTypeEnum.FIXED32
var478 != null -> PrimitiveTypeEnum.FIXED64
var479 != null -> PrimitiveTypeEnum.SFIXED32
var480 != null -> PrimitiveTypeEnum.SFIXED64
var481 != null -> PrimitiveTypeEnum.BOOL
var482 != null -> PrimitiveTypeEnum.STRING
else -> PrimitiveTypeEnum.BYTES
}
return var484
}

fun matchRpcTypeWheres(beginGen: Int, endGen: Int): RpcTypeWheres {
val var485 = getSequenceElems(history, 473, listOf(474,476), beginGen, endGen)
val var486 = matchRpcTypeWhere(var485[0].first, var485[0].second)
val var487 = unrollRepeat0(history, 476, 478, 9, 477, var485[1].first, var485[1].second).map { k ->
val var488 = getSequenceElems(history, 479, listOf(7,307,7,474), k.first, k.second)
val var489 = matchRpcTypeWhere(var488[3].first, var488[3].second)
var489
}
val var490 = RpcTypeWheres(listOf(var486) + var487, nextId(), beginGen, endGen)
return var490
}

fun matchRpcTypeWhere(beginGen: Int, endGen: Int): RpcTypeWhere {
val var491 = getSequenceElems(history, 475, listOf(62,7,167,7,249), beginGen, endGen)
val var492 = matchIdent(var491[0].first, var491[0].second)
val var493 = matchType(var491[4].first, var491[4].second)
val var494 = RpcTypeWhere(var492, var493, nextId(), beginGen, endGen)
return var494
}

fun matchOnTheFlySealedMessageType(beginGen: Int, endGen: Int): OnTheFlySealedMessageType {
val var495 = getSequenceElems(history, 429, listOf(430,432,7,236,437,7,378), beginGen, endGen)
val var496 = history[var495[1].second].findByBeginGenOpt(82, 1, var495[1].first)
val var497 = history[var495[1].second].findByBeginGenOpt(433, 1, var495[1].first)
check(hasSingleTrue(var496 != null, var497 != null))
val var498 = when {
var496 != null -> null
else -> {
val var499 = getSequenceElems(history, 434, listOf(7,435), var495[1].first, var495[1].second)
val var500 = matchIdentNoSealed(var499[1].first, var499[1].second)
var500
}
}
val var502 = history[var495[4].second].findByBeginGenOpt(82, 1, var495[4].first)
val var503 = history[var495[4].second].findByBeginGenOpt(438, 1, var495[4].first)
check(hasSingleTrue(var502 != null, var503 != null))
val var504 = when {
var502 != null -> null
else -> {
val var505 = matchSealedMembersWS(var495[4].first, var495[4].second)
var505
}
}
val var501 = var504
val var506 = OnTheFlySealedMessageType(var498, (var501 ?: listOf()), nextId(), beginGen, endGen)
return var506
}

fun matchIdentNoSealed(beginGen: Int, endGen: Int): Ident {
val var507 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var508 = history[endGen].findByBeginGenOpt(436, 1, beginGen)
check(hasSingleTrue(var507 != null, var508 != null))
val var509 = when {
var507 != null -> {
val var510 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var511 = matchIdentName(var510[1].first, var510[1].second)
val var512 = Ident(var511, nextId(), beginGen, endGen)
var512
}
else -> {
val var513 = matchIdentName(beginGen, endGen)
val var514 = Ident(var513, nextId(), beginGen, endGen)
var514
}
}
return var509
}

fun matchFieldOptions(beginGen: Int, endGen: Int): FieldOptions {
val var516 = getSequenceElems(history, 340, listOf(341,342,7,351), beginGen, endGen)
val var517 = history[var516[1].second].findByBeginGenOpt(82, 1, var516[1].first)
val var518 = history[var516[1].second].findByBeginGenOpt(343, 1, var516[1].first)
check(hasSingleTrue(var517 != null, var518 != null))
val var519 = when {
var517 != null -> null
else -> {
val var520 = getSequenceElems(history, 344, listOf(7,345,347), var516[1].first, var516[1].second)
val var521 = matchFieldOption(var520[1].first, var520[1].second)
val var522 = unrollRepeat0(history, 347, 349, 9, 348, var520[2].first, var520[2].second).map { k ->
val var523 = getSequenceElems(history, 350, listOf(7,307,7,345), k.first, k.second)
val var524 = matchFieldOption(var523[3].first, var523[3].second)
var524
}
listOf(var521) + var522
}
}
val var515 = var519
val var525 = FieldOptions((var515 ?: listOf()), nextId(), beginGen, endGen)
return var525
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var526 = getSequenceElems(history, 346, listOf(160,7,167,7,168), beginGen, endGen)
val var527 = matchOptionName(var526[0].first, var526[0].second)
val var528 = matchConstant(var526[4].first, var526[4].second)
val var529 = FieldOption(var527, var528, nextId(), beginGen, endGen)
return var529
}

fun matchMessageMemberDef(beginGen: Int, endGen: Int): MessageMemberDef {
val var530 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var531 = history[endGen].findByBeginGenOpt(334, 1, beginGen)
val var532 = history[endGen].findByBeginGenOpt(352, 1, beginGen)
val var533 = history[endGen].findByBeginGenOpt(379, 1, beginGen)
val var534 = history[endGen].findByBeginGenOpt(385, 1, beginGen)
val var535 = history[endGen].findByBeginGenOpt(399, 1, beginGen)
check(hasSingleTrue(var530 != null, var531 != null, var532 != null, var533 != null, var534 != null, var535 != null))
val var536 = when {
var530 != null -> {
val var537 = matchOptionDef(beginGen, endGen)
var537
}
var531 != null -> {
val var538 = matchFieldDef(beginGen, endGen)
var538
}
var532 != null -> {
val var539 = matchEnumDef(beginGen, endGen)
var539
}
var533 != null -> {
val var540 = matchMessageDef(beginGen, endGen)
var540
}
var534 != null -> {
val var541 = matchOneOfDef(beginGen, endGen)
var541
}
else -> {
val var542 = matchReservedDef(beginGen, endGen)
var542
}
}
return var536
}

fun matchOneOfDef(beginGen: Int, endGen: Int): OneOfDef {
val var543 = getSequenceElems(history, 386, listOf(387,7,62,7,236,391,7,378), beginGen, endGen)
val var544 = matchIdent(var543[2].first, var543[2].second)
val var546 = history[var543[5].second].findByBeginGenOpt(82, 1, var543[5].first)
val var547 = history[var543[5].second].findByBeginGenOpt(392, 1, var543[5].first)
check(hasSingleTrue(var546 != null, var547 != null))
val var548 = when {
var546 != null -> null
else -> {
val var549 = matchOneOfMembersWS(var543[5].first, var543[5].second)
var549
}
}
val var545 = var548
val var550 = OneOfDef(var544, (var545 ?: listOf()), nextId(), beginGen, endGen)
return var550
}

fun matchOneOfMembersWS(beginGen: Int, endGen: Int): List<OneOfMembersDefWS> {
val var551 = getSequenceElems(history, 393, listOf(7,394,395), beginGen, endGen)
val var552 = matchWS(var551[0].first, var551[0].second)
val var553 = matchOneOfMemberDef(var551[1].first, var551[1].second)
val var554 = OneOfMembersDefWS(var552, var553, nextId(), beginGen, endGen)
val var555 = unrollRepeat0(history, 395, 397, 9, 396, var551[2].first, var551[2].second).map { k ->
val var556 = getSequenceElems(history, 398, listOf(369,394), k.first, k.second)
val var557 = history[var556[0].second].findByBeginGenOpt(370, 1, var556[0].first)
val var558 = history[var556[0].second].findByBeginGenOpt(372, 1, var556[0].first)
check(hasSingleTrue(var557 != null, var558 != null))
val var559 = when {
var557 != null -> {
val var560 = getSequenceElems(history, 371, listOf(7,307,7), var556[0].first, var556[0].second)
val var561 = matchWS(var560[2].first, var560[2].second)
var561
}
else -> {
val var562 = matchWSNL(var556[0].first, var556[0].second)
var562
}
}
val var563 = matchOneOfMemberDef(var556[1].first, var556[1].second)
val var564 = OneOfMembersDefWS(var559, var563, nextId(), k.first, k.second)
var564
}
return listOf(var554) + var555
}

fun matchReservedDef(beginGen: Int, endGen: Int): ReservedDef {
val var565 = getSequenceElems(history, 400, listOf(401,7,165,7,405,417,421,7,166), beginGen, endGen)
val var566 = matchReservedItem(var565[4].first, var565[4].second)
val var567 = unrollRepeat0(history, 417, 419, 9, 418, var565[5].first, var565[5].second).map { k ->
val var568 = getSequenceElems(history, 420, listOf(7,307,7,405), k.first, k.second)
val var569 = matchReservedItem(var568[3].first, var568[3].second)
var569
}
val var570 = ReservedDef(listOf(var566) + var567, nextId(), beginGen, endGen)
return var570
}

fun matchReservedItem(beginGen: Int, endGen: Int): ReservedItem {
val var571 = history[endGen].findByBeginGenOpt(62, 1, beginGen)
val var572 = history[endGen].findByBeginGenOpt(406, 1, beginGen)
check(hasSingleTrue(var571 != null, var572 != null))
val var573 = when {
var571 != null -> {
val var574 = matchIdent(beginGen, endGen)
var574
}
else -> {
val var575 = matchReservedRange(beginGen, endGen)
var575
}
}
return var573
}

fun matchOneOfMemberDef(beginGen: Int, endGen: Int): OneOfMemberDef {
val var576 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var577 = history[endGen].findByBeginGenOpt(334, 1, beginGen)
check(hasSingleTrue(var576 != null, var577 != null))
val var578 = when {
var576 != null -> {
val var579 = matchOptionDef(beginGen, endGen)
var579
}
else -> {
val var580 = matchFieldDef(beginGen, endGen)
var580
}
}
return var578
}

fun matchReservedRange(beginGen: Int, endGen: Int): ReservedRange {
val var581 = getSequenceElems(history, 407, listOf(185,408), beginGen, endGen)
val var582 = matchIntLiteral(var581[0].first, var581[0].second)
val var583 = history[var581[1].second].findByBeginGenOpt(82, 1, var581[1].first)
val var584 = history[var581[1].second].findByBeginGenOpt(409, 1, var581[1].first)
check(hasSingleTrue(var583 != null, var584 != null))
val var585 = when {
var583 != null -> null
else -> {
val var586 = getSequenceElems(history, 410, listOf(7,411,7,413), var581[1].first, var581[1].second)
val var587 = matchReservedRangeEnd(var586[3].first, var586[3].second)
var587
}
}
val var588 = ReservedRange(var582, var585, nextId(), beginGen, endGen)
return var588
}

fun matchReservedRangeEnd(beginGen: Int, endGen: Int): ReservedRangeEnd {
val var589 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var590 = history[endGen].findByBeginGenOpt(414, 1, beginGen)
check(hasSingleTrue(var589 != null, var590 != null))
val var591 = when {
var589 != null -> {
val var592 = matchIntLiteral(beginGen, endGen)
var592
}
else -> {
val var593 = Max(nextId(), beginGen, endGen)
var593
}
}
return var591
}

}
