/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

import java.io.InputStream
import java.io.OutputStream

internal actual class PlatformReadChannel {
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
}

internal actual class PlatformWriteChannel {
    private val _outputStream: OutputStream

    constructor(outputStream: OutputStream) {
        _outputStream = outputStream
    }

    actual fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int) =
        _outputStream.write(value, startIndex, endIndex - startIndex)

    actual fun flush() = _outputStream.flush()
}