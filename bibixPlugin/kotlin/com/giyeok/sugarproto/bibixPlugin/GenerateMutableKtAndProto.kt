package com.giyeok.sugarproto.bibixPlugin

import com.giyeok.bibix.base.*
import com.giyeok.sugarproto.SugarProtoParser
import com.giyeok.sugarproto.mutkt.MutableKotlinDefConverter
import com.giyeok.sugarproto.mutkt.MutableKtDataClassGen
import com.giyeok.sugarproto.proto.ImportProtoFromPathsProvider
import com.giyeok.sugarproto.proto.ProtoDefTraverser
import com.giyeok.sugarproto.proto.ProtoGen
import kotlin.io.path.createDirectories
import kotlin.io.path.readText
import kotlin.io.path.writeText

class GenerateMutableKtAndProto {
  fun build(context: BuildContext): BibixValue {
    val source = (context.arguments.getValue("source") as FileValue).file
    val protoFileName = (context.arguments.getValue("protoFileName") as StringValue).value
    val kotlinFileName = (context.arguments.getValue("kotlinFileName") as StringValue).value
    val imports =
      (context.arguments.getValue("imports").nullOr<ListValue>())?.values?.map { value ->
        (value as StringValue).value
      }
    val protoDirs = (context.arguments.getValue("protoDirs") as SetValue).values
      .map { (it as DirectoryValue).directory }
    val gdxMode = (context.arguments.getValue("gdxMode") as BooleanValue).value

    val parsed = SugarProtoParser.parse(source.readText())
    val defs = ProtoDefTraverser(parsed, ImportProtoFromPathsProvider(protoDirs)).traverse()

    val srcsRoot = context.destDirectory.resolve("srcs")
    val protoDest = context.destDirectory.resolve(protoFileName)

    val protoDef = ProtoGen().generate(defs)
    protoDest.writeText(protoDef)

    val ktImports = (imports?.toMutableSet() ?: mutableSetOf())
    val ktDefs = MutableKotlinDefConverter(defs).convert()
    val mutableKt = MutableKtDataClassGen(
      ktDefs,
      ktImports.toList().sorted(),
      gdxMode,
    ).generate()
    val ktSrcDir =
      (ktDefs.kotlinPackageName ?: "").split('.').fold(srcsRoot) { a, b -> a.resolve(b) }
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
