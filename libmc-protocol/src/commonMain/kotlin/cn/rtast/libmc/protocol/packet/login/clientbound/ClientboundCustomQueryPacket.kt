/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.login.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundCustomQueryPacket(
    val messageId: Int,
    val channel: Identifier,
    val data: ByteArray,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundCustomQueryPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundCustomQueryPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundCustomQueryPacket {
            val messageId = buffer.readVarInt()
            val channel = buffer.readIdentifier()
            val data = buffer.readBytes(buffer.remaining.toInt())
            return ClientboundCustomQueryPacket(messageId, channel, data)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ClientboundCustomQueryPacket

        if (channel != other.channel) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = channel.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}