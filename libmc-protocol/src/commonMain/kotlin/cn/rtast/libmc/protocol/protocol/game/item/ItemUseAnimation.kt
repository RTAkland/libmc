/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.item

public enum class ItemUseAnimation(public val id: Int) {
    NONE(0),
    EAT(1),
    DRINK(2),
    BLOCK(3),
    BOW(4),
    SPEAR(5),
    CROSSBOW(6),
    SPYGLASS(7),
    TOOT_HORN(8),
    BRUSH(9);

    public companion object {
        public fun fromID(id: Int): ItemUseAnimation = entries.first { it.id == id }
    }
}