/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.protocol.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.windows.BCRYPT_USE_SYSTEM_PREFERRED_RNG
import platform.windows.BCryptGenRandom

public actual fun generateRandom16Bytes(): ByteArray {
    val bytes = ByteArray(16)
    bytes.usePinned { pinned ->
        val status = BCryptGenRandom(
            hAlgorithm = null,
            pbBuffer = pinned.addressOf(0).reinterpret(),
            cbBuffer = 16u,
            dwFlags = BCRYPT_USE_SYSTEM_PREFERRED_RNG.toUInt()
        )
        if (status != 0) throw IllegalStateException("BCryptGenRandom failed with status: $status")
    }
    return bytes
}