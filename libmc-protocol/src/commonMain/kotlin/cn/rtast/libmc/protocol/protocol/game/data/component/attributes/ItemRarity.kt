/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

public enum class ItemRarity(public val id: Int) {
    COMMON(0),
    UNCOMMON(1),
    RARE(2),
    EPIC(3);

    public companion object {
        public fun fromID(id: Int): ItemRarity = entries.first { it.id == id }
    }
}
