package com.giyeok.sugarproto.sugarformat

import com.giyeok.sugarproto.SugarFormatAst
import com.google.protobuf.Descriptors.FieldDescriptor
import com.google.protobuf.Message
import com.google.protobuf.Message.Builder

object SugarFormat {
  fun merge(sufText: String, builder: Builder) {
    val parsed = SugarFormatParser.parse(sufText)
    Parser(ItemStructurizer(parsed)).merge(builder)
  }

  class Parser(val struct: ItemStructurizer) {
    fun merge(builder: Message.Builder) {
      val siblings = struct.all.siblingsOfFirst()
      siblings.forEach { rootField ->
        val head = rootField.first
        check(head is SugarFormatAst.SingleItem)
        // lookupBuilder(builder, head.key)
        when (val value = head.value) {
          is SugarFormatAst.ScalarValue -> {
            setFieldValue(builder, head.key.first, head.key.access, value)
          }

          is SugarFormatAst.Header -> TODO()
          is SugarFormatAst.RepeatedValue -> TODO()
          is SugarFormatAst.ObjectOrMapValue -> TODO()
        }
      }
    }

    fun test() {
      val fields = struct.all.siblingsOfFirst()
      println(fields)
      val children = fields.first().childrenOfFirst()
      println(children)
      val listChildren = fields.last().listChildrenOfFirst()
      println(listChildren)
    }
  }
}

class ItemStructurizer(val items: List<SugarFormatAst.IndentItem>) {
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

    val first: SugarFormatAst.Item get() = items[start].item

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
