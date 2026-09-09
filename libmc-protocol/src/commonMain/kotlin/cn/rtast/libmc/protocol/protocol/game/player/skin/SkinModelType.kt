/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.player.skin

public enum class SkinModelType(public val id: Int) {
    WIDE(0), SLIM(1);

    public companion object {
        public fun fromID(id: Int): SkinModelType = entries.first { it.id == id }
    }
}