package com.giyeok.sugarproto.proto3kmp

import com.giyeok.sugarproto.Proto3Parser
import org.junit.jupiter.api.Test
import java.io.File

class A {
  @Test
  fun test() {
    val protodef = Proto3Parser.parse(File("proto3kmp/grpcresearch/proto/test.proto").readText())
    val compiler = Proto3ToKMPCompiler(ProtoNames(), protodef)

    val result = compiler.generateDefs()
    println(result)

    val codeWriter = CodeWriter()
    for (service in result.services) {
      KMPCodeGen.generateService(codeWriter, service)
    }

    for (message in result.messages) {
      KMPCodeGen.generateMessage(codeWriter, message)
    }

    println(codeWriter.toString())
  }
}
