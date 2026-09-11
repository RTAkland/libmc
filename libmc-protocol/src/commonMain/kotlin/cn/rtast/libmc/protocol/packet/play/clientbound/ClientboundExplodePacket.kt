/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.*
import cn.rtast.libmc.protocol.protocol.game.math.Vec3d
import cn.rtast.libmc.protocol.protocol.game.math.readVec3d
import cn.rtast.libmc.protocol.protocol.game.particle.BlockParticleAlternative
import cn.rtast.libmc.protocol.protocol.game.particle.ParticleData
import cn.rtast.libmc.protocol.protocol.game.particle.readParticleData
import cn.rtast.libmc.protocol.protocol.game.sound.SoundEvent
import cn.rtast.libmc.protocol.protocol.game.sound.readSoundEvent

public data class ClientboundExplodePacket(
    val position: Vec3d,
    val radius: Float,
    val blockCount: Int,
    val playerDeltaVelocity: Vec3d?,
    val explosionParticleId: Int,
    val explosionParticleData: ParticleData,
    val explosionSound: IdOrX<SoundEvent>,
    val blockParticleAlternative: List<BlockParticleAlternative>,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundExplodePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundExplodePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundExplodePacket {
            val position = buffer.readVec3d()
            val radius = buffer.readFloat()
            val blockCount = buffer.readInt()
            val playerDeltaVelocity = buffer.readPrefixOptional { readVec3d() }
            val explosionParticleId = buffer.readVarInt()
            val explosionParticleData = buffer.readParticleData(explosionParticleId)
            val explosionSound = buffer.readIdOrX { readSoundEvent() }
            val blockParticleAlternative = buffer.readPrefixed {
                val altParticleId = buffer.readVarInt()
                val particleData = buffer.readParticleData(altParticleId)
                val scaling = buffer.readFloat()
                val speed = buffer.readFloat()
                val weight = buffer.readVarInt()
                BlockParticleAlternative(altParticleId, particleData, scaling, speed, weight)
            }
            return ClientboundExplodePacket(
                position, radius, blockCount, playerDeltaVelocity,
                explosionParticleId, explosionParticleData,
                explosionSound, blockParticleAlternative
            )
        }
    }
}