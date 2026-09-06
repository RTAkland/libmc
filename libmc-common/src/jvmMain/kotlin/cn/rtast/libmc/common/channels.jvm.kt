/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.common

import java.io.EOFException
import java.io.InputStream
import java.io.OutputStream

public actual open class ReadChannel public actual constructor() {
    private lateinit var _inputStream: InputStream

    public constructor(inputStream: InputStream) : this() {
        this._inputStream = inputStream
    }

    public actual open fun readFully(out: ByteArray, start: Int, end: Int) {
        var bytesRead = 0
        val length = end - start
        while (bytesRead < length) {
            val read = _inputStream.read(out, start + bytesRead, length - bytesRead)
            if (read == -1) throw EOFException("End of stream reached")
            bytesRead += read
        }
    }

    public actual open fun readByte(): Byte {
        val buf = ByteArray(1)
        readFully(buf, 0, 1)
        return buf[0]
    }

    public actual open fun readBytes(length: Int): ByteArray {
        val bytes = ByteArray(length)
        readFully(bytes, 0, length)
        return bytes
    }

    public actual open fun readShort(endian: ByteOrder): Short = readBytes(2).toShort(endian)
    public actual open fun readInt(endian: ByteOrder): Int = readBytes(4).toInt(endian)
    public actual open fun readLong(endian: ByteOrder): Long = readBytes(8).toLong(endian)
}

public actual open class WriteChannel public actual constructor() {
    private lateinit var _outputStream: OutputStream

    public constructor(outputStream: OutputStream) : this() {
        _outputStream = outputStream
    }

    public actual open fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int): Unit =
        _outputStream.write(value, startIndex, endIndex - startIndex)

    public actual open fun flush(): Unit = _outputStream.flush()
}