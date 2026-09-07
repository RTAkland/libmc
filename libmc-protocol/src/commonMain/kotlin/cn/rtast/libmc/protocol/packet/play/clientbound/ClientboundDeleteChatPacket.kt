/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.readVarInt

public data class ClientboundDeleteChatPacket(val messageId: Int, val signature: ByteArray?) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundDeleteChatPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundDeleteChatPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundDeleteChatPacket {
            val messageId = buffer.readVarInt()
            val signature = buffer.readOptional { buffer.readBytes(256) }
            return ClientboundDeleteChatPacket(messageId, signature)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ClientboundDeleteChatPacket

        if (messageId != other.messageId) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = messageId
        result = 31 * result + (signature?.contentHashCode() ?: 0)
        return result
    }
}