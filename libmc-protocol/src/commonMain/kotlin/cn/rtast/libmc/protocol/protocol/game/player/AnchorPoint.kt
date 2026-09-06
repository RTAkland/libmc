/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.player

public enum class AnchorPoint(public val id: Int) {
    FEET(0), EYES(1);

    public companion object {
        public fun fromID(id: Int): AnchorPoint = when (id) {
            0 -> FEET
            1 -> EYES
            else -> FEET
        }
    }
}