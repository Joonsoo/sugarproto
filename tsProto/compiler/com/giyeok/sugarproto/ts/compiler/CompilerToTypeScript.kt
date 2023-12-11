package com.giyeok.sugarproto.ts.compiler

import com.giyeok.sugarproto.Proto3Ast
import com.giyeok.sugarproto.Proto3Parser

class CompilerToTypeScript {
  fun convertType(typ: Proto3Ast.Type): String = when (typ) {
    is Proto3Ast.BuiltinType -> {
      when (typ.typ) {
        Proto3Ast.BuiltinTypeEnum.BOOL -> "boolean"
        Proto3Ast.BuiltinTypeEnum.BYTES -> "Uint8Array"
        Proto3Ast.BuiltinTypeEnum.FLOAT,
        Proto3Ast.BuiltinTypeEnum.DOUBLE -> "number"

        Proto3Ast.BuiltinTypeEnum.INT32,
        Proto3Ast.BuiltinTypeEnum.INT64,
        Proto3Ast.BuiltinTypeEnum.SINT32,
        Proto3Ast.BuiltinTypeEnum.SINT64,
        Proto3Ast.BuiltinTypeEnum.UINT32,
        Proto3Ast.BuiltinTypeEnum.UINT64,
        Proto3Ast.BuiltinTypeEnum.FIXED32,
        Proto3Ast.BuiltinTypeEnum.FIXED64,
        Proto3Ast.BuiltinTypeEnum.SFIXED32,
        Proto3Ast.BuiltinTypeEnum.SFIXED64 -> "bigint"

        Proto3Ast.BuiltinTypeEnum.STRING -> "string"
      }
    }

    is Proto3Ast.MessageOrEnumType -> typ.name.name.name
  }

  fun convertType(typ: Proto3Ast.Type, mod: Proto3Ast.FieldModifier?): String =
    when (mod) {
      null -> convertType(typ)
      Proto3Ast.FieldModifier.OPTIONAL -> "${convertType(typ)} | undefined"
      Proto3Ast.FieldModifier.REPEATED -> "${convertType(typ)}[]"
    }

  fun compile(ast: Proto3Ast.Proto3) {
    val writer = CodeWriter()
    ast.defs.forEach { def ->
      when (def) {
        is Proto3Ast.Message -> {
          writer.writeLine("export interface ${def.name.name} {")
          writer.indent {
            def.body.forEach { elem ->
              when (elem) {
                is Proto3Ast.EmptyStatement -> {}
                is Proto3Ast.EnumDef -> {}
                is Proto3Ast.Field -> {
                  writer.writeLine("${elem.name.name}: ${convertType(elem.typ, elem.modifier)};")
                }

                is Proto3Ast.MapField -> {}
                is Proto3Ast.Message -> {}
                is Proto3Ast.OneofDef -> {}
                is Proto3Ast.OptionDef -> {}
                is Proto3Ast.Reserved -> {}
              }
            }
          }
          writer.writeLine("}")
          writer.writeLine()
          writer.writeLine("export class ${def.name.name}Builder {")
          writer.indent {
            def.body.forEach { elem ->
              when (elem) {
                is Proto3Ast.EmptyStatement -> {}
                is Proto3Ast.EnumDef -> {}
                is Proto3Ast.Field -> {
                  when (elem.modifier) {
                    Proto3Ast.FieldModifier.REPEATED -> {
                      writer.writeLine("${elem.name.name}: ${convertType(elem.typ)}[] = [];")
                    }

                    Proto3Ast.FieldModifier.OPTIONAL, null -> {
                      writer.writeLine("${elem.name.name}: ${convertType(elem.typ)} | undefined;")
                    }
                  }
                }

                is Proto3Ast.MapField -> {}
                is Proto3Ast.Message -> {}
                is Proto3Ast.OneofDef -> {}
                is Proto3Ast.OptionDef -> {}
                is Proto3Ast.Reserved -> {}
              }
            }
          }
          writer.writeLine("}")
          writer.writeLine()

          writer.writeLine("export const ${def.name.name} = {")
          writer.indent {
            /**
             *     builder(): ProjectBuilder {
             *         return new ProjectBuilder();
             *     },
             *     builderFrom(msg: Project): ProjectBuilder {
             *         throw "TODO";
             *     },
             *     encode(msg: Project): Uint8Array {
             *         throw "TODO";
             *     },
             *     decode(bytes: Uint8Array): Project {
             *         return Project.decodeFromBytesView(new BytesView(bytes, 0, bytes.length));
             *     },
             *     decodeToBuilder(bytes: Uint8Array): ProjectBuilder {
             *         return Project.decodeFromBytesViewToBuilder(new BytesView(bytes, 0, bytes.length));
             *     },
             *     decodeFromBytesView(view: BytesView): Project {
             *         return Project.decodeFromBytesViewToBuilder(view).build();
             *     },
             *     decodeFromBytesViewToBuilder(view: BytesView): ProjectBuilder {
             *
             */
            writer.writeLine("builder(): ${def.name.name}Builder {")
            writer.writeLineIndented("return new ${def.name.name}Builder();")
            writer.writeLine("},")
            writer.writeLine("builderFrom(msg: ${def.name.name}): ${def.name.name}Builder {")
            writer.writeLine("},")
            writer.writeLine("encode(msg: ${def.name.name}): Uint8Array {")
            writer.writeLine("},")
            writer.writeLine("decode(bytes: Uint8Array): ${def.name.name} {")
            writer.writeLineIndented("return ${def.name.name}.decodeFromBytesView(new BytesView(bytes, 0, bytes.length));")
            writer.writeLine("},")
            writer.writeLine("decodeToBuilder(bytes: Uint8Array): ${def.name.name}Builder {")
            writer.writeLineIndented("return ${def.name.name}.decodeFromBytesViewToBuilder(new BytesView(bytes, 0, bytes.length));")
            writer.writeLine("},")
            writer.writeLine("decodeFromBytesView(view: BytesView): ${def.name.name} {")
            writer.writeLineIndented("return ${def.name.name}.decodeFromBytesViewToBuilder(view).build();")
            writer.writeLine("},")
            writer.writeLine("decodeFromBytesViewToBuilder(view: BytesView): ${def.name.name}Builder {")
            writer.writeLineIndented("throw new Error();")
            writer.writeLine("},")
            writer.writeLine("encodeToJson(msg: ${def.name.name}): JSON {")
            writer.writeLineIndented("throw new Error();")
            writer.writeLine("},")
            writer.writeLine("decodeFromJson(json: JSON): ${def.name.name} {")
            writer.writeLineIndented("throw new Error();")
            writer.writeLine("},")
            writer.writeLine("decodeFromJsonToBuilder(json: JSON): ${def.name.name}Builder {")
            writer.writeLineIndented("throw new Error();")
            writer.writeLine("},")
          }
          writer.writeLine("}")
        }

        is Proto3Ast.EmptyStatement -> {
          // do nothing
        }

        is Proto3Ast.Import -> {}
        is Proto3Ast.OptionDef -> {}
        is Proto3Ast.Package -> {}
        is Proto3Ast.EnumDef -> {}
        is Proto3Ast.Service -> {}
      }
    }
    println(writer.toString())
  }

  fun compile(proto: String) {
    compile(Proto3Parser.parse(proto))
  }
}
