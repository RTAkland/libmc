/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.*
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.sound.SoundCategory
import cn.rtast.libmc.protocol.protocol.game.sound.SoundEvent
import cn.rtast.libmc.protocol.protocol.game.sound.readSoundEvent

public data class ClientboundEntitySoundEffectPacket(
    val soundEvent: IdOrX<SoundEvent>,
    val category: SoundCategory,
    val entityId: Int,
    val volume: Float,
    val pitch: Float,
    val seed: Long,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundEntitySoundEffectPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundEntitySoundEffectPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundEntitySoundEffectPacket {
            val soundEvent = buffer.readIdOrX { readSoundEvent() }
            val category = SoundCategory.fromID(buffer.readVarInt())
            val entityId = buffer.readVarInt()
            val volume = buffer.readFloat()
            val pitch = buffer.readFloat()
            val seed = buffer.readLong()
            return ClientboundEntitySoundEffectPacket(soundEvent, category, entityId, volume, pitch, seed)
        }
    }
}