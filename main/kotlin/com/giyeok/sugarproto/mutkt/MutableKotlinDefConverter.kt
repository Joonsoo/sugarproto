package com.giyeok.sugarproto.mutkt

import com.giyeok.sugarproto.SugarProtoAst
import com.giyeok.sugarproto.proto.*
import com.giyeok.sugarproto.toValue

// Proto Defs -> Kotlin mutable data class defs
class MutableKotlinDefConverter(val defs: ProtoDefs) {

  fun convertEnum(def: ProtoEnumDef): KtEnumClassDef =
    KtEnumClassDef(
      def.comments,
      def.name,
      def.members.mapNotNull { member ->
        when (member) {
          is ProtoEnumMember.EnumField ->
            // ignore options
            EnumValueDef(member.name, member.minusTag, member.tag)

          is ProtoEnumMember.EnumOption -> {
            // ignore
            null
          }
        }
      },
      def.trailingComments
    )

  fun convertField(member: ProtoMessageMember.MessageField): KtFieldDef {
    return KtFieldDef(member.comments, member.name, member.type)
  }

  fun convertMessage(def: ProtoMessageDef): KtDataClassDef {
    val sealedSuper = defs.sealedSupers[def.name]
    val inheritedFields: List<KtFieldDef> = if (sealedSuper != null) {
      defs.defs.filterIsInstance<ProtoSealedDef>()
        .find { it.name == sealedSuper.superClassName }!!
        .commonFields
        .map { convertField(it) }
    } else {
      listOf()
    }
    val uniqueFields = mutableListOf<KtFieldDef>()
    val nestedDefs = mutableListOf<KtDef>()
    def.members.forEach { member ->
      when (member) {
        is ProtoMessageMember.MessageField -> {
          uniqueFields.add(convertField(member))
        }

        is ProtoMessageMember.MessageOption -> {
          // ignore
        }

        is ProtoMessageMember.NestedEnum ->
          nestedDefs.add(convertEnum(member.enumDef))

        is ProtoMessageMember.NestedMessage ->
          nestedDefs.add(convertMessage(member.messageDef))

        is ProtoMessageMember.OneOf -> {
          // 이건 지원을 못할듯?
          throw IllegalStateException("oneof is not supported for kotlin class generation. Consider using sealed message")
        }

        is ProtoMessageMember.Reserved -> {
          // ignore
        }
      }
    }
    return KtDataClassDef(
      def.comments,
      def.name,
      sealedSuper,
      inheritedFields,
      uniqueFields,
      nestedDefs,
      def.trailingComments,
    )
  }

  fun convertSealed(def: ProtoSealedDef): KtSealedClassDef {
    val sealedSubs: List<KtSealedSubType> = def.sealedFields.map { sealed ->
      when (sealed.type) {
        is AtomicType.GeneratedMessageName ->
          KtSealedSubType.DedicatedMessage(sealed.name, sealed.type.refName)

        AtomicType.EmptyType ->
          KtSealedSubType.EmptySub(sealed.name)

        else ->
          KtSealedSubType.SingleSub(sealed.name, convertField(sealed))
      }
    }
    return KtSealedClassDef(
      def.comments,
      def.name,
      def.commonFields.map { convertField(it) },
      sealedSubs,
      def.trailingComments
    )
  }

  private fun javaOuterClassnameOption(): String {
    val javaOuterClassname = defs.options.find { option ->
      option.optionDef.name.name.names.map { it.name } == listOf("java_outer_classname")
    }?.optionDef?.value ?: throw IllegalStateException("java_outer_classname is needed")
    return (javaOuterClassname as SugarProtoAst.StringLiteral).toValue()
  }

  fun convert(): KtDefs {
    val ktDefs = defs.defs.mapNotNull { def ->
      when (def) {
        is ProtoEnumDef -> convertEnum(def)
        is ProtoMessageDef -> convertMessage(def)
        is ProtoSealedDef -> convertSealed(def)
        is ProtoServiceDef -> {
          // service는 무시
          null
        }
      }
    }
    return KtDefs(
      defs.comments,
      ktDefs,
      defs.sealedSupers,
      defs.trailingComments,
      javaOuterClassnameOption(),
      defs.kotlinPackageName,
      defs.kotlinImports
    )
  }
}
