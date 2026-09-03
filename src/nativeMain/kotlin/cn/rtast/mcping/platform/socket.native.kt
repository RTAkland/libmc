/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping.platform

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking

internal actual class PlatformSocket internal actual constructor(host: String, port: Int) {

    private val sm = SelectorManager(Dispatchers.IO)
    private val socket = runBlocking { aSocket(sm).tcp().connect(host, port) }

    actual fun openReadChannel(): PlatformReadChannel = PlatformReadChannel(socket.openReadChannel())
    actual fun openWriteChannel(): PlatformWriteChannel =
        PlatformWriteChannel(socket.openWriteChannel(autoFlush = true))

    actual fun close() {
        socket.close()
        sm.close()
    }
}