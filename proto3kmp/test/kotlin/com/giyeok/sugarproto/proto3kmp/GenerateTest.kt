package com.giyeok.sugarproto.proto3kmp

import com.giyeok.sugarproto.Proto3Parser
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs
import kotlin.random.Random

class GenerateTest {
  @Test
  fun test() {
    val protodef = Proto3Parser.parse(File("proto3kmp/grpcresearch/proto/test.proto").readText())
    val compiler =
      Proto3ToKMPCompiler(ProtoNames(mapOf("Timestamp" to ""), mapOf()), protodef)

    val result = compiler.generateDefs()
    println(result)

    val codeWriter = CodeWriter()
    for (service in result.services) {
      KMPCodeGen.generateService(codeWriter, service)
    }
    codeWriter.writeLine()

    for (message in result.messages) {
      KMPCodeGen.generateMessage(codeWriter, message)
    }

    println(codeWriter.toString())
  }

  @Test
  @OptIn(ExperimentalStdlibApi::class)
  fun testGeneratedTimestamp() {
    repeat(10) {
      val original = Timestamp(Random.nextLong(), Random.nextInt())
      val serialized = original.serialize()
      val serializedBytes = serialized.toByteArray()
      println(serializedBytes.toHexString())

      val pairs = parseBinary(serializedBytes)
      println(pairs)

      val restored = Timestamp.fromByteArray(serializedBytes)
      println(original)
      println(restored)
      assertThat(original).isEqualTo(restored)
    }
  }

  @Test
  @OptIn(ExperimentalStdlibApi::class)
  fun testGenerated() {
    val original = MyProtocolReq(
      "Hello",
      123,
      null,
      listOf(),
      mapOf("hello" to "world", "This is" to "utf8 string"),
      Timestamp(abs(Random.nextLong()), abs(Random.nextInt())),
      Timestamp(abs(Random.nextLong()), abs(Random.nextInt())),
      listOf(),
    )
    val serialized = original.serialize()
    val serializedBytes = serialized.toByteArray()
    println(serializedBytes.toHexString())
//    val coded = CodedInputStream.newInstance(serialized)
//    val xx = coded.readRawVarint32()
//    println(xx shr 3)
//    println(xx and 0x7)
//    println(coded.readString())

    val restored = MyProtocolReq.fromByteArray(serializedBytes)
    println(original)
    println(restored)
    println(original == restored)

    assertThat(original).isEqualTo(restored)
  }
}
