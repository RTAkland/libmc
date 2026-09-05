/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.common

import java.io.ByteArrayOutputStream

public actual class BytesBuffer {
    private val outStream = ByteArrayOutputStream()
    private var readBuffer: ByteArray? = null
    private var readOffset = 0

    public actual constructor()
    public actual constructor(bytes: ByteArray) {
        this.readBuffer = bytes
        outStream.write(bytes)
    }

    public actual fun writeByte(value: Byte) {
        readBuffer = null
        outStream.write(value.toInt())
    }

    public actual fun writeShort(value: Short, endian: ByteOrder) {
        readBuffer = null
        val v = value.toInt()
        if (endian == ByteOrder.BIG_ENDIAN) {
            outStream.write(v shr 8)
            outStream.write(v)
        } else {
            outStream.write(v)
            outStream.write(v shr 8)
        }
    }

    public actual fun writeInt(value: Int, endian: ByteOrder) {
        readBuffer = null
        if (endian == ByteOrder.BIG_ENDIAN) {
            outStream.write(value shr 24)
            outStream.write(value shr 16)
            outStream.write(value shr 8)
            outStream.write(value)
        } else {
            outStream.write(value)
            outStream.write(value shr 8)
            outStream.write(value shr 16)
            outStream.write(value shr 24)
        }
    }

    public actual fun writeLong(value: Long, endian: ByteOrder) {
        readBuffer = null
        if (endian == ByteOrder.BIG_ENDIAN) {
            for (i in 56 downTo 0 step 8) outStream.write((value shr i).toInt())
        } else {
            for (i in 0..56 step 8) outStream.write((value shr i).toInt())
        }
    }

    public actual fun writeDouble(value: Double, endian: ByteOrder) {
        writeLong(value.toRawBits(), endian)
    }

    public actual fun writeFloat(value: Float, endian: ByteOrder) {
        writeInt(value.toRawBits(), endian)
    }

    public actual fun writeBytes(bytes: ByteArray) {
        readBuffer = null
        outStream.write(bytes)
    }

    public actual fun writeBoolean(value: Boolean): Unit = writeByte(if (value) 0x01 else 0x00)

    private fun ensureReadArray(): ByteArray {
        var buf = readBuffer
        if (buf == null) {
            buf = outStream.toByteArray()
            readBuffer = buf
        }
        return buf
    }

    public actual fun readByte(): Byte {
        val array = ensureReadArray()
        if (readOffset >= array.size) throw IndexOutOfBoundsException("Buffer underflow")
        return array[readOffset++]
    }

    public actual fun readShort(endian: ByteOrder): Short = readBytes(2).toShort(endian)
    public actual fun readInt(endian: ByteOrder): Int = readBytes(4).toInt(endian)
    public actual fun readLong(endian: ByteOrder): Long = readBytes(8).toLong(endian)

    public actual fun readDouble(endian: ByteOrder): Double {
        return Double.fromBits(readLong(endian))
    }

    public actual fun readFloat(endian: ByteOrder): Float {
        return Float.fromBits(readInt(endian))
    }

    public actual fun readBytes(length: Int): ByteArray {
        val array = ensureReadArray()
        if (readOffset + length > array.size) throw IndexOutOfBoundsException("Buffer underflow")
        val result = array.copyOfRange(readOffset, readOffset + length)
        readOffset += length
        return result
    }

    public actual fun readBoolean(): Boolean = this.readByte() != 0x00.toByte()

    public actual fun readRemainingBytes(): ByteArray {
        val array = ensureReadArray()
        if (readOffset >= array.size) return byteArrayOf()
        val result = array.copyOfRange(readOffset, array.size)
        readOffset = array.size
        return result
    }

    public actual fun toByteArray(): ByteArray = outStream.toByteArray()
    public actual fun hasRemaining(): Boolean = readOffset < ensureReadArray().size

    public actual fun close(): Unit = outStream.close()
    public actual val size: Int get() = outStream.size()
    public actual val remaining: Long get() = (outStream.size() - readOffset).coerceAtLeast(0).toLong()
}