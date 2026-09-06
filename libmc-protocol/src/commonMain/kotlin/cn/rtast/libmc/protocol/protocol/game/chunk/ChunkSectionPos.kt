/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.chunk

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Update_Section_Blocks
 */
public data class ChunkSectionPos(
    val sectionX: Int,
    val sectionY: Int,
    val sectionZ: Int,
) {
    public companion object {
        public fun fromRaw(raw: Long): ChunkSectionPos {
            val x = (raw shr 42).toInt()
            val y = ((raw shl 44) shr 44).toInt()
            val z = ((raw shl 22) shr 42).toInt()
            return ChunkSectionPos(x, y, z)
        }
    }
}