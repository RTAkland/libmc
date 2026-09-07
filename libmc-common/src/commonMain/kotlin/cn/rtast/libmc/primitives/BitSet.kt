/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.primitives

import cn.rtast.libmc.stream.BytesBuffer

public typealias BitSet = LongArray

public suspend fun BytesBuffer.readBitSet(): BitSet {
    val count = this.readVarInt()
    return LongArray(count) { this.readLong() }
}

public suspend fun BytesBuffer.writeBitSet(data: BitSet) {
    this.writeVarInt(data.size)
    for (i in data.indices) this.writeLong(data[i])
}

public fun BitSet.countSetBits(): Int {
    var count = 0
    for (i in indices) count += this[i].countOneBits()
    return count
}

public fun BitSet.getBit(bitIndex: Int): Boolean {
    val longIndex = bitIndex shr 6
    if (longIndex !in this.indices) return false
    val bitOffset = bitIndex and 63
    return (this[longIndex] and (1L shl bitOffset)) != 0L
}

public class FixedBitSet20(initialBits: Int = 0) {
    private var bits: Int = initialBits and 0xFFFFF

    public operator fun get(index: Int): Boolean {
        require(index in 0..19) { "Index out of range [0, 19]" }
        return (bits and (1 shl index)) != 0
    }

    public operator fun set(index: Int, value: Boolean) {
        require(index in 0..19) { "Index out of range [0, 19]" }
        bits = if (value) {
            bits or (1 shl index)
        } else {
            bits and (1 shl index).inv()
        }
    }

    public fun toByteArray(): ByteArray {
        val result = ByteArray(3)
        result[0] = (bits and 0xFF).toByte()
        result[1] = ((bits shr 8) and 0xFF).toByte()
        result[2] = ((bits shr 16) and 0x0F).toByte()
        return result
    }
}

public fun createFixedBitSet20(initialBits: Int = 0): FixedBitSet20 =
    FixedBitSet20(initialBits)