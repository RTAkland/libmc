/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.network.BytesBuffer

public data class ClientboundSetTitleTextPacket(val text: TextComponent) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetTitleTextPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetTitleTextPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetTitleTextPacket {
            return ClientboundSetTitleTextPacket(buffer.readTextComponent())
        }
    }
}