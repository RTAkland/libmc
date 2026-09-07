/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data class ClientboundPingPacket(val id: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPingPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundPingPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundPingPacket {
            return ClientboundPingPacket(id = buffer.readInt())
        }
    }
}