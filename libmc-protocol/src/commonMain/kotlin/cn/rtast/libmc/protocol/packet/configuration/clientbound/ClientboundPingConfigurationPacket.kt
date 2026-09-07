/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data class ClientboundPingConfigurationPacket(val id: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPingConfigurationPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPingConfigurationPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPingConfigurationPacket {
            return ClientboundPingConfigurationPacket(id = buffer.readInt())
        }
    }
}