package com.giyeok.sugarproto.proto3kmp.compiler.generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.53.0)",
    comments = "Source: test.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class Proto3KmpTestServiceGrpc {

  private Proto3KmpTestServiceGrpc() {}

  public static final String SERVICE_NAME = "com.giyeok.sugarproto.proto3kmp.generated.Proto3KmpTestService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<Test.MyProtocolReq,
      Test.MyProtocolRes> getMyProtocolMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MyProtocol",
      requestType = Test.MyProtocolReq.class,
      responseType = Test.MyProtocolRes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Test.MyProtocolReq,
      Test.MyProtocolRes> getMyProtocolMethod() {
    io.grpc.MethodDescriptor<Test.MyProtocolReq, Test.MyProtocolRes> getMyProtocolMethod;
    if ((getMyProtocolMethod = Proto3KmpTestServiceGrpc.getMyProtocolMethod) == null) {
      synchronized (Proto3KmpTestServiceGrpc.class) {
        if ((getMyProtocolMethod = Proto3KmpTestServiceGrpc.getMyProtocolMethod) == null) {
          Proto3KmpTestServiceGrpc.getMyProtocolMethod = getMyProtocolMethod =
              io.grpc.MethodDescriptor.<Test.MyProtocolReq, Test.MyProtocolRes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MyProtocol"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Test.MyProtocolReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Test.MyProtocolRes.getDefaultInstance()))
              .setSchemaDescriptor(new Proto3KmpTestServiceMethodDescriptorSupplier("MyProtocol"))
              .build();
        }
      }
    }
    return getMyProtocolMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static Proto3KmpTestServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<Proto3KmpTestServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<Proto3KmpTestServiceStub>() {
        @java.lang.Override
        public Proto3KmpTestServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new Proto3KmpTestServiceStub(channel, callOptions);
        }
      };
    return Proto3KmpTestServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static Proto3KmpTestServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<Proto3KmpTestServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<Proto3KmpTestServiceBlockingStub>() {
        @java.lang.Override
        public Proto3KmpTestServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new Proto3KmpTestServiceBlockingStub(channel, callOptions);
        }
      };
    return Proto3KmpTestServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static Proto3KmpTestServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<Proto3KmpTestServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<Proto3KmpTestServiceFutureStub>() {
        @java.lang.Override
        public Proto3KmpTestServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new Proto3KmpTestServiceFutureStub(channel, callOptions);
        }
      };
    return Proto3KmpTestServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class Proto3KmpTestServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void myProtocol(Test.MyProtocolReq request,
                           io.grpc.stub.StreamObserver<Test.MyProtocolRes> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMyProtocolMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getMyProtocolMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                Test.MyProtocolReq,
                Test.MyProtocolRes>(
                  this, METHODID_MY_PROTOCOL)))
          .build();
    }
  }

  /**
   */
  public static final class Proto3KmpTestServiceStub extends io.grpc.stub.AbstractAsyncStub<Proto3KmpTestServiceStub> {
    private Proto3KmpTestServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected Proto3KmpTestServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new Proto3KmpTestServiceStub(channel, callOptions);
    }

    /**
     */
    public void myProtocol(Test.MyProtocolReq request,
                           io.grpc.stub.StreamObserver<Test.MyProtocolRes> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMyProtocolMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class Proto3KmpTestServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<Proto3KmpTestServiceBlockingStub> {
    private Proto3KmpTestServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected Proto3KmpTestServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new Proto3KmpTestServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public Test.MyProtocolRes myProtocol(Test.MyProtocolReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMyProtocolMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class Proto3KmpTestServiceFutureStub extends io.grpc.stub.AbstractFutureStub<Proto3KmpTestServiceFutureStub> {
    private Proto3KmpTestServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected Proto3KmpTestServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new Proto3KmpTestServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Test.MyProtocolRes> myProtocol(
        Test.MyProtocolReq request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMyProtocolMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_MY_PROTOCOL = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final Proto3KmpTestServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(Proto3KmpTestServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_MY_PROTOCOL:
          serviceImpl.myProtocol((Test.MyProtocolReq) request,
              (io.grpc.stub.StreamObserver<Test.MyProtocolRes>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class Proto3KmpTestServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    Proto3KmpTestServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return Test.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Proto3KmpTestService");
    }
  }

  private static final class Proto3KmpTestServiceFileDescriptorSupplier
      extends Proto3KmpTestServiceBaseDescriptorSupplier {
    Proto3KmpTestServiceFileDescriptorSupplier() {}
  }

  private static final class Proto3KmpTestServiceMethodDescriptorSupplier
      extends Proto3KmpTestServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    Proto3KmpTestServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (Proto3KmpTestServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new Proto3KmpTestServiceFileDescriptorSupplier())
              .addMethod(getMyProtocolMethod())
              .build();
        }
      }
    }
    return result;
  }
}
