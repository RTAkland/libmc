/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */

package cn.rtast.libmc.common

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
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

private fun ByteArray.gzipDecompress(): ByteArray {
    if (isEmpty()) return byteArrayOf()
    ByteArrayInputStream(this).use { bais ->
        GZIPInputStream(bais).use { gzis ->
            val outputStream = ByteArrayOutputStream(this.size * 2)
            val buffer = ByteArray(1024)
            var len: Int
            while (gzis.read(buffer).also { len = it } != -1) {
                outputStream.write(buffer, 0, len)
            }
            return outputStream.toByteArray()
        }
    }
}