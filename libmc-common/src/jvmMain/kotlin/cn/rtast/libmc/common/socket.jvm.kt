/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.common

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.Socket as JvmSocket

@Suppress("CLASSNAME")
public actual class Socket public actual constructor(host: String, port: Int, context: LibMCContext) {
    private val socket = JvmSocket(host, port)

    public actual fun openReadChannel(): ReadChannel = ReadChannel(socket.getInputStream())
    public actual fun openWriteChannel(): WriteChannel = WriteChannel(socket.getOutputStream())
    public actual fun close(): Unit = socket.close()
}

@Suppress("CLASSNAME")
public actual class _UdpSocket public actual constructor(host: String, port: Int, context: LibMCContext) {
    private val socket = DatagramSocket().apply {
        soTimeout = 3000
        connect(InetSocketAddress(host, port))
    }

    public actual fun sendAndReceive(data: ByteArray): ByteArray {
        socket.send(DatagramPacket(data, data.size))

        val buf = ByteArray(2048)
        val receivePacket = DatagramPacket(buf, buf.size)
        socket.receive(receivePacket)
        return buf.copyOf(receivePacket.length)
    }

    public actual fun close(): Unit = socket.close()
}