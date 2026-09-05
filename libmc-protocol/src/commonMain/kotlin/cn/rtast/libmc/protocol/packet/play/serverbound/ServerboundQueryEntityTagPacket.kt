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

public data class ServerboundQueryEntityTagPacket(
    /**
     * to get a transaction id, use `MinecraftClient.transactionManager.nextQueryEntitytagId`
     */
    val transactionId: Int,
    val entityId: Int,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundQueryEntityTagPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundQueryEntityTagPacket) {
            buffer.writeVarInt(value.transactionId)
            buffer.writeVarInt(value.entityId)
        }

        override fun decode(buffer: BytesBuffer): ServerboundQueryEntityTagPacket =
            throw UnsupportedOperationException()
    }
}