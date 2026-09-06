/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.signature

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readMcString
import cn.rtast.libmc.common.writeMcString

public data class ArgumentSignature(val name: String, val signature: ByteArray) {
    init {
        require(signature.size == 256)
    }

    internal companion object Codec : PacketCodec<ArgumentSignature> {
        override fun encode(buffer: BytesBuffer, value: ArgumentSignature) {
            buffer.writeMcString(value.name)
            buffer.writeBytes(value.signature)
        }

        override fun decode(buffer: BytesBuffer): ArgumentSignature {
            val name = buffer.readMcString()
            val signature = buffer.readBytes(256)
            return ArgumentSignature(name, signature)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ArgumentSignature

        if (name != other.name) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }
}