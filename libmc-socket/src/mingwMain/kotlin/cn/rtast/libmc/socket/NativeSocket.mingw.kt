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

private object WinsockManager {
    val isInitialized: Boolean by lazy {
        memScoped {
            val wsaData = alloc<WSADATA>()
            val result = WSAStartup(0x0202.toUShort(), wsaData.ptr)
            if (result != 0) error("WSAStartup failed with error code: $result")
            true
        }
    }
}

public actual class NativeSocket actual constructor(private val host: String, private val port: Int) : AutoCloseable {
    private var socketFd: SOCKET = INVALID_SOCKET

    public actual fun connect(): Unit = memScoped {
        check(WinsockManager.isInitialized)
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
                    setsockopt(socketFd, IPPROTO_TCP, TCP_NODELAY, "\u0001", 1)  // why there is string required?
                    break
                }
                closesocket(socketFd)
                socketFd = INVALID_SOCKET
            }
            ptr = ai.ai_next
        }
        if (res.value != null) freeaddrinfo(res.value)
        if (!connected || socketFd == INVALID_SOCKET) error("Could not connect to $host:$port")
    }

    public actual fun send(data: ByteArray): Int = send(data, 0, data.size)
    public actual fun receive(data: ByteArray): Int = receive(data, 0, data.size)
    public actual fun send(data: ByteArray, offset: Int, length: Int): Int {
        if (socketFd == INVALID_SOCKET) error("Socket is not connected")
        if (data.isEmpty() || length <= 0) return 0
        return data.usePinned { pinned ->
            val bytesSent = send(socketFd, pinned.addressOf(offset).reinterpret(), length, 0)
            if (bytesSent < 0) error("Socket send failed with WinSock error: ${WSAGetLastError()}")
            bytesSent
        }
    }

    public actual fun receive(data: ByteArray, offset: Int, length: Int): Int {
        if (socketFd == INVALID_SOCKET) error("Socket is not connected")
        if (data.isEmpty() || length <= 0) return 0
        return data.usePinned { pinned ->
            val bytesRead = recv(socketFd, pinned.addressOf(offset).reinterpret(), length, 0)
            if (bytesRead < 0) error("Socket receive failed with WinSock error: ${WSAGetLastError()}")
            bytesRead
        }
    }

    public actual override fun close() {
        if (socketFd != INVALID_SOCKET) {
            closesocket(socketFd)
            socketFd = INVALID_SOCKET
        }
    }
}

internal actual fun resolveHostToIp(host: String): List<String> = memScoped {
    check(WinsockManager.isInitialized)
    val ips = mutableListOf<String>()
    val hints = alloc<addrinfo>()
    memset(hints.ptr, 0, sizeOf<addrinfo>().toULong())
    hints.ai_family = AF_UNSPEC
    hints.ai_socktype = SOCK_STREAM
    val resultPtr = allocPointerTo<addrinfo>()
    if (getaddrinfo(host, null, hints.ptr, resultPtr.ptr) != 0) error("Failed to resolve host '$host'")
    var current = resultPtr.value
    val ipBuffer = allocArray<ByteVar>(INET6_ADDRSTRLEN)
    val inetNtop = InetNtopA
    while (current != null) {
        val addr = current.pointed
        val family = addr.ai_family
        if (inetNtop != null) {
            if (family == AF_INET) {
                val sockaddrIn = addr.ai_addr?.reinterpret<sockaddr_in>()
                if (sockaddrIn != null) {
                    if (inetNtop.invoke(
                            AF_INET,
                            sockaddrIn.pointed.sin_addr.ptr,
                            ipBuffer,
                            INET6_ADDRSTRLEN.toULong()
                        ) != null
                    ) ips.add(ipBuffer.toKString())
                }
            } else if (family == AF_INET6) {
                val sockaddrIn6 = addr.ai_addr?.reinterpret<sockaddr_in6>()
                if (sockaddrIn6 != null) {
                    if (inetNtop.invoke(
                            AF_INET6,
                            sockaddrIn6.pointed.sin6_addr.ptr,
                            ipBuffer,
                            INET6_ADDRSTRLEN.toULong()
                        ) != null
                    ) ips.add(ipBuffer.toKString())
                }
            }
        }
        current = addr.ai_next
    }
    if (resultPtr.value != null) freeaddrinfo(resultPtr.value)
    return ips.distinct()
}