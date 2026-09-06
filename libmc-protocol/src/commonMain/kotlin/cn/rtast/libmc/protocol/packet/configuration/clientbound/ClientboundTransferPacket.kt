/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readMcString
import cn.rtast.libmc.common.primitives.readVarInt

public data class ClientboundTransferPacket(val host: String, val port: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundTransferPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundTransferPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundTransferPacket {
            val host = buffer.readMcString()
            val port = buffer.readVarInt()
            return ClientboundTransferPacket(host, port)
        }
    }
}