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

public data class ClientboundSetExperiencePacket(
    /**
     * Between 0 and 1.
     */
    val experienceBar: Float,
    val level: Int,
    val totalExperience: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetExperiencePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetExperiencePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetExperiencePacket {
            val experienceBar = buffer.readFloat()
            val level = buffer.readVarInt()
            val totalExperience = buffer.readVarInt()
            return ClientboundSetExperiencePacket(experienceBar, level, totalExperience)
        }
    }
}