package com.giyeok.sugarproto.proto3kmp

import com.giyeok.sugarproto.Proto3Ast
import java.nio.file.Path

class Proto3NameTraverser {
  private val messages = mutableListOf<Pair<String, String>>()
  private val enums = mutableListOf<Pair<String, String>>()

  fun traverseNames(def: Proto3Ast.Proto3) {
    for (topLevelDef in def.defs.filterIsInstance<Proto3Ast.TopLevelDef>()) {
      when (topLevelDef) {
        is Proto3Ast.EnumDef -> TODO()
        is Proto3Ast.Message -> TODO()
        is Proto3Ast.Service -> TODO()
      }
    }
    TODO()
  }

  fun build(): ProtoNames {
    TODO()
  }
}

data class ProtoNames(
  // canonical type name to file name
  val messages: Map<String, String>,
  val enums: Map<String, String>,
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
