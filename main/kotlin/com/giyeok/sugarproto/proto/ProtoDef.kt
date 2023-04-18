package com.giyeok.sugarproto.proto

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.name.SemanticName

data class ProtoDefs(
  val comments: List<SugarProtoAst.Comment>,
  val packageName: String?,
  val imports: List<ImportDef>,
  val options: List<OptionDef>,
  val defs: ProtoDef,
  val trailingComments: List<SugarProtoAst.Comment>,
  // sealed 내부에서 정의된 on the fly message/sealed가 있을 경우
  // 하위 클래스 -> 상위 클래스 이름의 맵
  val sealedSupers: Map<SemanticName, SemanticName>,
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
  data class OneOfField(val field: ProtoMessageMember): ProtoOneOfMember()
}

sealed class ProtoSealedDef(
  val comments: List<SugarProtoAst.Comment>,
  val name: SemanticName,
  val commonFields: List<ProtoMessageMember.MessageField>,
  val sealedFields: List<ProtoMessageMember.MessageField>,
  val trailingComments: List<SugarProtoAst.Comment>,
  // sealedFields에서 사용되는 타입 중 on the fly로 생성되어서 이 sealed definition에서만 사용될 수 있는 메시지 타입 이름
  // proto 생성 결과물은 동일하지만, kt data class로 생성했을 때 처리가 달라짐
  val exclusiveSealedMessages: Set<SemanticName>,
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
    val inType: ProtoType,
    val outType: ProtoType,
    val options: SugarProtoAst.FieldOptions?
  ): ProtoServiceMember()
}
