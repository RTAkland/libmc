/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.entity

public enum class AxolotlVariantType(public val id: Int) {
    LUCY(0), WILD(1), GOLD(2), CYAN(3), BLUE(4);

    public companion object {
        public fun fromID(id: Int): AxolotlVariantType = entries.first { it.id == id }
    }
}