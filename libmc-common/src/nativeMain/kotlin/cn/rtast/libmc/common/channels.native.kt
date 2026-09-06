/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.common

import io.ktor.utils.io.*
import io.ktor.utils.io.bits.*
import kotlinx.coroutines.runBlocking

public actual open class ReadChannel public actual constructor() {

    private lateinit var _readChannel: ByteReadChannel

    public constructor(readChannel: ByteReadChannel) : this() {
        this._readChannel = readChannel
    }

    public actual open fun readByte(): Byte = runBlocking { _readChannel.readByte() }
    public actual open fun readBytes(length: Int): ByteArray = runBlocking { _readChannel.readByteArray(length) }
    public actual open fun readFully(out: ByteArray, start: Int, end: Int): Unit =
        runBlocking { _readChannel.readFully(out, start, end) }

    public actual open fun readShort(endian: ByteOrder): Short {
        val bytes = readBytes(2)
        val v = ((bytes[0].toInt() and 0xFF shl 8) or (bytes[1].toInt() and 0xFF)).toShort()
        return if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }

    public actual open fun readInt(endian: ByteOrder): Int {
        val bytes = readBytes(4)
        val v = (bytes[0].toInt() and 0xFF shl 24) or
                (bytes[1].toInt() and 0xFF shl 16) or
                (bytes[2].toInt() and 0xFF shl 8) or
                (bytes[3].toInt() and 0xFF)
        return if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }

    public actual open fun readLong(endian: ByteOrder): Long {
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

    public actual open fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int): Unit =
        runBlocking { _writeChannel.writeFully(value, startIndex, endIndex) }

    public actual open fun flush(): Unit = runBlocking { _writeChannel.flush() }
}