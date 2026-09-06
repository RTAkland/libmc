/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readPrefixedStringArray
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.ChatAction

public data class ClientboundCustomChatCompletionsPacket(val action: ChatAction, val entries: List<String>) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundCustomChatCompletionsPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundCustomChatCompletionsPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundCustomChatCompletionsPacket {
            val action = ChatAction.fromID(buffer.readVarInt())
            val entries = buffer.readPrefixedStringArray()
            return ClientboundCustomChatCompletionsPacket(action, entries)
        }
    }
}