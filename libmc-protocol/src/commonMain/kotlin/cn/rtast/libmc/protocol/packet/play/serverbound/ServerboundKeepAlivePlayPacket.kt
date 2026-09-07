/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket

public data class ServerboundKeepAlivePlayPacket(val id: Long) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundKeepAlivePlayPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundKeepAlivePlayPacket) {
            buffer.writeLong(value.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundKeepAlivePlayPacket =
            ServerboundKeepAlivePlayPacket(buffer.readLong())
    }
}