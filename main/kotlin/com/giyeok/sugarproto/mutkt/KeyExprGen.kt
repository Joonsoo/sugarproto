package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.name.SemanticName

fun SugarProtoAst.KeyExpr.genKeyExpr(inputExpr: String): String =
  when (this) {
    is SugarProtoAst.TargetElem -> inputExpr
    is SugarProtoAst.MemberAccess -> {
      val memberName = SemanticName.messageMember(this.name.name)
      "${this.expr.genKeyExpr(inputExpr)}.${memberName.classFieldName}"
    }
  }
