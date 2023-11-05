package com.giyeok.sugarproto.sugarformat

import org.junit.jupiter.api.Test

class SugarFormatParserTest {
  @Test
  fun test() {
    val source = """
      hello:
        world: "message"
        foo:
          bar: foofoo
      expire: "3h"
      numberValue: 123
      numberValue: 0123
      numberValue: 0x123
      enumValue: INFO
      list:
      - "hello"
      - "world"
      dict:
        "key1": "value1"
        "key2":
          value1: 123
    """.trimIndent()

    println(source)
    val parsed = SugarFormatParser.parse(source)

    println(parsed)
    SugarFormat.Parser(ItemStructurizer(parsed)).test()
  }
}
