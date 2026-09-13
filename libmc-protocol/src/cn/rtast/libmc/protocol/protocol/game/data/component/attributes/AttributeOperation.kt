/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */

package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

public enum class AttributeOperation(public val id: Int) {
    ADD(0),
    MULTIPLY_BASE(1),
    MULTIPLY_TOTAL(2);

    public companion object {
        public fun fromID(id: Int): AttributeOperation = entries.first { it.id == id }
    }
}