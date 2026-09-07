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

public data class ClientboundRemoveEntityEffectPacket(val entityId: Int, val effectId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundRemoveEntityEffectPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundRemoveEntityEffectPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundRemoveEntityEffectPacket {
            val entityId = buffer.readVarInt()
            val effectId = buffer.readVarInt()
            return ClientboundRemoveEntityEffectPacket(entityId, effectId)
        }
    }
}