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

data class ImportDef(
  val deep: Boolean,
  val target: StringLiteral,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class KotlinOptions(
  val options: List<KotlinOption>,
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
  val imports: List<ImportDef>,
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
  val extends: Ident?,
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
  val extends: Ident?,
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
val var1 = getSequenceElems(history, 3, listOf(4,83,157,232,274,7), beginGen, endGen)
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
val var10 = unrollRepeat0(history, 157, 159, 9, 158, var1[2].first, var1[2].second).map { k ->
val var11 = getSequenceElems(history, 160, listOf(7,161), k.first, k.second)
val var12 = matchOptionDef(var11[1].first, var11[1].second)
var12
}
val var13 = history[var1[3].second].findByBeginGenOpt(82, 1, var1[3].first)
val var14 = history[var1[3].second].findByBeginGenOpt(233, 1, var1[3].first)
check(hasSingleTrue(var13 != null, var14 != null))
val var15 = when {
var13 != null -> null
else -> {
val var16 = getSequenceElems(history, 234, listOf(7,235), var1[3].first, var1[3].second)
val var17 = matchKotlinOptions(var16[1].first, var16[1].second)
var17
}
}
val var18 = unrollRepeat0(history, 274, 276, 9, 275, var1[4].first, var1[4].second).map { k ->
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
val var29 = getSequenceElems(history, 162, listOf(163,7,168,7,175,7,176), beginGen, endGen)
val var30 = matchOptionName(var29[2].first, var29[2].second)
val var31 = matchConstant(var29[6].first, var29[6].second)
val var32 = OptionDef(var30, var31, nextId(), beginGen, endGen)
return var32
}

fun matchConstant(beginGen: Int, endGen: Int): Constant {
val var33 = history[endGen].findByBeginGenOpt(106, 1, beginGen)
val var34 = history[endGen].findByBeginGenOpt(177, 1, beginGen)
val var35 = history[endGen].findByBeginGenOpt(186, 2, beginGen)
val var36 = history[endGen].findByBeginGenOpt(189, 3, beginGen)
val var37 = history[endGen].findByBeginGenOpt(209, 3, beginGen)
val var38 = history[endGen].findByBeginGenOpt(230, 1, beginGen)
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
val var43 = getSequenceElems(history, 186, listOf(62,187), beginGen, endGen)
val var44 = matchIdent(var43[0].first, var43[0].second)
val var45 = unrollRepeat1(history, 187, 79, 79, 188, var43[1].first, var43[1].second).map { k ->
val var46 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var47 = matchIdent(var46[3].first, var46[3].second)
var47
}
val var48 = FullIdent(listOf(var44) + var45, nextId(), beginGen, endGen)
var48
}
var36 != null -> {
val var49 = getSequenceElems(history, 189, listOf(190,7,193), beginGen, endGen)
val var50 = matchIntLiteral(var49[2].first, var49[2].second)
var50
}
var37 != null -> {
val var51 = getSequenceElems(history, 209, listOf(190,7,210), beginGen, endGen)
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
val var54 = history[endGen].findByBeginGenOpt(211, 4, beginGen)
val var55 = history[endGen].findByBeginGenOpt(222, 2, beginGen)
val var56 = history[endGen].findByBeginGenOpt(223, 3, beginGen)
val var57 = history[endGen].findByBeginGenOpt(224, 1, beginGen)
val var58 = history[endGen].findByBeginGenOpt(227, 1, beginGen)
check(hasSingleTrue(var54 != null, var55 != null, var56 != null, var57 != null, var58 != null))
val var59 = when {
var54 != null -> {
val var60 = getSequenceElems(history, 211, listOf(212,81,215,216), beginGen, endGen)
val var61 = matchDecimals(var60[0].first, var60[0].second)
val var62 = history[var60[2].second].findByBeginGenOpt(82, 1, var60[2].first)
val var63 = history[var60[2].second].findByBeginGenOpt(212, 1, var60[2].first)
check(hasSingleTrue(var62 != null, var63 != null))
val var64 = when {
var62 != null -> null
else -> {
val var65 = matchDecimals(var60[2].first, var60[2].second)
var65
}
}
val var66 = history[var60[3].second].findByBeginGenOpt(82, 1, var60[3].first)
val var67 = history[var60[3].second].findByBeginGenOpt(217, 1, var60[3].first)
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
val var71 = getSequenceElems(history, 222, listOf(212,217), beginGen, endGen)
val var72 = matchDecimals(var71[0].first, var71[0].second)
val var73 = matchExponent(var71[1].first, var71[1].second)
val var74 = FloatLiteral(var72, null, var73, nextId(), beginGen, endGen)
var74
}
var56 != null -> {
val var75 = getSequenceElems(history, 223, listOf(81,212,216), beginGen, endGen)
val var76 = matchDecimals(var75[1].first, var75[1].second)
val var77 = history[var75[2].second].findByBeginGenOpt(82, 1, var75[2].first)
val var78 = history[var75[2].second].findByBeginGenOpt(217, 1, var75[2].first)
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
val var84 = history[endGen].findByBeginGenOpt(179, 1, beginGen)
val var85 = history[endGen].findByBeginGenOpt(181, 1, beginGen)
check(hasSingleTrue(var84 != null, var85 != null))
val var86 = when {
var84 != null -> BoolValueEnum.TRUE
else -> BoolValueEnum.FALSE
}
val var87 = BoolLiteral(var86, nextId(), beginGen, endGen)
return var87
}

fun matchStringLiteral(beginGen: Int, endGen: Int): StringLiteral {
val var88 = getSequenceElems(history, 107, listOf(108,153), beginGen, endGen)
val var89 = matchStringLiteralSingle(var88[0].first, var88[0].second)
val var90 = unrollRepeat0(history, 153, 155, 9, 154, var88[1].first, var88[1].second).map { k ->
val var91 = getSequenceElems(history, 156, listOf(7,108), k.first, k.second)
val var92 = matchStringLiteralSingle(var91[1].first, var91[1].second)
var92
}
val var93 = StringLiteral(listOf(var89) + var90, nextId(), beginGen, endGen)
return var93
}

fun matchStringLiteralSingle(beginGen: Int, endGen: Int): StringLiteralSingle {
val var94 = history[endGen].findByBeginGenOpt(109, 3, beginGen)
val var95 = history[endGen].findByBeginGenOpt(151, 3, beginGen)
check(hasSingleTrue(var94 != null, var95 != null))
val var96 = when {
var94 != null -> {
val var97 = getSequenceElems(history, 109, listOf(110,111,110), beginGen, endGen)
val var98 = unrollRepeat0(history, 111, 113, 9, 112, var97[1].first, var97[1].second).map { k ->
val var99 = matchCharValue(k.first, k.second)
var99
}
val var100 = StringLiteralSingle(var98, nextId(), beginGen, endGen)
var100
}
else -> {
val var101 = getSequenceElems(history, 151, listOf(152,111,152), beginGen, endGen)
val var102 = unrollRepeat0(history, 111, 113, 9, 112, var101[1].first, var101[1].second).map { k ->
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
val var105 = getSequenceElems(history, 169, listOf(170,77), beginGen, endGen)
val var106 = history[var105[0].second].findByBeginGenOpt(62, 1, var105[0].first)
val var107 = history[var105[0].second].findByBeginGenOpt(171, 1, var105[0].first)
check(hasSingleTrue(var106 != null, var107 != null))
val var108 = when {
var106 != null -> {
val var109 = matchIdent(var105[0].first, var105[0].second)
val var110 = FullIdent(listOf(var109), nextId(), var105[0].first, var105[0].second)
var110
}
else -> {
val var111 = getSequenceElems(history, 172, listOf(173,7,60,7,174), var105[0].first, var105[0].second)
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
val var117 = getSequenceElems(history, 277, listOf(7,278), beginGen, endGen)
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
val var126 = unrollRepeat1(history, 213, 72, 72, 214, beginGen, endGen).map { k ->
val var127 = matchDecimalDigit(k.first, k.second)
var127
}
return var126.joinToString("") { it.toString() }
}

fun matchDecimalDigit(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchCharValue(beginGen: Int, endGen: Int): CharValue {
val var128 = history[endGen].findByBeginGenOpt(114, 1, beginGen)
val var129 = history[endGen].findByBeginGenOpt(117, 1, beginGen)
val var130 = history[endGen].findByBeginGenOpt(125, 1, beginGen)
val var131 = history[endGen].findByBeginGenOpt(132, 1, beginGen)
val var132 = history[endGen].findByBeginGenOpt(135, 1, beginGen)
val var133 = history[endGen].findByBeginGenOpt(138, 1, beginGen)
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
val var141 = getSequenceElems(history, 133, listOf(119,134), beginGen, endGen)
val var142 = CharEscape(source[var141[1].first], nextId(), beginGen, endGen)
return var142
}

fun matchUnicodeLongEscape(beginGen: Int, endGen: Int): UnicodeLongEscape {
val var143 = getSequenceElems(history, 139, listOf(119,137,140), beginGen, endGen)
val var144 = history[var143[2].second].findByBeginGenOpt(141, 1, var143[2].first)
val var145 = history[var143[2].second].findByBeginGenOpt(146, 1, var143[2].first)
check(hasSingleTrue(var144 != null, var145 != null))
val var146 = when {
var144 != null -> {
val var147 = getSequenceElems(history, 142, listOf(143,122,122,122,122,122), var143[2].first, var143[2].second)
val var148 = matchHexDigit(var147[1].first, var147[1].second)
val var149 = matchHexDigit(var147[2].first, var147[2].second)
val var150 = matchHexDigit(var147[3].first, var147[3].second)
val var151 = matchHexDigit(var147[4].first, var147[4].second)
val var152 = matchHexDigit(var147[5].first, var147[5].second)
val var153 = UnicodeLongEscape("000" + var148.toString() + var149.toString() + var150.toString() + var151.toString() + var152.toString(), nextId(), var143[2].first, var143[2].second)
var153
}
else -> {
val var154 = getSequenceElems(history, 147, listOf(148,122,122,122,122), var143[2].first, var143[2].second)
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
val var161 = getSequenceElems(history, 218, listOf(219,220,212), beginGen, endGen)
val var162 = history[var161[1].second].findByBeginGenOpt(82, 1, var161[1].first)
val var163 = history[var161[1].second].findByBeginGenOpt(221, 1, var161[1].first)
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
val var167 = getSequenceElems(history, 118, listOf(119,120,121), beginGen, endGen)
val var168 = unrollRepeat1(history, 121, 122, 122, 124, var167[2].first, var167[2].second).map { k ->
val var169 = matchHexDigit(k.first, k.second)
var169
}
val var170 = HexEscape(var168.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var170
}

fun matchImportDef(beginGen: Int, endGen: Int): ImportDef {
val var171 = getSequenceElems(history, 88, listOf(89,98,7,106), beginGen, endGen)
val var172 = history[var171[1].second].findByBeginGenOpt(82, 1, var171[1].first)
val var173 = history[var171[1].second].findByBeginGenOpt(99, 1, var171[1].first)
check(hasSingleTrue(var172 != null, var173 != null))
val var174 = when {
var172 != null -> null
else -> {
val var175 = getSequenceElems(history, 100, listOf(7,101), var171[1].first, var171[1].second)
"deep"
}
}
val var176 = matchStringLiteral(var171[3].first, var171[3].second)
val var177 = ImportDef(var174 != null, var176, nextId(), beginGen, endGen)
return var177
}

fun matchKotlinOptions(beginGen: Int, endGen: Int): KotlinOptions {
val var178 = getSequenceElems(history, 236, listOf(237,7,241,242,7,273), beginGen, endGen)
val var179 = unrollRepeat0(history, 242, 244, 9, 243, var178[3].first, var178[3].second).map { k ->
val var180 = getSequenceElems(history, 245, listOf(7,246), k.first, k.second)
val var181 = matchKotlinOption(var180[1].first, var180[1].second)
var181
}
val var182 = KotlinOptions(var179, nextId(), beginGen, endGen)
return var182
}

fun matchKotlinOption(beginGen: Int, endGen: Int): KotlinOption {
val var183 = history[endGen].findByBeginGenOpt(44, 3, beginGen)
val var184 = history[endGen].findByBeginGenOpt(247, 1, beginGen)
check(hasSingleTrue(var183 != null, var184 != null))
val var185 = when {
var183 != null -> {
val var186 = getSequenceElems(history, 44, listOf(45,7,60), beginGen, endGen)
val var187 = matchFullIdent(var186[2].first, var186[2].second)
val var188 = KotlinPackage(var187, nextId(), beginGen, endGen)
var188
}
else -> {
val var189 = matchKotlinFromOtherPackage(beginGen, endGen)
var189
}
}
return var185
}

fun matchKotlinFromOtherPackage(beginGen: Int, endGen: Int): KotlinFromOtherPackage {
val var190 = getSequenceElems(history, 248, listOf(249,7,45,7,60,253,7,241,256,7,273), beginGen, endGen)
val var191 = matchFullIdent(var190[4].first, var190[4].second)
val var192 = history[var190[5].second].findByBeginGenOpt(82, 1, var190[5].first)
val var193 = history[var190[5].second].findByBeginGenOpt(254, 1, var190[5].first)
check(hasSingleTrue(var192 != null, var193 != null))
val var194 = when {
var192 != null -> null
else -> {
val var195 = getSequenceElems(history, 255, listOf(7,173,7,237,7,175,7,60,7,174), var190[5].first, var190[5].second)
val var196 = matchFullIdent(var195[7].first, var195[7].second)
var196
}
}
val var197 = unrollRepeat0(history, 256, 258, 9, 257, var190[8].first, var190[8].second).map { k ->
val var198 = getSequenceElems(history, 259, listOf(7,260), k.first, k.second)
val var199 = matchKotlinUse(var198[1].first, var198[1].second)
var199
}
val var200 = KotlinFromOtherPackage(var191, var194, var197, nextId(), beginGen, endGen)
return var200
}

fun matchKotlinUse(beginGen: Int, endGen: Int): KotlinUse {
val var201 = getSequenceElems(history, 261, listOf(262,7,266,7,60), beginGen, endGen)
val var202 = history[var201[2].second].findByBeginGenOpt(267, 1, var201[2].first)
val var203 = history[var201[2].second].findByBeginGenOpt(269, 1, var201[2].first)
val var204 = history[var201[2].second].findByBeginGenOpt(271, 1, var201[2].first)
check(hasSingleTrue(var202 != null, var203 != null, var204 != null))
val var205 = when {
var202 != null -> TypeKind.MESSAGE
var203 != null -> TypeKind.SEALED
else -> TypeKind.ENUM
}
val var206 = matchFullIdent(var201[4].first, var201[4].second)
val var207 = KotlinUse(var205, var206, nextId(), beginGen, endGen)
return var207
}

fun matchComment(beginGen: Int, endGen: Int): Comment {
val var208 = history[endGen].findByBeginGenOpt(14, 1, beginGen)
val var209 = history[endGen].findByBeginGenOpt(28, 1, beginGen)
check(hasSingleTrue(var208 != null, var209 != null))
val var210 = when {
var208 != null -> {
val var211 = matchLineComment(beginGen, endGen)
var211
}
else -> {
val var212 = matchBlockComment(beginGen, endGen)
var212
}
}
return var210
}

fun matchLineComment(beginGen: Int, endGen: Int): LineComment {
val var213 = getSequenceElems(history, 15, listOf(16,19,25), beginGen, endGen)
val var214 = unrollRepeat0(history, 19, 21, 9, 20, var213[1].first, var213[1].second).map { k ->
source[k.first]
}
val var215 = LineComment(var214.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var215
}

fun matchBlockComment(beginGen: Int, endGen: Int): BlockComment {
val var216 = history[endGen].findByBeginGenOpt(29, 4, beginGen)
val var217 = history[endGen].findByBeginGenOpt(41, 1, beginGen)
check(hasSingleTrue(var216 != null, var217 != null))
val var218 = when {
var216 != null -> {
val var219 = getSequenceElems(history, 29, listOf(30,33,23,39), beginGen, endGen)
val var220 = unrollRepeat0(history, 33, 35, 9, 34, var219[1].first, var219[1].second).map { k ->
val var221 = getSequenceElems(history, 36, listOf(23,37), k.first, k.second)
source[var221[0].first]
}
val var222 = BlockComment(var220.joinToString("") { it.toString() } + source[var219[2].first].toString(), nextId(), beginGen, endGen)
var222
}
else -> {
val var223 = BlockComment("", nextId(), beginGen, endGen)
var223
}
}
return var218
}

fun matchIntLiteral(beginGen: Int, endGen: Int): IntLiteral {
val var224 = history[endGen].findByBeginGenOpt(196, 1, beginGen)
val var225 = history[endGen].findByBeginGenOpt(198, 1, beginGen)
val var226 = history[endGen].findByBeginGenOpt(203, 1, beginGen)
val var227 = history[endGen].findByBeginGenOpt(207, 1, beginGen)
check(hasSingleTrue(var224 != null, var225 != null, var226 != null, var227 != null))
val var228 = when {
var224 != null -> {
val var229 = ZeroIntLiteral(nextId(), beginGen, endGen)
var229
}
var225 != null -> {
val var230 = matchDecimalLiteral(beginGen, endGen)
var230
}
var226 != null -> {
val var231 = matchOctalLiteral(beginGen, endGen)
var231
}
else -> {
val var232 = matchHexLiteral(beginGen, endGen)
var232
}
}
return var228
}

fun matchDecimalLiteral(beginGen: Int, endGen: Int): DecimalLiteral {
val var233 = getSequenceElems(history, 199, listOf(200,201), beginGen, endGen)
val var234 = unrollRepeat0(history, 201, 73, 9, 202, var233[1].first, var233[1].second).map { k ->
source[k.first]
}
val var235 = DecimalLiteral(source[var233[0].first].toString() + var234.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var235
}

fun matchOctalLiteral(beginGen: Int, endGen: Int): OctalLiteral {
val var236 = getSequenceElems(history, 204, listOf(145,205), beginGen, endGen)
val var237 = unrollRepeat1(history, 205, 127, 127, 206, var236[1].first, var236[1].second).map { k ->
source[k.first]
}
val var238 = OctalLiteral(var237.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var238
}

fun matchHexLiteral(beginGen: Int, endGen: Int): HexLiteral {
val var239 = getSequenceElems(history, 208, listOf(145,120,121), beginGen, endGen)
val var240 = unrollRepeat1(history, 121, 122, 122, 124, var239[2].first, var239[2].second).map { k ->
val var241 = matchHexDigit(k.first, k.second)
var241
}
val var242 = HexLiteral(var240.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var242
}

fun matchOctEscape(beginGen: Int, endGen: Int): OctEscape {
val var243 = getSequenceElems(history, 126, listOf(119,127,128), beginGen, endGen)
val var244 = history[var243[2].second].findByBeginGenOpt(82, 1, var243[2].first)
val var245 = history[var243[2].second].findByBeginGenOpt(129, 1, var243[2].first)
check(hasSingleTrue(var244 != null, var245 != null))
val var246 = when {
var244 != null -> null
else -> {
val var247 = getSequenceElems(history, 130, listOf(127,131), var243[2].first, var243[2].second)
val var248 = history[var247[1].second].findByBeginGenOpt(82, 1, var247[1].first)
val var249 = history[var247[1].second].findByBeginGenOpt(127, 1, var247[1].first)
check(hasSingleTrue(var248 != null, var249 != null))
val var250 = when {
var248 != null -> null
else -> source[var247[1].first]
}
var250
}
}
val var251 = OctEscape(source[var243[1].first].toString() + (var246?.let { it.toString() } ?: ""), nextId(), beginGen, endGen)
return var251
}

fun matchUnicodeEscape(beginGen: Int, endGen: Int): UnicodeEscape {
val var252 = getSequenceElems(history, 136, listOf(119,137,122,122,122,122), beginGen, endGen)
val var253 = matchHexDigit(var252[2].first, var252[2].second)
val var254 = matchHexDigit(var252[3].first, var252[3].second)
val var255 = matchHexDigit(var252[4].first, var252[4].second)
val var256 = matchHexDigit(var252[5].first, var252[5].second)
val var257 = UnicodeEscape(var253.toString() + var254.toString() + var255.toString() + var256.toString(), nextId(), beginGen, endGen)
return var257
}

fun matchIdent(beginGen: Int, endGen: Int): Ident {
val var258 = history[endGen].findByBeginGenOpt(63, 1, beginGen)
val var259 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
check(hasSingleTrue(var258 != null, var259 != null))
val var260 = when {
var258 != null -> {
val var261 = matchIdentName(beginGen, endGen)
val var262 = Ident(var261, nextId(), beginGen, endGen)
var262
}
else -> {
val var263 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var264 = matchIdentName(var263[1].first, var263[1].second)
val var265 = Ident(var264, nextId(), beginGen, endGen)
var265
}
}
return var260
}

fun matchIdentName(beginGen: Int, endGen: Int): String {
val var266 = getSequenceElems(history, 66, listOf(67,69), beginGen, endGen)
val var267 = matchLetter(var266[0].first, var266[0].second)
val var268 = unrollRepeat0(history, 69, 71, 9, 70, var266[1].first, var266[1].second).map { k ->
val var269 = history[k.second].findByBeginGenOpt(67, 1, k.first)
val var270 = history[k.second].findByBeginGenOpt(72, 1, k.first)
val var271 = history[k.second].findByBeginGenOpt(74, 1, k.first)
check(hasSingleTrue(var269 != null, var270 != null, var271 != null))
val var272 = when {
var269 != null -> {
val var273 = matchLetter(k.first, k.second)
var273
}
var270 != null -> {
val var274 = matchDecimalDigit(k.first, k.second)
var274
}
else -> source[k.first]
}
var272
}
return var267.toString() + var268.joinToString("") { it.toString() }
}

fun matchTopLevelDef(beginGen: Int, endGen: Int): TopLevelDef {
val var275 = history[endGen].findByBeginGenOpt(279, 1, beginGen)
val var276 = history[endGen].findByBeginGenOpt(407, 1, beginGen)
val var277 = history[endGen].findByBeginGenOpt(433, 1, beginGen)
val var278 = history[endGen].findByBeginGenOpt(539, 1, beginGen)
check(hasSingleTrue(var275 != null, var276 != null, var277 != null, var278 != null))
val var279 = when {
var275 != null -> {
val var280 = matchServiceDef(beginGen, endGen)
var280
}
var276 != null -> {
val var281 = matchEnumDef(beginGen, endGen)
var281
}
var277 != null -> {
val var282 = matchMessageDef(beginGen, endGen)
var282
}
else -> {
val var283 = matchSealedDef(beginGen, endGen)
var283
}
}
return var279
}

fun matchServiceDef(beginGen: Int, endGen: Int): ServiceDef {
val var284 = getSequenceElems(history, 280, listOf(281,7,62,7,241,286,7,273), beginGen, endGen)
val var285 = matchIdent(var284[2].first, var284[2].second)
val var286 = unrollRepeat0(history, 286, 288, 9, 287, var284[5].first, var284[5].second).map { k ->
val var287 = matchServiceMemberWS(k.first, k.second)
var287
}
val var288 = ServiceDef(var285, var286, nextId(), beginGen, endGen)
return var288
}

fun matchServiceMemberWS(beginGen: Int, endGen: Int): ServiceMemberWS {
val var289 = getSequenceElems(history, 289, listOf(7,290), beginGen, endGen)
val var290 = matchWS(var289[0].first, var289[0].second)
val var291 = matchServiceMember(var289[1].first, var289[1].second)
val var292 = ServiceMemberWS(var290, var291, nextId(), beginGen, endGen)
return var292
}

fun matchServiceMember(beginGen: Int, endGen: Int): ServiceMember {
val var293 = history[endGen].findByBeginGenOpt(161, 1, beginGen)
val var294 = history[endGen].findByBeginGenOpt(291, 1, beginGen)
check(hasSingleTrue(var293 != null, var294 != null))
val var295 = when {
var293 != null -> {
val var296 = matchOptionDef(beginGen, endGen)
var296
}
else -> {
val var297 = matchRpcDef(beginGen, endGen)
var297
}
}
return var295
}

fun matchRpcDef(beginGen: Int, endGen: Int): RpcDef {
val var298 = getSequenceElems(history, 292, listOf(293,7,62,7,297,7,298,7,520,7,298,522,391), beginGen, endGen)
val var299 = matchIdent(var298[2].first, var298[2].second)
val var300 = matchType(var298[6].first, var298[6].second)
val var301 = matchType(var298[10].first, var298[10].second)
val var302 = history[var298[11].second].findByBeginGenOpt(82, 1, var298[11].first)
val var303 = history[var298[11].second].findByBeginGenOpt(523, 1, var298[11].first)
check(hasSingleTrue(var302 != null, var303 != null))
val var304 = when {
var302 != null -> null
else -> {
val var305 = getSequenceElems(history, 524, listOf(7,525,7,531), var298[11].first, var298[11].second)
val var306 = matchRpcTypeWheres(var305[3].first, var305[3].second)
var306
}
}
val var307 = history[var298[12].second].findByBeginGenOpt(82, 1, var298[12].first)
val var308 = history[var298[12].second].findByBeginGenOpt(392, 1, var298[12].first)
check(hasSingleTrue(var307 != null, var308 != null))
val var309 = when {
var307 != null -> null
else -> {
val var310 = getSequenceElems(history, 393, listOf(7,394), var298[12].first, var298[12].second)
val var311 = matchFieldOptions(var310[1].first, var310[1].second)
var311
}
}
val var312 = RpcDef(var299, var300, var301, var304, var309, nextId(), beginGen, endGen)
return var312
}

fun matchMessageDef(beginGen: Int, endGen: Int): MessageDef {
val var313 = getSequenceElems(history, 434, listOf(435,7,62,437,7,241,378,7,273), beginGen, endGen)
val var314 = matchIdent(var313[2].first, var313[2].second)
val var315 = history[var313[3].second].findByBeginGenOpt(82, 1, var313[3].first)
val var316 = history[var313[3].second].findByBeginGenOpt(438, 1, var313[3].first)
check(hasSingleTrue(var315 != null, var316 != null))
val var317 = when {
var315 != null -> null
else -> {
val var318 = getSequenceElems(history, 439, listOf(7,173,7,62,7,174), var313[3].first, var313[3].second)
val var319 = matchIdent(var318[3].first, var318[3].second)
var319
}
}
val var321 = history[var313[6].second].findByBeginGenOpt(82, 1, var313[6].first)
val var322 = history[var313[6].second].findByBeginGenOpt(379, 1, var313[6].first)
check(hasSingleTrue(var321 != null, var322 != null))
val var323 = when {
var321 != null -> null
else -> {
val var324 = matchMessageMembersWS(var313[6].first, var313[6].second)
var324
}
}
val var320 = var323
val var325 = MessageDef(var314, var317, (var320 ?: listOf()), nextId(), beginGen, endGen)
return var325
}

fun matchMessageMembersWS(beginGen: Int, endGen: Int): List<MessageMemberDefWS> {
val var326 = getSequenceElems(history, 380, listOf(7,381,479), beginGen, endGen)
val var327 = matchWS(var326[0].first, var326[0].second)
val var328 = matchMessageMemberDef(var326[1].first, var326[1].second)
val var329 = MessageMemberDefWS(var327, var328, nextId(), beginGen, endGen)
val var330 = unrollRepeat0(history, 479, 481, 9, 480, var326[2].first, var326[2].second).map { k ->
val var331 = getSequenceElems(history, 482, listOf(424,381), k.first, k.second)
val var332 = history[var331[0].second].findByBeginGenOpt(425, 1, var331[0].first)
val var333 = history[var331[0].second].findByBeginGenOpt(427, 1, var331[0].first)
check(hasSingleTrue(var332 != null, var333 != null))
val var334 = when {
var332 != null -> {
val var335 = getSequenceElems(history, 426, listOf(7,360,7), var331[0].first, var331[0].second)
val var336 = matchWS(var335[2].first, var335[2].second)
var336
}
else -> {
val var337 = matchWSNL(var331[0].first, var331[0].second)
var337
}
}
val var338 = matchMessageMemberDef(var331[1].first, var331[1].second)
val var339 = MessageMemberDefWS(var334, var338, nextId(), k.first, k.second)
var339
}
return listOf(var329) + var330
}

fun matchWSNL(beginGen: Int, endGen: Int): List<Comment?> {
val var340 = getSequenceElems(history, 428, listOf(429,432,7), beginGen, endGen)
val var341 = history[var340[1].second].findByBeginGenOpt(14, 1, var340[1].first)
val var342 = history[var340[1].second].findByBeginGenOpt(24, 1, var340[1].first)
check(hasSingleTrue(var341 != null, var342 != null))
val var343 = when {
var341 != null -> {
val var344 = matchLineComment(var340[1].first, var340[1].second)
var344
}
else -> null
}
val var345 = matchWS(var340[2].first, var340[2].second)
return listOf(var343) + var345
}

fun matchEnumDef(beginGen: Int, endGen: Int): EnumDef {
val var346 = getSequenceElems(history, 408, listOf(409,7,62,7,241,411,7,273), beginGen, endGen)
val var347 = matchIdent(var346[2].first, var346[2].second)
val var349 = history[var346[5].second].findByBeginGenOpt(82, 1, var346[5].first)
val var350 = history[var346[5].second].findByBeginGenOpt(412, 1, var346[5].first)
check(hasSingleTrue(var349 != null, var350 != null))
val var351 = when {
var349 != null -> null
else -> {
val var352 = matchEnumMembersWS(var346[5].first, var346[5].second)
var352
}
}
val var348 = var351
val var353 = EnumDef(var347, (var348 ?: listOf()), nextId(), beginGen, endGen)
return var353
}

fun matchEnumMembersWS(beginGen: Int, endGen: Int): List<EnumMemberDefWS> {
val var354 = getSequenceElems(history, 413, listOf(7,414,420), beginGen, endGen)
val var355 = matchWS(var354[0].first, var354[0].second)
val var356 = matchEnumMemberDef(var354[1].first, var354[1].second)
val var357 = EnumMemberDefWS(var355, var356, nextId(), beginGen, endGen)
val var358 = unrollRepeat0(history, 420, 422, 9, 421, var354[2].first, var354[2].second).map { k ->
val var359 = getSequenceElems(history, 423, listOf(424,414), k.first, k.second)
val var360 = history[var359[0].second].findByBeginGenOpt(425, 1, var359[0].first)
val var361 = history[var359[0].second].findByBeginGenOpt(427, 1, var359[0].first)
check(hasSingleTrue(var360 != null, var361 != null))
val var362 = when {
var360 != null -> {
val var363 = getSequenceElems(history, 426, listOf(7,360,7), var359[0].first, var359[0].second)
val var364 = matchWS(var363[2].first, var363[2].second)
var364
}
else -> {
val var365 = matchWSNL(var359[0].first, var359[0].second)
var365
}
}
val var366 = matchEnumMemberDef(var359[1].first, var359[1].second)
val var367 = EnumMemberDefWS(var362, var366, nextId(), k.first, k.second)
var367
}
return listOf(var357) + var358
}

fun matchEnumMemberDef(beginGen: Int, endGen: Int): EnumMemberDef {
val var368 = history[endGen].findByBeginGenOpt(161, 1, beginGen)
val var369 = history[endGen].findByBeginGenOpt(415, 1, beginGen)
check(hasSingleTrue(var368 != null, var369 != null))
val var370 = when {
var368 != null -> {
val var371 = matchOptionDef(beginGen, endGen)
var371
}
else -> {
val var372 = matchEnumFieldDef(beginGen, endGen)
var372
}
}
return var370
}

fun matchEnumFieldDef(beginGen: Int, endGen: Int): EnumFieldDef {
val var373 = getSequenceElems(history, 416, listOf(417,193,7,62,391), beginGen, endGen)
val var374 = history[var373[0].second].findByBeginGenOpt(82, 1, var373[0].first)
val var375 = history[var373[0].second].findByBeginGenOpt(418, 1, var373[0].first)
check(hasSingleTrue(var374 != null, var375 != null))
val var376 = when {
var374 != null -> null
else -> {
val var377 = getSequenceElems(history, 419, listOf(192,7), var373[0].first, var373[0].second)
source[var377[0].first]
}
}
val var378 = matchIntLiteral(var373[1].first, var373[1].second)
val var379 = matchIdent(var373[3].first, var373[3].second)
val var380 = history[var373[4].second].findByBeginGenOpt(82, 1, var373[4].first)
val var381 = history[var373[4].second].findByBeginGenOpt(392, 1, var373[4].first)
check(hasSingleTrue(var380 != null, var381 != null))
val var382 = when {
var380 != null -> null
else -> {
val var383 = getSequenceElems(history, 393, listOf(7,394), var373[4].first, var373[4].second)
val var384 = matchFieldOptions(var383[1].first, var383[1].second)
var384
}
}
val var385 = EnumFieldDef(var376 != null, var378, var379, var382, nextId(), beginGen, endGen)
return var385
}

fun matchSealedDef(beginGen: Int, endGen: Int): SealedDef {
val var386 = getSequenceElems(history, 540, listOf(489,7,62,7,241,496,7,273), beginGen, endGen)
val var387 = matchIdent(var386[2].first, var386[2].second)
val var389 = history[var386[5].second].findByBeginGenOpt(82, 1, var386[5].first)
val var390 = history[var386[5].second].findByBeginGenOpt(497, 1, var386[5].first)
check(hasSingleTrue(var389 != null, var390 != null))
val var391 = when {
var389 != null -> null
else -> {
val var392 = matchSealedMembersWS(var386[5].first, var386[5].second)
var392
}
}
val var388 = var391
val var393 = SealedDef(var387, (var388 ?: listOf()), nextId(), beginGen, endGen)
return var393
}

fun matchSealedMembersWS(beginGen: Int, endGen: Int): List<SealedMemberDefWS> {
val var394 = getSequenceElems(history, 498, listOf(7,499,506), beginGen, endGen)
val var395 = matchWS(var394[0].first, var394[0].second)
val var396 = matchSealedMemberDef(var394[1].first, var394[1].second)
val var397 = SealedMemberDefWS(var395, var396, nextId(), beginGen, endGen)
val var398 = unrollRepeat0(history, 506, 508, 9, 507, var394[2].first, var394[2].second).map { k ->
val var399 = getSequenceElems(history, 509, listOf(424,499), k.first, k.second)
val var400 = history[var399[0].second].findByBeginGenOpt(425, 1, var399[0].first)
val var401 = history[var399[0].second].findByBeginGenOpt(427, 1, var399[0].first)
check(hasSingleTrue(var400 != null, var401 != null))
val var402 = when {
var400 != null -> {
val var403 = getSequenceElems(history, 426, listOf(7,360,7), var399[0].first, var399[0].second)
val var404 = matchWS(var403[2].first, var403[2].second)
var404
}
else -> {
val var405 = matchWSNL(var399[0].first, var399[0].second)
var405
}
}
val var406 = matchSealedMemberDef(var399[1].first, var399[1].second)
val var407 = SealedMemberDefWS(var402, var406, nextId(), k.first, k.second)
var407
}
return listOf(var397) + var398
}

fun matchSealedMemberDef(beginGen: Int, endGen: Int): SealedMemberDef {
val var408 = history[endGen].findByBeginGenOpt(382, 1, beginGen)
val var409 = history[endGen].findByBeginGenOpt(500, 1, beginGen)
check(hasSingleTrue(var408 != null, var409 != null))
val var410 = when {
var408 != null -> {
val var411 = matchFieldDef(beginGen, endGen)
var411
}
else -> {
val var412 = matchCommonFieldDef(beginGen, endGen)
var412
}
}
return var410
}

fun matchCommonFieldDef(beginGen: Int, endGen: Int): CommonFieldDef {
val var413 = getSequenceElems(history, 501, listOf(502,7,382), beginGen, endGen)
val var414 = matchFieldDef(var413[2].first, var413[2].second)
val var415 = CommonFieldDef(var414, nextId(), beginGen, endGen)
return var415
}

fun matchFieldDef(beginGen: Int, endGen: Int): FieldDef {
val var416 = getSequenceElems(history, 383, listOf(384,193,7,62,7,297,7,298,391), beginGen, endGen)
val var417 = history[var416[0].second].findByBeginGenOpt(82, 1, var416[0].first)
val var418 = history[var416[0].second].findByBeginGenOpt(385, 1, var416[0].first)
check(hasSingleTrue(var417 != null, var418 != null))
val var419 = when {
var417 != null -> null
else -> {
val var420 = getSequenceElems(history, 386, listOf(387,7), var416[0].first, var416[0].second)
val var421 = matchWS(var420[1].first, var420[1].second)
var421
}
}
val var422 = matchIntLiteral(var416[1].first, var416[1].second)
val var423 = matchIdent(var416[3].first, var416[3].second)
val var424 = matchType(var416[7].first, var416[7].second)
val var425 = history[var416[8].second].findByBeginGenOpt(82, 1, var416[8].first)
val var426 = history[var416[8].second].findByBeginGenOpt(392, 1, var416[8].first)
check(hasSingleTrue(var425 != null, var426 != null))
val var427 = when {
var425 != null -> null
else -> {
val var428 = getSequenceElems(history, 393, listOf(7,394), var416[8].first, var416[8].second)
val var429 = matchFieldOptions(var428[1].first, var428[1].second)
var429
}
}
val var430 = FieldDef(var419 != null, var422, var423, var424, var427, nextId(), beginGen, endGen)
return var430
}

fun matchLetter(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchType(beginGen: Int, endGen: Int): Type {
val var431 = history[endGen].findByBeginGenOpt(299, 1, beginGen)
val var432 = history[endGen].findByBeginGenOpt(338, 7, beginGen)
val var433 = history[endGen].findByBeginGenOpt(345, 7, beginGen)
val var434 = history[endGen].findByBeginGenOpt(350, 7, beginGen)
val var435 = history[endGen].findByBeginGenOpt(355, 11, beginGen)
val var436 = history[endGen].findByBeginGenOpt(361, 14, beginGen)
val var437 = history[endGen].findByBeginGenOpt(371, 7, beginGen)
val var438 = history[endGen].findByBeginGenOpt(376, 1, beginGen)
val var439 = history[endGen].findByBeginGenOpt(487, 1, beginGen)
val var440 = history[endGen].findByBeginGenOpt(510, 1, beginGen)
val var441 = history[endGen].findByBeginGenOpt(517, 1, beginGen)
check(hasSingleTrue(var431 != null, var432 != null, var433 != null, var434 != null, var435 != null, var436 != null, var437 != null, var438 != null, var439 != null, var440 != null, var441 != null))
val var442 = when {
var431 != null -> {
val var443 = matchPrimitiveType(beginGen, endGen)
val var444 = PrimitiveType(var443, nextId(), beginGen, endGen)
var444
}
var432 != null -> {
val var445 = getSequenceElems(history, 338, listOf(339,7,343,7,298,7,344), beginGen, endGen)
val var446 = matchType(var445[4].first, var445[4].second)
val var447 = RepeatedType(var446, nextId(), beginGen, endGen)
var447
}
var433 != null -> {
val var448 = getSequenceElems(history, 345, listOf(346,7,343,7,298,7,344), beginGen, endGen)
val var449 = matchType(var448[4].first, var448[4].second)
val var450 = SetType(var449, nextId(), beginGen, endGen)
var450
}
var434 != null -> {
val var451 = getSequenceElems(history, 350, listOf(351,7,343,7,298,7,344), beginGen, endGen)
val var452 = matchType(var451[4].first, var451[4].second)
val var453 = OptionalType(var452, nextId(), beginGen, endGen)
var453
}
var435 != null -> {
val var454 = getSequenceElems(history, 355, listOf(356,7,343,7,298,7,360,7,298,7,344), beginGen, endGen)
val var455 = matchType(var454[4].first, var454[4].second)
val var456 = matchType(var454[8].first, var454[8].second)
val var457 = MapType(var455, var456, nextId(), beginGen, endGen)
var457
}
var436 != null -> {
val var458 = getSequenceElems(history, 361, listOf(362,7,343,7,298,7,344,7,241,7,366,368,7,273), beginGen, endGen)
val var459 = matchType(var458[4].first, var458[4].second)
val var460 = matchKeyExpr(var458[10].first, var458[10].second)
val var461 = history[var458[11].second].findByBeginGenOpt(82, 1, var458[11].first)
val var462 = history[var458[11].second].findByBeginGenOpt(369, 1, var458[11].first)
check(hasSingleTrue(var461 != null, var462 != null))
val var463 = when {
var461 != null -> null
else -> {
val var464 = getSequenceElems(history, 370, listOf(7,297,7,298), var458[11].first, var458[11].second)
val var465 = matchType(var464[3].first, var464[3].second)
var465
}
}
val var466 = IndexedType(var459, var460, var463, nextId(), beginGen, endGen)
var466
}
var437 != null -> {
val var467 = getSequenceElems(history, 371, listOf(372,7,343,7,298,7,344), beginGen, endGen)
val var468 = matchType(var467[4].first, var467[4].second)
val var469 = StreamType(var468, nextId(), beginGen, endGen)
var469
}
var438 != null -> {
val var470 = matchOnTheFlyMessageType(beginGen, endGen)
var470
}
var439 != null -> {
val var471 = matchOnTheFlySealedMessageType(beginGen, endGen)
var471
}
var440 != null -> {
val var472 = matchOnTheFlyEnumType(beginGen, endGen)
var472
}
else -> {
val var473 = matchTypeName(beginGen, endGen)
var473
}
}
return var442
}

fun matchTypeName(beginGen: Int, endGen: Int): TypeName {
val var474 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var475 = history[endGen].findByBeginGenOpt(186, 2, beginGen)
val var476 = history[endGen].findByBeginGenOpt(518, 1, beginGen)
check(hasSingleTrue(var474 != null, var475 != null, var476 != null))
val var477 = when {
var474 != null -> {
val var478 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var479 = matchIdentName(var478[1].first, var478[1].second)
val var480 = SingleName(var479, nextId(), beginGen, endGen)
var480
}
var475 != null -> {
val var481 = getSequenceElems(history, 186, listOf(62,187), beginGen, endGen)
val var482 = matchIdent(var481[0].first, var481[0].second)
val var483 = unrollRepeat1(history, 187, 79, 79, 188, var481[1].first, var481[1].second).map { k ->
val var484 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var485 = matchIdent(var484[3].first, var484[3].second)
var485
}
val var486 = MultiName(listOf(var482) + var483, nextId(), beginGen, endGen)
var486
}
else -> {
val var487 = matchIdentName(beginGen, endGen)
val var488 = SingleName(var487, nextId(), beginGen, endGen)
var488
}
}
return var477
}

fun matchOnTheFlyMessageType(beginGen: Int, endGen: Int): OnTheFlyMessageType {
val var489 = history[endGen].findByBeginGenOpt(377, 4, beginGen)
val var490 = history[endGen].findByBeginGenOpt(483, 7, beginGen)
check(hasSingleTrue(var489 != null, var490 != null))
val var491 = when {
var489 != null -> {
val var493 = getSequenceElems(history, 377, listOf(241,378,7,273), beginGen, endGen)
val var494 = history[var493[1].second].findByBeginGenOpt(82, 1, var493[1].first)
val var495 = history[var493[1].second].findByBeginGenOpt(379, 1, var493[1].first)
check(hasSingleTrue(var494 != null, var495 != null))
val var496 = when {
var494 != null -> null
else -> {
val var497 = matchMessageMembersWS(var493[1].first, var493[1].second)
var497
}
}
val var492 = var496
val var498 = OnTheFlyMessageType(null, null, (var492 ?: listOf()), nextId(), beginGen, endGen)
var498
}
else -> {
val var499 = getSequenceElems(history, 483, listOf(484,437,7,241,378,7,273), beginGen, endGen)
val var500 = matchIdentNoSealedEnum(var499[0].first, var499[0].second)
val var501 = history[var499[1].second].findByBeginGenOpt(82, 1, var499[1].first)
val var502 = history[var499[1].second].findByBeginGenOpt(438, 1, var499[1].first)
check(hasSingleTrue(var501 != null, var502 != null))
val var503 = when {
var501 != null -> null
else -> {
val var504 = getSequenceElems(history, 439, listOf(7,173,7,62,7,174), var499[1].first, var499[1].second)
val var505 = matchIdent(var504[3].first, var504[3].second)
var505
}
}
val var507 = history[var499[4].second].findByBeginGenOpt(82, 1, var499[4].first)
val var508 = history[var499[4].second].findByBeginGenOpt(379, 1, var499[4].first)
check(hasSingleTrue(var507 != null, var508 != null))
val var509 = when {
var507 != null -> null
else -> {
val var510 = matchMessageMembersWS(var499[4].first, var499[4].second)
var510
}
}
val var506 = var509
val var511 = OnTheFlyMessageType(var500, var503, (var506 ?: listOf()), nextId(), beginGen, endGen)
var511
}
}
return var491
}

fun matchIdentNoSealedEnum(beginGen: Int, endGen: Int): Ident {
val var512 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var513 = history[endGen].findByBeginGenOpt(485, 1, beginGen)
check(hasSingleTrue(var512 != null, var513 != null))
val var514 = when {
var512 != null -> {
val var515 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var516 = matchIdentName(var515[1].first, var515[1].second)
val var517 = Ident(var516, nextId(), beginGen, endGen)
var517
}
else -> {
val var518 = matchIdentName(beginGen, endGen)
val var519 = Ident(var518, nextId(), beginGen, endGen)
var519
}
}
return var514
}

fun matchOnTheFlyEnumType(beginGen: Int, endGen: Int): OnTheFlyEnumType {
val var520 = getSequenceElems(history, 511, listOf(409,512,7,241,411,7,273), beginGen, endGen)
val var521 = history[var520[1].second].findByBeginGenOpt(82, 1, var520[1].first)
val var522 = history[var520[1].second].findByBeginGenOpt(513, 1, var520[1].first)
check(hasSingleTrue(var521 != null, var522 != null))
val var523 = when {
var521 != null -> null
else -> {
val var524 = getSequenceElems(history, 514, listOf(7,515), var520[1].first, var520[1].second)
val var525 = matchIdentNoEnum(var524[1].first, var524[1].second)
var525
}
}
val var527 = history[var520[4].second].findByBeginGenOpt(82, 1, var520[4].first)
val var528 = history[var520[4].second].findByBeginGenOpt(412, 1, var520[4].first)
check(hasSingleTrue(var527 != null, var528 != null))
val var529 = when {
var527 != null -> null
else -> {
val var530 = matchEnumMembersWS(var520[4].first, var520[4].second)
var530
}
}
val var526 = var529
val var531 = OnTheFlyEnumType(var523, (var526 ?: listOf()), nextId(), beginGen, endGen)
return var531
}

fun matchIdentNoEnum(beginGen: Int, endGen: Int): Ident {
val var532 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var533 = history[endGen].findByBeginGenOpt(516, 1, beginGen)
check(hasSingleTrue(var532 != null, var533 != null))
val var534 = when {
var532 != null -> {
val var535 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var536 = matchIdentName(var535[1].first, var535[1].second)
val var537 = Ident(var536, nextId(), beginGen, endGen)
var537
}
else -> {
val var538 = matchIdentName(beginGen, endGen)
val var539 = Ident(var538, nextId(), beginGen, endGen)
var539
}
}
return var534
}

fun matchKeyExpr(beginGen: Int, endGen: Int): KeyExpr {
val var540 = history[endGen].findByBeginGenOpt(74, 1, beginGen)
val var541 = history[endGen].findByBeginGenOpt(367, 5, beginGen)
check(hasSingleTrue(var540 != null, var541 != null))
val var542 = when {
var540 != null -> {
val var543 = TargetElem(nextId(), beginGen, endGen)
var543
}
else -> {
val var544 = getSequenceElems(history, 367, listOf(366,7,81,7,62), beginGen, endGen)
val var545 = matchKeyExpr(var544[0].first, var544[0].second)
val var546 = matchIdent(var544[4].first, var544[4].second)
val var547 = MemberAccess(var545, var546, nextId(), beginGen, endGen)
var547
}
}
return var542
}

fun matchPrimitiveType(beginGen: Int, endGen: Int): PrimitiveTypeEnum {
val var548 = history[endGen].findByBeginGenOpt(301, 1, beginGen)
val var549 = history[endGen].findByBeginGenOpt(304, 1, beginGen)
val var550 = history[endGen].findByBeginGenOpt(306, 1, beginGen)
val var551 = history[endGen].findByBeginGenOpt(310, 1, beginGen)
val var552 = history[endGen].findByBeginGenOpt(314, 1, beginGen)
val var553 = history[endGen].findByBeginGenOpt(316, 1, beginGen)
val var554 = history[endGen].findByBeginGenOpt(318, 1, beginGen)
val var555 = history[endGen].findByBeginGenOpt(320, 1, beginGen)
val var556 = history[endGen].findByBeginGenOpt(322, 1, beginGen)
val var557 = history[endGen].findByBeginGenOpt(325, 1, beginGen)
val var558 = history[endGen].findByBeginGenOpt(327, 1, beginGen)
val var559 = history[endGen].findByBeginGenOpt(329, 1, beginGen)
val var560 = history[endGen].findByBeginGenOpt(331, 1, beginGen)
val var561 = history[endGen].findByBeginGenOpt(333, 1, beginGen)
val var562 = history[endGen].findByBeginGenOpt(335, 1, beginGen)
check(hasSingleTrue(var548 != null, var549 != null, var550 != null, var551 != null, var552 != null, var553 != null, var554 != null, var555 != null, var556 != null, var557 != null, var558 != null, var559 != null, var560 != null, var561 != null, var562 != null))
val var563 = when {
var548 != null -> PrimitiveTypeEnum.DOUBLE
var549 != null -> PrimitiveTypeEnum.FLOAT
var550 != null -> PrimitiveTypeEnum.INT32
var551 != null -> PrimitiveTypeEnum.INT64
var552 != null -> PrimitiveTypeEnum.UINT32
var553 != null -> PrimitiveTypeEnum.UINT64
var554 != null -> PrimitiveTypeEnum.SINT32
var555 != null -> PrimitiveTypeEnum.SINT64
var556 != null -> PrimitiveTypeEnum.FIXED32
var557 != null -> PrimitiveTypeEnum.FIXED64
var558 != null -> PrimitiveTypeEnum.SFIXED32
var559 != null -> PrimitiveTypeEnum.SFIXED64
var560 != null -> PrimitiveTypeEnum.BOOL
var561 != null -> PrimitiveTypeEnum.STRING
else -> PrimitiveTypeEnum.BYTES
}
return var563
}

fun matchRpcTypeWheres(beginGen: Int, endGen: Int): RpcTypeWheres {
val var564 = getSequenceElems(history, 532, listOf(533,535), beginGen, endGen)
val var565 = matchRpcTypeWhere(var564[0].first, var564[0].second)
val var566 = unrollRepeat0(history, 535, 537, 9, 536, var564[1].first, var564[1].second).map { k ->
val var567 = getSequenceElems(history, 538, listOf(7,360,7,533), k.first, k.second)
val var568 = matchRpcTypeWhere(var567[3].first, var567[3].second)
var568
}
val var569 = RpcTypeWheres(listOf(var565) + var566, nextId(), beginGen, endGen)
return var569
}

fun matchRpcTypeWhere(beginGen: Int, endGen: Int): RpcTypeWhere {
val var570 = getSequenceElems(history, 534, listOf(62,7,175,7,298), beginGen, endGen)
val var571 = matchIdent(var570[0].first, var570[0].second)
val var572 = matchType(var570[4].first, var570[4].second)
val var573 = RpcTypeWhere(var571, var572, nextId(), beginGen, endGen)
return var573
}

fun matchOnTheFlySealedMessageType(beginGen: Int, endGen: Int): OnTheFlySealedMessageType {
val var574 = getSequenceElems(history, 488, listOf(489,491,7,241,496,7,273), beginGen, endGen)
val var575 = history[var574[1].second].findByBeginGenOpt(82, 1, var574[1].first)
val var576 = history[var574[1].second].findByBeginGenOpt(492, 1, var574[1].first)
check(hasSingleTrue(var575 != null, var576 != null))
val var577 = when {
var575 != null -> null
else -> {
val var578 = getSequenceElems(history, 493, listOf(7,494), var574[1].first, var574[1].second)
val var579 = matchIdentNoSealed(var578[1].first, var578[1].second)
var579
}
}
val var581 = history[var574[4].second].findByBeginGenOpt(82, 1, var574[4].first)
val var582 = history[var574[4].second].findByBeginGenOpt(497, 1, var574[4].first)
check(hasSingleTrue(var581 != null, var582 != null))
val var583 = when {
var581 != null -> null
else -> {
val var584 = matchSealedMembersWS(var574[4].first, var574[4].second)
var584
}
}
val var580 = var583
val var585 = OnTheFlySealedMessageType(var577, (var580 ?: listOf()), nextId(), beginGen, endGen)
return var585
}

fun matchIdentNoSealed(beginGen: Int, endGen: Int): Ident {
val var586 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var587 = history[endGen].findByBeginGenOpt(495, 1, beginGen)
check(hasSingleTrue(var586 != null, var587 != null))
val var588 = when {
var586 != null -> {
val var589 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var590 = matchIdentName(var589[1].first, var589[1].second)
val var591 = Ident(var590, nextId(), beginGen, endGen)
var591
}
else -> {
val var592 = matchIdentName(beginGen, endGen)
val var593 = Ident(var592, nextId(), beginGen, endGen)
var593
}
}
return var588
}

fun matchFieldOptions(beginGen: Int, endGen: Int): FieldOptions {
val var595 = getSequenceElems(history, 395, listOf(396,397,7,406), beginGen, endGen)
val var596 = history[var595[1].second].findByBeginGenOpt(82, 1, var595[1].first)
val var597 = history[var595[1].second].findByBeginGenOpt(398, 1, var595[1].first)
check(hasSingleTrue(var596 != null, var597 != null))
val var598 = when {
var596 != null -> null
else -> {
val var599 = getSequenceElems(history, 399, listOf(7,400,402), var595[1].first, var595[1].second)
val var600 = matchFieldOption(var599[1].first, var599[1].second)
val var601 = unrollRepeat0(history, 402, 404, 9, 403, var599[2].first, var599[2].second).map { k ->
val var602 = getSequenceElems(history, 405, listOf(7,360,7,400), k.first, k.second)
val var603 = matchFieldOption(var602[3].first, var602[3].second)
var603
}
listOf(var600) + var601
}
}
val var594 = var598
val var604 = FieldOptions((var594 ?: listOf()), nextId(), beginGen, endGen)
return var604
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var605 = getSequenceElems(history, 401, listOf(168,7,175,7,176), beginGen, endGen)
val var606 = matchOptionName(var605[0].first, var605[0].second)
val var607 = matchConstant(var605[4].first, var605[4].second)
val var608 = FieldOption(var606, var607, nextId(), beginGen, endGen)
return var608
}

fun matchMessageMemberDef(beginGen: Int, endGen: Int): MessageMemberDef {
val var609 = history[endGen].findByBeginGenOpt(161, 1, beginGen)
val var610 = history[endGen].findByBeginGenOpt(382, 1, beginGen)
val var611 = history[endGen].findByBeginGenOpt(407, 1, beginGen)
val var612 = history[endGen].findByBeginGenOpt(433, 1, beginGen)
val var613 = history[endGen].findByBeginGenOpt(440, 1, beginGen)
val var614 = history[endGen].findByBeginGenOpt(454, 1, beginGen)
check(hasSingleTrue(var609 != null, var610 != null, var611 != null, var612 != null, var613 != null, var614 != null))
val var615 = when {
var609 != null -> {
val var616 = matchOptionDef(beginGen, endGen)
var616
}
var610 != null -> {
val var617 = matchFieldDef(beginGen, endGen)
var617
}
var611 != null -> {
val var618 = matchEnumDef(beginGen, endGen)
var618
}
var612 != null -> {
val var619 = matchMessageDef(beginGen, endGen)
var619
}
var613 != null -> {
val var620 = matchOneOfDef(beginGen, endGen)
var620
}
else -> {
val var621 = matchReservedDef(beginGen, endGen)
var621
}
}
return var615
}

fun matchOneOfDef(beginGen: Int, endGen: Int): OneOfDef {
val var622 = getSequenceElems(history, 441, listOf(442,7,62,7,241,446,7,273), beginGen, endGen)
val var623 = matchIdent(var622[2].first, var622[2].second)
val var625 = history[var622[5].second].findByBeginGenOpt(82, 1, var622[5].first)
val var626 = history[var622[5].second].findByBeginGenOpt(447, 1, var622[5].first)
check(hasSingleTrue(var625 != null, var626 != null))
val var627 = when {
var625 != null -> null
else -> {
val var628 = matchOneOfMembersWS(var622[5].first, var622[5].second)
var628
}
}
val var624 = var627
val var629 = OneOfDef(var623, (var624 ?: listOf()), nextId(), beginGen, endGen)
return var629
}

fun matchOneOfMembersWS(beginGen: Int, endGen: Int): List<OneOfMembersDefWS> {
val var630 = getSequenceElems(history, 448, listOf(7,449,450), beginGen, endGen)
val var631 = matchWS(var630[0].first, var630[0].second)
val var632 = matchOneOfMemberDef(var630[1].first, var630[1].second)
val var633 = OneOfMembersDefWS(var631, var632, nextId(), beginGen, endGen)
val var634 = unrollRepeat0(history, 450, 452, 9, 451, var630[2].first, var630[2].second).map { k ->
val var635 = getSequenceElems(history, 453, listOf(424,449), k.first, k.second)
val var636 = history[var635[0].second].findByBeginGenOpt(425, 1, var635[0].first)
val var637 = history[var635[0].second].findByBeginGenOpt(427, 1, var635[0].first)
check(hasSingleTrue(var636 != null, var637 != null))
val var638 = when {
var636 != null -> {
val var639 = getSequenceElems(history, 426, listOf(7,360,7), var635[0].first, var635[0].second)
val var640 = matchWS(var639[2].first, var639[2].second)
var640
}
else -> {
val var641 = matchWSNL(var635[0].first, var635[0].second)
var641
}
}
val var642 = matchOneOfMemberDef(var635[1].first, var635[1].second)
val var643 = OneOfMembersDefWS(var638, var642, nextId(), k.first, k.second)
var643
}
return listOf(var633) + var634
}

fun matchReservedDef(beginGen: Int, endGen: Int): ReservedDef {
val var644 = getSequenceElems(history, 455, listOf(456,7,173,7,460,472,476,7,174), beginGen, endGen)
val var645 = matchReservedItem(var644[4].first, var644[4].second)
val var646 = unrollRepeat0(history, 472, 474, 9, 473, var644[5].first, var644[5].second).map { k ->
val var647 = getSequenceElems(history, 475, listOf(7,360,7,460), k.first, k.second)
val var648 = matchReservedItem(var647[3].first, var647[3].second)
var648
}
val var649 = ReservedDef(listOf(var645) + var646, nextId(), beginGen, endGen)
return var649
}

fun matchReservedItem(beginGen: Int, endGen: Int): ReservedItem {
val var650 = history[endGen].findByBeginGenOpt(62, 1, beginGen)
val var651 = history[endGen].findByBeginGenOpt(461, 1, beginGen)
check(hasSingleTrue(var650 != null, var651 != null))
val var652 = when {
var650 != null -> {
val var653 = matchIdent(beginGen, endGen)
var653
}
else -> {
val var654 = matchReservedRange(beginGen, endGen)
var654
}
}
return var652
}

fun matchOneOfMemberDef(beginGen: Int, endGen: Int): OneOfMemberDef {
val var655 = history[endGen].findByBeginGenOpt(161, 1, beginGen)
val var656 = history[endGen].findByBeginGenOpt(382, 1, beginGen)
check(hasSingleTrue(var655 != null, var656 != null))
val var657 = when {
var655 != null -> {
val var658 = matchOptionDef(beginGen, endGen)
var658
}
else -> {
val var659 = matchFieldDef(beginGen, endGen)
var659
}
}
return var657
}

fun matchReservedRange(beginGen: Int, endGen: Int): ReservedRange {
val var660 = getSequenceElems(history, 462, listOf(193,463), beginGen, endGen)
val var661 = matchIntLiteral(var660[0].first, var660[0].second)
val var662 = history[var660[1].second].findByBeginGenOpt(82, 1, var660[1].first)
val var663 = history[var660[1].second].findByBeginGenOpt(464, 1, var660[1].first)
check(hasSingleTrue(var662 != null, var663 != null))
val var664 = when {
var662 != null -> null
else -> {
val var665 = getSequenceElems(history, 465, listOf(7,466,7,468), var660[1].first, var660[1].second)
val var666 = matchReservedRangeEnd(var665[3].first, var665[3].second)
var666
}
}
val var667 = ReservedRange(var661, var664, nextId(), beginGen, endGen)
return var667
}

fun matchReservedRangeEnd(beginGen: Int, endGen: Int): ReservedRangeEnd {
val var668 = history[endGen].findByBeginGenOpt(193, 1, beginGen)
val var669 = history[endGen].findByBeginGenOpt(469, 1, beginGen)
check(hasSingleTrue(var668 != null, var669 != null))
val var670 = when {
var668 != null -> {
val var671 = matchIntLiteral(beginGen, endGen)
var671
}
else -> {
val var672 = Max(nextId(), beginGen, endGen)
var672
}
}
return var670
}

}
