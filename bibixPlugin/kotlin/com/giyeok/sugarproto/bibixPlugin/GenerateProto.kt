package com.giyeok.sugarproto.bibixPlugin

import com.giyeok.bibix.base.BibixValue
import com.giyeok.bibix.base.BuildContext
import com.giyeok.bibix.base.FileValue
import com.giyeok.bibix.base.StringValue
import com.giyeok.sugarproto.SugarProtoParser
import com.giyeok.sugarproto.proto.ProtoDefTraverser
import com.giyeok.sugarproto.proto.ProtoGen
import kotlin.io.path.readText
import kotlin.io.path.writeText

class GenerateProto {
  fun build(context: BuildContext): BibixValue {
    val source = (context.arguments.getValue("source") as FileValue).file
    val filename = (context.arguments.getValue("filename") as StringValue).value

    val destPath = context.destDirectory.resolve(filename)

    val parsed = SugarProtoParser.parse(source.readText())
    val defs = ProtoDefTraverser(parsed).traverse()
    val protoDef = ProtoGen().generate(defs)
    destPath.writeText(protoDef)
    return FileValue(destPath)
  }
}
