/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.primitives

import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.stream.BytesBuffer

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
