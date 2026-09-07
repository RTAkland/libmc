/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/25
 */


package cn.rtast.libmc.nbt

import cn.rtast.libmc.gzipCompress
import cn.rtast.libmc.gzipDecompress
import cn.rtast.libmc.zlibCompress
import cn.rtast.libmc.zlibDecompress

public sealed interface NbtCompressor {
    public fun compress(input: ByteArray): ByteArray
    public fun decompress(input: ByteArray): ByteArray

    public object GZipCompressor : NbtCompressor {
        override fun compress(input: ByteArray): ByteArray = input.gzipCompress()
        override fun decompress(input: ByteArray): ByteArray = input.gzipDecompress()
    }

    public object ZlibCompressor : NbtCompressor {
        override fun compress(input: ByteArray): ByteArray = input.zlibCompress()
        override fun decompress(input: ByteArray): ByteArray = input.zlibDecompress()
    }
}