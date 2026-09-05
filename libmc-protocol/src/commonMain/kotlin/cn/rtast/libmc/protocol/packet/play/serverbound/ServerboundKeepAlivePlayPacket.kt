/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ServerboundKeepAlivePlayPacket(val id: Long) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundKeepAlivePlayPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundKeepAlivePlayPacket) {
            buffer.writeLong(value.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundKeepAlivePlayPacket =
            ServerboundKeepAlivePlayPacket(buffer.readLong())
    }
}