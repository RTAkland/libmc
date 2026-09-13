/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.math.LpVec3d
import cn.rtast.libmc.protocol.protocol.game.math.writeLpVec3
import cn.rtast.libmc.protocol.protocol.game.player.Hand

public data class ServerboundInteractPacket(
    val entityId: Int,
    val hand: Hand,
    val targetOffset: LpVec3d,
    val isSneaking: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundInteractPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundInteractPacket) {
            buffer.writeVarInt(value.entityId)
            buffer.writeVarInt(value.hand.id)
            buffer.writeLpVec3(value.targetOffset)
            buffer.writeBoolean(value.isSneaking)
        }

        override fun decode(buffer: BytesBuffer): ServerboundInteractPacket =
            throw UnsupportedOperationException()
    }
}