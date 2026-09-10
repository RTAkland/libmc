/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.entity

public enum class RabbitVariantType(public val id: Int) {
    BROWN(0), WHITE(1), BLACK(2), WHITE_SPLOTCHED(3),
    GOLD(4), SALT(5), EVIL(6);

    public companion object {
        public fun fromID(id: Int): RabbitVariantType = entries.first { it.id == id }
    }
}