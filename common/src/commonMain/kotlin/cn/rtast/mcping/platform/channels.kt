/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

@Suppress("CLASSNAME")
public expect class _ReadChannel {
    public fun readByte(): Byte
    public fun readBytes(length: Int): ByteArray
    public fun readFully(out: ByteArray, start: Int = 0, end: Int = out.size)
    public fun readLong(): Long
}

@Suppress("CLASSNAME")
public expect class _WriteChannel {
    public fun writeFully(value: ByteArray, startIndex: Int = 0, endIndex: Int = value.size)
    public fun flush()
}