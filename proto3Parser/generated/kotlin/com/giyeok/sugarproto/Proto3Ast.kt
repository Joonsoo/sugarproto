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
    fun toShortString(): String
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
): MessageBodyElem, AstNode {
  override fun toShortString(): String = "MapField(keyType=${keyType}, valueType=${valueType}, mapName=${mapName}, number=${number}, options=${options})"
}

data class OneofField(
  val typ: Type,
  val name: Ident,
  val number: IntLit,
  val options: List<FieldOption>?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): OneOfElem, AstNode {
  override fun toShortString(): String = "OneofField(typ=${typ}, name=${name}, number=${number}, options=${options})"
}

data class OneofDef(
  val name: Ident,
  val elems: List<OneOfElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, AstNode {
  override fun toShortString(): String = "OneofDef(name=${name}, elems=${elems})"
}

sealed interface OneOfElem: AstNode

data class OctalEscape(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode {
  override fun toShortString(): String = "OctalEscape(value=${value})"
}

data class Ranges(
  val values: List<Range>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ReservedBody, AstNode {
  override fun toShortString(): String = "Ranges(values=${values})"
}

data class EnumDef(
  val name: Ident,
  val body: List<EnumBodyElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, TopLevelDef, AstNode {
  override fun toShortString(): String = "EnumDef(name=${name}, body=${body})"
}

data class RangeEndValue(
  val value: IntLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): RangeEnd, AstNode {
  override fun toShortString(): String = "RangeEndValue(value=${value})"
}

data class SingleQuoteStrLit(
  val value: List<CharValue>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StrLit, AstNode {
  override fun toShortString(): String = "SingleQuoteStrLit(value=${value})"
}

data class FloatConstant(
  val sign: Sign?,
  val value: FloatLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode {
  override fun toShortString(): String = "FloatConstant(sign=${sign}, value=${value})"
}

data class EnumType(
  val firstDot: Boolean,
  val parent: List<Ident>,
  val name: Ident,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "EnumType(firstDot=${firstDot}, parent=${parent}, name=${name})"
}

data class StringConstant(
  val value: StrLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode {
  override fun toShortString(): String = "StringConstant(value=${value})"
}

data class Proto3(
  val defs: List<ProtoDefElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "Proto3(defs=${defs})"
}

data class NaN(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): FloatLit, AstNode {
  override fun toShortString(): String = "NaN()"
}

data class FullIdent(
  val names: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, OptionScope, AstNode {
  override fun toShortString(): String = "FullIdent(names=${names})"
}

data class Inf(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): FloatLit, AstNode {
  override fun toShortString(): String = "Inf()"
}

sealed interface IntLit: AstNode

data class RangeEndMax(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): RangeEnd, AstNode {
  override fun toShortString(): String = "RangeEndMax()"
}

data class EnumFieldDef(
  val name: Ident,
  val minus: Boolean,
  val value: IntLit,
  val options: List<EnumValueOption>?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): EnumBodyElem, AstNode {
  override fun toShortString(): String = "EnumFieldDef(name=${name}, minus=${minus}, value=${value}, options=${options})"
}

sealed interface Constant: AstNode

data class FieldOption(
  val name: OptionName,
  val value: Constant,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "FieldOption(name=${name}, value=${value})"
}

data class FloatLiteral(
  val integral: String,
  val fractional: String,
  val exponent: Exponent?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): FloatLit, AstNode {
  override fun toShortString(): String = "FloatLiteral(integral=${integral}, fractional=${fractional}, exponent=${exponent})"
}

data class OptionDef(
  val name: OptionName,
  val value: Constant,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): EnumBodyElem, MessageBodyElem, OneOfElem, ProtoDefElem, ServiceBodyElem, AstNode {
  override fun toShortString(): String = "OptionDef(name=${name}, value=${value})"
}

data class HexLit(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLit, AstNode {
  override fun toShortString(): String = "HexLit(value=${value})"
}

sealed interface ProtoDefElem: AstNode

sealed interface ServiceBodyElem: AstNode

data class CharEscape(
  val value: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode {
  override fun toShortString(): String = "CharEscape(value=${value})"
}

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
): ServiceBodyElem, AstNode {
  override fun toShortString(): String = "Rpc(name=${name}, isInputStream=${isInputStream}, inputType=${inputType}, isOutputStream=${isOutputStream}, outputType=${outputType}, options=${options})"
}

data class FieldNames(
  val names: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ReservedBody, AstNode {
  override fun toShortString(): String = "FieldNames(names=${names})"
}

data class OptionName(
  val scope: OptionScope,
  val name: List<Ident>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "OptionName(scope=${scope}, name=${name})"
}

data class MessageType(
  val firstDot: Boolean,
  val parent: List<Ident>,
  val name: Ident,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "MessageType(firstDot=${firstDot}, parent=${parent}, name=${name})"
}

data class EmptyStatement(

  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): EnumBodyElem, MessageBodyElem, OneOfElem, ProtoDefElem, ServiceBodyElem, AstNode {
  override fun toShortString(): String = "EmptyStatement()"
}

data class Service(
  val name: Ident,
  val body: List<ServiceBodyElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): TopLevelDef, AstNode {
  override fun toShortString(): String = "Service(name=${name}, body=${body})"
}

data class Character(
  val value: Char,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode {
  override fun toShortString(): String = "Character(value=${value})"
}

data class IntConstant(
  val sign: Sign?,
  val value: IntLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode {
  override fun toShortString(): String = "IntConstant(sign=${sign}, value=${value})"
}

data class MessageOrEnumType(
  val name: MessageType,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode {
  override fun toShortString(): String = "MessageOrEnumType(name=${name})"
}

data class DecimalLit(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLit, AstNode {
  override fun toShortString(): String = "DecimalLit(value=${value})"
}

data class Field(
  val modifier: FieldModifier?,
  val typ: Type,
  val name: Ident,
  val fieldNumber: IntLit,
  val options: List<FieldOption>?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, AstNode {
  override fun toShortString(): String = "Field(modifier=${modifier}, typ=${typ}, name=${name}, fieldNumber=${fieldNumber}, options=${options})"
}

data class Package(
  val name: FullIdent,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ProtoDefElem, AstNode {
  override fun toShortString(): String = "Package(name=${name})"
}

sealed interface ReservedBody: AstNode

sealed interface StrLit: AstNode

sealed interface FloatLit: AstNode

data class Import(
  val importType: ImportType?,
  val target: StrLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): ProtoDefElem, AstNode {
  override fun toShortString(): String = "Import(importType=${importType}, target=${target})"
}

sealed interface RangeEnd: AstNode

data class BoolConstant(
  val value: BoolLit,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Constant, AstNode {
  override fun toShortString(): String = "BoolConstant(value=${value})"
}

sealed interface MessageBodyElem: AstNode

sealed interface OptionScope: AstNode

data class Message(
  val name: Ident,
  val body: List<MessageBodyElem>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, TopLevelDef, AstNode {
  override fun toShortString(): String = "Message(name=${name}, body=${body})"
}

data class Reserved(
  val value: ReservedBody,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): MessageBodyElem, AstNode {
  override fun toShortString(): String = "Reserved(value=${value})"
}

sealed interface CharValue: AstNode

data class DoubleQuoteStrLit(
  val value: List<CharValue>,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): StrLit, AstNode {
  override fun toShortString(): String = "DoubleQuoteStrLit(value=${value})"
}

data class Exponent(
  val sign: Sign?,
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "Exponent(sign=${sign}, value=${value})"
}

data class Ident(
  val name: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): OptionScope, AstNode {
  override fun toShortString(): String = "Ident(name=${name})"
}

data class EnumValueOption(
  val name: OptionName,
  val value: Constant,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "EnumValueOption(name=${name}, value=${value})"
}

data class BuiltinType(
  val typ: BuiltinTypeEnum,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): Type, AstNode {
  override fun toShortString(): String = "BuiltinType(typ=${typ})"
}

sealed interface TopLevelDef: ProtoDefElem, AstNode

data class OctalLit(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): IntLit, AstNode {
  override fun toShortString(): String = "OctalLit(value=${value})"
}

sealed interface Type: AstNode

data class Range(
  val rangeStart: IntLit,
  val rangeEnd: RangeEnd?,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): AstNode {
  override fun toShortString(): String = "Range(rangeStart=${rangeStart}, rangeEnd=${rangeEnd})"
}

data class HexEscape(
  val value: String,
  override val nodeId: Int,
  override val start: Int,
  override val end: Int,
): CharValue, AstNode {
  override fun toShortString(): String = "HexEscape(value=${value})"
}
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
val var329 = matchIdent(var328[0].first, var328[0].second)
var329
}
val var330 = matchMessageName(var323[2].first, var323[2].second)
val var331 = MessageType(var326 != null, var327, var330, nextId(), beginGen, endGen)
return var331
}

fun matchBuiltinType(beginGen: Int, endGen: Int): BuiltinTypeEnum {
val var332 = history[endGen].findByBeginGenOpt(248, 1, beginGen)
val var333 = history[endGen].findByBeginGenOpt(250, 1, beginGen)
val var334 = history[endGen].findByBeginGenOpt(252, 1, beginGen)
val var335 = history[endGen].findByBeginGenOpt(255, 1, beginGen)
val var336 = history[endGen].findByBeginGenOpt(259, 1, beginGen)
val var337 = history[endGen].findByBeginGenOpt(261, 1, beginGen)
val var338 = history[endGen].findByBeginGenOpt(263, 1, beginGen)
val var339 = history[endGen].findByBeginGenOpt(265, 1, beginGen)
val var340 = history[endGen].findByBeginGenOpt(267, 1, beginGen)
val var341 = history[endGen].findByBeginGenOpt(269, 1, beginGen)
val var342 = history[endGen].findByBeginGenOpt(271, 1, beginGen)
val var343 = history[endGen].findByBeginGenOpt(273, 1, beginGen)
val var344 = history[endGen].findByBeginGenOpt(275, 1, beginGen)
val var345 = history[endGen].findByBeginGenOpt(277, 1, beginGen)
val var346 = history[endGen].findByBeginGenOpt(279, 1, beginGen)
check(hasSingleTrue(var332 != null, var333 != null, var334 != null, var335 != null, var336 != null, var337 != null, var338 != null, var339 != null, var340 != null, var341 != null, var342 != null, var343 != null, var344 != null, var345 != null, var346 != null))
val var347 = when {
var332 != null -> BuiltinTypeEnum.DOUBLE
var333 != null -> BuiltinTypeEnum.FLOAT
var334 != null -> BuiltinTypeEnum.INT32
var335 != null -> BuiltinTypeEnum.INT64
var336 != null -> BuiltinTypeEnum.UINT32
var337 != null -> BuiltinTypeEnum.UINT64
var338 != null -> BuiltinTypeEnum.SINT32
var339 != null -> BuiltinTypeEnum.SINT64
var340 != null -> BuiltinTypeEnum.FIXED32
var341 != null -> BuiltinTypeEnum.FIXED64
var342 != null -> BuiltinTypeEnum.SFIXED32
var343 != null -> BuiltinTypeEnum.SFIXED64
var344 != null -> BuiltinTypeEnum.BOOL
var345 != null -> BuiltinTypeEnum.STRING
else -> BuiltinTypeEnum.BYTES
}
return var347
}

fun matchDecimalLit(beginGen: Int, endGen: Int): DecimalLit {
val var348 = getSequenceElems(history, 186, listOf(187,188), beginGen, endGen)
val var349 = unrollRepeat0(history, 188, 142, 6, 189, var348[1].first, var348[1].second).map { k ->
val var350 = matchDecimalDigit(k.first, k.second)
var350
}
val var351 = DecimalLit(source[var348[0].first].toString() + var349.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var351
}

fun matchRpc(beginGen: Int, endGen: Int): Rpc {
val var352 = getSequenceElems(history, 406, listOf(407,4,411,4,161,412,4,282,4,162,4,419,4,161,412,4,282,4,162,4,423), beginGen, endGen)
val var353 = matchRpcName(var352[2].first, var352[2].second)
val var354 = history[var352[5].second].findByBeginGenOpt(102, 1, var352[5].first)
val var355 = history[var352[5].second].findByBeginGenOpt(413, 1, var352[5].first)
check(hasSingleTrue(var354 != null, var355 != null))
val var356 = when {
var354 != null -> null
else -> {
val var357 = getSequenceElems(history, 414, listOf(4,415), var352[5].first, var352[5].second)
"stream"
}
}
val var358 = matchMessageType(var352[7].first, var352[7].second)
val var359 = history[var352[14].second].findByBeginGenOpt(102, 1, var352[14].first)
val var360 = history[var352[14].second].findByBeginGenOpt(413, 1, var352[14].first)
check(hasSingleTrue(var359 != null, var360 != null))
val var361 = when {
var359 != null -> null
else -> {
val var362 = getSequenceElems(history, 414, listOf(4,415), var352[14].first, var352[14].second)
"stream"
}
}
val var363 = matchMessageType(var352[16].first, var352[16].second)
val var364 = matchRpcEnding(var352[20].first, var352[20].second)
val var365 = Rpc(var353, var356 != null, var358, var361 != null, var363, var364, nextId(), beginGen, endGen)
return var365
}

fun matchRpcName(beginGen: Int, endGen: Int): Ident {
val var366 = matchIdent(beginGen, endGen)
return var366
}

fun matchOptionName(beginGen: Int, endGen: Int): OptionName {
val var367 = getSequenceElems(history, 157, listOf(158,163), beginGen, endGen)
val var368 = history[var367[0].second].findByBeginGenOpt(133, 1, var367[0].first)
val var369 = history[var367[0].second].findByBeginGenOpt(159, 1, var367[0].first)
check(hasSingleTrue(var368 != null, var369 != null))
val var370 = when {
var368 != null -> {
val var371 = matchIdent(var367[0].first, var367[0].second)
var371
}
else -> {
val var372 = getSequenceElems(history, 160, listOf(161,4,131,4,162), var367[0].first, var367[0].second)
val var373 = matchFullIdent(var372[2].first, var372[2].second)
var373
}
}
val var374 = unrollRepeat0(history, 163, 165, 6, 164, var367[1].first, var367[1].second).map { k ->
val var375 = getSequenceElems(history, 166, listOf(4,149,133), k.first, k.second)
val var376 = matchIdent(var375[2].first, var375[2].second)
var376
}
val var377 = OptionName(var370, var374, nextId(), beginGen, endGen)
return var377
}

fun matchRpcEnding(beginGen: Int, endGen: Int): List<OptionDef?> {
val var378 = history[endGen].findByBeginGenOpt(67, 1, beginGen)
val var379 = history[endGen].findByBeginGenOpt(424, 4, beginGen)
check(hasSingleTrue(var378 != null, var379 != null))
val var380 = when {
var378 != null -> listOf()
else -> {
val var381 = getSequenceElems(history, 424, listOf(225,425,4,333), beginGen, endGen)
val var382 = unrollRepeat0(history, 425, 427, 6, 426, var381[1].first, var381[1].second).map { k ->
val var383 = getSequenceElems(history, 428, listOf(4,429), k.first, k.second)
val var384 = history[var383[1].second].findByBeginGenOpt(150, 1, var383[1].first)
val var385 = history[var383[1].second].findByBeginGenOpt(332, 1, var383[1].first)
check(hasSingleTrue(var384 != null, var385 != null))
val var386 = when {
var384 != null -> {
val var387 = matchOption(var383[1].first, var383[1].second)
var387
}
else -> null
}
var386
}
var382
}
}
return var380
}

fun matchFloatLit(beginGen: Int, endGen: Int): FloatLit {
val var388 = history[endGen].findByBeginGenOpt(200, 4, beginGen)
val var389 = history[endGen].findByBeginGenOpt(209, 2, beginGen)
val var390 = history[endGen].findByBeginGenOpt(210, 3, beginGen)
val var391 = history[endGen].findByBeginGenOpt(211, 1, beginGen)
val var392 = history[endGen].findByBeginGenOpt(213, 1, beginGen)
check(hasSingleTrue(var388 != null, var389 != null, var390 != null, var391 != null, var392 != null))
val var393 = when {
var388 != null -> {
val var394 = getSequenceElems(history, 200, listOf(201,149,204,205), beginGen, endGen)
val var395 = matchDecimals(var394[0].first, var394[0].second)
val var397 = history[var394[2].second].findByBeginGenOpt(102, 1, var394[2].first)
val var398 = history[var394[2].second].findByBeginGenOpt(201, 1, var394[2].first)
check(hasSingleTrue(var397 != null, var398 != null))
val var399 = when {
var397 != null -> null
else -> {
val var400 = matchDecimals(var394[2].first, var394[2].second)
var400
}
}
val var396 = var399
val var401 = history[var394[3].second].findByBeginGenOpt(102, 1, var394[3].first)
val var402 = history[var394[3].second].findByBeginGenOpt(206, 1, var394[3].first)
check(hasSingleTrue(var401 != null, var402 != null))
val var403 = when {
var401 != null -> null
else -> {
val var404 = matchExponent(var394[3].first, var394[3].second)
var404
}
}
val var405 = FloatLiteral(var395, (var396 ?: ""), var403, nextId(), beginGen, endGen)
var405
}
var389 != null -> {
val var406 = getSequenceElems(history, 209, listOf(201,206), beginGen, endGen)
val var407 = matchDecimals(var406[0].first, var406[0].second)
val var408 = matchExponent(var406[1].first, var406[1].second)
val var409 = FloatLiteral(var407, "", var408, nextId(), beginGen, endGen)
var409
}
var390 != null -> {
val var410 = getSequenceElems(history, 210, listOf(149,201,205), beginGen, endGen)
val var411 = matchDecimals(var410[1].first, var410[1].second)
val var412 = history[var410[2].second].findByBeginGenOpt(102, 1, var410[2].first)
val var413 = history[var410[2].second].findByBeginGenOpt(206, 1, var410[2].first)
check(hasSingleTrue(var412 != null, var413 != null))
val var414 = when {
var412 != null -> null
else -> {
val var415 = matchExponent(var410[2].first, var410[2].second)
var415
}
}
val var416 = FloatLiteral("", var411, var414, nextId(), beginGen, endGen)
var416
}
var391 != null -> {
val var417 = Inf(nextId(), beginGen, endGen)
var417
}
else -> {
val var418 = NaN(nextId(), beginGen, endGen)
var418
}
}
return var393
}

fun matchExponent(beginGen: Int, endGen: Int): Exponent {
val var419 = getSequenceElems(history, 207, listOf(208,176,201), beginGen, endGen)
val var420 = history[var419[1].second].findByBeginGenOpt(102, 1, var419[1].first)
val var421 = history[var419[1].second].findByBeginGenOpt(177, 1, var419[1].first)
check(hasSingleTrue(var420 != null, var421 != null))
val var422 = when {
var420 != null -> null
else -> {
val var423 = matchSign(var419[1].first, var419[1].second)
var423
}
}
val var424 = matchDecimals(var419[2].first, var419[2].second)
val var425 = Exponent(var422, var424, nextId(), beginGen, endGen)
return var425
}

fun matchDecimals(beginGen: Int, endGen: Int): String {
val var426 = unrollRepeat1(history, 202, 142, 142, 203, beginGen, endGen).map { k ->
val var427 = matchDecimalDigit(k.first, k.second)
var427
}
return var426.joinToString("") { it.toString() }
}

fun matchOctalLit(beginGen: Int, endGen: Int): OctalLit {
val var428 = getSequenceElems(history, 191, listOf(184,192), beginGen, endGen)
val var429 = unrollRepeat1(history, 192, 116, 116, 193, var428[1].first, var428[1].second).map { k ->
val var430 = matchOctalDigit(k.first, k.second)
var430
}
val var431 = OctalLit(var429.joinToString("") { it.toString() }, nextId(), beginGen, endGen)
return var431
}

}
