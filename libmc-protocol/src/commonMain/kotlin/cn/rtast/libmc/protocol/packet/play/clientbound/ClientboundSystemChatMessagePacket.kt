/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.network.BytesBuffer

public data class ClientboundSystemChatMessagePacket(val content: TextComponent, val overlay: Boolean) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSystemChatMessagePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSystemChatMessagePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSystemChatMessagePacket {
            val content = buffer.readTextComponent()
            val overlay = buffer.readBoolean()
            return ClientboundSystemChatMessagePacket(content, overlay)
        }
    }
}