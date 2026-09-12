/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */


package test

import cn.rtast.libmc.socket.NativeSocket
import cn.rtast.libmc.socket.resolveHostToIp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NativeSocketTest {

    private val testPort = 16778

    @Test
    fun testIPv4Connection() {
        assertEchoSuccess("127.0.0.1", testPort, "Hello IPv4")
    }

    @Test
    fun testIPv6Connection() {
        assertEchoSuccess("::1", testPort, "Hello IPv6")
    }

    @Test
    fun testLocalhostConnection() {
        assertEchoSuccess("localhost", testPort, "Hello DualStack")
    }

    private fun assertEchoSuccess(host: String, port: Int, message: String) {
        val socket = NativeSocket(host, port)
        socket.connect()
        val sendData = message.encodeToByteArray()
        val sentBytes = socket.send(sendData)
        assertEquals(sendData.size, sentBytes)
        val recvBuffer = ByteArray(1024)
        val readBytes = socket.receive(recvBuffer)
        assertTrue(readBytes > 0)
        assertEquals(message, recvBuffer.decodeToString(0, readBytes))
        socket.close()
    }

    @Test
    public fun testResolvePublicHostToIp() {
        val targetHost = "baidu.com"
        val ips = resolveHostToIp(targetHost)
        println("Resolved IPs for $targetHost: $ips")
        assertTrue(ips.isNotEmpty(), "Failed to resolve any IP for $targetHost")
        ips.forEach { ip ->
            val isValidIp = isValidIPv4(ip) || isValidIPv6(ip)
            assertTrue(isValidIp, "Invalid IP format resolved: $ip")
        }
    }

    @Test
    public fun testResolveRawIpAddress() {
        val ipv4Raw = "1.1.1.1"
        val ipv6Raw = "2606:4700:4700::1111"
        val ipv4Result = resolveHostToIp(ipv4Raw)
        val ipv6Result = resolveHostToIp(ipv6Raw)
        assertTrue(ipv4Result.contains(ipv4Raw), "Failed to pass-through raw IPv4")
        assertTrue(ipv6Result.contains(ipv6Raw), "Failed to pass-through raw IPv6")
    }

    private fun isValidIPv4(ip: String): Boolean {
        val parts = ip.split(".")
        return parts.size == 4 && parts.all { part -> part.toIntOrNull()?.let { it in 0..255 } ?: false }
    }

    private fun isValidIPv6(ip: String): Boolean {
        return ip.contains(":") && !ip.contains(" ")
    }
}