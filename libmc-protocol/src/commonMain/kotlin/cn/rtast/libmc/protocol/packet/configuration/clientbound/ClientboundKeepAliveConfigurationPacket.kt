/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec

public data class ClientboundKeepAliveConfigurationPacket(val id: Long) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundKeepAliveConfigurationPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundKeepAliveConfigurationPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundKeepAliveConfigurationPacket {
            return ClientboundKeepAliveConfigurationPacket(buffer.readLong())
        }
    }
}