/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.login.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.common.primitives.writeVarInt

public data class ClientboundSetCompressionPacket(val threshold: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetCompressionPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetCompressionPacket) {
            buffer.writeVarInt(value = value.threshold)
        }

        override suspend fun decode(buffer: BytesBuffer): ClientboundSetCompressionPacket {
            return ClientboundSetCompressionPacket(buffer.readVarInt())
        }
    }
}