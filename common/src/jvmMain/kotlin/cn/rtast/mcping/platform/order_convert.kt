/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.mcping.platform

internal fun ByteArray.toShort(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Short {
    val b1 = this[0].toInt() and 0xFF
    val b2 = this[1].toInt() and 0xFF
    return if (endian == ByteOrder.BIG_ENDIAN) {
        ((b1 shl 8) or b2).toShort()
    } else {
        ((b2 shl 8) or b1).toShort()
    }
}

internal fun ByteArray.toInt(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Int {
    val b1 = this[0].toInt() and 0xFF
    val b2 = this[1].toInt() and 0xFF
    val b3 = this[2].toInt() and 0xFF
    val b4 = this[3].toInt() and 0xFF
    return if (endian == ByteOrder.BIG_ENDIAN) {
        (b1 shl 24) or (b2 shl 16) or (b3 shl 8) or b4
    } else {
        (b4 shl 24) or (b3 shl 16) or (b2 shl 8) or b1
    }
}

internal fun ByteArray.toLong(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Long {
    var result = 0L
    if (endian == ByteOrder.BIG_ENDIAN) {
        for (b in this) result = (result shl 8) or (b.toLong() and 0xFF)
    } else {
        for (i in 7 downTo 0) result = (result shl 8) or (this[i].toLong() and 0xFF)
    }
    return result
}