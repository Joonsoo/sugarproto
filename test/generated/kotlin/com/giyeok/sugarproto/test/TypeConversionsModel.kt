package com.giyeok.sugarproto.test

import com.giyeok.sugarproto.test.TypeConversionsProto

data class TypeTest(
  val a: MutableList<String>,
  val b: MutableList<MyEnum>,
  val c: MutableList<MyMessage>,
  var d: String?,
  var e: MyEnum?,
  var f: MyMessage?,
  val g: MutableMap<Int, String>,
  val h: MutableMap<Int, MyEnum>,
  val i: MutableMap<Int, MyMessage>,
  var j: String,
  var k: MyEnum,
  val l: MyMessage,
) {
  companion object {
    fun create(
      a: MutableList<String> = mutableListOf(),
      b: MutableList<MyEnum> = mutableListOf(),
      c: MutableList<MyMessage> = mutableListOf(),
      d: String? = null,
      e: MyEnum? = null,
      f: MyMessage? = null,
      g: MutableMap<Int, String> = mutableMapOf(),
      h: MutableMap<Int, MyEnum> = mutableMapOf(),
      i: MutableMap<Int, MyMessage> = mutableMapOf(),
      j: String = "",
      k: MyEnum = MyEnum.defaultValue,
      l: MyMessage = MyMessage.create(),
    ) = TypeTest(
      a,
      b,
      c,
      d,
      e,
      f,
      g,
      h,
      i,
      j,
      k,
      l,
    )

    fun fromProto(proto: TypeConversionsProto.TypeTest): TypeTest {
      val instance = TypeTest(
        a = mutableListOf(/* proto.aCount */),
        b = mutableListOf(/* proto.bCount */),
        c = mutableListOf(/* proto.cCount */),
        d = if (!proto.hasD()) null else proto.d,
        e = if (!proto.hasE()) null else MyEnum.fromProto(proto.e),
        f = if (!proto.hasF()) null else MyMessage.fromProto(proto.f),
        g = mutableMapOf(/* proto.gCount */),
        h = mutableMapOf(/* proto.hCount */),
        i = mutableMapOf(/* proto.iCount */),
        j = proto.j,
        k = MyEnum.fromProto(proto.k),
        l = MyMessage.fromProto(proto.l),
      )
      proto.aList.forEach { elem ->
        instance.a.add(elem)
      }
      proto.bList.forEach { elem ->
        instance.b.add(MyEnum.fromProto(elem))
      }
      proto.cList.forEach { elem ->
        instance.c.add(MyMessage.fromProto(elem))
      }
      proto.gMap.forEach { entry ->
        instance.g.put(entry.key, entry.value)
      }
      proto.hMap.forEach { entry ->
        instance.h.put(entry.key, MyEnum.fromProto(entry.value))
      }
      proto.iMap.forEach { entry ->
        instance.i.put(entry.key, MyMessage.fromProto(entry.value))
      }
      return instance
    }
  }

  fun toProto(builder: TypeConversionsProto.TypeTest.Builder) {
    this.a.forEach { elem ->
      builder.addA(elem)
    }
    this.b.forEach { elem ->
      builder.addB(elem.toProto())
    }
    this.c.forEach { elem ->
      elem.toProto(builder.addCBuilder())
    }
    this.d?.let { value -> builder.d = value } ?: builder.clearD()
    this.e?.let { value -> builder.e = value.toProto() } ?: builder.clearE()
    this.f?.toProto(builder.fBuilder) ?: builder.clearF()
    this.g.forEach { (key, value) ->
      builder.putG(key, value)
    }
    this.h.forEach { (key, value) ->
      builder.putH(key, value.toProto())
    }
    this.i.forEach { (key, value) ->
      val valueBuilder = TypeConversionsProto.MyMessage.newBuilder()
      value.toProto(valueBuilder)
      builder.putI(key, valueBuilder.build())
    }
    builder.j = this.j
    builder.k = this.k.toProto()
    this.l.toProto(builder.lBuilder)
  }
}

enum class MyEnum(val tag: Int) {
  HELLO(0),
  GOOD(1);

  companion object {
    val defaultValue = MyEnum.HELLO

    fun fromProto(proto: TypeConversionsProto.MyEnum): MyEnum =
      values().find { it.tag == proto.number }!!
  }

  fun toProto(): TypeConversionsProto.MyEnum =
    TypeConversionsProto.MyEnum.forNumber(tag)
}

data class MyMessage(
  var message: String,
) {
  companion object {
    fun create(
      message: String = "",
    ) = MyMessage(
      message,
    )

    fun fromProto(proto: TypeConversionsProto.MyMessage): MyMessage {
      val instance = MyMessage(
        message = proto.message,
      )
      return instance
    }
  }

  fun toProto(builder: TypeConversionsProto.MyMessage.Builder) {
    builder.message = this.message
  }
}

