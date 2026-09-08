/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package cn.rtast.libmc.protocol.crypto

import kotlin.random.Random

internal actual fun rsaEncrypt(publicKeyBytes: ByteArray, data: ByteArray): ByteArray {
    val (modulus, exponent) = parseRsaPublicKeyDer(publicKeyBytes)
    val paddedData = pkcs1Pad(data, keySizeBytes = 128)
    return rsa1024(paddedData, exponent, modulus)
}

private fun parseRsaPublicKeyDer(der: ByteArray): Pair<ByteArray, ByteArray> {
    var offset = 0
    fun readTag(): Int = der[offset++].toInt() and 0xFF
    fun readLength(): Int {
        var len = der[offset++].toInt() and 0xFF
        if (len and 0x80 != 0) {
            val numBytes = len and 0x7F; len = 0
            repeat(numBytes) { len = (len shl 8) or (der[offset++].toInt() and 0xFF) }
        }
        return len
    }
    if (readTag() != 0x30) error("Invalid DER Format")
    readLength()
    val tag = readTag()
    if (tag == 0x30) {
        val algLen = readLength()
        offset += algLen
    } else offset--
    if (readTag() == 0x03) {
        readLength(); offset++
        if (readTag() != 0x30) error("Invalid RSA Public Key")
        readLength()
    }
    if (readTag() != 0x02) error("Modulus Required")
    val modulusLen = readLength()
    val modulus = der.copyOfRange(offset, offset + modulusLen)
    offset += modulusLen
    if (readTag() != 0x02) error("Exponent Required")
    val exponentLen = readLength()
    val exponent = der.copyOfRange(offset, offset + exponentLen)
    return Pair(modulus, exponent)
}

private fun pkcs1Pad(data: ByteArray, keySizeBytes: Int): ByteArray {
    val maxDataLen = keySizeBytes - 11
    require(data.size <= maxDataLen)
    val padded = ByteArray(keySizeBytes)
    padded[0] = 0x00
    padded[1] = 0x02
    val psLen = keySizeBytes - data.size - 3
    var i = 2
    while (i < 2 + psLen) {
        val randomByte = Random.nextInt(1, 256).toByte(); padded[i] = randomByte; i++
    }
    padded[i] = 0x00; i++
    for (k in data.indices) padded[i + k] = data[k]
    return padded
}

private fun rsa1024(input: ByteArray, exponent: ByteArray, modulus: ByteArray): ByteArray {
    val dataLongs = bytesToLongArray16(input)
    val expoLongs = bytesToLongArray16(exponent)
    val modLongs = bytesToLongArray16(modulus)
    val resLongs = LongArray(18)
    rsa1024(resLongs, dataLongs, expoLongs, modLongs)
    return longArray16ToBytes(resLongs)
}

private fun rsa1024(res: LongArray, data: LongArray, expo: LongArray, key: LongArray): Boolean {
    val modData = LongArray(18)
    val result = LongArray(18)
    var tempExpo: Long
    modBigNumber(modData, data, key, 16)
    result[0] = 1L
    val expoLen = bitLength(expo, 16) / 64
    for (i in 0..expoLen) {
        tempExpo = expo[i]
        repeat(64) {
            if ((tempExpo and 1L) != 0L) modMultiply1024(result, result, modData, key)
            modMultiply1024(modData, modData, modData, key)
            tempExpo = tempExpo ushr 1
        }
    }

    for (i in 0 until 16) res[i] = result[i]
    return true
}

private fun addBigNumber(res: LongArray, op1: LongArray, op2: LongArray, n: Int): Boolean {
    var carry = 0L
    val mask32 = 0xFFFFFFFFL
    var i = 0
    while (i < n) {
        val j = (op1[i] and mask32) + (op2[i] and mask32) + carry
        val k = (op1[i] ushr 32) + (op2[i] ushr 32) + (j ushr 32)
        carry = k ushr 32
        res[i] = ((k and mask32) shl 32) or (j and mask32)
        i++
    }
    if (i < res.size) res[i] = carry
    return false
}

private fun multBigNumber(res: LongArray, op1: LongArray, op2: Int, n: Int): Boolean {
    var carry1: Long
    var carry2 = 0L
    val op2UL = op2.toLong() and 0xFFFFFFFFL
    val mask32 = 0xFFFFFFFFL
    var i = 0
    while (i < n) {
        var j = (op1[i] and mask32) * op2UL
        var k = (op1[i] ushr 32) * op2UL
        carry1 = k ushr 32
        k = (k and mask32) + (j ushr 32)
        j = (j and mask32) + carry2
        k += (j ushr 32)
        carry2 = carry1 + (k ushr 32)
        res[i] = ((k and mask32) shl 32) or (j and mask32)
        i++
    }
    if (i < res.size) res[i] = carry2
    return false
}

private fun modMultiply1024(res: LongArray, op1: LongArray, op2: LongArray, mod: LongArray): Boolean {
    val mult1 = LongArray(33)
    val mult2 = LongArray(33)
    val result = LongArray(33)
    val xmod = LongArray(33)
    for (i in 0 until 16) xmod[i] = mod[i]
    for (i in 0 until 16) {
        mult1.fill(0L)
        mult2.fill(0L)
        val op2Low = (op2[i] and 0xFFFFFFFFL).toInt()
        val op2High = ((op2[i] ushr 32) and 0xFFFFFFFFL).toInt()
        multBigNumber(mult1, op1, op2Low, 16)
        multBigNumber(mult2, op1, op2High, 16)
        slnBigNumber(mult2, mult2, 33, 32)
        addBigNumber(mult2, mult2, mult1, 32)
        slnBigNumber(mult2, mult2, 33, 64 * i)
        addBigNumber(result, result, mult2, 32)
    }
    modBigNumber(result, result, xmod, 33)
    for (i in 0 until 16) res[i] = result[i]
    return false
}

private fun modBigNumber(res: LongArray, op1: LongArray, op2: LongArray, n: Int): Boolean {
    val lenOp1 = bitLength(op1, n)
    val lenOp2 = bitLength(op2, n)
    val lenDif = lenOp1 - lenOp2
    for (i in 0 until n) res[i] = op1[i]
    if (lenDif < 0) return true
    if (lenDif == 0) {
        while (compare(res, op2, n) >= 0) subBigNumber(res, res, op2, n)
        return true
    }
    val op2Work = op2.copyOf()
    slnBigNumber(op2Work, op2Work, n, lenDif)
    repeat(lenDif) {
        srnBigNumber(op2Work, op2Work, n, 1)
        while (compare(res, op2Work, n) >= 0) subBigNumber(res, res, op2Work, n)
    }
    return true
}

private fun compare(op1: LongArray, op2: LongArray, n: Int): Int {
    for (i in n - 1 downTo 0) {
        val a = op1[i]
        val b = op2[i]
        if (a != b) {
            val aUnsigned = a xor Long.MIN_VALUE
            val bUnsigned = b xor Long.MIN_VALUE
            return if (aUnsigned > bUnsigned) 1 else -1
        }
    }
    return 0
}

private fun subBigNumber(res: LongArray, op1: LongArray, op2: LongArray, n: Int): Boolean {
    var carry = false
    val op1Copy = op1.copyOf()
    for (i in 0 until n) {
        var v1 = op1Copy[i]
        if (carry) {
            if (v1 != 0L) carry = false
            v1 -= 1L
            op1Copy[i] = v1
        }
        if ((v1 xor Long.MIN_VALUE) < (op2[i] xor Long.MIN_VALUE)) carry = true
        res[i] = v1 - op2[i]
    }
    return carry
}

private fun slnBigNumber(res: LongArray, op: LongArray, len: Int, n: Int): Boolean {
    val xShift = n / 64
    val yShift = n % 64
    var i = len
    while (i - xShift > 0) {
        res[i - 1] = op[i - 1 - xShift]
        i--
    }
    while (i > 0) {
        res[i - 1] = 0L
        i--
    }
    if (yShift == 0) return true
    var carry = 0L
    for (idx in 0 until len) {
        val j = res[idx]
        val nextCarry = j ushr (64 - yShift)
        res[idx] = (j shl yShift) or carry
        carry = nextCarry
    }
    return true
}

private fun srnBigNumber(res: LongArray, op: LongArray, len: Int, n: Int): Boolean {
    val xShift = n / 64
    val yShift = n % 64
    var i = 0
    while (i + xShift < len) {
        res[i] = op[i + xShift]; i++
    }
    while (i < len) {
        res[i] = 0L; i++
    }
    if (yShift == 0) return true

    var carry = 0L
    for (idx in len downTo 1) {
        val j = res[idx - 1]
        val nextCarry = j shl (64 - yShift)
        res[idx - 1] = (j ushr yShift) or carry
        carry = nextCarry
    }
    return true
}

private fun bitLength(op: LongArray, n: Int): Int {
    var len = 0
    val unit = 1L
    for (idx in n downTo 1) {
        if (op[idx - 1] == 0L) continue
        for (i in 64 downTo 1) {
            if ((op[idx - 1] and (unit shl (i - 1))) != 0L) {
                len = (64 * (idx - 1)) + i
                break
            }
        }
        if (len != 0) break
    }
    return len
}


private fun bytesToLongArray16(bytes: ByteArray): LongArray {
    val cleanBytes = if (bytes.size > 128 && bytes[0] == 0.toByte()) bytes.copyOfRange(1, bytes.size) else bytes
    val padded = ByteArray(128)
    val startIdx = 128 - cleanBytes.size
    for (k in cleanBytes.indices) padded[startIdx + k] = cleanBytes[k]
    for (k in 0 until 64) {
        val tmp = padded[k]
        padded[k] = padded[127 - k]
        padded[127 - k] = tmp
    }
    val result = LongArray(16)
    for (i in 0 until 16) {
        var value = 0L
        for (j in 0 until 8) {
            val byteVal = padded[i * 8 + j].toLong() and 0xFFL
            value = value or (byteVal shl (j * 8))
        }
        result[i] = value
    }
    return result
}

private fun longArray16ToBytes(array: LongArray): ByteArray {
    val bytes = ByteArray(128)
    for (i in 0 until 16) {
        val value = array[i]
        for (j in 0 until 8) bytes[i * 8 + j] = ((value ushr (j * 8)) and 0xFFL).toByte()
    }
    for (k in 0 until 64) {
        val tmp = bytes[k]
        bytes[k] = bytes[127 - k]
        bytes[127 - k] = tmp
    }
    return bytes
}