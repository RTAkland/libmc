/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/25
 */


package cn.rtast.libmc.nbt

import cn.rtast.libmc.common.ByteOrder

public interface NBTInput {
    public val order: ByteOrder

    public fun readByte(): Byte
    public fun readBytes(count: Int): ByteArray

    public fun readLong(): Long = if (order == ByteOrder.BIG_ENDIAN) readLongBE() else readLongLE()
    public fun readFloat(): Float = if (order == ByteOrder.BIG_ENDIAN) readFloatBE() else readFloatLE()
    public fun readDouble(): Double = if (order == ByteOrder.BIG_ENDIAN) readDoubleBE() else readDoubleLE()
    public fun readShort(): Short = if (order == ByteOrder.BIG_ENDIAN) readShortBE() else readShortLE()
    public fun readInt(): Int = if (order == ByteOrder.BIG_ENDIAN) readIntBE() else readIntLE()

    // big endian
    public fun readLongBE(): Long =
        (readIntBE().toLong() shl 32) or (readIntBE().toLong() and 0xFFFFFFFFL)

    public fun readFloatBE(): Float = Float.fromBits(readIntBE())
    public fun readDoubleBE(): Double = Double.fromBits(readLongBE())
    public fun readShortBE(): Short {
        val b1 = readByte().toInt() and 0xFF
        val b2 = readByte().toInt() and 0xFF
        return ((b1 shl 8) or b2).toShort()
    }

    public fun readIntBE(): Int {
        return ((readByte().toInt() and 0xFF) shl 24) or
                ((readByte().toInt() and 0xFF) shl 16) or
                ((readByte().toInt() and 0xFF) shl 8) or
                (readByte().toInt() and 0xFF)
    }

    // little endian
    public fun readLongLE(): Long {
        return ((readByte().toInt() and 0xFF).toLong() shl 0) or
                ((readByte().toInt() and 0xFF).toLong() shl 8) or
                ((readByte().toInt() and 0xFF).toLong() shl 16) or
                ((readByte().toInt() and 0xFF).toLong() shl 24) or
                ((readByte().toInt() and 0xFF).toLong() shl 32) or
                ((readByte().toInt() and 0xFF).toLong() shl 40) or
                ((readByte().toInt() and 0xFF).toLong() shl 48) or
                (readByte().toInt() and 0xFF).toLong()
    }

    public fun readFloatLE(): Float = Float.fromBits(readIntLE())

    public fun readDoubleLE(): Double = Double.fromBits(readLongLE())

    public fun readShortLE(): Short =
        ((readByte().toInt() and 0xFF) or (readByte().toInt() and 0xFF shl 8)).toShort()

    public fun readIntLE(): Int =
        ((readByte().toInt() and 0xFF)) or
                ((readByte().toInt() and 0xFF) shl 8) or
                ((readByte().toInt() and 0xFF) shl 16) or
                ((readByte().toInt() and 0xFF) shl 24)
}