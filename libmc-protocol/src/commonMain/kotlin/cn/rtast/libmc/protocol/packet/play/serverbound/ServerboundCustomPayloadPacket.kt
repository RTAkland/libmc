/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class ServerboundCustomPayloadPacket(val channel: Identifier, val data: ByteArray) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundCustomPayloadPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundCustomPayloadPacket) {
            buffer.writeIdentifier(value.channel)
            buffer.writeBytes(value.data)  // ?
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundCustomPayloadPacket =
            throw UnsupportedOperationException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ServerboundCustomPayloadPacket

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