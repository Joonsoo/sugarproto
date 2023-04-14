package com.giyeok.sugarproto

data class World(
  var randomSeed: Long,
  var currentTimestamp: Timestamp,
  var chunkRows: MutableList<ChunkRow>,
) {
  companion object {
    fun fromProto(proto: WorldProto.World) {

    }
  }

  fun toProto(builder: WorldProto.World.Builder) {

  }
}

data class Timestamp(
  var seconds: Int,
  var nanos: Int,
)

data class ChunkRow(
  var chunkY: Int,
  var chunks: MutableList<Chunk>
)

data class Chunk(
  var chunkX: Int,
  var chunkY: Int,
  var tiles: MutableList<Tile>,
)

data class Tile(
  var type: TileType,
)

enum class TileType(val tag: Int) {
  GRASS(0),
  DIRT(1),
  WATER(2)
}
