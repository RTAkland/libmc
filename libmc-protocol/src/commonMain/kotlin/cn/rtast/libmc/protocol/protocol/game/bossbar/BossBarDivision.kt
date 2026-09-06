/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.bossbar

public enum class BossBarDivision(public val id: Int) {
    NO_DIVISION(0), NOTCHES_6(1), NOTCHES_10(2), NOTCHES_12(3), NOTCHES_20(4);

    public companion object {
        public fun fromID(id: Int): BossBarDivision = entries.firstOrNull { it.id == id } ?: NO_DIVISION
    }
}