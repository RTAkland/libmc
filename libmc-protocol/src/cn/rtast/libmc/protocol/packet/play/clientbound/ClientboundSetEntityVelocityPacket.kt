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
import cn.rtast.libmc.protocol.protocol.game.math.LpVec3d
import cn.rtast.libmc.protocol.protocol.game.math.readLpVec3

public data class ClientboundSetEntityVelocityPacket(val entityId: Int, val velocity: LpVec3d) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetEntityVelocityPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetEntityVelocityPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetEntityVelocityPacket {
            val entityId = buffer.readVarInt()
            val velocity = buffer.readLpVec3()
            return ClientboundSetEntityVelocityPacket(entityId, velocity)
        }
    }
}