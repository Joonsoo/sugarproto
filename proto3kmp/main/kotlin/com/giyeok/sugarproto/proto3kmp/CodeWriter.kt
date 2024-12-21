package com.giyeok.sugarproto.proto3kmp

class CodeWriter {
  private var indentLevel = 0
  private val stringBuilder = StringBuilder()
  private val imports = mutableSetOf<String>()

  fun addImport(cls: String) {
    imports.add(cls)
  }

  fun writeLine() {
    stringBuilder.append('\n')
  }

  fun writeLine(line: String) {
    stringBuilder.append((0 until indentLevel).joinToString("") { "  " })
    stringBuilder.append(line)
    stringBuilder.append('\n')
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

  override fun toString(): String =
    imports.sorted().joinToString("\n") { "import $it" } + "\n\n" + stringBuilder.toString()
}
