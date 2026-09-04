/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.common

public interface Encoder<in T> {
    public fun encode(buffer: _Buffer, value: T)

    public fun encodeToByteArray(value: T): ByteArray {
        val buf = _Buffer()
        encode(buf, value)
        return buf.toByteArray()
    }
}

public interface Decoder<out T> {
    public fun decode(buffer: _Buffer): T

    public fun decodeFromByteArray(bytes: ByteArray): T = decode(bytes.wrap())
}

public interface PacketCodec<T> : Encoder<T>, Decoder<T>

@Suppress("NOTHING_TO_INLINE")
public inline fun <T> _Buffer.write(value: T, encoder: Encoder<T>): Unit = encoder.encode(this, value)

@Suppress("NOTHING_TO_INLINE")
public inline fun <T> _Buffer.read(decoder: Decoder<T>): T = decoder.decode(this)

public fun _Buffer.writeBuffer(source: _Buffer, length: Long = source.remaining) {
    if (length <= 0) return
    val bytes = source.readBytes(length.toInt())
    this.writeBytes(bytes)
}