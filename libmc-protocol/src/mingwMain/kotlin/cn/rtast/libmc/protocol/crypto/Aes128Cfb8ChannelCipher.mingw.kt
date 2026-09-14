/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/15
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.protocol.crypto

import cn.rtast.cipher.aes.AES_CFB8_decrypt_buffer
import cn.rtast.cipher.aes.AES_CFB8_encrypt_buffer
import cn.rtast.cipher.aes.AES_ctx
import cn.rtast.cipher.aes.AES_init_ctx_iv
import cn.rtast.libmc.context.NetworkChannelCipher
import kotlinx.cinterop.Arena
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned

internal actual class Aes128Cfb8ChannelCipher internal actual constructor(sharedKey: ByteArray) : NetworkChannelCipher {
    private val scope = Arena()
    private val encCtx: CPointer<AES_ctx> = scope.alloc<AES_ctx>().ptr
    private val decCtx: CPointer<AES_ctx> = scope.alloc<AES_ctx>().ptr

    init {
        require(sharedKey.size == 16) { "Shared key must be 16 bytes for AES-128" }
        sharedKey.usePinned { keyPinned ->
            val keyPtr = keyPinned.addressOf(0).reinterpret<UByteVar>()
            AES_init_ctx_iv(encCtx, keyPtr, keyPtr)
            AES_init_ctx_iv(decCtx, keyPtr, keyPtr)
        }
    }

    actual override fun encrypt(buffer: ByteArray, offset: Int, length: Int) {
        if (length <= 0) return
        buffer.usePinned { pinned ->
            val dataPtr = pinned.addressOf(offset).reinterpret<UByteVar>()
            AES_CFB8_encrypt_buffer(encCtx, dataPtr, length.toULong())
        }
    }

    actual override fun decrypt(buffer: ByteArray, offset: Int, length: Int) {
        if (length <= 0) return
        buffer.usePinned { pinned ->
            val dataPtr = pinned.addressOf(offset).reinterpret<UByteVar>()
            AES_CFB8_decrypt_buffer(decCtx, dataPtr, length.toULong())
        }
    }

    actual override fun close() {
        scope.clear()
    }
}