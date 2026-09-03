/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

@Suppress("CLASSNAME")
public expect class _Socket public constructor(host: String, port: Int, context: MCPingContext) {
    public fun openReadChannel(): _ReadChannel
    public fun openWriteChannel(): _WriteChannel
    public fun close()
}

@Suppress("CLASSNAME")
public expect class _UdpSocket public constructor(host: String, port: Int, context: MCPingContext) {
    public fun sendAndReceive(data: ByteArray): ByteArray
    public fun close()
}