/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.socket

import kotlinx.cinterop.*
import platform.linux.inet_ntop
import platform.posix.INET6_ADDRSTRLEN

internal actual fun formatSockAddrToIp(family: Int, addrPtr: CPointer<*>): String? = memScoped {
    val ipBuffer = allocArray<ByteVar>(INET6_ADDRSTRLEN)
    val res = inet_ntop(family, addrPtr, ipBuffer, INET6_ADDRSTRLEN.toUInt())
    return if (res != null) ipBuffer.toKString() else null
}