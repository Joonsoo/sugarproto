package com.giyeok.sugarproto.proto3kmp.example

import com.giyeok.sugarproto.proto3kmp.GrpcService
import com.giyeok.sugarproto.proto3kmp.ServiceDescriptor
import com.giyeok.sugarproto.proto3kmp.ServiceRpcDescriptor
import com.giyeok.sugarproto.proto3kmp.parseBinary
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ExampleGeneratedServiceClient(
  channel: HttpClient,
  val serverHost: String,
  val serverPort: Int
): GrpcService(channel) {
  override val serviceDescriptor: ServiceDescriptor = ServiceDescriptor(
    "", "Example",
    listOf(
      ServiceRpcDescriptor("SigninByFirebaseId", false, Pair("", ""), false, Pair("", ""))
    )
  )

  suspend fun signinByFirebaseId(request: ExampleGeneratedMessage): ExampleGeneratedMessage {
    val serialized = request.serialize()
    val size = serialized.calculateSerializedSize()
    val reqBytes = ByteArray(size + 5)
    reqBytes[0] = 0
    reqBytes[1] = ((size shr 24) and 0xff).toByte()
    reqBytes[2] = ((size shr 16) and 0xff).toByte()
    reqBytes[3] = ((size shr 8) and 0xff).toByte()
    reqBytes[4] = (size and 0xff).toByte()
    serialized.writeTo(reqBytes, 5)
    val reqBuilder = HttpRequestBuilder {
      this.host = serverHost
      this.port = serverPort
      this.path("/abc.Example/SigninByFirebaseId")
    }
    reqBuilder.setBody(reqBytes)
    val response = channel.post(reqBuilder)
    val resBytes = response.bodyAsBytes()
    check(resBytes[0] == 0.toByte())
    val resSize = resBytes[1].toInt() and 0xff // TODO
    check(resBytes.size == resSize + 5)
    val resBuilder = ExampleGeneratedMessage.Builder()
    val pairs = parseBinary(resBytes, 5)
    resBuilder.setFromEncodingResult(pairs)
    return resBuilder.build()
  }
}
