package com.giyeok.sugarproto.sugarformat

import PhoclubConfig
import Test1
import com.giyeok.bibix.repo.BibixRepoProto.BibixTargetLogs
import com.giyeok.bibix.repo.BibixRepoProto.TargetState
import com.giyeok.bibix.runner.RunConfigProto.RunConfig
import com.google.common.truth.Truth.assertThat
import com.google.protobuf.Message
import org.junit.jupiter.api.Test

class SugarFormatTest {
  private fun <B: Message.Builder> test(
    source: String,
    builderFunc: () -> B
  ) {
    println(source)
    println()

    println("=====")

    val parsed = SugarFormat.merge(source, builderFunc()).build()
    println(parsed)

    println("=====")

    val printed = SugarFormat.print(parsed)

    val parsedAgain = SugarFormat.merge(printed, builderFunc()).build()
    val printedAgain = SugarFormat.print(parsedAgain)
    assertThat(printed).isEqualTo(printedAgain)
  }

  @Test
  fun testHello() {
    val source = """
      world: hello
      hello:
      - "world"
      - "foo"
      - "bar good"
      # hello: [abc, def]
      timestamp: 2023-11-06T09:15Z
      duration: 30h30.00144s

      rep_timestamp:
      - 2023-11-06T09:15Z
      - 2023-11-06T09:15Z
      - 2023-11-06T09:15Z
      - 2023-11-06T09:15Z
      rep_duration:
      - 30h30.00144s
      - 60h30.00144s
      - 90h30.00144s
      
      #names_map.hello: abc
      greeting.greeting: "Hello!"
      greetings:
      - greeting: "안녕하세요"
        farewell: "안녕히 가세요"
      - greeting: "곤니찌와"
        farewell: "바이바이"
      - greeting: "니하오"
        farewell: "짜이찌엔"
    """.trimIndent()

    test(source, Test1.Hello::newBuilder)
  }

  @Test
  fun testRepeatedTimestamps() {
    val source = """
      rep_timestamp:
      - 2023-11-06T09:15Z
      - 2023-11-06T09:15Z
      - 2023-11-06T09:15Z
      - 2023-11-06T09:15Z
      rep_duration:
      - 30h30.00144s
      - 60h30.00144s
      - 90h30.00144s
    """.trimIndent()

    test(source, Test1.Hello::newBuilder)
  }

  @Test
  fun testEmptyList() {
    val source = """
      hello: []
      greetings: []
    """.trimIndent()

    test(source, Test1.Hello::newBuilder)
  }

  @Test
  fun testSingularList() {
    val source = """
      hello:
      - "world"
      greetings:
      - greeting: "안녕하세요"
        farewell: "안녕히 가세요"
    """.trimIndent()

    test(source, Test1.Hello::newBuilder)
  }

  @Test
  fun testPhoclubServerConfig() {
    val source = """
      port: 1234
      firebase_credential_file_path: "abc"
      event_assigner_threads_count: 4
      dev_database:
        location: ""
        username: ""
        password: ""
      aws_s3_config:
        access_key_id: "123"
        secret_access_key: "secret"
        upload_expire_time: 30h
        download_expire_time: 20m
    """.trimIndent()

    test(source, PhoclubConfig.ServerConfig::newBuilder)
  }

  @Test
  fun testSimpleMessage() {
    val source = """
      port: 1234
      firebase_credential_file_path: "abc"
      event_assigner_threads_count: 4
      dev_database: { location: "password", username: "location", password: "username" }
    """.trimIndent()

    test(source, PhoclubConfig.ServerConfig::newBuilder)
  }

  @Test
  fun testBibixRunConfig() {
    val source = """
      max_threads: 8
      min_log_level: INFO
      target_result_reuse_duration: 10d
    """.trimIndent()

    test(source, RunConfig::newBuilder)
  }

  @Test
  fun testBibixTargetStates() {
    val source = """
      unique_run_id: "123123"
      build_start_time: 2023-11-07T11:15:02Z
      input_hashes:
        directories:
        - path: "blahblah"
          files:
          - path: "blaa"
            last_modified_time: 2023-11-07T11:10:02Z
            size: 123123
            sha1_hash: b"123123"
      input_hash_string: h"111111"
      build_succeeded:
        build_end_time: 2023-11-07T11:15:15Z
    """.trimIndent()

    test(source, TargetState::newBuilder)
  }

  @Test
  fun testBibixTargetLogs() {
    val source = """
      target_logs:
      - unique_run_id: "123123"
        target_id: "321321"
        blocks:
        - level: INFO
          time: 2023-11-04T11:11Z
          message: "Hello\n"
                   "Foobar"
    """.trimIndent()

    test(source, BibixTargetLogs::newBuilder)
  }

  @Test
  fun testIntTypes() {
    val x1 = Test1.IntTypes.newBuilder()
    x1.a = 123123
    x1.b = 123123
    x1.c = 123123
    x1.d = 123123
    x1.e = 123123
    x1.f = 123123
    x1.g = 123123
    x1.h = 123123
    x1.i = 123123.0f
    x1.j = 123123.0
    x1.k = true
    x1.l = false

    val p1 = SugarFormat.print(x1)
    println(p1)

    val x2 = Test1.IntTypes.newBuilder()
    x2.a = -123123
    x2.b = -123123
    x2.c = -123123
    x2.d = -123123
    x2.e = -123123
    x2.f = -123123
    x2.g = -123123
    x2.h = -123123
    x2.i = -123123.0f
    x2.j = -123123.0
    x2.k = true
    x2.l = false

    val p2 = SugarFormat.print(x2)
    println(p2)

    val y1 = SugarFormat.merge(
      """
      a: 321321
      b: 321321
      c: 321321
      d: 321321
      e: 321321
      f: 321321
      g: 321321
      h: 321321
      i: 321321
      j: 321321
      k: true
      l: false
    """.trimIndent(), Test1.IntTypes.newBuilder()
    ).build()
    assertThat(y1.a).isEqualTo(321321)
    assertThat(y1.b).isEqualTo(321321)
    assertThat(y1.c).isEqualTo(321321)
    assertThat(y1.d).isEqualTo(321321)
    assertThat(y1.e).isEqualTo(321321)
    assertThat(y1.f).isEqualTo(321321)
    assertThat(y1.g).isEqualTo(321321)
    assertThat(y1.h).isEqualTo(321321)
    assertThat(y1.i).isEqualTo(321321.0f)
    assertThat(y1.j).isEqualTo(321321.0)
    assertThat(y1.k).isTrue()
    assertThat(y1.l).isFalse()

    val y2 = SugarFormat.merge(
      """
      a: -321321
      b: -321321
      c: -321321
      d: -321321
      e: -321321
      f: -321321
      g: -321321
      h: -321321
      i: -321321.123e-2
      j: -321321.123e-2
      k: true
      l: false
    """.trimIndent(), Test1.IntTypes.newBuilder()
    ).build()
    assertThat(y2.a).isEqualTo(-321321)
    assertThat(y2.b).isEqualTo(-321321)
    assertThat(y2.c).isEqualTo(-321321)
    assertThat(y2.d).isEqualTo(-321321)
    assertThat(y2.e).isEqualTo(-321321)
    assertThat(y2.f).isEqualTo(-321321)
    assertThat(y2.g).isEqualTo(-321321)
    assertThat(y2.h).isEqualTo(-321321)
    assertThat(y2.i).isEqualTo(-3213.21123f)
    assertThat(y2.j).isEqualTo(-3213.21123)
    assertThat(y2.k).isTrue()
    assertThat(y2.l).isFalse()

    val y3 = SugarFormat.merge(
      """
      a: "321321"
      b: "321321"
      c: "321321"
      d: "321321"
      e: "321321"
      f: "321321"
      g: "321321"
      h: "321321"
      i: "321321.0"
      j: "321321.0"
      k: true
      l: false
    """.trimIndent(), Test1.IntTypes.newBuilder()
    ).build()
    assertThat(y3.a).isEqualTo(321321)
    assertThat(y3.b).isEqualTo(321321)
    assertThat(y3.c).isEqualTo(321321)
    assertThat(y3.d).isEqualTo(321321)
    assertThat(y3.e).isEqualTo(321321)
    assertThat(y3.f).isEqualTo(321321)
    assertThat(y3.g).isEqualTo(321321)
    assertThat(y3.h).isEqualTo(321321)
    assertThat(y3.i).isEqualTo(321321.0f)
    assertThat(y3.j).isEqualTo(321321.0)
    assertThat(y3.k).isTrue()
    assertThat(y3.l).isFalse()
  }
}
