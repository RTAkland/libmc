/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.primitives

import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.stream.BytesBuffer

public object VarLongCodec : PacketCodec<Long> {
    override suspend fun encode(buffer: BytesBuffer, value: Long) {
        var v = value
        while (true) {
            if ((v and 0x7FL.inv()) == 0L) {
                buffer.writeByte(v.toByte())
                return
            }
            buffer.writeByte(((v and 0x7F) or 0x80).toByte())
            v = v ushr 7
        }
    }

    override suspend fun decode(buffer: BytesBuffer): Long {
        var numRead = 0
        var result = 0L
        var read: Byte
        do {
            read = buffer.readByte()
            val value = (read.toLong() and 0x7F)
            result = result or (value shl (7 * numRead))
            numRead++
            if (numRead > 10) throw IllegalArgumentException("VarLong is too big")
        } while ((read.toInt() and 0x80) != 0)
        return result
    }
}