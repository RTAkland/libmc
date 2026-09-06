/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.common.stream

public expect class BytesBuffer {
    public constructor()
    public constructor(bytes: ByteArray)

    public suspend fun writeByte(value: Byte)
    public suspend fun writeShort(value: Short, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public suspend fun writeInt(value: Int, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public suspend fun writeLong(value: Long, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public suspend fun writeDouble(value: Double, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public suspend fun writeFloat(value: Float, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public suspend fun writeBytes(bytes: ByteArray)
    public suspend fun writeBoolean(value: Boolean)

    public suspend fun readByte(): Byte
    public suspend fun readUByte(): UByte
    public suspend fun readShort(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Short
    public suspend fun readInt(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Int
    public suspend fun readLong(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Long
    public suspend fun readDouble(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Double
    public suspend fun readFloat(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Float
    public suspend fun readBytes(length: Int): ByteArray
    public suspend fun readBoolean(): Boolean
    public suspend fun readRemainingBytes(): ByteArray

    public suspend fun toByteArray(): ByteArray
    public suspend fun hasRemaining(): Boolean
    public suspend fun close()
    public val size: Int
    public val remaining: Long
}

@Suppress("NOTHING_TO_INLINE")
public inline fun ByteArray.wrap(): BytesBuffer = BytesBuffer(this)

public suspend fun ReadChannel.readPacketFrame(): BytesBuffer {
    val length = this.readVarInt()
    return this.readBytes(length).wrap()
}