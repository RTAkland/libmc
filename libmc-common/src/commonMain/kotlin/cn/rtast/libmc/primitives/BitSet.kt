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
