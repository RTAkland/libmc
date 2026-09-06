/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec

public data class ClientboundPingPacket(val id: Int) : ClientboundPlayPacket {
    internal companion object Codec : PacketCodec<ClientboundPingPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPingPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPingPacket {
            return ClientboundPingPacket(id = buffer.readInt())
        }
    }
}