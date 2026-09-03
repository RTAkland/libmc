/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping.platform

import java.net.Socket

internal actual class PlatformSocket internal actual constructor(host: String, port: Int) {
    private val socket = Socket(host, port)

    actual fun openReadChannel(): PlatformReadChannel = PlatformReadChannel(socket.getInputStream())
    actual fun openWriteChannel(): PlatformWriteChannel = PlatformWriteChannel(socket.getOutputStream())
    actual fun close() = socket.close()
}