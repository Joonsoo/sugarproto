package com.giyeok.sugarproto.sugarformat

class CodeWriter {
  private var indentLevel = 0
  private val stringBuilder = StringBuilder()

  fun writeLine() {
    stringBuilder.append('\n')
  }

  fun writeLine(line: String) {
    stringBuilder.append((0 until indentLevel).joinToString("") { "  " })
    stringBuilder.append(line)
    stringBuilder.append('\n')
  }

  fun writeLineIndented(line: String) {
    indent {
      writeLine(line)
    }
  }

  fun indent(body: () -> Unit) {
    indentLevel += 1
    body()
    indentLevel -= 1
  }

  open fun clear() {
    indentLevel = 0
    stringBuilder.clear()
  }

  open fun hasContent(): Boolean = stringBuilder.toString().isNotEmpty()

  override fun toString(): String = stringBuilder.toString()
}
