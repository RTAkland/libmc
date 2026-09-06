/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.util

import java.security.SecureRandom

private val secureRandom = SecureRandom()

public actual fun generateRandom16Bytes(): ByteArray {
    val bytes = ByteArray(16)
    secureRandom.nextBytes(bytes)
    return bytes
}