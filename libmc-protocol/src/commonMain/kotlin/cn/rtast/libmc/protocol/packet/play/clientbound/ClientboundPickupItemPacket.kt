/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.readVarInt

public data class ClientboundPickupItemPacket(
    val collectedEntityId: Int,
    val collectorEntityId: Int,
    /**
     * Seems to be 1 for XP orbs, otherwise the number of items in the stack.
     */
    val itemCount: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPickupItemPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundPickupItemPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundPickupItemPacket {
            val collectedEntityId = buffer.readVarInt()
            val collectorEntityId = buffer.readVarInt()
            val itemCount = buffer.readVarInt()
            return ClientboundPickupItemPacket(collectedEntityId, collectorEntityId, itemCount)
        }
    }
}