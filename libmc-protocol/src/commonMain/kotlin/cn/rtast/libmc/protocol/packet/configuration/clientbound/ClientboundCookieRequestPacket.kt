/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundCookieRequestPacket(val key: Identifier) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundCookieRequestPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundCookieRequestPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundCookieRequestPacket {
            return ClientboundCookieRequestPacket(key = buffer.readIdentifier())
        }
    }
}