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
  val keyExpr: KeyExpr,
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

sealed interface KeyExpr: AstNode

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
): KeyExpr, AstNode

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
  val useVal: Boolean,
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
  val expr: KeyExpr,
  val name: Ident,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): KeyExpr, AstNode
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
val var271 = history[endGen].findByBeginGenOpt(406, 1, beginGen)
val var272 = history[endGen].findByBeginGenOpt(432, 1, beginGen)
val var273 = history[endGen].findByBeginGenOpt(531, 1, beginGen)
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
val var293 = getSequenceElems(history, 285, listOf(286,7,62,7,290,7,291,7,512,7,291,514,390), beginGen, endGen)
val var294 = matchIdent(var293[2].first, var293[2].second)
val var295 = matchType(var293[6].first, var293[6].second)
val var296 = matchType(var293[10].first, var293[10].second)
val var297 = history[var293[11].second].findByBeginGenOpt(82, 1, var293[11].first)
val var298 = history[var293[11].second].findByBeginGenOpt(515, 1, var293[11].first)
check(hasSingleTrue(var297 != null, var298 != null))
val var299 = when {
var297 != null -> null
else -> {
val var300 = getSequenceElems(history, 516, listOf(7,517,7,523), var293[11].first, var293[11].second)
val var301 = matchRpcTypeWheres(var300[3].first, var300[3].second)
var301
}
}
val var302 = history[var293[12].second].findByBeginGenOpt(82, 1, var293[12].first)
val var303 = history[var293[12].second].findByBeginGenOpt(391, 1, var293[12].first)
check(hasSingleTrue(var302 != null, var303 != null))
val var304 = when {
var302 != null -> null
else -> {
val var305 = getSequenceElems(history, 392, listOf(7,393), var293[12].first, var293[12].second)
val var306 = matchFieldOptions(var305[1].first, var305[1].second)
var306
}
}
val var307 = RpcDef(var294, var295, var296, var299, var304, nextId(), beginGen, endGen)
return var307
}

fun matchMessageDef(beginGen: Int, endGen: Int): MessageDef {
val var308 = getSequenceElems(history, 433, listOf(434,7,62,7,233,377,7,266), beginGen, endGen)
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
val var316 = getSequenceElems(history, 379, listOf(7,380,475), beginGen, endGen)
val var317 = matchWS(var316[0].first, var316[0].second)
val var318 = matchMessageMemberDef(var316[1].first, var316[1].second)
val var319 = MessageMemberDefWS(var317, var318, nextId(), beginGen, endGen)
val var320 = unrollRepeat0(history, 475, 477, 9, 476, var316[2].first, var316[2].second).map { k ->
val var321 = getSequenceElems(history, 478, listOf(423,380), k.first, k.second)
val var322 = history[var321[0].second].findByBeginGenOpt(424, 1, var321[0].first)
val var323 = history[var321[0].second].findByBeginGenOpt(426, 1, var321[0].first)
check(hasSingleTrue(var322 != null, var323 != null))
val var324 = when {
var322 != null -> {
val var325 = getSequenceElems(history, 425, listOf(7,353,7), var321[0].first, var321[0].second)
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
val var330 = getSequenceElems(history, 427, listOf(428,431,7), beginGen, endGen)
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
val var336 = getSequenceElems(history, 407, listOf(408,7,62,7,233,410,7,266), beginGen, endGen)
val var337 = matchIdent(var336[2].first, var336[2].second)
val var339 = history[var336[5].second].findByBeginGenOpt(82, 1, var336[5].first)
val var340 = history[var336[5].second].findByBeginGenOpt(411, 1, var336[5].first)
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
val var344 = getSequenceElems(history, 412, listOf(7,413,419), beginGen, endGen)
val var345 = matchWS(var344[0].first, var344[0].second)
val var346 = matchEnumMemberDef(var344[1].first, var344[1].second)
val var347 = EnumMemberDefWS(var345, var346, nextId(), beginGen, endGen)
val var348 = unrollRepeat0(history, 419, 421, 9, 420, var344[2].first, var344[2].second).map { k ->
val var349 = getSequenceElems(history, 422, listOf(423,413), k.first, k.second)
val var350 = history[var349[0].second].findByBeginGenOpt(424, 1, var349[0].first)
val var351 = history[var349[0].second].findByBeginGenOpt(426, 1, var349[0].first)
check(hasSingleTrue(var350 != null, var351 != null))
val var352 = when {
var350 != null -> {
val var353 = getSequenceElems(history, 425, listOf(7,353,7), var349[0].first, var349[0].second)
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
val var359 = history[endGen].findByBeginGenOpt(414, 1, beginGen)
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
val var363 = getSequenceElems(history, 415, listOf(416,185,7,62,390), beginGen, endGen)
val var364 = history[var363[0].second].findByBeginGenOpt(82, 1, var363[0].first)
val var365 = history[var363[0].second].findByBeginGenOpt(417, 1, var363[0].first)
check(hasSingleTrue(var364 != null, var365 != null))
val var366 = when {
var364 != null -> null
else -> {
val var367 = getSequenceElems(history, 418, listOf(184,7), var363[0].first, var363[0].second)
source[var367[0].first]
}
}
val var368 = matchIntLiteral(var363[1].first, var363[1].second)
val var369 = matchIdent(var363[3].first, var363[3].second)
val var370 = history[var363[4].second].findByBeginGenOpt(82, 1, var363[4].first)
val var371 = history[var363[4].second].findByBeginGenOpt(391, 1, var363[4].first)
check(hasSingleTrue(var370 != null, var371 != null))
val var372 = when {
var370 != null -> null
else -> {
val var373 = getSequenceElems(history, 392, listOf(7,393), var363[4].first, var363[4].second)
val var374 = matchFieldOptions(var373[1].first, var373[1].second)
var374
}
}
val var375 = EnumFieldDef(var366 != null, var368, var369, var372, nextId(), beginGen, endGen)
return var375
}

fun matchSealedDef(beginGen: Int, endGen: Int): SealedDef {
val var376 = getSequenceElems(history, 532, listOf(481,7,62,7,233,488,7,266), beginGen, endGen)
val var377 = matchIdent(var376[2].first, var376[2].second)
val var379 = history[var376[5].second].findByBeginGenOpt(82, 1, var376[5].first)
val var380 = history[var376[5].second].findByBeginGenOpt(489, 1, var376[5].first)
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
val var384 = getSequenceElems(history, 490, listOf(7,491,498), beginGen, endGen)
val var385 = matchWS(var384[0].first, var384[0].second)
val var386 = matchSealedMemberDef(var384[1].first, var384[1].second)
val var387 = SealedMemberDefWS(var385, var386, nextId(), beginGen, endGen)
val var388 = unrollRepeat0(history, 498, 500, 9, 499, var384[2].first, var384[2].second).map { k ->
val var389 = getSequenceElems(history, 501, listOf(423,491), k.first, k.second)
val var390 = history[var389[0].second].findByBeginGenOpt(424, 1, var389[0].first)
val var391 = history[var389[0].second].findByBeginGenOpt(426, 1, var389[0].first)
check(hasSingleTrue(var390 != null, var391 != null))
val var392 = when {
var390 != null -> {
val var393 = getSequenceElems(history, 425, listOf(7,353,7), var389[0].first, var389[0].second)
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
val var399 = history[endGen].findByBeginGenOpt(492, 1, beginGen)
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
val var403 = getSequenceElems(history, 493, listOf(494,7,381), beginGen, endGen)
val var404 = matchFieldDef(var403[2].first, var403[2].second)
val var405 = CommonFieldDef(var404, nextId(), beginGen, endGen)
return var405
}

fun matchFieldDef(beginGen: Int, endGen: Int): FieldDef {
val var406 = getSequenceElems(history, 382, listOf(383,185,7,62,7,290,7,291,390), beginGen, endGen)
val var407 = history[var406[0].second].findByBeginGenOpt(82, 1, var406[0].first)
val var408 = history[var406[0].second].findByBeginGenOpt(384, 1, var406[0].first)
check(hasSingleTrue(var407 != null, var408 != null))
val var409 = when {
var407 != null -> null
else -> {
val var410 = getSequenceElems(history, 385, listOf(386,7), var406[0].first, var406[0].second)
val var411 = matchWS(var410[1].first, var410[1].second)
var411
}
}
val var412 = matchIntLiteral(var406[1].first, var406[1].second)
val var413 = matchIdent(var406[3].first, var406[3].second)
val var414 = matchType(var406[7].first, var406[7].second)
val var415 = history[var406[8].second].findByBeginGenOpt(82, 1, var406[8].first)
val var416 = history[var406[8].second].findByBeginGenOpt(391, 1, var406[8].first)
check(hasSingleTrue(var415 != null, var416 != null))
val var417 = when {
var415 != null -> null
else -> {
val var418 = getSequenceElems(history, 392, listOf(7,393), var406[8].first, var406[8].second)
val var419 = matchFieldOptions(var418[1].first, var418[1].second)
var419
}
}
val var420 = FieldDef(var409 != null, var412, var413, var414, var417, nextId(), beginGen, endGen)
return var420
}

fun matchLetter(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchType(beginGen: Int, endGen: Int): Type {
val var421 = history[endGen].findByBeginGenOpt(292, 1, beginGen)
val var422 = history[endGen].findByBeginGenOpt(331, 7, beginGen)
val var423 = history[endGen].findByBeginGenOpt(338, 7, beginGen)
val var424 = history[endGen].findByBeginGenOpt(343, 7, beginGen)
val var425 = history[endGen].findByBeginGenOpt(348, 11, beginGen)
val var426 = history[endGen].findByBeginGenOpt(354, 14, beginGen)
val var427 = history[endGen].findByBeginGenOpt(364, 7, beginGen)
val var428 = history[endGen].findByBeginGenOpt(369, 1, beginGen)
val var429 = history[endGen].findByBeginGenOpt(479, 1, beginGen)
val var430 = history[endGen].findByBeginGenOpt(502, 1, beginGen)
val var431 = history[endGen].findByBeginGenOpt(509, 1, beginGen)
check(hasSingleTrue(var421 != null, var422 != null, var423 != null, var424 != null, var425 != null, var426 != null, var427 != null, var428 != null, var429 != null, var430 != null, var431 != null))
val var432 = when {
var421 != null -> {
val var433 = matchPrimitiveType(beginGen, endGen)
val var434 = PrimitiveType(var433, nextId(), beginGen, endGen)
var434
}
var422 != null -> {
val var435 = getSequenceElems(history, 331, listOf(332,7,336,7,291,7,337), beginGen, endGen)
val var436 = matchType(var435[4].first, var435[4].second)
val var437 = RepeatedType(var436, nextId(), beginGen, endGen)
var437
}
var423 != null -> {
val var438 = getSequenceElems(history, 338, listOf(339,7,336,7,291,7,337), beginGen, endGen)
val var439 = matchType(var438[4].first, var438[4].second)
val var440 = SetType(var439, nextId(), beginGen, endGen)
var440
}
var424 != null -> {
val var441 = getSequenceElems(history, 343, listOf(344,7,336,7,291,7,337), beginGen, endGen)
val var442 = matchType(var441[4].first, var441[4].second)
val var443 = OptionalType(var442, nextId(), beginGen, endGen)
var443
}
var425 != null -> {
val var444 = getSequenceElems(history, 348, listOf(349,7,336,7,291,7,353,7,291,7,337), beginGen, endGen)
val var445 = matchType(var444[4].first, var444[4].second)
val var446 = matchType(var444[8].first, var444[8].second)
val var447 = MapType(var445, var446, nextId(), beginGen, endGen)
var447
}
var426 != null -> {
val var448 = getSequenceElems(history, 354, listOf(355,7,336,7,291,7,337,7,233,7,359,361,7,266), beginGen, endGen)
val var449 = matchType(var448[4].first, var448[4].second)
val var450 = matchKeyExpr(var448[10].first, var448[10].second)
val var451 = history[var448[11].second].findByBeginGenOpt(82, 1, var448[11].first)
val var452 = history[var448[11].second].findByBeginGenOpt(362, 1, var448[11].first)
check(hasSingleTrue(var451 != null, var452 != null))
val var453 = when {
var451 != null -> null
else -> {
val var454 = getSequenceElems(history, 363, listOf(7,290,7,291), var448[11].first, var448[11].second)
val var455 = matchType(var454[3].first, var454[3].second)
var455
}
}
val var456 = IndexedType(var449, var450, var453, nextId(), beginGen, endGen)
var456
}
var427 != null -> {
val var457 = getSequenceElems(history, 364, listOf(365,7,336,7,291,7,337), beginGen, endGen)
val var458 = matchType(var457[4].first, var457[4].second)
val var459 = StreamType(var458, nextId(), beginGen, endGen)
var459
}
var428 != null -> {
val var460 = matchOnTheFlyMessageType(beginGen, endGen)
var460
}
var429 != null -> {
val var461 = matchOnTheFlySealedMessageType(beginGen, endGen)
var461
}
var430 != null -> {
val var462 = matchOnTheFlyEnumType(beginGen, endGen)
var462
}
else -> {
val var463 = matchTypeName(beginGen, endGen)
var463
}
}
return var432
}

fun matchTypeName(beginGen: Int, endGen: Int): TypeName {
val var464 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var465 = history[endGen].findByBeginGenOpt(178, 2, beginGen)
val var466 = history[endGen].findByBeginGenOpt(510, 1, beginGen)
check(hasSingleTrue(var464 != null, var465 != null, var466 != null))
val var467 = when {
var464 != null -> {
val var468 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var469 = matchIdentName(var468[1].first, var468[1].second)
val var470 = SingleName(var469, nextId(), beginGen, endGen)
var470
}
var465 != null -> {
val var471 = getSequenceElems(history, 178, listOf(62,179), beginGen, endGen)
val var472 = matchIdent(var471[0].first, var471[0].second)
val var473 = unrollRepeat1(history, 179, 79, 79, 180, var471[1].first, var471[1].second).map { k ->
val var474 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var475 = matchIdent(var474[3].first, var474[3].second)
var475
}
val var476 = MultiName(listOf(var472) + var473, nextId(), beginGen, endGen)
var476
}
else -> {
val var477 = matchIdentName(beginGen, endGen)
val var478 = SingleName(var477, nextId(), beginGen, endGen)
var478
}
}
return var467
}

fun matchOnTheFlyMessageType(beginGen: Int, endGen: Int): OnTheFlyMessageType {
val var479 = getSequenceElems(history, 370, listOf(371,233,377,7,266), beginGen, endGen)
val var480 = history[var479[0].second].findByBeginGenOpt(82, 1, var479[0].first)
val var481 = history[var479[0].second].findByBeginGenOpt(372, 1, var479[0].first)
check(hasSingleTrue(var480 != null, var481 != null))
val var482 = when {
var480 != null -> null
else -> {
val var483 = getSequenceElems(history, 373, listOf(374,7), var479[0].first, var479[0].second)
val var484 = matchIdentNoSealedEnum(var483[0].first, var483[0].second)
var484
}
}
val var486 = history[var479[2].second].findByBeginGenOpt(82, 1, var479[2].first)
val var487 = history[var479[2].second].findByBeginGenOpt(378, 1, var479[2].first)
check(hasSingleTrue(var486 != null, var487 != null))
val var488 = when {
var486 != null -> null
else -> {
val var489 = matchMessageMembersWS(var479[2].first, var479[2].second)
var489
}
}
val var485 = var488
val var490 = OnTheFlyMessageType(var482, (var485 ?: listOf()), nextId(), beginGen, endGen)
return var490
}

fun matchIdentNoSealedEnum(beginGen: Int, endGen: Int): Ident {
val var491 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var492 = history[endGen].findByBeginGenOpt(375, 1, beginGen)
check(hasSingleTrue(var491 != null, var492 != null))
val var493 = when {
var491 != null -> {
val var494 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var495 = matchIdentName(var494[1].first, var494[1].second)
val var496 = Ident(var495, nextId(), beginGen, endGen)
var496
}
else -> {
val var497 = matchIdentName(beginGen, endGen)
val var498 = Ident(var497, nextId(), beginGen, endGen)
var498
}
}
return var493
}

fun matchOnTheFlyEnumType(beginGen: Int, endGen: Int): OnTheFlyEnumType {
val var499 = getSequenceElems(history, 503, listOf(408,504,7,233,410,7,266), beginGen, endGen)
val var500 = history[var499[1].second].findByBeginGenOpt(82, 1, var499[1].first)
val var501 = history[var499[1].second].findByBeginGenOpt(505, 1, var499[1].first)
check(hasSingleTrue(var500 != null, var501 != null))
val var502 = when {
var500 != null -> null
else -> {
val var503 = getSequenceElems(history, 506, listOf(7,507), var499[1].first, var499[1].second)
val var504 = matchIdentNoEnum(var503[1].first, var503[1].second)
var504
}
}
val var506 = history[var499[4].second].findByBeginGenOpt(82, 1, var499[4].first)
val var507 = history[var499[4].second].findByBeginGenOpt(411, 1, var499[4].first)
check(hasSingleTrue(var506 != null, var507 != null))
val var508 = when {
var506 != null -> null
else -> {
val var509 = matchEnumMembersWS(var499[4].first, var499[4].second)
var509
}
}
val var505 = var508
val var510 = OnTheFlyEnumType(var502, (var505 ?: listOf()), nextId(), beginGen, endGen)
return var510
}

fun matchIdentNoEnum(beginGen: Int, endGen: Int): Ident {
val var511 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var512 = history[endGen].findByBeginGenOpt(508, 1, beginGen)
check(hasSingleTrue(var511 != null, var512 != null))
val var513 = when {
var511 != null -> {
val var514 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var515 = matchIdentName(var514[1].first, var514[1].second)
val var516 = Ident(var515, nextId(), beginGen, endGen)
var516
}
else -> {
val var517 = matchIdentName(beginGen, endGen)
val var518 = Ident(var517, nextId(), beginGen, endGen)
var518
}
}
return var513
}

fun matchKeyExpr(beginGen: Int, endGen: Int): KeyExpr {
val var519 = history[endGen].findByBeginGenOpt(74, 1, beginGen)
val var520 = history[endGen].findByBeginGenOpt(360, 5, beginGen)
check(hasSingleTrue(var519 != null, var520 != null))
val var521 = when {
var519 != null -> {
val var522 = TargetElem(nextId(), beginGen, endGen)
var522
}
else -> {
val var523 = getSequenceElems(history, 360, listOf(359,7,81,7,62), beginGen, endGen)
val var524 = matchKeyExpr(var523[0].first, var523[0].second)
val var525 = matchIdent(var523[4].first, var523[4].second)
val var526 = MemberAccess(var524, var525, nextId(), beginGen, endGen)
var526
}
}
return var521
}

fun matchPrimitiveType(beginGen: Int, endGen: Int): PrimitiveTypeEnum {
val var527 = history[endGen].findByBeginGenOpt(294, 1, beginGen)
val var528 = history[endGen].findByBeginGenOpt(297, 1, beginGen)
val var529 = history[endGen].findByBeginGenOpt(299, 1, beginGen)
val var530 = history[endGen].findByBeginGenOpt(303, 1, beginGen)
val var531 = history[endGen].findByBeginGenOpt(307, 1, beginGen)
val var532 = history[endGen].findByBeginGenOpt(309, 1, beginGen)
val var533 = history[endGen].findByBeginGenOpt(311, 1, beginGen)
val var534 = history[endGen].findByBeginGenOpt(313, 1, beginGen)
val var535 = history[endGen].findByBeginGenOpt(315, 1, beginGen)
val var536 = history[endGen].findByBeginGenOpt(318, 1, beginGen)
val var537 = history[endGen].findByBeginGenOpt(320, 1, beginGen)
val var538 = history[endGen].findByBeginGenOpt(322, 1, beginGen)
val var539 = history[endGen].findByBeginGenOpt(324, 1, beginGen)
val var540 = history[endGen].findByBeginGenOpt(326, 1, beginGen)
val var541 = history[endGen].findByBeginGenOpt(328, 1, beginGen)
check(hasSingleTrue(var527 != null, var528 != null, var529 != null, var530 != null, var531 != null, var532 != null, var533 != null, var534 != null, var535 != null, var536 != null, var537 != null, var538 != null, var539 != null, var540 != null, var541 != null))
val var542 = when {
var527 != null -> PrimitiveTypeEnum.DOUBLE
var528 != null -> PrimitiveTypeEnum.FLOAT
var529 != null -> PrimitiveTypeEnum.INT32
var530 != null -> PrimitiveTypeEnum.INT64
var531 != null -> PrimitiveTypeEnum.UINT32
var532 != null -> PrimitiveTypeEnum.UINT64
var533 != null -> PrimitiveTypeEnum.SINT32
var534 != null -> PrimitiveTypeEnum.SINT64
var535 != null -> PrimitiveTypeEnum.FIXED32
var536 != null -> PrimitiveTypeEnum.FIXED64
var537 != null -> PrimitiveTypeEnum.SFIXED32
var538 != null -> PrimitiveTypeEnum.SFIXED64
var539 != null -> PrimitiveTypeEnum.BOOL
var540 != null -> PrimitiveTypeEnum.STRING
else -> PrimitiveTypeEnum.BYTES
}
return var542
}

fun matchRpcTypeWheres(beginGen: Int, endGen: Int): RpcTypeWheres {
val var543 = getSequenceElems(history, 524, listOf(525,527), beginGen, endGen)
val var544 = matchRpcTypeWhere(var543[0].first, var543[0].second)
val var545 = unrollRepeat0(history, 527, 529, 9, 528, var543[1].first, var543[1].second).map { k ->
val var546 = getSequenceElems(history, 530, listOf(7,353,7,525), k.first, k.second)
val var547 = matchRpcTypeWhere(var546[3].first, var546[3].second)
var547
}
val var548 = RpcTypeWheres(listOf(var544) + var545, nextId(), beginGen, endGen)
return var548
}

fun matchRpcTypeWhere(beginGen: Int, endGen: Int): RpcTypeWhere {
val var549 = getSequenceElems(history, 526, listOf(62,7,167,7,291), beginGen, endGen)
val var550 = matchIdent(var549[0].first, var549[0].second)
val var551 = matchType(var549[4].first, var549[4].second)
val var552 = RpcTypeWhere(var550, var551, nextId(), beginGen, endGen)
return var552
}

fun matchOnTheFlySealedMessageType(beginGen: Int, endGen: Int): OnTheFlySealedMessageType {
val var553 = getSequenceElems(history, 480, listOf(481,483,7,233,488,7,266), beginGen, endGen)
val var554 = history[var553[1].second].findByBeginGenOpt(82, 1, var553[1].first)
val var555 = history[var553[1].second].findByBeginGenOpt(484, 1, var553[1].first)
check(hasSingleTrue(var554 != null, var555 != null))
val var556 = when {
var554 != null -> null
else -> {
val var557 = getSequenceElems(history, 485, listOf(7,486), var553[1].first, var553[1].second)
val var558 = matchIdentNoSealed(var557[1].first, var557[1].second)
var558
}
}
val var560 = history[var553[4].second].findByBeginGenOpt(82, 1, var553[4].first)
val var561 = history[var553[4].second].findByBeginGenOpt(489, 1, var553[4].first)
check(hasSingleTrue(var560 != null, var561 != null))
val var562 = when {
var560 != null -> null
else -> {
val var563 = matchSealedMembersWS(var553[4].first, var553[4].second)
var563
}
}
val var559 = var562
val var564 = OnTheFlySealedMessageType(var556, (var559 ?: listOf()), nextId(), beginGen, endGen)
return var564
}

fun matchIdentNoSealed(beginGen: Int, endGen: Int): Ident {
val var565 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var566 = history[endGen].findByBeginGenOpt(487, 1, beginGen)
check(hasSingleTrue(var565 != null, var566 != null))
val var567 = when {
var565 != null -> {
val var568 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var569 = matchIdentName(var568[1].first, var568[1].second)
val var570 = Ident(var569, nextId(), beginGen, endGen)
var570
}
else -> {
val var571 = matchIdentName(beginGen, endGen)
val var572 = Ident(var571, nextId(), beginGen, endGen)
var572
}
}
return var567
}

fun matchFieldOptions(beginGen: Int, endGen: Int): FieldOptions {
val var574 = getSequenceElems(history, 394, listOf(395,396,7,405), beginGen, endGen)
val var575 = history[var574[1].second].findByBeginGenOpt(82, 1, var574[1].first)
val var576 = history[var574[1].second].findByBeginGenOpt(397, 1, var574[1].first)
check(hasSingleTrue(var575 != null, var576 != null))
val var577 = when {
var575 != null -> null
else -> {
val var578 = getSequenceElems(history, 398, listOf(7,399,401), var574[1].first, var574[1].second)
val var579 = matchFieldOption(var578[1].first, var578[1].second)
val var580 = unrollRepeat0(history, 401, 403, 9, 402, var578[2].first, var578[2].second).map { k ->
val var581 = getSequenceElems(history, 404, listOf(7,353,7,399), k.first, k.second)
val var582 = matchFieldOption(var581[3].first, var581[3].second)
var582
}
listOf(var579) + var580
}
}
val var573 = var577
val var583 = FieldOptions((var573 ?: listOf()), nextId(), beginGen, endGen)
return var583
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var584 = getSequenceElems(history, 400, listOf(160,7,167,7,168), beginGen, endGen)
val var585 = matchOptionName(var584[0].first, var584[0].second)
val var586 = matchConstant(var584[4].first, var584[4].second)
val var587 = FieldOption(var585, var586, nextId(), beginGen, endGen)
return var587
}

fun matchMessageMemberDef(beginGen: Int, endGen: Int): MessageMemberDef {
val var588 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var589 = history[endGen].findByBeginGenOpt(381, 1, beginGen)
val var590 = history[endGen].findByBeginGenOpt(406, 1, beginGen)
val var591 = history[endGen].findByBeginGenOpt(432, 1, beginGen)
val var592 = history[endGen].findByBeginGenOpt(436, 1, beginGen)
val var593 = history[endGen].findByBeginGenOpt(450, 1, beginGen)
check(hasSingleTrue(var588 != null, var589 != null, var590 != null, var591 != null, var592 != null, var593 != null))
val var594 = when {
var588 != null -> {
val var595 = matchOptionDef(beginGen, endGen)
var595
}
var589 != null -> {
val var596 = matchFieldDef(beginGen, endGen)
var596
}
var590 != null -> {
val var597 = matchEnumDef(beginGen, endGen)
var597
}
var591 != null -> {
val var598 = matchMessageDef(beginGen, endGen)
var598
}
var592 != null -> {
val var599 = matchOneOfDef(beginGen, endGen)
var599
}
else -> {
val var600 = matchReservedDef(beginGen, endGen)
var600
}
}
return var594
}

fun matchOneOfDef(beginGen: Int, endGen: Int): OneOfDef {
val var601 = getSequenceElems(history, 437, listOf(438,7,62,7,233,442,7,266), beginGen, endGen)
val var602 = matchIdent(var601[2].first, var601[2].second)
val var604 = history[var601[5].second].findByBeginGenOpt(82, 1, var601[5].first)
val var605 = history[var601[5].second].findByBeginGenOpt(443, 1, var601[5].first)
check(hasSingleTrue(var604 != null, var605 != null))
val var606 = when {
var604 != null -> null
else -> {
val var607 = matchOneOfMembersWS(var601[5].first, var601[5].second)
var607
}
}
val var603 = var606
val var608 = OneOfDef(var602, (var603 ?: listOf()), nextId(), beginGen, endGen)
return var608
}

fun matchOneOfMembersWS(beginGen: Int, endGen: Int): List<OneOfMembersDefWS> {
val var609 = getSequenceElems(history, 444, listOf(7,445,446), beginGen, endGen)
val var610 = matchWS(var609[0].first, var609[0].second)
val var611 = matchOneOfMemberDef(var609[1].first, var609[1].second)
val var612 = OneOfMembersDefWS(var610, var611, nextId(), beginGen, endGen)
val var613 = unrollRepeat0(history, 446, 448, 9, 447, var609[2].first, var609[2].second).map { k ->
val var614 = getSequenceElems(history, 449, listOf(423,445), k.first, k.second)
val var615 = history[var614[0].second].findByBeginGenOpt(424, 1, var614[0].first)
val var616 = history[var614[0].second].findByBeginGenOpt(426, 1, var614[0].first)
check(hasSingleTrue(var615 != null, var616 != null))
val var617 = when {
var615 != null -> {
val var618 = getSequenceElems(history, 425, listOf(7,353,7), var614[0].first, var614[0].second)
val var619 = matchWS(var618[2].first, var618[2].second)
var619
}
else -> {
val var620 = matchWSNL(var614[0].first, var614[0].second)
var620
}
}
val var621 = matchOneOfMemberDef(var614[1].first, var614[1].second)
val var622 = OneOfMembersDefWS(var617, var621, nextId(), k.first, k.second)
var622
}
return listOf(var612) + var613
}

fun matchReservedDef(beginGen: Int, endGen: Int): ReservedDef {
val var623 = getSequenceElems(history, 451, listOf(452,7,165,7,456,468,472,7,166), beginGen, endGen)
val var624 = matchReservedItem(var623[4].first, var623[4].second)
val var625 = unrollRepeat0(history, 468, 470, 9, 469, var623[5].first, var623[5].second).map { k ->
val var626 = getSequenceElems(history, 471, listOf(7,353,7,456), k.first, k.second)
val var627 = matchReservedItem(var626[3].first, var626[3].second)
var627
}
val var628 = ReservedDef(listOf(var624) + var625, nextId(), beginGen, endGen)
return var628
}

fun matchReservedItem(beginGen: Int, endGen: Int): ReservedItem {
val var629 = history[endGen].findByBeginGenOpt(62, 1, beginGen)
val var630 = history[endGen].findByBeginGenOpt(457, 1, beginGen)
check(hasSingleTrue(var629 != null, var630 != null))
val var631 = when {
var629 != null -> {
val var632 = matchIdent(beginGen, endGen)
var632
}
else -> {
val var633 = matchReservedRange(beginGen, endGen)
var633
}
}
return var631
}

fun matchOneOfMemberDef(beginGen: Int, endGen: Int): OneOfMemberDef {
val var634 = history[endGen].findByBeginGenOpt(153, 1, beginGen)
val var635 = history[endGen].findByBeginGenOpt(381, 1, beginGen)
check(hasSingleTrue(var634 != null, var635 != null))
val var636 = when {
var634 != null -> {
val var637 = matchOptionDef(beginGen, endGen)
var637
}
else -> {
val var638 = matchFieldDef(beginGen, endGen)
var638
}
}
return var636
}

fun matchReservedRange(beginGen: Int, endGen: Int): ReservedRange {
val var639 = getSequenceElems(history, 458, listOf(185,459), beginGen, endGen)
val var640 = matchIntLiteral(var639[0].first, var639[0].second)
val var641 = history[var639[1].second].findByBeginGenOpt(82, 1, var639[1].first)
val var642 = history[var639[1].second].findByBeginGenOpt(460, 1, var639[1].first)
check(hasSingleTrue(var641 != null, var642 != null))
val var643 = when {
var641 != null -> null
else -> {
val var644 = getSequenceElems(history, 461, listOf(7,462,7,464), var639[1].first, var639[1].second)
val var645 = matchReservedRangeEnd(var644[3].first, var644[3].second)
var645
}
}
val var646 = ReservedRange(var640, var643, nextId(), beginGen, endGen)
return var646
}

fun matchReservedRangeEnd(beginGen: Int, endGen: Int): ReservedRangeEnd {
val var647 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var648 = history[endGen].findByBeginGenOpt(465, 1, beginGen)
check(hasSingleTrue(var647 != null, var648 != null))
val var649 = when {
var647 != null -> {
val var650 = matchIntLiteral(beginGen, endGen)
var650
}
else -> {
val var651 = Max(nextId(), beginGen, endGen)
var651
}
}
return var649
}

}
