/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data class ClientboundKeepAlivePlayPacket(val id: Long) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundKeepAlivePlayPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundKeepAlivePlayPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundKeepAlivePlayPacket =
            ClientboundKeepAlivePlayPacket(buffer.readLong())
    }
}