package com.giyeok.sugarproto.proto3kmp

import com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestServiceGrpcKt
import com.giyeok.sugarproto.proto3kmp.generated.Test
import com.giyeok.sugarproto.proto3kmp.generated.myProtocolRes
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import io.ktor.server.engine.*
import io.ktor.server.jetty.jakarta.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.util.concurrent.Executors

class Proto3KmpTestServiceImpl: Proto3KmpTestServiceGrpcKt.Proto3KmpTestServiceCoroutineImplBase() {
  @OptIn(ExperimentalStdlibApi::class)
  override suspend fun myProtocol(request: Test.MyProtocolReq): Test.MyProtocolRes {
    println(request)
    return myProtocolRes {
      this.address = "hello world! ${request.toByteArray().toHexString()}"
    }
  }
}

fun runGrpcServer() {
  val server = NettyServerBuilder.forPort(8880)
    .addService(Proto3KmpTestServiceImpl())
    .executor(Executors.newFixedThreadPool(32))
    .build()
  server.start().awaitTermination()
}

@OptIn(ExperimentalStdlibApi::class)
fun runKtorServer() {
  embeddedServer(Jetty, port = 8880) {
    routing {
//      post("*") {
//        println(call.request.path())
//      }
//      post("*/*") {
//        println(call.request.path())
//      }
      post("/com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestService/MyProtocol") {
        println(call.request.path())
        println(call.request)
        println(call.request.httpVersion)
        println(call.request.pathVariables)
        for (entry in call.request.headers.entries()) {
          println("${entry.key}: ${entry.value}")
        }
        call.receiveStream().use { stream ->
          val bytes = stream.readAllBytes()
          println(bytes.toHexString())
        }
      }
    }
  }.start(wait = true)
}

fun main() {
  runGrpcServer()
//  runKtorServer()
}
