package com.giyeok.sugarproto.proto3kmp.generated

import com.giyeok.sugarproto.proto3kmp.GeneratedGrpcService
import com.giyeok.sugarproto.proto3kmp.ServiceDescriptor
import com.giyeok.sugarproto.proto3kmp.ServiceRpcDescriptor
import io.ktor.client.HttpClient

class Proto3KmpTestServiceClient(
  channel: HttpClient,
  serverHost: String,
  serverPort: Int,
): GeneratedGrpcService(channel, serverHost, serverPort) {
  companion object {
    val serviceDescriptor: ServiceDescriptor = ServiceDescriptor(
      "com.giyeok.sugarproto.proto3kmp.generated",
      "Proto3KmpTestService",
      listOf(
        ServiceRpcDescriptor("MyProtocol", false, "com.giyeok.sugarproto.proto3kmp.generated.MyProtocolReq", false, "com.giyeok.sugarproto.proto3kmp.generated.MyProtocolRes"),
      )
    )
  }

  suspend fun myProtocol(request: com.giyeok.sugarproto.proto3kmp.generated.MyProtocolReq): com.giyeok.sugarproto.proto3kmp.generated.MyProtocolRes {
    val resPairs = unaryRequest("com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestService/MyProtocol", request.serialize())
    val resBuilder = com.giyeok.sugarproto.proto3kmp.generated.MyProtocolRes.Builder()
    resBuilder.setFromEncodingResult(resPairs)
    return resBuilder.build()
  }
}
