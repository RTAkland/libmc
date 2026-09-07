/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.stream

public expect open class ReadChannel() {
    public open suspend fun readByte(): Byte
    public open suspend fun readShort(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Short
    public open suspend fun readInt(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Int
    public open suspend fun readLong(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Long
    public open suspend fun readBytes(length: Int): ByteArray
    public open suspend fun readFully(out: ByteArray, start: Int = 0, end: Int = out.size)
}

public expect open class WriteChannel() {
    public open suspend fun writeFully(value: ByteArray, startIndex: Int = 0, endIndex: Int = value.size)
    public open suspend fun flush()
}

public suspend fun ReadChannel.readVarInt(): Int {
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