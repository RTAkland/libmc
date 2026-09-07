/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeMcString
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.writeBlockPos
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class ServerboundSetJigsawBlockPacket(
    val location: BlockPos,
    val name: Identifier,
    val target: Identifier,
    val pool: Identifier,
    val finalState: String,
    val jointType: String,
    val selectionPriority: Int,
    val placementPriority: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSetJigsawBlockPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundSetJigsawBlockPacket) {
            buffer.writeBlockPos(value.location)
            buffer.writeIdentifier(value.name)
            buffer.writeIdentifier(value.target)
            buffer.writeIdentifier(value.pool)
            buffer.writeMcString(value.finalState)
            buffer.writeMcString(value.jointType)
            buffer.writeVarInt(value.selectionPriority)
            buffer.writeVarInt(value.placementPriority)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundSetJigsawBlockPacket =
            throw UnsupportedOperationException()
    }
}