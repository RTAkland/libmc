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

internal data class KeepAlivePacket(val id: Long) : MinecraftPacket, PacketDirection.AcrossPacket {
    override val packetId: Int = 0x04

    companion object Codec : PacketCodec<KeepAlivePacket> {
        override fun encode(buffer: _Buffer, value: KeepAlivePacket) {
            buffer.writeLong(value.id)
        }

        override fun decode(buffer: _Buffer): KeepAlivePacket {
            val id = buffer.readLong()
            return KeepAlivePacket(id)
        }
    }
}