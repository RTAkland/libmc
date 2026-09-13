/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos

public data class ClientboundOpenSignEditorPacket(val location: BlockPos, val isFrontText: Boolean) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundOpenSignEditorPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundOpenSignEditorPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundOpenSignEditorPacket {
            val location = buffer.readBlockPos()
            val isFrontText = buffer.readBoolean()
            return ClientboundOpenSignEditorPacket(location, isFrontText)
        }
    }
}