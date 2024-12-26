package com.giyeok.sugarproto.proto3kmp.compiler

import com.giyeok.sugarproto.Proto3Parser
import com.giyeok.sugarproto.proto3kmp.generated.MyProtocolRes
import org.junit.jupiter.api.Test
import java.io.File

@OptIn(ExperimentalStdlibApi::class)
class GenerateTest {
  @Test
  fun test() {
    val protodef = Proto3Parser.parse(File("proto3kmp/grpcresearch/proto/test.proto").readText())

    val compiler = com.giyeok.sugarproto.proto3kmp.compiler.Compiler(
      File("proto3kmp/grpcresearch/proto/test.proto"),
      listOf(File("proto3kmp/include"))
    )

    val result = compiler.compile()

    KMPCodeGen(result).generateFiles(result, File("proto3kmp/test/generated"))
//
//    val services = result.services
//
//    val codeWriter = CodeWriter()
//    for (service in services) {
//      KMPCodeGen.generateService(codeWriter, service)
//    }
//    codeWriter.writeLine()
//
//    for (message in messages) {
//      KMPCodeGen.generateMessage(codeWriter, message.value)
//    }
//
//    println(codeWriter.toString())
  }

//  @Test
//  @OptIn(ExperimentalStdlibApi::class)
//  fun testGeneratedTimestamp() {
//    repeat(10) {
//      val original = Timestamp(Random.nextLong(), Random.nextInt())
//      val serialized = original.serialize()
//      val serializedBytes = serialized.toByteArray()
//      println(serializedBytes.toHexString())
//
//      val pairs = parseBinary(serializedBytes)
//      println(pairs)
//
//      val restored = Timestamp.fromByteArray(serializedBytes)
//      println(original)
//      println(restored)
//      assertThat(original).isEqualTo(restored)
//    }
//  }
//
//  @Test
//  @OptIn(ExperimentalStdlibApi::class)
//  fun testGenerated() {
//    val original = MyProtocolReq(
//      "Hello",
//      123,
//      null,
//      listOf("joonsoo", "hyejin", "jaei"),
//      mapOf("hello" to "world", "This is" to "utf8 string"),
//      Timestamp(abs(Random.nextLong()), abs(Random.nextInt())),
//      Timestamp(abs(Random.nextLong()), abs(Random.nextInt())),
//      listOf(),
//    )
//    val serialized = original.serialize()
//    val serializedBytes = serialized.toByteArray()
//    println(serializedBytes.toHexString())
////    val coded = CodedInputStream.newInstance(serialized)
////    val xx = coded.readRawVarint32()
////    println(xx shr 3)
////    println(xx and 0x7)
////    println(coded.readString())
//
//    val restored = MyProtocolReq.fromByteArray(serializedBytes)
//    println(original)
//    println(restored)
//
//    assertThat(original).isEqualTo(restored)
//  }

  @Test
  fun testGeneratedMessage() {
//    val req = MyProtocolReq.Builder().apply {
//      this.name = "Joonsoo"
//      this.plainTs.seconds = 123123123
//      this.plainTs.nanos = 321321321
//    }.build()
//    println(req.serializeToByteArray().toHexString())

    val res = MyProtocolRes.Builder().apply {
      this.address = "Suji-gu"
    }.build()

    println(res.serializeToByteArray().toHexString())
  }
}
