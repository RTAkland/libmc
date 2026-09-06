/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec

public data class ClientboundClearTitlesPacket(val reset: Boolean) : ClientboundPlayPacket {
    internal companion object Codec : PacketCodec<ClientboundClearTitlesPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundClearTitlesPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundClearTitlesPacket {
            return ClientboundClearTitlesPacket(buffer.readBoolean())
        }
    }
}