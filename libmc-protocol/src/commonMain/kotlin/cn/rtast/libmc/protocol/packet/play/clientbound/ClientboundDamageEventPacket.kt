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
import cn.rtast.libmc.protocol.protocol.game.math.Vec3d
import cn.rtast.libmc.protocol.protocol.game.math.readVec3d

public data class ClientboundDamageEventPacket(
    val entityId: Int,
    val sourceTypeId: Int,
    val sourceCauseId: Int?,
    val sourceDirectId: Int?,
    val position: Vec3d?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundDamageEventPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundDamageEventPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundDamageEventPacket {
            val entityId = buffer.readVarInt()
            val sourceTypeId = buffer.readVarInt()
            val rawCauseId = buffer.readVarInt()
            val sourceCauseId = if (rawCauseId > 0) rawCauseId - 1 else null
            val rawDirectId = buffer.readVarInt()
            val sourceDirectId = if (rawDirectId > 0) rawDirectId - 1 else null
            val position = buffer.readVec3d(true)
            return ClientboundDamageEventPacket(entityId, sourceTypeId, sourceCauseId, sourceDirectId, position)
        }
    }
}