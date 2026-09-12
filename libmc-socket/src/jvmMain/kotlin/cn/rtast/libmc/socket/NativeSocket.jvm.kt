/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */

package cn.rtast.libmc.socket

import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.Socket

public actual class NativeSocket actual constructor(private val host: String, private val port: Int) : AutoCloseable {
    private var socket: Socket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    public actual fun connect() {
        val s = Socket(host, port)
        s.tcpNoDelay = true
        this.socket = s
        this.inputStream = s.getInputStream()
        this.outputStream = s.getOutputStream()
    }

    public actual fun send(data: ByteArray): Int = send(data, 0, data.size)

    public actual fun send(data: ByteArray, offset: Int, length: Int): Int {
        val out = outputStream ?: error("Socket is not connected")
        out.write(data, offset, length)
        out.flush()
        return length
    }

    public actual fun receive(data: ByteArray): Int = receive(data, 0, data.size)

    public actual fun receive(data: ByteArray, offset: Int, length: Int): Int {
        val input = inputStream ?: error("Socket is not connected")
        return input.read(data, offset, length)
    }

    public actual override fun close() {
        runCatching { socket?.close() }
        runCatching { inputStream?.close() }
        runCatching { outputStream?.close() }
        inputStream = null
        outputStream = null
        socket = null
    }
}

internal actual fun resolveHostToIp(host: String): List<String> =
    InetAddress.getAllByName(host).map { it.hostAddress }.distinct()