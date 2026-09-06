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
import cn.rtast.libmc.protocol.protocol.game.block.BlockFace
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.writeBlockPos
import cn.rtast.libmc.protocol.protocol.game.player.PlayerActionStatus

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Action
 */
public data class ServerboundPlayerActionPacket(
    val status: PlayerActionStatus,
    val location: BlockPos,
    val blockFace: BlockFace,
    /**
     * Block change sequence number
     * see [cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundAcknowledgeBlockChangePacket]
     */
    val sequence: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundPlayerActionPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundPlayerActionPacket) {
            buffer.writeVarInt(value.status.id)
            buffer.writeBlockPos(value.location)
            buffer.writeByte(value.blockFace.id)
            buffer.writeVarInt(value.sequence)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundPlayerActionPacket =
            throw UnsupportedOperationException()
    }
}