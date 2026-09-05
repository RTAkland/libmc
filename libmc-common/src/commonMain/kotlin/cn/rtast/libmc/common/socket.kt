/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.common

@Suppress("CLASSNAME")
public expect class Socket public constructor(host: String, port: Int, context: LibMCContext) {
    public fun openReadChannel(): ReadChannel
    public fun openWriteChannel(): WriteChannel
    public fun close()
}

@Suppress("CLASSNAME")
public expect class _UdpSocket public constructor(host: String, port: Int, context: LibMCContext) {
    public fun sendAndReceive(data: ByteArray): ByteArray
    public fun close()
}