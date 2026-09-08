/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.crypto

public interface NetworkChannelCipher {
    public fun encrypt(buffer: ByteArray, offset: Int, length: Int)
    public fun decrypt(buffer: ByteArray, offset: Int, length: Int)
    public fun close()
}