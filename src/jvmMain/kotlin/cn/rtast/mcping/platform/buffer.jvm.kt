/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping.platform

import java.io.ByteArrayOutputStream

internal actual class PlatformBuffer actual constructor() {
    private val stream = ByteArrayOutputStream()
    private var readOffset = 0

    actual fun writeByte(value: Byte) = stream.write(value.toInt())
    actual fun writeBytes(bytes: ByteArray) = stream.write(bytes)
    actual fun readByte(): Byte {
        val array = stream.toByteArray()
        if (readOffset >= array.size) throw IndexOutOfBoundsException("Buffer underflow")
        return array[readOffset++]
    }

    actual fun readBytes(length: Int): ByteArray {
        val array = stream.toByteArray()
        if (readOffset + length > array.size) throw IndexOutOfBoundsException("Buffer underflow")
        val result = array.copyOfRange(readOffset, readOffset + length)
        readOffset += length
        return result
    }

    actual fun toByteArray(): ByteArray = stream.toByteArray()

    actual val size: Int
        get() = stream.size()
}