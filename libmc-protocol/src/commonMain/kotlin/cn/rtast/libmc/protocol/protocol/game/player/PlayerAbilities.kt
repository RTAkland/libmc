/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.player

public enum class PlayerAbilities(public val flag: Byte) {
    FLYING(0x02);

    public companion object {
        public fun fromFlag(flag: Byte): PlayerAbilities = entries.first { it.flag == flag }
    }
}