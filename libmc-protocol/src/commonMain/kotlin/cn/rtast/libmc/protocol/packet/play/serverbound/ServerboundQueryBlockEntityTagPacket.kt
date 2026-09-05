/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos

/**
 * to get a transaction id, use `MinecraftClient.transactionManager.nextQueryId`
 */
public data class ServerboundQueryBlockEntityTagPacket(val transactionId: Int, val location: BlockPos) :
    MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundQueryBlockEntityTagPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundQueryBlockEntityTagPacket) {
            buffer.writeVarInt(value.transactionId)
        }

        override fun decode(buffer: BytesBuffer): ServerboundQueryBlockEntityTagPacket =
            throw UnsupportedOperationException()
    }
}