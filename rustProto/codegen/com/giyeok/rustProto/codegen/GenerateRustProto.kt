package com.giyeok.rustProto.codegen

import com.giyeok.bibix.base.*
import com.giyeok.sugarproto.Proto3Ast
import com.giyeok.sugarproto.Proto3Parser
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.readText

class GenerateRustProto {
  fun build(context: BuildContext): BibixValue {
    // class ProtoSchema(schemaFiles: set<file>, includes: set<path>)
    val schema = context.arguments.getValue("schema") as ClassInstanceValue
    val protoFiles = schema.getField("schemaFiles") as SetValue
    val includes = schema.getField("includes") as SetValue

    generate(
      protoFiles.values.map { (it as FileValue).file },
      includes.values.map { (it as PathValue).path })

    return NClassInstanceValue(
      "GeneratedRustFiles",
      mapOf("srcsRoot" to DirectoryValue(context.destDirectory))
    )
  }

  inline fun indent(block: () -> Unit) {
    block()
    // TODO
  }

  fun generate(protoFiles: List<Path>, includes: List<Path>) {
    protoFiles.forEach { protoFile ->
      val ast = Proto3Parser.parse(protoFile.readText())
      ast.defs.forEach { def ->
        when (def) {
          is Proto3Ast.Message -> {
            println("#[derive(Debug)]")
            println("pub struct ${def.name.name} {")
            indent {
              def.body.forEach { elem ->
                when (elem) {
                  is Proto3Ast.Field -> {
                    println("${elem.name.name}: ${elem.typ},")
                  }

                  is Proto3Ast.EmptyStatement -> {}
                  is Proto3Ast.EnumDef -> {}
                  is Proto3Ast.MapField -> {}
                  is Proto3Ast.Message -> {}
                  is Proto3Ast.OneofDef -> {}
                  is Proto3Ast.OptionDef -> {}
                  is Proto3Ast.Reserved -> {}
                }
              }
            }
            println("}")

            println("impl ${def.name.name} {")
            indent {
              println("pub fn builder() -> ${def.name.name}Builder {")
              indent {
                println("${def.name.name}Builder {")
                println("}")
              }
              println("}")
            }
            println("}")

            println("struct ${def.name.name}Builder {")
            indent {

            }
            println("}")
          }

          is Proto3Ast.EmptyStatement -> {}
          is Proto3Ast.Import -> {}
          is Proto3Ast.OptionDef -> {}
          is Proto3Ast.Package -> {}
          is Proto3Ast.EnumDef -> {}
          is Proto3Ast.Service -> {}
        }
      }
      println(ast)
    }
  }

  companion object {
    // For test
    @JvmStatic
    fun main(args: Array<String>) {
      val path = Path.of("bbxbuild/outputs/rustProto.test.steditProto/stedit.proto")
      println(path.absolutePathString())
      GenerateRustProto().generate(
        listOf(path),
        listOf()
      )
    }
  }
}
