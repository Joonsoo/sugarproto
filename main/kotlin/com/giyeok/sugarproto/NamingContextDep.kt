package com.giyeok.sugarproto

import com.giyeok.sugarproto.name.SemanticName

class NamingContextDep(val names: List<String>) {
  constructor(vararg names: SugarProtoAst.Ident):
    this(names.map { it.name }.toList())

  operator fun plus(name: String): NamingContextDep =
    NamingContextDep(names + name)

  operator fun plus(name: SugarProtoAst.Ident): NamingContextDep = plus(name.name)
}

class NamingContext(val names: List<SemanticName>) {
  constructor(vararg names: SemanticName): this(names.toList())

  fun messageName(): SemanticName =
    SemanticName(names.flatMap { it.words })

  fun enumName(): SemanticName =
    SemanticName(names.flatMap { it.words })

  operator fun plus(name: SemanticName): NamingContext =
    NamingContext(names + name)

  operator fun plus(name: String): NamingContext =
    NamingContext(names + SemanticName(listOf(name)))
}
