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
val var276 = history[endGen].findByBeginGenOpt(413, 1, beginGen)
val var277 = history[endGen].findByBeginGenOpt(439, 1, beginGen)
val var278 = history[endGen].findByBeginGenOpt(538, 1, beginGen)
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
val var298 = getSequenceElems(history, 292, listOf(293,7,62,7,297,7,298,7,519,7,298,521,397), beginGen, endGen)
val var299 = matchIdent(var298[2].first, var298[2].second)
val var300 = matchType(var298[6].first, var298[6].second)
val var301 = matchType(var298[10].first, var298[10].second)
val var302 = history[var298[11].second].findByBeginGenOpt(82, 1, var298[11].first)
val var303 = history[var298[11].second].findByBeginGenOpt(522, 1, var298[11].first)
check(hasSingleTrue(var302 != null, var303 != null))
val var304 = when {
var302 != null -> null
else -> {
val var305 = getSequenceElems(history, 523, listOf(7,524,7,530), var298[11].first, var298[11].second)
val var306 = matchRpcTypeWheres(var305[3].first, var305[3].second)
var306
}
}
val var307 = history[var298[12].second].findByBeginGenOpt(82, 1, var298[12].first)
val var308 = history[var298[12].second].findByBeginGenOpt(398, 1, var298[12].first)
check(hasSingleTrue(var307 != null, var308 != null))
val var309 = when {
var307 != null -> null
else -> {
val var310 = getSequenceElems(history, 399, listOf(7,400), var298[12].first, var298[12].second)
val var311 = matchFieldOptions(var310[1].first, var310[1].second)
var311
}
}
val var312 = RpcDef(var299, var300, var301, var304, var309, nextId(), beginGen, endGen)
return var312
}

fun matchMessageDef(beginGen: Int, endGen: Int): MessageDef {
val var313 = getSequenceElems(history, 440, listOf(441,7,62,7,241,384,7,273), beginGen, endGen)
val var314 = matchIdent(var313[2].first, var313[2].second)
val var316 = history[var313[5].second].findByBeginGenOpt(82, 1, var313[5].first)
val var317 = history[var313[5].second].findByBeginGenOpt(385, 1, var313[5].first)
check(hasSingleTrue(var316 != null, var317 != null))
val var318 = when {
var316 != null -> null
else -> {
val var319 = matchMessageMembersWS(var313[5].first, var313[5].second)
var319
}
}
val var315 = var318
val var320 = MessageDef(var314, (var315 ?: listOf()), nextId(), beginGen, endGen)
return var320
}

fun matchMessageMembersWS(beginGen: Int, endGen: Int): List<MessageMemberDefWS> {
val var321 = getSequenceElems(history, 386, listOf(7,387,482), beginGen, endGen)
val var322 = matchWS(var321[0].first, var321[0].second)
val var323 = matchMessageMemberDef(var321[1].first, var321[1].second)
val var324 = MessageMemberDefWS(var322, var323, nextId(), beginGen, endGen)
val var325 = unrollRepeat0(history, 482, 484, 9, 483, var321[2].first, var321[2].second).map { k ->
val var326 = getSequenceElems(history, 485, listOf(430,387), k.first, k.second)
val var327 = history[var326[0].second].findByBeginGenOpt(431, 1, var326[0].first)
val var328 = history[var326[0].second].findByBeginGenOpt(433, 1, var326[0].first)
check(hasSingleTrue(var327 != null, var328 != null))
val var329 = when {
var327 != null -> {
val var330 = getSequenceElems(history, 432, listOf(7,360,7), var326[0].first, var326[0].second)
val var331 = matchWS(var330[2].first, var330[2].second)
var331
}
else -> {
val var332 = matchWSNL(var326[0].first, var326[0].second)
var332
}
}
val var333 = matchMessageMemberDef(var326[1].first, var326[1].second)
val var334 = MessageMemberDefWS(var329, var333, nextId(), k.first, k.second)
var334
}
return listOf(var324) + var325
}

fun matchWSNL(beginGen: Int, endGen: Int): List<Comment?> {
val var335 = getSequenceElems(history, 434, listOf(435,438,7), beginGen, endGen)
val var336 = history[var335[1].second].findByBeginGenOpt(14, 1, var335[1].first)
val var337 = history[var335[1].second].findByBeginGenOpt(24, 1, var335[1].first)
check(hasSingleTrue(var336 != null, var337 != null))
val var338 = when {
var336 != null -> {
val var339 = matchLineComment(var335[1].first, var335[1].second)
var339
}
else -> null
}
val var340 = matchWS(var335[2].first, var335[2].second)
return listOf(var338) + var340
}

fun matchEnumDef(beginGen: Int, endGen: Int): EnumDef {
val var341 = getSequenceElems(history, 414, listOf(415,7,62,7,241,417,7,273), beginGen, endGen)
val var342 = matchIdent(var341[2].first, var341[2].second)
val var344 = history[var341[5].second].findByBeginGenOpt(82, 1, var341[5].first)
val var345 = history[var341[5].second].findByBeginGenOpt(418, 1, var341[5].first)
check(hasSingleTrue(var344 != null, var345 != null))
val var346 = when {
var344 != null -> null
else -> {
val var347 = matchEnumMembersWS(var341[5].first, var341[5].second)
var347
}
}
val var343 = var346
val var348 = EnumDef(var342, (var343 ?: listOf()), nextId(), beginGen, endGen)
return var348
}

fun matchEnumMembersWS(beginGen: Int, endGen: Int): List<EnumMemberDefWS> {
val var349 = getSequenceElems(history, 419, listOf(7,420,426), beginGen, endGen)
val var350 = matchWS(var349[0].first, var349[0].second)
val var351 = matchEnumMemberDef(var349[1].first, var349[1].second)
val var352 = EnumMemberDefWS(var350, var351, nextId(), beginGen, endGen)
val var353 = unrollRepeat0(history, 426, 428, 9, 427, var349[2].first, var349[2].second).map { k ->
val var354 = getSequenceElems(history, 429, listOf(430,420), k.first, k.second)
val var355 = history[var354[0].second].findByBeginGenOpt(431, 1, var354[0].first)
val var356 = history[var354[0].second].findByBeginGenOpt(433, 1, var354[0].first)
check(hasSingleTrue(var355 != null, var356 != null))
val var357 = when {
var355 != null -> {
val var358 = getSequenceElems(history, 432, listOf(7,360,7), var354[0].first, var354[0].second)
val var359 = matchWS(var358[2].first, var358[2].second)
var359
}
else -> {
val var360 = matchWSNL(var354[0].first, var354[0].second)
var360
}
}
val var361 = matchEnumMemberDef(var354[1].first, var354[1].second)
val var362 = EnumMemberDefWS(var357, var361, nextId(), k.first, k.second)
var362
}
return listOf(var352) + var353
}

fun matchEnumMemberDef(beginGen: Int, endGen: Int): EnumMemberDef {
val var363 = history[endGen].findByBeginGenOpt(161, 1, beginGen)
val var364 = history[endGen].findByBeginGenOpt(421, 1, beginGen)
check(hasSingleTrue(var363 != null, var364 != null))
val var365 = when {
var363 != null -> {
val var366 = matchOptionDef(beginGen, endGen)
var366
}
else -> {
val var367 = matchEnumFieldDef(beginGen, endGen)
var367
}
}
return var365
}

fun matchEnumFieldDef(beginGen: Int, endGen: Int): EnumFieldDef {
val var368 = getSequenceElems(history, 422, listOf(423,193,7,62,397), beginGen, endGen)
val var369 = history[var368[0].second].findByBeginGenOpt(82, 1, var368[0].first)
val var370 = history[var368[0].second].findByBeginGenOpt(424, 1, var368[0].first)
check(hasSingleTrue(var369 != null, var370 != null))
val var371 = when {
var369 != null -> null
else -> {
val var372 = getSequenceElems(history, 425, listOf(192,7), var368[0].first, var368[0].second)
source[var372[0].first]
}
}
val var373 = matchIntLiteral(var368[1].first, var368[1].second)
val var374 = matchIdent(var368[3].first, var368[3].second)
val var375 = history[var368[4].second].findByBeginGenOpt(82, 1, var368[4].first)
val var376 = history[var368[4].second].findByBeginGenOpt(398, 1, var368[4].first)
check(hasSingleTrue(var375 != null, var376 != null))
val var377 = when {
var375 != null -> null
else -> {
val var378 = getSequenceElems(history, 399, listOf(7,400), var368[4].first, var368[4].second)
val var379 = matchFieldOptions(var378[1].first, var378[1].second)
var379
}
}
val var380 = EnumFieldDef(var371 != null, var373, var374, var377, nextId(), beginGen, endGen)
return var380
}

fun matchSealedDef(beginGen: Int, endGen: Int): SealedDef {
val var381 = getSequenceElems(history, 539, listOf(488,7,62,7,241,495,7,273), beginGen, endGen)
val var382 = matchIdent(var381[2].first, var381[2].second)
val var384 = history[var381[5].second].findByBeginGenOpt(82, 1, var381[5].first)
val var385 = history[var381[5].second].findByBeginGenOpt(496, 1, var381[5].first)
check(hasSingleTrue(var384 != null, var385 != null))
val var386 = when {
var384 != null -> null
else -> {
val var387 = matchSealedMembersWS(var381[5].first, var381[5].second)
var387
}
}
val var383 = var386
val var388 = SealedDef(var382, (var383 ?: listOf()), nextId(), beginGen, endGen)
return var388
}

fun matchSealedMembersWS(beginGen: Int, endGen: Int): List<SealedMemberDefWS> {
val var389 = getSequenceElems(history, 497, listOf(7,498,505), beginGen, endGen)
val var390 = matchWS(var389[0].first, var389[0].second)
val var391 = matchSealedMemberDef(var389[1].first, var389[1].second)
val var392 = SealedMemberDefWS(var390, var391, nextId(), beginGen, endGen)
val var393 = unrollRepeat0(history, 505, 507, 9, 506, var389[2].first, var389[2].second).map { k ->
val var394 = getSequenceElems(history, 508, listOf(430,498), k.first, k.second)
val var395 = history[var394[0].second].findByBeginGenOpt(431, 1, var394[0].first)
val var396 = history[var394[0].second].findByBeginGenOpt(433, 1, var394[0].first)
check(hasSingleTrue(var395 != null, var396 != null))
val var397 = when {
var395 != null -> {
val var398 = getSequenceElems(history, 432, listOf(7,360,7), var394[0].first, var394[0].second)
val var399 = matchWS(var398[2].first, var398[2].second)
var399
}
else -> {
val var400 = matchWSNL(var394[0].first, var394[0].second)
var400
}
}
val var401 = matchSealedMemberDef(var394[1].first, var394[1].second)
val var402 = SealedMemberDefWS(var397, var401, nextId(), k.first, k.second)
var402
}
return listOf(var392) + var393
}

fun matchSealedMemberDef(beginGen: Int, endGen: Int): SealedMemberDef {
val var403 = history[endGen].findByBeginGenOpt(388, 1, beginGen)
val var404 = history[endGen].findByBeginGenOpt(499, 1, beginGen)
check(hasSingleTrue(var403 != null, var404 != null))
val var405 = when {
var403 != null -> {
val var406 = matchFieldDef(beginGen, endGen)
var406
}
else -> {
val var407 = matchCommonFieldDef(beginGen, endGen)
var407
}
}
return var405
}

fun matchCommonFieldDef(beginGen: Int, endGen: Int): CommonFieldDef {
val var408 = getSequenceElems(history, 500, listOf(501,7,388), beginGen, endGen)
val var409 = matchFieldDef(var408[2].first, var408[2].second)
val var410 = CommonFieldDef(var409, nextId(), beginGen, endGen)
return var410
}

fun matchFieldDef(beginGen: Int, endGen: Int): FieldDef {
val var411 = getSequenceElems(history, 389, listOf(390,193,7,62,7,297,7,298,397), beginGen, endGen)
val var412 = history[var411[0].second].findByBeginGenOpt(82, 1, var411[0].first)
val var413 = history[var411[0].second].findByBeginGenOpt(391, 1, var411[0].first)
check(hasSingleTrue(var412 != null, var413 != null))
val var414 = when {
var412 != null -> null
else -> {
val var415 = getSequenceElems(history, 392, listOf(393,7), var411[0].first, var411[0].second)
val var416 = matchWS(var415[1].first, var415[1].second)
var416
}
}
val var417 = matchIntLiteral(var411[1].first, var411[1].second)
val var418 = matchIdent(var411[3].first, var411[3].second)
val var419 = matchType(var411[7].first, var411[7].second)
val var420 = history[var411[8].second].findByBeginGenOpt(82, 1, var411[8].first)
val var421 = history[var411[8].second].findByBeginGenOpt(398, 1, var411[8].first)
check(hasSingleTrue(var420 != null, var421 != null))
val var422 = when {
var420 != null -> null
else -> {
val var423 = getSequenceElems(history, 399, listOf(7,400), var411[8].first, var411[8].second)
val var424 = matchFieldOptions(var423[1].first, var423[1].second)
var424
}
}
val var425 = FieldDef(var414 != null, var417, var418, var419, var422, nextId(), beginGen, endGen)
return var425
}

fun matchLetter(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchType(beginGen: Int, endGen: Int): Type {
val var426 = history[endGen].findByBeginGenOpt(299, 1, beginGen)
val var427 = history[endGen].findByBeginGenOpt(338, 7, beginGen)
val var428 = history[endGen].findByBeginGenOpt(345, 7, beginGen)
val var429 = history[endGen].findByBeginGenOpt(350, 7, beginGen)
val var430 = history[endGen].findByBeginGenOpt(355, 11, beginGen)
val var431 = history[endGen].findByBeginGenOpt(361, 14, beginGen)
val var432 = history[endGen].findByBeginGenOpt(371, 7, beginGen)
val var433 = history[endGen].findByBeginGenOpt(376, 1, beginGen)
val var434 = history[endGen].findByBeginGenOpt(486, 1, beginGen)
val var435 = history[endGen].findByBeginGenOpt(509, 1, beginGen)
val var436 = history[endGen].findByBeginGenOpt(516, 1, beginGen)
check(hasSingleTrue(var426 != null, var427 != null, var428 != null, var429 != null, var430 != null, var431 != null, var432 != null, var433 != null, var434 != null, var435 != null, var436 != null))
val var437 = when {
var426 != null -> {
val var438 = matchPrimitiveType(beginGen, endGen)
val var439 = PrimitiveType(var438, nextId(), beginGen, endGen)
var439
}
var427 != null -> {
val var440 = getSequenceElems(history, 338, listOf(339,7,343,7,298,7,344), beginGen, endGen)
val var441 = matchType(var440[4].first, var440[4].second)
val var442 = RepeatedType(var441, nextId(), beginGen, endGen)
var442
}
var428 != null -> {
val var443 = getSequenceElems(history, 345, listOf(346,7,343,7,298,7,344), beginGen, endGen)
val var444 = matchType(var443[4].first, var443[4].second)
val var445 = SetType(var444, nextId(), beginGen, endGen)
var445
}
var429 != null -> {
val var446 = getSequenceElems(history, 350, listOf(351,7,343,7,298,7,344), beginGen, endGen)
val var447 = matchType(var446[4].first, var446[4].second)
val var448 = OptionalType(var447, nextId(), beginGen, endGen)
var448
}
var430 != null -> {
val var449 = getSequenceElems(history, 355, listOf(356,7,343,7,298,7,360,7,298,7,344), beginGen, endGen)
val var450 = matchType(var449[4].first, var449[4].second)
val var451 = matchType(var449[8].first, var449[8].second)
val var452 = MapType(var450, var451, nextId(), beginGen, endGen)
var452
}
var431 != null -> {
val var453 = getSequenceElems(history, 361, listOf(362,7,343,7,298,7,344,7,241,7,366,368,7,273), beginGen, endGen)
val var454 = matchType(var453[4].first, var453[4].second)
val var455 = matchKeyExpr(var453[10].first, var453[10].second)
val var456 = history[var453[11].second].findByBeginGenOpt(82, 1, var453[11].first)
val var457 = history[var453[11].second].findByBeginGenOpt(369, 1, var453[11].first)
check(hasSingleTrue(var456 != null, var457 != null))
val var458 = when {
var456 != null -> null
else -> {
val var459 = getSequenceElems(history, 370, listOf(7,297,7,298), var453[11].first, var453[11].second)
val var460 = matchType(var459[3].first, var459[3].second)
var460
}
}
val var461 = IndexedType(var454, var455, var458, nextId(), beginGen, endGen)
var461
}
var432 != null -> {
val var462 = getSequenceElems(history, 371, listOf(372,7,343,7,298,7,344), beginGen, endGen)
val var463 = matchType(var462[4].first, var462[4].second)
val var464 = StreamType(var463, nextId(), beginGen, endGen)
var464
}
var433 != null -> {
val var465 = matchOnTheFlyMessageType(beginGen, endGen)
var465
}
var434 != null -> {
val var466 = matchOnTheFlySealedMessageType(beginGen, endGen)
var466
}
var435 != null -> {
val var467 = matchOnTheFlyEnumType(beginGen, endGen)
var467
}
else -> {
val var468 = matchTypeName(beginGen, endGen)
var468
}
}
return var437
}

fun matchTypeName(beginGen: Int, endGen: Int): TypeName {
val var469 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var470 = history[endGen].findByBeginGenOpt(186, 2, beginGen)
val var471 = history[endGen].findByBeginGenOpt(517, 1, beginGen)
check(hasSingleTrue(var469 != null, var470 != null, var471 != null))
val var472 = when {
var469 != null -> {
val var473 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var474 = matchIdentName(var473[1].first, var473[1].second)
val var475 = SingleName(var474, nextId(), beginGen, endGen)
var475
}
var470 != null -> {
val var476 = getSequenceElems(history, 186, listOf(62,187), beginGen, endGen)
val var477 = matchIdent(var476[0].first, var476[0].second)
val var478 = unrollRepeat1(history, 187, 79, 79, 188, var476[1].first, var476[1].second).map { k ->
val var479 = getSequenceElems(history, 80, listOf(7,81,7,62), k.first, k.second)
val var480 = matchIdent(var479[3].first, var479[3].second)
var480
}
val var481 = MultiName(listOf(var477) + var478, nextId(), beginGen, endGen)
var481
}
else -> {
val var482 = matchIdentName(beginGen, endGen)
val var483 = SingleName(var482, nextId(), beginGen, endGen)
var483
}
}
return var472
}

fun matchOnTheFlyMessageType(beginGen: Int, endGen: Int): OnTheFlyMessageType {
val var484 = getSequenceElems(history, 377, listOf(378,241,384,7,273), beginGen, endGen)
val var485 = history[var484[0].second].findByBeginGenOpt(82, 1, var484[0].first)
val var486 = history[var484[0].second].findByBeginGenOpt(379, 1, var484[0].first)
check(hasSingleTrue(var485 != null, var486 != null))
val var487 = when {
var485 != null -> null
else -> {
val var488 = getSequenceElems(history, 380, listOf(381,7), var484[0].first, var484[0].second)
val var489 = matchIdentNoSealedEnum(var488[0].first, var488[0].second)
var489
}
}
val var491 = history[var484[2].second].findByBeginGenOpt(82, 1, var484[2].first)
val var492 = history[var484[2].second].findByBeginGenOpt(385, 1, var484[2].first)
check(hasSingleTrue(var491 != null, var492 != null))
val var493 = when {
var491 != null -> null
else -> {
val var494 = matchMessageMembersWS(var484[2].first, var484[2].second)
var494
}
}
val var490 = var493
val var495 = OnTheFlyMessageType(var487, (var490 ?: listOf()), nextId(), beginGen, endGen)
return var495
}

fun matchIdentNoSealedEnum(beginGen: Int, endGen: Int): Ident {
val var496 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var497 = history[endGen].findByBeginGenOpt(382, 1, beginGen)
check(hasSingleTrue(var496 != null, var497 != null))
val var498 = when {
var496 != null -> {
val var499 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var500 = matchIdentName(var499[1].first, var499[1].second)
val var501 = Ident(var500, nextId(), beginGen, endGen)
var501
}
else -> {
val var502 = matchIdentName(beginGen, endGen)
val var503 = Ident(var502, nextId(), beginGen, endGen)
var503
}
}
return var498
}

fun matchOnTheFlyEnumType(beginGen: Int, endGen: Int): OnTheFlyEnumType {
val var504 = getSequenceElems(history, 510, listOf(415,511,7,241,417,7,273), beginGen, endGen)
val var505 = history[var504[1].second].findByBeginGenOpt(82, 1, var504[1].first)
val var506 = history[var504[1].second].findByBeginGenOpt(512, 1, var504[1].first)
check(hasSingleTrue(var505 != null, var506 != null))
val var507 = when {
var505 != null -> null
else -> {
val var508 = getSequenceElems(history, 513, listOf(7,514), var504[1].first, var504[1].second)
val var509 = matchIdentNoEnum(var508[1].first, var508[1].second)
var509
}
}
val var511 = history[var504[4].second].findByBeginGenOpt(82, 1, var504[4].first)
val var512 = history[var504[4].second].findByBeginGenOpt(418, 1, var504[4].first)
check(hasSingleTrue(var511 != null, var512 != null))
val var513 = when {
var511 != null -> null
else -> {
val var514 = matchEnumMembersWS(var504[4].first, var504[4].second)
var514
}
}
val var510 = var513
val var515 = OnTheFlyEnumType(var507, (var510 ?: listOf()), nextId(), beginGen, endGen)
return var515
}

fun matchIdentNoEnum(beginGen: Int, endGen: Int): Ident {
val var516 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var517 = history[endGen].findByBeginGenOpt(515, 1, beginGen)
check(hasSingleTrue(var516 != null, var517 != null))
val var518 = when {
var516 != null -> {
val var519 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var520 = matchIdentName(var519[1].first, var519[1].second)
val var521 = Ident(var520, nextId(), beginGen, endGen)
var521
}
else -> {
val var522 = matchIdentName(beginGen, endGen)
val var523 = Ident(var522, nextId(), beginGen, endGen)
var523
}
}
return var518
}

fun matchKeyExpr(beginGen: Int, endGen: Int): KeyExpr {
val var524 = history[endGen].findByBeginGenOpt(74, 1, beginGen)
val var525 = history[endGen].findByBeginGenOpt(367, 5, beginGen)
check(hasSingleTrue(var524 != null, var525 != null))
val var526 = when {
var524 != null -> {
val var527 = TargetElem(nextId(), beginGen, endGen)
var527
}
else -> {
val var528 = getSequenceElems(history, 367, listOf(366,7,81,7,62), beginGen, endGen)
val var529 = matchKeyExpr(var528[0].first, var528[0].second)
val var530 = matchIdent(var528[4].first, var528[4].second)
val var531 = MemberAccess(var529, var530, nextId(), beginGen, endGen)
var531
}
}
return var526
}

fun matchPrimitiveType(beginGen: Int, endGen: Int): PrimitiveTypeEnum {
val var532 = history[endGen].findByBeginGenOpt(301, 1, beginGen)
val var533 = history[endGen].findByBeginGenOpt(304, 1, beginGen)
val var534 = history[endGen].findByBeginGenOpt(306, 1, beginGen)
val var535 = history[endGen].findByBeginGenOpt(310, 1, beginGen)
val var536 = history[endGen].findByBeginGenOpt(314, 1, beginGen)
val var537 = history[endGen].findByBeginGenOpt(316, 1, beginGen)
val var538 = history[endGen].findByBeginGenOpt(318, 1, beginGen)
val var539 = history[endGen].findByBeginGenOpt(320, 1, beginGen)
val var540 = history[endGen].findByBeginGenOpt(322, 1, beginGen)
val var541 = history[endGen].findByBeginGenOpt(325, 1, beginGen)
val var542 = history[endGen].findByBeginGenOpt(327, 1, beginGen)
val var543 = history[endGen].findByBeginGenOpt(329, 1, beginGen)
val var544 = history[endGen].findByBeginGenOpt(331, 1, beginGen)
val var545 = history[endGen].findByBeginGenOpt(333, 1, beginGen)
val var546 = history[endGen].findByBeginGenOpt(335, 1, beginGen)
check(hasSingleTrue(var532 != null, var533 != null, var534 != null, var535 != null, var536 != null, var537 != null, var538 != null, var539 != null, var540 != null, var541 != null, var542 != null, var543 != null, var544 != null, var545 != null, var546 != null))
val var547 = when {
var532 != null -> PrimitiveTypeEnum.DOUBLE
var533 != null -> PrimitiveTypeEnum.FLOAT
var534 != null -> PrimitiveTypeEnum.INT32
var535 != null -> PrimitiveTypeEnum.INT64
var536 != null -> PrimitiveTypeEnum.UINT32
var537 != null -> PrimitiveTypeEnum.UINT64
var538 != null -> PrimitiveTypeEnum.SINT32
var539 != null -> PrimitiveTypeEnum.SINT64
var540 != null -> PrimitiveTypeEnum.FIXED32
var541 != null -> PrimitiveTypeEnum.FIXED64
var542 != null -> PrimitiveTypeEnum.SFIXED32
var543 != null -> PrimitiveTypeEnum.SFIXED64
var544 != null -> PrimitiveTypeEnum.BOOL
var545 != null -> PrimitiveTypeEnum.STRING
else -> PrimitiveTypeEnum.BYTES
}
return var547
}

fun matchRpcTypeWheres(beginGen: Int, endGen: Int): RpcTypeWheres {
val var548 = getSequenceElems(history, 531, listOf(532,534), beginGen, endGen)
val var549 = matchRpcTypeWhere(var548[0].first, var548[0].second)
val var550 = unrollRepeat0(history, 534, 536, 9, 535, var548[1].first, var548[1].second).map { k ->
val var551 = getSequenceElems(history, 537, listOf(7,360,7,532), k.first, k.second)
val var552 = matchRpcTypeWhere(var551[3].first, var551[3].second)
var552
}
val var553 = RpcTypeWheres(listOf(var549) + var550, nextId(), beginGen, endGen)
return var553
}

fun matchRpcTypeWhere(beginGen: Int, endGen: Int): RpcTypeWhere {
val var554 = getSequenceElems(history, 533, listOf(62,7,175,7,298), beginGen, endGen)
val var555 = matchIdent(var554[0].first, var554[0].second)
val var556 = matchType(var554[4].first, var554[4].second)
val var557 = RpcTypeWhere(var555, var556, nextId(), beginGen, endGen)
return var557
}

fun matchOnTheFlySealedMessageType(beginGen: Int, endGen: Int): OnTheFlySealedMessageType {
val var558 = getSequenceElems(history, 487, listOf(488,490,7,241,495,7,273), beginGen, endGen)
val var559 = history[var558[1].second].findByBeginGenOpt(82, 1, var558[1].first)
val var560 = history[var558[1].second].findByBeginGenOpt(491, 1, var558[1].first)
check(hasSingleTrue(var559 != null, var560 != null))
val var561 = when {
var559 != null -> null
else -> {
val var562 = getSequenceElems(history, 492, listOf(7,493), var558[1].first, var558[1].second)
val var563 = matchIdentNoSealed(var562[1].first, var562[1].second)
var563
}
}
val var565 = history[var558[4].second].findByBeginGenOpt(82, 1, var558[4].first)
val var566 = history[var558[4].second].findByBeginGenOpt(496, 1, var558[4].first)
check(hasSingleTrue(var565 != null, var566 != null))
val var567 = when {
var565 != null -> null
else -> {
val var568 = matchSealedMembersWS(var558[4].first, var558[4].second)
var568
}
}
val var564 = var567
val var569 = OnTheFlySealedMessageType(var561, (var564 ?: listOf()), nextId(), beginGen, endGen)
return var569
}

fun matchIdentNoSealed(beginGen: Int, endGen: Int): Ident {
val var570 = history[endGen].findByBeginGenOpt(75, 3, beginGen)
val var571 = history[endGen].findByBeginGenOpt(494, 1, beginGen)
check(hasSingleTrue(var570 != null, var571 != null))
val var572 = when {
var570 != null -> {
val var573 = getSequenceElems(history, 75, listOf(76,63,76), beginGen, endGen)
val var574 = matchIdentName(var573[1].first, var573[1].second)
val var575 = Ident(var574, nextId(), beginGen, endGen)
var575
}
else -> {
val var576 = matchIdentName(beginGen, endGen)
val var577 = Ident(var576, nextId(), beginGen, endGen)
var577
}
}
return var572
}

fun matchFieldOptions(beginGen: Int, endGen: Int): FieldOptions {
val var579 = getSequenceElems(history, 401, listOf(402,403,7,412), beginGen, endGen)
val var580 = history[var579[1].second].findByBeginGenOpt(82, 1, var579[1].first)
val var581 = history[var579[1].second].findByBeginGenOpt(404, 1, var579[1].first)
check(hasSingleTrue(var580 != null, var581 != null))
val var582 = when {
var580 != null -> null
else -> {
val var583 = getSequenceElems(history, 405, listOf(7,406,408), var579[1].first, var579[1].second)
val var584 = matchFieldOption(var583[1].first, var583[1].second)
val var585 = unrollRepeat0(history, 408, 410, 9, 409, var583[2].first, var583[2].second).map { k ->
val var586 = getSequenceElems(history, 411, listOf(7,360,7,406), k.first, k.second)
val var587 = matchFieldOption(var586[3].first, var586[3].second)
var587
}
listOf(var584) + var585
}
}
val var578 = var582
val var588 = FieldOptions((var578 ?: listOf()), nextId(), beginGen, endGen)
return var588
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var589 = getSequenceElems(history, 407, listOf(168,7,175,7,176), beginGen, endGen)
val var590 = matchOptionName(var589[0].first, var589[0].second)
val var591 = matchConstant(var589[4].first, var589[4].second)
val var592 = FieldOption(var590, var591, nextId(), beginGen, endGen)
return var592
}

fun matchMessageMemberDef(beginGen: Int, endGen: Int): MessageMemberDef {
val var593 = history[endGen].findByBeginGenOpt(161, 1, beginGen)
val var594 = history[endGen].findByBeginGenOpt(388, 1, beginGen)
val var595 = history[endGen].findByBeginGenOpt(413, 1, beginGen)
val var596 = history[endGen].findByBeginGenOpt(439, 1, beginGen)
val var597 = history[endGen].findByBeginGenOpt(443, 1, beginGen)
val var598 = history[endGen].findByBeginGenOpt(457, 1, beginGen)
check(hasSingleTrue(var593 != null, var594 != null, var595 != null, var596 != null, var597 != null, var598 != null))
val var599 = when {
var593 != null -> {
val var600 = matchOptionDef(beginGen, endGen)
var600
}
var594 != null -> {
val var601 = matchFieldDef(beginGen, endGen)
var601
}
var595 != null -> {
val var602 = matchEnumDef(beginGen, endGen)
var602
}
var596 != null -> {
val var603 = matchMessageDef(beginGen, endGen)
var603
}
var597 != null -> {
val var604 = matchOneOfDef(beginGen, endGen)
var604
}
else -> {
val var605 = matchReservedDef(beginGen, endGen)
var605
}
}
return var599
}

fun matchOneOfDef(beginGen: Int, endGen: Int): OneOfDef {
val var606 = getSequenceElems(history, 444, listOf(445,7,62,7,241,449,7,273), beginGen, endGen)
val var607 = matchIdent(var606[2].first, var606[2].second)
val var609 = history[var606[5].second].findByBeginGenOpt(82, 1, var606[5].first)
val var610 = history[var606[5].second].findByBeginGenOpt(450, 1, var606[5].first)
check(hasSingleTrue(var609 != null, var610 != null))
val var611 = when {
var609 != null -> null
else -> {
val var612 = matchOneOfMembersWS(var606[5].first, var606[5].second)
var612
}
}
val var608 = var611
val var613 = OneOfDef(var607, (var608 ?: listOf()), nextId(), beginGen, endGen)
return var613
}

fun matchOneOfMembersWS(beginGen: Int, endGen: Int): List<OneOfMembersDefWS> {
val var614 = getSequenceElems(history, 451, listOf(7,452,453), beginGen, endGen)
val var615 = matchWS(var614[0].first, var614[0].second)
val var616 = matchOneOfMemberDef(var614[1].first, var614[1].second)
val var617 = OneOfMembersDefWS(var615, var616, nextId(), beginGen, endGen)
val var618 = unrollRepeat0(history, 453, 455, 9, 454, var614[2].first, var614[2].second).map { k ->
val var619 = getSequenceElems(history, 456, listOf(430,452), k.first, k.second)
val var620 = history[var619[0].second].findByBeginGenOpt(431, 1, var619[0].first)
val var621 = history[var619[0].second].findByBeginGenOpt(433, 1, var619[0].first)
check(hasSingleTrue(var620 != null, var621 != null))
val var622 = when {
var620 != null -> {
val var623 = getSequenceElems(history, 432, listOf(7,360,7), var619[0].first, var619[0].second)
val var624 = matchWS(var623[2].first, var623[2].second)
var624
}
else -> {
val var625 = matchWSNL(var619[0].first, var619[0].second)
var625
}
}
val var626 = matchOneOfMemberDef(var619[1].first, var619[1].second)
val var627 = OneOfMembersDefWS(var622, var626, nextId(), k.first, k.second)
var627
}
return listOf(var617) + var618
}

fun matchReservedDef(beginGen: Int, endGen: Int): ReservedDef {
val var628 = getSequenceElems(history, 458, listOf(459,7,173,7,463,475,479,7,174), beginGen, endGen)
val var629 = matchReservedItem(var628[4].first, var628[4].second)
val var630 = unrollRepeat0(history, 475, 477, 9, 476, var628[5].first, var628[5].second).map { k ->
val var631 = getSequenceElems(history, 478, listOf(7,360,7,463), k.first, k.second)
val var632 = matchReservedItem(var631[3].first, var631[3].second)
var632
}
val var633 = ReservedDef(listOf(var629) + var630, nextId(), beginGen, endGen)
return var633
}

fun matchReservedItem(beginGen: Int, endGen: Int): ReservedItem {
val var634 = history[endGen].findByBeginGenOpt(62, 1, beginGen)
val var635 = history[endGen].findByBeginGenOpt(464, 1, beginGen)
check(hasSingleTrue(var634 != null, var635 != null))
val var636 = when {
var634 != null -> {
val var637 = matchIdent(beginGen, endGen)
var637
}
else -> {
val var638 = matchReservedRange(beginGen, endGen)
var638
}
}
return var636
}

fun matchOneOfMemberDef(beginGen: Int, endGen: Int): OneOfMemberDef {
val var639 = history[endGen].findByBeginGenOpt(161, 1, beginGen)
val var640 = history[endGen].findByBeginGenOpt(388, 1, beginGen)
check(hasSingleTrue(var639 != null, var640 != null))
val var641 = when {
var639 != null -> {
val var642 = matchOptionDef(beginGen, endGen)
var642
}
else -> {
val var643 = matchFieldDef(beginGen, endGen)
var643
}
}
return var641
}

fun matchReservedRange(beginGen: Int, endGen: Int): ReservedRange {
val var644 = getSequenceElems(history, 465, listOf(193,466), beginGen, endGen)
val var645 = matchIntLiteral(var644[0].first, var644[0].second)
val var646 = history[var644[1].second].findByBeginGenOpt(82, 1, var644[1].first)
val var647 = history[var644[1].second].findByBeginGenOpt(467, 1, var644[1].first)
check(hasSingleTrue(var646 != null, var647 != null))
val var648 = when {
var646 != null -> null
else -> {
val var649 = getSequenceElems(history, 468, listOf(7,469,7,471), var644[1].first, var644[1].second)
val var650 = matchReservedRangeEnd(var649[3].first, var649[3].second)
var650
}
}
val var651 = ReservedRange(var645, var648, nextId(), beginGen, endGen)
return var651
}

fun matchReservedRangeEnd(beginGen: Int, endGen: Int): ReservedRangeEnd {
val var652 = history[endGen].findByBeginGenOpt(193, 1, beginGen)
val var653 = history[endGen].findByBeginGenOpt(472, 1, beginGen)
check(hasSingleTrue(var652 != null, var653 != null))
val var654 = when {
var652 != null -> {
val var655 = matchIntLiteral(beginGen, endGen)
var655
}
else -> {
val var656 = Max(nextId(), beginGen, endGen)
var656
}
}
return var654
}

}
