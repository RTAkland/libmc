/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundCustomPayloadPacket(
    val channel: Identifier,
    val data: ByteArray,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundCustomPayloadPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundCustomPayloadPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundCustomPayloadPacket {
            val channel = buffer.readIdentifier()
            val data = buffer.readRemainingBytes()
            return ClientboundCustomPayloadPacket(channel, data)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ClientboundCustomPayloadPacket

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