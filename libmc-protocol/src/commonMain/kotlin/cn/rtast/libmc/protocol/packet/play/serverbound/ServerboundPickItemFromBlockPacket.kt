/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.writeBlockPos

public data class ServerboundPickItemFromBlockPacket(
    val location: BlockPos,
    /**
     * Used to tell the server to include block data in the new stack,
     * works only if in creative mode.
     */
    val includeData: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundPickItemFromBlockPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPickItemFromBlockPacket) {
            buffer.writeBlockPos(value.location)
            buffer.writeBoolean(value.includeData)
        }

        override fun decode(buffer: BytesBuffer): ServerboundPickItemFromBlockPacket =
            throw UnsupportedOperationException()
    }
}