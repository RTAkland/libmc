/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.entity

public enum class TropicalFishPatternType(public val id: Int) {
    KOB(0),
    SUNSTREAK(1),
    SNOOPER(2),
    DASHER(3),
    BRINELY(4),
    SPOTTY(5),
    FLOPPER(6),
    STRIPEY(7),
    GLITTER(8),
    BLOCKFISH(9),
    BETTY(10),
    CLAYFISH(11);

    public companion object {
        public fun fromID(id: Int): TropicalFishPatternType = entries.first { it.id == id }
    }
}