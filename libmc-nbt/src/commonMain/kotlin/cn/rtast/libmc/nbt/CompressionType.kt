/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/27
 */


package cn.rtast.libmc.nbt

public enum class CompressionType(internal val compressor: NbtCompressor?) {
    Zlib(NbtCompressor.ZlibCompressor),
    Gzip(NbtCompressor.GZipCompressor),
    Raw(null)
}