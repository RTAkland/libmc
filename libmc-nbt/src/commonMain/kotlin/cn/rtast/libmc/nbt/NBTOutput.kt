/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/25
 */


package cn.rtast.libmc.nbt

import cn.rtast.libmc.network.ByteOrder

public interface NBTOutput {
    public val order: ByteOrder
    public val root: NBTTag.CompoundTag

    public fun writeByte(value: Byte)
    public fun writeBytes(value: ByteArray)

    public fun writeShort(value: Short): Unit =
        if (order == ByteOrder.BIG_ENDIAN) writeShortBE(value) else writeShortLE(value)

    public fun writeInt(value: Int): Unit =
        if (order == ByteOrder.BIG_ENDIAN) writeIntBE(value) else writeIntLE(value)

    public fun writeLong(value: Long): Unit =
        if (order == ByteOrder.BIG_ENDIAN) writeLongBE(value) else writeLongLE(value)

    public fun writeFloat(value: Float): Unit =
        if (order == ByteOrder.BIG_ENDIAN) writeFloatBE(value) else writeFloatLE(value)

    public fun writeDouble(value: Double): Unit =
        if (order == ByteOrder.BIG_ENDIAN) writeDoubleBE(value) else writeDoubleLE(value)

    // big endian
    public fun writeShortBE(value: Short) {
        writeByte(((value.toInt() shr 8) and 0xFF).toByte())
        writeByte((value.toInt() and 0xFF).toByte())
    }

    public fun writeIntBE(value: Int) {
        writeByte(((value shr 24) and 0xFF).toByte())
        writeByte(((value shr 16) and 0xFF).toByte())
        writeByte(((value shr 8) and 0xFF).toByte())
        writeByte((value and 0xFF).toByte())
    }

    public fun writeLongBE(value: Long) {
        writeByte(((value shr 56).toInt() and 0xFF).toByte())
        writeByte(((value shr 48).toInt() and 0xFF).toByte())
        writeByte(((value shr 40).toInt() and 0xFF).toByte())
        writeByte(((value shr 32).toInt() and 0xFF).toByte())
        writeByte(((value shr 24).toInt() and 0xFF).toByte())
        writeByte(((value shr 16).toInt() and 0xFF).toByte())
        writeByte(((value shr 8).toInt() and 0xFF).toByte())
        writeByte((value.toInt() and 0xFF).toByte())
    }

    public fun writeFloatBE(value: Float) {
        writeIntBE(value.toBits())
    }

    public fun writeDoubleBE(value: Double) {
        writeLongBE(value.toBits())
    }

    // Little Endian
    public fun writeShortLE(value: Short) {
        writeByte((value.toInt() and 0xFF).toByte())
        writeByte(((value.toInt() shr 8) and 0xFF).toByte())
    }

    public fun writeIntLE(value: Int) {
        writeByte((value and 0xFF).toByte())
        writeByte(((value shr 8) and 0xFF).toByte())
        writeByte(((value shr 16) and 0xFF).toByte())
        writeByte(((value shr 24) and 0xFF).toByte())
    }

    public fun writeLongLE(value: Long) {
        writeByte(((value and 0xFF).toInt()).toByte())
        writeByte((((value shr 8) and 0xFF).toInt()).toByte())
        writeByte((((value shr 16) and 0xFF).toInt()).toByte())
        writeByte((((value shr 24) and 0xFF).toInt()).toByte())
        writeByte((((value shr 32) and 0xFF).toInt()).toByte())
        writeByte((((value shr 40) and 0xFF).toInt()).toByte())
        writeByte((((value shr 48) and 0xFF).toInt()).toByte())
        writeByte((((value shr 56) and 0xFF).toInt()).toByte())
    }

    public fun writeFloatLE(value: Float): Unit = writeIntLE(value.toBits())
    public fun writeDoubleLE(value: Double): Unit = writeLongLE(value.toBits())
    public fun toByteArray(): ByteArray
}