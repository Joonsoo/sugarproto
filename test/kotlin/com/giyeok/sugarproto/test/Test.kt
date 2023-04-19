package com.giyeok.sugarproto.test

import com.giyeok.sugarproto.SugarProtoParser
import com.giyeok.sugarproto.mutkt.MutableKotlinDefConverter
import com.giyeok.sugarproto.mutkt.MutableKtDataClassGen
import com.giyeok.sugarproto.proto.ProtoDefTraverser
import com.giyeok.sugarproto.proto.ProtoGen
import org.junit.jupiter.api.Test

class Test {
  @Test
  fun test() {
    val sourceText = javaClass.getResourceAsStream("/test.supro")!!.bufferedReader().readText()
    val parsed = SugarProtoParser.parse(sourceText)

    val defs = ProtoDefTraverser(parsed).traverse()

    val proto = ProtoGen().generate(defs)
    println(proto)

    val ktDefs = MutableKotlinDefConverter(defs).convert()
    val kt =
      MutableKtDataClassGen(ktDefs, protoOuterClassName = "WorldProto.", gdxMode = true).generate()
    println(kt)
  }
}
