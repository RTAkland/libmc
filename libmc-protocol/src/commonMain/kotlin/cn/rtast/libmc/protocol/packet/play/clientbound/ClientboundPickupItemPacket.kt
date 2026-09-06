/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readVarInt

public data class ClientboundPickupItemPacket(
    val collectedEntityId: Int,
    val collectorEntityId: Int,
    /**
     * Seems to be 1 for XP orbs, otherwise the number of items in the stack.
     */
    val itemCount: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPickupItemPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPickupItemPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPickupItemPacket {
            val collectedEntityId = buffer.readVarInt()
            val collectorEntityId = buffer.readVarInt()
            val itemCount = buffer.readVarInt()
            return ClientboundPickupItemPacket(collectedEntityId, collectorEntityId, itemCount)
        }
    }
}