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

/**
 * to get a transaction id, use `MinecraftClient.transactionManager.nextQueryId`
 */
public data class ServerboundQueryBlockEntityTagPacket(val transactionId: Int, val location: BlockPos) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundQueryBlockEntityTagPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundQueryBlockEntityTagPacket) {
            buffer.writeVarInt(value.transactionId)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundQueryBlockEntityTagPacket =
            throw UnsupportedOperationException()
    }
}