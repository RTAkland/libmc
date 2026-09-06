/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.network

import cn.rtast.libmc.common.ReadChannel
import cn.rtast.libmc.common.WriteChannel
import cn.rtast.libmc.protocol.crypto.NetworkCipher

internal class CipherReadChannel(
    private val delegate: ReadChannel,
    private val crypto: NetworkCipher
) : ReadChannel() {
    override fun readFully(out: ByteArray, start: Int, end: Int) {
        delegate.readFully(out, start, end)
        val length = end - start
        if (length > 0) crypto.decrypt(out, start, length)
    }

    override fun readByte(): Byte {
        val buf = ByteArray(1)
        readFully(buf, 0, 1)
        return buf[0]
    }

    override fun readBytes(length: Int): ByteArray {
        val bytes = ByteArray(length)
        readFully(bytes, 0, length)
        return bytes
    }
}

internal class CipherWriteChannel(
    private val delegate: WriteChannel,
    private val crypto: NetworkCipher
) : WriteChannel() {
    override fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int) {
        val length = endIndex - startIndex
        if (length <= 0) return
        val encrypted = value.copyOfRange(startIndex, endIndex)
        crypto.encrypt(encrypted, 0, length)
        delegate.writeFully(encrypted, 0, length)
    }
    override fun flush() {
        delegate.flush()
    }
}