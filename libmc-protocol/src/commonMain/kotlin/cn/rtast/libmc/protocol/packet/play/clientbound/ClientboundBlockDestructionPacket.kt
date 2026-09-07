/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.block.BlockDestroyStage
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Block_Destroy_Stage
 */
public data class ClientboundBlockDestructionPacket(
    val entityId: Int,
    val location: BlockPos,
    val stage: BlockDestroyStage,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundBlockDestructionPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundBlockDestructionPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundBlockDestructionPacket {
            val entityId = buffer.readVarInt()
            val location = buffer.readBlockPos()
            val stage = buffer.readByte()
            return ClientboundBlockDestructionPacket(entityId, location, BlockDestroyStage(stage))
        }
    }
}