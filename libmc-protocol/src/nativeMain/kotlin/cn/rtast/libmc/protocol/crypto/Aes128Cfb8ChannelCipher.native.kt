/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package cn.rtast.libmc.protocol.crypto

import cn.rtast.libmc.context.NetworkChannelCipher

internal actual class Aes128Cfb8ChannelCipher internal actual constructor(sharedKey: ByteArray) : NetworkChannelCipher {
    private val encryptor = Aes128Cfb8(sharedKey, sharedKey)
    private val decryptor = Aes128Cfb8(sharedKey, sharedKey)

    actual override fun encrypt(buffer: ByteArray, offset: Int, length: Int) {
        val stream = ByteArray(Aes128Cfb8.BLOCK_SIZE)
        for (i in offset until (offset + length)) {
            encryptor.getIv(stream)
            encryptor.encryptBlock(stream, stream)
            val ciphertext = (buffer[i].toInt() and 0xff xor stream[0].toInt() and 0xff).toByte()
            encryptor.shiftFeedback(ciphertext)
            buffer[i] = ciphertext
        }
    }

    actual override fun decrypt(buffer: ByteArray, offset: Int, length: Int) {
        val stream = ByteArray(Aes128Cfb8.BLOCK_SIZE)
        for (i in offset until (offset + length)) {
            val ciphertext = buffer[i]
            decryptor.getIv(stream)
            decryptor.encryptBlock(stream, stream)
            buffer[i] = (ciphertext.toInt() and 0xff xor stream[0].toInt() and 0xff).toByte()
            decryptor.shiftFeedback(ciphertext)
        }
    }

    actual override fun close() {}
}

/**
 * PERFORMANCE IMPROVEMENT REQUIRED.
 */
private class Aes128Cfb8(key: ByteArray, iv: ByteArray) {
    companion object {
        const val BLOCK_SIZE = 16
        private val S_BOX = byteArrayOf(
            0x63, 0x7c, 0x77, 0x7b, 0xf2.toByte(), 0x6b, 0x6f, 0xc5.toByte(),
            0x30, 0x01, 0x67, 0x2b, 0xfe.toByte(), 0xd7.toByte(), 0xab.toByte(), 0x76,
            0xca.toByte(), 0x82.toByte(), 0xc9.toByte(), 0x7d, 0xfa.toByte(), 0x59,
            0x47, 0xf0.toByte(), 0xad.toByte(), 0xd4.toByte(), 0xa2.toByte(),
            0xaf.toByte(), 0x9c.toByte(), 0xa4.toByte(), 0x72,
            0xc0.toByte(), 0xb7.toByte(), 0xfd.toByte(), 0x93.toByte(), 0x26,
            0x36, 0x3f, 0xf7.toByte(), 0xcc.toByte(), 0x34, 0xa5.toByte(),
            0xe5.toByte(), 0xf1.toByte(), 0x71, 0xd8.toByte(), 0x31, 0x15,
            0x04, 0xc7.toByte(), 0x23, 0xc3.toByte(), 0x18, 0x96.toByte(),
            0x05, 0x9a.toByte(), 0x07, 0x12, 0x80.toByte(), 0xe2.toByte(),
            0xeb.toByte(), 0x27, 0xb2.toByte(), 0x75,
            0x09, 0x83.toByte(), 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0.toByte(),
            0x52, 0x3b, 0xd6.toByte(), 0xb3.toByte(), 0x29, 0xe3.toByte(),
            0x2f, 0x84.toByte(), 0x53, 0xd1.toByte(), 0x00, 0xed.toByte(),
            0x20, 0xfc.toByte(), 0xb1.toByte(), 0x5b, 0x6a, 0xcb.toByte(),
            0xbe.toByte(), 0x39, 0x4a, 0x4c, 0x58, 0xcf.toByte(),
            0xd0.toByte(), 0xef.toByte(), 0xaa.toByte(), 0xfb.toByte(),
            0x43, 0x4d, 0x33, 0x85.toByte(), 0x45, 0xf9.toByte(),
            0x02, 0x7f, 0x50, 0x3c, 0x9f.toByte(), 0xa8.toByte(),
            0x51, 0xa3.toByte(), 0x40, 0x8f.toByte(), 0x92.toByte(),
            0x9d.toByte(), 0x38, 0xf5.toByte(), 0xbc.toByte(),
            0xb6.toByte(), 0xda.toByte(), 0x21, 0x10, 0xff.toByte(),
            0xf3.toByte(), 0xd2.toByte(), 0xcd.toByte(), 0x0c,
            0x13, 0xec.toByte(), 0x5f, 0x97.toByte(), 0x44, 0x17,
            0xc4.toByte(), 0xa7.toByte(), 0x7e, 0x3d, 0x64, 0x5d,
            0x19, 0x73, 0x60, 0x81.toByte(), 0x4f, 0xdc.toByte(),
            0x22, 0x2a, 0x90.toByte(), 0x88.toByte(), 0x46, 0xee.toByte(),
            0xb8.toByte(), 0x14, 0xde.toByte(), 0x5e, 0x0b,
            0xdb.toByte(), 0xe0.toByte(), 0x32, 0x3a, 0x0a, 0x49,
            0x06, 0x24, 0x5c, 0xc2.toByte(), 0xd3.toByte(),
            0xac.toByte(), 0x62, 0x91.toByte(), 0x95.toByte(), 0xe4.toByte(),
            0x79, 0xe7.toByte(), 0xc8.toByte(), 0x37, 0x6d,
            0x8d.toByte(), 0xd5.toByte(), 0x4e, 0xa9.toByte(), 0x6c,
            0x56, 0xf4.toByte(), 0xea.toByte(), 0x65, 0x7a,
            0xae.toByte(), 0x08, 0xba.toByte(), 0x78, 0x25,
            0x2e, 0x1c, 0xa6.toByte(), 0xb4.toByte(), 0xc6.toByte(),
            0xe8.toByte(), 0xdd.toByte(), 0x74, 0x1f, 0x4b,
            0xbd.toByte(), 0x8b.toByte(), 0x8a.toByte(), 0x70,
            0x3e, 0xb5.toByte(), 0x66, 0x48, 0x03, 0xf6.toByte(),
            0x0e, 0x61, 0x35, 0x57, 0xb9.toByte(), 0x86.toByte(),
            0xc1.toByte(), 0x1d, 0x9e.toByte(), 0xe1.toByte(),
            0xf8.toByte(), 0x98.toByte(), 0x11, 0x69, 0xd9.toByte(),
            0x8e.toByte(), 0x94.toByte(), 0x9b.toByte(), 0x1e,
            0x87.toByte(), 0xe9.toByte(), 0xce.toByte(), 0x55,
            0x28, 0xdf.toByte(), 0x8c.toByte(), 0xa1.toByte(),
            0x89.toByte(), 0x0d, 0xbf.toByte(), 0xe6.toByte(),
            0x42, 0x68, 0x41, 0x99.toByte(), 0x2d, 0x0f,
            0xb0.toByte(), 0x54, 0xbb.toByte(), 0x16
        )

        private val INV_S_BOX = ByteArray(256).also { inverse ->
            for (i in 0 until 256) inverse[S_BOX[i].toInt() and 0xff] = i.toByte()
        }

        private val RCON = byteArrayOf(
            0x00, 0x01, 0x02, 0x04, 0x08, 0x10,
            0x20, 0x40, 0x80.toByte(), 0x1b, 0x36, 0x6c, 0xd8.toByte(),
            0xab.toByte(), 0x4d, 0x9a.toByte()
        )

        private fun Byte.u(): Int = toInt() and 0xff
        private fun sBox(value: Byte): Byte = S_BOX[value.u()]
        private fun invSBox(value: Byte): Byte = INV_S_BOX[value.u()]
        private fun xtime(value: Byte): Byte {
            val x = value.u()
            return (((x shl 1) xor (((x ushr 7) and 1) * 0x1b)) and 0xff).toByte()
        }

        private fun multiply(x: Byte, y: Int): Byte {
            var a = x.u()
            var b = y
            var result = 0
            while (b != 0) {
                if ((b and 1) != 0) result = result xor a
                a = if ((a and 0x80) != 0) ((a shl 1) xor 0x1b) and 0xff else (a shl 1) and 0xff
                b = b ushr 1
            }
            return result.toByte()
        }
    }

    private val rounds: Int
    private val roundKey: ByteArray
    private val iv = ByteArray(BLOCK_SIZE)
    private val ctrBuffer = ByteArray(BLOCK_SIZE)
    private var ctrPosition = BLOCK_SIZE

    init {
        iv.copyInto(this.iv)
        val nk = key.size / 4
        rounds = nk + 6
        roundKey = ByteArray(BLOCK_SIZE * (rounds + 1))
        expandKey(key, nk, rounds, roundKey)
    }

    fun setIv(newIv: ByteArray) {
        newIv.copyInto(iv)
        ctrPosition = BLOCK_SIZE
    }

    fun encryptBlock(input: ByteArray, output: ByteArray = ByteArray(BLOCK_SIZE)) {
        input.copyInto(output, 0, 0, BLOCK_SIZE)
        cipher(output)
    }

    fun decryptBlock(input: ByteArray, output: ByteArray = ByteArray(BLOCK_SIZE)) {
        input.copyInto(output, 0, 0, BLOCK_SIZE)
        invCipher(output)
    }

    private fun cipher(state: ByteArray) {
        addRoundKey(state, 0)
        for (round in 1 until rounds) {
            subBytes(state)
            shiftRows(state)
            mixColumns(state)
            addRoundKey(state, round)
        }
        subBytes(state)
        shiftRows(state)
        addRoundKey(state, rounds)
    }

    private fun invCipher(state: ByteArray) {
        addRoundKey(state, rounds)
        for (round in rounds - 1 downTo 1) {
            invShiftRows(state)
            invSubBytes(state)
            addRoundKey(state, round)
            invMixColumns(state)
        }
        invShiftRows(state)
        invSubBytes(state)
        addRoundKey(state, 0)
    }

    private fun addRoundKey(state: ByteArray, round: Int) {
        val offset = round * BLOCK_SIZE
        for (i in 0 until BLOCK_SIZE) state[i] = (state[i].u() xor roundKey[offset + i].u()).toByte()
    }

    private fun subBytes(state: ByteArray) = run { for (i in 0 until BLOCK_SIZE) state[i] = sBox(state[i]) }
    private fun invSubBytes(state: ByteArray) = run { for (i in 0 until BLOCK_SIZE) state[i] = invSBox(state[i]) }
    private fun shiftRows(state: ByteArray) {
        var tmp = state[1]
        state[1] = state[5]
        state[5] = state[9]
        state[9] = state[13]
        state[13] = tmp
        tmp = state[2]
        state[2] = state[10]
        state[10] = tmp
        tmp = state[6]
        state[6] = state[14]
        state[14] = tmp
        tmp = state[3]
        state[3] = state[15]
        state[15] = state[11]
        state[11] = state[7]
        state[7] = tmp
    }

    private fun invShiftRows(state: ByteArray) {
        var tmp = state[13]
        state[13] = state[9]
        state[9] = state[5]
        state[5] = state[1]
        state[1] = tmp
        tmp = state[2]
        state[2] = state[10]
        state[10] = tmp
        tmp = state[6]
        state[6] = state[14]
        state[14] = tmp
        tmp = state[3]
        state[3] = state[7]
        state[7] = state[11]
        state[11] = state[15]
        state[15] = tmp
    }

    private fun mixColumns(state: ByteArray) {
        for (column in 0 until 4) {
            val i = column * 4
            val a0 = state[i]
            val a1 = state[i + 1]
            val a2 = state[i + 2]
            val a3 = state[i + 3]
            val t = a0.u() xor a1.u() xor a2.u() xor a3.u()
            state[i] = (a0.u() xor (xtime((a0.u() xor a1.u()).toByte()).u()) xor t).toByte()
            state[i + 1] = (a1.u() xor (xtime((a1.u() xor a2.u()).toByte()).u()) xor t).toByte()
            state[i + 2] = (a2.u() xor (xtime((a2.u() xor a3.u()).toByte()).u()) xor t).toByte()
            state[i + 3] = (a3.u() xor (xtime((a3.u() xor a0.u()).toByte()).u()) xor t).toByte()
        }
    }

    private fun invMixColumns(state: ByteArray) {
        for (column in 0 until 4) {
            val i = column * 4
            val a = state[i]
            val b = state[i + 1]
            val c = state[i + 2]
            val d = state[i + 3]
            state[i] = (multiply(a, 0x0e).u() xor multiply(b, 0x0b).u()
                    xor multiply(c, 0x0d).u() xor multiply(d, 0x09).u()).toByte()
            state[i + 1] = (multiply(a, 0x09).u() xor multiply(b, 0x0e).u()
                    xor multiply(c, 0x0b).u() xor multiply(d, 0x0d).u()).toByte()
            state[i + 2] = (multiply(a, 0x0d).u() xor multiply(b, 0x09).u()
                    xor multiply(c, 0x0e).u() xor multiply(d, 0x0b).u()).toByte()
            state[i + 3] = (multiply(a, 0x0b).u() xor multiply(b, 0x0d).u()
                    xor multiply(c, 0x09).u() xor multiply(d, 0x0e).u()).toByte()
        }
    }

    fun ecbEncrypt(data: ByteArray): ByteArray {
        val output = data.copyOf()
        for (offset in output.indices step BLOCK_SIZE) cipherAt(output, offset)
        return output
    }

    fun ecbDecrypt(data: ByteArray): ByteArray {
        val output = data.copyOf()
        for (offset in output.indices step BLOCK_SIZE) invCipherAt(output, offset)
        return output
    }

    private fun cipherAt(data: ByteArray, offset: Int) {
        val block = ByteArray(BLOCK_SIZE)
        data.copyInto(block, 0, offset, offset + BLOCK_SIZE)
        cipher(block)
        block.copyInto(data, offset)
    }

    private fun invCipherAt(data: ByteArray, offset: Int) {
        val block = ByteArray(BLOCK_SIZE)
        data.copyInto(block, 0, offset, offset + BLOCK_SIZE)
        invCipher(block)
        block.copyInto(data, offset)
    }

    fun cbcEncrypt(data: ByteArray): ByteArray {
        val output = data.copyOf()
        val block = ByteArray(BLOCK_SIZE)
        for (offset in output.indices step BLOCK_SIZE) {
            for (i in 0 until BLOCK_SIZE) block[i] = (output[offset + i].u() xor iv[i].u()).toByte()
            cipher(block)
            block.copyInto(output, offset)
            block.copyInto(iv)
        }
        return output
    }

    fun cbcDecrypt(data: ByteArray): ByteArray {
        val output = data.copyOf()
        val block = ByteArray(BLOCK_SIZE)
        val nextIv = ByteArray(BLOCK_SIZE)
        for (offset in output.indices step BLOCK_SIZE) {
            output.copyInto(nextIv, 0, offset, offset + BLOCK_SIZE)
            output.copyInto(block, 0, offset, offset + BLOCK_SIZE)
            invCipher(block)
            for (i in 0 until BLOCK_SIZE) block[i] = (block[i].u() xor iv[i].u()).toByte()
            block.copyInto(output, offset)
            nextIv.copyInto(iv)
        }
        return output
    }

    fun ctrXcrypt(data: ByteArray): ByteArray {
        val output = data.copyOf()
        for (i in output.indices) {
            if (ctrPosition == BLOCK_SIZE) {
                iv.copyInto(ctrBuffer)
                cipher(ctrBuffer)
                incrementCounter()
                ctrPosition = 0
            }
            output[i] = (output[i].u() xor ctrBuffer[ctrPosition].u()).toByte()
            ctrPosition++
        }
        return output
    }

    private fun incrementCounter() {
        for (i in BLOCK_SIZE - 1 downTo 0) {
            if (iv[i].u() == 0xff) iv[i] = 0 else {
                iv[i] = (iv[i].u() + 1).toByte()
                break
            }
        }
    }

    fun cfb8Encrypt(data: ByteArray): ByteArray {
        val output = data.copyOf()
        val stream = ByteArray(BLOCK_SIZE)
        for (i in output.indices) {
            iv.copyInto(stream)
            cipher(stream)
            val ciphertext = (output[i].u() xor stream[0].u()).toByte()
            shiftFeedback(ciphertext)
            output[i] = ciphertext
        }
        return output
    }

    fun cfb8Decrypt(data: ByteArray): ByteArray {
        val output = data.copyOf()
        val stream = ByteArray(BLOCK_SIZE)
        for (i in output.indices) {
            val ciphertext = output[i]
            iv.copyInto(stream)
            cipher(stream)
            output[i] = (ciphertext.u() xor stream[0].u()).toByte()
            shiftFeedback(ciphertext)
        }
        return output
    }

    fun shiftFeedback(value: Byte) {
        for (i in 0 until BLOCK_SIZE - 1) iv[i] = iv[i + 1]
        iv[BLOCK_SIZE - 1] = value
    }

    private fun expandKey(key: ByteArray, nk: Int, rounds: Int, output: ByteArray) {
        key.copyInto(output)
        var generated = key.size
        var rconIndex = 1
        val total = BLOCK_SIZE * (rounds + 1)
        val temp = ByteArray(4)
        while (generated < total) {
            for (i in 0 until 4) temp[i] = output[generated - 4 + i]
            if (generated % key.size == 0) {
                val t = temp[0]
                temp[0] = temp[1]
                temp[1] = temp[2]
                temp[2] = temp[3]
                temp[3] = t
                for (i in 0 until 4) temp[i] = sBox(temp[i])
                temp[0] = (temp[0].u() xor RCON[rconIndex].u()).toByte()
                rconIndex++
            } else if (nk == 8 && generated % key.size == 16) for (i in 0 until 4) temp[i] = sBox(temp[i])
            for (i in 0 until 4) {
                output[generated] = (output[generated - key.size].u() xor temp[i].u()).toByte()
                generated++
            }
        }
    }

    fun getIv(output: ByteArray): ByteArray = iv.copyInto(output)
}
