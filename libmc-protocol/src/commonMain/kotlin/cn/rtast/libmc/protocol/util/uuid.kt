/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.util

import kotlin.uuid.Uuid


/**
 * translate from PHP code
 * ref: https://gist.github.com/TuxCoding/2b6a00a8fc21fd3b88375f03c9e2e603
 */
public fun generateOfflineUuid(username: String): Uuid {
    val data = "OfflinePlayer:$username".encodeToByteArray().digest()
    data[6] = ((data[6].toInt() and 0x0F) or 0x30).toByte()
    data[8] = ((data[8].toInt() and 0x3F) or 0x80).toByte()
    val hexChars = "0123456789abcdef"
    val sb = StringBuilder(36)
    for (i in 0 until 16) {
        if (i == 4 || i == 6 || i == 8 || i == 10) sb.append('-')
        val v = data[i].toInt() and 0xFF
        sb.append(hexChars[v ushr 4])
        sb.append(hexChars[v and 0x0F])
    }
    return Uuid.parse(sb.toString())
}