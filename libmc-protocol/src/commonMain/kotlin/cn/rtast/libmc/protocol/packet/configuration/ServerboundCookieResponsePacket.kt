/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class ServerboundCookieResponsePacket(val key: Identifier, val payload: ByteArray?) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundCookieResponsePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundCookieResponsePacket) {
            buffer.writeIdentifier(value.key)
            if (value.payload != null) {
                buffer.writeBoolean(true)
                buffer.writeVarInt(value.payload.size)
                buffer.writeBytes(value.payload)
            } else {
                buffer.writeBoolean(false)
            }
        }

        override fun decode(buffer: BytesBuffer): ServerboundCookieResponsePacket {
            val key = buffer.readIdentifier()
            val hasPayload = buffer.readBoolean()
            val payload = if (hasPayload) {
                val length = buffer.readVarInt()
                buffer.readBytes(length)
            } else null
            return ServerboundCookieResponsePacket(key, payload)
        }
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