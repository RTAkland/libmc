/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.network

public interface WriteChannel {
    public suspend fun writeFully(value: ByteArray, startIndex: Int = 0, endIndex: Int = value.size)
    public suspend fun flush()
}