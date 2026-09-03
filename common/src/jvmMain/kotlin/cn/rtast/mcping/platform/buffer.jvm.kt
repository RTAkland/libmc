/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping.platform

import java.io.ByteArrayOutputStream

@Suppress("CLASSNAME")
public actual class _Buffer {
    private val outStream = ByteArrayOutputStream()
    private var readBuffer: ByteArray? = null
    private var readOffset = 0

    public actual constructor()
    public actual constructor(bytes: ByteArray) {
        this.readBuffer = bytes
        outStream.write(bytes)
    }

    public actual fun writeByte(value: Byte) {
        outStream.write(value.toInt())
    }

    public actual fun writeShort(value: Short) {
        val v = value.toInt()
        outStream.write(v shr 8)
        outStream.write(v)
    }

    public actual fun writeLong(value: Long) {
        outStream.write((value shr 56).toInt())
        outStream.write((value shr 48).toInt())
        outStream.write((value shr 40).toInt())
        outStream.write((value shr 32).toInt())
        outStream.write((value shr 24).toInt())
        outStream.write((value shr 16).toInt())
        outStream.write((value shr 8).toInt())
        outStream.write(value.toInt())
    }

    public actual fun writeBytes(bytes: ByteArray) {
        outStream.write(bytes)
    }

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

    public actual fun readShort(): Short {
        val b1 = readByte().toInt() and 0xFF
        val b2 = readByte().toInt() and 0xFF
        return ((b1 shl 8) or b2).toShort()
    }

    public actual fun readLong(): Long {
        return (readByte().toLong() and 0xFF shl 56) or
                (readByte().toLong() and 0xFF shl 48) or
                (readByte().toLong() and 0xFF shl 40) or
                (readByte().toLong() and 0xFF shl 32) or
                (readByte().toLong() and 0xFF shl 24) or
                (readByte().toLong() and 0xFF shl 16) or
                (readByte().toLong() and 0xFF shl 8) or
                (readByte().toLong() and 0xFF)
    }

    public actual fun readBytes(length: Int): ByteArray {
        val array = ensureReadArray()
        if (readOffset + length > array.size) throw IndexOutOfBoundsException("Buffer underflow")
        val result = array.copyOfRange(readOffset, readOffset + length)
        readOffset += length
        return result
    }

    public actual fun toByteArray(): ByteArray = outStream.toByteArray()

    public actual val size: Int
        get() = outStream.size()
}