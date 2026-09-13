/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.entity

public enum class FoxVariantType(public val id: Int) {
    RED(0), SNOW(1);

    public companion object {
        public fun fromID(id: Int): FoxVariantType = entries.first { it.id == id }
    }
}