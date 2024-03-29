package com.giyeok.sugarproto

import com.giyeok.sugarproto.SugarProtoParser
import com.giyeok.sugarproto.mutkt.MutableKotlinDefConverter
import com.giyeok.sugarproto.mutkt.MutableKtDataClassGen
import com.giyeok.sugarproto.proto.ImportProtoFromResourcesProvider
import com.giyeok.sugarproto.proto.ProtoDefTraverser
import com.giyeok.sugarproto.proto.ProtoGen
import org.junit.jupiter.api.Test

class Test {
  @Test
  fun test() {
    val sourceText = javaClass.getResourceAsStream("/test.supro")!!.bufferedReader().readText()
    val parsed = SugarProtoParser.parse(sourceText)

    val defs = ProtoDefTraverser(parsed, ImportProtoFromResourcesProvider()).traverse()

    val proto = ProtoGen().generate(defs)
    println(proto)

    val ktDefs = MutableKotlinDefConverter(defs).convert()
    val kt = MutableKtDataClassGen(ktDefs, gdxMode = true).generate()
    println(kt)
  }

  @Test
  fun testTypeConversions() {
    val sourceText = javaClass.getResourceAsStream("/type_conversions.supro")!!.bufferedReader().readText()
    val parsed = SugarProtoParser.parse(sourceText)

    val defs = ProtoDefTraverser(parsed, ImportProtoFromResourcesProvider()).traverse()

    val proto = ProtoGen().generate(defs)
    println(proto)

    val ktDefs = MutableKotlinDefConverter(defs).convert()
    val kt = MutableKtDataClassGen(ktDefs, gdxMode = true).generate()
    println(kt)
  }

  @Test
  fun testDeepImport() {
    val sourceText = javaClass.getResourceAsStream("/deep_import.supro")!!.bufferedReader().readText()
    val parsed = SugarProtoParser.parse(sourceText)

    val defs = ProtoDefTraverser(parsed, ImportProtoFromResourcesProvider()).traverse()

    val proto = ProtoGen().generate(defs)
    println(proto)
  }
}
