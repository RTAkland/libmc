/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.inventory.equipment

public enum class EntityEquipmentSlot(public val id: Int) {
    MAIN_HAND(0),
    OFF_HAND(1),
    BOOTS(2),
    LEGGINGS(3),
    CHESTPLATE(4),
    HELMET(5),
    BODY(6),
    SADDLE(7);

    public companion object {
        public fun fromID(id: Int): EntityEquipmentSlot = entries.firstOrNull { it.id == id } ?: MAIN_HAND
    }
}