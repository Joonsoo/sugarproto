package com.giyeok.sugarproto.proto

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.name.SemanticName

data class ProtoDefs(
  val comments: List<SugarProtoAst.Comment>,
  val packageName: String?,
  val imports: List<ImportDef>,
  val emptyRequired: Boolean,
  val options: List<OptionDef>,
  val defs: List<ProtoDef>,
  // sealed 내부에서 정의된 on the fly message/sealed가 있을 경우
  // 하위 클래스 -> (상위 클래스 이름, 해당 필드 이름)의 맵
  val sealedSupers: Map<SemanticName, SuperName>,
  val trailingComments: List<SugarProtoAst.Comment>,
  val kotlinPackageName: String?,
  val kotlinImports: Set<String>,
)

data class SuperName(
  val superClassName: SemanticName,
  val fieldName: SemanticName,
)

data class ImportDef(
  val comments: List<SugarProtoAst.Comment>,
  val import: String,
)

data class OptionDef(
  val comments: List<SugarProtoAst.Comment>,
  val optionDef: SugarProtoAst.OptionDef,
)

sealed class ProtoDef

data class ProtoMessageDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: SemanticName,
  val members: List<ProtoMessageMember>,
  val trailingComments: List<SugarProtoAst.Comment>,
): ProtoDef()

sealed class ProtoMessageMember {
  data class MessageOption(val option: OptionDef): ProtoMessageMember()

  data class MessageField(
    val comments: List<SugarProtoAst.Comment>,
    val name: SemanticName,
    val type: ValueType,
    val tag: SugarProtoAst.IntLiteral,
    val options: SugarProtoAst.FieldOptions?
  ): ProtoMessageMember()

  data class OneOf(
    val comments: List<SugarProtoAst.Comment>,
    val name: SemanticName,
    val members: List<ProtoOneOfMember>
  ): ProtoMessageMember()

  data class NestedMessage(
    val messageDef: ProtoMessageDef
  ): ProtoMessageMember()

  data class NestedEnum(
    val enumDef: ProtoEnumDef
  ): ProtoMessageMember()

  data class Reserved(
    val comments: List<SugarProtoAst.Comment>,
    val ranges: List<SugarProtoAst.ReservedItem>
  ): ProtoMessageMember()
}

sealed class ProtoOneOfMember {
  data class OneOfOption(val option: OptionDef): ProtoOneOfMember()
  data class OneOfField(val field: ProtoMessageMember.MessageField): ProtoOneOfMember()
}

data class ProtoSealedDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: SemanticName,
  val commonFields: List<ProtoMessageMember.MessageField>,
  val sealedFields: List<ProtoMessageMember.MessageField>,
  val trailingComments: List<SugarProtoAst.Comment>,
): ProtoDef()

data class ProtoEnumDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: SemanticName,
  val members: List<ProtoEnumMember>,
  val trailingComments: List<SugarProtoAst.Comment>,
): ProtoDef()

sealed class ProtoEnumMember {
  data class EnumOption(val option: OptionDef): ProtoEnumMember()
  data class EnumField(
    val comments: List<SugarProtoAst.Comment>,
    val name: SemanticName,
    val minusTag: Boolean,
    val tag: SugarProtoAst.IntLiteral,
    val options: SugarProtoAst.FieldOptions?,
  ): ProtoEnumMember()
}

data class ProtoServiceDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: SemanticName,
  val members: List<ProtoServiceMember>,
  val trailingComments: List<SugarProtoAst.Comment>,
): ProtoDef()

sealed class ProtoServiceMember {
  data class ServiceOption(val option: OptionDef): ProtoServiceMember()
  data class ServiceRpc(
    val comments: List<SugarProtoAst.Comment>,
    val name: SemanticName,
    val inType: ProtoType,
    val outType: ProtoType,
    val options: SugarProtoAst.FieldOptions?
  ): ProtoServiceMember()
}
