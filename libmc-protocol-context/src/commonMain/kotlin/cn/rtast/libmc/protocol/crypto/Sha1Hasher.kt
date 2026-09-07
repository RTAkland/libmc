/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


@file:OptIn(DelicateCryptographyApi::class)

package cn.rtast.libmc.protocol.crypto

import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.SHA1

public fun minecraftServerIdHash(serverId: String, secretKey: ByteArray, publicKey: ByteArray): String {
    val serverIdBytes = serverId.encodeToByteArray()
    for (b in serverIdBytes) require((b.toInt() and 0xFF) <= 0x7F) { "serverId contains non-US-ASCII character" }
    val data = serverIdBytes + secretKey + publicKey
    val hash = provider.get(SHA1).hasher().hashBlocking(data)
    return mcDigestToString(hash)
}

private fun mcDigestToString(digest: ByteArray): String {
    val isNegative = (digest[0].toInt() and 0x80) != 0
    val bytes = if (isNegative) twosComplement(digest) else digest
    var hex = bytes.joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }
    hex = hex.trimStart('0')
    if (hex.isEmpty()) hex = "0"
    return if (isNegative) "-$hex" else hex
}

private fun twosComplement(bytes: ByteArray): ByteArray {
    val result = ByteArray(bytes.size)
    var carry = 1
    for (i in bytes.size - 1 downTo 0) {
        val inverted = (bytes[i].toInt().inv() and 0xFF) + carry
        result[i] = (inverted and 0xFF).toByte()
        carry = inverted ushr 8
    }
    return result
}