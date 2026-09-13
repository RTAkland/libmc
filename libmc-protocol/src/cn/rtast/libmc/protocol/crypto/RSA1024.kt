/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package cn.rtast.libmc.protocol.crypto

internal expect fun rsaEncrypt(publicKeyBytes: ByteArray, data: ByteArray): ByteArray