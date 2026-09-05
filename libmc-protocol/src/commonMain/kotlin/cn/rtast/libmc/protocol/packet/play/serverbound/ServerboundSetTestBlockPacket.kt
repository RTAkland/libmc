/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeMcString
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.TestBlockMode
import cn.rtast.libmc.protocol.protocol.game.block.writeBlockPos

public data class ServerboundSetTestBlockPacket(
    val position: BlockPos,
    val mode: TestBlockMode,
    val message: String,
) : MinecraftPacket {

    public companion object Codec : PacketCodec<ServerboundSetTestBlockPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSetTestBlockPacket) {
            buffer.writeBlockPos(value.position)
            buffer.writeVarInt(value.mode.id)
            buffer.writeMcString(value.message)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetTestBlockPacket =
            throw UnsupportedOperationException()
    }
}