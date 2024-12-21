package com.giyeok.sugarproto.proto3kmp

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

abstract class GeneratedGrpcService(
  val channel: HttpClient,
  val serverHost: String,
  val serverPort: Int
) {
  suspend fun unaryRequest(
    path: String,
    serializedReq: MessageEncodingResult
  ): MessageEncodingResult {
    val size = serializedReq.calculateSerializedSize()
    val bytes = ByteArray(size + 5)
    bytes[0] = 0
    writeLength(bytes, 1, size)
    serializedReq.writeTo(bytes, 5)
    val reqBuilder = HttpRequestBuilder {
      this.host = serverHost
      this.port = serverPort
      this.path(path)
    }
    reqBuilder.setBody(bytes)
    val response = channel.post(reqBuilder)
    val resBytes = response.bodyAsBytes()
    check(resBytes[0] == 0.toByte())
//    val resSize = resBytes[1].toInt() and 0xff // TODO
//    check(resBytes.size == resSize + 5)
    return parseBinary(resBytes, 5)
  }
}

fun writeLength(bytes: ByteArray, offset: Int, size: Int) {
  bytes[offset] = ((size shr 16) and 0xff).toByte()
  bytes[offset + 1] = ((size shr 16) and 0xff).toByte()
  bytes[offset + 2] = ((size shr 8) and 0xff).toByte()
  bytes[offset + 3] = (size and 0xff).toByte()
}

data class ServiceDescriptor(
  val pkg: String,
  val name: String,
  val methods: List<ServiceRpcDescriptor>
)

data class ServiceRpcDescriptor(
  val name: String,
  val streamRequest: Boolean,
  val requestTypeName: String,
  val streamResponse: Boolean,
  val responseTypeName: String,
)
