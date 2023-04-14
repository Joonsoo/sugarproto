package com.giyeok.sugarproto

fun SugarProtoAst.StringLiteral.toValue(): String {
  val builder = StringBuilder()
  this.singles.forEach { single ->
    single.value.forEach { charValue ->
      when (charValue) {
        is SugarProtoAst.PlainChar -> builder.append(charValue.value)
        is SugarProtoAst.CharEscape -> TODO()
        is SugarProtoAst.HexEscape -> TODO()
        is SugarProtoAst.OctEscape -> TODO()
        is SugarProtoAst.UnicodeEscape -> TODO()
        is SugarProtoAst.UnicodeLongEscape -> TODO()
      }
    }
  }
  return builder.toString()
}

fun SugarProtoAst.IntLiteral.toValueString(): String = when (this) {
  is SugarProtoAst.ZeroIntLiteral -> "0"
  is SugarProtoAst.DecimalLiteral -> this.value
  is SugarProtoAst.HexLiteral -> "0x${this.value}"
  is SugarProtoAst.OctalLiteral -> "0${this.value}"
}
