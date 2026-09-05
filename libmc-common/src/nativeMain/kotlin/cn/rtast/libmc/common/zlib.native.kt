/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */

@file:OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)

package cn.rtast.libmc.common

import kotlinx.cinterop.*
import platform.posix.u_longVar
import platform.zlib.*

private const val ENABLE_ZLIB_GZIP_HEADER = 15 + 32

public actual fun ByteArray.zlibDecompress(): ByteArray {
    if (isEmpty()) return ByteArray(0)
    return memScoped {
        val stream = alloc<z_stream>()
        stream.zalloc = null
        stream.zfree = null
        stream.opaque = null

        val initResult = inflateInit2_(
            stream.ptr,
            ENABLE_ZLIB_GZIP_HEADER,
            ZLIB_VERSION,
            sizeOf<z_stream>().toInt()
        )
        check(initResult == Z_OK) { "inflateInit2_ failed with code: $initResult" }

        val inputPinned = this@zlibDecompress.pin()
        try {
            stream.next_in = inputPinned.addressOf(0).reinterpret()
            stream.avail_in = this@zlibDecompress.size.toUInt()

            val bufferSize = 4096
            val tempBuffer = ByteArray(bufferSize)
            val tempPinned = tempBuffer.pin()
            val output = ArrayList<Byte>(this@zlibDecompress.size * 3)

            try {
                var result: Int
                do {
                    stream.next_out = tempPinned.addressOf(0).reinterpret()
                    stream.avail_out = bufferSize.toUInt()

                    result = inflate(stream.ptr, Z_NO_FLUSH)
                    check(result == Z_OK || result == Z_STREAM_END) { "inflate error: $result" }

                    val bytesDecompressed = bufferSize - stream.avail_out.toInt()
                    for (i in 0 until bytesDecompressed) {
                        output.add(tempBuffer[i])
                    }

                    if (result == Z_STREAM_END) break
                } while (stream.avail_in > 0u || stream.avail_out == 0u)
            } finally {
                tempPinned.unpin()
            }

            inflateEnd(stream.ptr)
            return@memScoped output.toByteArray()
        } finally {
            inputPinned.unpin()
        }
    }
}

public actual fun ByteArray.zlibDecompress(expectedSize: Int): ByteArray {
    val result = ByteArray(expectedSize)
    if (this.isEmpty()) return result
    memScoped {
        val destLen = alloc<u_longVar>()
        destLen.value = expectedSize.toUInt()
        val res = uncompress(
            result.refTo(0).getPointer(this).reinterpret(),
            destLen.ptr,
            this@zlibDecompress.refTo(0).getPointer(this).reinterpret(),
            this@zlibDecompress.size.toUInt()
        )
        check(res == Z_OK) { "zlib uncompress failed with error code: $res" }
    }
    return result
}

public actual fun ByteArray.zlibCompress(): ByteArray {
    if (this.isEmpty()) return byteArrayOf()
    val maxCompressedLen = compressBound(this.size.toUInt()).toInt()
    val output = ByteArray(maxCompressedLen)
    memScoped {
        val destLen = alloc<u_longVar>()
        destLen.value = maxCompressedLen.toUInt()
        val res = compress(
            output.refTo(0).getPointer(this).reinterpret(),
            destLen.ptr,
            this@zlibCompress.refTo(0).getPointer(this).reinterpret(),
            this@zlibCompress.size.toUInt()
        )
        check(res == Z_OK) { "zlib compress failed with error code: $res" }
        return output.copyOf(destLen.value.toInt())
    }
}