/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.packet

import cn.rtast.libmc.network.BytesBuffer

public interface Encoder<in T> {
    public fun encode(buffer: BytesBuffer, value: T)
}

public interface Decoder<out T> {
    public fun decode(buffer: BytesBuffer): T
}

public interface PacketCodec<T> : Encoder<T>, Decoder<T>

public abstract class EmptyPacketCodec<T>(private val instance: T) : PacketCodec<T> {
    override fun encode(buffer: BytesBuffer, value: T) {}
    override fun decode(buffer: BytesBuffer): T = instance
}

public fun BytesBuffer.writeBuffer(source: BytesBuffer, length: Int = source.size) {
    if (length <= 0) return
    val bytes = source.readBytes(length)
    this.writeBytes(bytes)
}