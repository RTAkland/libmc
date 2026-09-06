/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.protocol.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.arc4random_buf

public actual fun generateRandom16Bytes(): ByteArray {
    val bytes = ByteArray(16)
    bytes.usePinned { pinned -> arc4random_buf(pinned.addressOf(0), 16.toULong()) }
    return bytes
}