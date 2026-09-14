/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/15
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.protocol.crypto

import cn.rtast.libmc.context.NetworkChannelCipher
import kotlinx.cinterop.*
import platform.CoreCrypto.*

internal actual class Aes128Cfb8ChannelCipher internal actual constructor(sharedKey: ByteArray) : NetworkChannelCipher {
    private val encCryptor: CCCryptorRef
    private val decCryptor: CCCryptorRef

    init {
        require(sharedKey.size == 16) { "Shared key must be 16 bytes" }
        encCryptor = createCryptor(kCCEncrypt, sharedKey)
        decCryptor = createCryptor(kCCDecrypt, sharedKey)
    }

    private fun createCryptor(op: CCOperation, key: ByteArray): CCCryptorRef = memScoped {
        val cryptorRef = alloc<CCCryptorRefVar>()
        key.usePinned { pinnedKey ->
            val keyPtr = pinnedKey.addressOf(0)
            CCCryptorCreateWithMode(
                op, kCCModeCFB8, kCCAlgorithmAES128,
                ccNoPadding, keyPtr, keyPtr, key.size.toULong(),
                null, 0u, 0, 0u, cryptorRef.ptr
            )
        }
        return cryptorRef.value!!
    }

    actual override fun encrypt(buffer: ByteArray, offset: Int, length: Int) {
        if (length <= 0) return
        processBuffer(encCryptor, buffer, offset, length)
    }

    actual override fun decrypt(buffer: ByteArray, offset: Int, length: Int) {
        if (length <= 0) return
        processBuffer(decCryptor, buffer, offset, length)
    }

    private fun processBuffer(cryptor: CCCryptorRef, buffer: ByteArray, offset: Int, length: Int) = memScoped {
        buffer.usePinned { pinnedData ->
            val dataPtr = pinnedData.addressOf(offset)
            val movedBytes = alloc<ULongVar>()
            CCCryptorUpdate(cryptor, dataPtr, length.toULong(), dataPtr, length.toULong(), movedBytes.ptr)
        }
    }

    actual override fun close() {
        CCCryptorRelease(encCryptor)
        CCCryptorRelease(decCryptor)
    }
}