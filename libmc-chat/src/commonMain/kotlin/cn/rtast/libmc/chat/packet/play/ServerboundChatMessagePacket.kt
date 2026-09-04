/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.play

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.common.*
import kotlin.time.Clock

internal data class ServerboundChatMessagePacket(
    val message: String,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val salt: Long = 0L,
) : MinecraftPacket, PacketDirection.ServerboundPacket {
    override val packetId: Int = 0x09

    companion object Codec : PacketCodec<ServerboundChatMessagePacket> {
        override fun encode(buffer: _Buffer, value: ServerboundChatMessagePacket) {
            buffer.writeMcString(value.message)
            buffer.writeLong(value.timestamp)
            buffer.writeLong(value.salt)
            buffer.writeBoolean(false)  // has signature
            buffer.writeVarInt(0)  // message count
            buffer.writeBytes(byteArrayOf(0, 0, 0))
        }

        override fun decode(buffer: _Buffer): ServerboundChatMessagePacket = throw UnsupportedOperationException()
    }
}