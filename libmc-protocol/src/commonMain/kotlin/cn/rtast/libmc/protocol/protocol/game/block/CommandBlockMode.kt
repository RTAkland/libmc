/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.block

public enum class CommandBlockMode(public val id: Int) {
    SEQUENCE(0),  // Chain
    AUTO(1),      // Repeating
    REDSTONE(2);  // Impulse

    public companion object {
        public fun fromID(id: Int): CommandBlockMode = entries.first { it.id == id }
    }
}