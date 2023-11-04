package com.giyeok.sugarproto.proto3ts

import org.junit.jupiter.api.Test

class Test {
  @Test
  fun test() {
    Proto3TypeScriptCompiler().main("""
      syntax = "proto3";

      message Project {
        int32 schema_version = 1;
        Document document = 2;
        EditState edit_state = 3;
      }

      message EditState {
        int32 pointing_node = 1;
        repeated int32 expanded_nodes = 2;
      }

      message Document {
        repeated Node roots = 1;
      }

      message Node {
        int32 id = 1;
        // 사용자가 지정한 ID
        optional string user_id = 2;
        repeated string tag = 3;
        NodeMeta meta = 4;
        NodeContent content = 5;
        // e.g. 다국어 번역 버전
        map<string, NodeContent> alt_versions = 6;
      }

      message NodeMeta {
        bool is_todo = 1;
      }

      message NodeContent {
        oneof node_content {
          HeaderNode header = 1;
          ParagraphNode paragraph = 2;
          BlockNode block = 3;
          ListNode list = 4;
          CodeBlockNode code_block = 5;
          HorizontalLineNode horizontal_line = 6;
          TableNode table = 7;
          ImageNode image = 8;
          DrawingNode drawing = 9;
        }
      }

      message HeaderNode {
        int32 header_level = 1;
        Text title = 2;
        repeated Node children = 3;
      }

      message ParagraphNode {
        string style_class = 1;
        Text text = 2;
      }

      message BlockNode {
        string style_class = 1;
        repeated Node children = 2;
      }

      message ListNode {
        bool is_numbered = 1;
        repeated ListItem items = 2;
      }

      message ListItem {
        optional string item_bullet = 1;
        repeated Node content = 2;
        repeated ListItem sub_items = 3;
      }

      message CodeBlockNode {
        string style_class = 1;
        optional string language = 2;
        string code = 3;
      }

      message HorizontalLineNode {
        string style_class = 1;
      }

      message TableNode {
      }

      message ImageNode {
        // bundle에 포함된 이미지의 이름
        string image_file = 1;
      }

      message DrawingNode {
      }

      // Text는 기본적으로 string이되, 몇가지 escape sequence가 들어갈 수 있음
      message Text {
        repeated Chunk chunks = 1;
      }

      message Chunk {
        oneof chunk {
          TextChunk text = 1;
          CodeChunk code = 2;
          ReferenceChunk reference = 3;
          FootNote footnote = 4;
        }
      }

      message TextChunk {
        string text = 1;
        ChunkStyle style = 2;
        optional LinkTo link = 3;
      }

      message ChunkStyle {
        string style_class = 1;
        bool is_italic = 2;
        bool is_bold = 3;
      }

      message LinkTo {
        oneof link_to {
          string node_id = 1;
          string external_link = 2;
        }
      }

      message CodeChunk {
        optional string language = 1;
        string text = 2;
      }

      message ReferenceChunk {
        string node_id = 1;
        // 해당 레퍼런스를 어떻게 표시할지 포맷. 구체적인 내용은 미정
        string presentation = 2;
      }

      message FootNote {
        Text footnote = 1;
      }
    """.trimIndent())
  }
}