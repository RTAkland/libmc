/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping.platform

import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlinx.io.readByteArray

internal actual class Socket internal actual constructor(host: String, port: Int, context: PingContext) {
    private val ctx = context
    private val socket = runBlocking { aSocket(ctx._selectorManager).tcp().connect(host, port) }

    actual fun openReadChannel(): ReadChannel = ReadChannel(socket.openReadChannel())
    actual fun openWriteChannel(): WriteChannel =
        WriteChannel(socket.openWriteChannel(autoFlush = true))

    actual fun close() {
        socket.close()
        if (ctx._autoCloseSelectorManager) ctx._selectorManager.close()
    }
}

internal actual class UdpSocket internal actual constructor(host: String, port: Int, context: PingContext) {
    private val ctx = context

    // use bind to create an unconnected socket
    private val socket = runBlocking { aSocket(ctx._selectorManager).udp().bind() }
    private val remoteAddress = InetSocketAddress(host, port)

    actual fun sendAndReceive(data: ByteArray): ByteArray = runBlocking {
        val packet = buildPacket { writeFully(data) }
        socket.send(Datagram(packet, remoteAddress))
        socket.receive().packet.readByteArray()
    }

    actual fun close() {
        socket.close()
        if (ctx._autoCloseSelectorManager) ctx._selectorManager.close()
    }
}