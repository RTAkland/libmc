/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.chat

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.readMcString
import cn.rtast.libmc.common.readPrefixed
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ChatTypeDecoration(
    val translationKey: String,
    val parameters: List<ChatTypeParameter>,
    val style: NBTCompound,
) {
    public enum class ChatTypeParameter(public val id: Int) {
        Sender(0), Target(1), Content(2);

        public companion object {
            public fun fromID(id: Int): ChatTypeParameter = entries.first { it.id == id }
        }
    }
}

internal fun BytesBuffer.readChatTypeDecoration(): ChatTypeDecoration {
    return ChatTypeDecoration(
        translationKey = this.readMcString(),
        parameters = this.readPrefixed { ChatTypeDecoration.ChatTypeParameter.fromID(readVarInt()) },
        style = this.readNetworkNBTCompound()
    )
}