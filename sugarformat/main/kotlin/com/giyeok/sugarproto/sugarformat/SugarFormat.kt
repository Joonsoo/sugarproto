package com.giyeok.sugarproto.sugarformat

import com.google.protobuf.Message
import com.google.protobuf.MessageOrBuilder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import java.nio.charset.Charset

object SugarFormat {
  fun print(message: MessageOrBuilder): String =
    StringWriter().use { writer ->
      printTo(message, writer)
      writer.toString()
    }

  fun printTo(message: MessageOrBuilder, writer: Writer) {
    SugarFormatPrinterImpl2(CodeWriter(writer)).print(message)
  }

  fun <T: Message.Builder> merge(sufText: String, builder: T): T {
    val ast = SugarFormatParser.parse(sufText)
    if (ast.isEmpty()) {
      return builder
    }
    val parsed = SugarFormatParserImpl(ItemStructure(ast))
      .parse(builder.descriptorForType)
    parsed.mergeTo(builder)
    return builder
  }
}
