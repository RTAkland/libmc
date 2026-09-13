/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */

package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

public enum class AttributeSlot(public val id: Int) {
    ANY(0),
    MAINHAND(1),
    OFFHAND(2),
    HAND(3),
    FEET(4),
    LEGS(5),
    CHEST(6),
    HEAD(7),
    ARMOR(8),
    BODY(9);

    public companion object {
        public fun fromID(id: Int): AttributeSlot = entries.first { it.id == id }
    }
}