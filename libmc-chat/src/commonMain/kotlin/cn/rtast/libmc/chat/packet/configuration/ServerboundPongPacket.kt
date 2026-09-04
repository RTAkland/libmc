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

internal data class ServerboundPongPacket(val id: Int) : MinecraftPacket, PacketDirection.ServerboundPacket {
    override val packetId: Int = 0x2D

    companion object Codec : PacketCodec<ServerboundPongPacket> {
        override fun encode(buffer: _Buffer, value: ServerboundPongPacket) {
            buffer.writeInt(value.id)
        }

        override fun decode(buffer: _Buffer): ServerboundPongPacket = ServerboundPongPacket(buffer.readInt())
    }
}