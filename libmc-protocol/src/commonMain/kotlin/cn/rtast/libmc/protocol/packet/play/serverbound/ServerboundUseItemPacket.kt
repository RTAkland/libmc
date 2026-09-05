/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.player.Hand

public data class ServerboundUseItemPacket(
    val hand: Hand,
    /**
     * see [cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundAcknowledgeBlockChangePacket]
     */
    val sequence: Int,
    val yaw: Float,
    val pitch: Float,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundUseItemPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundUseItemPacket) {
            buffer.writeVarInt(value.hand.id)
            buffer.writeVarInt(value.sequence)
            buffer.writeFloat(value.yaw)
            buffer.writeFloat(value.pitch)
        }

        override fun decode(buffer: BytesBuffer): ServerboundUseItemPacket =
            throw UnsupportedOperationException()
    }
}