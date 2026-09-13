/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.sound

public enum class SoundCategory(public val id: Int) {
    MASTER(0),
    MUSIC(1),
    RECORD(2),
    WEATHER(3),
    BLOCK(4),
    HOSTILE(5),
    NEUTRAL(6),
    PLAYER(7),
    AMBIENT(8),
    VOICE(9);

    public companion object {
        public fun fromID(id: Int): SoundCategory = entries.first { it.id == id }
    }
}