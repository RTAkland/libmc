/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class ServerboundCookieResponsePacket(val key: Identifier, val payload: ByteArray?) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundCookieResponsePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundCookieResponsePacket) {
            buffer.writeIdentifier(value.key)
            if (value.payload != null) {
                buffer.writeBoolean(true)
                buffer.writeVarInt(value.payload.size)
                buffer.writeBytes(value.payload)
            } else buffer.writeBoolean(false)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundCookieResponsePacket =
            throw UnsupportedOperationException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ServerboundCookieResponsePacket

        if (key != other.key) return false
        if (!payload.contentEquals(other.payload)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + (payload?.contentHashCode() ?: 0)
        return result
    }
}