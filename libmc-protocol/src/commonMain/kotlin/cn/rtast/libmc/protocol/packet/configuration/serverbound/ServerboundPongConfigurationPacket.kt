/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration.serverbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket

public data class ServerboundPongConfigurationPacket(val id: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundPongConfigurationPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundPongConfigurationPacket) {
            buffer.writeInt(value.id)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundPongConfigurationPacket =
            throw UnsupportedOperationException()
    }
}