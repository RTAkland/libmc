/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.util

import cn.rtast.libmc.common._Buffer

/**
 * tmp
 */
internal fun _Buffer.writeMinimalTextNbt(text: String) {
    writeByte(0x0A)
    writeByte(0x08)
    val keyBytes = "text".encodeToByteArray()
    writeShort(keyBytes.size.toShort())
    writeBytes(keyBytes)
    val valBytes = text.encodeToByteArray()
    require(valBytes.size <= 32767)
    writeShort(valBytes.size.toShort())
    writeBytes(valBytes)
    writeByte(0x00)
}

internal fun _Buffer.readMinimalTextNbt(): String {
    val rootTagType = readByte().toInt()
    if (rootTagType != 0x0A) return ""
    var resultText = ""
    while (true) {
        val tagType = readByte().toInt()
        if (tagType == 0x00) break
        val keyLength = readShort().toInt()
        val key = readBytes(keyLength).decodeToString()
        if (tagType == 0x08 && key == "text") {
            val valLength = readShort().toInt()
            resultText = readBytes(valLength).decodeToString()
        } else break
    }
    return resultText
}