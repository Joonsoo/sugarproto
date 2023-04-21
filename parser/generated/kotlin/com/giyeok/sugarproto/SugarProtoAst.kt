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

data class IndexedType(
  val elemType: Type,
  val keyExpr: IndexExpr,
  val keyType: Type?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

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

data class TargetElem(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IndexExpr, AstNode

data class StringLiteralSingle(
  val value: List<CharValue>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class SetType(
  val elemType: Type,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

data class FullIdent(
  val names: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode

sealed interface IndexExpr: AstNode

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

data class KotlinFromOtherPackage(
  val protoPkg: FullIdent,
  val kotlinPkg: FullIdent?,
  val uses: List<KotlinUse>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KotlinOption, AstNode

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

data class KotlinUse(
  val kind: TypeKind,
  val name: FullIdent,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

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

data class MemberAccess(
  val expr: IndexExpr,
  val name: Ident,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IndexExpr, AstNode
enum class BoolValueEnum { FALSE, TRUE }
enum class PrimitiveTypeEnum { BOOL, BYTES, DOUBLE, FIXED32, FIXED64, FLOAT, INT32, INT64, SFIXED32, SFIXED64, SINT32, SINT64, STRING, UINT32, UINT64 }
enum class TypeKind { ENUM, MESSAGE, SEALED }

fun matchStart(): CompilationUnit {
  val lastGen = source.length
  val kernel = history[lastGen].getSingle(2, 1, 0, lastGen)
  return matchCompilationUnit(kernel.beginGen, kernel.endGen)
}

fun matchCompilationUnit(beginGen: Int, endGen: Int): CompilationUnit {
val var1 = getSequenceElems(history, 3, listOf(4,83,149,224,267,7), beginGen, endGen)
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
val var18 = unrollRepeat0(history, 267, 269, 9, 268, var1[4].first, var1[4].second).map { k ->
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
val var117 = getSequenceElems(history, 270, listOf(7,271), beginGen, endGen)
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
val var173 = getSequenceElems(history, 228, listOf(229,7,233,234,7,266), beginGen, endGen)
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
val var179 = history[endGen].findByBeginGenOpt(239, 1, beginGen)
check(hasSingleTrue(var178 != null, var179 != null))
val var180 = when {
var178 != null -> {
val var181 = getSequenceElems(history, 44, listOf(45,7,60), beginGen, endGen)
val var182 = matchFullIdent(var181[2].first, var181[2].second)
val var183 = KotlinPackage(var182, nextId(), beginGen, endGen)
var183
}
else -> {
val var184 = matchKotlinFromOtherPackage(beginGen, endGen)
var184
}
}
return var180
}

fun matchKotlinFromOtherPackage(beginGen: Int, endGen: Int): KotlinFromOtherPackage {
val var185 = getSequenceElems(history, 240, listOf(241,7,45,7,60,245,7,233,248,7,266), beginGen, endGen)
val var186 = matchFullIdent(var185[4].first, var185[4].second)
val var187 = history[var185[5].second].findByBeginGenOpt(82, 1, var185[5].first)
val var188 = history[var185[5].second].findByBeginGenOpt(246, 1, var185[5].first)
check(hasSingleTrue(var187 != null, var188 != null))
val var189 = when {
var187 != null -> null
else -> {
val var190 = getSequenceElems(history, 247, listOf(7,165,7,229,7,167,7,60,7,166), var185[5].first, var185[5].second)
val var191 = matchFullIdent(var190[7].first, var190[7].second)
var191
}
}
val var192 = unrollRepeat0(history, 248, 250, 9, 249, var185[8].first, var185[8].second).map { k ->
val var193 = getSequenceElems(history, 251, listOf(7,252), k.first, k.second)
val var194 = matchKotlinUse(var193[1].first, var193[1].second)
var194
}
val var195 = KotlinFromOtherPackage(var186, var189, var192, nextId(), beginGen, endGen)
return var195
}

fun matchKotlinUse(beginGen: Int, endGen: Int): KotlinUse {
val var196 = getSequenceElems(history, 253, listOf(254,7,258,7,60), beginGen, endGen)
val var197 = history[var196[2].second].findByBeginGenOpt(259, 1, var196[2].first)
val var198 = history[var196[2].second].findByBeginGenOpt(261, 1, var196[2].first)
val var199 = history[var196[2].second].findByBeginGenOpt(264, 1, var196[2].first)
check(hasSingleTrue(var197 != null, var198 != null, var199 != null))
val var200 = when {
var197 != null -> TypeKind.MESSAGE
var198 != null -> TypeKind.SEALED
else -> TypeKind.ENUM
}
val var201 = matchFullIdent(var196[4].first, var196[4].second)
val var202 = KotlinUse(var200, var201, nextId(), beginGen, endGen)
return var202
}

fun matchComment(beginGen: Int, endGen: Int): Comment {
val var203 = history[endGen].findByBeginGenOpt(14, 1, beginGen)
val var204 = history[endGen].findByBeginGenOpt(28, 1, beginGen)
check(hasSingleTrue(var203 != null, var204 != null))
val var205 = when {
var203 != null -> {
val var206 = matchLineComment(beginGen, endGen)
var206
}
else -> {
val var207 = matchBlockComment(beginGen, endGen)
var207
}
}
return var205
}

fun matchLineComment(beginGen: Int, endGen: Int): LineComment {
val var208 = getSequenceElems(history, 15, listOf(16,19,25), beginGen, endGen)
val var209 = unrollRepeat0(history, 19, 21, 9, 20, var208[1].first, var208[1].second).map { k ->
source[k.first]
}
val var210 = LineComment(var209.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var210
}

fun matchBlockComment(beginGen: Int, endGen: Int): BlockComment {
val var211 = history[endGen].findByBeginGenOpt(29, 4, beginGen)
val var212 = history[endGen].findByBeginGenOpt(41, 1, beginGen)
check(hasSingleTrue(var211 != null, var212 != null))
val var213 = when {
var211 != null -> {
val var214 = getSequenceElems(history, 29, listOf(30,33,23,39), beginGen, endGen)
val var215 = unrollRepeat0(history, 33, 35, 9, 34, var214[1].first, var214[1].second).map { k ->
val var216 = getSequenceElems(history, 36, listOf(23,37), k.first, k.second)
source[var216[0].first]
}
val var217 = BlockComment(var215.joinToString("") { it.toString() } + source[var214[2].first].toString(), nextId(), beginGen, endGen)
var217
}
else -> {
val var218 = BlockComment("", nextId(), beginGen, endGen)
var218
}
}
return var213
}

fun matchIntLiteral(beginGen: Int, endGen: Int): IntLiteral {
val var219 = history[endGen].findByBeginGenOpt(188, 1, beginGen)
val var220 = history[endGen].findByBeginGenOpt(190, 1, beginGen)
val var221 = history[endGen].findByBeginGenOpt(195, 1, beginGen)
val var222 = history[endGen].findByBeginGenOpt(199, 1, beginGen)
check(hasSingleTrue(var219 != null, var220 != null, var221 != null, var222 != null))
val var223 = when {
var219 != null -> {
val var224 = ZeroIntLiteral(nextId(), beginGen, endGen)
var224
}
var220 != null -> {
val var225 = matchDecimalLiteral(beginGen, endGen)
var225
}
var221 != null -> {
val var226 = matchOctalLiteral(beginGen, endGen)
var226
}
else -> {
val var227 = matchHexLiteral(beginGen, endGen)
var227
}
}
return var223
}

fun matchDecimalLiteral(beginGen: Int, endGen: Int): DecimalLiteral {
val var228 = getSequenceElems(history, 191, listOf(192,193), beginGen, endGen)
val var229 = unrollRepeat0(history, 193, 73, 9, 194, var228[1].first, var228[1].second).map { k ->
source[k.first]
}
val var230 = DecimalLiteral(source[var228[0].first].toString() + var229.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var230
}

fun matchOctalLiteral(beginGen: Int, endGen: Int): OctalLiteral {
val var231 = getSequenceElems(history, 196, listOf(137,197), beginGen, endGen)
val var232 = unrollRepeat1(history, 197, 119, 119, 198, var231[1].first, var231[1].second).map { k ->
source[k.first]
}
val var233 = OctalLiteral(var232.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var233
}

fun matchHexLiteral(beginGen: Int, endGen: Int): HexLiteral {
val var234 = getSequenceElems(history, 200, listOf(137,112,113), beginGen, endGen)
val var235 = unrollRepeat1(history, 113, 114, 114, 116, var234[2].first, var234[2].second).map { k ->
val var236 = matchHexDigit(k.first, k.second)
var236
}
val var237 = HexLiteral(var235.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var237
}

fun matchOctEscape(beginGen: Int, endGen: Int): OctEscape {
val var238 = getSequenceElems(history, 118, listOf(111,119,120), beginGen, endGen)
val var239 = history[var238[2].second].findByBeginGenOpt(82, 1, var238[2].first)
val var240 = history[var238[2].second].findByBeginGenOpt(121, 1, var238[2].first)
check(hasSingleTrue(var239 != null, var240 != null))
val var241 = when {
var239 != null -> null
else -> {
val var242 = getSequenceElems(history, 122, listOf(119,123), var238[2].first, var238[2].second)
val var243 = history[var242[1].second].findByBeginGenOpt(82, 1, var242[1].first)
val var244 = history[var242[1].second].findByBeginGenOpt(119, 1, var242[1].first)
check(hasSingleTrue(var243 != null, var244 != null))
val var245 = when {
var243 != null -> null
else -> source[var242[1].first]
}
var245
}
}
val var246 = OctEscape(source[var238[1].first].toString() + (var241?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
return var246
}

fun matchUnicodeEscape(beginGen: Int, endGen: Int): UnicodeEscape {
val var247 = getSequenceElems(history, 128, listOf(111,129,114,114,114,114), beginGen, endGen)
val var248 = matchHexDigit(var247[2].first, var247[2].second)
val var249 = matchHexDigit(var247[3].first, var247[3].second)
val var250 = matchHexDigit(var247[4].first, var247[4].second)
val var251 = matchHexDigit(var247[5].first, var247[5].second)
val var252 = UnicodeEscape(var248.toString() + var249.toString() + var250.toString() + var251.toString(), nextId(), beginGen, endGen)
return var252
}

fun matchIdent(beginGen: Int, endGen: Int): Ident {
val var253 = history[endGen].findByBeginGenOpt(63, 1, beginGen)
val var254 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
check(hasSingleTrue(var253 != null, var254 != null))
val var255 = when {
var253 != null -> {
val var256 = matchIdentName(beginGen, endGen)
val var257 = Ident(var256, nextId(), beginGen, endGen)
var257
}
else -> {
val var258 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var259 = matchIdentName(var258[1].first, var258[1].second)
val var260 = Ident(var259, nextId(), beginGen, endGen)
var260
}
}
return var255
}

fun matchIdentName(beginGen: Int, endGen: Int): String {
val var261 = getSequenceElems(history, 66, listOf(67,69), beginGen, endGen)
val var262 = matchLetter(var261[0].first, var261[0].second)
val var263 = unrollRepeat0(history, 69, 71, 9, 70, var261[1].first, var261[1].second).map { k ->
val var264 = history[k.second].findByBeginGenOpt(67, 1, k.first)
val var265 = history[k.second].findByBeginGenOpt(72, 1, k.first)
val var266 = history[k.second].findByBeginGenOpt(74, 1, k.first)
check(hasSingleTrue(var264 != null, var265 != null, var266 != null))
val var267 = when {
var264 != null -> {
val var268 = matchLetter(k.first, k.second)
var268
}
var265 != null -> {
val var269 = matchDecimalDigit(k.first, k.second)
var269
}
else -> source[k.first]
}
var267
}
return var262.toString() + var263.joinToString("") { it.toString() }
}

fun matchTopLevelDef(beginGen: Int, endGen: Int): TopLevelDef {
val var270 = history[endGen].findByBeginGenOpt(272, 1, beginGen)
val var271 = history[endGen].findByBeginGenOpt(399, 1, beginGen)
val var272 = history[endGen].findByBeginGenOpt(425, 1, beginGen)
val var273 = history[endGen].findByBeginGenOpt(524, 1, beginGen)
check(hasSingleTrue(var270 != null, var271 != null, var272 != null, var273 != null))
val var274 = when {
var270 != null -> {
val var275 = matchServiceDef(beginGen, endGen)
var275
}
var271 != null -> {
val var276 = matchEnumDef(beginGen, endGen)
var276
}
var272 != null -> {
val var277 = matchMessageDef(beginGen, endGen)
var277
}
else -> {
val var278 = matchSealedDef(beginGen, endGen)
var278
}
}
return var274
}

fun matchServiceDef(beginGen: Int, endGen: Int): ServiceDef {
val var279 = getSequenceElems(history, 273, listOf(274,7,62,7,233,279,7,266), beginGen, endGen)
val var280 = matchIdent(var279[2].first, var279[2].second)
val var281 = unrollRepeat0(history, 279, 281, 9, 280, var279[5].first, var279[5].second).map { k ->
val var282 = matchServiceMemberWS(k.first, k.second)
var282
}
val var283 = ServiceDef(var280, var281, nextId(), beginGen, endGen)
return var283
}

fun matchServiceMemberWS(beginGen: Int, endGen: Int): ServiceMemberWS {
val var284 = getSequenceElems(history, 282, listOf(7,283), beginGen, endGen)
val var285 = matchWS(var284[0].first, var284[0].second)
val var286 = matchServiceMember(var284[1].first, var284[1].second)
val var287 = ServiceMemberWS(var285, var286, nextId(), beginGen, endGen)
return var287
}

fun matchServiceMember(beginGen: Int, endGen: Int): ServiceMember {
val var288 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var289 = history[endGen].findByBeginGenOpt(284, 1, beginGen)
check(hasSingleTrue(var288 != null, var289 != null))
val var290 = when {
var288 != null -> {
val var291 = matchOptionDef(beginGen, endGen)
var291
}
else -> {
val var292 = matchRpcDef(beginGen, endGen)
var292
}
}
return var290
}

fun matchRpcDef(beginGen: Int, endGen: Int): RpcDef {
val var293 = getSequenceElems(history, 285, listOf(286,7,62,7,290,7,291,7,505,7,291,507,383), beginGen, endGen)
val var294 = matchIdent(var293[2].first, var293[2].second)
val var295 = matchType(var293[6].first, var293[6].second)
val var296 = matchType(var293[10].first, var293[10].second)
val var297 = history[var293[11].second].findByBeginGenOpt(82, 1, var293[11].first)
val var298 = history[var293[11].second].findByBeginGenOpt(508, 1, var293[11].first)
check(hasSingleTrue(var297 != null, var298 != null))
val var299 = when {
var297 != null -> null
else -> {
val var300 = getSequenceElems(history, 509, listOf(7,510,7,516), var293[11].first, var293[11].second)
val var301 = matchRpcTypeWheres(var300[3].first, var300[3].second)
var301
}
}
val var302 = history[var293[12].second].findByBeginGenOpt(82, 1, var293[12].first)
val var303 = history[var293[12].second].findByBeginGenOpt(384, 1, var293[12].first)
check(hasSingleTrue(var302 != null, var303 != null))
val var304 = when {
var302 != null -> null
else -> {
val var305 = getSequenceElems(history, 385, listOf(7,386), var293[12].first, var293[12].second)
val var306 = matchFieldOptions(var305[1].first, var305[1].second)
var306
}
}
val var307 = RpcDef(var294, var295, var296, var299, var304, nextId(), beginGen, endGen)
return var307
}

fun matchMessageDef(beginGen: Int, endGen: Int): MessageDef {
val var308 = getSequenceElems(history, 426, listOf(427,7,62,7,233,377,7,266), beginGen, endGen)
val var309 = matchIdent(var308[2].first, var308[2].second)
val var311 = history[var308[5].second].findByBeginGenOpt(82, 1, var308[5].first)
val var312 = history[var308[5].second].findByBeginGenOpt(378, 1, var308[5].first)
check(hasSingleTrue(var311 != null, var312 != null))
val var313 = when {
var311 != null -> null
else -> {
val var314 = matchMessageMembersWS(var308[5].first, var308[5].second)
var314
}
}
val var310 = var313
val var315 = MessageDef(var309, (var310 ?: listOf()), nextId(), beginGen, endGen)
return var315
}

fun matchMessageMembersWS(beginGen: Int, endGen: Int): List<MessageMemberDefWS> {
val var316 = getSequenceElems(history, 379, listOf(7,380,468), beginGen, endGen)
val var317 = matchWS(var316[0].first, var316[0].second)
val var318 = matchMessageMemberDef(var316[1].first, var316[1].second)
val var319 = MessageMemberDefWS(var317, var318, nextId(), beginGen, endGen)
val var320 = unrollRepeat0(history, 468, 470, 9, 469, var316[2].first, var316[2].second).map { k ->
val var321 = getSequenceElems(history, 471, listOf(416,380), k.first, k.second)
val var322 = history[var321[0].second].findByBeginGenOpt(417, 1, var321[0].first)
val var323 = history[var321[0].second].findByBeginGenOpt(419, 1, var321[0].first)
check(hasSingleTrue(var322 != null, var323 != null))
val var324 = when {
var322 != null -> {
val var325 = getSequenceElems(history, 418, listOf(7,353,7), var321[0].first, var321[0].second)
val var326 = matchWS(var325[2].first, var325[2].second)
var326
}
else -> {
val var327 = matchWSNL(var321[0].first, var321[0].second)
var327
}
}
val var328 = matchMessageMemberDef(var321[1].first, var321[1].second)
val var329 = MessageMemberDefWS(var324, var328, nextId(), k.first, k.second)
var329
}
return listOf(var319) + var320
}

fun matchWSNL(beginGen: Int, endGen: Int): List<Comment?> {
val var330 = getSequenceElems(history, 420, listOf(421,424,7), beginGen, endGen)
val var331 = history[var330[1].second].findByBeginGenOpt(14, 1, var330[1].first)
val var332 = history[var330[1].second].findByBeginGenOpt(24, 1, var330[1].first)
check(hasSingleTrue(var331 != null, var332 != null))
val var333 = when {
var331 != null -> {
val var334 = matchLineComment(var330[1].first, var330[1].second)
var334
}
else -> null
}
val var335 = matchWS(var330[2].first, var330[2].second)
return listOf(var333) + var335
}

fun matchEnumDef(beginGen: Int, endGen: Int): EnumDef {
val var336 = getSequenceElems(history, 400, listOf(401,7,62,7,233,403,7,266), beginGen, endGen)
val var337 = matchIdent(var336[2].first, var336[2].second)
val var339 = history[var336[5].second].findByBeginGenOpt(82, 1, var336[5].first)
val var340 = history[var336[5].second].findByBeginGenOpt(404, 1, var336[5].first)
check(hasSingleTrue(var339 != null, var340 != null))
val var341 = when {
var339 != null -> null
else -> {
val var342 = matchEnumMembersWS(var336[5].first, var336[5].second)
var342
}
}
val var338 = var341
val var343 = EnumDef(var337, (var338 ?: listOf()), nextId(), beginGen, endGen)
return var343
}

fun matchEnumMembersWS(beginGen: Int, endGen: Int): List<EnumMemberDefWS> {
val var344 = getSequenceElems(history, 405, listOf(7,406,412), beginGen, endGen)
val var345 = matchWS(var344[0].first, var344[0].second)
val var346 = matchEnumMemberDef(var344[1].first, var344[1].second)
val var347 = EnumMemberDefWS(var345, var346, nextId(), beginGen, endGen)
val var348 = unrollRepeat0(history, 412, 414, 9, 413, var344[2].first, var344[2].second).map { k ->
val var349 = getSequenceElems(history, 415, listOf(416,406), k.first, k.second)
val var350 = history[var349[0].second].findByBeginGenOpt(417, 1, var349[0].first)
val var351 = history[var349[0].second].findByBeginGenOpt(419, 1, var349[0].first)
check(hasSingleTrue(var350 != null, var351 != null))
val var352 = when {
var350 != null -> {
val var353 = getSequenceElems(history, 418, listOf(7,353,7), var349[0].first, var349[0].second)
val var354 = matchWS(var353[2].first, var353[2].second)
var354
}
else -> {
val var355 = matchWSNL(var349[0].first, var349[0].second)
var355
}
}
val var356 = matchEnumMemberDef(var349[1].first, var349[1].second)
val var357 = EnumMemberDefWS(var352, var356, nextId(), k.first, k.second)
var357
}
return listOf(var347) + var348
}

fun matchEnumMemberDef(beginGen: Int, endGen: Int): EnumMemberDef {
val var358 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var359 = history[endGen].findByBeginGenOpt(407, 1, beginGen)
check(hasSingleTrue(var358 != null, var359 != null))
val var360 = when {
var358 != null -> {
val var361 = matchOptionDef(beginGen, endGen)
var361
}
else -> {
val var362 = matchEnumFieldDef(beginGen, endGen)
var362
}
}
return var360
}

fun matchEnumFieldDef(beginGen: Int, endGen: Int): EnumFieldDef {
val var363 = getSequenceElems(history, 408, listOf(409,185,7,62,383), beginGen, endGen)
val var364 = history[var363[0].second].findByBeginGenOpt(82, 1, var363[0].first)
val var365 = history[var363[0].second].findByBeginGenOpt(410, 1, var363[0].first)
check(hasSingleTrue(var364 != null, var365 != null))
val var366 = when {
var364 != null -> null
else -> {
val var367 = getSequenceElems(history, 411, listOf(184,7), var363[0].first, var363[0].second)
source[var367[0].first]
}
}
val var368 = matchIntLiteral(var363[1].first, var363[1].second)
val var369 = matchIdent(var363[3].first, var363[3].second)
val var370 = history[var363[4].second].findByBeginGenOpt(82, 1, var363[4].first)
val var371 = history[var363[4].second].findByBeginGenOpt(384, 1, var363[4].first)
check(hasSingleTrue(var370 != null, var371 != null))
val var372 = when {
var370 != null -> null
else -> {
val var373 = getSequenceElems(history, 385, listOf(7,386), var363[4].first, var363[4].second)
val var374 = matchFieldOptions(var373[1].first, var373[1].second)
var374
}
}
val var375 = EnumFieldDef(var366 != null, var368, var369, var372, nextId(), beginGen, endGen)
return var375
}

fun matchSealedDef(beginGen: Int, endGen: Int): SealedDef {
val var376 = getSequenceElems(history, 525, listOf(474,7,62,7,233,481,7,266), beginGen, endGen)
val var377 = matchIdent(var376[2].first, var376[2].second)
val var379 = history[var376[5].second].findByBeginGenOpt(82, 1, var376[5].first)
val var380 = history[var376[5].second].findByBeginGenOpt(482, 1, var376[5].first)
check(hasSingleTrue(var379 != null, var380 != null))
val var381 = when {
var379 != null -> null
else -> {
val var382 = matchSealedMembersWS(var376[5].first, var376[5].second)
var382
}
}
val var378 = var381
val var383 = SealedDef(var377, (var378 ?: listOf()), nextId(), beginGen, endGen)
return var383
}

fun matchSealedMembersWS(beginGen: Int, endGen: Int): List<SealedMemberDefWS> {
val var384 = getSequenceElems(history, 483, listOf(7,484,491), beginGen, endGen)
val var385 = matchWS(var384[0].first, var384[0].second)
val var386 = matchSealedMemberDef(var384[1].first, var384[1].second)
val var387 = SealedMemberDefWS(var385, var386, nextId(), beginGen, endGen)
val var388 = unrollRepeat0(history, 491, 493, 9, 492, var384[2].first, var384[2].second).map { k ->
val var389 = getSequenceElems(history, 494, listOf(416,484), k.first, k.second)
val var390 = history[var389[0].second].findByBeginGenOpt(417, 1, var389[0].first)
val var391 = history[var389[0].second].findByBeginGenOpt(419, 1, var389[0].first)
check(hasSingleTrue(var390 != null, var391 != null))
val var392 = when {
var390 != null -> {
val var393 = getSequenceElems(history, 418, listOf(7,353,7), var389[0].first, var389[0].second)
val var394 = matchWS(var393[2].first, var393[2].second)
var394
}
else -> {
val var395 = matchWSNL(var389[0].first, var389[0].second)
var395
}
}
val var396 = matchSealedMemberDef(var389[1].first, var389[1].second)
val var397 = SealedMemberDefWS(var392, var396, nextId(), k.first, k.second)
var397
}
return listOf(var387) + var388
}

fun matchSealedMemberDef(beginGen: Int, endGen: Int): SealedMemberDef {
val var398 = history[endGen].findByBeginGenOpt(381, 1, beginGen)
val var399 = history[endGen].findByBeginGenOpt(485, 1, beginGen)
check(hasSingleTrue(var398 != null, var399 != null))
val var400 = when {
var398 != null -> {
val var401 = matchFieldDef(beginGen, endGen)
var401
}
else -> {
val var402 = matchCommonFieldDef(beginGen, endGen)
var402
}
}
return var400
}

fun matchCommonFieldDef(beginGen: Int, endGen: Int): CommonFieldDef {
val var403 = getSequenceElems(history, 486, listOf(487,7,381), beginGen, endGen)
val var404 = matchFieldDef(var403[2].first, var403[2].second)
val var405 = CommonFieldDef(var404, nextId(), beginGen, endGen)
return var405
}

fun matchFieldDef(beginGen: Int, endGen: Int): FieldDef {
val var406 = getSequenceElems(history, 382, listOf(185,7,62,7,290,7,291,383), beginGen, endGen)
val var407 = matchIntLiteral(var406[0].first, var406[0].second)
val var408 = matchIdent(var406[2].first, var406[2].second)
val var409 = matchType(var406[6].first, var406[6].second)
val var410 = history[var406[7].second].findByBeginGenOpt(82, 1, var406[7].first)
val var411 = history[var406[7].second].findByBeginGenOpt(384, 1, var406[7].first)
check(hasSingleTrue(var410 != null, var411 != null))
val var412 = when {
var410 != null -> null
else -> {
val var413 = getSequenceElems(history, 385, listOf(7,386), var406[7].first, var406[7].second)
val var414 = matchFieldOptions(var413[1].first, var413[1].second)
var414
}
}
val var415 = FieldDef(var407, var408, var409, var412, nextId(), beginGen, endGen)
return var415
}

fun matchLetter(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchType(beginGen: Int, endGen: Int): Type {
val var416 = history[endGen].findByBeginGenOpt(292, 1, beginGen)
val var417 = history[endGen].findByBeginGenOpt(331, 7, beginGen)
val var418 = history[endGen].findByBeginGenOpt(338, 7, beginGen)
val var419 = history[endGen].findByBeginGenOpt(343, 7, beginGen)
val var420 = history[endGen].findByBeginGenOpt(348, 11, beginGen)
val var421 = history[endGen].findByBeginGenOpt(354, 14, beginGen)
val var422 = history[endGen].findByBeginGenOpt(364, 7, beginGen)
val var423 = history[endGen].findByBeginGenOpt(369, 1, beginGen)
val var424 = history[endGen].findByBeginGenOpt(472, 1, beginGen)
val var425 = history[endGen].findByBeginGenOpt(495, 1, beginGen)
val var426 = history[endGen].findByBeginGenOpt(502, 1, beginGen)
check(hasSingleTrue(var416 != null, var417 != null, var418 != null, var419 != null, var420 != null, var421 != null, var422 != null, var423 != null, var424 != null, var425 != null, var426 != null))
val var427 = when {
var416 != null -> {
val var428 = matchPrimitiveType(beginGen, endGen)
val var429 = PrimitiveType(var428, nextId(), beginGen, endGen)
var429
}
var417 != null -> {
val var430 = getSequenceElems(history, 331, listOf(332,7,336,7,291,7,337), beginGen, endGen)
val var431 = matchType(var430[4].first, var430[4].second)
val var432 = RepeatedType(var431, nextId(), beginGen, endGen)
var432
}
var418 != null -> {
val var433 = getSequenceElems(history, 338, listOf(339,7,336,7,291,7,337), beginGen, endGen)
val var434 = matchType(var433[4].first, var433[4].second)
val var435 = SetType(var434, nextId(), beginGen, endGen)
var435
}
var419 != null -> {
val var436 = getSequenceElems(history, 343, listOf(344,7,336,7,291,7,337), beginGen, endGen)
val var437 = matchType(var436[4].first, var436[4].second)
val var438 = OptionalType(var437, nextId(), beginGen, endGen)
var438
}
var420 != null -> {
val var439 = getSequenceElems(history, 348, listOf(349,7,336,7,291,7,353,7,291,7,337), beginGen, endGen)
val var440 = matchType(var439[4].first, var439[4].second)
val var441 = matchType(var439[8].first, var439[8].second)
val var442 = MapType(var440, var441, nextId(), beginGen, endGen)
var442
}
var421 != null -> {
val var443 = getSequenceElems(history, 354, listOf(355,7,336,7,291,7,337,7,233,7,359,361,7,266), beginGen, endGen)
val var444 = matchType(var443[4].first, var443[4].second)
val var445 = matchIndexExpr(var443[10].first, var443[10].second)
val var446 = history[var443[11].second].findByBeginGenOpt(82, 1, var443[11].first)
val var447 = history[var443[11].second].findByBeginGenOpt(362, 1, var443[11].first)
check(hasSingleTrue(var446 != null, var447 != null))
val var448 = when {
var446 != null -> null
else -> {
val var449 = getSequenceElems(history, 363, listOf(7,290,7,291), var443[11].first, var443[11].second)
val var450 = matchType(var449[3].first, var449[3].second)
var450
}
}
val var451 = IndexedType(var444, var445, var448, nextId(), beginGen, endGen)
var451
}
var422 != null -> {
val var452 = getSequenceElems(history, 364, listOf(365,7,336,7,291,7,337), beginGen, endGen)
val var453 = matchType(var452[4].first, var452[4].second)
val var454 = StreamType(var453, nextId(), beginGen, endGen)
var454
}
var423 != null -> {
val var455 = matchOnTheFlyMessageType(beginGen, endGen)
var455
}
var424 != null -> {
val var456 = matchOnTheFlySealedMessageType(beginGen, endGen)
var456
}
var425 != null -> {
val var457 = matchOnTheFlyEnumType(beginGen, endGen)
var457
}
else -> {
val var458 = matchTypeName(beginGen, endGen)
var458
}
}
return var427
}

fun matchIndexExpr(beginGen: Int, endGen: Int): IndexExpr {
val var459 = history[endGen].findByBeginGenOpt(74, 1, beginGen)
val var460 = history[endGen].findByBeginGenOpt(360, 5, beginGen)
check(hasSingleTrue(var459 != null, var460 != null))
val var461 = when {
var459 != null -> {
val var462 = TargetElem(nextId(), beginGen, endGen)
var462
}
else -> {
val var463 = getSequenceElems(history, 360, listOf(359,7,81,7,62), beginGen, endGen)
val var464 = matchIndexExpr(var463[0].first, var463[0].second)
val var465 = matchIdent(var463[4].first, var463[4].second)
val var466 = MemberAccess(var464, var465, nextId(), beginGen, endGen)
var466
}
}
return var461
}

fun matchTypeName(beginGen: Int, endGen: Int): TypeName {
val var467 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var468 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var469 = history[endGen].findByBeginGenOpt(503, 1, beginGen)
check(hasSingleTrue(var467 != null, var468 != null, var469 != null))
val var470 = when {
var467 != null -> {
val var471 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var472 = matchIdentName(var471[1].first, var471[1].second)
val var473 = SingleName(var472, nextId(), beginGen, endGen)
var473
}
var468 != null -> {
val var474 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var475 = matchIdent(var474[0].first, var474[0].second)
val var476 = unrollRepeat1(history, 179, 79, 79, 180, var474[1].first, var474[1].second).map { k ->
val var477 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var478 = matchIdent(var477[3].first, var477[3].second)
var478
}
val var479 = MultiName(listOf(var475) + var476, nextId(), beginGen, endGen)
var479
}
else -> {
val var480 = matchIdentName(beginGen, endGen)
val var481 = SingleName(var480, nextId(), beginGen, endGen)
var481
}
}
return var470
}

fun matchOnTheFlyMessageType(beginGen: Int, endGen: Int): OnTheFlyMessageType {
val var482 = getSequenceElems(history, 370, listOf(371,233,377,7,266), beginGen, endGen)
val var483 = history[var482[0].second].findByBeginGenOpt(82, 1, var482[0].first)
val var484 = history[var482[0].second].findByBeginGenOpt(372, 1, var482[0].first)
check(hasSingleTrue(var483 != null, var484 != null))
val var485 = when {
var483 != null -> null
else -> {
val var486 = getSequenceElems(history, 373, listOf(374,7), var482[0].first, var482[0].second)
val var487 = matchIdentNoSealedEnum(var486[0].first, var486[0].second)
var487
}
}
val var489 = history[var482[2].second].findByBeginGenOpt(82, 1, var482[2].first)
val var490 = history[var482[2].second].findByBeginGenOpt(378, 1, var482[2].first)
check(hasSingleTrue(var489 != null, var490 != null))
val var491 = when {
var489 != null -> null
else -> {
val var492 = matchMessageMembersWS(var482[2].first, var482[2].second)
var492
}
}
val var488 = var491
val var493 = OnTheFlyMessageType(var485, (var488 ?: listOf()), nextId(), beginGen, endGen)
return var493
}

fun matchIdentNoSealedEnum(beginGen: Int, endGen: Int): Ident {
val var494 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var495 = history[endGen].findByBeginGenOpt(375, 1, beginGen)
check(hasSingleTrue(var494 != null, var495 != null))
val var496 = when {
var494 != null -> {
val var497 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var498 = matchIdentName(var497[1].first, var497[1].second)
val var499 = Ident(var498, nextId(), beginGen, endGen)
var499
}
else -> {
val var500 = matchIdentName(beginGen, endGen)
val var501 = Ident(var500, nextId(), beginGen, endGen)
var501
}
}
return var496
}

fun matchOnTheFlyEnumType(beginGen: Int, endGen: Int): OnTheFlyEnumType {
val var502 = getSequenceElems(history, 496, listOf(401,497,7,233,403,7,266), beginGen, endGen)
val var503 = history[var502[1].second].findByBeginGenOpt(82, 1, var502[1].first)
val var504 = history[var502[1].second].findByBeginGenOpt(498, 1, var502[1].first)
check(hasSingleTrue(var503 != null, var504 != null))
val var505 = when {
var503 != null -> null
else -> {
val var506 = getSequenceElems(history, 499, listOf(7,500), var502[1].first, var502[1].second)
val var507 = matchIdentNoEnum(var506[1].first, var506[1].second)
var507
}
}
val var509 = history[var502[4].second].findByBeginGenOpt(82, 1, var502[4].first)
val var510 = history[var502[4].second].findByBeginGenOpt(404, 1, var502[4].first)
check(hasSingleTrue(var509 != null, var510 != null))
val var511 = when {
var509 != null -> null
else -> {
val var512 = matchEnumMembersWS(var502[4].first, var502[4].second)
var512
}
}
val var508 = var511
val var513 = OnTheFlyEnumType(var505, (var508 ?: listOf()), nextId(), beginGen, endGen)
return var513
}

fun matchIdentNoEnum(beginGen: Int, endGen: Int): Ident {
val var514 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var515 = history[endGen].findByBeginGenOpt(501, 1, beginGen)
check(hasSingleTrue(var514 != null, var515 != null))
val var516 = when {
var514 != null -> {
val var517 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var518 = matchIdentName(var517[1].first, var517[1].second)
val var519 = Ident(var518, nextId(), beginGen, endGen)
var519
}
else -> {
val var520 = matchIdentName(beginGen, endGen)
val var521 = Ident(var520, nextId(), beginGen, endGen)
var521
}
}
return var516
}

fun matchPrimitiveType(beginGen: Int, endGen: Int): PrimitiveTypeEnum {
val var522 = history[endGen].findByBeginGenOpt(294, 1, beginGen)
val var523 = history[endGen].findByBeginGenOpt(297, 1, beginGen)
val var524 = history[endGen].findByBeginGenOpt(299, 1, beginGen)
val var525 = history[endGen].findByBeginGenOpt(303, 1, beginGen)
val var526 = history[endGen].findByBeginGenOpt(307, 1, beginGen)
val var527 = history[endGen].findByBeginGenOpt(309, 1, beginGen)
val var528 = history[endGen].findByBeginGenOpt(311, 1, beginGen)
val var529 = history[endGen].findByBeginGenOpt(313, 1, beginGen)
val var530 = history[endGen].findByBeginGenOpt(315, 1, beginGen)
val var531 = history[endGen].findByBeginGenOpt(318, 1, beginGen)
val var532 = history[endGen].findByBeginGenOpt(320, 1, beginGen)
val var533 = history[endGen].findByBeginGenOpt(322, 1, beginGen)
val var534 = history[endGen].findByBeginGenOpt(324, 1, beginGen)
val var535 = history[endGen].findByBeginGenOpt(326, 1, beginGen)
val var536 = history[endGen].findByBeginGenOpt(328, 1, beginGen)
check(hasSingleTrue(var522 != null, var523 != null, var524 != null, var525 != null, var526 != null, var527 != null, var528 != null, var529 != null, var530 != null, var531 != null, var532 != null, var533 != null, var534 != null, var535 != null, var536 != null))
val var537 = when {
var522 != null -> PrimitiveTypeEnum.DOUBLE
var523 != null -> PrimitiveTypeEnum.FLOAT
var524 != null -> PrimitiveTypeEnum.INT32
var525 != null -> PrimitiveTypeEnum.INT64
var526 != null -> PrimitiveTypeEnum.UINT32
var527 != null -> PrimitiveTypeEnum.UINT64
var528 != null -> PrimitiveTypeEnum.SINT32
var529 != null -> PrimitiveTypeEnum.SINT64
var530 != null -> PrimitiveTypeEnum.FIXED32
var531 != null -> PrimitiveTypeEnum.FIXED64
var532 != null -> PrimitiveTypeEnum.SFIXED32
var533 != null -> PrimitiveTypeEnum.SFIXED64
var534 != null -> PrimitiveTypeEnum.BOOL
var535 != null -> PrimitiveTypeEnum.STRING
else -> PrimitiveTypeEnum.BYTES
}
return var537
}

fun matchRpcTypeWheres(beginGen: Int, endGen: Int): RpcTypeWheres {
val var538 = getSequenceElems(history, 517, listOf(518,520), beginGen, endGen)
val var539 = matchRpcTypeWhere(var538[0].first, var538[0].second)
val var540 = unrollRepeat0(history, 520, 522, 9, 521, var538[1].first, var538[1].second).map { k ->
val var541 = getSequenceElems(history, 523, listOf(7,353,7,518), k.first, k.second)
val var542 = matchRpcTypeWhere(var541[3].first, var541[3].second)
var542
}
val var543 = RpcTypeWheres(listOf(var539) + var540, nextId(), beginGen, endGen)
return var543
}

fun matchRpcTypeWhere(beginGen: Int, endGen: Int): RpcTypeWhere {
val var544 = getSequenceElems(history, 519, listOf(62,7,167,7,291), beginGen, endGen)
val var545 = matchIdent(var544[0].first, var544[0].second)
val var546 = matchType(var544[4].first, var544[4].second)
val var547 = RpcTypeWhere(var545, var546, nextId(), beginGen, endGen)
return var547
}

fun matchOnTheFlySealedMessageType(beginGen: Int, endGen: Int): OnTheFlySealedMessageType {
val var548 = getSequenceElems(history, 473, listOf(474,476,7,233,481,7,266), beginGen, endGen)
val var549 = history[var548[1].second].findByBeginGenOpt(82, 1, var548[1].first)
val var550 = history[var548[1].second].findByBeginGenOpt(477, 1, var548[1].first)
check(hasSingleTrue(var549 != null, var550 != null))
val var551 = when {
var549 != null -> null
else -> {
val var552 = getSequenceElems(history, 478, listOf(7,479), var548[1].first, var548[1].second)
val var553 = matchIdentNoSealed(var552[1].first, var552[1].second)
var553
}
}
val var555 = history[var548[4].second].findByBeginGenOpt(82, 1, var548[4].first)
val var556 = history[var548[4].second].findByBeginGenOpt(482, 1, var548[4].first)
check(hasSingleTrue(var555 != null, var556 != null))
val var557 = when {
var555 != null -> null
else -> {
val var558 = matchSealedMembersWS(var548[4].first, var548[4].second)
var558
}
}
val var554 = var557
val var559 = OnTheFlySealedMessageType(var551, (var554 ?: listOf()), nextId(), beginGen, endGen)
return var559
}

fun matchIdentNoSealed(beginGen: Int, endGen: Int): Ident {
val var560 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var561 = history[endGen].findByBeginGenOpt(480, 1, beginGen)
check(hasSingleTrue(var560 != null, var561 != null))
val var562 = when {
var560 != null -> {
val var563 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var564 = matchIdentName(var563[1].first, var563[1].second)
val var565 = Ident(var564, nextId(), beginGen, endGen)
var565
}
else -> {
val var566 = matchIdentName(beginGen, endGen)
val var567 = Ident(var566, nextId(), beginGen, endGen)
var567
}
}
return var562
}

fun matchFieldOptions(beginGen: Int, endGen: Int): FieldOptions {
val var569 = getSequenceElems(history, 387, listOf(388,389,7,398), beginGen, endGen)
val var570 = history[var569[1].second].findByBeginGenOpt(82, 1, var569[1].first)
val var571 = history[var569[1].second].findByBeginGenOpt(390, 1, var569[1].first)
check(hasSingleTrue(var570 != null, var571 != null))
val var572 = when {
var570 != null -> null
else -> {
val var573 = getSequenceElems(history, 391, listOf(7,392,394), var569[1].first, var569[1].second)
val var574 = matchFieldOption(var573[1].first, var573[1].second)
val var575 = unrollRepeat0(history, 394, 396, 9, 395, var573[2].first, var573[2].second).map { k ->
val var576 = getSequenceElems(history, 397, listOf(7,353,7,392), k.first, k.second)
val var577 = matchFieldOption(var576[3].first, var576[3].second)
var577
}
listOf(var574) + var575
}
}
val var568 = var572
val var578 = FieldOptions((var568 ?: listOf()), nextId(), beginGen, endGen)
return var578
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var579 = getSequenceElems(history, 393, listOf(160,7,167,7,168), beginGen, endGen)
val var580 = matchOptionName(var579[0].first, var579[0].second)
val var581 = matchConstant(var579[4].first, var579[4].second)
val var582 = FieldOption(var580, var581, nextId(), beginGen, endGen)
return var582
}

fun matchMessageMemberDef(beginGen: Int, endGen: Int): MessageMemberDef {
val var583 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var584 = history[endGen].findByBeginGenOpt(381, 1, beginGen)
val var585 = history[endGen].findByBeginGenOpt(399, 1, beginGen)
val var586 = history[endGen].findByBeginGenOpt(425, 1, beginGen)
val var587 = history[endGen].findByBeginGenOpt(429, 1, beginGen)
val var588 = history[endGen].findByBeginGenOpt(443, 1, beginGen)
check(hasSingleTrue(var583 != null, var584 != null, var585 != null, var586 != null, var587 != null, var588 != null))
val var589 = when {
var583 != null -> {
val var590 = matchOptionDef(beginGen, endGen)
var590
}
var584 != null -> {
val var591 = matchFieldDef(beginGen, endGen)
var591
}
var585 != null -> {
val var592 = matchEnumDef(beginGen, endGen)
var592
}
var586 != null -> {
val var593 = matchMessageDef(beginGen, endGen)
var593
}
var587 != null -> {
val var594 = matchOneOfDef(beginGen, endGen)
var594
}
else -> {
val var595 = matchReservedDef(beginGen, endGen)
var595
}
}
return var589
}

fun matchOneOfDef(beginGen: Int, endGen: Int): OneOfDef {
val var596 = getSequenceElems(history, 430, listOf(431,7,62,7,233,435,7,266), beginGen, endGen)
val var597 = matchIdent(var596[2].first, var596[2].second)
val var599 = history[var596[5].second].findByBeginGenOpt(82, 1, var596[5].first)
val var600 = history[var596[5].second].findByBeginGenOpt(436, 1, var596[5].first)
check(hasSingleTrue(var599 != null, var600 != null))
val var601 = when {
var599 != null -> null
else -> {
val var602 = matchOneOfMembersWS(var596[5].first, var596[5].second)
var602
}
}
val var598 = var601
val var603 = OneOfDef(var597, (var598 ?: listOf()), nextId(), beginGen, endGen)
return var603
}

fun matchOneOfMembersWS(beginGen: Int, endGen: Int): List<OneOfMembersDefWS> {
val var604 = getSequenceElems(history, 437, listOf(7,438,439), beginGen, endGen)
val var605 = matchWS(var604[0].first, var604[0].second)
val var606 = matchOneOfMemberDef(var604[1].first, var604[1].second)
val var607 = OneOfMembersDefWS(var605, var606, nextId(), beginGen, endGen)
val var608 = unrollRepeat0(history, 439, 441, 9, 440, var604[2].first, var604[2].second).map { k ->
val var609 = getSequenceElems(history, 442, listOf(416,438), k.first, k.second)
val var610 = history[var609[0].second].findByBeginGenOpt(417, 1, var609[0].first)
val var611 = history[var609[0].second].findByBeginGenOpt(419, 1, var609[0].first)
check(hasSingleTrue(var610 != null, var611 != null))
val var612 = when {
var610 != null -> {
val var613 = getSequenceElems(history, 418, listOf(7,353,7), var609[0].first, var609[0].second)
val var614 = matchWS(var613[2].first, var613[2].second)
var614
}
else -> {
val var615 = matchWSNL(var609[0].first, var609[0].second)
var615
}
}
val var616 = matchOneOfMemberDef(var609[1].first, var609[1].second)
val var617 = OneOfMembersDefWS(var612, var616, nextId(), k.first, k.second)
var617
}
return listOf(var607) + var608
}

fun matchReservedDef(beginGen: Int, endGen: Int): ReservedDef {
val var618 = getSequenceElems(history, 444, listOf(445,7,165,7,449,461,465,7,166), beginGen, endGen)
val var619 = matchReservedItem(var618[4].first, var618[4].second)
val var620 = unrollRepeat0(history, 461, 463, 9, 462, var618[5].first, var618[5].second).map { k ->
val var621 = getSequenceElems(history, 464, listOf(7,353,7,449), k.first, k.second)
val var622 = matchReservedItem(var621[3].first, var621[3].second)
var622
}
val var623 = ReservedDef(listOf(var619) + var620, nextId(), beginGen, endGen)
return var623
}

fun matchReservedItem(beginGen: Int, endGen: Int): ReservedItem {
val var624 = history[endGen].findByBeginGenOpt(62, 1, beginGen)
val var625 = history[endGen].findByBeginGenOpt(450, 1, beginGen)
check(hasSingleTrue(var624 != null, var625 != null))
val var626 = when {
var624 != null -> {
val var627 = matchIdent(beginGen, endGen)
var627
}
else -> {
val var628 = matchReservedRange(beginGen, endGen)
var628
}
}
return var626
}

fun matchOneOfMemberDef(beginGen: Int, endGen: Int): OneOfMemberDef {
val var629 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var630 = history[endGen].findByBeginGenOpt(381, 1, beginGen)
check(hasSingleTrue(var629 != null, var630 != null))
val var631 = when {
var629 != null -> {
val var632 = matchOptionDef(beginGen, endGen)
var632
}
else -> {
val var633 = matchFieldDef(beginGen, endGen)
var633
}
}
return var631
}

fun matchReservedRange(beginGen: Int, endGen: Int): ReservedRange {
val var634 = getSequenceElems(history, 451, listOf(185,452), beginGen, endGen)
val var635 = matchIntLiteral(var634[0].first, var634[0].second)
val var636 = history[var634[1].second].findByBeginGenOpt(82, 1, var634[1].first)
val var637 = history[var634[1].second].findByBeginGenOpt(453, 1, var634[1].first)
check(hasSingleTrue(var636 != null, var637 != null))
val var638 = when {
var636 != null -> null
else -> {
val var639 = getSequenceElems(history, 454, listOf(7,455,7,457), var634[1].first, var634[1].second)
val var640 = matchReservedRangeEnd(var639[3].first, var639[3].second)
var640
}
}
val var641 = ReservedRange(var635, var638, nextId(), beginGen, endGen)
return var641
}

fun matchReservedRangeEnd(beginGen: Int, endGen: Int): ReservedRangeEnd {
val var642 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var643 = history[endGen].findByBeginGenOpt(458, 1, beginGen)
check(hasSingleTrue(var642 != null, var643 != null))
val var644 = when {
var642 != null -> {
val var645 = matchIntLiteral(beginGen, endGen)
var645
}
else -> {
val var646 = Max(nextId(), beginGen, endGen)
var646
}
}
return var644
}

}
