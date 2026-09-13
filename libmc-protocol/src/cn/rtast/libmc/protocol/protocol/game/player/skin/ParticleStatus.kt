/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.player.skin

public enum class ParticleStatus(public val id: Int) {
    All(0),
    Decreased(1),
    Minimal(2);

    public companion object {
        public fun fromID(id: Int): ParticleStatus = entries.first { it.id == id }
    }
}