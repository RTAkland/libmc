/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeMcString
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.signature.ArgumentSignature

public data class ServerboundSignedChatCommandPacket(
    val command: String,
    val timestamp: Long,
    val salt: Long,
    val argumentSignatures: List<ArgumentSignature>,
    val messageCount: Int,
    val acknowledged: ByteArray,
    val checksum: Byte,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSignedChatCommandPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSignedChatCommandPacket) {
            buffer.writeMcString(value.command)
            buffer.writeLong(value.timestamp)
            buffer.writeLong(value.salt)
            buffer.writeVarInt(value.argumentSignatures.size)
            value.argumentSignatures.forEach { ArgumentSignature.encode(buffer, it) }
            buffer.writeVarInt(value.messageCount)
            buffer.writeBytes(value.acknowledged)
            buffer.writeByte(value.checksum)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSignedChatCommandPacket =
            throw UnsupportedOperationException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ServerboundSignedChatCommandPacket

        if (timestamp != other.timestamp) return false
        if (salt != other.salt) return false
        if (messageCount != other.messageCount) return false
        if (checksum != other.checksum) return false
        if (command != other.command) return false
        if (argumentSignatures != other.argumentSignatures) return false
        if (!acknowledged.contentEquals(other.acknowledged)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + salt.hashCode()
        result = 31 * result + messageCount
        result = 31 * result + checksum
        result = 31 * result + command.hashCode()
        result = 31 * result + argumentSignatures.hashCode()
        result = 31 * result + acknowledged.contentHashCode()
        return result
    }
}