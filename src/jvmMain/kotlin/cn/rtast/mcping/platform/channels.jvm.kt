/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

import java.io.EOFException
import java.io.InputStream
import java.io.OutputStream

internal actual class ReadChannel {
    private val _inputStream: InputStream

    constructor(inputStream: InputStream) {
        _inputStream = inputStream
    }

    actual fun readByte(): Byte = _inputStream.read().toByte()
    actual fun readBytes(length: Int): ByteArray = _inputStream.readNBytes(length)
    actual fun readFully(out: ByteArray, start: Int, end: Int) {
        var bytesRead = 0
        val length = end - start
        while (bytesRead < length) {
            val read = _inputStream.read(out, start + bytesRead, length - bytesRead)
            if (read == -1) throw IllegalStateException("End of stream")
            bytesRead += read
        }
    }

    actual fun readLong(): Long {
        val bytes = ByteArray(8)
        var read = 0
        while (read < 8) {
            val count = _inputStream.read(bytes, read, 8 - read)
            if (count == -1) throw EOFException()
            read += count
        }
        return ((bytes[0].toLong() and 0xFF shl 56) or
                (bytes[1].toLong() and 0xFF shl 48) or
                (bytes[2].toLong() and 0xFF shl 40) or
                (bytes[3].toLong() and 0xFF shl 32) or
                (bytes[4].toLong() and 0xFF shl 24) or
                (bytes[5].toLong() and 0xFF shl 16) or
                (bytes[6].toLong() and 0xFF shl 8) or
                (bytes[7].toLong() and 0xFF))
    }
}

internal actual class WriteChannel {
    private val _outputStream: OutputStream

    constructor(outputStream: OutputStream) {
        _outputStream = outputStream
    }

    actual fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int) =
        _outputStream.write(value, startIndex, endIndex - startIndex)

    actual fun flush() = _outputStream.flush()
}

private fun InputStream.readNBytes(length: Int): ByteArray {
    val buffer = ByteArray(length)
    var totalRead = 0
    while (totalRead < length) {
        val read = this.read(buffer, totalRead, length - totalRead)
        if (read == -1) throw EOFException()
        totalRead += read
    }
    return buffer
}