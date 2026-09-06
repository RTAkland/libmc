/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chunk.ChunkBiomeData

public data class ClientboundChunksBiomesPacket(val chunkBiomes: List<ChunkBiomeData>) : ClientboundPlayPacket {
    internal companion object Codec : PacketCodec<ClientboundChunksBiomesPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundChunksBiomesPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundChunksBiomesPacket {
            val chunkCount = buffer.readVarInt()
            val chunks = ArrayList<ChunkBiomeData>(chunkCount)
            repeat(chunkCount) { chunks.add(ChunkBiomeData.decode(buffer)) }
            return ClientboundChunksBiomesPacket(chunks)
        }
    }
}