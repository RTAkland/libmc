/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.handshake

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.common.MinecraftPacket
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer
import cn.rtast.libmc.common.writeMcString
import cn.rtast.libmc.common.writeVarInt

internal data class HandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: UShort,
    // 1 for Status, 2 for Login, 3 for Transfer
    val intent: Int
) : MinecraftPacket, PacketDirection.ServerboundPacket {
    override val packetId: Int = 0x00

    companion object Codec : PacketCodec<HandshakePacket> {
        override fun encode(buffer: _Buffer, value: HandshakePacket) {
            buffer.writeVarInt(value.protocolVersion)
            buffer.writeMcString(value.serverAddress)
            buffer.writeShort(value.serverPort.toShort())
            buffer.writeVarInt(value.intent)
        }

        override fun decode(buffer: _Buffer): HandshakePacket = throw UnsupportedOperationException()
    }
}