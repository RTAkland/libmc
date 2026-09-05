/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.protocol.game

public enum class GameMode(public val id: Byte) {
    Survival(0),
    Creative(1),
    Adventure(2),
    Spectator(3),
    Undefined(-1),

    /**
     * reserved
     */
    Unknown(-128);

    public companion object {
        public fun fromID(id: Byte): GameMode = entries.firstOrNull { it.id == id } ?: Unknown
        public fun fromID(id: UByte): GameMode = fromID(id.toByte())
    }
}