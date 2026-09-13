/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.entity

public enum class ParrotVariantType(public val id: Int) {
    RED_BLUE(0), BLUE(1), GREEN(2), YELLOW_BLUE(3), GRAY(4);

    public companion object {
        public fun fromID(id: Int): ParrotVariantType = entries.first { it.id == id }
    }
}