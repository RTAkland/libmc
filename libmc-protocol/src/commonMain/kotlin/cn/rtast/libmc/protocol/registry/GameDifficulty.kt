/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.registry

public enum class GameDifficulty(public val id: Byte) {
    Peaceful(0),
    Easy(1),
    Normal(2),
    Hard(3);

    public companion object {
        public fun fromID(id: Byte): GameDifficulty = entries.first { it.id == id }
        public fun fromID(id: Int): GameDifficulty = fromID(id.toByte())
    }
}