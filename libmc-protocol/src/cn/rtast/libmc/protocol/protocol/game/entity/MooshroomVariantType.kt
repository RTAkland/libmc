/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.entity

public enum class MooshroomVariantType(public val id: Int) {
    RED(0), BROWN(1);

    public companion object {
        public fun fromID(id: Int): MooshroomVariantType = entries.first { it.id == id }
    }
}