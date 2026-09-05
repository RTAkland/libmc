/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.inventory

public enum class InventoryClickType(public val id: Int) {
    PICKUP(0),
    QUICK_MOVE(1),
    SWAP(2),
    CLONE(3),
    THROW(4),
    QUICK_CRAFT(5),
    PICKUP_ALL(6);

    public companion object {
        public fun fromID(id: Int): InventoryClickType =
            requireNotNull(entries.firstOrNull { it.id == id }) { "Unknown inventory click type id $id" }
    }
}