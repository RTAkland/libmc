/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 9/15/26
 */


package test

import cn.rtast.libmc.protocol.crypto.Aes128Cfb8ChannelCipher
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

class Aes128Cfb8ChannelCipherTest {
    private val testKey = ByteArray(16) { (it + 1).toByte() }

    @Test
    fun testEncryptAndDecrypt() {
        val cipher = Aes128Cfb8ChannelCipher(testKey)
        val originalText = "test origin text123456".encodeToByteArray()
        val buffer = originalText.copyOf()
        cipher.encrypt(buffer, 0, buffer.size)
        check(!originalText.contentEquals(buffer)) { "encrypted should not match original" }
        cipher.decrypt(buffer, 0, buffer.size)
        assertContentEquals(originalText, buffer, "decrypted should match original")
        cipher.close()
    }

    @Test
    fun testPartialBufferEncryption() {
        val cipher = Aes128Cfb8ChannelCipher(testKey)
        val prefix = byteArrayOf(0x01, 0x02)
        val plainText = "test partial buffer test123456".encodeToByteArray()
        val suffix = byteArrayOf(0x03, 0x04)
        val buffer = prefix + plainText + suffix
        val offset = prefix.size
        val length = plainText.size
        cipher.encrypt(buffer, offset, length)
        assertContentEquals(prefix, buffer.copyOfRange(0, offset))
        assertContentEquals(suffix, buffer.copyOfRange(offset + length, buffer.size))
        cipher.decrypt(buffer, offset, length)
        val decryptedSegment = buffer.copyOfRange(offset, offset + length)
        assertContentEquals(plainText, decryptedSegment)
        cipher.close()
    }

    @Test
    fun testStreamContinuousState() {
        val encCipher = Aes128Cfb8ChannelCipher(testKey)
        val decCipher = Aes128Cfb8ChannelCipher(testKey)
        val fullData = Random.nextBytes(1024)
        val encryptedData = fullData.copyOf()
        val chunkSize = 256
        for (i in fullData.indices step chunkSize) encCipher.encrypt(encryptedData, i, chunkSize)
        val decryptedData = encryptedData.copyOf()
        decCipher.decrypt(decryptedData, 0, 512)
        decCipher.decrypt(decryptedData, 512, 512)
        assertContentEquals(fullData, decryptedData, "stream decryption failed")
        encCipher.close()
        decCipher.close()
    }

    @Test
    fun testInvalidKeySizeThrowsException() {
        val invalidKeyShort = ByteArray(15)
        val invalidKeyLong = ByteArray(32)
        assertFailsWith<IllegalArgumentException> {
            Aes128Cfb8ChannelCipher(invalidKeyShort)
        }
        assertFailsWith<IllegalArgumentException> {
            Aes128Cfb8ChannelCipher(invalidKeyLong)
        }
    }
}