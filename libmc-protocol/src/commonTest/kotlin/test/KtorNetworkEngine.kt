/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package test

import cn.rtast.libmc.network.RawSocket
import cn.rtast.libmc.network.ReadChannel
import cn.rtast.libmc.network.SocketEngine
import cn.rtast.libmc.network.WriteChannel
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlin.coroutines.cancellation.CancellationException

class KtorNetworkEngine : SocketEngine {
    override fun create(host: String, port: Int): RawSocket = KtorNetworkSocket(host, port)
}

class KtorNetworkSocket(private val host: String, private val port: Int) : RawSocket {
    private val sm = SelectorManager(Dispatchers.IO)
    private lateinit var socket: Socket
    private var ktorReadChannel: ByteReadChannel? = null
    private var ktorWriteChannel: ByteWriteChannel? = null

    override suspend fun connect(): Unit = run { socket = aSocket(sm).tcp().connect(host, port) }

    override fun openReadChannel(): ReadChannel {
        val channel = socket.openReadChannel()
        this.ktorReadChannel = channel
        return KtorReadChannel(channel)
    }

    override fun openWriteChannel(): WriteChannel {
        val channel = socket.openWriteChannel(autoFlush = false)
        this.ktorWriteChannel = channel
        return KtorWriteChannel(channel)
    }

    override fun close() {
        try {
            ktorReadChannel?.cancel(CancellationException("Socket closed"))
            ktorWriteChannel?.close(null)
            if (::socket.isInitialized) socket.dispose()
        } catch (_: Exception) {
        } finally {
            sm.close()
        }
    }
}

class KtorReadChannel(private val readChannel: ByteReadChannel) : ReadChannel {
    private inline fun <T> wrapChannelException(block: () -> T): T {
        return try {
            block()
        } catch (e: Throwable) {
            if (e is ClosedByteChannelException || e is CancellationException || readChannel.isClosedForRead) {
                throw CancellationException("ReadChannel was closed", e)
            }
            throw e
        }
    }

    override suspend fun readByte(): Byte = wrapChannelException {
        if (readChannel.isClosedForRead) throw CancellationException("ReadChannel is closed for read")
        readChannel.readByte()
    }

    override suspend fun readBytes(length: Int): ByteArray = wrapChannelException {
        if (readChannel.isClosedForRead) throw CancellationException("ReadChannel is closed for read")
        readChannel.readByteArray(length)
    }

    override suspend fun readFully(out: ByteArray, start: Int, end: Int): Unit = wrapChannelException {
        if (readChannel.isClosedForRead) throw CancellationException("ReadChannel is closed for read")
        readChannel.readFully(out, start, end)
    }
}

class KtorWriteChannel(private val writeChannel: ByteWriteChannel) : WriteChannel {
    override suspend fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int) {
        writeChannel.writeFully(value, startIndex, endIndex)
    }

    override suspend fun flush(): Unit = writeChannel.flush()
}