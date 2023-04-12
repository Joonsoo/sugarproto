package com.giyeok.sugarproto

class NamingContext(val names: List<String>) {
  constructor(vararg names: SugarProtoAst.Ident):
    this(names.map { it.name }.toList())

  operator fun plus(name: String): NamingContext =
    NamingContext(names + name)

  operator fun plus(name: SugarProtoAst.Ident): NamingContext = plus(name.name)
}
