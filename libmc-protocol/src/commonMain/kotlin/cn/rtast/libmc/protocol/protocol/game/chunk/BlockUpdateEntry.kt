/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.chunk

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Update_Section_Blocks
 */
public data class BlockUpdateEntry(
    val localX: Int,
    val localY: Int,
    val localZ: Int,
    val blockStateId: Int,
) {
    public val sectionIndex: Int get() = (localY shl 8) or (localZ shl 4) or localX

    public companion object {
        public fun fromRaw(raw: Long): BlockUpdateEntry {
            val blockStateId = (raw shr 12).toInt()
            val localX = ((raw shr 8) and 0xFL).toInt()
            val localZ = ((raw shr 4) and 0xFL).toInt()
            val localY = (raw and 0xFL).toInt()
            return BlockUpdateEntry(localX, localY, localZ, blockStateId)
        }
    }
}