package com.giyeok.sugarproto.sugarformat

import com.google.protobuf.util.JsonFormat
import org.junit.jupiter.api.Test

class SugarFormatTest {
  @Test
  fun test() {
    val source = """
      hello::
        world: "message"
      expire: "3h"
      numberValue: 123
      numberValue: 0123
      numberValue: 0x123
      enumValue: INFO
    """.trimIndent()

    println(source)
    val parsed = SugarFormatParser.parse(source)

    println(parsed)
  }

  @Test
  fun test2() {
    val source = """
      hello: "good"
      hello: "message"
      world::
        a: "abc"
        b: "def"
        c:: 
          e: "xyz"
        d::
          e: "asdf"
        d::
          e: "qwer"
      world::
        a: "abc"
        b: "def"
        c:: 
          e: "xyz"
        d::
          e: "asdf"
        d::
          e: "qwer"
    """.trimIndent()

    println(source)
    val parsed = SugarFormatParser.parse(source)

    println(parsed)
  }

  @Test
  fun testSimpleObjects() {
    val source = """
      simple: { a: 1, b: 2 }
      simple: { a: 1 b: 2 }
    """.trimIndent()

    println(source)
    val parsed = SugarFormatParser.parse(source)

    println(parsed)
  }

  @Test
  fun testSimpleLists() {
    val source = """
      list1: ["hello", "world"]
      list2: [123, 456]
      list3: [{a:"hello"}, {b:"world"}]
    """.trimIndent()

    println(source)
    val parsed = SugarFormatParser.parse(source)

    println(parsed)
  }

  @Test
  fun testMaps() {
    val source = """
      myMap{}
        "simple"::
          a: "hello"
          b: "bar"
        "simple": { a: "foo" b: "bar" c: "foo" "bar" }
    """.trimIndent()

    println(source)
    val parsed = SugarFormatParser.parse(source)

    println(parsed)
  }

  @Test
  fun testDuration() {
    val duration = SugarFormatParser.parseDuration("3d 1h 3.2s")
    println(duration)

    val dur = SugarFormatParser.convertDuration("3d 1h 3.2s")
    println(JsonFormat.printer().print(dur))
  }
}
