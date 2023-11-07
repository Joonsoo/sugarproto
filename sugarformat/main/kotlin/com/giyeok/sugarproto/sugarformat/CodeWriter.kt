package com.giyeok.sugarproto.sugarformat

class CodeWriter(val defaultIndentDepth: String = "  ") {
  private var indent = ""
  private val stringBuilder = StringBuilder()

  fun writeLine() {
    stringBuilder.append('\n')
  }

  fun writeLine(line: String) {
    stringBuilder.append(indent)
    stringBuilder.append(line)
    stringBuilder.append('\n')
  }

  fun writeLineIndented(line: String) {
    indent {
      writeLine(line)
    }
  }

  fun indent(body: () -> Unit) {
    indent(defaultIndentDepth, body)
  }

  fun indent(indentDepth: String, body: () -> Unit) {
    val prevIndent = indent
    indent += indentDepth
    body()
    indent = prevIndent
  }

  open fun clear() {
    indent = ""
    stringBuilder.clear()
  }

  open fun hasContent(): Boolean = stringBuilder.toString().isNotEmpty()

  override fun toString(): String = stringBuilder.toString()
}
