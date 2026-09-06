/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.protocol.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.O_RDONLY
import platform.posix.close
import platform.posix.open
import platform.posix.read

public actual fun generateRandom16Bytes(): ByteArray {
    val bytes = ByteArray(16)
    val fd = open("/dev/urandom", O_RDONLY)
    if (fd < 0) throw IllegalStateException("Failed to open /dev/urandom via POSIX open")
    try {
        bytes.usePinned { pinned ->
            val readBytes = read(fd, pinned.addressOf(0), 16u)
            if (readBytes < 16L) throw IllegalStateException("Failed to read 16 bytes, read count: $readBytes")
        }
    } finally {
        close(fd)
    }
    return bytes
}