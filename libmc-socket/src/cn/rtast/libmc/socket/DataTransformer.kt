/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */


package cn.rtast.libmc.socket

public fun interface DataTransformer {
    public fun transform(buffer: ByteArray, offset: Int, length: Int)
}