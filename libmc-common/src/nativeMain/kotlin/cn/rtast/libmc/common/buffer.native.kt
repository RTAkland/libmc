/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.common

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import kotlinx.io.Buffer
import kotlinx.io.readByteArray

@Suppress("CLASSNAME")
public actual class _Buffer {
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
        else _delegateBuf.writeShort(value.reverseByteOrder())
    }

    public actual fun writeInt(value: Int, endian: ByteOrder) {
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeInt(value)
        else _delegateBuf.writeInt(value.reverseByteOrder())
    }

    public actual fun writeLong(value: Long, endian: ByteOrder) {
        if (endian == ByteOrder.BIG_ENDIAN) _delegateBuf.writeLong(value)
        else _delegateBuf.writeLong(value.reverseByteOrder())
    }

    public actual fun writeBytes(bytes: ByteArray): Unit = _delegateBuf.write(bytes)
    public actual fun readByte(): Byte = _delegateBuf.readByte()
    public actual fun readShort(endian: ByteOrder): Short {
        val v = _delegateBuf.readShort()
        return if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }

    public actual fun readInt(endian: ByteOrder): Int {
        val v = _delegateBuf.readInt()
        return if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }

    public actual fun readLong(endian: ByteOrder): Long {
        val v = _delegateBuf.readLong()
        return if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }

    public actual fun readBytes(length: Int): ByteArray = _delegateBuf.readByteArray(length)
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