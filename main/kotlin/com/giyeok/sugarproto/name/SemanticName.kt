package com.giyeok.sugarproto.name

import com.giyeok.sugarproto.SugarProtoAst

data class SemanticName(val scope: List<String>, val words: List<String>) {
  constructor(name: String): this(listOf(), name.toWords())
  constructor(name: SugarProtoAst.Ident): this(listOf(), name.name.toWords())

  companion object {
    fun String.toWords(): List<String> {
      return this.split('_')
        .flatMap { it.split(Regex("(?=\\p{Upper})")) }
        .filter { it.isNotEmpty() }
    }
  }
}
