/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping.platform

import kotlinx.io.Buffer
import kotlinx.io.readByteArray

@Suppress("CLASSNAME")
public actual class _Buffer {
    private val _delegateBuf: Buffer

    public actual constructor() {
        _delegateBuf = Buffer()
    }

    public actual constructor(bytes: ByteArray) {
        _delegateBuf = Buffer().apply { write(bytes) }
    }

    public actual fun writeByte(value: Byte): Unit = _delegateBuf.writeByte(value)
    public actual fun writeShort(value: Short): Unit = _delegateBuf.writeShort(value)
    public actual fun writeLong(value: Long): Unit = _delegateBuf.writeLong(value)
    public actual fun writeBytes(bytes: ByteArray): Unit = _delegateBuf.write(bytes)

    public actual fun readByte(): Byte = _delegateBuf.readByte()
    public actual fun readShort(): Short = _delegateBuf.readShort()
    public actual fun readLong(): Long = _delegateBuf.readLong()
    public actual fun readBytes(length: Int): ByteArray = _delegateBuf.readByteArray(length)

    public actual fun toByteArray(): ByteArray = _delegateBuf.peek().readByteArray()

    public actual val size: Int
        get() = _delegateBuf.size.toInt()
}