/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.common

import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlinx.io.readByteArray

@Suppress("CLASSNAME")
public actual class Socket public actual constructor(host: String, port: Int, context: LibMCContext) {
    private val ctx = context
    private val socket = runBlocking { aSocket(ctx._selectorManager).tcp().connect(host, port) }

    public actual fun openReadChannel(): ReadChannel = ReadChannel(socket.openReadChannel())
    public actual fun openWriteChannel(): WriteChannel =
        WriteChannel(socket.openWriteChannel(autoFlush = true))

    public actual fun close() {
        socket.close()
        if (ctx._autoCloseSelectorManager) ctx._selectorManager.close()
    }
}

@Suppress("CLASSNAME")
public actual class _UdpSocket public actual constructor(host: String, port: Int, context: LibMCContext) {
    private val ctx = context

    // use bind to create an unconnected socket
    private val socket = runBlocking { aSocket(ctx._selectorManager).udp().bind() }
    private val remoteAddress = InetSocketAddress(host, port)

    public actual fun sendAndReceive(data: ByteArray): ByteArray = runBlocking {
        val packet = buildPacket { writeFully(data) }
        socket.send(Datagram(packet, remoteAddress))
        socket.receive().packet.readByteArray()
    }

    public actual fun close() {
        socket.close()
        if (ctx._autoCloseSelectorManager) ctx._selectorManager.close()
    }
}