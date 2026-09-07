/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readPrefixedStringArray
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.ChatAction

public data class ClientboundCustomChatCompletionsPacket(val action: ChatAction, val entries: List<String>) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundCustomChatCompletionsPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundCustomChatCompletionsPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundCustomChatCompletionsPacket {
            val action = ChatAction.fromID(buffer.readVarInt())
            val entries = buffer.readPrefixedStringArray()
            return ClientboundCustomChatCompletionsPacket(action, entries)
        }
    }
}