/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.network


public expect class BytesBuffer {
    public constructor()
    public constructor(capacity: Int)
    public constructor(bytes: ByteArray)
    public constructor(bytes: ByteArray, capacity: Int)

    public fun writeByte(value: Byte)
    public fun writeShort(value: Short)
    public fun writeInt(value: Int)
    public fun writeLong(value: Long)
    public fun writeDouble(value: Double)
    public fun writeFloat(value: Float)
    public fun writeBytes(bytes: ByteArray)
    public fun writeBoolean(value: Boolean)

    public fun readByte(): Byte
    public fun readUByte(): UByte
    public fun readShort(): Short
    public fun readInt(): Int
    public fun readLong(): Long
    public fun readDouble(): Double
    public fun readFloat(): Float
    public fun readBytes(length: Int): ByteArray
    public fun readBoolean(): Boolean
    public fun toByteArray(): ByteArray
    public fun peek(): ByteArray
    public fun close()
    public val size: Int
}

@Suppress("NOTHING_TO_INLINE")
public inline fun ByteArray.wrap(capacity: Int = this.size): BytesBuffer = BytesBuffer(this, capacity)