/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */

package cn.rtast.libmc.network

import kotlinx.io.*

public actual class BytesBuffer {
    private val _buffer = Buffer()

    public actual constructor()
    public actual constructor(capacity: Int)
    public actual constructor(bytes: ByteArray, capacity: Int) {
        val count = minOf(bytes.size, capacity)
        this._buffer.write(bytes, 0, count)
    }

    public actual constructor(bytes: ByteArray) {
        this._buffer.write(bytes)
    }

    public actual fun writeByte(value: Byte): Unit = _buffer.writeByte(value)
    public actual fun writeShort(value: Short): Unit = _buffer.writeShort(value)
    public actual fun writeInt(value: Int): Unit = _buffer.writeInt(value)
    public actual fun writeLong(value: Long): Unit = _buffer.writeLong(value)
    public actual fun writeDouble(value: Double): Unit = _buffer.writeDouble(value)
    public actual fun writeFloat(value: Float): Unit = _buffer.writeFloat(value)
    public actual fun writeBytes(bytes: ByteArray): Unit = _buffer.write(bytes)
    public actual fun writeBoolean(value: Boolean): Unit = _buffer.writeByte(if (value) 0x01 else 0x00)

    public actual fun readByte(): Byte = _buffer.readByte()
    public actual fun readUByte(): UByte = _buffer.readUByte()
    public actual fun readShort(): Short = _buffer.readShort()
    public actual fun readInt(): Int = _buffer.readInt()
    public actual fun readLong(): Long = _buffer.readLong()
    public actual fun readDouble(): Double = _buffer.readDouble()
    public actual fun readFloat(): Float = _buffer.readFloat()
    public actual fun readBytes(length: Int): ByteArray = _buffer.readByteArray(length)
    public actual fun readBoolean(): Boolean = _buffer.readByte() != 0x00.toByte()
    public actual fun toByteArray(): ByteArray = _buffer.readByteArray()
    public actual fun peek(): ByteArray = _buffer.peek().readByteArray()
    public actual fun close(): Unit = _buffer.close()
    public actual val size: Int get() = _buffer.size.toInt()
}

@Suppress("NOTHING_TO_INLINE")
public inline fun ByteArray.wrap(): BytesBuffer = BytesBuffer(this)