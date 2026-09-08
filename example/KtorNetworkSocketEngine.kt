/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package engines


public class KtorNetworkEngine : SocketEngine {
    override fun create(host: String, port: Int): RawSocket = KtorNetworkSocket(host, port)
}

public class KtorNetworkSocket(private val host: String, private val port: Int) : RawSocket {
    private val sm = SelectorManager(Dispatchers.IO)
    private lateinit var socket: Socket

    override suspend fun connect(): Unit = run { socket = aSocket(sm).tcp().connect(host, port) }
    override fun openReadChannel(): ReadChannel = KtorReadChannel(socket.openReadChannel())
    override fun openWriteChannel(): WriteChannel = KtorWriteChannel(socket.openWriteChannel())
    override fun close() {
        socket.close()
        sm.close()
    }
}

public class KtorReadChannel(private val readChannel: ByteReadChannel) : ReadChannel {
    override suspend fun readByte(): Byte = readChannel.readByte()
    override suspend fun readBytes(length: Int): ByteArray = readChannel.readByteArray(length)
    override suspend fun readFully(out: ByteArray, start: Int, end: Int): Unit =
        readChannel.readFully(out, start, end)
}

public class KtorWriteChannel(private val writeChannel: ByteWriteChannel) : WriteChannel {
    override suspend fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int) {
        writeChannel.writeFully(value, startIndex, endIndex)
    }

    override suspend fun flush(): Unit = writeChannel.flush()
}