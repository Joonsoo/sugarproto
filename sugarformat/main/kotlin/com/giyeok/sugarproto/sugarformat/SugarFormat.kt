package com.giyeok.sugarproto.sugarformat

import com.google.protobuf.Message
import com.google.protobuf.MessageOrBuilder

object SugarFormat {
  fun print(message: MessageOrBuilder): String {
    val writer = CodeWriter()
    SugarFormatPrinterImpl(writer).print(message)
    return writer.toString()
  }

  fun <T: Message.Builder> merge(sufText: String, builder: T): T {
    val ast = SugarFormatParser.parse(sufText)
    val parsed = SugarFormatParserImpl(ItemStructure(ast)).parse(builder.descriptorForType)
    parsed.mergeTo(builder)
    return builder
  }
}
