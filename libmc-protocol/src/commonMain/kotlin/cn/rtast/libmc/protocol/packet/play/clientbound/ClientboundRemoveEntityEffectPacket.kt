/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readVarInt

public data class ClientboundRemoveEntityEffectPacket(val entityId: Int, val effectId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundRemoveEntityEffectPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundRemoveEntityEffectPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundRemoveEntityEffectPacket {
            val entityId = buffer.readVarInt()
            val effectId = buffer.readVarInt()
            return ClientboundRemoveEntityEffectPacket(entityId, effectId)
        }
    }
}