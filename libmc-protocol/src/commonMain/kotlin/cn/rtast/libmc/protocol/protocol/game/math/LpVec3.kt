/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.math

import cn.rtast.libmc.common.stream.ByteOrder
import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.common.primitives.writeVarInt
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

// ref: https://minecraft.wiki/w/Java_Edition_protocol/Data_types#LpVec3
public data class LpVec3(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    @Suppress("UNUSED")
    public companion object {
        public val ZERO: LpVec3 = LpVec3(0.0, 0.0, 0.0)

        private const val MAX_QUANTIZED_VALUE = 32766.0
        private const val CONTINUATION_FLAG = 0x04L
        private const val SCALE_BITS = 0x03L

        public fun pack(value: Long): Long =
            ((value * 0.5 + 0.5) * MAX_QUANTIZED_VALUE).toLong()

        public fun unpack(value: Long): Double =
            min((value and 32767L).toDouble(), MAX_QUANTIZED_VALUE) * 2.0 / MAX_QUANTIZED_VALUE - 1.0
    }
}

// ref: https://minecraft.wiki/w/Java_Edition_protocol/Data_types#LpVec3
internal suspend fun BytesBuffer.readLpVec3(): LpVec3 {
    val byte1 = readByte().toInt() and 0xFF
    if (byte1 == 0) return LpVec3.ZERO
    val byte2 = readByte().toInt() and 0xFF
    val bytes3To6 = readInt(ByteOrder.BIG_ENDIAN).toLong() and 0xFFFFFFFFL
    val packed = (bytes3To6 shl 16) or (byte2.toLong() shl 8) or byte1.toLong()
    var scaleFactor = byte1.toLong() and 0x03L
    if ((byte1.toLong() and 0x04L) != 0L) scaleFactor = scaleFactor or (readVarInt().toLong() shl 2)
    val scale = scaleFactor.toDouble()
    val x = LpVec3.unpack(packed shr 3) * scale
    val y = LpVec3.unpack(packed shr 18) * scale
    val z = LpVec3.unpack(packed shr 33) * scale
    return LpVec3(x, y, z)
}

// ref: https://minecraft.wiki/w/Java_Edition_protocol/Data_types#LpVec3
internal suspend fun BytesBuffer.writeLpVec3(vec3: LpVec3) {
    val maxCoordinate = max(abs(vec3.x), max(abs(vec3.y), abs(vec3.z)))
    if (maxCoordinate.isNaN() || maxCoordinate < 1.0 / 32766.0) {
        writeByte(0x00)
        return
    }
    val scaleFactor = ceil(maxCoordinate).toLong()
    val needContinuation = (scaleFactor and 0x03L) != scaleFactor
    val packedScale = if (needContinuation) ((scaleFactor and 0x03L) or 0x04L) else scaleFactor
    val packedX = LpVec3.pack((vec3.x / scaleFactor.toDouble()).toLong()) shl 3
    val packedY = LpVec3.pack((vec3.y / scaleFactor.toDouble()).toLong()) shl 18
    val packedZ = LpVec3.pack((vec3.z / scaleFactor.toDouble()).toLong()) shl 33
    val packed = packedZ or packedY or packedX or packedScale
    writeByte(packed.toByte())
    writeByte((packed shr 8).toByte())
    writeInt((packed shr 16).toInt(), ByteOrder.BIG_ENDIAN)
    if (needContinuation) writeVarInt((scaleFactor shr 2).toInt())
}