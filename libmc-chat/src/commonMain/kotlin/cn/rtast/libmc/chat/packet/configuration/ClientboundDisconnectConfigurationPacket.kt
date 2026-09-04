/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.configuration

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.chat.util.readMinimalTextNbt
import cn.rtast.libmc.common.MinecraftPacket
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer

internal data class ClientboundDisconnectConfigurationPacket(
    val reason: String,
) : MinecraftPacket, PacketDirection.ClientboundPacket {
    override val packetId: Int = 0x02

    companion object Codec : PacketCodec<ClientboundDisconnectConfigurationPacket> {
        override fun encode(buffer: _Buffer, value: ClientboundDisconnectConfigurationPacket) {}
        override fun decode(buffer: _Buffer): ClientboundDisconnectConfigurationPacket {
            val reasonText = buffer.readMinimalTextNbt()
            return ClientboundDisconnectConfigurationPacket(reason = reasonText)
        }
    }
}