/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package cn.rtast.libmc.protocol.crypto

import cn.rtast.libmc.context.NetworkChannelCipher

internal expect class Aes128Cfb8ChannelCipher internal constructor(sharedKey: ByteArray) : NetworkChannelCipher {
    override fun encrypt(buffer: ByteArray, offset: Int, length: Int)
    override fun decrypt(buffer: ByteArray, offset: Int, length: Int)
    override fun close()
}