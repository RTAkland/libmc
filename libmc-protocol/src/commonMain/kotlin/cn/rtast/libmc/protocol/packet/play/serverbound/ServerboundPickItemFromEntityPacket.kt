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

public data class ServerboundPickItemFromEntityPacket(
    val entityId: Int,
    /**
     * Unused by the vanilla server.
     */
    val includeData: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundPickItemFromEntityPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundPickItemFromEntityPacket) {
            buffer.writeVarInt(value.entityId)
            buffer.writeBoolean(value.includeData)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundPickItemFromEntityPacket =
            throw UnsupportedOperationException()
    }
}