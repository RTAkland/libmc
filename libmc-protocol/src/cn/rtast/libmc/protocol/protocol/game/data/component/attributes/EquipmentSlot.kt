/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

public enum class EquipmentSlot(public val id: Int) {
    MAINHAND(0),
    FEET(1),
    LEGS(2),
    CHEST(3),
    HEAD(4),
    OFFHAND(5),
    BODY(6);

    public companion object {
        public fun fromID(id: Int): EquipmentSlot = entries.first { it.id == id }
    }
}