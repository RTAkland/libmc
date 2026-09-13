/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.entity

public enum class LlamaVariantType(public val id: Int) {
    CREAMY(0), WHITE(1), BROWN(2), GRAY(3);

    public companion object {
        public fun fromID(id: Int): LlamaVariantType = entries.first { it.id == id }
    }
}