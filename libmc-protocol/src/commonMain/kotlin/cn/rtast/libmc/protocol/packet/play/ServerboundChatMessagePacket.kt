/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeMcString
import cn.rtast.libmc.common.writeVarInt
import kotlin.time.Clock

public data class ServerboundChatMessagePacket(
    val message: String,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val salt: Long = 0L,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundChatMessagePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundChatMessagePacket) {
            buffer.writeMcString(value.message)
            buffer.writeLong(value.timestamp)
            buffer.writeLong(value.salt)
            buffer.writeBoolean(false)  // has signature
            buffer.writeVarInt(0)  // message count
            buffer.writeBytes(byteArrayOf(0, 0, 0))
        }

        override fun decode(buffer: BytesBuffer): ServerboundChatMessagePacket = throw UnsupportedOperationException()
    }
}