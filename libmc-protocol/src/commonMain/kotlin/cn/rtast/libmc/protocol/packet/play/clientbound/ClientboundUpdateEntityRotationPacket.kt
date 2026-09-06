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
import cn.rtast.libmc.protocol.protocol.game.math.Angle
import cn.rtast.libmc.protocol.protocol.game.math.readAngle

public data class ClientboundUpdateEntityRotationPacket(
    val entityId: Int,
    val yaw: Angle,
    val pitch: Angle,
    val onGround: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUpdateEntityRotationPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundUpdateEntityRotationPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundUpdateEntityRotationPacket {
            val entityId = buffer.readVarInt()
            val yaw = buffer.readAngle()
            val pitch = buffer.readAngle()
            val onGround = buffer.readBoolean()
            return ClientboundUpdateEntityRotationPacket(entityId, yaw, pitch, onGround)
        }
    }
}