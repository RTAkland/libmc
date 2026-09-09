/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */

package cn.rtast.libmc.protocol.network

import cn.rtast.libmc.network.RawSocket
import cn.rtast.libmc.network.ReadChannel
import cn.rtast.libmc.network.WriteChannel
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.client.MinecraftClient
import cn.rtast.libmc.protocol.crypto.Aes128Cfb8ChannelCipher

public class NetworkSession internal constructor(private val client: MinecraftClient) {
    private var socket: RawSocket? = null
    public var readChannel: ReadChannel? = null
        private set

    public var writeChannel: WriteChannel? = null
        private set

    public suspend fun connect() {
        val sk = client.protocolContext.createSocket(client.host, client.port)
        sk.connect()
        this.socket = sk
        this.readChannel = sk.openReadChannel()
        this.writeChannel = sk.openWriteChannel()
    }

    public fun enableEncryption(sharedKey: ByteArray) {
        val currentRead = requireNotNull(readChannel)
        val currentWrite = requireNotNull(writeChannel)
        val cipher = Aes128Cfb8ChannelCipher(sharedKey)
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