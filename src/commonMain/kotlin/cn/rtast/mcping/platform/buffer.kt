/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

internal expect class PlatformBuffer {
    constructor()
    constructor(bytes: ByteArray)

    fun writeByte(value: Byte)
    fun writeShort(value: Short)
    fun writeLong(value: Long)
    fun writeBytes(bytes: ByteArray)

    fun readByte(): Byte
    fun readShort(): Short
    fun readLong(): Long
    fun readBytes(length: Int): ByteArray
    fun toByteArray(): ByteArray
    val size: Int
}

internal fun ByteArray.wrap(): PlatformBuffer = PlatformBuffer(this)