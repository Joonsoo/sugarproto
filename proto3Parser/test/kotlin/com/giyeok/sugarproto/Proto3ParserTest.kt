package com.giyeok.sugarproto

import org.junit.jupiter.api.Test

class Proto3ParserTest {
  @Test
  fun test() {
    val source = """
    syntax = "proto3";
    
    package com.giyeok.datclub3.proto;
    
    import "google/protobuf/duration.proto";
    import "google/protobuf/empty.proto";
    import "google/protobuf/timestamp.proto";
    
    option java_multiple_files = true;
    
    message UserKey {
      int64 id = 1;
    }
    // enum ReactType = List(LIKE, DISLIKE)
    enum ReactType {
      REACT_TYPE_UNSPECIFIED = 0;
      REACT_TYPE_LIKE = 1;
      REACT_TYPE_DISLIKE = 2;
    }
  """.trimIndent()

    val parsed = Proto3Parser.parse(source)
    println(parsed)
  }
}
