package com.giyeok.sugarproto.test

import com.giyeok.sugarproto.SugarProtoParser
import com.giyeok.sugarproto.proto.DefTraverser
import com.giyeok.sugarproto.proto.ProtoGen
import org.junit.jupiter.api.Test

class Test {
  @Test
  fun test() {
    val sourceText = javaClass.getResourceAsStream("/world.supro")!!.bufferedReader().readText()
    val parsed = SugarProtoParser.parse(sourceText)

    val trav = DefTraverser(parsed).traverse()
    val proto = ProtoGen().generate(trav)
    println(proto)
  }
}
