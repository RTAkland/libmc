/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping.platform

import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking

@Suppress("CLASSNAME")
public actual class _ReadChannel {
    private val _readChannel: ByteReadChannel

    public constructor(readChannel: ByteReadChannel) {
        _readChannel = readChannel
    }

    public actual fun readByte(): Byte = runBlocking { _readChannel.readByte() }
    public actual fun readBytes(length: Int): ByteArray = runBlocking { _readChannel.readByteArray(length) }
    public actual fun readFully(out: ByteArray, start: Int, end: Int): Unit =
        runBlocking { _readChannel.readFully(out, start, end) }

    public actual fun readLong(): Long = runBlocking { _readChannel.readLong() }
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