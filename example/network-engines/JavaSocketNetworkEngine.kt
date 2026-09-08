/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package engines

import cn.rtast.libmc.network.RawSocket
import cn.rtast.libmc.network.ReadChannel
import cn.rtast.libmc.network.SocketEngine
import cn.rtast.libmc.network.WriteChannel
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

class JavaSocketEngine : SocketEngine {
    override fun create(host: String, port: Int): RawSocket = JavaNetworkSocket(host, port)
}

class JavaNetworkSocket(
    private val host: String,
    private val port: Int,
    private val connectTimeoutMs: Int = 10000,
) : RawSocket {
    private lateinit var socket: Socket
    private lateinit var readChannel: JavaReadChannel
    private lateinit var writeChannel: JavaWriteChannel

    override suspend fun connect() {
        val s = Socket()
        s.tcpNoDelay = true
        s.connect(InetSocketAddress(host, port), connectTimeoutMs)

        socket = s
        readChannel = JavaReadChannel(s.getInputStream())
        writeChannel = JavaWriteChannel(s.getOutputStream())
    }

    override fun openReadChannel(): ReadChannel = readChannel
    override fun openWriteChannel(): WriteChannel = writeChannel

    override fun close() {
        if (::socket.isInitialized && !socket.isClosed) {
            runCatching { socket.close() }
        }
    }
}

class JavaReadChannel(private val inputStream: InputStream) : ReadChannel {
    override suspend fun readByte(): Byte {
        val b = inputStream.read()
        if (b == -1) throw IllegalStateException("Socket stream reached EOF while reading byte")
        return b.toByte()
    }

    override suspend fun readBytes(length: Int): ByteArray {
        val buffer = ByteArray(length)
        readFullyInternal(buffer, 0, length)
        return buffer
    }

    override suspend fun readFully(out: ByteArray, start: Int, end: Int) {
        readFullyInternal(out, start, end - start)
    }

    private fun readFullyInternal(out: ByteArray, offset: Int, length: Int) {
        var bytesRead = 0
        while (bytesRead < length) {
            val count = inputStream.read(out, offset + bytesRead, length - bytesRead)
            if (count == -1) {
                throw IllegalStateException("Socket stream closed unexpectedly (read $bytesRead of $length bytes)")
            }
            bytesRead += count
        }
    }
}

class JavaWriteChannel(private val outputStream: OutputStream) : WriteChannel {
    override suspend fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int) {
        outputStream.write(value, startIndex, endIndex - startIndex)
    }

    override suspend fun flush() {
        outputStream.flush()
    }
}