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

data class KotlinAssume(
  val kind: AssumeKind,
  val name: FullIdent,
  val packageName: FullIdent,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KotlinOption, AstNode

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

data class KotlinOptions(
  val options: List<KotlinOption>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

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
  val kotlinOptions: KotlinOptions?,
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

data class OctalLiteral(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLiteral, AstNode

sealed interface KotlinOption: AstNode

data class PrimitiveType(
  val typ: PrimitiveTypeEnum,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

data class KotlinPackage(
  val name: FullIdent,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KotlinOption, AstNode

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
enum class AssumeKind { ENUM, MESSAGE, SEALED }
enum class BoolValueEnum { FALSE, TRUE }
enum class PrimitiveTypeEnum { BOOL, BYTES, DOUBLE, FIXED32, FIXED64, FLOAT, INT32, INT64, SFIXED32, SFIXED64, SINT32, SINT64, STRING, UINT32, UINT64 }

fun matchStart(): CompilationUnit {
  val lastGen = source.length
  val kernel = history[lastGen].getSingle(2, 1, 0, lastGen)
  return matchCompilationUnit(kernel.beginGen, kernel.endGen)
}

fun matchCompilationUnit(beginGen: Int, endGen: Int): CompilationUnit {
val var1 = getSequenceElems(history, 3, listOf(4,83,149,224,257,7), beginGen, endGen)
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
val var13 = history[var1[3].second].findByBeginGenOpt(82, 1, var1[3].first)
val var14 = history[var1[3].second].findByBeginGenOpt(225, 1, var1[3].first)
check(hasSingleTrue(var13 != null, var14 != null))
val var15 = when {
var13 != null -> null
else -> {
val var16 = getSequenceElems(history, 226, listOf(7,227), var1[3].first, var1[3].second)
val var17 = matchKotlinOptions(var16[1].first, var16[1].second)
var17
}
}
val var18 = unrollRepeat0(history, 257, 259, 9, 258, var1[4].first, var1[4].second).map { k ->
val var19 = matchTopLevelDefWS(k.first, k.second)
var19
}
val var20 = CompilationUnit(var4, var7, var10, var15, var18, nextId(), beginGen, endGen)
return var20
}

fun matchPackageDef(beginGen: Int, endGen: Int): FullIdent {
val var21 = getSequenceElems(history, 44, listOf(45,7,60), beginGen, endGen)
val var22 = matchFullIdent(var21[2].first, var21[2].second)
return var22
}

fun matchFullIdent(beginGen: Int, endGen: Int): FullIdent {
val var23 = getSequenceElems(history, 61, listOf(62,77), beginGen, endGen)
val var24 = matchIdent(var23[0].first, var23[0].second)
val var25 = unrollRepeat0(history, 77, 79, 9, 78, var23[1].first, var23[1].second).map { k ->
val var26 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var27 = matchIdent(var26[3].first, var26[3].second)
var27
}
val var28 = FullIdent(listOf(var24) + var25, nextId(), beginGen, endGen)
return var28
}

fun matchOptionDef(beginGen: Int, endGen: Int): OptionDef {
val var29 = getSequenceElems(history, 154, listOf(155,7,160,7,167,7,168), beginGen, endGen)
val var30 = matchOptionName(var29[2].first, var29[2].second)
val var31 = matchConstant(var29[6].first, var29[6].second)
val var32 = OptionDef(var30, var31, nextId(), beginGen, endGen)
return var32
}

fun matchConstant(beginGen: Int, endGen: Int): Constant {
val var33 = history[endGen].findByBeginGenOpt(98, 1, beginGen)
val var34 = history[endGen].findByBeginGenOpt(169, 1, beginGen)
val var35 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var36 = history[endGen].findByBeginGenOpt(181, 3, beginGen)
val var37 = history[endGen].findByBeginGenOpt(201, 3, beginGen)
val var38 = history[endGen].findByBeginGenOpt(222, 1, beginGen)
check(hasSingleTrue(var33 != null, var34 != null, var35 != null, var36 != null, var37 != null, var38 != null))
val var39 = when {
var33 != null -> {
val var40 = matchStringLiteral(beginGen, endGen)
var40
}
var34 != null -> {
val var41 = matchIdent(beginGen, endGen)
val var42 = FullIdent(listOf(var41), nextId(), beginGen, endGen)
var42
}
var35 != null -> {
val var43 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var44 = matchIdent(var43[0].first, var43[0].second)
val var45 = unrollRepeat1(history, 179, 79, 79, 180, var43[1].first, var43[1].second).map { k ->
val var46 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var47 = matchIdent(var46[3].first, var46[3].second)
var47
}
val var48 = FullIdent(listOf(var44) + var45, nextId(), beginGen, endGen)
var48
}
var36 != null -> {
val var49 = getSequenceElems(history, 181, listOf(182,7,185), beginGen, endGen)
val var50 = matchIntLiteral(var49[2].first, var49[2].second)
var50
}
var37 != null -> {
val var51 = getSequenceElems(history, 201, listOf(182,7,202), beginGen, endGen)
val var52 = matchFloatLiteral(var51[2].first, var51[2].second)
var52
}
else -> {
val var53 = matchBoolLiteral(beginGen, endGen)
var53
}
}
return var39
}

fun matchFloatLiteral(beginGen: Int, endGen: Int): AbstractFloatLiteral {
val var54 = history[endGen].findByBeginGenOpt(203, 4, beginGen)
val var55 = history[endGen].findByBeginGenOpt(214, 2, beginGen)
val var56 = history[endGen].findByBeginGenOpt(215, 3, beginGen)
val var57 = history[endGen].findByBeginGenOpt(216, 1, beginGen)
val var58 = history[endGen].findByBeginGenOpt(219, 1, beginGen)
check(hasSingleTrue(var54 != null, var55 != null, var56 != null, var57 != null, var58 != null))
val var59 = when {
var54 != null -> {
val var60 = getSequenceElems(history, 203, listOf(204,81,207,208), beginGen, endGen)
val var61 = matchDecimals(var60[0].first, var60[0].second)
val var62 = history[var60[2].second].findByBeginGenOpt(82, 1, var60[2].first)
val var63 = history[var60[2].second].findByBeginGenOpt(204, 1, var60[2].first)
check(hasSingleTrue(var62 != null, var63 != null))
val var64 = when {
var62 != null -> null
else -> {
val var65 = matchDecimals(var60[2].first, var60[2].second)
var65
}
}
val var66 = history[var60[3].second].findByBeginGenOpt(82, 1, var60[3].first)
val var67 = history[var60[3].second].findByBeginGenOpt(209, 1, var60[3].first)
check(hasSingleTrue(var66 != null, var67 != null))
val var68 = when {
var66 != null -> null
else -> {
val var69 = matchExponent(var60[3].first, var60[3].second)
var69
}
}
val var70 = FloatLiteral(var61, var64, var68, nextId(), beginGen, endGen)
var70
}
var55 != null -> {
val var71 = getSequenceElems(history, 214, listOf(204,209), beginGen, endGen)
val var72 = matchDecimals(var71[0].first, var71[0].second)
val var73 = matchExponent(var71[1].first, var71[1].second)
val var74 = FloatLiteral(var72, null, var73, nextId(), beginGen, endGen)
var74
}
var56 != null -> {
val var75 = getSequenceElems(history, 215, listOf(81,204,208), beginGen, endGen)
val var76 = matchDecimals(var75[1].first, var75[1].second)
val var77 = history[var75[2].second].findByBeginGenOpt(82, 1, var75[2].first)
val var78 = history[var75[2].second].findByBeginGenOpt(209, 1, var75[2].first)
check(hasSingleTrue(var77 != null, var78 != null))
val var79 = when {
var77 != null -> null
else -> {
val var80 = matchExponent(var75[2].first, var75[2].second)
var80
}
}
val var81 = FloatLiteral(null, var76, var79, nextId(), beginGen, endGen)
var81
}
var57 != null -> {
val var82 = Inf(nextId(), beginGen, endGen)
var82
}
else -> {
val var83 = Nan(nextId(), beginGen, endGen)
var83
}
}
return var59
}

fun matchBoolLiteral(beginGen: Int, endGen: Int): BoolLiteral {
val var84 = history[endGen].findByBeginGenOpt(171, 1, beginGen)
val var85 = history[endGen].findByBeginGenOpt(173, 1, beginGen)
check(hasSingleTrue(var84 != null, var85 != null))
val var86 = when {
var84 != null -> BoolValueEnum.TRUE
else -> BoolValueEnum.FALSE
}
val var87 = BoolLiteral(var86, nextId(), beginGen, endGen)
return var87
}

fun matchStringLiteral(beginGen: Int, endGen: Int): StringLiteral {
val var88 = getSequenceElems(history, 99, listOf(100,145), beginGen, endGen)
val var89 = matchStringLiteralSingle(var88[0].first, var88[0].second)
val var90 = unrollRepeat0(history, 145, 147, 9, 146, var88[1].first, var88[1].second).map { k ->
val var91 = getSequenceElems(history, 148, listOf(7,100), k.first, k.second)
val var92 = matchStringLiteralSingle(var91[1].first, var91[1].second)
var92
}
val var93 = StringLiteral(listOf(var89) + var90, nextId(), beginGen, endGen)
return var93
}

fun matchStringLiteralSingle(beginGen: Int, endGen: Int): StringLiteralSingle {
val var94 = history[endGen].findByBeginGenOpt(101, 3, beginGen)
val var95 = history[endGen].findByBeginGenOpt(143, 3, beginGen)
check(hasSingleTrue(var94 != null, var95 != null))
val var96 = when {
var94 != null -> {
val var97 = getSequenceElems(history, 101, listOf(102,103,102), beginGen, endGen)
val var98 = unrollRepeat0(history, 103, 105, 9, 104, var97[1].first, var97[1].second).map { k ->
val var99 = matchCharValue(k.first, k.second)
var99
}
val var100 = StringLiteralSingle(var98, nextId(), beginGen, endGen)
var100
}
else -> {
val var101 = getSequenceElems(history, 143, listOf(144,103,144), beginGen, endGen)
val var102 = unrollRepeat0(history, 103, 105, 9, 104, var101[1].first, var101[1].second).map { k ->
val var103 = matchCharValue(k.first, k.second)
var103
}
val var104 = StringLiteralSingle(var102, nextId(), beginGen, endGen)
var104
}
}
return var96
}

fun matchOptionName(beginGen: Int, endGen: Int): OptionName {
val var105 = getSequenceElems(history, 161, listOf(162,77), beginGen, endGen)
val var106 = history[var105[0].second].findByBeginGenOpt(62, 1, var105[0].first)
val var107 = history[var105[0].second].findByBeginGenOpt(163, 1, var105[0].first)
check(hasSingleTrue(var106 != null, var107 != null))
val var108 = when {
var106 != null -> {
val var109 = matchIdent(var105[0].first, var105[0].second)
val var110 = FullIdent(listOf(var109), nextId(), var105[0].first, var105[0].second)
var110
}
else -> {
val var111 = getSequenceElems(history, 164, listOf(165,7,60,7,166), var105[0].first, var105[0].second)
val var112 = matchFullIdent(var111[2].first, var111[2].second)
var112
}
}
val var113 = unrollRepeat0(history, 77, 79, 9, 78, var105[1].first, var105[1].second).map { k ->
val var114 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var115 = matchIdent(var114[3].first, var114[3].second)
var115
}
val var116 = OptionName(var108, var113, nextId(), beginGen, endGen)
return var116
}

fun matchTopLevelDefWS(beginGen: Int, endGen: Int): TopLevelDefWS {
val var117 = getSequenceElems(history, 260, listOf(7,261), beginGen, endGen)
val var118 = matchWS(var117[0].first, var117[0].second)
val var119 = matchTopLevelDef(var117[1].first, var117[1].second)
val var120 = TopLevelDefWS(var118, var119, nextId(), beginGen, endGen)
return var120
}

fun matchWS(beginGen: Int, endGen: Int): List<Comment?> {
val var121 = unrollRepeat0(history, 8, 11, 9, 10, beginGen, endGen).map { k ->
val var122 = history[k.second].findByBeginGenOpt(12, 1, k.first)
val var123 = history[k.second].findByBeginGenOpt(13, 1, k.first)
check(hasSingleTrue(var122 != null, var123 != null))
val var124 = when {
var122 != null -> null
else -> {
val var125 = matchComment(k.first, k.second)
var125
}
}
var124
}
return var121
}

fun matchDecimals(beginGen: Int, endGen: Int): String {
val var126 = unrollRepeat1(history, 205, 72, 72, 206, beginGen, endGen).map { k ->
val var127 = matchDecimalDigit(k.first, k.second)
var127
}
return var126.joinToString("") { it.toString() }
}

fun matchDecimalDigit(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchCharValue(beginGen: Int, endGen: Int): CharValue {
val var128 = history[endGen].findByBeginGenOpt(106, 1, beginGen)
val var129 = history[endGen].findByBeginGenOpt(109, 1, beginGen)
val var130 = history[endGen].findByBeginGenOpt(117, 1, beginGen)
val var131 = history[endGen].findByBeginGenOpt(124, 1, beginGen)
val var132 = history[endGen].findByBeginGenOpt(127, 1, beginGen)
val var133 = history[endGen].findByBeginGenOpt(130, 1, beginGen)
check(hasSingleTrue(var128 != null, var129 != null, var130 != null, var131 != null, var132 != null, var133 != null))
val var134 = when {
var128 != null -> {
val var135 = matchPlainChar(beginGen, endGen)
var135
}
var129 != null -> {
val var136 = matchHexEscape(beginGen, endGen)
var136
}
var130 != null -> {
val var137 = matchOctEscape(beginGen, endGen)
var137
}
var131 != null -> {
val var138 = matchCharEscape(beginGen, endGen)
var138
}
var132 != null -> {
val var139 = matchUnicodeEscape(beginGen, endGen)
var139
}
else -> {
val var140 = matchUnicodeLongEscape(beginGen, endGen)
var140
}
}
return var134
}

fun matchCharEscape(beginGen: Int, endGen: Int): CharEscape {
val var141 = getSequenceElems(history, 125, listOf(111,126), beginGen, endGen)
val var142 = CharEscape(source[var141[1].first], nextId(), beginGen, endGen)
return var142
}

fun matchUnicodeLongEscape(beginGen: Int, endGen: Int): UnicodeLongEscape {
val var143 = getSequenceElems(history, 131, listOf(111,129,132), beginGen, endGen)
val var144 = history[var143[2].second].findByBeginGenOpt(133, 1, var143[2].first)
val var145 = history[var143[2].second].findByBeginGenOpt(138, 1, var143[2].first)
check(hasSingleTrue(var144 != null, var145 != null))
val var146 = when {
var144 != null -> {
val var147 = getSequenceElems(history, 134, listOf(135,114,114,114,114,114), var143[2].first, var143[2].second)
val var148 = matchHexDigit(var147[1].first, var147[1].second)
val var149 = matchHexDigit(var147[2].first, var147[2].second)
val var150 = matchHexDigit(var147[3].first, var147[3].second)
val var151 = matchHexDigit(var147[4].first, var147[4].second)
val var152 = matchHexDigit(var147[5].first, var147[5].second)
val var153 = UnicodeLongEscape("000" + var148.toString() + var149.toString() + var150.toString() + var151.toString() + var152.toString(), nextId(), var143[2].first, var143[2].second)
var153
}
else -> {
val var154 = getSequenceElems(history, 139, listOf(140,114,114,114,114), var143[2].first, var143[2].second)
val var155 = matchHexDigit(var154[1].first, var154[1].second)
val var156 = matchHexDigit(var154[2].first, var154[2].second)
val var157 = matchHexDigit(var154[3].first, var154[3].second)
val var158 = matchHexDigit(var154[4].first, var154[4].second)
val var159 = UnicodeLongEscape("0010" + var155.toString() + var156.toString() + var157.toString() + var158.toString(), nextId(), var143[2].first, var143[2].second)
var159
}
}
return var146
}

fun matchPlainChar(beginGen: Int, endGen: Int): PlainChar {
val var160 = PlainChar(source[beginGen], nextId(), beginGen, endGen)
return var160
}

fun matchHexDigit(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchExponent(beginGen: Int, endGen: Int): Exponent {
val var161 = getSequenceElems(history, 210, listOf(211,212,204), beginGen, endGen)
val var162 = history[var161[1].second].findByBeginGenOpt(82, 1, var161[1].first)
val var163 = history[var161[1].second].findByBeginGenOpt(213, 1, var161[1].first)
check(hasSingleTrue(var162 != null, var163 != null))
val var164 = when {
var162 != null -> null
else -> source[var161[1].first]
}
val var165 = matchDecimals(var161[2].first, var161[2].second)
val var166 = Exponent(var164, var165, nextId(), beginGen, endGen)
return var166
}

fun matchHexEscape(beginGen: Int, endGen: Int): HexEscape {
val var167 = getSequenceElems(history, 110, listOf(111,112,113), beginGen, endGen)
val var168 = unrollRepeat1(history, 113, 114, 114, 116, var167[2].first, var167[2].second).map { k ->
val var169 = matchHexDigit(k.first, k.second)
var169
}
val var170 = HexEscape(var168.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var170
}

fun matchImportDef(beginGen: Int, endGen: Int): StringLiteral {
val var171 = getSequenceElems(history, 88, listOf(89,7,98), beginGen, endGen)
val var172 = matchStringLiteral(var171[2].first, var171[2].second)
return var172
}

fun matchKotlinOptions(beginGen: Int, endGen: Int): KotlinOptions {
val var173 = getSequenceElems(history, 228, listOf(229,7,233,234,7,256), beginGen, endGen)
val var174 = unrollRepeat0(history, 234, 236, 9, 235, var173[3].first, var173[3].second).map { k ->
val var175 = getSequenceElems(history, 237, listOf(7,238), k.first, k.second)
val var176 = matchKotlinOption(var175[1].first, var175[1].second)
var176
}
val var177 = KotlinOptions(var174, nextId(), beginGen, endGen)
return var177
}

fun matchKotlinOption(beginGen: Int, endGen: Int): KotlinOption {
val var178 = history[endGen].findByBeginGenOpt(44, 3, beginGen)
val var179 = history[endGen].findByBeginGenOpt(239, 9, beginGen)
check(hasSingleTrue(var178 != null, var179 != null))
val var180 = when {
var178 != null -> {
val var181 = getSequenceElems(history, 44, listOf(45,7,60), beginGen, endGen)
val var182 = matchFullIdent(var181[2].first, var181[2].second)
val var183 = KotlinPackage(var182, nextId(), beginGen, endGen)
var183
}
else -> {
val var184 = getSequenceElems(history, 239, listOf(240,7,244,7,60,7,252,7,60), beginGen, endGen)
val var185 = history[var184[2].second].findByBeginGenOpt(245, 1, var184[2].first)
val var186 = history[var184[2].second].findByBeginGenOpt(247, 1, var184[2].first)
val var187 = history[var184[2].second].findByBeginGenOpt(250, 1, var184[2].first)
check(hasSingleTrue(var185 != null, var186 != null, var187 != null))
val var188 = when {
var185 != null -> AssumeKind.MESSAGE
var186 != null -> AssumeKind.SEALED
else -> AssumeKind.ENUM
}
val var189 = matchFullIdent(var184[4].first, var184[4].second)
val var190 = matchFullIdent(var184[8].first, var184[8].second)
val var191 = KotlinAssume(var188, var189, var190, nextId(), beginGen, endGen)
var191
}
}
return var180
}

fun matchComment(beginGen: Int, endGen: Int): Comment {
val var192 = history[endGen].findByBeginGenOpt(14, 1, beginGen)
val var193 = history[endGen].findByBeginGenOpt(28, 1, beginGen)
check(hasSingleTrue(var192 != null, var193 != null))
val var194 = when {
var192 != null -> {
val var195 = matchLineComment(beginGen, endGen)
var195
}
else -> {
val var196 = matchBlockComment(beginGen, endGen)
var196
}
}
return var194
}

fun matchLineComment(beginGen: Int, endGen: Int): LineComment {
val var197 = getSequenceElems(history, 15, listOf(16,19,25), beginGen, endGen)
val var198 = unrollRepeat0(history, 19, 21, 9, 20, var197[1].first, var197[1].second).map { k ->
source[k.first]
}
val var199 = LineComment(var198.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var199
}

fun matchBlockComment(beginGen: Int, endGen: Int): BlockComment {
val var200 = history[endGen].findByBeginGenOpt(29, 4, beginGen)
val var201 = history[endGen].findByBeginGenOpt(41, 1, beginGen)
check(hasSingleTrue(var200 != null, var201 != null))
val var202 = when {
var200 != null -> {
val var203 = getSequenceElems(history, 29, listOf(30,33,23,39), beginGen, endGen)
val var204 = unrollRepeat0(history, 33, 35, 9, 34, var203[1].first, var203[1].second).map { k ->
val var205 = getSequenceElems(history, 36, listOf(23,37), k.first, k.second)
source[var205[0].first]
}
val var206 = BlockComment(var204.joinToString("") { it.toString() } + source[var203[2].first].toString(), nextId(), beginGen, endGen)
var206
}
else -> {
val var207 = BlockComment("", nextId(), beginGen, endGen)
var207
}
}
return var202
}

fun matchIntLiteral(beginGen: Int, endGen: Int): IntLiteral {
val var208 = history[endGen].findByBeginGenOpt(188, 1, beginGen)
val var209 = history[endGen].findByBeginGenOpt(190, 1, beginGen)
val var210 = history[endGen].findByBeginGenOpt(195, 1, beginGen)
val var211 = history[endGen].findByBeginGenOpt(199, 1, beginGen)
check(hasSingleTrue(var208 != null, var209 != null, var210 != null, var211 != null))
val var212 = when {
var208 != null -> {
val var213 = ZeroIntLiteral(nextId(), beginGen, endGen)
var213
}
var209 != null -> {
val var214 = matchDecimalLiteral(beginGen, endGen)
var214
}
var210 != null -> {
val var215 = matchOctalLiteral(beginGen, endGen)
var215
}
else -> {
val var216 = matchHexLiteral(beginGen, endGen)
var216
}
}
return var212
}

fun matchDecimalLiteral(beginGen: Int, endGen: Int): DecimalLiteral {
val var217 = getSequenceElems(history, 191, listOf(192,193), beginGen, endGen)
val var218 = unrollRepeat0(history, 193, 73, 9, 194, var217[1].first, var217[1].second).map { k ->
source[k.first]
}
val var219 = DecimalLiteral(source[var217[0].first].toString() + var218.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var219
}

fun matchOctalLiteral(beginGen: Int, endGen: Int): OctalLiteral {
val var220 = getSequenceElems(history, 196, listOf(137,197), beginGen, endGen)
val var221 = unrollRepeat1(history, 197, 119, 119, 198, var220[1].first, var220[1].second).map { k ->
source[k.first]
}
val var222 = OctalLiteral(var221.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var222
}

fun matchHexLiteral(beginGen: Int, endGen: Int): HexLiteral {
val var223 = getSequenceElems(history, 200, listOf(137,112,113), beginGen, endGen)
val var224 = unrollRepeat1(history, 113, 114, 114, 116, var223[2].first, var223[2].second).map { k ->
val var225 = matchHexDigit(k.first, k.second)
var225
}
val var226 = HexLiteral(var224.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var226
}

fun matchOctEscape(beginGen: Int, endGen: Int): OctEscape {
val var227 = getSequenceElems(history, 118, listOf(111,119,120), beginGen, endGen)
val var228 = history[var227[2].second].findByBeginGenOpt(82, 1, var227[2].first)
val var229 = history[var227[2].second].findByBeginGenOpt(121, 1, var227[2].first)
check(hasSingleTrue(var228 != null, var229 != null))
val var230 = when {
var228 != null -> null
else -> {
val var231 = getSequenceElems(history, 122, listOf(119,123), var227[2].first, var227[2].second)
val var232 = history[var231[1].second].findByBeginGenOpt(82, 1, var231[1].first)
val var233 = history[var231[1].second].findByBeginGenOpt(119, 1, var231[1].first)
check(hasSingleTrue(var232 != null, var233 != null))
val var234 = when {
var232 != null -> null
else -> source[var231[1].first]
}
var234
}
}
val var235 = OctEscape(source[var227[1].first].toString() + (var230?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
return var235
}

fun matchUnicodeEscape(beginGen: Int, endGen: Int): UnicodeEscape {
val var236 = getSequenceElems(history, 128, listOf(111,129,114,114,114,114), beginGen, endGen)
val var237 = matchHexDigit(var236[2].first, var236[2].second)
val var238 = matchHexDigit(var236[3].first, var236[3].second)
val var239 = matchHexDigit(var236[4].first, var236[4].second)
val var240 = matchHexDigit(var236[5].first, var236[5].second)
val var241 = UnicodeEscape(var237.toString() + var238.toString() + var239.toString() + var240.toString(), nextId(), beginGen, endGen)
return var241
}

fun matchIdent(beginGen: Int, endGen: Int): Ident {
val var242 = history[endGen].findByBeginGenOpt(63, 1, beginGen)
val var243 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
check(hasSingleTrue(var242 != null, var243 != null))
val var244 = when {
var242 != null -> {
val var245 = matchIdentName(beginGen, endGen)
val var246 = Ident(var245, nextId(), beginGen, endGen)
var246
}
else -> {
val var247 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var248 = matchIdentName(var247[1].first, var247[1].second)
val var249 = Ident(var248, nextId(), beginGen, endGen)
var249
}
}
return var244
}

fun matchIdentName(beginGen: Int, endGen: Int): String {
val var250 = getSequenceElems(history, 66, listOf(67,69), beginGen, endGen)
val var251 = matchLetter(var250[0].first, var250[0].second)
val var252 = unrollRepeat0(history, 69, 71, 9, 70, var250[1].first, var250[1].second).map { k ->
val var253 = history[k.second].findByBeginGenOpt(67, 1, k.first)
val var254 = history[k.second].findByBeginGenOpt(72, 1, k.first)
val var255 = history[k.second].findByBeginGenOpt(74, 1, k.first)
check(hasSingleTrue(var253 != null, var254 != null, var255 != null))
val var256 = when {
var253 != null -> {
val var257 = matchLetter(k.first, k.second)
var257
}
var254 != null -> {
val var258 = matchDecimalDigit(k.first, k.second)
var258
}
else -> source[k.first]
}
var256
}
return var251.toString() + var252.joinToString("") { it.toString() }
}

fun matchTopLevelDef(beginGen: Int, endGen: Int): TopLevelDef {
val var259 = history[endGen].findByBeginGenOpt(262, 1, beginGen)
val var260 = history[endGen].findByBeginGenOpt(379, 1, beginGen)
val var261 = history[endGen].findByBeginGenOpt(405, 1, beginGen)
val var262 = history[endGen].findByBeginGenOpt(504, 1, beginGen)
check(hasSingleTrue(var259 != null, var260 != null, var261 != null, var262 != null))
val var263 = when {
var259 != null -> {
val var264 = matchServiceDef(beginGen, endGen)
var264
}
var260 != null -> {
val var265 = matchEnumDef(beginGen, endGen)
var265
}
var261 != null -> {
val var266 = matchMessageDef(beginGen, endGen)
var266
}
else -> {
val var267 = matchSealedDef(beginGen, endGen)
var267
}
}
return var263
}

fun matchServiceDef(beginGen: Int, endGen: Int): ServiceDef {
val var268 = getSequenceElems(history, 263, listOf(264,7,62,7,233,269,7,256), beginGen, endGen)
val var269 = matchIdent(var268[2].first, var268[2].second)
val var270 = unrollRepeat0(history, 269, 271, 9, 270, var268[5].first, var268[5].second).map { k ->
val var271 = matchServiceMemberWS(k.first, k.second)
var271
}
val var272 = ServiceDef(var269, var270, nextId(), beginGen, endGen)
return var272
}

fun matchServiceMemberWS(beginGen: Int, endGen: Int): ServiceMemberWS {
val var273 = getSequenceElems(history, 272, listOf(7,273), beginGen, endGen)
val var274 = matchWS(var273[0].first, var273[0].second)
val var275 = matchServiceMember(var273[1].first, var273[1].second)
val var276 = ServiceMemberWS(var274, var275, nextId(), beginGen, endGen)
return var276
}

fun matchServiceMember(beginGen: Int, endGen: Int): ServiceMember {
val var277 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var278 = history[endGen].findByBeginGenOpt(274, 1, beginGen)
check(hasSingleTrue(var277 != null, var278 != null))
val var279 = when {
var277 != null -> {
val var280 = matchOptionDef(beginGen, endGen)
var280
}
else -> {
val var281 = matchRpcDef(beginGen, endGen)
var281
}
}
return var279
}

fun matchRpcDef(beginGen: Int, endGen: Int): RpcDef {
val var282 = getSequenceElems(history, 275, listOf(276,7,62,7,280,7,281,7,485,7,281,487,363), beginGen, endGen)
val var283 = matchIdent(var282[2].first, var282[2].second)
val var284 = matchType(var282[6].first, var282[6].second)
val var285 = matchType(var282[10].first, var282[10].second)
val var286 = history[var282[11].second].findByBeginGenOpt(82, 1, var282[11].first)
val var287 = history[var282[11].second].findByBeginGenOpt(488, 1, var282[11].first)
check(hasSingleTrue(var286 != null, var287 != null))
val var288 = when {
var286 != null -> null
else -> {
val var289 = getSequenceElems(history, 489, listOf(7,490,7,496), var282[11].first, var282[11].second)
val var290 = matchRpcTypeWheres(var289[3].first, var289[3].second)
var290
}
}
val var291 = history[var282[12].second].findByBeginGenOpt(82, 1, var282[12].first)
val var292 = history[var282[12].second].findByBeginGenOpt(364, 1, var282[12].first)
check(hasSingleTrue(var291 != null, var292 != null))
val var293 = when {
var291 != null -> null
else -> {
val var294 = getSequenceElems(history, 365, listOf(7,366), var282[12].first, var282[12].second)
val var295 = matchFieldOptions(var294[1].first, var294[1].second)
var295
}
}
val var296 = RpcDef(var283, var284, var285, var288, var293, nextId(), beginGen, endGen)
return var296
}

fun matchMessageDef(beginGen: Int, endGen: Int): MessageDef {
val var297 = getSequenceElems(history, 406, listOf(407,7,62,7,233,357,7,256), beginGen, endGen)
val var298 = matchIdent(var297[2].first, var297[2].second)
val var300 = history[var297[5].second].findByBeginGenOpt(82, 1, var297[5].first)
val var301 = history[var297[5].second].findByBeginGenOpt(358, 1, var297[5].first)
check(hasSingleTrue(var300 != null, var301 != null))
val var302 = when {
var300 != null -> null
else -> {
val var303 = matchMessageMembersWS(var297[5].first, var297[5].second)
var303
}
}
val var299 = var302
val var304 = MessageDef(var298, (var299 ?: listOf()), nextId(), beginGen, endGen)
return var304
}

fun matchMessageMembersWS(beginGen: Int, endGen: Int): List<MessageMemberDefWS> {
val var305 = getSequenceElems(history, 359, listOf(7,360,448), beginGen, endGen)
val var306 = matchWS(var305[0].first, var305[0].second)
val var307 = matchMessageMemberDef(var305[1].first, var305[1].second)
val var308 = MessageMemberDefWS(var306, var307, nextId(), beginGen, endGen)
val var309 = unrollRepeat0(history, 448, 450, 9, 449, var305[2].first, var305[2].second).map { k ->
val var310 = getSequenceElems(history, 451, listOf(396,360), k.first, k.second)
val var311 = history[var310[0].second].findByBeginGenOpt(397, 1, var310[0].first)
val var312 = history[var310[0].second].findByBeginGenOpt(399, 1, var310[0].first)
check(hasSingleTrue(var311 != null, var312 != null))
val var313 = when {
var311 != null -> {
val var314 = getSequenceElems(history, 398, listOf(7,343,7), var310[0].first, var310[0].second)
val var315 = matchWS(var314[2].first, var314[2].second)
var315
}
else -> {
val var316 = matchWSNL(var310[0].first, var310[0].second)
var316
}
}
val var317 = matchMessageMemberDef(var310[1].first, var310[1].second)
val var318 = MessageMemberDefWS(var313, var317, nextId(), k.first, k.second)
var318
}
return listOf(var308) + var309
}

fun matchWSNL(beginGen: Int, endGen: Int): List<Comment?> {
val var319 = getSequenceElems(history, 400, listOf(401,404,7), beginGen, endGen)
val var320 = history[var319[1].second].findByBeginGenOpt(14, 1, var319[1].first)
val var321 = history[var319[1].second].findByBeginGenOpt(24, 1, var319[1].first)
check(hasSingleTrue(var320 != null, var321 != null))
val var322 = when {
var320 != null -> {
val var323 = matchLineComment(var319[1].first, var319[1].second)
var323
}
else -> null
}
val var324 = matchWS(var319[2].first, var319[2].second)
return listOf(var322) + var324
}

fun matchEnumDef(beginGen: Int, endGen: Int): EnumDef {
val var325 = getSequenceElems(history, 380, listOf(381,7,62,7,233,383,7,256), beginGen, endGen)
val var326 = matchIdent(var325[2].first, var325[2].second)
val var328 = history[var325[5].second].findByBeginGenOpt(82, 1, var325[5].first)
val var329 = history[var325[5].second].findByBeginGenOpt(384, 1, var325[5].first)
check(hasSingleTrue(var328 != null, var329 != null))
val var330 = when {
var328 != null -> null
else -> {
val var331 = matchEnumMembersWS(var325[5].first, var325[5].second)
var331
}
}
val var327 = var330
val var332 = EnumDef(var326, (var327 ?: listOf()), nextId(), beginGen, endGen)
return var332
}

fun matchEnumMembersWS(beginGen: Int, endGen: Int): List<EnumMemberDefWS> {
val var333 = getSequenceElems(history, 385, listOf(7,386,392), beginGen, endGen)
val var334 = matchWS(var333[0].first, var333[0].second)
val var335 = matchEnumMemberDef(var333[1].first, var333[1].second)
val var336 = EnumMemberDefWS(var334, var335, nextId(), beginGen, endGen)
val var337 = unrollRepeat0(history, 392, 394, 9, 393, var333[2].first, var333[2].second).map { k ->
val var338 = getSequenceElems(history, 395, listOf(396,386), k.first, k.second)
val var339 = history[var338[0].second].findByBeginGenOpt(397, 1, var338[0].first)
val var340 = history[var338[0].second].findByBeginGenOpt(399, 1, var338[0].first)
check(hasSingleTrue(var339 != null, var340 != null))
val var341 = when {
var339 != null -> {
val var342 = getSequenceElems(history, 398, listOf(7,343,7), var338[0].first, var338[0].second)
val var343 = matchWS(var342[2].first, var342[2].second)
var343
}
else -> {
val var344 = matchWSNL(var338[0].first, var338[0].second)
var344
}
}
val var345 = matchEnumMemberDef(var338[1].first, var338[1].second)
val var346 = EnumMemberDefWS(var341, var345, nextId(), k.first, k.second)
var346
}
return listOf(var336) + var337
}

fun matchEnumMemberDef(beginGen: Int, endGen: Int): EnumMemberDef {
val var347 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var348 = history[endGen].findByBeginGenOpt(387, 1, beginGen)
check(hasSingleTrue(var347 != null, var348 != null))
val var349 = when {
var347 != null -> {
val var350 = matchOptionDef(beginGen, endGen)
var350
}
else -> {
val var351 = matchEnumFieldDef(beginGen, endGen)
var351
}
}
return var349
}

fun matchEnumFieldDef(beginGen: Int, endGen: Int): EnumFieldDef {
val var352 = getSequenceElems(history, 388, listOf(389,185,7,62,363), beginGen, endGen)
val var353 = history[var352[0].second].findByBeginGenOpt(82, 1, var352[0].first)
val var354 = history[var352[0].second].findByBeginGenOpt(390, 1, var352[0].first)
check(hasSingleTrue(var353 != null, var354 != null))
val var355 = when {
var353 != null -> null
else -> {
val var356 = getSequenceElems(history, 391, listOf(184,7), var352[0].first, var352[0].second)
source[var356[0].first]
}
}
val var357 = matchIntLiteral(var352[1].first, var352[1].second)
val var358 = matchIdent(var352[3].first, var352[3].second)
val var359 = history[var352[4].second].findByBeginGenOpt(82, 1, var352[4].first)
val var360 = history[var352[4].second].findByBeginGenOpt(364, 1, var352[4].first)
check(hasSingleTrue(var359 != null, var360 != null))
val var361 = when {
var359 != null -> null
else -> {
val var362 = getSequenceElems(history, 365, listOf(7,366), var352[4].first, var352[4].second)
val var363 = matchFieldOptions(var362[1].first, var362[1].second)
var363
}
}
val var364 = EnumFieldDef(var355 != null, var357, var358, var361, nextId(), beginGen, endGen)
return var364
}

fun matchSealedDef(beginGen: Int, endGen: Int): SealedDef {
val var365 = getSequenceElems(history, 505, listOf(454,7,62,7,233,461,7,256), beginGen, endGen)
val var366 = matchIdent(var365[2].first, var365[2].second)
val var368 = history[var365[5].second].findByBeginGenOpt(82, 1, var365[5].first)
val var369 = history[var365[5].second].findByBeginGenOpt(462, 1, var365[5].first)
check(hasSingleTrue(var368 != null, var369 != null))
val var370 = when {
var368 != null -> null
else -> {
val var371 = matchSealedMembersWS(var365[5].first, var365[5].second)
var371
}
}
val var367 = var370
val var372 = SealedDef(var366, (var367 ?: listOf()), nextId(), beginGen, endGen)
return var372
}

fun matchSealedMembersWS(beginGen: Int, endGen: Int): List<SealedMemberDefWS> {
val var373 = getSequenceElems(history, 463, listOf(7,464,471), beginGen, endGen)
val var374 = matchWS(var373[0].first, var373[0].second)
val var375 = matchSealedMemberDef(var373[1].first, var373[1].second)
val var376 = SealedMemberDefWS(var374, var375, nextId(), beginGen, endGen)
val var377 = unrollRepeat0(history, 471, 473, 9, 472, var373[2].first, var373[2].second).map { k ->
val var378 = getSequenceElems(history, 474, listOf(396,464), k.first, k.second)
val var379 = history[var378[0].second].findByBeginGenOpt(397, 1, var378[0].first)
val var380 = history[var378[0].second].findByBeginGenOpt(399, 1, var378[0].first)
check(hasSingleTrue(var379 != null, var380 != null))
val var381 = when {
var379 != null -> {
val var382 = getSequenceElems(history, 398, listOf(7,343,7), var378[0].first, var378[0].second)
val var383 = matchWS(var382[2].first, var382[2].second)
var383
}
else -> {
val var384 = matchWSNL(var378[0].first, var378[0].second)
var384
}
}
val var385 = matchSealedMemberDef(var378[1].first, var378[1].second)
val var386 = SealedMemberDefWS(var381, var385, nextId(), k.first, k.second)
var386
}
return listOf(var376) + var377
}

fun matchSealedMemberDef(beginGen: Int, endGen: Int): SealedMemberDef {
val var387 = history[endGen].findByBeginGenOpt(361, 1, beginGen)
val var388 = history[endGen].findByBeginGenOpt(465, 1, beginGen)
check(hasSingleTrue(var387 != null, var388 != null))
val var389 = when {
var387 != null -> {
val var390 = matchFieldDef(beginGen, endGen)
var390
}
else -> {
val var391 = matchCommonFieldDef(beginGen, endGen)
var391
}
}
return var389
}

fun matchCommonFieldDef(beginGen: Int, endGen: Int): CommonFieldDef {
val var392 = getSequenceElems(history, 466, listOf(467,7,361), beginGen, endGen)
val var393 = matchFieldDef(var392[2].first, var392[2].second)
val var394 = CommonFieldDef(var393, nextId(), beginGen, endGen)
return var394
}

fun matchFieldDef(beginGen: Int, endGen: Int): FieldDef {
val var395 = getSequenceElems(history, 362, listOf(185,7,62,7,280,7,281,363), beginGen, endGen)
val var396 = matchIntLiteral(var395[0].first, var395[0].second)
val var397 = matchIdent(var395[2].first, var395[2].second)
val var398 = matchType(var395[6].first, var395[6].second)
val var399 = history[var395[7].second].findByBeginGenOpt(82, 1, var395[7].first)
val var400 = history[var395[7].second].findByBeginGenOpt(364, 1, var395[7].first)
check(hasSingleTrue(var399 != null, var400 != null))
val var401 = when {
var399 != null -> null
else -> {
val var402 = getSequenceElems(history, 365, listOf(7,366), var395[7].first, var395[7].second)
val var403 = matchFieldOptions(var402[1].first, var402[1].second)
var403
}
}
val var404 = FieldDef(var396, var397, var398, var401, nextId(), beginGen, endGen)
return var404
}

fun matchLetter(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchType(beginGen: Int, endGen: Int): Type {
val var405 = history[endGen].findByBeginGenOpt(282, 1, beginGen)
val var406 = history[endGen].findByBeginGenOpt(321, 7, beginGen)
val var407 = history[endGen].findByBeginGenOpt(328, 7, beginGen)
val var408 = history[endGen].findByBeginGenOpt(333, 7, beginGen)
val var409 = history[endGen].findByBeginGenOpt(338, 11, beginGen)
val var410 = history[endGen].findByBeginGenOpt(344, 7, beginGen)
val var411 = history[endGen].findByBeginGenOpt(349, 1, beginGen)
val var412 = history[endGen].findByBeginGenOpt(452, 1, beginGen)
val var413 = history[endGen].findByBeginGenOpt(475, 1, beginGen)
val var414 = history[endGen].findByBeginGenOpt(482, 1, beginGen)
check(hasSingleTrue(var405 != null, var406 != null, var407 != null, var408 != null, var409 != null, var410 != null, var411 != null, var412 != null, var413 != null, var414 != null))
val var415 = when {
var405 != null -> {
val var416 = matchPrimitiveType(beginGen, endGen)
val var417 = PrimitiveType(var416, nextId(), beginGen, endGen)
var417
}
var406 != null -> {
val var418 = getSequenceElems(history, 321, listOf(322,7,326,7,281,7,327), beginGen, endGen)
val var419 = matchType(var418[4].first, var418[4].second)
val var420 = RepeatedType(var419, nextId(), beginGen, endGen)
var420
}
var407 != null -> {
val var421 = getSequenceElems(history, 328, listOf(329,7,326,7,281,7,327), beginGen, endGen)
val var422 = matchType(var421[4].first, var421[4].second)
val var423 = SetType(var422, nextId(), beginGen, endGen)
var423
}
var408 != null -> {
val var424 = getSequenceElems(history, 333, listOf(334,7,326,7,281,7,327), beginGen, endGen)
val var425 = matchType(var424[4].first, var424[4].second)
val var426 = OptionalType(var425, nextId(), beginGen, endGen)
var426
}
var409 != null -> {
val var427 = getSequenceElems(history, 338, listOf(339,7,326,7,281,7,343,7,281,7,327), beginGen, endGen)
val var428 = matchType(var427[4].first, var427[4].second)
val var429 = matchType(var427[8].first, var427[8].second)
val var430 = MapType(var428, var429, nextId(), beginGen, endGen)
var430
}
var410 != null -> {
val var431 = getSequenceElems(history, 344, listOf(345,7,326,7,281,7,327), beginGen, endGen)
val var432 = matchType(var431[4].first, var431[4].second)
val var433 = StreamType(var432, nextId(), beginGen, endGen)
var433
}
var411 != null -> {
val var434 = matchOnTheFlyMessageType(beginGen, endGen)
var434
}
var412 != null -> {
val var435 = matchOnTheFlySealedMessageType(beginGen, endGen)
var435
}
var413 != null -> {
val var436 = matchOnTheFlyEnumType(beginGen, endGen)
var436
}
else -> {
val var437 = matchTypeName(beginGen, endGen)
var437
}
}
return var415
}

fun matchTypeName(beginGen: Int, endGen: Int): TypeName {
val var438 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var439 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var440 = history[endGen].findByBeginGenOpt(483, 1, beginGen)
check(hasSingleTrue(var438 != null, var439 != null, var440 != null))
val var441 = when {
var438 != null -> {
val var442 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var443 = matchIdentName(var442[1].first, var442[1].second)
val var444 = SingleName(var443, nextId(), beginGen, endGen)
var444
}
var439 != null -> {
val var445 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var446 = matchIdent(var445[0].first, var445[0].second)
val var447 = unrollRepeat1(history, 179, 79, 79, 180, var445[1].first, var445[1].second).map { k ->
val var448 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var449 = matchIdent(var448[3].first, var448[3].second)
var449
}
val var450 = MultiName(listOf(var446) + var447, nextId(), beginGen, endGen)
var450
}
else -> {
val var451 = matchIdentName(beginGen, endGen)
val var452 = SingleName(var451, nextId(), beginGen, endGen)
var452
}
}
return var441
}

fun matchOnTheFlyMessageType(beginGen: Int, endGen: Int): OnTheFlyMessageType {
val var453 = getSequenceElems(history, 350, listOf(351,233,357,7,256), beginGen, endGen)
val var454 = history[var453[0].second].findByBeginGenOpt(82, 1, var453[0].first)
val var455 = history[var453[0].second].findByBeginGenOpt(352, 1, var453[0].first)
check(hasSingleTrue(var454 != null, var455 != null))
val var456 = when {
var454 != null -> null
else -> {
val var457 = getSequenceElems(history, 353, listOf(354,7), var453[0].first, var453[0].second)
val var458 = matchIdentNoSealedEnum(var457[0].first, var457[0].second)
var458
}
}
val var460 = history[var453[2].second].findByBeginGenOpt(82, 1, var453[2].first)
val var461 = history[var453[2].second].findByBeginGenOpt(358, 1, var453[2].first)
check(hasSingleTrue(var460 != null, var461 != null))
val var462 = when {
var460 != null -> null
else -> {
val var463 = matchMessageMembersWS(var453[2].first, var453[2].second)
var463
}
}
val var459 = var462
val var464 = OnTheFlyMessageType(var456, (var459 ?: listOf()), nextId(), beginGen, endGen)
return var464
}

fun matchIdentNoSealedEnum(beginGen: Int, endGen: Int): Ident {
val var465 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var466 = history[endGen].findByBeginGenOpt(355, 1, beginGen)
check(hasSingleTrue(var465 != null, var466 != null))
val var467 = when {
var465 != null -> {
val var468 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var469 = matchIdentName(var468[1].first, var468[1].second)
val var470 = Ident(var469, nextId(), beginGen, endGen)
var470
}
else -> {
val var471 = matchIdentName(beginGen, endGen)
val var472 = Ident(var471, nextId(), beginGen, endGen)
var472
}
}
return var467
}

fun matchOnTheFlyEnumType(beginGen: Int, endGen: Int): OnTheFlyEnumType {
val var473 = getSequenceElems(history, 476, listOf(381,477,7,233,383,7,256), beginGen, endGen)
val var474 = history[var473[1].second].findByBeginGenOpt(82, 1, var473[1].first)
val var475 = history[var473[1].second].findByBeginGenOpt(478, 1, var473[1].first)
check(hasSingleTrue(var474 != null, var475 != null))
val var476 = when {
var474 != null -> null
else -> {
val var477 = getSequenceElems(history, 479, listOf(7,480), var473[1].first, var473[1].second)
val var478 = matchIdentNoEnum(var477[1].first, var477[1].second)
var478
}
}
val var480 = history[var473[4].second].findByBeginGenOpt(82, 1, var473[4].first)
val var481 = history[var473[4].second].findByBeginGenOpt(384, 1, var473[4].first)
check(hasSingleTrue(var480 != null, var481 != null))
val var482 = when {
var480 != null -> null
else -> {
val var483 = matchEnumMembersWS(var473[4].first, var473[4].second)
var483
}
}
val var479 = var482
val var484 = OnTheFlyEnumType(var476, (var479 ?: listOf()), nextId(), beginGen, endGen)
return var484
}

fun matchIdentNoEnum(beginGen: Int, endGen: Int): Ident {
val var485 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var486 = history[endGen].findByBeginGenOpt(481, 1, beginGen)
check(hasSingleTrue(var485 != null, var486 != null))
val var487 = when {
var485 != null -> {
val var488 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var489 = matchIdentName(var488[1].first, var488[1].second)
val var490 = Ident(var489, nextId(), beginGen, endGen)
var490
}
else -> {
val var491 = matchIdentName(beginGen, endGen)
val var492 = Ident(var491, nextId(), beginGen, endGen)
var492
}
}
return var487
}

fun matchPrimitiveType(beginGen: Int, endGen: Int): PrimitiveTypeEnum {
val var493 = history[endGen].findByBeginGenOpt(284, 1, beginGen)
val var494 = history[endGen].findByBeginGenOpt(287, 1, beginGen)
val var495 = history[endGen].findByBeginGenOpt(289, 1, beginGen)
val var496 = history[endGen].findByBeginGenOpt(293, 1, beginGen)
val var497 = history[endGen].findByBeginGenOpt(297, 1, beginGen)
val var498 = history[endGen].findByBeginGenOpt(299, 1, beginGen)
val var499 = history[endGen].findByBeginGenOpt(301, 1, beginGen)
val var500 = history[endGen].findByBeginGenOpt(303, 1, beginGen)
val var501 = history[endGen].findByBeginGenOpt(305, 1, beginGen)
val var502 = history[endGen].findByBeginGenOpt(308, 1, beginGen)
val var503 = history[endGen].findByBeginGenOpt(310, 1, beginGen)
val var504 = history[endGen].findByBeginGenOpt(312, 1, beginGen)
val var505 = history[endGen].findByBeginGenOpt(314, 1, beginGen)
val var506 = history[endGen].findByBeginGenOpt(316, 1, beginGen)
val var507 = history[endGen].findByBeginGenOpt(318, 1, beginGen)
check(hasSingleTrue(var493 != null, var494 != null, var495 != null, var496 != null, var497 != null, var498 != null, var499 != null, var500 != null, var501 != null, var502 != null, var503 != null, var504 != null, var505 != null, var506 != null, var507 != null))
val var508 = when {
var493 != null -> PrimitiveTypeEnum.DOUBLE
var494 != null -> PrimitiveTypeEnum.FLOAT
var495 != null -> PrimitiveTypeEnum.INT32
var496 != null -> PrimitiveTypeEnum.INT64
var497 != null -> PrimitiveTypeEnum.UINT32
var498 != null -> PrimitiveTypeEnum.UINT64
var499 != null -> PrimitiveTypeEnum.SINT32
var500 != null -> PrimitiveTypeEnum.SINT64
var501 != null -> PrimitiveTypeEnum.FIXED32
var502 != null -> PrimitiveTypeEnum.FIXED64
var503 != null -> PrimitiveTypeEnum.SFIXED32
var504 != null -> PrimitiveTypeEnum.SFIXED64
var505 != null -> PrimitiveTypeEnum.BOOL
var506 != null -> PrimitiveTypeEnum.STRING
else -> PrimitiveTypeEnum.BYTES
}
return var508
}

fun matchRpcTypeWheres(beginGen: Int, endGen: Int): RpcTypeWheres {
val var509 = getSequenceElems(history, 497, listOf(498,500), beginGen, endGen)
val var510 = matchRpcTypeWhere(var509[0].first, var509[0].second)
val var511 = unrollRepeat0(history, 500, 502, 9, 501, var509[1].first, var509[1].second).map { k ->
val var512 = getSequenceElems(history, 503, listOf(7,343,7,498), k.first, k.second)
val var513 = matchRpcTypeWhere(var512[3].first, var512[3].second)
var513
}
val var514 = RpcTypeWheres(listOf(var510) + var511, nextId(), beginGen, endGen)
return var514
}

fun matchRpcTypeWhere(beginGen: Int, endGen: Int): RpcTypeWhere {
val var515 = getSequenceElems(history, 499, listOf(62,7,167,7,281), beginGen, endGen)
val var516 = matchIdent(var515[0].first, var515[0].second)
val var517 = matchType(var515[4].first, var515[4].second)
val var518 = RpcTypeWhere(var516, var517, nextId(), beginGen, endGen)
return var518
}

fun matchOnTheFlySealedMessageType(beginGen: Int, endGen: Int): OnTheFlySealedMessageType {
val var519 = getSequenceElems(history, 453, listOf(454,456,7,233,461,7,256), beginGen, endGen)
val var520 = history[var519[1].second].findByBeginGenOpt(82, 1, var519[1].first)
val var521 = history[var519[1].second].findByBeginGenOpt(457, 1, var519[1].first)
check(hasSingleTrue(var520 != null, var521 != null))
val var522 = when {
var520 != null -> null
else -> {
val var523 = getSequenceElems(history, 458, listOf(7,459), var519[1].first, var519[1].second)
val var524 = matchIdentNoSealed(var523[1].first, var523[1].second)
var524
}
}
val var526 = history[var519[4].second].findByBeginGenOpt(82, 1, var519[4].first)
val var527 = history[var519[4].second].findByBeginGenOpt(462, 1, var519[4].first)
check(hasSingleTrue(var526 != null, var527 != null))
val var528 = when {
var526 != null -> null
else -> {
val var529 = matchSealedMembersWS(var519[4].first, var519[4].second)
var529
}
}
val var525 = var528
val var530 = OnTheFlySealedMessageType(var522, (var525 ?: listOf()), nextId(), beginGen, endGen)
return var530
}

fun matchIdentNoSealed(beginGen: Int, endGen: Int): Ident {
val var531 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var532 = history[endGen].findByBeginGenOpt(460, 1, beginGen)
check(hasSingleTrue(var531 != null, var532 != null))
val var533 = when {
var531 != null -> {
val var534 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var535 = matchIdentName(var534[1].first, var534[1].second)
val var536 = Ident(var535, nextId(), beginGen, endGen)
var536
}
else -> {
val var537 = matchIdentName(beginGen, endGen)
val var538 = Ident(var537, nextId(), beginGen, endGen)
var538
}
}
return var533
}

fun matchFieldOptions(beginGen: Int, endGen: Int): FieldOptions {
val var540 = getSequenceElems(history, 367, listOf(368,369,7,378), beginGen, endGen)
val var541 = history[var540[1].second].findByBeginGenOpt(82, 1, var540[1].first)
val var542 = history[var540[1].second].findByBeginGenOpt(370, 1, var540[1].first)
check(hasSingleTrue(var541 != null, var542 != null))
val var543 = when {
var541 != null -> null
else -> {
val var544 = getSequenceElems(history, 371, listOf(7,372,374), var540[1].first, var540[1].second)
val var545 = matchFieldOption(var544[1].first, var544[1].second)
val var546 = unrollRepeat0(history, 374, 376, 9, 375, var544[2].first, var544[2].second).map { k ->
val var547 = getSequenceElems(history, 377, listOf(7,343,7,372), k.first, k.second)
val var548 = matchFieldOption(var547[3].first, var547[3].second)
var548
}
listOf(var545) + var546
}
}
val var539 = var543
val var549 = FieldOptions((var539 ?: listOf()), nextId(), beginGen, endGen)
return var549
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var550 = getSequenceElems(history, 373, listOf(160,7,167,7,168), beginGen, endGen)
val var551 = matchOptionName(var550[0].first, var550[0].second)
val var552 = matchConstant(var550[4].first, var550[4].second)
val var553 = FieldOption(var551, var552, nextId(), beginGen, endGen)
return var553
}

fun matchMessageMemberDef(beginGen: Int, endGen: Int): MessageMemberDef {
val var554 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var555 = history[endGen].findByBeginGenOpt(361, 1, beginGen)
val var556 = history[endGen].findByBeginGenOpt(379, 1, beginGen)
val var557 = history[endGen].findByBeginGenOpt(405, 1, beginGen)
val var558 = history[endGen].findByBeginGenOpt(409, 1, beginGen)
val var559 = history[endGen].findByBeginGenOpt(423, 1, beginGen)
check(hasSingleTrue(var554 != null, var555 != null, var556 != null, var557 != null, var558 != null, var559 != null))
val var560 = when {
var554 != null -> {
val var561 = matchOptionDef(beginGen, endGen)
var561
}
var555 != null -> {
val var562 = matchFieldDef(beginGen, endGen)
var562
}
var556 != null -> {
val var563 = matchEnumDef(beginGen, endGen)
var563
}
var557 != null -> {
val var564 = matchMessageDef(beginGen, endGen)
var564
}
var558 != null -> {
val var565 = matchOneOfDef(beginGen, endGen)
var565
}
else -> {
val var566 = matchReservedDef(beginGen, endGen)
var566
}
}
return var560
}

fun matchOneOfDef(beginGen: Int, endGen: Int): OneOfDef {
val var567 = getSequenceElems(history, 410, listOf(411,7,62,7,233,415,7,256), beginGen, endGen)
val var568 = matchIdent(var567[2].first, var567[2].second)
val var570 = history[var567[5].second].findByBeginGenOpt(82, 1, var567[5].first)
val var571 = history[var567[5].second].findByBeginGenOpt(416, 1, var567[5].first)
check(hasSingleTrue(var570 != null, var571 != null))
val var572 = when {
var570 != null -> null
else -> {
val var573 = matchOneOfMembersWS(var567[5].first, var567[5].second)
var573
}
}
val var569 = var572
val var574 = OneOfDef(var568, (var569 ?: listOf()), nextId(), beginGen, endGen)
return var574
}

fun matchOneOfMembersWS(beginGen: Int, endGen: Int): List<OneOfMembersDefWS> {
val var575 = getSequenceElems(history, 417, listOf(7,418,419), beginGen, endGen)
val var576 = matchWS(var575[0].first, var575[0].second)
val var577 = matchOneOfMemberDef(var575[1].first, var575[1].second)
val var578 = OneOfMembersDefWS(var576, var577, nextId(), beginGen, endGen)
val var579 = unrollRepeat0(history, 419, 421, 9, 420, var575[2].first, var575[2].second).map { k ->
val var580 = getSequenceElems(history, 422, listOf(396,418), k.first, k.second)
val var581 = history[var580[0].second].findByBeginGenOpt(397, 1, var580[0].first)
val var582 = history[var580[0].second].findByBeginGenOpt(399, 1, var580[0].first)
check(hasSingleTrue(var581 != null, var582 != null))
val var583 = when {
var581 != null -> {
val var584 = getSequenceElems(history, 398, listOf(7,343,7), var580[0].first, var580[0].second)
val var585 = matchWS(var584[2].first, var584[2].second)
var585
}
else -> {
val var586 = matchWSNL(var580[0].first, var580[0].second)
var586
}
}
val var587 = matchOneOfMemberDef(var580[1].first, var580[1].second)
val var588 = OneOfMembersDefWS(var583, var587, nextId(), k.first, k.second)
var588
}
return listOf(var578) + var579
}

fun matchReservedDef(beginGen: Int, endGen: Int): ReservedDef {
val var589 = getSequenceElems(history, 424, listOf(425,7,165,7,429,441,445,7,166), beginGen, endGen)
val var590 = matchReservedItem(var589[4].first, var589[4].second)
val var591 = unrollRepeat0(history, 441, 443, 9, 442, var589[5].first, var589[5].second).map { k ->
val var592 = getSequenceElems(history, 444, listOf(7,343,7,429), k.first, k.second)
val var593 = matchReservedItem(var592[3].first, var592[3].second)
var593
}
val var594 = ReservedDef(listOf(var590) + var591, nextId(), beginGen, endGen)
return var594
}

fun matchReservedItem(beginGen: Int, endGen: Int): ReservedItem {
val var595 = history[endGen].findByBeginGenOpt(62, 1, beginGen)
val var596 = history[endGen].findByBeginGenOpt(430, 1, beginGen)
check(hasSingleTrue(var595 != null, var596 != null))
val var597 = when {
var595 != null -> {
val var598 = matchIdent(beginGen, endGen)
var598
}
else -> {
val var599 = matchReservedRange(beginGen, endGen)
var599
}
}
return var597
}

fun matchOneOfMemberDef(beginGen: Int, endGen: Int): OneOfMemberDef {
val var600 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var601 = history[endGen].findByBeginGenOpt(361, 1, beginGen)
check(hasSingleTrue(var600 != null, var601 != null))
val var602 = when {
var600 != null -> {
val var603 = matchOptionDef(beginGen, endGen)
var603
}
else -> {
val var604 = matchFieldDef(beginGen, endGen)
var604
}
}
return var602
}

fun matchReservedRange(beginGen: Int, endGen: Int): ReservedRange {
val var605 = getSequenceElems(history, 431, listOf(185,432), beginGen, endGen)
val var606 = matchIntLiteral(var605[0].first, var605[0].second)
val var607 = history[var605[1].second].findByBeginGenOpt(82, 1, var605[1].first)
val var608 = history[var605[1].second].findByBeginGenOpt(433, 1, var605[1].first)
check(hasSingleTrue(var607 != null, var608 != null))
val var609 = when {
var607 != null -> null
else -> {
val var610 = getSequenceElems(history, 434, listOf(7,435,7,437), var605[1].first, var605[1].second)
val var611 = matchReservedRangeEnd(var610[3].first, var610[3].second)
var611
}
}
val var612 = ReservedRange(var606, var609, nextId(), beginGen, endGen)
return var612
}

fun matchReservedRangeEnd(beginGen: Int, endGen: Int): ReservedRangeEnd {
val var613 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var614 = history[endGen].findByBeginGenOpt(438, 1, beginGen)
check(hasSingleTrue(var613 != null, var614 != null))
val var615 = when {
var613 != null -> {
val var616 = matchIntLiteral(beginGen, endGen)
var616
}
else -> {
val var617 = Max(nextId(), beginGen, endGen)
var617
}
}
return var615
}

}
