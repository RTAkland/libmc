/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game

public enum class ChatMode(public val id: Int) {
    ENABLED(0),
    COMMANDS_ONLY(1),
    HIDDEN(2);

    public companion object {
        public fun fromID(id: Int): ChatMode = entries.first { it.id == id }
    }
}