/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.common.primitives

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec

public object McStringCodec : PacketCodec<String> {
    override suspend fun encode(buffer: BytesBuffer, value: String) {
        val bytes = value.encodeToByteArray()
        VarIntCodec.encode(buffer, bytes.size)
        buffer.writeBytes(bytes)
    }

    override suspend fun decode(buffer: BytesBuffer): String {
        val length = VarIntCodec.decode(buffer)
        val bytes = buffer.readBytes(length)
        return bytes.decodeToString()
    }
}

public suspend fun BytesBuffer.writeMcString(value: String): Unit = McStringCodec.encode(this, value)
public suspend fun BytesBuffer.readMcString(): String = McStringCodec.decode(this)
