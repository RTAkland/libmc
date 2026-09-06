/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.common

public object VarIntCodec : PacketCodec<Int> {
    override fun encode(buffer: BytesBuffer, value: Int) {
        var v = value
        while (true) {
            if ((v and 0x7F.inv()) == 0) {
                buffer.writeByte(v.toByte())
                return
            }
            buffer.writeByte(((v and 0x7F) or 0x80).toByte())
            v = v ushr 7
        }
    }

    override fun decode(buffer: BytesBuffer): Int {
        var numRead = 0
        var result = 0
        var read: Byte
        do {
            read = buffer.readByte()
            val value = (read.toInt() and 0x7F)
            result = result or (value shl (7 * numRead))
            numRead++
            if (numRead > 5) throw IllegalArgumentException("VarInt is too big")
        } while ((read.toInt() and 0x80) != 0)
        return result
    }
}

public object VarLongCodec : PacketCodec<Long> {
    override fun encode(buffer: BytesBuffer, value: Long) {
        var v = value
        while (true) {
            if ((v and 0x7FL.inv()) == 0L) {
                buffer.writeByte(v.toByte())
                return
            }
            buffer.writeByte(((v and 0x7F) or 0x80).toByte())
            v = v ushr 7
        }
    }

    override fun decode(buffer: BytesBuffer): Long {
        var numRead = 0
        var result = 0L
        var read: Byte
        do {
            read = buffer.readByte()
            val value = (read.toLong() and 0x7F)
            result = result or (value shl (7 * numRead))
            numRead++
            if (numRead > 10) throw IllegalArgumentException("VarLong is too big")
        } while ((read.toInt() and 0x80) != 0)
        return result
    }
}

public object McStringCodec : PacketCodec<String> {
    override fun encode(buffer: BytesBuffer, value: String) {
        val bytes = value.encodeToByteArray()
        VarIntCodec.encode(buffer, bytes.size)
        buffer.writeBytes(bytes)
    }

    override fun decode(buffer: BytesBuffer): String {
        val length = VarIntCodec.decode(buffer)
        val bytes = buffer.readBytes(length)
        return bytes.decodeToString()
    }
}

public sealed interface IdOrX<out T> {
    public data class Inline<T>(val value: T) : IdOrX<T>
    public data class Reference(val registryId: Int) : IdOrX<Nothing>
}

public inline fun <T> BytesBuffer.writeIdOrX(value: IdOrX<T>, writeX: BytesBuffer.(T) -> Unit) {
    when (value) {
        is IdOrX.Inline -> {
            this.writeVarInt(0)
            this.writeX(value.value)
        }

        is IdOrX.Reference -> this.writeVarInt(value.registryId + 1)
    }
}

public inline fun <T> BytesBuffer.readIdOrX(readX: BytesBuffer.() -> T): IdOrX<T> {
    val id = this.readVarInt()
    return if (id == 0) IdOrX.Inline(this.readX()) else IdOrX.Reference(id - 1)
}