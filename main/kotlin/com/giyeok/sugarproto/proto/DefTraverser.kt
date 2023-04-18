package com.giyeok.sugarproto.proto

import com.giyeok.sugarproto.SugarProtoAst

// AST -> ProtoMessageDef
// on the fly message/enum들은 모두 top level에 정의한다
// 특별히 nested로 명시한 경우에만 nested message/enum으로 변환
object DefTraverser {
  fun traverse(ast: SugarProtoAst.CompilationUnit): ProtoDefs {
    TODO()
  }
}
