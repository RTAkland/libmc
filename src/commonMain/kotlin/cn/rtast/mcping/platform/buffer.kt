/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

internal expect class PlatformBuffer() {
    fun writeByte(value: Byte)
    fun writeBytes(bytes: ByteArray)
    fun readByte(): Byte
    fun readBytes(length: Int): ByteArray
    fun toByteArray(): ByteArray
    val size: Int
}