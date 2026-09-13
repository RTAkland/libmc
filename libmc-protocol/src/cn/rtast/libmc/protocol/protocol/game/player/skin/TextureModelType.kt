/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.player.skin

public enum class TextureModelType(public val id: Int) {
    WIDE(0), SLIM(1);

    public companion object {
        public fun fromID(id: Int): TextureModelType = entries.first { it.id == id }
    }
}