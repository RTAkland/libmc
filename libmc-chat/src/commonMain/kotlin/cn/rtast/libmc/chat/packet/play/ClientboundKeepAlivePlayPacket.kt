/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.play

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.common.MinecraftPacket
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer

internal data class ClientboundKeepAlivePlayPacket(val id: Long) : MinecraftPacket, PacketDirection.ClientboundPacket {
    override val packetId: Int = 0x33

    companion object Codec : PacketCodec<ClientboundKeepAlivePlayPacket> {
        override fun encode(buffer: _Buffer, value: ClientboundKeepAlivePlayPacket) {
            buffer.writeLong(value.id)
        }

        override fun decode(buffer: _Buffer): ClientboundKeepAlivePlayPacket =
            ClientboundKeepAlivePlayPacket(buffer.readLong())
    }
}