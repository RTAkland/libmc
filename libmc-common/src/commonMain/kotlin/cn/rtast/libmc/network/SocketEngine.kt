/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.network

public fun interface SocketEngine {
    public fun create(host: String, port: Int): RawSocket
}