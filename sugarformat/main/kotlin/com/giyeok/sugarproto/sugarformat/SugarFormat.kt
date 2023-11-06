package com.giyeok.sugarproto.sugarformat

import com.giyeok.sugarproto.SugarFormatAst
import com.google.protobuf.Message

object SugarFormat {
  fun <T: Message.Builder> merge(sufText: String, builder: T): T {
    val parsed = SugarFormatParser.parse(sufText)
    Parser(ItemStructurizer(parsed)).merge(builder)
    return builder
  }

  class Parser(val struct: ItemStructurizer) {
    fun merge(builder: Message.Builder) {
      val siblings = struct.all.siblingsOfFirst()
      siblings.forEach { rootField ->
        mergeField(BuilderSetter.MessageBuilder(builder), rootField)
      }
    }

    private fun mergeField(bs: BuilderSetter, field: ItemStructurizer.Range) {
      val head = field.first
      check(head is SugarFormatAst.SingleItem)
      val path = head.key
      when {
        field.isSingular -> {
          when (val value = head.value) {
            is SugarFormatAst.ScalarValue -> {
              val setter = BuilderSetter.lookup(bs, path)
              check(setter is BuilderSetter.ScalarSetter)
              setter.setScalarValue(value)
            }

            is SugarFormatAst.RepeatedValue -> {
              val setter = BuilderSetter.lookup(bs, path)
              check(setter is BuilderSetter.RepeatedBuilder)
              setter.setRepeatedValues(value)
            }

            is SugarFormatAst.ObjectOrMapValue -> {
              // setObjectOrMapValues(bs, head.key.first, head.key.access, value)
              TODO()
            }

            is SugarFormatAst.Header -> throw IllegalStateException()
          }
        }

        field.isRepeated -> {
          check(head.value is SugarFormatAst.Header)
          val children = field.listChildrenOfFirst()
          val setter = BuilderSetter.lookup(bs, head.key)
          check(setter is BuilderSetter.RepeatedBuilder)
          when (setter) {
            is BuilderSetter.ScalarRepeatedBuilder -> {
              children.forEach { child ->
                val item = child.first
                check(item is SugarFormatAst.ListValueItem)
                setter.addRepeatedValue(item.value)
              }
            }

            is BuilderSetter.MessageRepeatedBuilder -> {
              children.forEach { child ->
                mergeField(setter.addValueBuilder(), child)
              }
            }
          }
        }

        else -> {
          check(head.value is SugarFormatAst.Header)
          // scalar value setter
          // message value builder
          // scalar list value builder
          // message list value builder
          // scalar map value builder
          // message map value builder
          when (val setter = BuilderSetter.lookup(bs, head.key)) {
            is BuilderSetter.ScalarSetter -> throw IllegalStateException()
            is BuilderSetter.MessageBuilder -> TODO()
            is BuilderSetter.MessageMapBuilder -> TODO()
            is BuilderSetter.MessageRepeatedBuilder -> TODO()
            is BuilderSetter.ScalarMapBuilder -> TODO()
            is BuilderSetter.ScalarRepeatedBuilder -> TODO()
          }
        }
      }
    }

    private fun setRepeatedValues(
      builder: Message.Builder,
      first: SugarFormatAst.KeyValue,
      rest: List<SugarFormatAst.KeyValue>,
      value: SugarFormatAst.RepeatedValue
    ) {
      TODO()
    }

    private fun setObjectOrMapValues(
      builder: Message.Builder,
      first: SugarFormatAst.KeyValue,
      rest: List<SugarFormatAst.KeyValue>,
      value: SugarFormatAst.ObjectOrMapValue
    ) {
      TODO()
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
    val isRepeated: Boolean get() = !isSingular && items[start + 1].item is SugarFormatAst.ListItem

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
