/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.stream

import io.ktor.utils.io.core.*
import kotlinx.io.*
import kotlinx.io.Buffer

public actual class BytesBuffer {
    private val _delegateBuf: Buffer

    public actual constructor() {
        _delegateBuf = Buffer()
    }

    public actual constructor(bytes: ByteArray) {
        _delegateBuf = Buffer().apply { write(bytes) }
    }

    public actual suspend fun writeByte(value: Byte): Unit = _delegateBuf.writeByte(value)
    public actual suspend fun writeShort(value: Short, endian: ByteOrder) {
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeShort(value)
        else _delegateBuf.writeShortLe(value)
    }

    public actual suspend fun writeInt(value: Int, endian: ByteOrder) {
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeInt(value)
        else _delegateBuf.writeIntLe(value)
    }

    public actual suspend fun writeLong(value: Long, endian: ByteOrder) {
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeLong(value)
        else _delegateBuf.writeLongLe(value)
    }

    public actual suspend fun writeDouble(value: Double, endian: ByteOrder): Unit =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeDouble(value) else _delegateBuf.writeDoubleLe(value)

    public actual suspend fun writeFloat(value: Float, endian: ByteOrder): Unit =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeFloat(value) else _delegateBuf.writeFloatLe(value)

    public actual suspend fun writeBytes(bytes: ByteArray): Unit = _delegateBuf.write(bytes)
    public actual suspend fun writeBoolean(value: Boolean): Unit = _delegateBuf.writeByte(if (value) 0x01 else 0x00)

    public actual suspend fun readByte(): Byte = _delegateBuf.readByte()
    public actual suspend fun readUByte(): UByte = this.readByte().toUByte()
    public actual suspend fun readShort(endian: ByteOrder): Short =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.readShort() else _delegateBuf.readShortLe()

    public actual suspend fun readInt(endian: ByteOrder): Int =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.readInt() else _delegateBuf.readIntLe()

    public actual suspend fun readLong(endian: ByteOrder): Long =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.readLong() else _delegateBuf.readLongLe()

    public actual suspend fun readDouble(endian: ByteOrder): Double =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.readDouble() else _delegateBuf.readDoubleLe()

    public actual suspend fun readFloat(endian: ByteOrder): Float =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.readFloat() else _delegateBuf.readFloatLe()

    public actual suspend fun readBytes(length: Int): ByteArray = _delegateBuf.readByteArray(length)
    public actual suspend fun readBoolean(): Boolean = _delegateBuf.readByte() != 0x00.toByte()
    public actual suspend fun readRemainingBytes(): ByteArray = this.toByteArray()

    public actual suspend fun toByteArray(): ByteArray {
        val copy = _delegateBuf.peek()
        return try {
            copy.readByteArray()
        } finally {
            copy.close()
        }
    }

    public actual suspend fun hasRemaining(): Boolean = !_delegateBuf.exhausted()
    public actual suspend fun close(): Unit = _delegateBuf.close()

    public actual val size: Int get() = _delegateBuf.size.toInt()
    public actual val remaining: Long get() = _delegateBuf.remaining
}