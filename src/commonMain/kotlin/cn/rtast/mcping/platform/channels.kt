/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

internal expect class PlatformReadChannel {
    fun readByte(): Byte
    fun readBytes(length: Int): ByteArray
    fun readFully(out: ByteArray, start: Int = 0, end: Int = out.size)
}

internal expect class PlatformWriteChannel {
    fun writeFully(value: ByteArray, startIndex: Int = 0, endIndex: Int = value.size)
    fun flush()
}