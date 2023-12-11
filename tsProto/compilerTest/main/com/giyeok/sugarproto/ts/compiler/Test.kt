package com.giyeok.sugarproto.ts.compiler

import com.giyeok.stedit.proto.Test.Project
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class Test {
  @OptIn(ExperimentalStdlibApi::class)
  @Test
  fun test() {
    val compiler = CompilerToTypeScript()

    val project = Project.newBuilder()
      .setSchemaVersion(1)
    project.documentBuilder.addRootsBuilder()
      .setId(2)
      .setUserId("Hello")
    val stream = ByteArrayOutputStream()
    project.build()
      .writeTo(stream)
    println(stream.toByteArray().toHexString())

    compiler.compile(javaClass.getResourceAsStream("/test.proto")!!.readAllBytes().decodeToString())
  }
}
