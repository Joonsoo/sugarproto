package com.giyeok.sugarproto

import org.junit.jupiter.api.Test

class Test {
  @Test
  fun test() {
    val sourceText = javaClass.getResourceAsStream("/service.supro")!!.bufferedReader().readText()
    val parsed = SugarProtoParser.parse(sourceText)

    val gen = ProtoDefGenerator(parsed)
    gen.traverse()
    println(gen.generate())
  }
}
