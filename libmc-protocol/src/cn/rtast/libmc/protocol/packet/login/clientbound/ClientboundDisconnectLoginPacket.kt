/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.login.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent

public data class ClientboundDisconnectLoginPacket(val reason: TextComponent) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundDisconnectLoginPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundDisconnectLoginPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundDisconnectLoginPacket {
            TODO("FIX ME. it's a raw JSON string text component with a varint prefix not a network NBT binary")
//            return ClientboundDisconnectLoginPacket(reason = buffer.readTextComponent())
        }
    }
}