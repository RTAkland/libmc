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
import cn.rtast.libmc.protocol.protocol.game.math.LpVec3
import cn.rtast.libmc.protocol.protocol.game.math.readLpVec3

public data class ClientboundSetEntityVelocityPacket(val entityId: Int, val velocity: LpVec3) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetEntityVelocityPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetEntityVelocityPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetEntityVelocityPacket {
            val entityId = buffer.readVarInt()
            val velocity = buffer.readLpVec3()
            return ClientboundSetEntityVelocityPacket(entityId, velocity)
        }
    }
}