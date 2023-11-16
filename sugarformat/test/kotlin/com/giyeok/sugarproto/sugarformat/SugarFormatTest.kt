package com.giyeok.sugarproto.sugarformat

import PhoclubConfig
import Test1
import com.giyeok.bibix.repo.BibixRepoProto.BibixTargetLogs
import com.giyeok.bibix.repo.BibixRepoProto.TargetState
import com.giyeok.bibix.runner.RunConfigProto.RunConfig
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

    println(SugarFormat.print(parsed))
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
}
