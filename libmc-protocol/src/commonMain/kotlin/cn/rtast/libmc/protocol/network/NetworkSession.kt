/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */

package cn.rtast.libmc.protocol.network

import cn.rtast.libmc.protocol.client.MinecraftClient
import cn.rtast.libmc.protocol.crypto.Aes128Cfb8ChannelCipher
import cn.rtast.libmc.socket.*

public class NetworkSession internal constructor(private val client: MinecraftClient) {
    private var socket: NativeSocket? = null
    public var readChannel: ReadChannel? = null
        private set

    public var writeChannel: WriteChannel? = null
        private set

    public fun connect() {
        val sk = NativeSocket(client.host, client.port)
        sk.connect()
        this.socket = sk
        this.readChannel = sk.openReadChannel()
        this.writeChannel = sk.openWriteChannel()
    }

    public fun enableEncryption(sharedKey: ByteArray) {
        val currentRead = requireNotNull(readChannel)
        val currentWrite = requireNotNull(writeChannel)
        val decryptCipher = Aes128Cfb8ChannelCipher(sharedKey)
        val encryptCipher = Aes128Cfb8ChannelCipher(sharedKey)
        currentRead.transformer = DataTransformer { buffer, offset, length ->
            decryptCipher.decrypt(buffer, offset, length)
        }
        currentWrite.transformer = DataTransformer { buffer, offset, length ->
            encryptCipher.encrypt(buffer, offset, length)
        }
    }

    internal fun readBytes(length: Int): ByteArray = requireNotNull(readChannel).readBytes(length)
    internal fun readVarInt(): Int {
        var numRead = 0
        var result = 0
        var read: Byte
        do {
            read = requireNotNull(readChannel).readByte()
            val value = (read.toInt() and 0x7F)
            result = result or (value shl (7 * numRead))
            numRead++
            if (numRead > 5) error("VarInt is too big")
        } while ((read.toInt() and 0x80) != 0)
        return result
    }

    internal fun writeFully(data: ByteArray) {
        val channel = requireNotNull(writeChannel)
        channel.writeFully(data, 0, data.size)
        channel.flush()
    }

    internal fun close() {
        socket?.close()
    }
}