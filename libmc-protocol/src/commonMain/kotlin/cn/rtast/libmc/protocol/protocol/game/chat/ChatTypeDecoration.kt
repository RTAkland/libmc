/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.chat

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ChatTypeDecoration(
    val translationKey: String,
    val parameters: List<ChatTypeParameter>,
    val style: NBTCompound,  // TODO TextComponent or Raw NBT Compound
) {
    public enum class ChatTypeParameter(public val id: Int) {
        Sender(0), Target(1), Content(2);

        public companion object {
            public fun fromID(id: Int): ChatTypeParameter = entries.first { it.id == id }
        }
    }
}

internal suspend fun BytesBuffer.readChatTypeDecoration(): ChatTypeDecoration {
    val translationKey = this.readMcString()
    val parameters = this.readPrefixed { ChatTypeDecoration.ChatTypeParameter.fromID(readVarInt()) }
    val style = this.readNetworkNBTCompound()
    return ChatTypeDecoration(translationKey, parameters, style)
}