/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */


package cn.rtast.libmc.socket

public expect class NativeSocket public constructor(host: String, port: Int) : AutoCloseable {
    public fun connect()
    public fun send(data: ByteArray): Int
    public fun receive(data: ByteArray): Int
    public override fun close()
}

public fun NativeSocket.openReadChannel(bufferSize: Int = 8192): ReadChannel = ReadChannel(this, bufferSize)
public fun NativeSocket.openWriteChannel(): WriteChannel = WriteChannel(this)

internal expect fun resolveHostToIp(host: String): List<String>