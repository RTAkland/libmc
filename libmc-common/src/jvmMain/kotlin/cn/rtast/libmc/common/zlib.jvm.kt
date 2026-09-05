/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */

package cn.rtast.libmc.common

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.Inflater

public actual fun ByteArray.zlibDecompress(): ByteArray {
    if (isEmpty()) return byteArrayOf()
    val isGzip = size >= 2 && this[0] == 0x1F.toByte() && this[1] == 0x8B.toByte()
    return if (isGzip) this.gzipDecompress() else {
        val inflater = Inflater(false)
        val outputStream = ByteArrayOutputStream(this.size)
        val buffer = ByteArray(1024)
        inflater.setInput(this)
        while (!inflater.finished()) {
            val length = inflater.inflate(buffer)
            if (length > 0) outputStream.write(buffer, 0, length)
        }
        inflater.end()
        outputStream.toByteArray()
    }
}

public actual fun ByteArray.zlibDecompress(expectedSize: Int): ByteArray {
    val inflater = Inflater()
    inflater.setInput(this)
    val result = ByteArray(expectedSize)
    var totalRead = 0
    try {
        while (!inflater.finished() && totalRead < expectedSize) {
            val read = inflater.inflate(result, totalRead, expectedSize - totalRead)
            if (read == 0) break
            totalRead += read
        }
        check(totalRead == expectedSize) { "Decompression failed: expected $expectedSize bytes, but got $totalRead" }
        return result
    } finally {
        inflater.end()
    }
}

public actual fun ByteArray.zlibCompress(): ByteArray {
    val deflater = Deflater()
    deflater.setInput(this)
    deflater.finish()
    val bos = ByteArrayOutputStream(this.size)
    val buffer = ByteArray(1024)
    while (!deflater.finished()) {
        val count = deflater.deflate(buffer)
        if (count > 0) bos.write(buffer, 0, count)
    }
    deflater.end()
    return bos.toByteArray()
}

public actual fun ByteArray.gzipCompress(): ByteArray {
    val bos = ByteArrayOutputStream()
    GZIPOutputStream(bos).use { it.write(this) }
    return bos.toByteArray()
}

public actual fun ByteArray.gzipDecompress(): ByteArray {
    return GZIPInputStream(ByteArrayInputStream(this)).use { it.readBytes() }
}