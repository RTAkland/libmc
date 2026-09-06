/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.math.LpVec3
import cn.rtast.libmc.protocol.protocol.game.math.writeLpVec3
import cn.rtast.libmc.protocol.protocol.game.player.Hand

public data class ServerboundInteractPacket(
    val entityId: Int,
    val hand: Hand,
    val targetOffset: LpVec3,
    val isSneaking: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundInteractPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundInteractPacket) {
            buffer.writeVarInt(value.entityId)
            buffer.writeVarInt(value.hand.id)
            buffer.writeLpVec3(value.targetOffset)
            buffer.writeBoolean(value.isSneaking)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundInteractPacket =
            throw UnsupportedOperationException()
    }
}