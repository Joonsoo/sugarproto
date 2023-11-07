package com.giyeok.sugarproto.sugarformat

import com.giyeok.sugarproto.SugarFormatAst

class ItemStructure(val items: List<SugarFormatAst.IndentItem>) {
  fun checkIndent(checker: Boolean, item: SugarFormatAst.IndentItem) {
    if (!checker) {
      // TODO improve message
      throw IllegalStateException("Invalid indent at $item")
    }
  }

  val all = Range(0, items.size)

  inner class Range(val start: Int, val end: Int) {
    override fun toString(): String = "[$start..${end - 1}]"

    val isSingular: Boolean get() = start + 1 == end
    val isRepeated: Boolean get() = !isSingular && items[start + 1].item is SugarFormatAst.ListItem

    val head: SugarFormatAst.Item get() = items[start].item

    fun siblingsOfFirst(): List<Range> {
      val indent = items[start].indent
      var lastIdx = start
      val siblings = mutableListOf<Range>()
      (start + 1 until end).forEach { index ->
        checkIndent(items[index].indent.startsWith(indent), items[index])
        if (items[index].indent == indent && items[index].item !is SugarFormatAst.ListItem) {
          siblings.add(Range(lastIdx, index))
          lastIdx = index
        }
      }
      siblings.add(Range(lastIdx, end))
      return siblings
    }

    fun siblingsOfFirstListField(): List<Range> {
      val head = items[start]
      val headItem = head.item
      check(headItem is SugarFormatAst.ListFieldItem)
      val indent = head.indent + " " + headItem.innerIndent.drop(1)
      var lastIdx = start
      val siblings = mutableListOf<Range>()
      (start + 1 until end).forEach { index ->
        checkIndent(items[index].indent.startsWith(indent), items[index])
        if (items[index].indent == indent && items[index].item !is SugarFormatAst.ListItem) {
          siblings.add(Range(lastIdx, index))
          lastIdx = index
        }
      }
      siblings.add(Range(lastIdx, end))
      return siblings
    }

    fun childrenOfFirst(): List<Range> {
      check(!isSingular)
      val childIndent = items[start + 1].indent
      var lastIdx = start + 1
      val siblings = mutableListOf<Range>()
      (start + 2 until end).forEach { index ->
        checkIndent(items[index].indent.startsWith(childIndent), items[index])
        if (items[index].indent == childIndent) {
          check(items[index].item !is SugarFormatAst.ListItem)
          siblings.add(Range(lastIdx, index))
          lastIdx = index
        }
      }
      siblings.add(Range(lastIdx, end))
      return siblings
    }

    fun listChildrenOfFirst(): List<Range> {
      check(!isSingular)
      val childIndent = items[start + 1].indent
      var lastIdx = start + 1
      val siblings = mutableListOf<Range>()
      (start + 2 until end).forEach { index ->
        checkIndent(items[index].indent.startsWith(childIndent), items[index])
        if (items[index].indent == childIndent) {
          check(items[index].item is SugarFormatAst.ListItem)
          siblings.add(Range(lastIdx, index))
          lastIdx = index
        }
      }
      siblings.add(Range(lastIdx, end))
      return siblings
    }
  }
}