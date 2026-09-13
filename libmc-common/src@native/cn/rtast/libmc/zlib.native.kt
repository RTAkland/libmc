/*
 * Copyright © 2025-2026 RTAkland
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc

import kotlinx.cinterop.*
import platform.zlib.*

private const val MAX_WBITS = 15
private const val DEFAULT_BUFFER_SIZE = 4096

private inline fun ByteArray.processDecompress(initStream: (CPointer<z_stream>) -> Unit): ByteArray = memScoped {
    val stream = alloc<z_stream>()
    stream.zalloc = null
    stream.zfree = null
    stream.opaque = null
    initStream(stream.ptr)
    stream.next_in = this@processDecompress.refTo(0).getPointer(this).reinterpret()
    stream.avail_in = this@processDecompress.size.toUInt()
    val output = mutableListOf<Byte>()
    val tempBuffer = ByteArray(DEFAULT_BUFFER_SIZE)

    try {
        do {
            stream.next_out = tempBuffer.refTo(0).getPointer(this).reinterpret()
            stream.avail_out = DEFAULT_BUFFER_SIZE.toUInt()
            val result = inflate(stream.ptr, Z_NO_FLUSH)
            check(result == Z_OK || result == Z_STREAM_END) { "inflate error: $result" }
            val bytesDecompressed = DEFAULT_BUFFER_SIZE - stream.avail_out.toInt()
            output.addAll(tempBuffer.take(bytesDecompressed))

            if (result == Z_STREAM_END) break
        } while (stream.avail_out == 0u)
    } finally {
        inflateEnd(stream.ptr)
    }
    return output.toByteArray()
}

private inline fun ByteArray.processCompress(initStream: (CPointer<z_stream>) -> Unit): ByteArray = memScoped {
    val stream = alloc<z_stream>()
    stream.zalloc = null
    stream.zfree = null
    stream.opaque = null
    initStream(stream.ptr)
    stream.next_in = this@processCompress.refTo(0).getPointer(this).reinterpret()
    stream.avail_in = this@processCompress.size.toUInt()
    val output = mutableListOf<Byte>()
    val tempBuffer = ByteArray(DEFAULT_BUFFER_SIZE)
    try {
        do {
            stream.next_out = tempBuffer.refTo(0).getPointer(this).reinterpret()
            stream.avail_out = DEFAULT_BUFFER_SIZE.toUInt()
            val result = deflate(stream.ptr, Z_FINISH)
            check(result == Z_OK || result == Z_STREAM_END) { "deflate error: $result" }
            val have = DEFAULT_BUFFER_SIZE - stream.avail_out.toInt()
            output.addAll(tempBuffer.take(have))
        } while (stream.avail_out == 0u)
    } finally {
        deflateEnd(stream.ptr)
    }

    return output.toByteArray()
}

public actual fun ByteArray.zlibDecompress(expectedSize: Int): ByteArray = memScoped {
    val stream = alloc<z_stream>()
    stream.zalloc = null
    stream.zfree = null
    stream.opaque = null
    check(inflateInit_(stream.ptr, ZLIB_VERSION, sizeOf<z_stream>().toInt()) == Z_OK) { "inflateInit_ failed" }
    val result = ByteArray(expectedSize)
    try {
        stream.next_in = this@zlibDecompress.refTo(0).getPointer(this).reinterpret()
        stream.avail_in = this@zlibDecompress.size.toUInt()
        stream.next_out = result.refTo(0).getPointer(this).reinterpret()
        stream.avail_out = expectedSize.toUInt()
        var totalRead = 0
        while (stream.avail_in > 0u && totalRead < expectedSize) {
            val status = inflate(stream.ptr, Z_NO_FLUSH)
            check(status == Z_STREAM_END || status == Z_OK) { "inflate failed with status: $status" }
            val decompressedThisTurn = expectedSize - totalRead - stream.avail_out.toInt()
            totalRead += decompressedThisTurn
            if (status == Z_STREAM_END) break
        }
        check(totalRead == expectedSize) {
            "Decompression failed: expected $expectedSize bytes, but got $totalRead"
        }
        return result
    } finally {
        inflateEnd(stream.ptr)
    }
}

public actual fun ByteArray.gzipCompress(): ByteArray = processCompress { ptr ->
    check(
        deflateInit2_(
            ptr, Z_DEFAULT_COMPRESSION, Z_DEFLATED,
            16 + MAX_WBITS, 8, Z_DEFAULT_STRATEGY, ZLIB_VERSION, sizeOf<z_stream>().toInt()
        ) == Z_OK
    ) { "deflateInit2_ failed" }
}

public actual fun ByteArray.gzipDecompress(): ByteArray = processDecompress { ptr ->
    check(
        inflateInit2_(
            ptr, 16 + MAX_WBITS, ZLIB_VERSION, sizeOf<z_stream>().toInt()
        ) == Z_OK
    ) { "inflateInit2_ failed" }
}

public actual fun ByteArray.zlibCompress(): ByteArray = processCompress { ptr ->
    check(
        deflateInit_(
            ptr, Z_DEFAULT_COMPRESSION, ZLIB_VERSION, sizeOf<z_stream>().toInt()
        ) == Z_OK
    ) { "deflateInit_ failed" }
}

public actual fun ByteArray.zlibDecompress(): ByteArray = processDecompress { ptr ->
    check(
        inflateInit_(
            ptr, ZLIB_VERSION, sizeOf<z_stream>().toInt()
        ) == Z_OK
    ) { "inflateInit_ failed" }
}