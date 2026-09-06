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

public data class ServerboundContainerClosePacket(val windowId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundContainerClosePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundContainerClosePacket) {
            buffer.writeVarInt(value.windowId)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundContainerClosePacket =
            throw UnsupportedOperationException()
    }
}