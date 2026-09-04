/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.java

import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer

internal interface MinecraftPacket {
    val packetId: Int
}

// ref https://minecraft.wiki/w/Java_Edition_protocol/Packets#Handshake
internal data class HandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: UShort,
    // 1 -> Status, 2 -> Login
    val nextState: Int,
) : MinecraftPacket {
    override val packetId: Int = 0x00

    companion object Codec : PacketCodec<HandshakePacket> {
        override fun encode(buffer: _Buffer, value: HandshakePacket) {
            VarIntCodec.encode(buffer, value.protocolVersion)
            McStringCodec.encode(buffer, value.serverAddress)
            buffer.writeShort(value.serverPort.toShort())
            VarIntCodec.encode(buffer, value.nextState)
        }

        override fun decode(buffer: _Buffer): HandshakePacket = throw UnsupportedOperationException()
    }
}

// ref https://minecraft.wiki/w/Java_Edition_protocol/Packets#Status
internal data object StatusRequestPacket : MinecraftPacket, PacketCodec<StatusRequestPacket> {
    override val packetId: Int = 0x00

    override fun encode(buffer: _Buffer, value: StatusRequestPacket) {}
    override fun decode(buffer: _Buffer): StatusRequestPacket = throw UnsupportedOperationException()
}

internal data class PingPacket(val currentTime: Long) : MinecraftPacket {
    override val packetId: Int = 0x01

    companion object : PacketCodec<PingPacket> {
        override fun encode(buffer: _Buffer, value: PingPacket) {
            buffer.writeLong(value.currentTime)
        }

        override fun decode(buffer: _Buffer): PingPacket {
            return PingPacket(buffer.readLong())
        }
    }
}