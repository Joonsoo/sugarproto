package com.giyeok.sugarproto.bibixPlugin

import com.giyeok.bibix.base.BibixValue
import com.giyeok.bibix.base.BuildContext
import com.giyeok.bibix.base.FileValue
import com.giyeok.bibix.base.StringValue
import com.giyeok.sugarproto.DefTraverser
import com.giyeok.sugarproto.ProtoDefGenerator
import com.giyeok.sugarproto.SugarProtoParser
import kotlin.io.path.readText
import kotlin.io.path.writeText

class GenerateProto {
  fun build(context: BuildContext): BibixValue {
    val source = (context.arguments.getValue("source") as FileValue).file
    val filename = (context.arguments.getValue("filename") as StringValue).value

    val destPath = context.destDirectory.resolve(filename)

    val parsed = SugarProtoParser.parse(source.readText())
    val traverseResult = DefTraverser(parsed).traverse()
    val protoDef = ProtoDefGenerator(
      traverseResult.packageName,
      traverseResult.imports,
      traverseResult.options,
      traverseResult.defs
    ).generate()
    destPath.writeText(protoDef)
    return FileValue(destPath)
  }
}
