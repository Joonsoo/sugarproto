package com.giyeok.sugarproto.sugarformat

import org.junit.jupiter.api.Test

class SugarFormatTest {
  @Test
  fun testHello() {
    val source = """
      world: hello
      hello:
      - "world"
      - "foo"
      - "bar"
    """.trimIndent()
    println(source)
    SugarFormat.merge(source, Test1.Hello.newBuilder())
  }

  @Test
  fun test123() {
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
        "key3".value1: 234
      logBlocks:
      - level: INFO
        timestamp: 2023-11-23T10:00:22
        message: "hello!"
      - level: WARN
        timestamp: 2023-11-23T10:00:30
        message: "world!\n"
                 "foo!\n"
                 "bar!"
      - level: ERROR
        timestamp: 2023-11-23T10:02:01
        message: "Hello!"
    """.trimIndent()

    println(source)
    val parsed = SugarFormatParser.parse(source)
    SugarFormat.Parser(ItemStructurizer(parsed)).test()
  }
}
