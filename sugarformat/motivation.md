

Json format, text format 모두 줄도 많이 차지하고 부피가 큰 듯하여..


```
hello::              # object 시작.
  world: "message"   # hello object의 field
expire: "3h"         # duration과 timestamp <-> 스트링 자동 변환
numberValue: 123
numberValue: 0123    # 8진수
numberValue: 0x123   # 16진수
enumValue: INFO
```

* bytes를 좀더 예쁘게:
  * `b"base64-encoded-bytes"`
  * `h"hex-encoded-bytes"`

list:
```
hello[]
```

repeated와 map을 좀 더 예쁘게 쓸 수는 없을까?

```
repeated string hello = 1;
repeated MyMessage world = 2;

message MyMessage {
  string a = 1;
  string b = 2;
  AnotherMessage c = 3;
  repeated AnotherMessage d = 4;
}

message AnotherMessage {
  int32 e = 1;
}
```

다음과 같이 쓸 수도 있고
```
hello: "good"
hello: "message"
world::
  a: "abc"
  b: "def"
  c:: 
    e: "xyz"
  d::
    e: "asdf"
  d::
    e: "qwer"
world::
  a: "abc"
  b: "def"
  c:: 
    e: "xyz"
  d::
    e: "asdf"
  d::
    e: "qwer"
```

list field 이름 뒤에 []를 붙이고
item들은 - 뒤에 넣을 수도 있다.
-는 list field와 같은 레벨의 indent에 위치해야 한다.
```
hello[]
  - "good"
  - "message"
world[]
- a: "abc"
  b: "def"
  c::
    e: "xyz"
  d[]
  - e: "asdf"
  - e: "qwer"
- a: "abc"
  b: "def"
  c::
    e: "xyz"
  d[]
  - e: "asdf"
  - e: "qwer"
```


필드가 몇 개 없는 단순한 message는 다음과 같이 줄여 쓸 수도 있다.
{} 안에서는 indent 관계 없는 기존 text field와 유사한 문법이 적용된다.
{} 안의 컴마는 생략 가능하다
```
SimpleMessage simple = 1;

message SimpleMessage {
  string a = 1;
  string b = 2;
}

simple: { a: 1, b: 2 }
```

단순한 리스트도 비슷하게 쓸 수 있다
```
list1: ["hello", "world"]
list2: [123, 456]
list3: [{a:"hello"}, {b:"world"}]
```



map<key, value> 타입은 다음과 같이 줄여 쓸 수 있다
```
map<string, SimpleMessage> myMap = 1;

myMap{}
  "simple"::
    a: "hello"
    b: "bar"
  "simple": { a: "foo" b: "bar" }
```



라인의 종류:
  object header
  field value(simple literals & object literal)

  list header
  list item header

  map header
  -> name 대신 map key literal 가능

Empty 의 경우:
```
a::
a: {}
```

두개는 동일한 의미를 갖는다
