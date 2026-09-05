/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class ClientboundCustomPayloadPacket(val channel: Identifier, val data: ByteArray) : MinecraftPacket {
    public companion object Codec : PacketCodec<ClientboundCustomPayloadPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundCustomPayloadPacket) {
            buffer.writeIdentifier(value.channel)
            buffer.writeBytes(value.data)
        }

        override fun decode(buffer: BytesBuffer): ClientboundCustomPayloadPacket {
            val channel = buffer.readIdentifier()
            val data = buffer.readBytes(buffer.remaining.toInt())
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