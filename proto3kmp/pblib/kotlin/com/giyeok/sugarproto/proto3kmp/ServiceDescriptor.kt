package com.giyeok.sugarproto.proto3kmp

import io.ktor.client.*

abstract class GrpcService(val channel: HttpClient) {
  abstract val serviceDescriptor: ServiceDescriptor
}

data class ServiceDescriptor(
  val pkg: String,
  val name: String,
  val methods: List<ServiceRpcDescriptor>
)

data class ServiceRpcDescriptor(
  val name: String,
  val streamRequest: Boolean,
  val requestTypeName: Pair<String, String>,
  val streamResponse: Boolean,
  val responseTypeName: Pair<String, String>,
)
