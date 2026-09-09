/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readUuid
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.math.Angle
import cn.rtast.libmc.protocol.protocol.game.math.LpVec3d
import cn.rtast.libmc.protocol.protocol.game.math.readAngle
import cn.rtast.libmc.protocol.protocol.game.math.readLpVec3
import kotlin.uuid.Uuid

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Spawn_Entity
 */
public data class ClientboundSpawnEntityPacket(
    val entityId: Int,
    val entityUuid: Uuid,
    val type: Int,
    val x: Double,
    val y: Double,
    val z: Double,
    val velocity: LpVec3d,
    val pitch: Angle,
    val yaw: Angle,
    val headYaw: Angle,
    val data: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSpawnEntityPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSpawnEntityPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSpawnEntityPacket {
            val entityId = buffer.readVarInt()
            val entityUuid = buffer.readUuid()
            val type = buffer.readVarInt()
            val x = buffer.readDouble()
            val y = buffer.readDouble()
            val z = buffer.readDouble()
            val velocity = buffer.readLpVec3()
            val pitch = buffer.readAngle()
            val yaw = buffer.readAngle()
            val headYaw = buffer.readAngle()
            val data = buffer.readVarInt()
            return ClientboundSpawnEntityPacket(
                entityId, entityUuid,
                type, x, y, z, velocity,
                pitch, yaw, headYaw, data
            )
        }
    }
}