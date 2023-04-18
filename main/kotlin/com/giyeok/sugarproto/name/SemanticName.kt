package com.giyeok.sugarproto.name

import com.giyeok.sugarproto.SugarProtoAst

data class SemanticName constructor(val words: List<String>) {
  private constructor(name: String): this(name.toWords())
  private constructor(ident: SugarProtoAst.Ident): this(ident.name.toWords())

  fun camelCase(): String {
    val restWords = words.drop(1)
    return words.first().lowercase() +
      restWords.joinToString("") { word -> word.replaceFirstChar { it.uppercase() } }
  }

  fun capitalCamelCase(): String =
    words.joinToString("") { word -> word.replaceFirstChar { it.uppercase() } }

  fun capitalSnakeCase(): String =
    words.joinToString("_") { word -> word.uppercase() }

  fun lowerSnakeCase(): String =
    words.joinToString("_") { word -> word.lowercase() }

  val enumFieldName: String get() = capitalSnakeCase()
  val enumName: String get() = capitalCamelCase()
  val messageName: String get() = capitalCamelCase()
  val messageFieldName: String get() = lowerSnakeCase()
  val serviceName: String get() = capitalCamelCase()
  val rpcName: String get() = capitalCamelCase()

  val enumClassName: String get() = capitalCamelCase()
  val enumClassFieldName: String get() = capitalSnakeCase()
  val className: String get() = capitalCamelCase()
  val classFieldName: String get() = camelCase()
  val capitalClassFieldName: String get() = capitalCamelCase()

  companion object {
    fun enumName(name: String) = SemanticName(name)
    fun enumName(ident: SugarProtoAst.Ident) = SemanticName(ident.name)

    fun enumMember(ident: SugarProtoAst.Ident) = SemanticName(ident.name)

    fun messageName(name: String) = SemanticName(name)
    fun messageName(ident: SugarProtoAst.Ident) = messageName(ident.name)
    fun messageMember(ident: SugarProtoAst.Ident) = SemanticName(ident.name)

    fun serviceName(ident: SugarProtoAst.Ident) = SemanticName(ident.name)
    fun rpcName(ident: SugarProtoAst.Ident): SemanticName = SemanticName(ident.name)

    fun String.toWords(): List<String> {
      return this.split('_')
        .flatMap { it.split(Regex("(?=\\p{Upper})")) }
        .filter { it.isNotEmpty() }
    }

  }
}
