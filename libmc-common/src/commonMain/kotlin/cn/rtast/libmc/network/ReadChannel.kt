/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.network

public interface ReadChannel {
    public suspend fun readByte(): Byte
    public suspend fun readBytes(length: Int): ByteArray
    public suspend fun readFully(out: ByteArray, start: Int = 0, end: Int = out.size)
}