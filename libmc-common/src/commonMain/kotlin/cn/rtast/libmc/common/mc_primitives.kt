/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.common

import kotlin.uuid.Uuid

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

public fun BytesBuffer.writeUuid(uuid: Uuid): Unit = uuid.toLongs { mostSignificantBits, leastSignificantBits ->
    this.writeLong(mostSignificantBits)
    this.writeLong(leastSignificantBits)
}

public fun BytesBuffer.readUuid(): Uuid {
    val most = this.readLong()
    val least = this.readLong()
    return Uuid.fromLongs(most, least)
}

public fun BytesBuffer.writeVarInt(value: Int): Unit = VarIntCodec.encode(this, value)
public fun BytesBuffer.readVarInt(): Int = VarIntCodec.decode(this)

public fun BytesBuffer.writeVarLong(value: Long): Unit = VarLongCodec.encode(this, value)
public fun BytesBuffer.readVarLong(): Long = VarLongCodec.decode(this)

public fun BytesBuffer.writeMcString(value: String): Unit = McStringCodec.encode(this, value)
public fun BytesBuffer.readMcString(): String = McStringCodec.decode(this)

public fun BytesBuffer.readPrefixedByteArray(): ByteArray {
    val length = this.readVarInt()
    val data = this.readBytes(length)
    return data
}

public fun BytesBuffer.writePrefixedByteArray(data: ByteArray) {
    this.writeVarInt(data.size)
    this.writeBytes(data)
}

public fun BytesBuffer.readOptionalPrefixedByteArray(): ByteArray? {
    val isPresent = this.readBoolean()
    return if (isPresent) {
        val length = this.readVarInt()
        this.readBytes(length)
    } else null
}

public fun BytesBuffer.writeOptionalPrefixedByteArray(data: ByteArray?) {
    if (data != null) {
        this.writeBoolean(true)
        this.writeVarInt(data.size)
        this.writeBytes(data)
    } else this.writeBoolean(false)
}

public fun BytesBuffer.readPrefixedVarIntArray(): List<Int> {
    val length = readVarInt()
    val list = ArrayList<Int>(length)
    repeat(length) { list.add(readVarInt()) }
    return list
}

public fun BytesBuffer.writePrefixedVarIntArray(value: List<Int>) {
    writeVarInt(value.size)
    for (item in value) writeVarInt(item)
}

public fun BytesBuffer.readPrefixedStringArray(): List<String> {
    val length = readVarInt()
    val list = ArrayList<String>(length)
    repeat(length) { list.add(readMcString()) }
    return list
}

public fun BytesBuffer.writePrefixedStringArray(value: List<String>) {
    writeVarInt(value.size)
    for (item in value) writeMcString(item)
}

public inline fun <T> BytesBuffer.writePrefixedArray(value: List<T>, writeItem: BytesBuffer.(T) -> Unit) {
    writeVarInt(value.size)
    for (item in value) writeItem(item)
}