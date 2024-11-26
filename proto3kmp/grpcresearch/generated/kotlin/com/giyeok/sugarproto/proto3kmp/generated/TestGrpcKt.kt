package com.giyeok.sugarproto.proto3kmp.generated

import com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestServiceGrpc.getServiceDescriptor
import io.grpc.CallOptions
import io.grpc.CallOptions.DEFAULT
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServerServiceDefinition.builder
import io.grpc.ServiceDescriptor
import io.grpc.Status.UNIMPLEMENTED
import io.grpc.StatusException
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.String
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Holder for Kotlin coroutine-based client and server APIs for
 * com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestService.
 */
public object Proto3KmpTestServiceGrpcKt {
  public const val SERVICE_NAME: String = Proto3KmpTestServiceGrpc.SERVICE_NAME

  @JvmStatic
  public val serviceDescriptor: ServiceDescriptor
    get() = Proto3KmpTestServiceGrpc.getServiceDescriptor()

  public val myProtocolMethod: MethodDescriptor<Test.MyProtocolReq, Test.MyProtocolRes>
    @JvmStatic
    get() = Proto3KmpTestServiceGrpc.getMyProtocolMethod()

  /**
   * A stub for issuing RPCs to a(n) com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestService
   * service as suspending coroutines.
   */
  @StubFor(Proto3KmpTestServiceGrpc::class)
  public class Proto3KmpTestServiceCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT,
  ) : AbstractCoroutineStub<Proto3KmpTestServiceCoroutineStub>(channel, callOptions) {
    public override fun build(channel: Channel, callOptions: CallOptions):
        Proto3KmpTestServiceCoroutineStub = Proto3KmpTestServiceCoroutineStub(channel, callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][io.grpc.Status].  If the RPC completes with another status, a
     * corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun myProtocol(request: Test.MyProtocolReq, headers: Metadata = Metadata()):
        Test.MyProtocolRes = unaryRpc(
      channel,
      Proto3KmpTestServiceGrpc.getMyProtocolMethod(),
      request,
      callOptions,
      headers
    )
  }

  /**
   * Skeletal implementation of the com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestService
   * service based on Kotlin coroutines.
   */
  public abstract class Proto3KmpTestServiceCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for
     * com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestService.MyProtocol.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [io.grpc.Status].  If this method fails with a [java.util.concurrent.CancellationException],
     * the RPC will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun myProtocol(request: Test.MyProtocolReq): Test.MyProtocolRes = throw
        StatusException(UNIMPLEMENTED.withDescription("Method com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestService.MyProtocol is unimplemented"))

    public final override fun bindService(): ServerServiceDefinition =
        builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = Proto3KmpTestServiceGrpc.getMyProtocolMethod(),
      implementation = ::myProtocol
    )).build()
  }
}
