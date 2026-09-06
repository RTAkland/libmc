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
import cn.rtast.libmc.protocol.protocol.game.math.LpVec3
import cn.rtast.libmc.protocol.protocol.game.math.readLpVec3

public data class ClientboundSetEntityVelocityPacket(val entityId: Int, val velocity: LpVec3) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetEntityVelocityPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetEntityVelocityPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetEntityVelocityPacket {
            val entityId = buffer.readVarInt()
            val velocity = buffer.readLpVec3()
            return ClientboundSetEntityVelocityPacket(entityId, velocity)
        }
    }
}