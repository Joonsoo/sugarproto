package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.name.SemanticName
import com.giyeok.sugarproto.proto.SuperName
import com.giyeok.sugarproto.proto.ValueType

data class KtDefs(
  val comments: List<SugarProtoAst.Comment>,
  val defs: List<KtDef>,
  val sealedSupers: Map<SemanticName, SuperName>,
  val trailingComments: List<SugarProtoAst.Comment>,
  val protoJavaOuterClassName: String,
  val kotlinPackageName: String?,
  val kotlinImports: Set<String>,
)

sealed class KtDef

data class KtEnumClassDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: SemanticName,
  val values: List<EnumValueDef>,
  val trailingComments: List<SugarProtoAst.Comment>,
): KtDef()

data class EnumValueDef(
  val name: SemanticName,
  val minusTag: Boolean,
  val tag: SugarProtoAst.IntLiteral
)

data class KtDataClassDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: SemanticName,
  val superClass: SuperName?,
  val inheritedFields: List<KtFieldDef>,
  val uniqueFields: List<KtFieldDef>,
  val nestedDefs: List<KtDef>,
  val trailingComments: List<SugarProtoAst.Comment>,
): KtDef() {
  val allFields get() = inheritedFields + uniqueFields
}

data class KtFieldDef(
  val comments: List<SugarProtoAst.Comment>,
  // name에서 kotlin 이름과 proto 이름 생성 가능
  val name: SemanticName,
  val type: ValueType,
)

data class KtSealedClassDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: SemanticName,
  val commonFields: List<KtFieldDef>,
  val subTypes: List<KtSealedSubType>,
  val trailingComments: List<SugarProtoAst.Comment>,
): KtDef()

sealed class KtSealedSubType {
  abstract val fieldName: SemanticName

  data class EmptySub(override val fieldName: SemanticName): KtSealedSubType()

  data class SingleSub(
    override val fieldName: SemanticName,
    val fieldDef: KtFieldDef,
  ): KtSealedSubType()

  data class DedicatedMessage(
    override val fieldName: SemanticName,
    val typeName: SemanticName
  ): KtSealedSubType()
}
