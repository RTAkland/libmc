/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos

public data class ClientboundBlockUpdatePacket(val location: BlockPos, val blockId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundBlockUpdatePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundBlockUpdatePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundBlockUpdatePacket {
            val position = buffer.readBlockPos()
            val blockId = buffer.readVarInt()
            return ClientboundBlockUpdatePacket(position, blockId)
        }
    }
}