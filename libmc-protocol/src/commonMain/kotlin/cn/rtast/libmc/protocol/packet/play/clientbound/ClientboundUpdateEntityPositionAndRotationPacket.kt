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
import cn.rtast.libmc.protocol.protocol.game.math.Angle
import cn.rtast.libmc.protocol.protocol.game.math.readAngle

public data class ClientboundUpdateEntityPositionAndRotationPacket(
    val entityId: Int,
    val deltaX: Short,
    val deltaY: Short,
    val deltaZ: Short,
    val yaw: Angle,
    val pitch: Angle,
    val onGround: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUpdateEntityPositionAndRotationPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundUpdateEntityPositionAndRotationPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundUpdateEntityPositionAndRotationPacket {
            val entityId = buffer.readVarInt()
            val x = buffer.readShort()
            val y = buffer.readShort()
            val z = buffer.readShort()
            val yaw = buffer.readAngle()
            val pitch = buffer.readAngle()
            val onGround = buffer.readBoolean()
            return ClientboundUpdateEntityPositionAndRotationPacket(entityId, x, y, z,yaw, pitch, onGround)
        }
    }
}