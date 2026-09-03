/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping.platform

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.Socket as JvmSocket

internal actual class Socket internal actual constructor(host: String, port: Int, context: PingContext) {
    private val socket = JvmSocket(host, port)

    actual fun openReadChannel(): ReadChannel = ReadChannel(socket.getInputStream())
    actual fun openWriteChannel(): WriteChannel = WriteChannel(socket.getOutputStream())
    actual fun close() = socket.close()
}

internal actual class UdpSocket internal actual constructor(host: String, port: Int, context: PingContext) {
    private val socket = DatagramSocket().apply {
        soTimeout = 3000
        connect(InetSocketAddress(host, port))
    }

    actual fun sendAndReceive(data: ByteArray): ByteArray {
        socket.send(DatagramPacket(data, data.size))

        val buf = ByteArray(2048)
        val receivePacket = DatagramPacket(buf, buf.size)
        socket.receive(receivePacket)
        return buf.copyOf(receivePacket.length)
    }

    actual fun close() = socket.close()
}