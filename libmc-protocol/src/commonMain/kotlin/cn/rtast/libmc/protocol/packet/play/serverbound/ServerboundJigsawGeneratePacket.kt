/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.writeBlockPos

public data class ServerboundJigsawGeneratePacket(
    val location: BlockPos,
    /**
     * Value of the levels slider/max depth to generate.
     */
    val levels: Int,
    val keepJigsaw: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundJigsawGeneratePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundJigsawGeneratePacket) {
            buffer.writeBlockPos(value.location)
            buffer.writeVarInt(value.levels)
            buffer.writeBoolean(value.keepJigsaw)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundJigsawGeneratePacket =
            throw UnsupportedOperationException()
    }
}