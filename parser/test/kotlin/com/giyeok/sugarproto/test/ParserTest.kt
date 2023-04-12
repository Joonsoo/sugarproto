package com.giyeok.sugarproto.test

import com.giyeok.sugarproto.SugarProtoParser
import org.junit.jupiter.api.Test

class ParserTest {
  @Test
  fun test() {
    val source = """
    package abc.def
    
    import "google/protobuf/empty.proto"
    
    service Abc {
      rpc hello: {1 message: string} -> {1 echo: string}
    }
  """.trimIndent()

    val parsed = SugarProtoParser.parse(source)
    println(parsed)
  }
}
