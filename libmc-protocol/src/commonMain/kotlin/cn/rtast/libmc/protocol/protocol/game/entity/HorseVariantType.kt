/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.entity

public enum class HorseVariantType(public val id: Int) {
    WHITE(0), CREAMY(1), CHESTNUT(2), BROWN(3), BLACK(4), GRAY(5), DARK_BROWN(6);

    public companion object {
        public fun fromID(id: Int): HorseVariantType = entries.first { it.id == id }
    }
}