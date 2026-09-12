/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.socket

import kotlinx.cinterop.*
import platform.posix.*

public actual class NativeSocket actual constructor(private val host: String, private val port: Int) : AutoCloseable {
    private var socketFd = -1

    public actual fun connect(): Unit = memScoped {
        val hints = alloc<addrinfo>()
        memset(hints.ptr, 0, sizeOf<addrinfo>().toULong())
        hints.ai_family = AF_UNSPEC
        hints.ai_socktype = SOCK_STREAM
        val res = allocPointerTo<addrinfo>()
        val portStr = port.toString()
        if (getaddrinfo(host, portStr, hints.ptr, res.ptr) != 0) error("Failed to resolve host '$host:$port'")
        var ptr = res.value
        var connected = false
        while (ptr != null) {
            val ai = ptr.pointed
            socketFd = socket(ai.ai_family, ai.ai_socktype, ai.ai_protocol)
            if (socketFd >= 0) {
                if (connect(socketFd, ai.ai_addr, ai.ai_addrlen) == 0) {
                    connected = true
                    break
                }
                close(socketFd)
                socketFd = -1
            }
            ptr = ai.ai_next
        }
        if (res.value != null) freeaddrinfo(res.value)
        if (!connected || socketFd < 0) error("Could not connect to $host:$port via IPv4 or IPv6")
    }

    public actual fun send(data: ByteArray): Int = memScoped {
        if (socketFd < 0) throw IllegalStateException("Socket is not connected")
        if (data.isEmpty()) return 0
        val pinned = data.pin()
        val bytesSent = send(socketFd, pinned.addressOf(0), data.size.toULong(), 0)
        pinned.unpin()
        if (bytesSent < 0) error("Socket send failed: errno = $errno")
        return bytesSent.toInt()
    }

    public actual fun receive(data: ByteArray): Int = memScoped {
        if (socketFd < 0) throw IllegalStateException("Socket is not connected")
        if (data.isEmpty()) return 0
        val pinned = data.pin()
        val bytesRead = recv(socketFd, pinned.addressOf(0), data.size.toULong(), 0)
        pinned.unpin()
        if (bytesRead < 0) error("Socket receive failed: errno = $errno")
        return bytesRead.toInt()
    }

    public actual override fun close() {
        if (socketFd >= 0) {
            close(socketFd)
            socketFd = -1
        }
    }
}

internal actual fun resolveHostToIp(host: String): List<String> = memScoped {
    val ips = mutableListOf<String>()
    val hints = alloc<addrinfo>()
    memset(hints.ptr, 0, sizeOf<addrinfo>().toULong())
    hints.ai_family = AF_UNSPEC
    hints.ai_socktype = SOCK_STREAM
    val resultPtr = allocPointerTo<addrinfo>()
    if (getaddrinfo(host, null, hints.ptr, resultPtr.ptr) != 0) error("Failed to resolve host '$host'")
    var current = resultPtr.value
    while (current != null) {
        val addr = current.pointed
        val family = addr.ai_family
        if (family == AF_INET) {
            val sockaddrIn = addr.ai_addr?.reinterpret<sockaddr_in>()
            if (sockaddrIn != null) formatSockAddrToIp(AF_INET, sockaddrIn.pointed.sin_addr.ptr)?.let { ips.add(it) }
        } else if (family == AF_INET6) {
            val sockaddrIn6 = addr.ai_addr?.reinterpret<sockaddr_in6>()
            if (sockaddrIn6 != null) formatSockAddrToIp(AF_INET6, sockaddrIn6.pointed.sin6_addr.ptr)
                ?.let { ips.add(it) }
        }
        current = addr.ai_next
    }
    if (resultPtr.value != null) freeaddrinfo(resultPtr.value)
    return ips.distinct()
}