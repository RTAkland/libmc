/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt

public data class ServerboundSpectatorActionPacket(
    /**
     * If 0, the player was not targeting an entity.
     * Otherwise, the ID of the targeted entity plus 1.
     */
    val entityId: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSpectatorActionPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSpectatorActionPacket) {
            buffer.writeVarInt(value.entityId)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSpectatorActionPacket =
            throw UnsupportedOperationException()
    }
}