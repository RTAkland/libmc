/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.entity

public enum class VillagerLevel(public val level: Int) {
    Novice(1), Apprentice(2), Journeyman(3), Expert(4), Master(5);

    public companion object {
        public fun fromLevel(level: Int): VillagerLevel = entries.first { it.level == level }
    }
}