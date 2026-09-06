/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.scoreboard

public enum class ObjectiveRenderType(public val id: Int) {
    INTEGER(0),
    HEARTS(1);

    public companion object {
        public fun fromID(id: Int): ObjectiveRenderType = when (id) {
            1 -> HEARTS
            else -> INTEGER
        }
    }
}