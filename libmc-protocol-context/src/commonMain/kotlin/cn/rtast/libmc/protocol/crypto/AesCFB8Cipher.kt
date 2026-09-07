/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


@file:OptIn(DelicateCryptographyApi::class)

package cn.rtast.libmc.protocol.crypto

import cn.rtast.libmc.crypto.NetworkCipher
import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.AES

public class AesCFB8Cipher(sharedKey: ByteArray) : NetworkCipher {
    private val encryptIv = sharedKey.copyOf()
    private val decryptIv = sharedKey.copyOf()

    private val cipher = provider.get(AES.CFB8)
        .keyDecoder()
        .decodeFromByteArrayBlocking(AES.Key.Format.RAW, sharedKey)
        .cipher()

    override fun encrypt(buffer: ByteArray, offset: Int, length: Int) {
        val plaintext = buffer.copyOfRange(offset, offset + length)
        val ciphertext = cipher.encryptWithIvBlocking(encryptIv, plaintext)
        ciphertext.copyInto(buffer, destinationOffset = offset)
        updateIv(encryptIv, ciphertext)
    }

    override fun decrypt(buffer: ByteArray, offset: Int, length: Int) {
        val ciphertext = buffer.copyOfRange(offset, offset + length)
        val plaintext = cipher.decryptWithIvBlocking(decryptIv, ciphertext)
        plaintext.copyInto(buffer, destinationOffset = offset)
        updateIv(decryptIv, ciphertext)
    }

    private fun updateIv(iv: ByteArray, ciphertext: ByteArray) {
        val len = ciphertext.size
        if (len >= iv.size) {
            ciphertext.copyInto(iv, destinationOffset = 0, startIndex = len - iv.size, endIndex = len)
        } else {
            iv.copyInto(iv, destinationOffset = 0, startIndex = len, endIndex = iv.size)
            ciphertext.copyInto(iv, destinationOffset = iv.size - len)
        }
    }
}