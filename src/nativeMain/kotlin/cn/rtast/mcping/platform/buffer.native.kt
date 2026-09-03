/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

import kotlinx.io.Buffer
import kotlinx.io.readByteArray

internal actual class PlatformBuffer actual constructor() {
    private val _delegateBuf = Buffer()

    actual fun writeByte(value: Byte) = _delegateBuf.writeByte(value)
    actual fun writeBytes(bytes: ByteArray) = _delegateBuf.write(bytes)
    actual fun readByte(): Byte = _delegateBuf.readByte()
    actual fun readBytes(length: Int): ByteArray = _delegateBuf.readByteArray(length)
    actual fun toByteArray(): ByteArray = _delegateBuf.peek().readByteArray()

    actual val size: Int
        get() = _delegateBuf.size.toInt()
}