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
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.TestBlockMode
import cn.rtast.libmc.protocol.protocol.game.block.writeBlockPos

public data class ServerboundSetTestBlockPacket(
    val position: BlockPos,
    val mode: TestBlockMode,
    val message: String,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSetTestBlockPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundSetTestBlockPacket) {
            buffer.writeBlockPos(value.position)
            buffer.writeVarInt(value.mode.id)
            buffer.writeMcString(value.message)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundSetTestBlockPacket =
            throw UnsupportedOperationException()
    }
}