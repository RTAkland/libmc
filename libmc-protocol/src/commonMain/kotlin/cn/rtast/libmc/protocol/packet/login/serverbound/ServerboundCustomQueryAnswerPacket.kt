/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.login.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeOptionalPrefixedByteArray
import cn.rtast.libmc.common.writeVarInt

public data class ServerboundCustomQueryAnswerPacket(val messageId: Int, val data: ByteArray?) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundCustomQueryAnswerPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundCustomQueryAnswerPacket) {
            buffer.writeVarInt(value.messageId)
            buffer.writeOptionalPrefixedByteArray(value.data)
        }

        override fun decode(buffer: BytesBuffer): ServerboundCustomQueryAnswerPacket =
            throw UnsupportedOperationException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ServerboundCustomQueryAnswerPacket

        if (messageId != other.messageId) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = messageId
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }

}