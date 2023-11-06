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
      - "bar good"
      # hello: [abc, def]
      timestamp: 2023-11-06T09:15Z
      duration: 30h30.00144s

      rep_timestamp:
      - 2023-11-06T09:15Z
      - 2023-11-06T09:15Z
      - 2023-11-06T09:15Z
      - 2023-11-06T09:15Z
      rep_duration:
      - 30h30.00144s
      - 60h30.00144s
      - 90h30.00144s
      
      #names_map.hello: abc
      greeting.greeting: "Hello!"
      greetings:
      - greeting: "안녕하세요"
      - greeting: "곤니찌와"
      - greeting: "니하오"
    """.trimIndent()
    println(source)
    println()

    val items = SugarFormatParser.parse(source)
    val parsed = SugarFormatParserImpl(ItemStructure(items))
      .parse(Test1.Hello.getDescriptor())
    // println(parsed)

    println("=====")

    val builder = Test1.Hello.newBuilder()
    parsed.mergeTo(builder)
    println(builder.build())

    println("=====")

    println(SugarFormat.print(builder))
  }
}
