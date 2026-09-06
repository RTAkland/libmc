/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.handshake

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.writeMcString
import cn.rtast.libmc.common.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.state.HandshakeIntent

public data class ServerboundHandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: UShort,
    val intent: HandshakeIntent,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundHandshakePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundHandshakePacket) {
            buffer.writeVarInt(value.protocolVersion)
            buffer.writeMcString(value.serverAddress)
            buffer.writeShort(value.serverPort.toShort())
            buffer.writeVarInt(value.intent.intentID)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundHandshakePacket = throw UnsupportedOperationException()
    }
}