/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos

public data class ClientboundTestHighlightPositionPacket(val absolute: BlockPos, val relative: BlockPos) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundTestHighlightPositionPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundTestHighlightPositionPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundTestHighlightPositionPacket {
            val abs = buffer.readBlockPos()
            val rel = buffer.readBlockPos()
            return ClientboundTestHighlightPositionPacket(abs, rel)
        }
    }
}