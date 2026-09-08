/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */

package cn.rtast.libmc.protocol.crypto

import java.security.MessageDigest

internal actual fun sha1Digest(data: ByteArray): ByteArray =
    MessageDigest.getInstance("SHA-1").digest(data)