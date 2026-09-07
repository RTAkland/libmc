/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.status.serverbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data class ServerboundPingRequestPacket(val timestamp: Long) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundPingRequestPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundPingRequestPacket) {
            buffer.writeLong(value.timestamp)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundPingRequestPacket =
            throw UnsupportedOperationException()
    }
}