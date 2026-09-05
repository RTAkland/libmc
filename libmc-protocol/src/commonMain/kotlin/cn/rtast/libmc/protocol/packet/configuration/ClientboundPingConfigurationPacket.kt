/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec

public data class ClientboundPingConfigurationPacket(val id: Int) : ClientboundConfigurationPacket {
    public companion object Codec : PacketCodec<ClientboundPingConfigurationPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPingConfigurationPacket) {
            buffer.writeInt(value.id)
        }

        override fun decode(buffer: BytesBuffer): ClientboundPingConfigurationPacket = ClientboundPingConfigurationPacket(buffer.readInt())
    }
}