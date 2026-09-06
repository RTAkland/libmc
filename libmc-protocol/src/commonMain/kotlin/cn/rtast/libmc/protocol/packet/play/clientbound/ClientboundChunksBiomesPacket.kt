/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chunk.ChunkBiomeData

public data class ClientboundChunksBiomesPacket(val chunkBiomes: List<ChunkBiomeData>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundChunksBiomesPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundChunksBiomesPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundChunksBiomesPacket {
            val chunkCount = buffer.readVarInt()
            val chunks = ArrayList<ChunkBiomeData>(chunkCount)
            repeat(chunkCount) { chunks.add(ChunkBiomeData.decode(buffer)) }
            return ClientboundChunksBiomesPacket(chunks)
        }
    }
}