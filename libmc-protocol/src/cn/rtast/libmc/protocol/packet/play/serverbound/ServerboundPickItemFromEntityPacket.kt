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

public data class ServerboundPickItemFromEntityPacket(
    val entityId: Int,
    /**
     * Unused by the vanilla server.
     */
    val includeData: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundPickItemFromEntityPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPickItemFromEntityPacket) {
            buffer.writeVarInt(value.entityId)
            buffer.writeBoolean(value.includeData)
        }

        override fun decode(buffer: BytesBuffer): ServerboundPickItemFromEntityPacket =
            throw UnsupportedOperationException()
    }
}