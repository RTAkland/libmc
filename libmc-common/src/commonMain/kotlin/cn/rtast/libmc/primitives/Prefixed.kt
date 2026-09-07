/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.primitives

import cn.rtast.libmc.stream.BytesBuffer


public suspend fun BytesBuffer.readPrefixedByteArray(): ByteArray {
    val length = this.readVarInt()
    val data = this.readBytes(length)
    return data
}

public suspend fun BytesBuffer.writePrefixedByteArray(data: ByteArray) {
    this.writeVarInt(data.size)
    this.writeBytes(data)
}

public suspend fun BytesBuffer.writeOptionalPrefixedByteArray(data: ByteArray?) {
    if (data != null) {
        this.writeBoolean(true)
        this.writeVarInt(data.size)
        this.writeBytes(data)
    } else this.writeBoolean(false)
}

public suspend fun BytesBuffer.readPrefixedStringArray(): List<String> {
    val length = readVarInt()
    val list = ArrayList<String>(length)
    repeat(length) { list.add(readMcString()) }
    return list
}

public suspend fun BytesBuffer.writePrefixedStringArray(value: List<String>) {
    writeVarInt(value.size)
    for (item in value) writeMcString(item)
}



public suspend inline fun <T> BytesBuffer.readPrefixed(reader: BytesBuffer.() -> T): List<T> {
    val count = this.readVarInt()
    require(count in 0..4096) {
        "Prefixed array count $count is invalid (expected 0..4096). " +
                "Stream offset is corrupted. Check Heightmaps/NBT encoding."
    }
    val list = ArrayList<T>(count)
    repeat(count) { _ -> list.add(this.reader()) }
    return list
}

public suspend inline fun <T> BytesBuffer.writePrefixed(list: List<T>, writer: BytesBuffer.(T) -> Unit) {
    this.writeVarInt(list.size)
    for (item in list) this.writer(item)
}