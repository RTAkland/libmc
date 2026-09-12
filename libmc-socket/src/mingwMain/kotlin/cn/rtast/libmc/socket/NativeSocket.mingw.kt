/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.socket

import kotlinx.cinterop.*
import platform.posix.*
import platform.windows.INET6_ADDRSTRLEN
import platform.windows.InetNtopA
import platform.windows.addrinfo
import platform.windows.freeaddrinfo
import platform.windows.getaddrinfo
import platform.windows.sockaddr_in6
import kotlin.AutoCloseable
import kotlin.ByteArray
import kotlin.IllegalStateException
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.Unit
import kotlin.error
import kotlin.toULong
import kotlin.toUShort

public actual class NativeSocket actual constructor(private val host: String, private val port: Int) : AutoCloseable {
    private var socketFd: SOCKET = INVALID_SOCKET

    public actual fun connect(): Unit = memScoped {
        initWinSock()
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
            if (socketFd != INVALID_SOCKET) {
                if (connect(socketFd, ai.ai_addr, ai.ai_addrlen.toInt()) == 0) {
                    connected = true
                    break
                }
                closesocket(socketFd)
                socketFd = INVALID_SOCKET
            }
            ptr = ai.ai_next
        }
        if (res.value != null) freeaddrinfo(res.value)
        if (!connected || socketFd == INVALID_SOCKET) error("Could not connect to $host:$port via IPv4 or IPv6")
    }

    public actual fun send(data: ByteArray): Int = memScoped {
        if (socketFd == INVALID_SOCKET) throw IllegalStateException("Socket is not connected")
        if (data.isEmpty()) return 0
        val pinned = data.pin()
        val bytesSent = send(socketFd, pinned.addressOf(0).reinterpret(), data.size, 0)
        pinned.unpin()
        if (bytesSent < 0) error("Socket send failed with WinSock error: ${WSAGetLastError()}")
        return bytesSent
    }

    public actual fun receive(data: ByteArray): Int = memScoped {
        if (socketFd == INVALID_SOCKET) throw IllegalStateException("Socket is not connected")
        if (data.isEmpty()) return 0
        val pinned = data.pin()
        val bytesRead = recv(socketFd, pinned.addressOf(0).reinterpret(), data.size, 0)
        pinned.unpin()
        if (bytesRead < 0) error("Socket receive failed with WinSock error: ${WSAGetLastError()}")
        return bytesRead
    }

    public actual override fun close() {
        if (socketFd != INVALID_SOCKET) {
            closesocket(socketFd)
            socketFd = INVALID_SOCKET
        }
    }

    private fun initWinSock() = memScoped {
        val wsaData = alloc<WSADATA>()
        val result = WSAStartup(0x0202.toUShort(), wsaData.ptr)
        if (result != 0) error("WSAStartup failed with error code: $result")
    }
}

internal actual fun resolveHostToIp(host: String): List<String> = memScoped {
    val wsaData = alloc<WSADATA>()
    if (WSAStartup(0x0202.toUShort(), wsaData.ptr) != 0) error("WSAStartup failed")
    val ips = mutableListOf<String>()
    val hints = alloc<addrinfo>()
    memset(hints.ptr, 0, sizeOf<addrinfo>().toULong())
    hints.ai_family = AF_UNSPEC
    hints.ai_socktype = SOCK_STREAM
    val resultPtr = allocPointerTo<addrinfo>()
    if (getaddrinfo(host, null, hints.ptr, resultPtr.ptr) != 0) error("Failed to resolve host '$host'")
    var current = resultPtr.value
    val ipBuffer = allocArray<ByteVar>(INET6_ADDRSTRLEN)
    while (current != null) {
        val addr = current.pointed
        val family = addr.ai_family
        if (family == AF_INET) {
            val sockaddrIn = addr.ai_addr?.reinterpret<sockaddr_in>()
            if (sockaddrIn != null) if (InetNtopA?.invoke(
                    AF_INET,
                    sockaddrIn.pointed.sin_addr.ptr,
                    ipBuffer,
                    INET6_ADDRSTRLEN.toULong()
                ) != null
            ) ips.add(ipBuffer.toKString())
        } else if (family == AF_INET6) {
            val sockaddrIn6 = addr.ai_addr?.reinterpret<sockaddr_in6>()
            if (sockaddrIn6 != null) if (InetNtopA?.invoke(
                    AF_INET6,
                    sockaddrIn6.pointed.sin6_addr.ptr,
                    ipBuffer,
                    INET6_ADDRSTRLEN.toULong()
                ) != null
            ) ips.add(ipBuffer.toKString())
        }
        current = addr.ai_next
    }
    if (resultPtr.value != null) freeaddrinfo(resultPtr.value)
    return ips.distinct()
}