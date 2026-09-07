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
import cn.rtast.libmc.protocol.protocol.game.math.Angle
import cn.rtast.libmc.protocol.protocol.game.math.readAngle

public data class ClientboundSetHeadRotationPacket(val entityId: Int, val headYaw: Angle) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetHeadRotationPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetHeadRotationPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetHeadRotationPacket {
            val entityId = buffer.readVarInt()
            val headYaw = buffer.readAngle()
            return ClientboundSetHeadRotationPacket(entityId, headYaw)
        }
    }
}