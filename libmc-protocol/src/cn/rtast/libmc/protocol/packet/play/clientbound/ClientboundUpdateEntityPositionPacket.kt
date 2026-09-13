/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readVarInt

public data class ClientboundUpdateEntityPositionPacket(
    val entityId: Int,
    val deltaX: Short,
    val deltaY: Short,
    val deltaZ: Short,
    val onGround: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUpdateEntityPositionPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundUpdateEntityPositionPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundUpdateEntityPositionPacket {
            val entityId = buffer.readVarInt()
            val x = buffer.readShort()
            val y = buffer.readShort()
            val z = buffer.readShort()
            val onGround = buffer.readBoolean()
            return ClientboundUpdateEntityPositionPacket(entityId, x, y, z, onGround)
        }
    }
}