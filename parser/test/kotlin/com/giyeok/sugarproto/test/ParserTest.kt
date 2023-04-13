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

  @Test
  fun testEnum() {
    val source = """
      enum Hello {
        -1 world
        2 best
      }
  """.trimIndent()

    val parsed = SugarProtoParser.parse(source)
    println(parsed)
  }

  @Test
  fun testMessage() {
    val source = """
      message Message {
        option hello = true
        
        enum Hello {
          -1 world
          2 best
        }
        
        message NestedMessage {
          1 hello: sint64
        }
        
        reserved(1, 2, 3..5, 8..max)
      }
  """.trimIndent()

    val parsed = SugarProtoParser.parse(source)
    println(parsed)
  }
}
