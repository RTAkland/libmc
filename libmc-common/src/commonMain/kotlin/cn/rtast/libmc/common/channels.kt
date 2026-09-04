/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.common

@Suppress("CLASSNAME")
public expect class _ReadChannel {
    public fun readByte(): Byte
    public fun readShort(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Short
    public fun readInt(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Int
    public fun readLong(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Long
    public fun readBytes(length: Int): ByteArray
    public fun readFully(out: ByteArray, start: Int = 0, end: Int = out.size)
}

@Suppress("CLASSNAME")
public expect class _WriteChannel {
    public fun writeFully(value: ByteArray, startIndex: Int = 0, endIndex: Int = value.size)
    public fun flush()
}

public fun _ReadChannel.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Byte
    do {
        read = this.readByte()
        val value = (read.toInt() and 0x7F)
        result = result or (value shl (7 * numRead))
        numRead++
        if (numRead > 5) throw IllegalArgumentException("VarInt is too big")
    } while ((read.toInt() and 0x80) != 0)
    return result
}