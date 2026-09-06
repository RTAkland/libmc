/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.bedrock

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import kotlin.random.Random

private val RAKNET_MAGIC = byteArrayOf(
    0x00, 0xFF.toByte(),
    0xFF.toByte(), 0x00,
    0xFE.toByte(), 0xFE.toByte(),
    0xFE.toByte(), 0xFE.toByte(),
    0xFD.toByte(), 0xFD.toByte(),
    0xFD.toByte(), 0xFD.toByte(),
    0x12, 0x34, 0x56, 0x78
)

internal interface MinecraftBedrockPacket {
    val packetId: Byte
}

internal data class BedrockRequestPacket(
    val time: Long,
    val magic: ByteArray = RAKNET_MAGIC,
    val guid: Long = Random.nextLong(),
) : MinecraftBedrockPacket {
    override val packetId: Byte = 0x01

    companion object Codec : PacketCodec<BedrockRequestPacket> {
        override fun encode(buffer: BytesBuffer, value: BedrockRequestPacket) {
            buffer.writeByte(value.packetId)
            buffer.writeLong(value.time)
            buffer.writeBytes(value.magic)
            buffer.writeLong(value.guid)
        }

        override fun decode(buffer: BytesBuffer): BedrockRequestPacket = throw UnsupportedOperationException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as BedrockRequestPacket
        if (time != other.time) return false
        if (guid != other.guid) return false
        if (packetId != other.packetId) return false
        if (!magic.contentEquals(other.magic)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = time.hashCode()
        result = 31 * result + guid.hashCode()
        result = 31 * result + packetId
        result = 31 * result + magic.contentHashCode()
        return result
    }
}

internal data class BedrockResponsePacket(
    override val packetId: Byte,
    val time: Long,
    val serverGuid: Long,
    val magic: ByteArray,
    val payload: String,
) : MinecraftBedrockPacket {

    companion object Codec : PacketCodec<BedrockResponsePacket> {
        override fun encode(buffer: BytesBuffer, value: BedrockResponsePacket) = throw UnsupportedOperationException()
        override fun decode(buffer: BytesBuffer): BedrockResponsePacket {
            val packetId = buffer.readByte()
            if (packetId != 0x1C.toByte()) throw IllegalStateException("Expected pong id 0x1C, got $packetId")
            val time = buffer.readLong()
            val serverGuid = buffer.readLong()
            val magic = buffer.readBytes(16)
            if (!magic.contentEquals(RAKNET_MAGIC)) throw IllegalStateException("Invalid magic in response")
            val payloadLength = buffer.readShort().toInt() and 0xFFFF
            val payloadBytes = buffer.readBytes(payloadLength)
            val payload = payloadBytes.decodeToString()
            return BedrockResponsePacket(packetId, time, serverGuid, magic, payload)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as BedrockResponsePacket
        if (packetId != other.packetId) return false
        if (time != other.time) return false
        if (serverGuid != other.serverGuid) return false
        if (!magic.contentEquals(other.magic)) return false
        if (payload != other.payload) return false
        return true
    }

    override fun hashCode(): Int {
        var result = packetId.toInt()
        result = 31 * result + time.hashCode()
        result = 31 * result + serverGuid.hashCode()
        result = 31 * result + magic.contentHashCode()
        result = 31 * result + payload.hashCode()
        return result
    }
}