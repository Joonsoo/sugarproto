package com.giyeok.sugarproto

import com.giyeok.sugarproto.name.SemanticName

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
