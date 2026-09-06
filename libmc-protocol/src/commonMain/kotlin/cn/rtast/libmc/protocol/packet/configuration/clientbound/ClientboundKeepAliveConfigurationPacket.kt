/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec

public data class ClientboundKeepAliveConfigurationPacket(val id: Long) : ClientboundConfigurationPacket {
    public companion object Codec : PacketCodec<ClientboundKeepAliveConfigurationPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundKeepAliveConfigurationPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundKeepAliveConfigurationPacket {
            return ClientboundKeepAliveConfigurationPacket(buffer.readLong())
        }
    }
}