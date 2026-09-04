/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.chat

public enum class ChatFilterType(public val id: Int) {
    PASS_THROUGH(0),
    FULLY_FILTERED(1),
    PARTIALLY_FILTERED(2);

    public companion object {
        public fun fromId(id: Int): ChatFilterType =
            entries.firstOrNull { it.id == id } ?: PASS_THROUGH
    }
}