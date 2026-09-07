/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.network

import cn.rtast.libmc.crypto.ProtocolContextBuilder

public abstract class SocketContext {
    public abstract val engine: SocketEngine

    public fun createSocket(host: String, port: Int): RawSocket = engine.create(host, port)
}

public fun (ProtocolContextBuilder.() -> Unit).withCustom(
    block: ProtocolContextBuilder.() -> Unit,
): ProtocolContextBuilder.() -> Unit = { this@withCustom.invoke(this); this.block() }