/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.chat

import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.writeVarInt

public data class PreviousMessageEntry(
    val messageId: Int,
    val signature: ByteArray?,
) {
    public companion object Codec : PacketCodec<PreviousMessageEntry> {
        override fun encode(buffer: BytesBuffer, value: PreviousMessageEntry) {
            buffer.writeVarInt(value.messageId)
            if (value.messageId == 0) {
                val sig = requireNotNull(value.signature) { "signature must be present when messageId is 0" }
                require(sig.size == 256)
                buffer.writeBytes(sig)
            }
        }

        override fun decode(buffer: BytesBuffer): PreviousMessageEntry = throw UnsupportedOperationException()  // TODO
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PreviousMessageEntry

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