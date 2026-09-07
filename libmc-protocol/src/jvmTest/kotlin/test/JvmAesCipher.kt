/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package test

import cn.rtast.libmc.crypto.NetworkCipher
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class JvmAesCipher(sharedKey: ByteArray) : NetworkCipher {
    private val encryptCipher = Cipher.getInstance("AES/CFB8/NoPadding").apply {
        init(Cipher.ENCRYPT_MODE, SecretKeySpec(sharedKey, "AES"), IvParameterSpec(sharedKey))
    }

    private val decryptCipher = Cipher.getInstance("AES/CFB8/NoPadding").apply {
        init(Cipher.DECRYPT_MODE, SecretKeySpec(sharedKey, "AES"), IvParameterSpec(sharedKey))
    }

    override suspend fun encrypt(buffer: ByteArray, offset: Int, length: Int) {
        encryptCipher.update(buffer, offset, length, buffer, offset)
    }

    override suspend fun decrypt(buffer: ByteArray, offset: Int, length: Int) {
        decryptCipher.update(buffer, offset, length, buffer, offset)
    }
}