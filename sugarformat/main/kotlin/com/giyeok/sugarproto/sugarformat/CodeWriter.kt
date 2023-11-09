package com.giyeok.sugarproto.sugarformat

import java.io.Writer

class CodeWriter(
  val writer: Writer,
  val defaultIndentDepth: String = "  "
) {
  private var indent = ""

  fun writeLine() {
    writer.append('\n')
  }

  fun writeLine(line: String) {
    writer.append(indent)
    writer.append(line)
    writer.append('\n')
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
}
