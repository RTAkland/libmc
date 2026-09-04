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

internal data class ServerboundKeepAlivePlayPacket(val id: Long) : MinecraftPacket, PacketDirection.ServerboundPacket {
    override val packetId: Int = 0x1C

    companion object Codec : PacketCodec<ServerboundKeepAlivePlayPacket> {
        override fun encode(buffer: _Buffer, value: ServerboundKeepAlivePlayPacket) {
            buffer.writeLong(value.id)
        }

        override fun decode(buffer: _Buffer): ServerboundKeepAlivePlayPacket =
            ServerboundKeepAlivePlayPacket(buffer.readLong())
    }
}