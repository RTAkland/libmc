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
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundCooldownPacket(
    val group: Identifier,
    /**
     * 0 to clear the cooldown.
     */
    val ticks: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundCooldownPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundCooldownPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundCooldownPacket {
            val group = buffer.readIdentifier()
            val ticks = buffer.readVarInt()
            return ClientboundCooldownPacket(group, ticks)
        }
    }
}