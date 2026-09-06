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
import cn.rtast.libmc.protocol.protocol.game.block.BlockFace
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.writeBlockPos
import cn.rtast.libmc.protocol.protocol.game.player.Hand

public data class ServerboundUseItemOnPacket(
    val hand: Hand,
    val location: BlockPos,
    val face: BlockFace,
    val cursorPositionX: Float,
    val cursorPositionY: Float,
    val cursorPositionZ: Float,
    val insideBlock: Boolean,
    val worldBorderHit: Boolean,
    /**
     * see [cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundAcknowledgeBlockChangePacket]
     */
    val sequence: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundUseItemOnPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundUseItemOnPacket) {
            require(value.cursorPositionX in 0.0f..1.0f) { "cursorPositionX must be between 0.0 and 1.0" }
            require(value.cursorPositionY in 0.0f..1.0f) { "cursorPositionY must be between 0.0 and 1.0" }
            require(value.cursorPositionZ in 0.0f..1.0f) { "cursorPositionZ must be between 0.0 and 1.0" }
            buffer.writeVarInt(value.hand.id)
            buffer.writeBlockPos(value.location)
            buffer.writeVarInt(value.face.id.toInt())
            buffer.writeFloat(value.cursorPositionX)
            buffer.writeFloat(value.cursorPositionY)
            buffer.writeFloat(value.cursorPositionZ)
            buffer.writeBoolean(value.insideBlock)
            buffer.writeBoolean(value.worldBorderHit)
            buffer.writeVarInt(value.sequence)
        }

        override fun decode(buffer: BytesBuffer): ServerboundUseItemOnPacket =
            throw UnsupportedOperationException()
    }
}