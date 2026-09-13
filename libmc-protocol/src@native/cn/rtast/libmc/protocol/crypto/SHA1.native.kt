/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */

package cn.rtast.libmc.protocol.crypto

internal actual fun sha1Digest(data: ByteArray): ByteArray = Sha1.digest(data)

/**
 * PERFORMANCE IMPROVEMENT REQUIRED.
 */
private object Sha1 {
    private fun rotateLeft(value: Int, bits: Int): Int {
        return (value shl bits) or (value ushr (32 - bits))
    }

    private fun align(address: Int, alignment: Int): Int {
        val tmp = alignment - 1
        return (address + tmp) and tmp.inv()
    }

    fun digest(input: ByteArray): ByteArray {
        val bitLength = input.size.toLong() * 8L
        val bufferSize = align(input.size + 9, 64)
        val buffer = ByteArray(bufferSize)
        input.copyInto(buffer)
        buffer[input.size] = 0x80.toByte()
        for (i in 0 until 8) buffer[bufferSize - 8 + i] = ((bitLength ushr ((7 - i) * 8)) and 0xFFL).toByte()
        var h0 = 0x67452301
        var h1 = -0x10325477
        var h2 = -0x67452302
        var h3 = 0x10325476
        var h4 = -0x3C2D1E10
        val w = IntArray(80)
        for (offset in buffer.indices step 64) {
            for (i in 0 until 16) {
                val idx = offset + (i * 4)
                w[i] = ((buffer[idx].toInt() and 0xFF) shl 24) or
                        ((buffer[idx + 1].toInt() and 0xFF) shl 16) or
                        ((buffer[idx + 2].toInt() and 0xFF) shl 8) or
                        (buffer[idx + 3].toInt() and 0xFF)
            }
            for (i in 16 until 80) w[i] = rotateLeft(w[i - 3] xor w[i - 8] xor w[i - 14] xor w[i - 16], 1)
            var a = h0
            var b = h1
            var c = h2
            var d = h3
            var e = h4
            for (i in 0 until 80) {
                val f: Int
                val k: Int
                when (i) {
                    in 0..19 -> {
                        f = (b and c) or (b.inv() and d)
                        k = 0x5A827999
                    }

                    in 20..39 -> {
                        f = b xor c xor d
                        k = 0x6ED9EBA1.toInt()
                    }

                    in 40..59 -> {
                        f = (b and c) or (b and d) or (c and d)
                        k = -0x70E44324
                    }

                    else -> {
                        f = b xor c xor d
                        k = -0x359D3E2A
                    }
                }

                val temp = rotateLeft(a, 5) + f + e + k + w[i]
                e = d; d = c
                c = rotateLeft(b, 30)
                b = a; a = temp
            }
            h0 += a; h1 += b
            h2 += c; h3 += d
            h4 += e
        }
        val result = ByteArray(20)
        val state = intArrayOf(h0, h1, h2, h3, h4)
        for (i in 0 until 5) {
            val v = state[i]
            result[i * 4] = (v ushr 24).toByte()
            result[i * 4 + 1] = (v ushr 16).toByte()
            result[i * 4 + 2] = (v ushr 8).toByte()
            result[i * 4 + 3] = v.toByte()
        }
        return result
    }
}