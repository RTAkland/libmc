/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.status.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data class ClientboundPongResponsePacket(val timestamp: Long) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPongResponsePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundPongResponsePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundPongResponsePacket {
            return ClientboundPongResponsePacket(buffer.readLong())
        }
    }
}