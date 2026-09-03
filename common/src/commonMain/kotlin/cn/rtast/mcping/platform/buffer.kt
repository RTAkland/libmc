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
    public fun writeShort(value: Short, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeInt(value: Int, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeLong(value: Long, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeBytes(bytes: ByteArray)

    public fun readByte(): Byte
    public fun readShort(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Short
    public fun readInt(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Int
    public fun readLong(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Long
    public fun readBytes(length: Int): ByteArray
    public fun toByteArray(): ByteArray
    public fun close()
    public val size: Int
}

public fun ByteArray.wrap(): _Buffer = _Buffer(this)