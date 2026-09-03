/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.java

import cn.rtast.mcping.platform._Buffer

internal interface MinecraftPacket {
    val packetId: Int

    fun writePayload(buffer: _Buffer)
}

// ref https://minecraft.wiki/w/Java_Edition_protocol/Packets#Handshake
internal data class HandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: UShort,
    // 1 -> Status
    val nextState: Int,
) : MinecraftPacket {
    override val packetId: Int = 0x00

    override fun writePayload(buffer: _Buffer) {
        buffer.writeVarInt(protocolVersion)
        buffer.writeMcString(serverAddress)
        // write UShort
        // write 2 bytes big endian
        buffer.writeByte((serverPort.toInt() shr 8).toByte())
        buffer.writeByte(serverPort.toByte())
        buffer.writeVarInt(nextState)
    }
}

// ref https://minecraft.wiki/w/Java_Edition_protocol/Packets#Status
internal data object StatusRequestPacket : MinecraftPacket {
    override val packetId: Int = 0x00
    override fun writePayload(buffer: _Buffer) {}
}

internal data class PingPacket(val currentTime: Long) : MinecraftPacket {
    override val packetId: Int = 0x01
    override fun writePayload(buffer: _Buffer) = buffer.writeLong(currentTime)
}