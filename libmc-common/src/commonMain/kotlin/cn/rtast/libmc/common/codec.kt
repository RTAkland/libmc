/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.common

public interface Encoder<in T> {
    public fun encode(buffer: BytesBuffer, value: T)
}

public interface Decoder<out T> {
    public fun decode(buffer: BytesBuffer): T
}

public interface PacketCodec<T> : Encoder<T>, Decoder<T>

@Suppress("NOTHING_TO_INLINE")
public inline fun <T> BytesBuffer.write(value: T, encoder: Encoder<T>): Unit = encoder.encode(this, value)

@Suppress("NOTHING_TO_INLINE")
public inline fun <T> BytesBuffer.read(decoder: Decoder<T>): T = decoder.decode(this)

public fun BytesBuffer.writeBuffer(source: BytesBuffer, length: Long = source.remaining) {
    if (length <= 0) return
    val bytes = source.readBytes(length.toInt())
    this.writeBytes(bytes)
}