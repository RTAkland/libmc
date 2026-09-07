/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt

public data class ClientboundChunkBatchFinishedPacket(val batchSize: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundChunkBatchFinishedPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundChunkBatchFinishedPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundChunkBatchFinishedPacket {
            return ClientboundChunkBatchFinishedPacket(buffer.readVarInt())
        }
    }
}