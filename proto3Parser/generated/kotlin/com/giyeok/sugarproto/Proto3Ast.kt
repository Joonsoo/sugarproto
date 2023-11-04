package com.giyeok.sugarproto

import com.giyeok.jparser.ktlib.*

class Proto3Ast(
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

data class MapField(
  val keyType: MapKeyType,
  val valueType: Type,
  val mapName: Ident,
  val number: IntLit,
  val options: List<FieldOption>?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, AstNode

data class OneofField(
  val typ: Type,
  val name: Ident,
  val number: IntLit,
  val options: List<FieldOption>?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): OneOfElem, AstNode

data class OneofDef(
  val name: Ident,
  val elems: List<OneOfElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, AstNode

sealed interface OneOfElem: AstNode

data class OctalEscape(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

data class Ranges(
  val values: List<Range>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ReservedBody, AstNode

data class EnumDef(
  val name: Ident,
  val body: List<EnumBodyElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, TopLevelDef, AstNode

data class RangeEndValue(
  val value: IntLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): RangeEnd, AstNode

data class SingleQuoteStrLit(
  val value: List<CharValue>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StrLit, AstNode

data class FloatConstant(
  val sign: Sign?,
  val value: FloatLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode

data class EnumType(
  val firstDot: Boolean,
  val parent: List<Char>,
  val name: Ident,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class StringConstant(
  val value: StrLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode

data class Proto3(
  val defs: List<ProtoDefElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class NaN(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): FloatLit, AstNode

data class FullIdent(
  val names: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, OptionScope, AstNode

data class Inf(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): FloatLit, AstNode

sealed interface IntLit: AstNode

data class RangeEndMax(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): RangeEnd, AstNode

data class EnumFieldDef(
  val name: Ident,
  val minus: Boolean,
  val value: IntLit,
  val options: List<EnumValueOption>?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): EnumBodyElem, AstNode

sealed interface Constant: AstNode

data class FieldOption(
  val name: OptionName,
  val value: Constant,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class FloatLiteral(
  val integral: String,
  val fractional: String,
  val exponent: Exponent?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): FloatLit, AstNode

data class OptionDef(
  val name: OptionName,
  val value: Constant,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): EnumBodyElem, MessageBodyElem, OneOfElem, ProtoDefElem, ServiceBodyElem, AstNode

data class HexLit(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLit, AstNode

sealed interface ProtoDefElem: AstNode

sealed interface ServiceBodyElem: AstNode

data class CharEscape(
  val value: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

sealed interface EnumBodyElem: AstNode

data class Rpc(
  val name: Ident,
  val isInputStream: Boolean,
  val inputType: MessageType,
  val isOutputStream: Boolean,
  val outputType: MessageType,
  val options: List<OptionDef?>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ServiceBodyElem, AstNode

data class FieldNames(
  val names: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ReservedBody, AstNode

data class OptionName(
  val scope: OptionScope,
  val name: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class MessageType(
  val firstDot: Boolean,
  val parent: List<Char>,
  val name: Ident,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class EmptyStatement(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): EnumBodyElem, MessageBodyElem, OneOfElem, ProtoDefElem, ServiceBodyElem, AstNode

data class Service(
  val name: Ident,
  val body: List<ServiceBodyElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): TopLevelDef, AstNode

data class Character(
  val value: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode

data class IntConstant(
  val sign: Sign?,
  val value: IntLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode

data class MessageOrEnumType(
  val name: MessageType,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

data class DecimalLit(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLit, AstNode

data class Field(
  val modifier: FieldModifier?,
  val typ: Type,
  val name: Ident,
  val fieldNumber: IntLit,
  val options: List<FieldOption>?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, AstNode

data class Package(
  val name: FullIdent,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ProtoDefElem, AstNode

sealed interface ReservedBody: AstNode

sealed interface StrLit: AstNode

sealed interface FloatLit: AstNode

data class Import(
  val importType: ImportType?,
  val target: StrLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ProtoDefElem, AstNode

sealed interface RangeEnd: AstNode

data class BoolConstant(
  val value: BoolLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode

sealed interface MessageBodyElem: AstNode

sealed interface OptionScope: AstNode

data class Message(
  val name: Ident,
  val body: List<MessageBodyElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, TopLevelDef, AstNode

data class Reserved(
  val value: ReservedBody,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, AstNode

sealed interface CharValue: AstNode

data class DoubleQuoteStrLit(
  val value: List<CharValue>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StrLit, AstNode

data class Exponent(
  val sign: Sign?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class Ident(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): OptionScope, AstNode

data class EnumValueOption(
  val name: OptionName,
  val value: Constant,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class BuiltinType(
  val typ: BuiltinTypeEnum,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode

sealed interface TopLevelDef: ProtoDefElem, AstNode

data class OctalLit(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLit, AstNode

sealed interface Type: AstNode

data class Range(
  val rangeStart: IntLit,
  val rangeEnd: RangeEnd?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode

data class HexEscape(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode
enum class BoolLit { FALSE, TRUE }
enum class BuiltinTypeEnum { BOOL, BYTES, DOUBLE, FIXED32, FIXED64, FLOAT, INT32, INT64, SFIXED32, SFIXED64, SINT32, SINT64, STRING, UINT32, UINT64 }
enum class FieldModifier { OPTIONAL, REPEATED }
enum class ImportType { PUBLIC, WEAK }
enum class MapKeyType { BOOL, FIXED32, FIXED64, INT32, INT64, SFIXED32, SFIXED64, SINT32, SINT64, STRING, UINT32, UINT64 }
enum class Sign { MINUS, PLUS }

fun matchStart(): Proto3 {
  val lastGen = source.length
  val kernel = history[lastGen].getSingle(2, 1, 0, lastGen)
  return matchProto3(kernel.beginGen, kernel.endGen)
}

fun matchProto3(beginGen: Int, endGen: Int): Proto3 {
val var1 = getSequenceElems(history, 3, listOf(4,38,68,4), beginGen, endGen)
val var2 = unrollRepeat0(history, 68, 70, 6, 69, var1[2].first, var1[2].second).map { k ->
val var3 = getSequenceElems(history, 71, listOf(4,72), k.first, k.second)
val var4 = history[var3[1].second].findByBeginGenOpt(73, 1, var3[1].first)
val var5 = history[var3[1].second].findByBeginGenOpt(124, 1, var3[1].first)
val var6 = history[var3[1].second].findByBeginGenOpt(150, 1, var3[1].first)
val var7 = history[var3[1].second].findByBeginGenOpt(215, 1, var3[1].first)
val var8 = history[var3[1].second].findByBeginGenOpt(332, 1, var3[1].first)
check(hasSingleTrue(var4 != null, var5 != null, var6 != null, var7 != null, var8 != null))
val var9 = when {
var4 != null -> {
val var10 = matchImport(var3[1].first, var3[1].second)
var10
}
var5 != null -> {
val var11 = matchPackage(var3[1].first, var3[1].second)
var11
}
var6 != null -> {
val var12 = matchOption(var3[1].first, var3[1].second)
var12
}
var7 != null -> {
val var13 = matchTopLevelDef(var3[1].first, var3[1].second)
var13
}
else -> {
val var14 = matchEmptyStatement(var3[1].first, var3[1].second)
var14
}
}
var9
}
val var15 = Proto3(var2, nextId(), beginGen, endGen)
return var15
}

fun matchPackage(beginGen: Int, endGen: Int): Package {
val var16 = getSequenceElems(history, 125, listOf(126,4,131,4,67), beginGen, endGen)
val var17 = matchFullIdent(var16[2].first, var16[2].second)
val var18 = Package(var17, nextId(), beginGen, endGen)
return var18
}

fun matchOption(beginGen: Int, endGen: Int): OptionDef {
val var19 = getSequenceElems(history, 151, listOf(152,4,156,4,55,4,167,4,67), beginGen, endGen)
val var20 = matchOptionName(var19[2].first, var19[2].second)
val var21 = matchConstant(var19[6].first, var19[6].second)
val var22 = OptionDef(var20, var21, nextId(), beginGen, endGen)
return var22
}

fun matchConstant(beginGen: Int, endGen: Int): Constant {
val var23 = history[endGen].findByBeginGenOpt(103, 1, beginGen)
val var24 = history[endGen].findByBeginGenOpt(168, 1, beginGen)
val var25 = history[endGen].findByBeginGenOpt(169, 1, beginGen)
val var26 = history[endGen].findByBeginGenOpt(175, 2, beginGen)
val var27 = history[endGen].findByBeginGenOpt(198, 2, beginGen)
check(hasSingleTrue(var23 != null, var24 != null, var25 != null, var26 != null, var27 != null))
val var28 = when {
var23 != null -> {
val var29 = matchStrLit(beginGen, endGen)
val var30 = StringConstant(var29, nextId(), beginGen, endGen)
var30
}
var24 != null -> {
val var31 = matchFullIdent(beginGen, endGen)
var31
}
var25 != null -> {
val var32 = matchBoolLit(beginGen, endGen)
val var33 = BoolConstant(var32, nextId(), beginGen, endGen)
var33
}
var26 != null -> {
val var34 = getSequenceElems(history, 175, listOf(176,180), beginGen, endGen)
val var35 = history[var34[0].second].findByBeginGenOpt(102, 1, var34[0].first)
val var36 = history[var34[0].second].findByBeginGenOpt(177, 1, var34[0].first)
check(hasSingleTrue(var35 != null, var36 != null))
val var37 = when {
var35 != null -> null
else -> {
val var38 = matchSign(var34[0].first, var34[0].second)
var38
}
}
val var39 = matchIntLit(var34[1].first, var34[1].second)
val var40 = IntConstant(var37, var39, nextId(), beginGen, endGen)
var40
}
else -> {
val var41 = getSequenceElems(history, 198, listOf(176,199), beginGen, endGen)
val var42 = history[var41[0].second].findByBeginGenOpt(102, 1, var41[0].first)
val var43 = history[var41[0].second].findByBeginGenOpt(177, 1, var41[0].first)
check(hasSingleTrue(var42 != null, var43 != null))
val var44 = when {
var42 != null -> null
else -> {
val var45 = matchSign(var41[0].first, var41[0].second)
var45
}
}
val var46 = matchFloatLit(var41[1].first, var41[1].second)
val var47 = FloatConstant(var44, var46, nextId(), beginGen, endGen)
var47
}
}
return var28
}

fun matchIntLit(beginGen: Int, endGen: Int): IntLit {
val var48 = history[endGen].findByBeginGenOpt(183, 1, beginGen)
val var49 = history[endGen].findByBeginGenOpt(185, 1, beginGen)
val var50 = history[endGen].findByBeginGenOpt(190, 1, beginGen)
val var51 = history[endGen].findByBeginGenOpt(194, 1, beginGen)
check(hasSingleTrue(var48 != null, var49 != null, var50 != null, var51 != null))
val var52 = when {
var48 != null -> {
val var53 = matchZeroLit(beginGen, endGen)
var53
}
var49 != null -> {
val var54 = matchDecimalLit(beginGen, endGen)
var54
}
var50 != null -> {
val var55 = matchOctalLit(beginGen, endGen)
var55
}
else -> {
val var56 = matchHexLit(beginGen, endGen)
var56
}
}
return var52
}

fun matchHexLit(beginGen: Int, endGen: Int): HexLit {
val var57 = getSequenceElems(history, 195, listOf(184,111,196), beginGen, endGen)
val var58 = unrollRepeat1(history, 196, 112, 112, 197, var57[2].first, var57[2].second).map { k ->
val var59 = matchHexDigit(k.first, k.second)
var59
}
val var60 = HexLit(var58.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var60
}

fun matchZeroLit(beginGen: Int, endGen: Int): DecimalLit {
val var61 = DecimalLit("0", nextId(), beginGen, endGen)
return var61
}

fun matchSign(beginGen: Int, endGen: Int): Sign {
val var62 = history[endGen].findByBeginGenOpt(178, 1, beginGen)
val var63 = history[endGen].findByBeginGenOpt(179, 1, beginGen)
check(hasSingleTrue(var62 != null, var63 != null))
val var64 = when {
var62 != null -> Sign.PLUS
else -> Sign.MINUS
}
return var64
}

fun matchBoolLit(beginGen: Int, endGen: Int): BoolLit {
val var65 = history[endGen].findByBeginGenOpt(170, 1, beginGen)
val var66 = history[endGen].findByBeginGenOpt(172, 1, beginGen)
check(hasSingleTrue(var65 != null, var66 != null))
val var67 = when {
var65 != null -> BoolLit.TRUE
else -> BoolLit.FALSE
}
return var67
}

fun matchEmptyStatement(beginGen: Int, endGen: Int): EmptyStatement {
val var68 = EmptyStatement(nextId(), beginGen, endGen)
return var68
}

fun matchTopLevelDef(beginGen: Int, endGen: Int): TopLevelDef {
val var69 = history[endGen].findByBeginGenOpt(216, 1, beginGen)
val var70 = history[endGen].findByBeginGenOpt(305, 1, beginGen)
val var71 = history[endGen].findByBeginGenOpt(393, 1, beginGen)
check(hasSingleTrue(var69 != null, var70 != null, var71 != null))
val var72 = when {
var69 != null -> {
val var73 = matchMessage(beginGen, endGen)
var73
}
var70 != null -> {
val var74 = matchEnum(beginGen, endGen)
var74
}
else -> {
val var75 = matchService(beginGen, endGen)
var75
}
}
return var72
}

fun matchService(beginGen: Int, endGen: Int): Service {
val var76 = getSequenceElems(history, 394, listOf(395,4,399,4,225,400,4,333), beginGen, endGen)
val var77 = matchServiceName(var76[2].first, var76[2].second)
val var78 = unrollRepeat0(history, 400, 402, 6, 401, var76[5].first, var76[5].second).map { k ->
val var79 = getSequenceElems(history, 403, listOf(4,404), k.first, k.second)
val var80 = history[var79[1].second].findByBeginGenOpt(150, 1, var79[1].first)
val var81 = history[var79[1].second].findByBeginGenOpt(332, 1, var79[1].first)
val var82 = history[var79[1].second].findByBeginGenOpt(405, 1, var79[1].first)
check(hasSingleTrue(var80 != null, var81 != null, var82 != null))
val var83 = when {
var80 != null -> {
val var84 = matchOption(var79[1].first, var79[1].second)
var84
}
var81 != null -> {
val var85 = matchEmptyStatement(var79[1].first, var79[1].second)
var85
}
else -> {
val var86 = matchRpc(var79[1].first, var79[1].second)
var86
}
}
var83
}
val var87 = Service(var77, var78, nextId(), beginGen, endGen)
return var87
}

fun matchServiceName(beginGen: Int, endGen: Int): Ident {
val var88 = matchIdent(beginGen, endGen)
return var88
}

fun matchEnum(beginGen: Int, endGen: Int): EnumDef {
val var89 = getSequenceElems(history, 306, listOf(307,4,311,4,312), beginGen, endGen)
val var90 = matchEnumName(var89[2].first, var89[2].second)
val var91 = matchEnumBody(var89[4].first, var89[4].second)
val var92 = EnumDef(var90, var91, nextId(), beginGen, endGen)
return var92
}

fun matchEnumBody(beginGen: Int, endGen: Int): List<EnumBodyElem> {
val var93 = getSequenceElems(history, 313, listOf(225,314,4,333), beginGen, endGen)
val var94 = unrollRepeat0(history, 314, 316, 6, 315, var93[1].first, var93[1].second).map { k ->
val var95 = getSequenceElems(history, 317, listOf(4,318), k.first, k.second)
val var96 = history[var95[1].second].findByBeginGenOpt(150, 1, var95[1].first)
val var97 = history[var95[1].second].findByBeginGenOpt(319, 1, var95[1].first)
val var98 = history[var95[1].second].findByBeginGenOpt(332, 1, var95[1].first)
check(hasSingleTrue(var96 != null, var97 != null, var98 != null))
val var99 = when {
var96 != null -> {
val var100 = matchOption(var95[1].first, var95[1].second)
var100
}
var97 != null -> {
val var101 = matchEnumField(var95[1].first, var95[1].second)
var101
}
else -> {
val var102 = matchEmptyStatement(var95[1].first, var95[1].second)
var102
}
}
var99
}
return var94
}

fun matchEnumField(beginGen: Int, endGen: Int): EnumFieldDef {
val var103 = getSequenceElems(history, 320, listOf(133,4,55,321,4,180,324,4,67), beginGen, endGen)
val var104 = matchIdent(var103[0].first, var103[0].second)
val var105 = history[var103[3].second].findByBeginGenOpt(102, 1, var103[3].first)
val var106 = history[var103[3].second].findByBeginGenOpt(322, 1, var103[3].first)
check(hasSingleTrue(var105 != null, var106 != null))
val var107 = when {
var105 != null -> null
else -> {
val var108 = getSequenceElems(history, 323, listOf(4,179), var103[3].first, var103[3].second)
source[var108[1].first]
}
}
val var109 = matchIntLit(var103[5].first, var103[5].second)
val var110 = history[var103[6].second].findByBeginGenOpt(102, 1, var103[6].first)
val var111 = history[var103[6].second].findByBeginGenOpt(325, 1, var103[6].first)
check(hasSingleTrue(var110 != null, var111 != null))
val var112 = when {
var110 != null -> null
else -> {
val var113 = getSequenceElems(history, 326, listOf(4,294,4,327,328,4,304), var103[6].first, var103[6].second)
val var114 = matchEnumValueOption(var113[3].first, var113[3].second)
val var115 = unrollRepeat0(history, 328, 330, 6, 329, var113[4].first, var113[4].second).map { k ->
val var116 = getSequenceElems(history, 331, listOf(4,303,4,327), k.first, k.second)
val var117 = matchEnumValueOption(var116[3].first, var116[3].second)
var117
}
listOf(var114) + var115
}
}
val var118 = EnumFieldDef(var104, var107 != null, var109, var112, nextId(), beginGen, endGen)
return var118
}

fun matchEnumValueOption(beginGen: Int, endGen: Int): EnumValueOption {
val var119 = getSequenceElems(history, 298, listOf(156,4,55,4,167), beginGen, endGen)
val var120 = matchOptionName(var119[0].first, var119[0].second)
val var121 = matchConstant(var119[4].first, var119[4].second)
val var122 = EnumValueOption(var120, var121, nextId(), beginGen, endGen)
return var122
}

fun matchMessage(beginGen: Int, endGen: Int): Message {
val var123 = getSequenceElems(history, 217, listOf(218,4,222,4,223), beginGen, endGen)
val var124 = matchMessageName(var123[2].first, var123[2].second)
val var125 = matchMessageBody(var123[4].first, var123[4].second)
val var126 = Message(var124, var125, nextId(), beginGen, endGen)
return var126
}

fun matchMessageName(beginGen: Int, endGen: Int): Ident {
val var127 = matchIdent(beginGen, endGen)
return var127
}

fun matchMessageBody(beginGen: Int, endGen: Int): List<MessageBodyElem> {
val var128 = getSequenceElems(history, 224, listOf(225,226,4,333), beginGen, endGen)
val var129 = unrollRepeat0(history, 226, 228, 6, 227, var128[1].first, var128[1].second).map { k ->
val var130 = getSequenceElems(history, 229, listOf(4,230), k.first, k.second)
val var131 = matchMessageBodyElem(var130[1].first, var130[1].second)
var131
}
return var129
}

fun matchMessageBodyElem(beginGen: Int, endGen: Int): MessageBodyElem {
val var132 = history[endGen].findByBeginGenOpt(150, 1, beginGen)
val var133 = history[endGen].findByBeginGenOpt(216, 1, beginGen)
val var134 = history[endGen].findByBeginGenOpt(231, 1, beginGen)
val var135 = history[endGen].findByBeginGenOpt(305, 1, beginGen)
val var136 = history[endGen].findByBeginGenOpt(332, 1, beginGen)
val var137 = history[endGen].findByBeginGenOpt(334, 1, beginGen)
val var138 = history[endGen].findByBeginGenOpt(348, 1, beginGen)
val var139 = history[endGen].findByBeginGenOpt(360, 1, beginGen)
check(hasSingleTrue(var132 != null, var133 != null, var134 != null, var135 != null, var136 != null, var137 != null, var138 != null, var139 != null))
val var140 = when {
var132 != null -> {
val var141 = matchOption(beginGen, endGen)
var141
}
var133 != null -> {
val var142 = matchMessage(beginGen, endGen)
var142
}
var134 != null -> {
val var143 = matchField(beginGen, endGen)
var143
}
var135 != null -> {
val var144 = matchEnum(beginGen, endGen)
var144
}
var136 != null -> {
val var145 = matchEmptyStatement(beginGen, endGen)
var145
}
var137 != null -> {
val var146 = matchOneof(beginGen, endGen)
var146
}
var138 != null -> {
val var147 = matchMapField(beginGen, endGen)
var147
}
else -> {
val var148 = matchReserved(beginGen, endGen)
var148
}
}
return var140
}

fun matchReserved(beginGen: Int, endGen: Int): Reserved {
val var149 = getSequenceElems(history, 361, listOf(362,4,367,4,67), beginGen, endGen)
val var150 = history[var149[2].second].findByBeginGenOpt(368, 1, var149[2].first)
val var151 = history[var149[2].second].findByBeginGenOpt(387, 1, var149[2].first)
check(hasSingleTrue(var150 != null, var151 != null))
val var152 = when {
var150 != null -> {
val var153 = matchRanges(var149[2].first, var149[2].second)
var153
}
else -> {
val var154 = matchFieldNames(var149[2].first, var149[2].second)
var154
}
}
val var155 = Reserved(var152, nextId(), beginGen, endGen)
return var155
}

fun matchOneof(beginGen: Int, endGen: Int): OneofDef {
val var156 = getSequenceElems(history, 335, listOf(336,4,340,4,225,341,4,333), beginGen, endGen)
val var157 = matchOneofName(var156[2].first, var156[2].second)
val var158 = unrollRepeat0(history, 341, 343, 6, 342, var156[5].first, var156[5].second).map { k ->
val var159 = getSequenceElems(history, 344, listOf(4,345), k.first, k.second)
val var160 = history[var159[1].second].findByBeginGenOpt(150, 1, var159[1].first)
val var161 = history[var159[1].second].findByBeginGenOpt(332, 1, var159[1].first)
val var162 = history[var159[1].second].findByBeginGenOpt(346, 1, var159[1].first)
check(hasSingleTrue(var160 != null, var161 != null, var162 != null))
val var163 = when {
var160 != null -> {
val var164 = matchOption(var159[1].first, var159[1].second)
var164
}
var161 != null -> {
val var165 = matchEmptyStatement(var159[1].first, var159[1].second)
var165
}
else -> {
val var166 = matchOneofField(var159[1].first, var159[1].second)
var166
}
}
var163
}
val var167 = OneofDef(var157, var158, nextId(), beginGen, endGen)
return var167
}

fun matchField(beginGen: Int, endGen: Int): Field {
val var168 = getSequenceElems(history, 232, listOf(233,244,4,289,4,55,4,290,291,4,67), beginGen, endGen)
val var169 = history[var168[0].second].findByBeginGenOpt(102, 1, var168[0].first)
val var170 = history[var168[0].second].findByBeginGenOpt(234, 1, var168[0].first)
check(hasSingleTrue(var169 != null, var170 != null))
val var171 = when {
var169 != null -> null
else -> {
val var172 = getSequenceElems(history, 235, listOf(236,4), var168[0].first, var168[0].second)
val var173 = matchFieldModifier(var172[0].first, var172[0].second)
var173
}
}
val var174 = matchType(var168[1].first, var168[1].second)
val var175 = matchFieldName(var168[3].first, var168[3].second)
val var176 = matchFieldNumber(var168[7].first, var168[7].second)
val var177 = history[var168[8].second].findByBeginGenOpt(102, 1, var168[8].first)
val var178 = history[var168[8].second].findByBeginGenOpt(292, 1, var168[8].first)
check(hasSingleTrue(var177 != null, var178 != null))
val var179 = when {
var177 != null -> null
else -> {
val var180 = getSequenceElems(history, 293, listOf(4,294,4,295,4,304), var168[8].first, var168[8].second)
val var181 = matchFieldOptions(var180[3].first, var180[3].second)
var181
}
}
val var182 = Field(var171, var174, var175, var176, var179, nextId(), beginGen, endGen)
return var182
}

fun matchFieldNames(beginGen: Int, endGen: Int): FieldNames {
val var183 = getSequenceElems(history, 388, listOf(289,389), beginGen, endGen)
val var184 = matchFieldName(var183[0].first, var183[0].second)
val var185 = unrollRepeat0(history, 389, 391, 6, 390, var183[1].first, var183[1].second).map { k ->
val var186 = getSequenceElems(history, 392, listOf(4,303,4,289), k.first, k.second)
val var187 = matchFieldName(var186[3].first, var186[3].second)
var187
}
val var188 = FieldNames(listOf(var184) + var185, nextId(), beginGen, endGen)
return var188
}

fun matchOneofField(beginGen: Int, endGen: Int): OneofField {
val var189 = getSequenceElems(history, 347, listOf(244,4,289,4,55,4,290,291,4,67), beginGen, endGen)
val var190 = matchType(var189[0].first, var189[0].second)
val var191 = matchFieldName(var189[2].first, var189[2].second)
val var192 = matchFieldNumber(var189[6].first, var189[6].second)
val var193 = history[var189[7].second].findByBeginGenOpt(102, 1, var189[7].first)
val var194 = history[var189[7].second].findByBeginGenOpt(292, 1, var189[7].first)
check(hasSingleTrue(var193 != null, var194 != null))
val var195 = when {
var193 != null -> null
else -> {
val var196 = getSequenceElems(history, 293, listOf(4,294,4,295,4,304), var189[7].first, var189[7].second)
val var197 = matchFieldOptions(var196[3].first, var196[3].second)
var197
}
}
val var198 = OneofField(var190, var191, var192, var195, nextId(), beginGen, endGen)
return var198
}

fun matchFieldModifier(beginGen: Int, endGen: Int): FieldModifier {
val var199 = history[endGen].findByBeginGenOpt(239, 1, beginGen)
val var200 = history[endGen].findByBeginGenOpt(242, 1, beginGen)
check(hasSingleTrue(var199 != null, var200 != null))
val var201 = when {
var199 != null -> FieldModifier.REPEATED
else -> FieldModifier.OPTIONAL
}
return var201
}

fun matchMapField(beginGen: Int, endGen: Int): MapField {
val var202 = getSequenceElems(history, 349, listOf(350,4,354,4,355,4,303,4,244,4,358,4,359,4,55,4,290,291,4,67), beginGen, endGen)
val var203 = matchKeyType(var202[4].first, var202[4].second)
val var204 = matchType(var202[8].first, var202[8].second)
val var205 = matchMapName(var202[12].first, var202[12].second)
val var206 = matchFieldNumber(var202[16].first, var202[16].second)
val var207 = history[var202[17].second].findByBeginGenOpt(102, 1, var202[17].first)
val var208 = history[var202[17].second].findByBeginGenOpt(292, 1, var202[17].first)
check(hasSingleTrue(var207 != null, var208 != null))
val var209 = when {
var207 != null -> null
else -> {
val var210 = getSequenceElems(history, 293, listOf(4,294,4,295,4,304), var202[17].first, var202[17].second)
val var211 = matchFieldOptions(var210[3].first, var210[3].second)
var211
}
}
val var212 = MapField(var203, var204, var205, var206, var209, nextId(), beginGen, endGen)
return var212
}

fun matchKeyType(beginGen: Int, endGen: Int): MapKeyType {
val var213 = history[endGen].findByBeginGenOpt(252, 1, beginGen)
val var214 = history[endGen].findByBeginGenOpt(255, 1, beginGen)
val var215 = history[endGen].findByBeginGenOpt(259, 1, beginGen)
val var216 = history[endGen].findByBeginGenOpt(261, 1, beginGen)
val var217 = history[endGen].findByBeginGenOpt(263, 1, beginGen)
val var218 = history[endGen].findByBeginGenOpt(265, 1, beginGen)
val var219 = history[endGen].findByBeginGenOpt(267, 1, beginGen)
val var220 = history[endGen].findByBeginGenOpt(269, 1, beginGen)
val var221 = history[endGen].findByBeginGenOpt(271, 1, beginGen)
val var222 = history[endGen].findByBeginGenOpt(273, 1, beginGen)
val var223 = history[endGen].findByBeginGenOpt(275, 1, beginGen)
val var224 = history[endGen].findByBeginGenOpt(277, 1, beginGen)
check(hasSingleTrue(var213 != null, var214 != null, var215 != null, var216 != null, var217 != null, var218 != null, var219 != null, var220 != null, var221 != null, var222 != null, var223 != null, var224 != null))
val var225 = when {
var213 != null -> MapKeyType.INT32
var214 != null -> MapKeyType.INT64
var215 != null -> MapKeyType.UINT32
var216 != null -> MapKeyType.UINT64
var217 != null -> MapKeyType.SINT32
var218 != null -> MapKeyType.SINT64
var219 != null -> MapKeyType.FIXED32
var220 != null -> MapKeyType.FIXED64
var221 != null -> MapKeyType.SFIXED32
var222 != null -> MapKeyType.SFIXED64
var223 != null -> MapKeyType.BOOL
else -> MapKeyType.STRING
}
return var225
}

fun matchMapName(beginGen: Int, endGen: Int): Ident {
val var226 = matchIdent(beginGen, endGen)
return var226
}

fun matchFieldName(beginGen: Int, endGen: Int): Ident {
val var227 = matchIdent(beginGen, endGen)
return var227
}

fun matchFieldOptions(beginGen: Int, endGen: Int): List<FieldOption> {
val var228 = getSequenceElems(history, 296, listOf(297,299), beginGen, endGen)
val var229 = matchFieldOption(var228[0].first, var228[0].second)
val var230 = unrollRepeat0(history, 299, 301, 6, 300, var228[1].first, var228[1].second).map { k ->
val var231 = getSequenceElems(history, 302, listOf(4,303,4,297), k.first, k.second)
val var232 = matchFieldOption(var231[3].first, var231[3].second)
var232
}
return listOf(var229) + var230
}

fun matchFieldOption(beginGen: Int, endGen: Int): FieldOption {
val var233 = getSequenceElems(history, 298, listOf(156,4,55,4,167), beginGen, endGen)
val var234 = matchOptionName(var233[0].first, var233[0].second)
val var235 = matchConstant(var233[4].first, var233[4].second)
val var236 = FieldOption(var234, var235, nextId(), beginGen, endGen)
return var236
}

fun matchEnumName(beginGen: Int, endGen: Int): Ident {
val var237 = matchIdent(beginGen, endGen)
return var237
}

fun matchIdent(beginGen: Int, endGen: Int): Ident {
val var238 = getSequenceElems(history, 136, listOf(137,139), beginGen, endGen)
val var239 = matchLetter(var238[0].first, var238[0].second)
val var240 = unrollRepeat0(history, 139, 141, 6, 140, var238[1].first, var238[1].second).map { k ->
val var241 = history[k.second].findByBeginGenOpt(137, 1, k.first)
val var242 = history[k.second].findByBeginGenOpt(142, 1, k.first)
val var243 = history[k.second].findByBeginGenOpt(144, 1, k.first)
check(hasSingleTrue(var241 != null, var242 != null, var243 != null))
val var244 = when {
var241 != null -> {
val var245 = matchLetter(k.first, k.second)
var245
}
var242 != null -> {
val var246 = matchDecimalDigit(k.first, k.second)
var246
}
else -> source[k.first]
}
var244
}
val var247 = Ident(var239.toString() + var240.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var247
}

fun matchDecimalDigit(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchLetter(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchImport(beginGen: Int, endGen: Int): Import {
val var248 = getSequenceElems(history, 74, listOf(75,81,4,103,4,67), beginGen, endGen)
val var249 = history[var248[1].second].findByBeginGenOpt(82, 1, var248[1].first)
val var250 = history[var248[1].second].findByBeginGenOpt(102, 1, var248[1].first)
check(hasSingleTrue(var249 != null, var250 != null))
val var251 = when {
var249 != null -> {
val var252 = history[var248[1].second].findByBeginGenOpt(83, 1, var248[1].first)
val var253 = history[var248[1].second].findByBeginGenOpt(92, 1, var248[1].first)
check(hasSingleTrue(var252 != null, var253 != null))
val var254 = when {
var252 != null -> ImportType.WEAK
else -> ImportType.PUBLIC
}
var254
}
else -> null
}
val var255 = matchStrLit(var248[3].first, var248[3].second)
val var256 = Import(var251, var255, nextId(), beginGen, endGen)
return var256
}

fun matchHexDigit(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchFieldNumber(beginGen: Int, endGen: Int): IntLit {
val var257 = matchIntLit(beginGen, endGen)
return var257
}

fun matchStrLit(beginGen: Int, endGen: Int): StrLit {
val var258 = history[endGen].findByBeginGenOpt(104, 3, beginGen)
val var259 = history[endGen].findByBeginGenOpt(123, 3, beginGen)
check(hasSingleTrue(var258 != null, var259 != null))
val var260 = when {
var258 != null -> {
val var261 = getSequenceElems(history, 104, listOf(66,105,66), beginGen, endGen)
val var262 = unrollRepeat0(history, 105, 107, 6, 106, var261[1].first, var261[1].second).map { k ->
val var263 = matchCharValue(k.first, k.second)
var263
}
val var264 = SingleQuoteStrLit(var262, nextId(), beginGen, endGen)
var264
}
else -> {
val var265 = getSequenceElems(history, 123, listOf(59,105,59), beginGen, endGen)
val var266 = unrollRepeat0(history, 105, 107, 6, 106, var265[1].first, var265[1].second).map { k ->
val var267 = matchCharValue(k.first, k.second)
var267
}
val var268 = DoubleQuoteStrLit(var266, nextId(), beginGen, endGen)
var268
}
}
return var260
}

fun matchCharValue(beginGen: Int, endGen: Int): CharValue {
val var269 = history[endGen].findByBeginGenOpt(108, 1, beginGen)
val var270 = history[endGen].findByBeginGenOpt(114, 1, beginGen)
val var271 = history[endGen].findByBeginGenOpt(118, 1, beginGen)
val var272 = history[endGen].findByBeginGenOpt(121, 1, beginGen)
check(hasSingleTrue(var269 != null, var270 != null, var271 != null, var272 != null))
val var273 = when {
var269 != null -> {
val var274 = matchHexEscape(beginGen, endGen)
var274
}
var270 != null -> {
val var275 = matchOctEscape(beginGen, endGen)
var275
}
var271 != null -> {
val var276 = matchCharEscape(beginGen, endGen)
var276
}
else -> {
val var277 = Character(source[beginGen], nextId(), beginGen, endGen)
var277
}
}
return var273
}

fun matchOctEscape(beginGen: Int, endGen: Int): OctalEscape {
val var278 = getSequenceElems(history, 115, listOf(110,116,116,116), beginGen, endGen)
val var279 = matchOctalDigit(var278[1].first, var278[1].second)
val var280 = matchOctalDigit(var278[2].first, var278[2].second)
val var281 = matchOctalDigit(var278[3].first, var278[3].second)
val var282 = OctalEscape(var279.toString() + var280.toString() + var281.toString(), nextId(), beginGen, endGen)
return var282
}

fun matchCharEscape(beginGen: Int, endGen: Int): CharEscape {
val var283 = getSequenceElems(history, 119, listOf(110,120), beginGen, endGen)
val var284 = CharEscape(source[var283[1].first], nextId(), beginGen, endGen)
return var284
}

fun matchHexEscape(beginGen: Int, endGen: Int): HexEscape {
val var285 = getSequenceElems(history, 109, listOf(110,111,112,112), beginGen, endGen)
val var286 = matchHexDigit(var285[2].first, var285[2].second)
val var287 = matchHexDigit(var285[3].first, var285[3].second)
val var288 = HexEscape(var286.toString() + var287.toString(), nextId(), beginGen, endGen)
return var288
}

fun matchFullIdent(beginGen: Int, endGen: Int): FullIdent {
val var289 = getSequenceElems(history, 132, listOf(133,145), beginGen, endGen)
val var290 = matchIdent(var289[0].first, var289[0].second)
val var291 = unrollRepeat0(history, 145, 147, 6, 146, var289[1].first, var289[1].second).map { k ->
val var292 = getSequenceElems(history, 148, listOf(149,133), k.first, k.second)
val var293 = matchIdent(var292[1].first, var292[1].second)
var293
}
val var294 = FullIdent(listOf(var290) + var291, nextId(), beginGen, endGen)
return var294
}

fun matchOctalDigit(beginGen: Int, endGen: Int): Char {
return source[beginGen]
}

fun matchOneofName(beginGen: Int, endGen: Int): Ident {
val var295 = matchIdent(beginGen, endGen)
return var295
}

fun matchRanges(beginGen: Int, endGen: Int): Ranges {
val var296 = getSequenceElems(history, 369, listOf(370,383), beginGen, endGen)
val var297 = matchRange(var296[0].first, var296[0].second)
val var298 = unrollRepeat0(history, 383, 385, 6, 384, var296[1].first, var296[1].second).map { k ->
val var299 = getSequenceElems(history, 386, listOf(4,303,4,370), k.first, k.second)
val var300 = matchRange(var299[3].first, var299[3].second)
var300
}
val var301 = Ranges(listOf(var297) + var298, nextId(), beginGen, endGen)
return var301
}

fun matchRange(beginGen: Int, endGen: Int): Range {
val var302 = getSequenceElems(history, 371, listOf(180,372), beginGen, endGen)
val var303 = matchIntLit(var302[0].first, var302[0].second)
val var304 = history[var302[1].second].findByBeginGenOpt(102, 1, var302[1].first)
val var305 = history[var302[1].second].findByBeginGenOpt(373, 1, var302[1].first)
check(hasSingleTrue(var304 != null, var305 != null))
val var306 = when {
var304 != null -> null
else -> {
val var307 = getSequenceElems(history, 374, listOf(4,375,4,379), var302[1].first, var302[1].second)
val var308 = matchRangeEnd(var307[3].first, var307[3].second)
var308
}
}
val var309 = Range(var303, var306, nextId(), beginGen, endGen)
return var309
}

fun matchRangeEnd(beginGen: Int, endGen: Int): RangeEnd {
val var310 = history[endGen].findByBeginGenOpt(180, 1, beginGen)
val var311 = history[endGen].findByBeginGenOpt(380, 1, beginGen)
check(hasSingleTrue(var310 != null, var311 != null))
val var312 = when {
var310 != null -> {
val var313 = matchIntLit(beginGen, endGen)
val var314 = RangeEndValue(var313, nextId(), beginGen, endGen)
var314
}
else -> {
val var315 = RangeEndMax(nextId(), beginGen, endGen)
var315
}
}
return var312
}

fun matchType(beginGen: Int, endGen: Int): Type {
val var316 = history[endGen].findByBeginGenOpt(245, 1, beginGen)
val var317 = history[endGen].findByBeginGenOpt(281, 1, beginGen)
check(hasSingleTrue(var316 != null, var317 != null))
val var318 = when {
var316 != null -> {
val var319 = matchBuiltinType(beginGen, endGen)
val var320 = BuiltinType(var319, nextId(), beginGen, endGen)
var320
}
else -> {
val var321 = matchMessageType(beginGen, endGen)
val var322 = MessageOrEnumType(var321, nextId(), beginGen, endGen)
var322
}
}
return var318
}

fun matchMessageType(beginGen: Int, endGen: Int): MessageType {
val var323 = getSequenceElems(history, 283, listOf(284,285,222), beginGen, endGen)
val var324 = history[var323[0].second].findByBeginGenOpt(102, 1, var323[0].first)
val var325 = history[var323[0].second].findByBeginGenOpt(149, 1, var323[0].first)
check(hasSingleTrue(var324 != null, var325 != null))
val var326 = when {
var324 != null -> null
else -> source[var323[0].first]
}
val var327 = unrollRepeat0(history, 285, 287, 6, 286, var323[1].first, var323[1].second).map { k ->
val var328 = getSequenceElems(history, 288, listOf(133,149), k.first, k.second)
source[var328[1].first]
}
val var329 = matchMessageName(var323[2].first, var323[2].second)
val var330 = MessageType(var326 != null, var327, var329, nextId(), beginGen, endGen)
return var330
}

fun matchBuiltinType(beginGen: Int, endGen: Int): BuiltinTypeEnum {
val var331 = history[endGen].findByBeginGenOpt(248, 1, beginGen)
val var332 = history[endGen].findByBeginGenOpt(250, 1, beginGen)
val var333 = history[endGen].findByBeginGenOpt(252, 1, beginGen)
val var334 = history[endGen].findByBeginGenOpt(255, 1, beginGen)
val var335 = history[endGen].findByBeginGenOpt(259, 1, beginGen)
val var336 = history[endGen].findByBeginGenOpt(261, 1, beginGen)
val var337 = history[endGen].findByBeginGenOpt(263, 1, beginGen)
val var338 = history[endGen].findByBeginGenOpt(265, 1, beginGen)
val var339 = history[endGen].findByBeginGenOpt(267, 1, beginGen)
val var340 = history[endGen].findByBeginGenOpt(269, 1, beginGen)
val var341 = history[endGen].findByBeginGenOpt(271, 1, beginGen)
val var342 = history[endGen].findByBeginGenOpt(273, 1, beginGen)
val var343 = history[endGen].findByBeginGenOpt(275, 1, beginGen)
val var344 = history[endGen].findByBeginGenOpt(277, 1, beginGen)
val var345 = history[endGen].findByBeginGenOpt(279, 1, beginGen)
check(hasSingleTrue(var331 != null, var332 != null, var333 != null, var334 != null, var335 != null, var336 != null, var337 != null, var338 != null, var339 != null, var340 != null, var341 != null, var342 != null, var343 != null, var344 != null, var345 != null))
val var346 = when {
var331 != null -> BuiltinTypeEnum.DOUBLE
var332 != null -> BuiltinTypeEnum.FLOAT
var333 != null -> BuiltinTypeEnum.INT32
var334 != null -> BuiltinTypeEnum.INT64
var335 != null -> BuiltinTypeEnum.UINT32
var336 != null -> BuiltinTypeEnum.UINT64
var337 != null -> BuiltinTypeEnum.SINT32
var338 != null -> BuiltinTypeEnum.SINT64
var339 != null -> BuiltinTypeEnum.FIXED32
var340 != null -> BuiltinTypeEnum.FIXED64
var341 != null -> BuiltinTypeEnum.SFIXED32
var342 != null -> BuiltinTypeEnum.SFIXED64
var343 != null -> BuiltinTypeEnum.BOOL
var344 != null -> BuiltinTypeEnum.STRING
else -> BuiltinTypeEnum.BYTES
}
return var346
}

fun matchDecimalLit(beginGen: Int, endGen: Int): DecimalLit {
val var347 = getSequenceElems(history, 186, listOf(187,188), beginGen, endGen)
val var348 = unrollRepeat0(history, 188, 142, 6, 189, var347[1].first, var347[1].second).map { k ->
val var349 = matchDecimalDigit(k.first, k.second)
var349
}
val var350 = DecimalLit(source[var347[0].first].toString() + var348.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var350
}

fun matchRpc(beginGen: Int, endGen: Int): Rpc {
val var351 = getSequenceElems(history, 406, listOf(407,4,411,4,161,412,4,282,4,162,4,419,4,161,412,4,282,4,162,4,423), beginGen, endGen)
val var352 = matchRpcName(var351[2].first, var351[2].second)
val var353 = history[var351[5].second].findByBeginGenOpt(102, 1, var351[5].first)
val var354 = history[var351[5].second].findByBeginGenOpt(413, 1, var351[5].first)
check(hasSingleTrue(var353 != null, var354 != null))
val var355 = when {
var353 != null -> null
else -> {
val var356 = getSequenceElems(history, 414, listOf(4,415), var351[5].first, var351[5].second)
"stream"
}
}
val var357 = matchMessageType(var351[7].first, var351[7].second)
val var358 = history[var351[14].second].findByBeginGenOpt(102, 1, var351[14].first)
val var359 = history[var351[14].second].findByBeginGenOpt(413, 1, var351[14].first)
check(hasSingleTrue(var358 != null, var359 != null))
val var360 = when {
var358 != null -> null
else -> {
val var361 = getSequenceElems(history, 414, listOf(4,415), var351[14].first, var351[14].second)
"stream"
}
}
val var362 = matchMessageType(var351[16].first, var351[16].second)
val var363 = matchRpcEnding(var351[20].first, var351[20].second)
val var364 = Rpc(var352, var355 != null, var357, var360 != null, var362, var363, nextId(), beginGen, endGen)
return var364
}

fun matchRpcName(beginGen: Int, endGen: Int): Ident {
val var365 = matchIdent(beginGen, endGen)
return var365
}

fun matchOptionName(beginGen: Int, endGen: Int): OptionName {
val var366 = getSequenceElems(history, 157, listOf(158,163), beginGen, endGen)
val var367 = history[var366[0].second].findByBeginGenOpt(133, 1, var366[0].first)
val var368 = history[var366[0].second].findByBeginGenOpt(159, 1, var366[0].first)
check(hasSingleTrue(var367 != null, var368 != null))
val var369 = when {
var367 != null -> {
val var370 = matchIdent(var366[0].first, var366[0].second)
var370
}
else -> {
val var371 = getSequenceElems(history, 160, listOf(161,4,131,4,162), var366[0].first, var366[0].second)
val var372 = matchFullIdent(var371[2].first, var371[2].second)
var372
}
}
val var373 = unrollRepeat0(history, 163, 165, 6, 164, var366[1].first, var366[1].second).map { k ->
val var374 = getSequenceElems(history, 166, listOf(4,149,133), k.first, k.second)
val var375 = matchIdent(var374[2].first, var374[2].second)
var375
}
val var376 = OptionName(var369, var373, nextId(), beginGen, endGen)
return var376
}

fun matchRpcEnding(beginGen: Int, endGen: Int): List<OptionDef?> {
val var377 = history[endGen].findByBeginGenOpt(67, 1, beginGen)
val var378 = history[endGen].findByBeginGenOpt(424, 4, beginGen)
check(hasSingleTrue(var377 != null, var378 != null))
val var379 = when {
var377 != null -> listOf()
else -> {
val var380 = getSequenceElems(history, 424, listOf(225,425,4,333), beginGen, endGen)
val var381 = unrollRepeat0(history, 425, 427, 6, 426, var380[1].first, var380[1].second).map { k ->
val var382 = getSequenceElems(history, 428, listOf(4,429), k.first, k.second)
val var383 = history[var382[1].second].findByBeginGenOpt(150, 1, var382[1].first)
val var384 = history[var382[1].second].findByBeginGenOpt(332, 1, var382[1].first)
check(hasSingleTrue(var383 != null, var384 != null))
val var385 = when {
var383 != null -> {
val var386 = matchOption(var382[1].first, var382[1].second)
var386
}
else -> null
}
var385
}
var381
}
}
return var379
}

fun matchFloatLit(beginGen: Int, endGen: Int): FloatLit {
val var387 = history[endGen].findByBeginGenOpt(200, 4, beginGen)
val var388 = history[endGen].findByBeginGenOpt(209, 2, beginGen)
val var389 = history[endGen].findByBeginGenOpt(210, 3, beginGen)
val var390 = history[endGen].findByBeginGenOpt(211, 1, beginGen)
val var391 = history[endGen].findByBeginGenOpt(213, 1, beginGen)
check(hasSingleTrue(var387 != null, var388 != null, var389 != null, var390 != null, var391 != null))
val var392 = when {
var387 != null -> {
val var393 = getSequenceElems(history, 200, listOf(201,149,204,205), beginGen, endGen)
val var394 = matchDecimals(var393[0].first, var393[0].second)
val var396 = history[var393[2].second].findByBeginGenOpt(102, 1, var393[2].first)
val var397 = history[var393[2].second].findByBeginGenOpt(201, 1, var393[2].first)
check(hasSingleTrue(var396 != null, var397 != null))
val var398 = when {
var396 != null -> null
else -> {
val var399 = matchDecimals(var393[2].first, var393[2].second)
var399
}
}
val var395 = var398
val var400 = history[var393[3].second].findByBeginGenOpt(102, 1, var393[3].first)
val var401 = history[var393[3].second].findByBeginGenOpt(206, 1, var393[3].first)
check(hasSingleTrue(var400 != null, var401 != null))
val var402 = when {
var400 != null -> null
else -> {
val var403 = matchExponent(var393[3].first, var393[3].second)
var403
}
}
val var404 = FloatLiteral(var394, (var395 ?: ""), var402, nextId(), beginGen, endGen)
var404
}
var388 != null -> {
val var405 = getSequenceElems(history, 209, listOf(201,206), beginGen, endGen)
val var406 = matchDecimals(var405[0].first, var405[0].second)
val var407 = matchExponent(var405[1].first, var405[1].second)
val var408 = FloatLiteral(var406, "", var407, nextId(), beginGen, endGen)
var408
}
var389 != null -> {
val var409 = getSequenceElems(history, 210, listOf(149,201,205), beginGen, endGen)
val var410 = matchDecimals(var409[1].first, var409[1].second)
val var411 = history[var409[2].second].findByBeginGenOpt(102, 1, var409[2].first)
val var412 = history[var409[2].second].findByBeginGenOpt(206, 1, var409[2].first)
check(hasSingleTrue(var411 != null, var412 != null))
val var413 = when {
var411 != null -> null
else -> {
val var414 = matchExponent(var409[2].first, var409[2].second)
var414
}
}
val var415 = FloatLiteral("", var410, var413, nextId(), beginGen, endGen)
var415
}
var390 != null -> {
val var416 = Inf(nextId(), beginGen, endGen)
var416
}
else -> {
val var417 = NaN(nextId(), beginGen, endGen)
var417
}
}
return var392
}

fun matchExponent(beginGen: Int, endGen: Int): Exponent {
val var418 = getSequenceElems(history, 207, listOf(208,176,201), beginGen, endGen)
val var419 = history[var418[1].second].findByBeginGenOpt(102, 1, var418[1].first)
val var420 = history[var418[1].second].findByBeginGenOpt(177, 1, var418[1].first)
check(hasSingleTrue(var419 != null, var420 != null))
val var421 = when {
var419 != null -> null
else -> {
val var422 = matchSign(var418[1].first, var418[1].second)
var422
}
}
val var423 = matchDecimals(var418[2].first, var418[2].second)
val var424 = Exponent(var421, var423, nextId(), beginGen, endGen)
return var424
}

fun matchDecimals(beginGen: Int, endGen: Int): String {
val var425 = unrollRepeat1(history, 202, 142, 142, 203, beginGen, endGen).map { k ->
val var426 = matchDecimalDigit(k.first, k.second)
var426
}
return var425.joinToString("") { it.toString() }
}

fun matchOctalLit(beginGen: Int, endGen: Int): OctalLit {
val var427 = getSequenceElems(history, 191, listOf(184,192), beginGen, endGen)
val var428 = unrollRepeat1(history, 192, 116, 116, 193, var427[1].first, var427[1].second).map { k ->
val var429 = matchOctalDigit(k.first, k.second)
var429
}
val var430 = OctalLit(var428.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var430
}

}
