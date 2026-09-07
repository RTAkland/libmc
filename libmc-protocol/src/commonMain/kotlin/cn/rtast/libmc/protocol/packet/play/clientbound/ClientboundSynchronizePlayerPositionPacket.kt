/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.math.Vec3d
import cn.rtast.libmc.protocol.protocol.game.math.readVec3d
import cn.rtast.libmc.protocol.protocol.game.player.TeleportFlags

public data class ClientboundSynchronizePlayerPositionPacket(
    val teleportId: Int,
    val position: Vec3d,
    val velocity: Vec3d,
    val yaw: Float,
    val pitch: Float,
    val flags: TeleportFlags,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSynchronizePlayerPositionPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSynchronizePlayerPositionPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSynchronizePlayerPositionPacket {
            val teleportId = buffer.readVarInt()
            val position = buffer.readVec3d()!!
            val velocity = buffer.readVec3d()!!
            val yaw = buffer.readFloat()
            val pitch = buffer.readFloat()
            val rawFlags = buffer.readInt()
            val flags = TeleportFlags.fromInt(rawFlags)
            return ClientboundSynchronizePlayerPositionPacket(
                teleportId, position, velocity,
                yaw, pitch, flags
            )
        }
    }
}