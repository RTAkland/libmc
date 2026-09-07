/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.writeVarInt

public data class ServerboundAcknowledgeChatMessagePacket(val messageCount: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundAcknowledgeChatMessagePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundAcknowledgeChatMessagePacket) {
            buffer.writeVarInt(value.messageCount)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundAcknowledgeChatMessagePacket =
            throw UnsupportedOperationException()
    }
}