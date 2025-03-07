package com.giyeok.sugarproto.sugarformat

import Test1
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.extensions.proto.ProtoTruth.assertThat
import com.google.protobuf.util.Durations
import com.google.protobuf.util.Timestamps
import org.junit.jupiter.api.Test

class RepeatTest {
  @Test
  fun test1() {
    val x = Test1.TimestampAndDurations.newBuilder()
    val rep1 = x.addRepBuilder()
    rep1.timestamp = Timestamps.fromSeconds(1000)
    rep1.duration = Durations.fromDays(2)
    x.addRepBuilder().duration = Durations.fromDays(3)
    x.addRepBuilder().timestamp = Timestamps.fromSeconds(2000)
    x.addRepBuilder().duration = Durations.fromDays(4)
    x.addRepTimestamp(Timestamps.fromSeconds(3000))
    x.addRepTimestamp(Timestamps.fromSeconds(4000))
    x.addRepTimestamp(Timestamps.fromSeconds(5000))
    x.addRepDuration(Durations.fromHours(2))
    x.addRepDuration(Durations.fromHours(3))
    x.addRepDuration(Durations.fromHours(4))
    x.addRepString("hello2")
    x.addRepString("hello3")
    x.addRepString("hello4")

    val p = SugarFormat.print(x)
    println(p)
    assertThat(p).isEqualTo(
      """
        rep:
        - timestamp: 1970-01-01T00:16:40Z
          duration: 2d
        - duration: 3d
        - timestamp: 1970-01-01T00:33:20Z
        - duration: 4d
        rep_timestamp:
        - 1970-01-01T00:50Z
        - 1970-01-01T01:06:40Z
        - 1970-01-01T01:23:20Z
        rep_duration:
        - 2h
        - 3h
        - 4h
        rep_string:
        - hello2
        - hello3
        - hello4
        
      """.trimIndent()
    )

    val y = SugarFormat.merge(p, Test1.TimestampAndDurations.newBuilder()).build()
    assertThat(x.build()).isEqualTo(y)
  }

  @Test
  fun test2() {
    val x = Test1.PullRequestActionLogs.newBuilder()
    val b1 = x.addLogsBuilder()
    b1.timestamp = Timestamps.fromSeconds(1000)
    b1.beforeSplit = "hello"

    val b2 = x.addLogsBuilder()
    b2.afterSplit = "world"

    val p = SugarFormat.print(x)
    println(p)

    val y = SugarFormat.merge(p, Test1.PullRequestActionLogs.newBuilder()).build()
    assertThat(x.build()).isEqualTo(y)
  }
}
