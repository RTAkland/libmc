/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data class ClientboundClearTitlesPacket(val reset: Boolean) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundClearTitlesPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundClearTitlesPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundClearTitlesPacket {
            return ClientboundClearTitlesPacket(buffer.readBoolean())
        }
    }
}