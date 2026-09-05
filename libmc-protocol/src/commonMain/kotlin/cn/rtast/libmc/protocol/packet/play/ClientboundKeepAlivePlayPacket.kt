/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ClientboundKeepAlivePlayPacket(val id: Long) : MinecraftPacket {
    public companion object Codec : PacketCodec<ClientboundKeepAlivePlayPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundKeepAlivePlayPacket) {
            buffer.writeLong(value.id)
        }

        override fun decode(buffer: BytesBuffer): ClientboundKeepAlivePlayPacket =
            ClientboundKeepAlivePlayPacket(buffer.readLong())
    }
}