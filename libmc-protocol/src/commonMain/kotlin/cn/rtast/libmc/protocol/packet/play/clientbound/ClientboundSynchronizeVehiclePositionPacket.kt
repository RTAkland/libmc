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
import cn.rtast.libmc.protocol.protocol.game.math.Vec3d
import cn.rtast.libmc.protocol.protocol.game.math.readVec3d
import cn.rtast.libmc.protocol.protocol.game.player.TeleportFlags

public data class ClientboundSynchronizeVehiclePositionPacket(
    val entityId: Int,
    val position: Vec3d,
    val velocityPosition: Vec3d,
    val yaw: Float,
    val pitch: Float,
    val flags: TeleportFlags,
    val onGround: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSynchronizeVehiclePositionPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSynchronizeVehiclePositionPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSynchronizeVehiclePositionPacket {
            val entityId = buffer.readVarInt()
            val position = buffer.readVec3d()!!
            val velocityPosition = buffer.readVec3d()!!
            val yaw = buffer.readFloat()
            val pitch = buffer.readFloat()
            val flags = TeleportFlags.fromInt(buffer.readInt())
            val onGround = buffer.readBoolean()
            return ClientboundSynchronizeVehiclePositionPacket(
                entityId, position,
                velocityPosition, yaw,
                pitch, flags, onGround
            )
        }
    }
}