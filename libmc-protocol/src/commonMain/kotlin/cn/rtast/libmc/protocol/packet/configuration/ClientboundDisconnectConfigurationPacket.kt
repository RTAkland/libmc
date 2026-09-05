/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration

import cn.rtast.libmc.protocol.util.readMinimalTextNbt
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.BytesBuffer

public data class ClientboundDisconnectConfigurationPacket(val reason: String) : MinecraftPacket {
    public companion object Codec : PacketCodec<ClientboundDisconnectConfigurationPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundDisconnectConfigurationPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundDisconnectConfigurationPacket {
            val reasonText = buffer.readMinimalTextNbt()
            return ClientboundDisconnectConfigurationPacket(reason = reasonText)
        }
    }
}