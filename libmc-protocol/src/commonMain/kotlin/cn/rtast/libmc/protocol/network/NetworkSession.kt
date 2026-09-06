/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */

package cn.rtast.libmc.protocol.network

import cn.rtast.libmc.common.LibMCContext
import cn.rtast.libmc.common.ReadChannel
import cn.rtast.libmc.common.Socket
import cn.rtast.libmc.common.WriteChannel
import cn.rtast.libmc.protocol.crypto.NetworkCipher

internal class NetworkSession(
    private val host: String,
    private val port: Int,
    private val context: LibMCContext,
    private var cipherProvider: (ByteArray) -> NetworkCipher,
) {
    private var socket: Socket? = null

    var readChannel: ReadChannel? = null
        private set

    var writeChannel: WriteChannel? = null
        private set

    fun connect() {
        val sk = Socket(host, port, context)
        this.socket = sk
        this.readChannel = sk.openReadChannel()
        this.writeChannel = sk.openWriteChannel()
    }

    fun enableEncryption(sharedKey: ByteArray) {
        val currentRead = requireNotNull(readChannel) { "ReadChannel not connected" }
        val currentWrite = requireNotNull(writeChannel) { "WriteChannel not connected" }
        val cipher = cipherProvider(sharedKey)
        this.readChannel = CipherReadChannel(currentRead, cipher)
        this.writeChannel = CipherWriteChannel(currentWrite, cipher)
    }

    fun readByte(): Byte {
        val channel = requireNotNull(readChannel) { "ReadChannel not connected" }
        return channel.readByte()
    }

    fun readBytes(length: Int): ByteArray {
        val channel = requireNotNull(readChannel) { "ReadChannel not connected" }
        return channel.readBytes(length)
    }

    fun readVarInt(): Int {
        var numRead = 0
        var result = 0
        var read: Byte
        do {
            read = readByte()
            val value = (read.toInt() and 0x7F)
            result = result or (value shl (7 * numRead))
            numRead++
            if (numRead > 5) throw IllegalArgumentException("VarInt is too big")
        } while ((read.toInt() and 0x80) != 0)
        return result
    }

    fun writeFully(data: ByteArray) {
        val channel = requireNotNull(writeChannel) { "WriteChannel not connected" }
        channel.writeFully(data, 0, data.size)
        channel.flush()
    }

    fun close() {
        socket?.close()
    }
}