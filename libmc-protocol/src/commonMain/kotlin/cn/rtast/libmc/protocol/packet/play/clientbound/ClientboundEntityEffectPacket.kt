/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.effect.EntityEffectFlags
import cn.rtast.libmc.protocol.protocol.game.effect.readEntityEffectFlags
import cn.rtast.libmc.protocol.util.ticks
import kotlin.time.Duration

public data class ClientboundEntityEffectPacket(
    val entityId: Int,
    val effectId: Int,
    val amplifier: Int,
    val durationTicks: Int,
    val flags: EntityEffectFlags,
) : MinecraftPacket {
    public val duration: Duration get() = if (durationTicks == -1) Duration.INFINITE else durationTicks.ticks

    internal companion object Codec : PacketCodec<ClientboundEntityEffectPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundEntityEffectPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundEntityEffectPacket {
            val entityId = buffer.readVarInt()
            val effectId = buffer.readVarInt()
            val amplifier = buffer.readVarInt()
            val durationTicks = buffer.readVarInt()
            val flags = buffer.readEntityEffectFlags()
            return ClientboundEntityEffectPacket(entityId, effectId, amplifier, durationTicks, flags)
        }
    }
}