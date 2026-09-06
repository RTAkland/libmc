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
import cn.rtast.libmc.protocol.protocol.game.entity.MinecartStep
import cn.rtast.libmc.protocol.protocol.game.entity.readMinecartStep

public data class ClientboundMoveMinecartAlongTrackPacket(val entityId: Int, val steps: List<MinecartStep>) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundMoveMinecartAlongTrackPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundMoveMinecartAlongTrackPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundMoveMinecartAlongTrackPacket {
            val entityId = buffer.readVarInt()
            val stepCount = buffer.readVarInt()
            val steps = ArrayList<MinecartStep>()
            repeat(stepCount) { steps.add(buffer.readMinecartStep()) }
            return ClientboundMoveMinecartAlongTrackPacket(entityId, steps)
        }
    }
}