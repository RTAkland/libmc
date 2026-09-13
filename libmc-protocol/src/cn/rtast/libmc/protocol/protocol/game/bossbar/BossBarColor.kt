/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.bossbar

public enum class BossBarColor(public val id: Int) {
    PINK(0), BLUE(1), RED(2), GREEN(3), YELLOW(4), PURPLE(5), WHITE(6);

    public companion object {
        public fun fromID(id: Int): BossBarColor = entries.firstOrNull { it.id == id } ?: PINK
    }
}