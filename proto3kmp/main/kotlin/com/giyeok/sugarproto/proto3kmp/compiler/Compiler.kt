package com.giyeok.sugarproto.proto3kmp.compiler

import com.giyeok.sugarproto.Proto3Ast
import java.io.File

class Compiler(val mainProto: File, val includePaths: List<File>) {
  val names =
    com.giyeok.sugarproto.proto3kmp.compiler.ProtoNames.Companion.readFrom(mainProto, includePaths)

  fun compile(): com.giyeok.sugarproto.proto3kmp.compiler.CompileResult {
    val messages = mutableMapOf<String, com.giyeok.sugarproto.proto3kmp.compiler.MessageDef>()
    val enums = mutableMapOf<String, com.giyeok.sugarproto.proto3kmp.compiler.EnumDef>()

    val compile1 = com.giyeok.sugarproto.proto3kmp.compiler.Proto3ToKMPCompiler(
      names,
      names.mainFile
    ).generateDefs()
    messages.putAll(compile1.messages)
    enums.putAll(compile1.enums)
    val requiredTypes = compile1.requiredTypes.toMutableSet()
    val compiledTypes = messages.keys.toMutableSet()

    while (!compiledTypes.containsAll(requiredTypes)) {
      val toCompile = requiredTypes - compiledTypes
      for (type in toCompile) {
        val msg = names.messages[type]
        if (msg != null) {
          val compiled = com.giyeok.sugarproto.proto3kmp.compiler.Proto3ToKMPCompiler(
            names,
            names.files.getValue(msg.sourceFile)
          )
            .compileMessage(msg.def as Proto3Ast.Message)
          messages[type] = compiled
          compiledTypes.add(type)
          requiredTypes.addAll(compiled.requiredTypes)
        } else {
          val enum = names.enums[type]
          checkNotNull(enum)
          enums[type] = com.giyeok.sugarproto.proto3kmp.compiler.Proto3ToKMPCompiler(
            names,
            names.files.getValue(enum.sourceFile)
          )
            .compileEnum(enum.def as Proto3Ast.EnumDef)
          compiledTypes.add(type)
        }
      }
    }

    return com.giyeok.sugarproto.proto3kmp.compiler.CompileResult(
      compile1.services,
      messages,
      enums
    )
  }
}

data class CompileResult(
  val services: List<com.giyeok.sugarproto.proto3kmp.compiler.ServiceDef>,
  val messages: Map<String, com.giyeok.sugarproto.proto3kmp.compiler.MessageDef>,
  val enums: Map<String, com.giyeok.sugarproto.proto3kmp.compiler.EnumDef>,
)
