/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.common

public expect class Socket public constructor(host: String, port: Int, context: LibMCContext) {
    public fun openReadChannel(): ReadChannel
    public fun openWriteChannel(): WriteChannel
    public fun close()
}

public expect class UdpSocket public constructor(host: String, port: Int, context: LibMCContext) {
    public fun sendAndReceive(data: ByteArray): ByteArray
    public fun close()
}