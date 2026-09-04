/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.bedrock

import cn.rtast.libmc.common._Buffer
import kotlin.random.Random

private val rakNetMagic = byteArrayOf(
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
    val time: Long
    val magic: ByteArray

    fun writePayload(buffer: _Buffer)
}

internal data class BedrockRequestPacket(
    override val time: Long,
    override val magic: ByteArray = rakNetMagic,
    val guid: Long = Random.nextLong(),
) : MinecraftBedrockPacket {
    override val packetId: Byte = 0x01

    override fun writePayload(buffer: _Buffer) {
        buffer.writeByte(packetId)
        buffer.writeLong(time)
        buffer.writeBytes(magic)
        buffer.writeLong(guid)
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
    override val time: Long,
    val serverGuid: Long,
    override val magic: ByteArray,
    val stringLength: Short,
    val payload: String,
) : MinecraftBedrockPacket {
    override fun writePayload(buffer: _Buffer) {}

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as BedrockResponsePacket
        if (packetId != other.packetId) return false
        if (time != other.time) return false
        if (serverGuid != other.serverGuid) return false
        if (stringLength != other.stringLength) return false
        if (!magic.contentEquals(other.magic)) return false
        if (payload != other.payload) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packetId.toInt()
        result = 31 * result + time.hashCode()
        result = 31 * result + serverGuid.hashCode()
        result = 31 * result + stringLength
        result = 31 * result + magic.contentHashCode()
        result = 31 * result + payload.hashCode()
        return result
    }
}