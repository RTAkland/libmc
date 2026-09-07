/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeMcString
import cn.rtast.libmc.primitives.writeVarInt

public data class ServerboundChatMessagePacket(
    val message: String,
    val timestamp: Long,
    val salt: Long,
    val signature: ByteArray?,
    val messageCount: Int,
    val acknowledged: ByteArray,
    val checksum: Byte,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundChatMessagePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundChatMessagePacket) {
            buffer.writeMcString(value.message)
            buffer.writeLong(value.timestamp)
            buffer.writeLong(value.salt)
            val hasSignature = value.signature != null
            buffer.writeBoolean(hasSignature)
            if (hasSignature) buffer.writeBytes(value.signature)
            buffer.writeVarInt(value.messageCount)
            buffer.writeBytes(value.acknowledged)
            buffer.writeByte(value.checksum)
        }

        override fun decode(buffer: BytesBuffer): ServerboundChatMessagePacket =
            throw UnsupportedOperationException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ServerboundChatMessagePacket

        if (timestamp != other.timestamp) return false
        if (salt != other.salt) return false
        if (messageCount != other.messageCount) return false
        if (checksum != other.checksum) return false
        if (message != other.message) return false
        if (!signature.contentEquals(other.signature)) return false
        if (!acknowledged.contentEquals(other.acknowledged)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + salt.hashCode()
        result = 31 * result + messageCount
        result = 31 * result + checksum
        result = 31 * result + message.hashCode()
        result = 31 * result + (signature?.contentHashCode() ?: 0)
        result = 31 * result + acknowledged.contentHashCode()
        return result
    }
}