/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.common

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
    public fun readUByte(): UByte
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

public fun ReadChannel.readPacketFrame(): BytesBuffer {
    val length = this.readVarInt()
    return this.readBytes(length).wrap()
}