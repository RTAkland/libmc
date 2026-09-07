/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.network

import kotlinx.io.*


public class BytesBuffer {
    private val _buffer = Buffer()

    public constructor()
    public constructor(bytes: ByteArray) {
        this._buffer.write(bytes)
    }

    public fun writeByte(value: Byte): Unit = _buffer.writeByte(value)
    public fun writeShort(value: Short): Unit = _buffer.writeShort(value)
    public fun writeInt(value: Int): Unit = _buffer.writeInt(value)
    public fun writeLong(value: Long): Unit = _buffer.writeLong(value)
    public fun writeDouble(value: Double): Unit = _buffer.writeDouble(value)
    public fun writeFloat(value: Float): Unit = _buffer.writeFloat(value)
    public fun writeBytes(bytes: ByteArray): Unit = _buffer.write(bytes)
    public fun writeBoolean(value: Boolean): Unit = _buffer.writeByte(if (value) 0x01 else 0x00)

    public fun readByte(): Byte = _buffer.readByte()
    public fun readUByte(): UByte = _buffer.readUByte()
    public fun readShort(): Short = _buffer.readShort()
    public fun readInt(): Int = _buffer.readInt()
    public fun readLong(): Long = _buffer.readLong()
    public fun readDouble(): Double = _buffer.readDouble()
    public fun readFloat(): Float = _buffer.readFloat()
    public fun readBytes(length: Int): ByteArray = _buffer.readByteArray(length)
    public fun readBoolean(): Boolean = _buffer.readByte() != 0x00.toByte()
    public fun toByteArray(): ByteArray = _buffer.readByteArray()
    public fun peek(): ByteArray = _buffer.peek().readByteArray()
    public fun close(): Unit = _buffer.close()

    public val size: Int get() = _buffer.size.toInt()
}

@Suppress("NOTHING_TO_INLINE")
public inline fun ByteArray.wrap(): BytesBuffer = BytesBuffer(this)
