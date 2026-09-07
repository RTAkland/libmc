/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.stream

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    public actual suspend fun writeByte(value: Byte) {
        readBuffer = null
        outStream.write(value.toInt())
    }

    public actual suspend fun writeShort(value: Short, endian: ByteOrder) {
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

    public actual suspend fun writeInt(value: Int, endian: ByteOrder) {
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

    public actual suspend fun writeLong(value: Long, endian: ByteOrder) {
        readBuffer = null
        if (endian == ByteOrder.BIG_ENDIAN) {
            for (i in 56 downTo 0 step 8) outStream.write((value shr i).toInt())
        } else {
            for (i in 0..56 step 8) outStream.write((value shr i).toInt())
        }
    }

    public actual suspend fun writeDouble(value: Double, endian: ByteOrder) {
        writeLong(value.toRawBits(), endian)
    }

    public actual suspend fun writeFloat(value: Float, endian: ByteOrder) {
        writeInt(value.toRawBits(), endian)
    }

    public actual suspend fun writeBytes(bytes: ByteArray) {
        readBuffer = null
        withContext(Dispatchers.IO) { outStream.write(bytes) }
    }

    public actual suspend fun writeBoolean(value: Boolean): Unit = writeByte(if (value) 0x01 else 0x00)

    private suspend fun ensureReadArray(): ByteArray {
        var buf = readBuffer
        if (buf == null) {
            buf = outStream.toByteArray()
            readBuffer = buf
        }
        return buf
    }

    public actual suspend fun readByte(): Byte {
        val array = ensureReadArray()
        if (readOffset >= array.size) throw IndexOutOfBoundsException("Buffer underflow")
        return array[readOffset++]
    }

    public actual suspend fun readUByte(): UByte = this.readByte().toUByte()
    public actual suspend fun readShort(endian: ByteOrder): Short = readBytes(2).toShort(endian)
    public actual suspend fun readInt(endian: ByteOrder): Int = readBytes(4).toInt(endian)
    public actual suspend fun readLong(endian: ByteOrder): Long = readBytes(8).toLong(endian)

    public actual suspend fun readDouble(endian: ByteOrder): Double {
        return Double.fromBits(readLong(endian))
    }

    public actual suspend fun readFloat(endian: ByteOrder): Float {
        return Float.fromBits(readInt(endian))
    }

    public actual suspend fun readBytes(length: Int): ByteArray {
        val array = ensureReadArray()
        if (readOffset + length > array.size) throw IndexOutOfBoundsException("Buffer underflow")
        val result = array.copyOfRange(readOffset, readOffset + length)
        readOffset += length
        return result
    }

    public actual suspend fun readBoolean(): Boolean = this.readByte() != 0x00.toByte()

    public actual suspend fun readRemainingBytes(): ByteArray {
        val array = ensureReadArray()
        if (readOffset >= array.size) return byteArrayOf()
        val result = array.copyOfRange(readOffset, array.size)
        readOffset = array.size
        return result
    }

    public actual suspend fun toByteArray(): ByteArray = outStream.toByteArray()
    public actual suspend fun hasRemaining(): Boolean = readOffset < ensureReadArray().size

    public actual suspend fun close(): Unit = withContext(Dispatchers.IO) { outStream.close() }
    public actual val size: Int get() = outStream.size()
    public actual val remaining: Long get() = (outStream.size() - readOffset).coerceAtLeast(0).toLong()
}