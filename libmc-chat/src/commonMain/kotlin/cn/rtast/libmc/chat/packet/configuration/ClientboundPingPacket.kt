/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.configuration

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.common.MinecraftPacket
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer

internal data class ClientboundPingPacket(val id: Int) : MinecraftPacket, PacketDirection.ClientboundPacket {
    override val packetId: Int = 0x51

    companion object Codec : PacketCodec<ClientboundPingPacket> {
        override fun encode(buffer: _Buffer, value: ClientboundPingPacket) {
            buffer.writeInt(value.id)
        }

        override fun decode(buffer: _Buffer): ClientboundPingPacket = ClientboundPingPacket(buffer.readInt())
    }
}