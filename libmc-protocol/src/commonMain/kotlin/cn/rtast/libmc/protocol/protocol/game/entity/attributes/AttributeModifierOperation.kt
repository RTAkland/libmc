/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.entity.attributes

public enum class AttributeModifierOperation(public val id: Byte) {
    ADD_VALUE(0),
    ADD_MULTIPLIED_BASE(1),
    ADD_MULTIPLIED_TOTAL(2);

    public companion object {
        public fun fromID(id: Byte): AttributeModifierOperation = when (id.toInt()) {
            0 -> ADD_VALUE
            1 -> ADD_MULTIPLIED_BASE
            2 -> ADD_MULTIPLIED_TOTAL
            else -> ADD_VALUE
        }
    }
}