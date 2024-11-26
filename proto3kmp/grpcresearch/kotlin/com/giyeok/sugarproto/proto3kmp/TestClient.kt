package com.giyeok.sugarproto.proto3kmp

import com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestServiceGrpcKt
import com.giyeok.sugarproto.proto3kmp.generated.Test.MyProtocolReq
import com.giyeok.sugarproto.proto3kmp.generated.Test.MyProtocolRes
import com.giyeok.sugarproto.proto3kmp.generated.myProtocolReq
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

fun main() {
  val req = myProtocolReq {
    this.name = "Joonsoo"
    this.age = 36
  }

  println("Grpc::")
  try {
    tryGrpc(req)
  } catch (e: Exception) {
    e.printStackTrace()
  }

  println("Okhttp::")
  try {
    tryOkHttp(req)
  } catch (e: Exception) {
    e.printStackTrace()
  }
}

fun tryGrpc(req: MyProtocolReq) {
  val stub = Proto3KmpTestServiceGrpcKt.Proto3KmpTestServiceCoroutineStub(
    NettyChannelBuilder.forAddress("localhost", 8880)
      .usePlaintext()
      .build()
  )
  runBlocking {
    val res = stub.myProtocol(req)
    println(res)
  }
}

fun tryKtor(msg: MyProtocolReq) {
//  val client = HttpClient(OkHttp)
//
//  runBlocking {
//
//    println(Base64.encode(msgBytes))
//
//    val res = client.post("http://localhost:8880/Proto3KmpTestService/MyProtocol") {
//      contentType(ContentType("application", "grpc+proto"))
////      this.userAgent("grpc-java/1.2.3")
//      this.header("Message-Length", msgBytes.size.toString())
//      setBody(ByteReadChannel(msgBytes, 0, msgBytes.size))
//    }
//    println(res)
//  }
}

fun tryOkHttp(req: MyProtocolReq) {
  val client = OkHttpClient.Builder()
    .protocols(listOf(Protocol.H2_PRIOR_KNOWLEDGE))
    .build()

  val msgBytes = req.toByteArray()
  val payload = ByteArrayOutputStream().use { stream ->
    val len = msgBytes.size
    // Length-Prefixed-Message → Compressed-Flag Message-Length Message
    // Compressed-Flag → 0 / 1 ; encoded as 1 byte unsigned integer
    stream.write(0)
    // Message-Length → {length of Message} ; encoded as 4 byte unsigned integer (big endian)
    stream.write((len shr 24) and 0xff)
    stream.write((len shr 16) and 0xff)
    stream.write((len shr 8) and 0xff)
    stream.write(len and 0xff)
    // Message → *{binary octet}
    stream.writeBytes(msgBytes)
    stream.flush()
    stream.toByteArray()
  }

  val url = "http://localhost:8880/com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestService/MyProtocol".toHttpUrl()
  println(url.scheme)
  println(url.host)
  println(url.encodedPath)
  val req = Request.Builder()
    .url(url)
    .header("te", "trailers")
    .header("grpc-accept-encoding", "gzip")
    // .header("content-length", payload.size.toString())
    .header("content-type", "application/grpc")
    .header("user-agent", "grpc-java-netty/1.59.0")
    .removeHeader("Connection")
    .removeHeader("Accept-Encoding")
    .post(payload.toRequestBody())
  val res = client.newCall(req.build()).execute()

  res.body?.byteStream()?.buffered()?.use { stream ->
    val compressed = stream.read()
    val len1 = stream.read()
    val len2 = stream.read()
    val len3 = stream.read()
    val len4 = stream.read()
    val len = (len1 shl 24) or (len2 shl 16) or (len3 shl 8) or len4
    println("Compressed: $compressed Length: $len")
    val res = MyProtocolRes.parseFrom(stream)
    println(res)
  }
}
