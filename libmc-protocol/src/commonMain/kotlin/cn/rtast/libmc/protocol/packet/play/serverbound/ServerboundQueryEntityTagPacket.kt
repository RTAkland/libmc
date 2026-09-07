/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt

public data class ServerboundQueryEntityTagPacket(
    /**
     * to get a transaction id, use `MinecraftClient.transactionManager.nextQueryEntitytagId`
     */
    val transactionId: Int,
    val entityId: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundQueryEntityTagPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundQueryEntityTagPacket) {
            buffer.writeVarInt(value.transactionId)
            buffer.writeVarInt(value.entityId)
        }

        override fun decode(buffer: BytesBuffer): ServerboundQueryEntityTagPacket =
            throw UnsupportedOperationException()
    }
}