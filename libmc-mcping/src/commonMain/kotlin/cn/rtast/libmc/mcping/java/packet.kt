/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.java

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.primitives.McStringCodec
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.VarIntCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

// ref https://minecraft.wiki/w/Java_Edition_protocol/Packets#Handshake
internal data class HandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: UShort,
    // 1 -> Status, 2 -> Login
    val nextState: Int,
) : MinecraftPacket {
    companion object Codec : PacketCodec<HandshakePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: HandshakePacket) {
            VarIntCodec.encode(buffer, value.protocolVersion)
            McStringCodec.encode(buffer, value.serverAddress)
            buffer.writeShort(value.serverPort.toShort())
            VarIntCodec.encode(buffer, value.nextState)
        }

        override suspend fun decode(buffer: BytesBuffer): HandshakePacket = throw UnsupportedOperationException()
    }
}

// ref https://minecraft.wiki/w/Java_Edition_protocol/Packets#Status
internal data object StatusRequestPacket : MinecraftPacket, PacketCodec<StatusRequestPacket> {
    override suspend fun encode(buffer: BytesBuffer, value: StatusRequestPacket) {}
    override suspend fun decode(buffer: BytesBuffer): StatusRequestPacket = throw UnsupportedOperationException()
}

internal data class PingPacket(val currentTime: Long) : MinecraftPacket {
    companion object : PacketCodec<PingPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: PingPacket) {
            buffer.writeLong(value.currentTime)
        }

        override suspend fun decode(buffer: BytesBuffer): PingPacket {
            return PingPacket(buffer.readLong())
        }
    }
}