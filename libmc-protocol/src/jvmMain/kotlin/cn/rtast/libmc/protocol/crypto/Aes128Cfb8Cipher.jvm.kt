/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package cn.rtast.libmc.protocol.crypto

import cn.rtast.libmc.crypto.NetworkChannelCipher
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

internal actual class Aes128Cfb8ChannelCipher internal actual constructor(sharedKey: ByteArray) : NetworkChannelCipher {
    private val encryptCipher = Cipher.getInstance("AES/CFB8/NoPadding").apply {
        init(Cipher.ENCRYPT_MODE, SecretKeySpec(sharedKey, "AES"), IvParameterSpec(sharedKey))
    }

    private val decryptCipher = Cipher.getInstance("AES/CFB8/NoPadding").apply {
        init(Cipher.DECRYPT_MODE, SecretKeySpec(sharedKey, "AES"), IvParameterSpec(sharedKey))
    }

    actual override fun encrypt(buffer: ByteArray, offset: Int, length: Int) {
        encryptCipher.update(buffer, offset, length, buffer, offset)
    }

    actual override fun decrypt(buffer: ByteArray, offset: Int, length: Int) {
        decryptCipher.update(buffer, offset, length, buffer, offset)
    }

    actual override fun close() {}
}