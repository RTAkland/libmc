/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.mcping

import cn.rtast.mcping.platform.PlatformBuffer
import cn.rtast.mcping.platform.PlatformReadChannel


internal fun PlatformBuffer.writeVarInt(value: Int) {
    var v = value
    while (true) {
        if ((v and 0x7F.inv()) == 0) {
            this.writeByte(v.toByte())
            return
        }
        this.writeByte(((v and 0x7F) or 0x80).toByte())
        v = v ushr 7
    }
}

internal fun PlatformReadChannel.readVarInt(): Int {
    var value = 0
    var position = 0
    while (true) {
        val currentByte = this.readByte().toInt() and 0xFF
        value = value or ((currentByte and 0x7F) shl position)
        if ((currentByte and 0x80) == 0) break
        position += 7
        if (position >= 35) throw IllegalArgumentException("VarInt too long")
    }
    return value
}

internal fun PlatformBuffer.writeMcString(value: String) {
    val bytes = value.encodeToByteArray()
    this.writeVarInt(bytes.size)
    this.writeBytes(bytes)
}

internal fun PlatformReadChannel.readMcString(): String {
    val length = this.readVarInt()
    val bytes = this.readBytes(length)
    return bytes.decodeToString()
}