/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.common

import kotlin.uuid.Uuid

public expect class BytesBuffer {
    public constructor()
    public constructor(bytes: ByteArray)

    public fun writeByte(value: Byte)
    public fun writeShort(value: Short, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeInt(value: Int, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeLong(value: Long, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeDouble(value: Double, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeFloat(value: Float, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeBytes(bytes: ByteArray)
    public fun writeBoolean(value: Boolean)

    public fun readByte(): Byte
    public fun readShort(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Short
    public fun readInt(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Int
    public fun readLong(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Long
    public fun readDouble(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Double
    public fun readFloat(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Float
    public fun readBytes(length: Int): ByteArray
    public fun readBoolean(): Boolean
    public fun readRemainingBytes(): ByteArray

    public fun toByteArray(): ByteArray
    public fun hasRemaining(): Boolean
    public fun close()
    public val size: Int
    public val remaining: Long
}

@Suppress("NOTHING_TO_INLINE")
public inline fun ByteArray.wrap(): BytesBuffer = BytesBuffer(this)

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

public fun ReadChannel.readPacketFrame(): BytesBuffer {
    val length = this.readVarInt()
    return this.readBytes(length).wrap()
}