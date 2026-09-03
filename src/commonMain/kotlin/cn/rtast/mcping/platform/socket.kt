/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

internal expect class Socket internal constructor(host: String, port: Int, context: PingContext) {
    fun openReadChannel(): ReadChannel
    fun openWriteChannel(): WriteChannel
    fun close()
}

internal expect class UdpSocket internal constructor(host: String, port: Int, context: PingContext) {
    fun sendAndReceive(data: ByteArray): ByteArray
    fun close()
}