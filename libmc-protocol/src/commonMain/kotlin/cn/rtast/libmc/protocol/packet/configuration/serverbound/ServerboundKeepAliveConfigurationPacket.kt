/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ServerboundKeepAliveConfigurationPacket(val id: Long) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundKeepAliveConfigurationPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundKeepAliveConfigurationPacket) {
            buffer.writeLong(value.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundKeepAliveConfigurationPacket =
            throw UnsupportedOperationException()
    }
}