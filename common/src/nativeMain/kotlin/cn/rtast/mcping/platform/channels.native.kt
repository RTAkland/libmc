/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping.platform

import io.ktor.utils.io.*
import io.ktor.utils.io.bits.reverseByteOrder
import kotlinx.coroutines.runBlocking

@Suppress("CLASSNAME")
public actual class _ReadChannel(private val _readChannel: ByteReadChannel) {

    public actual fun readByte(): Byte = runBlocking { _readChannel.readByte() }
    public actual fun readBytes(length: Int): ByteArray = runBlocking { _readChannel.readByteArray(length) }
    public actual fun readFully(out: ByteArray, start: Int, end: Int): Unit =
        runBlocking { _readChannel.readFully(out, start, end) }

    public actual fun readShort(endian: ByteOrder): Short = runBlocking {
        val v = _readChannel.readShort()
        if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }

    public actual fun readInt(endian: ByteOrder): Int = runBlocking {
        val v = _readChannel.readInt()
        if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }

    public actual fun readLong(endian: ByteOrder): Long = runBlocking {
        val v = _readChannel.readLong()
        if (endian == ByteOrder.BIG_ENDIAN) v else v.reverseByteOrder()
    }
}

@Suppress("CLASSNAME")
public actual class _WriteChannel {
    private val _writeChannel: ByteWriteChannel

    public constructor(writeChannel: ByteWriteChannel) {
        _writeChannel = writeChannel
    }

    public actual fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int): Unit =
        runBlocking { _writeChannel.writeFully(value, startIndex, endIndex) }

    public actual fun flush(): Unit = runBlocking { _writeChannel.flush() }
}