/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.player

public enum class PlayerPositionFlag(public val flag: Byte) {
    ON_GROUND(0x01),
    PUSHING_AGAINST_WALL(0x02);

    public companion object {
        public fun fromFlag(flag: Byte): PlayerPositionFlag = entries.first { it.flag == flag }
    }
}