/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.math.Vec3d
import cn.rtast.libmc.protocol.protocol.game.math.Vec3f
import cn.rtast.libmc.protocol.protocol.game.math.readVec3d
import cn.rtast.libmc.protocol.protocol.game.math.readVec3f
import cn.rtast.libmc.protocol.protocol.game.particle.ParticleData
import cn.rtast.libmc.protocol.protocol.game.particle.ParticleType
import cn.rtast.libmc.protocol.protocol.game.particle.readParticleData

public data class ClientboundLevelParticlePacket(
    val longDistance: Boolean,
    val alwaysVisible: Boolean,
    val position: Vec3d,
    val offset: Vec3f,
    val maxSpeed: Float,
    val particleCount: Int,
    val particleId: Int,
    val particleType: ParticleType,
    val data: ParticleData,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundLevelParticlePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundLevelParticlePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundLevelParticlePacket {
            val longDistance = buffer.readBoolean()
            val alwaysVisible = buffer.readBoolean()
            val position = buffer.readVec3d()
            val offset = buffer.readVec3f()
            val maxSpeed = buffer.readFloat()
            val particleCount = buffer.readInt()
            val particleId = buffer.readVarInt()
            val particleType = ParticleType.fromID(particleId)
            val data = buffer.readParticleData(particleType)
            return ClientboundLevelParticlePacket(
                longDistance, alwaysVisible, position,
                offset, maxSpeed, particleCount,
                particleId, particleType, data
            )
        }
    }
}