/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.block

public enum class BlockFace(public val id: Byte) {
    DOWN(0),  // -Y (Bottom)
    UP(1),    // +Y (Top)
    NORTH(2), // -Z
    SOUTH(3), // +Z
    WEST(4),  // -X
    EAST(5);  // +X

    public companion object {
        public fun fromID(id: Byte): BlockFace = entries.first { it.id == id }
    }
}