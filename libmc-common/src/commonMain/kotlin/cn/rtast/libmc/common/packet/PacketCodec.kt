/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.common.packet

import cn.rtast.libmc.common.stream.BytesBuffer

public interface Encoder<in T> {
    public suspend fun encode(buffer: BytesBuffer, value: T)
}

public interface Decoder<out T> {
    public suspend fun decode(buffer: BytesBuffer): T
}

public interface PacketCodec<T> : Encoder<T>, Decoder<T>

public suspend fun BytesBuffer.writeBuffer(source: BytesBuffer, length: Long = source.remaining) {
    if (length <= 0) return
    val bytes = source.readBytes(length.toInt())
    this.writeBytes(bytes)
}