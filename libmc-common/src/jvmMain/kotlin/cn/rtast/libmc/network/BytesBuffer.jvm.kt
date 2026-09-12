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
    private var writePosition: Int = 0

    public actual constructor() : this(32)
    public actual constructor(capacity: Int) {
        this.buffer = ByteBuffer.allocate(capacity)
    }

    public actual constructor(bytes: ByteArray) : this(bytes, bytes.size)
    public actual constructor(bytes: ByteArray, capacity: Int) {
        val count = minOf(bytes.size, capacity)
        val initialCap = maxOf(32, count)
        this.buffer = ByteBuffer.allocate(initialCap)
        this.buffer.put(bytes, 0, count)
        this.writePosition = count
    }

    private fun ensureCapacity(needed: Int) {
        val currentCapacity = buffer.capacity()
        if (currentCapacity - writePosition < needed) {
            var newCapacity = currentCapacity * 2
            while (newCapacity - writePosition < needed) {
                newCapacity *= 2
            }
            val newBuffer = ByteBuffer.allocate(newCapacity)
            buffer.position(0)
            buffer.limit(writePosition)
            newBuffer.put(buffer)
            this.buffer = newBuffer
        }
    }

    public actual fun writeByte(value: Byte) {
        ensureCapacity(1)
        buffer.put(writePosition, value)
        writePosition += 1
    }

    public actual fun writeShort(value: Short) {
        ensureCapacity(2)
        buffer.putShort(writePosition, value)
        writePosition += 2
    }

    public actual fun writeInt(value: Int) {
        ensureCapacity(4)
        buffer.putInt(writePosition, value)
        writePosition += 4
    }

    public actual fun writeLong(value: Long) {
        ensureCapacity(8)
        buffer.putLong(writePosition, value)
        writePosition += 8
    }

    public actual fun writeDouble(value: Double) {
        ensureCapacity(8)
        buffer.putDouble(writePosition, value)
        writePosition += 8
    }

    public actual fun writeFloat(value: Float) {
        ensureCapacity(4)
        buffer.putFloat(writePosition, value)
        writePosition += 4
    }

    public actual fun writeBytes(bytes: ByteArray) {
        ensureCapacity(bytes.size)
        buffer.position(writePosition)
        buffer.put(bytes)
        writePosition += bytes.size
    }

    public actual fun writeBoolean(value: Boolean): Unit = writeByte(if (value) 0x01 else 0x00)

    public actual fun readByte(): Byte {
        require(size >= 1) { "Buffer underflow: expected 1 byte, available $size" }
        val value = buffer.get(readPosition)
        readPosition += 1
        return value
    }

    public actual fun readUByte(): UByte = readByte().toUByte()

    public actual fun readShort(): Short {
        require(size >= 2) { "Buffer underflow: expected 2 bytes, available $size" }
        val value = buffer.getShort(readPosition)
        readPosition += 2
        return value
    }

    public actual fun readInt(): Int {
        require(size >= 4) { "Buffer underflow: expected 4 bytes, available $size" }
        val value = buffer.getInt(readPosition)
        readPosition += 4
        return value
    }

    public actual fun readLong(): Long {
        require(size >= 8) { "Buffer underflow: expected 8 bytes, available $size" }
        val value = buffer.getLong(readPosition)
        readPosition += 8
        return value
    }

    public actual fun readDouble(): Double {
        require(size >= 8) { "Buffer underflow: expected 8 bytes, available $size" }
        val value = buffer.getDouble(readPosition)
        readPosition += 8
        return value
    }

    public actual fun readFloat(): Float {
        require(size >= 4) { "Buffer underflow: expected 4 bytes, available $size" }
        val value = buffer.getFloat(readPosition)
        readPosition += 4
        return value
    }

    public actual fun readBytes(length: Int): ByteArray {
        require(size >= length) { "Buffer underflow: expected $length bytes, available $size" }
        val bytes = ByteArray(length)
        buffer.position(readPosition)
        buffer.get(bytes, 0, length)
        readPosition += length
        return bytes
    }

    public actual fun readBoolean(): Boolean = readByte() != 0x00.toByte()

    public actual fun toByteArray(): ByteArray {
        val readableLength = size
        val bytes = ByteArray(readableLength)
        buffer.position(readPosition)
        buffer.get(bytes, 0, readableLength)
        return bytes
    }

    public actual fun peek(): ByteArray = toByteArray()

    public actual fun close() {
        buffer.clear()
        readPosition = 0
        writePosition = 0
    }

    public actual val size: Int
        get() = writePosition - readPosition
}