/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.readVarInt

public data class ClientboundProjectilePowerPacket(val entityId: Int, val power: Double) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundProjectilePowerPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundProjectilePowerPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundProjectilePowerPacket {
            val entityId = buffer.readVarInt()
            val power = buffer.readDouble()
            return ClientboundProjectilePowerPacket(entityId, power)
        }
    }
}