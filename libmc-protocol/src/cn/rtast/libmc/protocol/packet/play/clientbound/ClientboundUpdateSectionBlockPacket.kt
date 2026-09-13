/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarLong
import cn.rtast.libmc.protocol.protocol.game.chunk.BlockUpdateEntry
import cn.rtast.libmc.protocol.protocol.game.chunk.ChunkSectionPos

public data class ClientboundUpdateSectionBlockPacket(
    val sectionPos: ChunkSectionPos,
    val blockUpdates: List<BlockUpdateEntry>,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUpdateSectionBlockPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundUpdateSectionBlockPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundUpdateSectionBlockPacket {
            val rawSectionPos = buffer.readLong()
            val sectionPos = ChunkSectionPos.fromRaw(rawSectionPos)
            val blockUpdates = buffer.readPrefixed {
                val rawBlockEntry = readVarLong()
                BlockUpdateEntry.fromRaw(rawBlockEntry)
            }
            return ClientboundUpdateSectionBlockPacket(sectionPos, blockUpdates)
        }
    }
}