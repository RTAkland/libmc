/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping.platform

import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking

internal actual class ReadChannel {
    private val _readChannel: ByteReadChannel

    constructor(readChannel: ByteReadChannel) {
        _readChannel = readChannel
    }

    actual fun readByte(): Byte = runBlocking { _readChannel.readByte() }
    actual fun readBytes(length: Int): ByteArray = runBlocking { _readChannel.readByteArray(length) }
    actual fun readFully(out: ByteArray, start: Int, end: Int) = runBlocking { _readChannel.readFully(out, start, end) }
    actual fun readLong(): Long = runBlocking { _readChannel.readLong() }
}

internal actual class WriteChannel {
    private val _writeChannel: ByteWriteChannel

    constructor(writeChannel: ByteWriteChannel) {
        _writeChannel = writeChannel
    }

    actual fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int) =
        runBlocking { _writeChannel.writeFully(value, startIndex, endIndex) }

    actual fun flush() = runBlocking { _writeChannel.flush() }
}