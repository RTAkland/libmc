/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.block

public enum class EnchantmentButton(public val id: Int) {
    TOPMOST(0),
    MIDDLE(1),
    BOTTOM(2);

    public companion object {
        public fun fromID(id: Int): EnchantmentButton =
            requireNotNull(entries.firstOrNull { it.id == id }) { "Unknown enchantment button id $id" }
    }
}