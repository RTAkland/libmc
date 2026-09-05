/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.util

internal object CycloneMd5 {
    const val BLOCK_SIZE = 64
    const val DIGEST_SIZE = 16
    const val MIN_PAD_SIZE = 9

    val OID = byteArrayOf(
        0x2A.toByte(), 0x86.toByte(), 0x48.toByte(), 0x86.toByte(),
        0xF7.toByte(), 0x0D.toByte(), 0x02.toByte(), 0x05.toByte()
    )

    private val PADDING = ByteArray(64).apply { this[0] = 0x80.toByte() }

    private val K = intArrayOf(
        0xD76AA478.toInt(), 0xE8C7B756.toInt(), 0x242070DB, 0xC1BDCEEE.toInt(),
        0xF57C0FAF.toInt(), 0x4787C62A, 0xA8304613.toInt(), 0xFD469501.toInt(),
        0x698098D8, 0x8B44F7AF.toInt(), 0xFFFF5BB1.toInt(), 0x895CD7BE.toInt(),
        0x6B901122, 0xFD987193.toInt(), 0xA679438E.toInt(), 0x49B40821,
        0xF61E2562.toInt(), 0xC040B340.toInt(), 0x265E5A51, 0xE9B6C7AA.toInt(),
        0xD62F105D.toInt(), 0x02441453, 0xD8A1E681.toInt(), 0xE7D3FBC8.toInt(),
        0x21E1CDE6, 0xC33707D6.toInt(), 0xF4D50D87.toInt(), 0x455A14ED,
        0xA9E3E905.toInt(), 0xFCEFA3F8.toInt(), 0x676F02D9, 0x8D2A4C8A.toInt(),
        0xFFFA3942.toInt(), 0x8771F681.toInt(), 0x6D9D6122, 0xFDE5380C.toInt(),
        0xA4BEEA44.toInt(), 0x4BDECFA9, 0xF6BB4B60.toInt(), 0xBEBFBC70.toInt(),
        0x289B7EC6, 0xEAA127FA.toInt(), 0xD4EF3085.toInt(), 0x04881D05,
        0xD9D4D039.toInt(), 0xE6DB99E5.toInt(), 0x1FA27CF8, 0xC4AC5665.toInt(),
        0xF4292244.toInt(), 0x432AFF97, 0xAB9423A7.toInt(), 0xFC93A039.toInt(),
        0x655B59C3, 0x8F0CCC92.toInt(), 0xFFEFF47D.toInt(), 0x85845DD1.toInt(),
        0x6FA87E4F, 0xFE2CE6E0.toInt(), 0xA3014314.toInt(), 0x4E0811A1,
        0xF7537E82.toInt(), 0xBD3AF235.toInt(), 0x2AD7D2BB, 0xEB86D391.toInt()
    )

    private class Context {
        val h = IntArray(4)
        val buffer = ByteArray(64)
        val x = IntArray(16)
        var size: Int = 0
        var totalSize: Long = 0L
    }

    fun compute(data: ByteArray): ByteArray {
        val digest = ByteArray(DIGEST_SIZE)
        val context = Context()
        initContext(context)
        updateContext(context, data, 0, data.size)
        finalContext(context, digest)
        return digest
    }

    fun computeToHex(data: ByteArray): String {
        return compute(data).toHexString()
    }

    fun computeToHex(text: String): String {
        return compute(text.encodeToByteArray()).toHexString()
    }

    fun computeToBytes(data: ByteArray): ByteArray = compute(data)

    private fun initContext(context: Context) {
        context.h[0] = 0x67452301
        context.h[1] = 0xEFCDAB89.toInt()
        context.h[2] = 0x98BADCFE.toInt()
        context.h[3] = 0x10325476
        context.size = 0
        context.totalSize = 0L
    }

    private fun updateContext(context: Context, data: ByteArray, offset: Int, length: Int) {
        var dataOffset = offset
        var remLength = length

        while (remLength > 0) {
            val n = minOf(remLength, 64 - context.size)
            data.copyInto(context.buffer, context.size, dataOffset, dataOffset + n)

            context.size += n
            context.totalSize += n
            dataOffset += n
            remLength -= n

            if (context.size == 64) {
                processBlock(context)
                context.size = 0
            }
        }
    }

    private fun finalContext(context: Context, digest: ByteArray) {
        var totalBits: Long = context.totalSize * 8L
        val paddingSize = if (context.size < 56) {
            56 - context.size
        } else {
            64 + 56 - context.size
        }

        updateContext(context, PADDING, 0, paddingSize)

        for (i in 0 until 8) {
            context.buffer[56 + i] = (totalBits and 0xFFL).toByte()
            totalBits = totalBits ushr 8
        }

        processBlock(context)

        for (i in 0 until (DIGEST_SIZE / 4)) {
            store32le(context.h[i], digest, i * 4)
        }
    }

    private fun processBlock(context: Context) {
        var a = context.h[0]
        var b = context.h[1]
        var c = context.h[2]
        var d = context.h[3]

        val x = context.x

        for (i in 0 until 16) {
            x[i] = load32le(context.buffer, i * 4)
        }

        // Round 1
        a = ff(a, b, c, d, x[0], 7, K[0])
        d = ff(d, a, b, c, x[1], 12, K[1])
        c = ff(c, d, a, b, x[2], 17, K[2])
        b = ff(b, c, d, a, x[3], 22, K[3])
        a = ff(a, b, c, d, x[4], 7, K[4])
        d = ff(d, a, b, c, x[5], 12, K[5])
        c = ff(c, d, a, b, x[6], 17, K[6])
        b = ff(b, c, d, a, x[7], 22, K[7])
        a = ff(a, b, c, d, x[8], 7, K[8])
        d = ff(d, a, b, c, x[9], 12, K[9])
        c = ff(c, d, a, b, x[10], 17, K[10])
        b = ff(b, c, d, a, x[11], 22, K[11])
        a = ff(a, b, c, d, x[12], 7, K[12])
        d = ff(d, a, b, c, x[13], 12, K[13])
        c = ff(c, d, a, b, x[14], 17, K[14])
        b = ff(b, c, d, a, x[15], 22, K[15])

        // Round 2
        a = gg(a, b, c, d, x[1], 5, K[16])
        d = gg(d, a, b, c, x[6], 9, K[17])
        c = gg(c, d, a, b, x[11], 14, K[18])
        b = gg(b, c, d, a, x[0], 20, K[19])
        a = gg(a, b, c, d, x[5], 5, K[20])
        d = gg(d, a, b, c, x[10], 9, K[21])
        c = gg(c, d, a, b, x[15], 14, K[22])
        b = gg(b, c, d, a, x[4], 20, K[23])
        a = gg(a, b, c, d, x[9], 5, K[24])
        d = gg(d, a, b, c, x[14], 9, K[25])
        c = gg(c, d, a, b, x[3], 14, K[26])
        b = gg(b, c, d, a, x[8], 20, K[27])
        a = gg(a, b, c, d, x[13], 5, K[28])
        d = gg(d, a, b, c, x[2], 9, K[29])
        c = gg(c, d, a, b, x[7], 14, K[30])
        b = gg(b, c, d, a, x[12], 20, K[31])

        // Round 3
        a = hh(a, b, c, d, x[5], 4, K[32])
        d = hh(d, a, b, c, x[8], 11, K[33])
        c = hh(c, d, a, b, x[11], 16, K[34])
        b = hh(b, c, d, a, x[14], 23, K[35])
        a = hh(a, b, c, d, x[1], 4, K[36])
        d = hh(d, a, b, c, x[4], 11, K[37])
        c = hh(c, d, a, b, x[7], 16, K[38])
        b = hh(b, c, d, a, x[10], 23, K[39])
        a = hh(a, b, c, d, x[13], 4, K[40])
        d = hh(d, a, b, c, x[0], 11, K[41])
        c = hh(c, d, a, b, x[3], 16, K[42])
        b = hh(b, c, d, a, x[6], 23, K[43])
        a = hh(a, b, c, d, x[9], 4, K[44])
        d = hh(d, a, b, c, x[12], 11, K[45])
        c = hh(c, d, a, b, x[15], 16, K[46])
        b = hh(b, c, d, a, x[2], 23, K[47])

        // Round 4
        a = ii(a, b, c, d, x[0], 6, K[48])
        d = ii(d, a, b, c, x[7], 10, K[49])
        c = ii(c, d, a, b, x[14], 15, K[50])
        b = ii(b, c, d, a, x[5], 21, K[51])
        a = ii(a, b, c, d, x[12], 6, K[52])
        d = ii(d, a, b, c, x[3], 10, K[53])
        c = ii(c, d, a, b, x[10], 15, K[54])
        b = ii(b, c, d, a, x[1], 21, K[55])
        a = ii(a, b, c, d, x[8], 6, K[56])
        d = ii(d, a, b, c, x[15], 10, K[57])
        c = ii(c, d, a, b, x[6], 15, K[58])
        b = ii(b, c, d, a, x[13], 21, K[59])
        a = ii(a, b, c, d, x[4], 6, K[60])
        d = ii(d, a, b, c, x[11], 10, K[61])
        c = ii(c, d, a, b, x[2], 15, K[62])
        b = ii(b, c, d, a, x[9], 21, K[63])

        context.h[0] += a
        context.h[1] += b
        context.h[2] += c
        context.h[3] += d
    }

    private fun ByteArray.toHexString(): String {
        val hexChars = CharArray(size * 2)
        val hexArray = "0123456789abcdef".toCharArray()
        for (i in indices) {
            val v = this[i].toInt() and 0xFF
            hexChars[i * 2] = hexArray[v ushr 4]
            hexChars[i * 2 + 1] = hexArray[v and 0x0F]
        }
        return hexChars.concatToString()
    }

    private fun rol32(a: Int, s: Int): Int = (a shl s) or (a ushr (32 - s))

    private fun load32le(buf: ByteArray, offset: Int): Int {
        return (buf[offset].toInt() and 0xFF) or
                ((buf[offset + 1].toInt() and 0xFF) shl 8) or
                ((buf[offset + 2].toInt() and 0xFF) shl 16) or
                ((buf[offset + 3].toInt() and 0xFF) shl 24)
    }

    private fun store32le(val32: Int, buf: ByteArray, offset: Int) {
        buf[offset] = (val32 and 0xFF).toByte()
        buf[offset + 1] = ((val32 ushr 8) and 0xFF).toByte()
        buf[offset + 2] = ((val32 ushr 16) and 0xFF).toByte()
        buf[offset + 3] = ((val32 ushr 24) and 0xFF).toByte()
    }

    private fun f(x: Int, y: Int, z: Int): Int = (x and y) or (x.inv() and z)
    private fun g(x: Int, y: Int, z: Int): Int = (x and z) or (y and z.inv())
    private fun h(x: Int, y: Int, z: Int): Int = x xor y xor z
    private fun i(x: Int, y: Int, z: Int): Int = y xor (x or z.inv())

    private fun ff(a: Int, b: Int, c: Int, d: Int, x: Int, s: Int, k: Int): Int =
        rol32(a + f(b, c, d) + x + k, s) + b

    private fun gg(a: Int, b: Int, c: Int, d: Int, x: Int, s: Int, k: Int): Int =
        rol32(a + g(b, c, d) + x + k, s) + b

    private fun hh(a: Int, b: Int, c: Int, d: Int, x: Int, s: Int, k: Int): Int =
        rol32(a + h(b, c, d) + x + k, s) + b

    private fun ii(a: Int, b: Int, c: Int, d: Int, x: Int, s: Int, k: Int): Int =
        rol32(a + i(b, c, d) + x + k, s) + b
}

internal fun ByteArray.digest(): ByteArray = CycloneMd5.computeToBytes(this)