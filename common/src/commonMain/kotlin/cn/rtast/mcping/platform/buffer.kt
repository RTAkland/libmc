/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

@Suppress("CLASSNAME")
public expect class _Buffer {
    public constructor()
    public constructor(bytes: ByteArray)

    public fun writeByte(value: Byte)
    public fun writeShort(value: Short)
    public fun writeLong(value: Long)
    public fun writeBytes(bytes: ByteArray)

    public fun readByte(): Byte
    public fun readShort(): Short
    public fun readLong(): Long
    public fun readBytes(length: Int): ByteArray
    public fun toByteArray(): ByteArray
    public val size: Int
}

public fun ByteArray.wrap(): _Buffer = _Buffer(this)