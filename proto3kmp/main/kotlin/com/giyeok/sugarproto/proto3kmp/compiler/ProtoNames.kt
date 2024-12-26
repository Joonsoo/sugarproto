package com.giyeok.sugarproto.proto3kmp.compiler

import com.giyeok.sugarproto.Proto3Ast
import com.giyeok.sugarproto.Proto3Parser
import java.io.File
import java.nio.file.Path

data class ProtoNames(
  val files: Map<String, Proto3Ast.Proto3>,
  // canonical type name to file name
  val messages: Map<String, MessageTypeInfo>,
  val enums: Map<String, MessageTypeInfo>,
) {
  val mainFile get() = files.getValue("")

  companion object {
    fun readFrom(mainFile: File, includePaths: List<File>): ProtoNames {
      val builder = Builder(mutableMapOf(), mutableMapOf(), mutableMapOf())
      builder.traverse(Proto3Parser.parse(mainFile.readText()), "")

      for (includePath in includePaths) {
        for (file in includePath.walkTopDown()) {
          if (file.isFile && file.extension == "proto") {
            println(file.absolutePath)
            builder.traverse(Proto3Parser.parse(file.readText()), file.absolutePath)
          }
        }
      }
      return builder.build()
    }
  }

  class Builder(
    val files: MutableMap<String, Proto3Ast.Proto3>,
    val messages: MutableMap<String, MessageTypeInfo>,
    val enums: MutableMap<String, MessageTypeInfo>,
  ) {
    fun build(): ProtoNames {
      return ProtoNames(files, messages, enums)
    }

    fun traverse(ast: Proto3Ast.Proto3, sourceFile: String) {
      check(sourceFile !in files)
      files[sourceFile] = ast
      var javaPkg = ""
      val protoPkgs = ast.defs.filterIsInstance<Proto3Ast.Package>()
      check(protoPkgs.size <= 1)
      val protoPkg =
        if (protoPkgs.isEmpty()) "" else protoPkgs.single().name.names.joinToString(".") { it.name }
      for (def in ast.defs) {
        when (def) {
          is Proto3Ast.EmptyStatement -> {}
          is Proto3Ast.Import -> {}
          is Proto3Ast.OptionDef -> {
            val scope = def.name.scope
            if (scope is Proto3Ast.Ident && scope.name == "java_package" && def.name.name.isEmpty()) {
              val v = def.value
              check(v is Proto3Ast.StringConstant)
              javaPkg = when (val str = v.value) {
                is Proto3Ast.DoubleQuoteStrLit -> str.value.joinToString("") { c ->
                  when (c) {
                    is Proto3Ast.Character -> c.value.toString()
                    is Proto3Ast.CharEscape -> c.value.toString()
                    is Proto3Ast.HexEscape -> TODO()
                    is Proto3Ast.OctalEscape -> TODO()
                  }
                }

                is Proto3Ast.SingleQuoteStrLit -> TODO()
              }
            }
          }

          is Proto3Ast.Package -> {
          }

          is Proto3Ast.EnumDef -> {
            val canonicalName =
              if (protoPkg.isEmpty()) def.name.name else "$protoPkg.${def.name.name}"
            check(canonicalName !in enums)
            val javaName = if (javaPkg.isEmpty()) def.name.name else "$javaPkg.${def.name.name}"
            enums[canonicalName] = MessageTypeInfo(sourceFile, javaName, def)
          }

          is Proto3Ast.Message -> {
            val canonicalName =
              if (protoPkg.isEmpty()) def.name.name else "$protoPkg.${def.name.name}"
            check(canonicalName !in enums)
            val javaName = if (javaPkg.isEmpty()) def.name.name else "$javaPkg.${def.name.name}"
            messages[canonicalName] = MessageTypeInfo(sourceFile, javaName, def)
          }

          is Proto3Ast.Service -> {
          }
        }
      }
    }
  }
}

data class MessageTypeInfo(
  val sourceFile: String,
  val javaCanonicalName: String,
  val def: Proto3Ast.ProtoDefElem
)

data class RequiredGens(
  val requiredTypes: Set<String>,
  // TODO oneofCases type
  val oneofCases: Set<String>
)

data class ProtoDef(
  val mainDef: Proto3Ast.Proto3,
  val includePaths: List<Path>
) {
  // TODO resolve
}
