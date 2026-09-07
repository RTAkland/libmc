/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */

package cn.rtast.libmc.protocol.network

import cn.rtast.libmc.crypto.NetworkCipher
import cn.rtast.libmc.crypto.ProtocolContext
import cn.rtast.libmc.network.RawSocket
import cn.rtast.libmc.network.ReadChannel
import cn.rtast.libmc.network.WriteChannel
import cn.rtast.libmc.primitives.readVarInt

public class NetworkSession internal constructor(
    private val host: String,
    private val port: Int,
    private var cipherProvider: (ByteArray) -> NetworkCipher,
    private val context: ProtocolContext,
) {
    private var socket: RawSocket? = null

    public var readChannel: ReadChannel? = null
        private set

    public var writeChannel: WriteChannel? = null
        private set

    public suspend fun connect() {
        val sk = context.createSocket(host, port)
        sk.connect()
        this.socket = sk
        this.readChannel = sk.openReadChannel()
        this.writeChannel = sk.openWriteChannel()
    }

    public fun enableEncryption(sharedKey: ByteArray) {
        val currentRead = requireNotNull(readChannel)
        val currentWrite = requireNotNull(writeChannel)
        val cipher = cipherProvider(sharedKey)
        this.readChannel = CipherReadChannel(currentRead, cipher)
        this.writeChannel = CipherWriteChannel(currentWrite, cipher)
    }

    internal suspend fun readBytes(length: Int): ByteArray = requireNotNull(readChannel).readBytes(length)

    internal suspend fun readVarInt(): Int = requireNotNull(readChannel).readVarInt()

    internal suspend fun writeFully(data: ByteArray) {
        val channel = requireNotNull(writeChannel)
        channel.writeFully(data, 0, data.size)
        channel.flush()
    }

    internal fun close() {
        socket?.close()
    }
}