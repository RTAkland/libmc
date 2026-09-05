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

public data class ServerboundPickItemFromEntityPacket(
    val entityId: Int,
    /**
     * Unused by the vanilla server.
     */
    val includeData: Boolean,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundPickItemFromEntityPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPickItemFromEntityPacket) {
            buffer.writeVarInt(value.entityId)
            buffer.writeBoolean(value.includeData)
        }

        override fun decode(buffer: BytesBuffer): ServerboundPickItemFromEntityPacket =
            throw UnsupportedOperationException()
    }
}