/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.login.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.common.writeVarInt

public data class ClientboundSetCompressionPacket(val threshold: Int) : ClientboundLoginPacket {
    public companion object Codec : PacketCodec<ClientboundSetCompressionPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetCompressionPacket) {
            buffer.writeVarInt(value.threshold)
        }

        override fun decode(buffer: BytesBuffer): ClientboundSetCompressionPacket {
            return ClientboundSetCompressionPacket(buffer.readVarInt())
        }
    }
}