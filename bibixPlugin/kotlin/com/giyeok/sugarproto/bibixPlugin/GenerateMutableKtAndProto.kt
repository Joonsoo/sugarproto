package com.giyeok.sugarproto.bibixPlugin

import com.giyeok.bibix.base.*
import com.giyeok.sugarproto.DefTraverser
import com.giyeok.sugarproto.MutableKtDataClassGenerator
import com.giyeok.sugarproto.ProtoDefGenerator
import com.giyeok.sugarproto.SugarProtoParser
import kotlin.io.path.createDirectories
import kotlin.io.path.readText
import kotlin.io.path.writeText

class GenerateMutableKtAndProto {
  fun build(context: BuildContext): BibixValue {
    val source = (context.arguments.getValue("source") as FileValue).file
    val protoFileName = (context.arguments.getValue("protoFileName") as StringValue).value
    val protoOuterClassName =
      (context.arguments.getValue("protoOuterClassName") as StringValue).value
    val packageName = (context.arguments.getValue("packageName") as StringValue).value
    val kotlinFileName = (context.arguments.getValue("kotlinFileName") as StringValue).value
    val imports =
      (context.arguments.getValue("imports").nullOr<ListValue>())?.values?.map { value ->
        (value as StringValue).value
      }
    val gdxMode = (context.arguments.getValue("gdxMode") as BooleanValue).value

    val parsed = SugarProtoParser.parse(source.readText())
    val traverseResult = DefTraverser(parsed).traverse()

    val srcsRoot = context.destDirectory.resolve("srcs")
    val protoDest = context.destDirectory.resolve(protoFileName)

    val protoDef = ProtoDefGenerator(
      traverseResult.packageName,
      traverseResult.imports,
      traverseResult.options,
      traverseResult.defs
    ).generate()
    protoDest.writeText(protoDef)

    val ktImports = (imports?.toMutableSet() ?: mutableSetOf())
    if (gdxMode) {
      ktImports.add("com.badlogic.gdx.utils.Array as GdxArray")
    }
    val mutableKt = MutableKtDataClassGenerator(
      packageName,
      traverseResult.imports,
      traverseResult.options,
      traverseResult.defs,
      ktImports.toList().sorted(),
      "$protoOuterClassName.",
      gdxMode,
    ).generate()
    val ktSrcDir = packageName.split('.').fold(srcsRoot) { a, b -> a.resolve(b) }
    ktSrcDir.createDirectories()
    val ktSrcFile = ktSrcDir.resolve(kotlinFileName)
    ktSrcFile.writeText(mutableKt)

    return ClassInstanceValue(
      "com.giyeok.sugarproto",
      "Generated",
      mapOf(
        "srcsRoot" to DirectoryValue(srcsRoot),
        "protoFile" to FileValue(protoDest),
      )
    )
  }
}
