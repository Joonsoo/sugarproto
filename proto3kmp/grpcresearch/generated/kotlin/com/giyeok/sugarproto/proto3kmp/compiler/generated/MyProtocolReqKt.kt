// Generated by the protocol buffer compiler. DO NOT EDIT!
// source: test.proto

// Generated files should ignore deprecation warnings
@file:Suppress("DEPRECATION")
package com.giyeok.sugarproto.proto3kmp.compiler.generated;

@kotlin.jvm.JvmName("-initializemyProtocolReq")
public inline fun myProtocolReq(block: com.giyeok.sugarproto.proto3kmp.generated.MyProtocolReqKt.Dsl.() -> kotlin.Unit): com.giyeok.sugarproto.proto3kmp.compiler.generated.Test.MyProtocolReq =
  com.giyeok.sugarproto.proto3kmp.generated.MyProtocolReqKt.Dsl._create(com.giyeok.sugarproto.proto3kmp.compiler.generated.Test.MyProtocolReq.newBuilder()).apply { block() }._build()
/**
 * Protobuf type `com.giyeok.sugarproto.proto3kmp.generated.MyProtocolReq`
 */
public object MyProtocolReqKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  public class Dsl private constructor(
    private val _builder: com.giyeok.sugarproto.proto3kmp.compiler.generated.Test.MyProtocolReq.Builder
  ) {
    public companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.giyeok.sugarproto.proto3kmp.compiler.generated.Test.MyProtocolReq.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.giyeok.sugarproto.proto3kmp.compiler.generated.Test.MyProtocolReq = _builder.build()

    /**
     * `string name = 1;`
     */
    public var name: kotlin.String
      @JvmName("getName")
      get() = _builder.getName()
      @JvmName("setName")
      set(value) {
        _builder.setName(value)
      }
    /**
     * `string name = 1;`
     */
    public fun clearName() {
      _builder.clearName()
    }

    /**
     * `int32 age = 2;`
     */
    public var age: kotlin.Int
      @JvmName("getAge")
      get() = _builder.getAge()
      @JvmName("setAge")
      set(value) {
        _builder.setAge(value)
      }
    /**
     * `int32 age = 2;`
     */
    public fun clearAge() {
      _builder.clearAge()
    }
  }
}
@kotlin.jvm.JvmSynthetic
@com.google.errorprone.annotations.CheckReturnValue
public inline fun com.giyeok.sugarproto.proto3kmp.compiler.generated.Test.MyProtocolReq.copy(block: com.giyeok.sugarproto.proto3kmp.generated.MyProtocolReqKt.Dsl.() -> kotlin.Unit): com.giyeok.sugarproto.proto3kmp.compiler.generated.Test.MyProtocolReq =
  com.giyeok.sugarproto.proto3kmp.generated.MyProtocolReqKt.Dsl._create(this.toBuilder()).apply { block() }._build()

