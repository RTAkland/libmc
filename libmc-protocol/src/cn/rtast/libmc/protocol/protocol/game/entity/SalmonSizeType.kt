/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.entity

public enum class SalmonSizeType(public val id: Int) {
    SMALL(0), MEDIUM(1), LARGE(2);

    public companion object {
        public fun fromID(id: Int): SalmonSizeType = entries.first { it.id == id }
    }
}