/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.inventory

public enum class EquipmentSlot(public val rawId: Int) {
    MAIN_HAND(0),
    OFF_HAND(1),
    FEET(2),
    LEGS(3),
    CHEST(4),
    HEAD(5),
    BODY(6),
    SADDLE(7);

    public companion object {
        public fun fromID(id: Int): EquipmentSlot = when (id) {
            0 -> MAIN_HAND
            1 -> OFF_HAND
            2 -> FEET
            3 -> LEGS
            4 -> CHEST
            5 -> HEAD
            6 -> BODY
            7 -> SADDLE
            else -> MAIN_HAND
        }
    }
}