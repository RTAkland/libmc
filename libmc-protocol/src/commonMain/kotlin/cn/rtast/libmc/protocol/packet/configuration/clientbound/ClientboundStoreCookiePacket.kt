/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readPrefixedByteArray
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundStoreCookiePacket(
    val key: Identifier,
    /**
     * cookie
     */
    val payload: ByteArray,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundStoreCookiePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundStoreCookiePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundStoreCookiePacket {
            val key = buffer.readIdentifier()
            val payload = buffer.readPrefixedByteArray()
            return ClientboundStoreCookiePacket(key, payload)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ClientboundStoreCookiePacket

        if (key != other.key) return false
        if (!payload.contentEquals(other.payload)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + payload.contentHashCode()
        return result
    }
}