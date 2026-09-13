/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.network

import kotlinx.cinterop.*
import platform.posix.free
import platform.posix.malloc
import platform.posix.memcpy
import platform.posix.realloc

public actual class BytesBuffer : AutoCloseable {
    private var ptr: CPointer<ByteVar>?
    private var capacity: Int
    private var readPosition: Int = 0
    private var writePosition: Int = 0

    public actual constructor() : this(32)
    public actual constructor(capacity: Int) {
        val initialCap = maxOf(32, capacity)
        this.capacity = initialCap
        val rawPtr = malloc(initialCap.toULong())
            ?: throw OutOfMemoryError("Failed to allocate native memory ($initialCap bytes)")
        this.ptr = rawPtr.reinterpret()
    }

    public actual constructor(bytes: ByteArray) : this(bytes, bytes.size)

    public actual constructor(bytes: ByteArray, capacity: Int) {
        val count = minOf(bytes.size, capacity)
        val initialCap = maxOf(32, count)
        this.capacity = initialCap
        val rawPtr = malloc(initialCap.toULong())
            ?: throw OutOfMemoryError("Failed to allocate native memory ($initialCap bytes)")
        this.ptr = rawPtr.reinterpret()

        writeBytes(bytes)
    }

    private fun ensureCapacity(needed: Int) {
        checkNotNull(ptr) { "BytesBuffer is closed" }
        if (capacity - writePosition < needed) {
            if (readPosition > capacity / 2) {
                val unreadSize = size
                if (unreadSize > 0) memcpy(ptr, ptr!! + readPosition, unreadSize.toULong())
                readPosition = 0
                writePosition = unreadSize
            }

            if (capacity - writePosition < needed) {
                var newCapacity = capacity * 2
                while (newCapacity - writePosition < needed) newCapacity *= 2
                val newPtr = realloc(ptr, newCapacity.toULong())
                    ?: throw OutOfMemoryError("Failed to reallocate native memory ($newCapacity bytes)")
                this.ptr = newPtr.reinterpret()
                this.capacity = newCapacity
            }
        }
    }


    public actual fun writeByte(value: Byte) {
        ensureCapacity(1)
        ptr!![writePosition] = value
        writePosition += 1
    }

    public actual fun writeShort(value: Short) {
        ensureCapacity(2)
        val v = value.toInt()
        writeByte((v shr 8).toByte())
        writeByte(v.toByte())
    }

    public actual fun writeInt(value: Int) {
        ensureCapacity(4)
        writeByte((value shr 24).toByte())
        writeByte((value shr 16).toByte())
        writeByte((value shr 8).toByte())
        writeByte(value.toByte())
    }

    public actual fun writeLong(value: Long) {
        ensureCapacity(8)
        writeByte((value shr 56).toByte())
        writeByte((value shr 48).toByte())
        writeByte((value shr 40).toByte())
        writeByte((value shr 32).toByte())
        writeByte((value shr 24).toByte())
        writeByte((value shr 16).toByte())
        writeByte((value shr 8).toByte())
        writeByte(value.toByte())
    }

    public actual fun writeFloat(value: Float) {
        writeInt(value.toRawBits())
    }

    public actual fun writeDouble(value: Double) {
        writeLong(value.toRawBits())
    }

    public actual fun writeBytes(bytes: ByteArray) {
        if (bytes.isEmpty()) return
        ensureCapacity(bytes.size)
        memScoped {
            val pinned = bytes.refTo(0).getPointer(this)
            memcpy(ptr!! + writePosition, pinned, bytes.size.toULong())
        }
        writePosition += bytes.size
    }

    public actual fun writeBoolean(value: Boolean): Unit = writeByte(if (value) 0x01 else 0x00)


    public actual fun readByte(): Byte {
        require(size >= 1) { "Buffer underflow: expected 1 byte, available $size" }
        checkNotNull(ptr) { "BytesBuffer is closed" }
        val value = ptr!![readPosition]
        readPosition += 1
        if (readPosition == writePosition) {
            readPosition = 0
            writePosition = 0
        }
        return value
    }

    public actual fun readUByte(): UByte = readByte().toUByte()

    public actual fun readShort(): Short {
        require(size >= 2) { "Buffer underflow: expected 2 bytes, available $size" }
        val b0 = readByte().toInt() and 0xFF
        val b1 = readByte().toInt() and 0xFF
        return ((b0 shl 8) or b1).toShort()
    }

    public actual fun readInt(): Int {
        require(size >= 4) { "Buffer underflow: expected 4 bytes, available $size" }
        val b0 = readByte().toInt() and 0xFF
        val b1 = readByte().toInt() and 0xFF
        val b2 = readByte().toInt() and 0xFF
        val b3 = readByte().toInt() and 0xFF
        return (b0 shl 24) or (b1 shl 16) or (b2 shl 8) or b3
    }

    public actual fun readLong(): Long {
        require(size >= 8) { "Buffer underflow: expected 8 bytes, available $size" }
        val b0 = readByte().toLong() and 0xFF
        val b1 = readByte().toLong() and 0xFF
        val b2 = readByte().toLong() and 0xFF
        val b3 = readByte().toLong() and 0xFF
        val b4 = readByte().toLong() and 0xFF
        val b5 = readByte().toLong() and 0xFF
        val b6 = readByte().toLong() and 0xFF
        val b7 = readByte().toLong() and 0xFF
        return (b0 shl 56) or (b1 shl 48) or (b2 shl 40) or (b3 shl 32) or
                (b4 shl 24) or (b5 shl 16) or (b6 shl 8) or b7
    }

    public actual fun readFloat(): Float = Float.fromBits(readInt())
    public actual fun readDouble(): Double = Double.fromBits(readLong())
    public actual fun readBytes(length: Int): ByteArray {
        require(size >= length) { "Buffer underflow: expected $length bytes, available $size" }
        checkNotNull(ptr) { "BytesBuffer is closed" }
        val bytes = ByteArray(length)
        if (length > 0) {
            memScoped {
                val pinned = bytes.refTo(0).getPointer(this)
                memcpy(pinned, ptr!! + readPosition, length.toULong())
            }
            readPosition += length
        }
        if (readPosition == writePosition) {
            readPosition = 0
            writePosition = 0
        }
        return bytes
    }

    public actual fun readBytes(): ByteArray = readBytes(size)

    public actual fun readBoolean(): Boolean = readByte() != 0x00.toByte()
    public actual fun peek(): ByteArray {
        val currentSize = size
        if (currentSize == 0) return ByteArray(0)
        val bytes = ByteArray(currentSize)
        memScoped {
            val pinned = bytes.refTo(0).getPointer(this)
            memcpy(pinned, ptr!! + readPosition, currentSize.toULong())
        }
        return bytes
    }

    actual override fun close() {
        if (ptr != null) {
            free(ptr)
            ptr = null
        }
        readPosition = 0
        writePosition = 0
        capacity = 0
    }

    public actual val size: Int
        get() = writePosition - readPosition

    public fun rawPointer(): CPointer<ByteVar>? {
        return if (ptr != null) (ptr!! + readPosition) else null
    }
}