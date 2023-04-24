package com.giyeok.sugarproto.proto

import com.giyeok.sugarproto.Proto3Ast
import com.giyeok.sugarproto.Proto3Parser
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.readText

interface ImportProtoProvider {
  fun lookup(fileName: String): Proto3Ast.Proto3
}

class ImportProtoFromPathsProvider(val dirs: List<Path>) : ImportProtoProvider {
  override fun lookup(fileName: String): Proto3Ast.Proto3 {
    val text = dirs.map { it.resolve(fileName) }
      .find { it.exists() && it.isRegularFile() }
      ?.readText()
    checkNotNull(text) { "Cannot find $fileName" }
    return Proto3Parser.parse(text)
  }
}

class ImportProtoFromResourcesProvider() : ImportProtoProvider {
  override fun lookup(fileName: String): Proto3Ast.Proto3 {
    val text = javaClass.getResourceAsStream("/" + fileName)?.bufferedReader()?.readText()
    checkNotNull(text) { "Cannot find $fileName" }
    return Proto3Parser.parse(text)
  }
}
