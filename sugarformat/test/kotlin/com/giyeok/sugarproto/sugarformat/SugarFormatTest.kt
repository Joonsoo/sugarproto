package com.giyeok.sugarproto.sugarformat

import com.google.common.truth.Truth.assertThat
import com.google.protobuf.util.JsonFormat
import org.junit.jupiter.api.Test

class SugarFormatTest {
  @Test
  fun test() {
    val source = """
      hello:
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
      world:
        a: "abc"
        b: "def"
        c: 
          e: "xyz"
        d:
          e: "asdf"
        d:
          e: "qwer"
      world:
        a: "abc"
        b: "def"
        c: 
          e: "xyz"
        d:
          e: "asdf"
        d:
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
      simple: {a:{b:1 d:2},c:{}}
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
      myMap:
        "simple1":
          a: "hello"
          b: "bar"
        "simple2": { a: "foo" b: "bar" c: "foo" "bar" }
        "simple3":
          - 123
          - 456
          - good:
            - hello: "123"
        "simple4":
          "simple4-1": "hello"
    """.trimIndent()

    println(source)
    val parsed = SugarFormatParser.parse(source)

    println(parsed)
  }

  @Test
  fun testBibixConfig() {
    val source = """
      threadCounts: 8
      minLogLevel: INFO
      targetValueExpireTime: 1h
      pluginConfigs:
        "com.giyeok.bibix.prelude:git":
          expireTime: "30m"
    """.trimIndent()

    println(source)
    val parsed = SugarFormatParser.parse(source)

    println(parsed)
  }

  @Test
  fun testComments() {
    val source = """
      threadCounts: 8  # comment
      
      
      minLogLevel: INFO  # comment
      
        # empty line with comment
      
      targetValueExpireTime: 1h 30m
      baseTime: 2023-11-12T00:00:00Z
      pluginConfigs:
        "com.giyeok.bibix.prelude:git":  # another comment
    """.trimIndent()

    println(source)
    val parsed = SugarFormatParser.parse(source)

    println(parsed)
  }

  @Test
  fun testDuration() {
    val duration = SugarFormatParser.parseDuration("3d 1h 3.2s")
    println(duration)

    println(convertToJsonFormat("3d 1h 3.2s"))
    assertThat(convertToJsonFormat("1h")).isEqualTo("3600s")
    assertThat(convertToJsonFormat("1m")).isEqualTo("60s")
    assertThat(convertToJsonFormat("1h 30m")).isEqualTo("5400s")
    assertThat(convertToJsonFormat("1s")).isEqualTo("1s")
    assertThat(convertToJsonFormat("0.000000001s")).isEqualTo("0.000000001s")
    assertThat(convertToJsonFormat("123.456s")).isEqualTo("123.456s")
    assertThat(convertToJsonFormat("1d")).isEqualTo("86400s")
  }

  private fun convertToJsonFormat(durationString: String): String {
    val json = JsonFormat.printer().print(SugarFormatParser.convertDuration(durationString))
    check(json.first() == '"' && json.last() == '"')
    return json.substring(1, json.length - 1)
  }
}
