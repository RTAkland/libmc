/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundCookieRequestPacket(val key: Identifier) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundCookieRequestPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundCookieRequestPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundCookieRequestPacket {
            return ClientboundCookieRequestPacket(key = buffer.readIdentifier())
        }
    }
}