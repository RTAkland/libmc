/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.mcping.java

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.stream.ReadChannel
import cn.rtast.libmc.stream.wrap

private suspend fun ReadChannel.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Byte
    do {
        read = this.readByte()
        val value = (read.toInt() and 0x7F)
        result = result or (value shl (7 * numRead))
        numRead++
        if (numRead > 5) throw IllegalArgumentException("VarInt is too big")
    } while ((read.toInt() and 0x80) != 0)
    return result
}

public suspend fun ReadChannel.readPacketFrame(): BytesBuffer {
    val length = this.readVarInt()
    return this.readBytes(length).wrap()
}