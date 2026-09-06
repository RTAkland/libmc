/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.status.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ServerboundPingRequestPacket(val timestamp: Long) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundPingRequestPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPingRequestPacket) {
            buffer.writeLong(value.timestamp)
        }

        override fun decode(buffer: BytesBuffer): ServerboundPingRequestPacket =
            throw UnsupportedOperationException()
    }
}