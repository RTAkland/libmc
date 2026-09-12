/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */

package cn.rtast.libmc.network

import java.nio.ByteBuffer

public actual class BytesBuffer {
    private var buffer: ByteBuffer
    private var readPosition: Int = 0

    public actual constructor() : this(32)
    public actual constructor(capacity: Int) {
        this.buffer = ByteBuffer.allocate(capacity)
    }

    public actual constructor(bytes: ByteArray) : this(bytes, bytes.size)
    public actual constructor(bytes: ByteArray, capacity: Int) {
        val count = minOf(bytes.size, capacity)
        this.buffer = ByteBuffer.allocate(maxOf(32, count))
        this.buffer.put(bytes, 0, count)
    }

    private fun ensureCapacity(needed: Int) {
        if (buffer.remaining() < needed) {
            var newCapacity = buffer.capacity() * 2
            while (newCapacity - buffer.position() < needed) newCapacity *= 2
            val newBuffer = ByteBuffer.allocate(newCapacity)
            val currentPos = buffer.position()
            buffer.position(0)
            buffer.limit(currentPos)
            newBuffer.put(buffer)
            newBuffer.position(currentPos)
            this.buffer = newBuffer
        }
    }
    public actual fun writeByte(value: Byte) {
        ensureCapacity(1)
        buffer.put(value)
    }

    public actual fun writeShort(value: Short) {
        ensureCapacity(2)
        buffer.putShort(value)
    }

    public actual fun writeInt(value: Int) {
        ensureCapacity(4)
        buffer.putInt(value)
    }

    public actual fun writeLong(value: Long) {
        ensureCapacity(8)
        buffer.putLong(value)
    }

    public actual fun writeDouble(value: Double) {
        ensureCapacity(8)
        buffer.putDouble(value)
    }

    public actual fun writeFloat(value: Float) {
        ensureCapacity(4)
        buffer.putFloat(value)
    }

    public actual fun writeBytes(bytes: ByteArray) {
        ensureCapacity(bytes.size)
        buffer.put(bytes)
    }

    public actual fun writeBoolean(value: Boolean): Unit = writeByte(if (value) 0x01 else 0x00)

    public actual fun readByte(): Byte {
        val value = buffer.get(readPosition)
        readPosition += 1
        return value
    }

    public actual fun readUByte(): UByte = readByte().toUByte()

    public actual fun readShort(): Short {
        val value = buffer.getShort(readPosition)
        readPosition += 2
        return value
    }

    public actual fun readInt(): Int {
        val value = buffer.getInt(readPosition)
        readPosition += 4
        return value
    }

    public actual fun readLong(): Long {
        val value = buffer.getLong(readPosition)
        readPosition += 8
        return value
    }

    public actual fun readDouble(): Double {
        val value = buffer.getDouble(readPosition)
        readPosition += 8
        return value
    }

    public actual fun readFloat(): Float {
        val value = buffer.getFloat(readPosition)
        readPosition += 4
        return value
    }

    public actual fun readBytes(length: Int): ByteArray {
        val bytes = ByteArray(length)
        System.arraycopy(buffer.array(), readPosition, bytes, 0, length)
        readPosition += length
        return bytes
    }

    public actual fun readBoolean(): Boolean = readByte() != 0x00.toByte()

    public actual fun toByteArray(): ByteArray {
        val readableLength = size
        val bytes = ByteArray(readableLength)
        System.arraycopy(buffer.array(), readPosition, bytes, 0, readableLength)
        return bytes
    }

    public actual fun peek(): ByteArray {
        val readableLength = size
        val bytes = ByteArray(readableLength)
        System.arraycopy(buffer.array(), readPosition, bytes, 0, readableLength)
        return bytes
    }

    public actual fun close() {
        buffer.clear()
        readPosition = 0
    }

    public actual val size: Int
        get() = buffer.position() - readPosition
}