/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.common

import java.io.EOFException
import java.io.InputStream
import java.io.OutputStream

public actual class ReadChannel(private val _inputStream: InputStream) {

    public actual fun readByte(): Byte = _inputStream.read().toByte()
    public actual fun readBytes(length: Int): ByteArray = _inputStream.readNBytes(length)

    public actual fun readFully(out: ByteArray, start: Int, end: Int) {
        var bytesRead = 0
        val length = end - start
        while (bytesRead < length) {
            val read = _inputStream.read(out, start + bytesRead, length - bytesRead)
            if (read == -1) throw IllegalStateException("End of stream")
            bytesRead += read
        }
    }

    public actual fun readShort(endian: ByteOrder): Short = readBytes(2).toShort(endian)
    public actual fun readInt(endian: ByteOrder): Int = readBytes(4).toInt(endian)
    public actual fun readLong(endian: ByteOrder): Long = readBytes(8).toLong(endian)
}

public actual class WriteChannel {
    private val _outputStream: OutputStream

    public constructor(outputStream: OutputStream) {
        _outputStream = outputStream
    }

    public actual fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int): Unit =
        _outputStream.write(value, startIndex, endIndex - startIndex)

    public actual fun flush(): Unit = _outputStream.flush()
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