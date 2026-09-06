/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.chat

public enum class ChatAction(public val id: Int) {
    Add(0), Remove(1), Set(2);

    public companion object {
        public fun fromID(id: Int): ChatAction = entries.first { it.id == id }
    }
}