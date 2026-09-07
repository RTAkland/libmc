/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/25
 */


package cn.rtast.libmc.nbt

import cn.rtast.libmc.stream.ByteOrder

public interface NBTInput {
    public val order: ByteOrder

    public suspend fun readByte(): Byte
    public suspend fun readBytes(count: Int): ByteArray

    public suspend fun readLong(): Long = if (order == ByteOrder.BIG_ENDIAN) readLongBE() else readLongLE()
    public suspend fun readFloat(): Float = if (order == ByteOrder.BIG_ENDIAN) readFloatBE() else readFloatLE()
    public suspend fun readDouble(): Double = if (order == ByteOrder.BIG_ENDIAN) readDoubleBE() else readDoubleLE()
    public suspend fun readShort(): Short = if (order == ByteOrder.BIG_ENDIAN) readShortBE() else readShortLE()
    public suspend fun readInt(): Int = if (order == ByteOrder.BIG_ENDIAN) readIntBE() else readIntLE()

    // big endian
    public suspend fun readLongBE(): Long =
        (readIntBE().toLong() shl 32) or (readIntBE().toLong() and 0xFFFFFFFFL)

    public suspend fun readFloatBE(): Float = Float.fromBits(readIntBE())
    public suspend fun readDoubleBE(): Double = Double.fromBits(readLongBE())
    public suspend fun readShortBE(): Short {
        val b1 = readByte().toInt() and 0xFF
        val b2 = readByte().toInt() and 0xFF
        return ((b1 shl 8) or b2).toShort()
    }

    public suspend fun readIntBE(): Int {
        return ((readByte().toInt() and 0xFF) shl 24) or
                ((readByte().toInt() and 0xFF) shl 16) or
                ((readByte().toInt() and 0xFF) shl 8) or
                (readByte().toInt() and 0xFF)
    }

    // little endian
    public suspend fun readLongLE(): Long {
        return ((readByte().toInt() and 0xFF).toLong() shl 0) or
                ((readByte().toInt() and 0xFF).toLong() shl 8) or
                ((readByte().toInt() and 0xFF).toLong() shl 16) or
                ((readByte().toInt() and 0xFF).toLong() shl 24) or
                ((readByte().toInt() and 0xFF).toLong() shl 32) or
                ((readByte().toInt() and 0xFF).toLong() shl 40) or
                ((readByte().toInt() and 0xFF).toLong() shl 48) or
                (readByte().toInt() and 0xFF).toLong()
    }

    public suspend fun readFloatLE(): Float = Float.fromBits(readIntLE())

    public suspend fun readDoubleLE(): Double = Double.fromBits(readLongLE())

    public suspend fun readShortLE(): Short =
        ((readByte().toInt() and 0xFF) or (readByte().toInt() and 0xFF shl 8)).toShort()

    public suspend fun readIntLE(): Int =
        ((readByte().toInt() and 0xFF)) or
                ((readByte().toInt() and 0xFF) shl 8) or
                ((readByte().toInt() and 0xFF) shl 16) or
                ((readByte().toInt() and 0xFF) shl 24)
}