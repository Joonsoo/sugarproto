package com.giyeok.sugarproto.sugarformat

import com.giyeok.sugarproto.SugarFormatAst

object SugarFormat {
  class Parser(val struct: ItemStructurizer) {
    fun test() {
      struct.findSiblingsOfFirst()
    }
  }
}

class ItemStructurizer(
  val items: List<SugarFormatAst.IndentItem>,
  val start: Int,
  val end: Int
) {
  constructor(items: List<SugarFormatAst.IndentItem>): this(items, 0, items.size)

  fun checkIndent(checker: Boolean, item: SugarFormatAst.IndentItem) {
    if (!checker) {
      // TODO improve message
      throw IllegalStateException("Invalid indent at $item")
    }
  }

  fun findSiblingsOfFirst(): List<ItemStructurizer> {
    val indent = items[start].indent
    var lastIdx = 0
    val siblings = mutableListOf<ItemStructurizer>()
    (start + 1 until end).forEach { index ->
      checkIndent(items[index].indent.startsWith(indent), items[index])
      if (items[index].indent == indent) {
        siblings.add(ItemStructurizer(items, lastIdx, index))
        lastIdx = index
      }
    }
    return siblings
  }

  // list head가 headIdx 위치에 들어있을 때, 해당 리스트의 아이템에 해당되는 것들의 위치를 반환
  // 반환되는 위치의 start 위치에는 ListItem 이 들어있어야 한다
  fun findListElemsFrom(headIdx: Int): List<ItemStructurizer> {
    TODO()
  }

  // map head가 headIdx 위치에 들어있을 때, 해당 map의 key value pair에 해당되는 것들의 위치를 반환
  // 반환되는 위치의 start 위치에는 MapItem 이 들어있어야 한다
  fun findObjectElemsFrom(headIdx: Int): List<ItemStructurizer> {
    TODO()
  }

  // object head가 headIdx 위치에 들어있을 때, 해당 object의 field들에 해당되는 것들의 위치를 반환
  fun findFieldsFrom(headIdx: Int): List<ItemStructurizer> {
    TODO()
  }
}
