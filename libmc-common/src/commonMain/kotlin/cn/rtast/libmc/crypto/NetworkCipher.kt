/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.crypto

public interface NetworkCipher {
    public suspend fun encrypt(buffer: ByteArray, offset: Int, length: Int)
    public suspend fun decrypt(buffer: ByteArray, offset: Int, length: Int)
}