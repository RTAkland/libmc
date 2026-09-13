/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package client

import cn.rtast.libmc.primitives.FixedBitSet20
import cn.rtast.libmc.primitives.createFixedBitSet20

class ClientChatTracker {
    private val lastSeenQueue = ArrayDeque<ByteArray>(20)
    var pendingMessageCount: Int = 0
        private set

    fun onReceivePlayerChat(signature: ByteArray?) {
        if (signature == null || signature.size != 256) return
        if (lastSeenQueue.size >= 20) lastSeenQueue.removeFirst()
        lastSeenQueue.addLast(signature)
        pendingMessageCount++
    }

    fun prepareForOutgoingMessage(): ChatStateSnapshot {
        val signatures = lastSeenQueue.toList()
        val count = pendingMessageCount
        pendingMessageCount = 0
        return ChatStateSnapshot(signatures, count)
    }
}

data class ChatStateSnapshot(val lastSeenSignatures: List<ByteArray>, val messageCount: Int)

fun createAcknowledgedBitSet(lastSeenSignatures: List<ByteArray>): FixedBitSet20 {
    val bitSet = createFixedBitSet20()
    for (i in lastSeenSignatures.indices) {
        if (i >= 20) break
        bitSet[i] = true
    }
    return bitSet
}

object ChatPacketUtils {
    fun computeByteArrayHashCode(bytes: ByteArray): Int {
        var result = 1
        for (element in bytes) result = 31 * result + element.toInt()
        return result
    }

    fun computePacketChecksum(lastSeenSignatures: List<ByteArray>): Byte {
        var combinedHash = 1
        for (sig in lastSeenSignatures) {
            val sigHash = computeByteArrayHashCode(sig)
            combinedHash = 31 * combinedHash + sigHash
        }
        val resultByte = combinedHash.toByte()
        return if (resultByte == 0.toByte()) 1.toByte() else resultByte
    }
}