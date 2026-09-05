/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ClientboundKeepAliveConfigurationPacket(val id: Long) : MinecraftPacket {
    public companion object Codec : PacketCodec<ClientboundKeepAliveConfigurationPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundKeepAliveConfigurationPacket) {
            buffer.writeLong(value.id)
        }

        override fun decode(buffer: BytesBuffer): ClientboundKeepAliveConfigurationPacket {
            return ClientboundKeepAliveConfigurationPacket(buffer.readLong())
        }
    }
}