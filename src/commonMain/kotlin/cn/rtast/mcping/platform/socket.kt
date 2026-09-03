/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.platform

internal expect class PlatformSocket internal constructor(host: String, port: Int){
    fun openReadChannel(): PlatformReadChannel
    fun openWriteChannel(): PlatformWriteChannel
    fun close()
}