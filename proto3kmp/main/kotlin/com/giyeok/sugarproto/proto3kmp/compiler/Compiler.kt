package com.giyeok.sugarproto.proto3kmp.compiler

import com.giyeok.sugarproto.Proto3Ast
import java.io.File

class Compiler(val mainProto: File, val includePaths: List<File>) {
  val names = ProtoNames.Companion.readFrom(mainProto, includePaths)

  fun compile(): CompileResult {
    val messages = mutableMapOf<String, MessageDef>()
    val enums = mutableMapOf<String, EnumDef>()

    val compile1 = Proto3ToKMPCompiler(
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
          val compiled = Proto3ToKMPCompiler(
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
          enums[type] = Proto3ToKMPCompiler(
            names,
            names.files.getValue(enum.sourceFile)
          )
            .compileEnum(enum.def as Proto3Ast.EnumDef)
          compiledTypes.add(type)
        }
      }
    }

    return CompileResult(
      compile1.services,
      messages,
      enums
    )
  }
}

data class CompileResult(
  val services: List<ServiceDef>,
  val messages: Map<String, MessageDef>,
  val enums: Map<String, EnumDef>,
)
