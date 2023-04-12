# sugarproto

Protocol Buffer schema 문법을 좀더 사용하기 쉽게 개선하는 것이 목표이다. 기존 protocol buffer를 최대한 활용한다. 그래서 직접 코드를 생성하거나 인코딩 방식을 변경하거나 하지는 않고, sugarproto 정의를 주면 proto3 문법의 .proto 파일을 생성하고, 그 이후로는 프로토버프를 그대로 사용한다.

### 개선 1. service에서 rpc의 request와 response 타입을 rpc 정의에 함께 쓸 수 있다

이 프로젝트를 시작한 가장 큰 동기는 여러 개의 rpc를 지원하는 service를 정의할 때 rpc의 request와 response 타입을 한눈에 확인하기 어렵다는 점 때문이었다.

```
service MyService {
  rpc HelloWorld(HelloWorldReq) returns (HelloWorldRes);
}

message HelloWorldReq {
  string message = 1;
}

message HelloWorldRes {
  string response = 1;
}
```

위와 같은 코드에서, MyService의 rpc가 한 화면에 담기지 않을 만큼 많아진다면? 서비스의 rpc 목록을 보다가 HelloWorldReq와 HelloWorldRes를 확인하기 위해 파일의 한참 아래쪽으로 모험을 떠나야 한다. sugarproto에서는 위의 코드를 다음과 같이 간단하게 쓸 수 있다.

```
service MyService {
  rpc helloWorld: HelloWorldReq {1 message: string} -> HelloWorldRes {1 response: string}
}
```

보다시피 helloWorld라는 rpc의 request 타입과 response 타입을 rpc 정의 내부에 적을 수 있다. 이 코드를 컴파일하면 위의 프로토 정의와 동일한 코드가 나온다.

필드가 여러개인 메시지의 경우에도 사용법은 동일하다.

```
service MyService {
  rpc helloWorld: HelloWorldReq {
    1 message: string
    2 sender: string
  } -> HelloWorldRes {
    1 response: string
    2 responder: string
  }
}
```

### 개선 2. message 타입을 on the fly로 정의할 수 있다

사실 sugarproto에서도 기존의 방식처럼 메시지 이름으로 rpc 시그니쳐를 정의할 수 있다.

```
service MyService {
  rpc helloWorld: HelloWorldReq -> HelloWorldRes
}
```

다만 여기서 HelloWorldReq나 HelloWorldRes 메시지의 정의를 별도의 위치에 하지 않고, helloWorld의 시그니쳐 정의 안에 넣을 수 있는 것이다. 이것을 메시지 타입을 on the fly로 정의한다고 부르기로 한다.

on the fly로 메시지를 정의할 때 메시지의 이름을 생략할 수도 있다.

```
rpc helloWorld: {1 message: string} -> {1 response: string}
```

이런 경우 "적당하게" 메시지에 이름을 붙여주는데, 위의 경우 "MyServiceHelloWorldReq"와 "MyServiceHelloWorldRes"가 된다.

### 개선(?) 3. 사소한 문법 스타일 변경

취향의 영역이긴 하지만, 내가 보기에는 프로토버프의 스키마 정의 문법이 다소 올드한 느낌이 있다. 세미콜론을 사용하는 것도 그렇고, `타입 이름` 순서로 나오는 것도 그렇고 별로 내 취향이 아니라서 바꾸어 보았다. 세미콜론을 사용하지 않고(세미콜론을 사용하면 오류가 발생한다), `이름: 타입`의 형태로 쓰게 바꿨으며, `= 태그` 형태도 별로 마음에 안 들어서 태그를 필드 이름 앞에 나오게 변경했다.

```
protobuf:
string message = 1;

sugarproto:
1 message: string
```

### 개선(?) 4. repeated, optional, stream 관련 문법 변경

역시 취향의 영역일 수 있지만, repeated나 optional이 필드의 modifier처럼 들어가는 것이 맘에 들지 않았다. 왜냐하면 이 정보들은 사실 타입 정보의 영역이라고 봐야 하기 때문이다. 그래서 다음과 같이 바꾸었다.

```
message MyMessage {
  1 rep: repeated<string>
  2 opt: optional<string>
}
```

프로토로 변환하면 다음과 같이 된다.

```
message MyMessage {
  repeated string rep = 1;
  optional string opt = 2;
}
```

`repeated<repeated<string>>`, `optional<repeated<string>>` 같은 것은 protobuf 메시지에서 지원하지 않기 때문에 동작하지 않는다. 의미가 다음과 같은 방식으로 이들과 거의 동일한 것을 정의할 수 있다.

```
message MyMessage {
  1 reprep: repeated<{1 rep: repeated<string>}>
  2 optrep: optional<{1 rep: repeated<string>}>
}
```

그러면 다음과 같은 프로토 메시지 정의가 나온다.

```
message MyMessage {
  repeated MyMessageReprep reprep = 1;
  optional MyMessageOptrep optrep = 2;
}

message MyMessageReprep {
  repeated string rep = 1;
}

message MyMessageOptrep {
  repeated string rep = 1;
}
```

이처럼 `repeated<repeated<string>>`같은 타입을 만났을 때 `repeated<string>` 필드를 갖는 메시지를 자동으로 만들어서 처리해줄 수도 있긴 한데, 사용 빈도가 많은 것 같지는 않아서 우선 그냥 지원하지 않는 상태로 두기로 한다.

비슷하게, rpc의 request나 response에 stream을 넣는 것도 다음과 같이 문법을 바꾸었다.

```
service MyService {
  rpc streamingService: stream<StreamingServiceReq> -> stream<StreamingServiceRes>
}
```

물론 여기서 `StreamingServiceReq`나 `StreamingServiceRes` 자리에 on-the-fly message 정의도 넣을 수 있다.

```
service MyService {
  rpc streamingService: stream<ReqMessage {1 message: string}> -> stream<ResMessage {1 detail: string}>
}
```

### 개선 5. sealed 타입 추가

수요가 얼마나 있을지는 모르겠지만, 내 경우엔 다음과 같은 형태의 message를 정의하는 경우가 왕왕 있었다.

```
message Ast {
  oneof ast {
    string string_literal = 1;
    AstBinOp bin_op = 2;
    AstUnOp un_op = 3;
  }
}

message AstBinOp {
  string operator = 1;
  Ast lhs = 2;
  Ast rhs = 3;
}

message AstUnOp {
  string operator = 1;
  Ast operand = 2;
}
```

이 코드를 다음과 같이 조금 더 간단하게 쓸 수 있다.

```
sealed Ast {
  1 string_literal: string
  2 bin_op: {
    1 operator: string
    2 lhs: Ast
    3 rhs: Ast
  }
  3 un_op: {
    1 operator: string
    2 operand: Ast
  }
}
```

궁극적인 목표는 sealed class 기능을 지원하는 언어용으로는 unmarshalling할 때 sealed class의 형태로 바꿔주는 코드를 생성하는 것인데.. 일단은 귀찮다.

내 생각에 sealed class를 protobuf에서 유용하게 쓸 수 있을 것 같은 지점 중 하나가 rpc의 streaming output이다.

```
service MyService {
  rpc listenEvents: {1 target: string} -> stream<sealed {
    1 opened: {1 name: string}
    2 closed: {1 name: string}
    3 shutdown: {}
  }>
}
```


### 한계 1. 아무런 체킹을 하지 않는다

필드의 태그가 다른 필드와 겹치지는 않는지, import한 파일이 실제로 존재하는지, message의 이름은 적절한지 등 그 어떠한 체킹도 하지 않는다. proto 파일로 변환한 후에 프로토버프 컴파일러에서 체크해주는 것에 의존한다.


### 한계 2. protobuf의 모든 기능을 지원하지는 않는다

현재는 enum 등은 아예 문법이 없고, 문법에 있어도 지원되지 않는 기능들이 몇가지 있다. 내가 쓰면서 필요할 때마다 추가 구현할 예정.


### 사용법

[bibix](https://github.com/joonsoo/bibix)를 사용해 빌드해야 한다. 현재는 그 외의 별도 인터페이스를 제공하지 않는다.

```
import git("https://github.com/Joonsoo/sugarproto.git") as sugarproto
protoGen = sugarproto.generateProto("schema/api.supro", "api.proto")

action generateApi {
  file.copyFile(protoGen, "generated/proto")
}
```
