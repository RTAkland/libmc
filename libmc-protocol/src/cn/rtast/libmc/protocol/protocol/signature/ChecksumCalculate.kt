/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.signature

public fun javaArraysHashCode(bytes: ByteArray): Int {
    var result = 1
    for (b in bytes) {
        result = 31 * result + b.toInt()
    }
    return result
}

public fun calculateChatChecksum(signaturesLastSeen: List<ByteArray>): Byte {
    if (signaturesLastSeen.isEmpty()) return 1
    val hashCodes = IntArray(signaturesLastSeen.size)
    for (i in signaturesLastSeen.indices) {
        hashCodes[i] = javaArraysHashCode(signaturesLastSeen[i])
    }
    val combinedBytes = ByteArray(hashCodes.size * 4)
    for (i in hashCodes.indices) {
        val h = hashCodes[i]
        val offset = i * 4
        combinedBytes[offset] = (h ushr 24).toByte()
        combinedBytes[offset + 1] = (h ushr 16).toByte()
        combinedBytes[offset + 2] = (h ushr 8).toByte()
        combinedBytes[offset + 3] = h.toByte()
    }
    val finalIntHash = javaArraysHashCode(combinedBytes)
    val finalByte = finalIntHash.toByte()
    return if (finalByte == 0.toByte()) 1 else finalByte
}