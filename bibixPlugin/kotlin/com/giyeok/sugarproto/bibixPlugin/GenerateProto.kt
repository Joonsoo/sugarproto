package com.giyeok.sugarproto.bibixPlugin

import com.giyeok.bibix.base.*
import com.giyeok.sugarproto.SugarProtoParser
import com.giyeok.sugarproto.proto.ImportProtoFromPathsProvider
import com.giyeok.sugarproto.proto.ProtoDefTraverser
import com.giyeok.sugarproto.proto.ProtoGen
import kotlin.io.path.readText
import kotlin.io.path.writeText

class GenerateProto {
  fun build(context: BuildContext): BibixValue {
    val source = (context.arguments.getValue("source") as FileValue).file
    val filename = (context.arguments.getValue("filename") as StringValue).value
    val protoDirs = (context.arguments.getValue("protoDirs") as SetValue).values
      .map { (it as DirectoryValue).directory }

    val destPath = context.destDirectory.resolve(filename)

    val parsed = SugarProtoParser.parse(source.readText())
    val defs = ProtoDefTraverser(parsed, ImportProtoFromPathsProvider(protoDirs)).traverse()
    val protoDef = ProtoGen().generate(defs)
    destPath.writeText(protoDef)
    return FileValue(destPath)
  }
}
