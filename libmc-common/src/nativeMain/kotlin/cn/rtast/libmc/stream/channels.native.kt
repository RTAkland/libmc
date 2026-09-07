/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.stream

import io.ktor.utils.io.*
import io.ktor.utils.io.bits.*

public actual open class ReadChannel public actual constructor() {

    private lateinit var _readChannel: ByteReadChannel

    public constructor(readChannel: ByteReadChannel) : this() {
        this._readChannel = readChannel
    }

    public actual open suspend fun readByte(): Byte = _readChannel.readByte()
    public actual open suspend fun readBytes(length: Int): ByteArray = _readChannel.readByteArray(length)
    public actual open suspend fun readFully(out: ByteArray, start: Int, end: Int): Unit =
        _readChannel.readFully(out, start, end)

    public actual open suspend fun readShort(endian: ByteOrder): Short {
        val bytes = readBytes(2)
        val v = ((bytes[0].toInt() and 0xFF shl 8) or (bytes[1].toInt() and 0xFF)).toShort()
        return if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }

    public actual open suspend fun readInt(endian: ByteOrder): Int {
        val bytes = readBytes(4)
        val v = (bytes[0].toInt() and 0xFF shl 24) or
                (bytes[1].toInt() and 0xFF shl 16) or
                (bytes[2].toInt() and 0xFF shl 8) or
                (bytes[3].toInt() and 0xFF)
        return if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }

    public actual open suspend fun readLong(endian: ByteOrder): Long {
        val bytes = readBytes(8)
        var v = 0L
        for (i in 0 until 8) {
            v = (v shl 8) or (bytes[i].toLong() and 0xFF)
        }
        return if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }
}

public actual open class WriteChannel public actual constructor() {
    private lateinit var _writeChannel: ByteWriteChannel

    public constructor(writeChannel: ByteWriteChannel) : this() {
        _writeChannel = writeChannel
    }

    public actual open suspend fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int): Unit =
        _writeChannel.writeFully(value, startIndex, endIndex)

    public actual open suspend fun flush(): Unit = _writeChannel.flush()
}