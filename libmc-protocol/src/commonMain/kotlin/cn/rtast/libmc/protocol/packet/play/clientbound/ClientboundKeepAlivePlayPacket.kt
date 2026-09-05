/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec

public data class ClientboundKeepAlivePlayPacket(val id: Long) : ClientboundPlayPacket {
    public companion object Codec : PacketCodec<ClientboundKeepAlivePlayPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundKeepAlivePlayPacket) {
            buffer.writeLong(value.id)
        }

        override fun decode(buffer: BytesBuffer): ClientboundKeepAlivePlayPacket =
            ClientboundKeepAlivePlayPacket(buffer.readLong())
    }
}