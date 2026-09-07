/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.network

public abstract class SocketContext {
    public abstract val engine: SocketEngine

    public fun createSocket(host: String, port: Int): RawSocket = engine.create(host, port)
}