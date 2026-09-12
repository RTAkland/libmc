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
        this.socket = s
        this.inputStream = s.getInputStream()
        this.outputStream = s.getOutputStream()
    }

    public actual fun send(data: ByteArray): Int {
        val out = outputStream ?: error("Socket is not connected")
        out.write(data)
        out.flush()
        return data.size
    }

    public actual fun receive(data: ByteArray): Int {
        val input = inputStream ?: error("Socket is not connected")
        return input.read(data)
    }

    public actual override fun close() {
        runCatching { inputStream?.close() }
        runCatching { outputStream?.close() }
        runCatching { socket?.close() }
        inputStream = null
        outputStream = null
        socket = null
    }
}

internal actual fun resolveHostToIp(host: String): List<String> =
    InetAddress.getAllByName(host).map { it.hostAddress }.distinct()