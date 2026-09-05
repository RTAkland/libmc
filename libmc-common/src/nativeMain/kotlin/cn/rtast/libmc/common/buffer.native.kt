/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.common

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

    public actual fun writeByte(value: Byte): Unit = _delegateBuf.writeByte(value)
    public actual fun writeShort(value: Short, endian: ByteOrder) {
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeShort(value)
        else _delegateBuf.writeShortLe(value)
    }

    public actual fun writeInt(value: Int, endian: ByteOrder) {
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeInt(value)
        else _delegateBuf.writeIntLe(value)
    }

    public actual fun writeLong(value: Long, endian: ByteOrder) {
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeLong(value)
        else _delegateBuf.writeLongLe(value)
    }

    public actual fun writeDouble(value: Double, endian: ByteOrder): Unit =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeDouble(value) else _delegateBuf.writeDoubleLe(value)

    public actual fun writeFloat(value: Float, endian: ByteOrder): Unit =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeFloat(value) else _delegateBuf.writeFloatLe(value)

    public actual fun writeBytes(bytes: ByteArray): Unit = _delegateBuf.write(bytes)
    public actual fun writeBoolean(value: Boolean): Unit = _delegateBuf.writeByte(if (value) 0x01 else 0x00)

    public actual fun readByte(): Byte = _delegateBuf.readByte()
    public actual fun readShort(endian: ByteOrder): Short =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.readShort() else _delegateBuf.readShortLe()

    public actual fun readInt(endian: ByteOrder): Int =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.readInt() else _delegateBuf.readIntLe()

    public actual fun readLong(endian: ByteOrder): Long =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.readLong() else _delegateBuf.readLongLe()

    public actual fun readDouble(endian: ByteOrder): Double =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.readDouble() else _delegateBuf.readDoubleLe()

    public actual fun readFloat(endian: ByteOrder): Float =
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.readFloat() else _delegateBuf.readFloatLe()

    public actual fun readBytes(length: Int): ByteArray = _delegateBuf.readByteArray(length)
    public actual fun readBoolean(): Boolean = _delegateBuf.readByte() != 0x00.toByte()
    public actual fun readRemainingBytes(): ByteArray = this.toByteArray()

    public actual fun toByteArray(): ByteArray {
        val copy = _delegateBuf.peek()
        return try {
            copy.readByteArray()
        } finally {
            copy.close()
        }
    }

    public actual fun hasRemaining(): Boolean = !_delegateBuf.exhausted()
    public actual fun close(): Unit = _delegateBuf.close()

    public actual val size: Int get() = _delegateBuf.size.toInt()
    public actual val remaining: Long get() = _delegateBuf.remaining
}