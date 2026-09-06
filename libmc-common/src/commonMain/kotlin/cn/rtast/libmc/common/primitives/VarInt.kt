/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.common.primitives

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec

public object VarIntCodec : PacketCodec<Int> {
    override suspend fun encode(buffer: BytesBuffer, value: Int) {
        var v = value
        while (true) {
            if ((v and 0x7F.inv()) == 0) {
                buffer.writeByte(v.toByte())
                return
            }
            buffer.writeByte(((v and 0x7F) or 0x80).toByte())
            v = v ushr 7
        }
    }

    override suspend fun decode(buffer: BytesBuffer): Int {
        var numRead = 0
        var result = 0
        var read: Byte
        do {
            read = buffer.readByte()
            val value = (read.toInt() and 0x7F)
            result = result or (value shl (7 * numRead))
            numRead++
            if (numRead > 5) throw IllegalArgumentException("VarInt is too big")
        } while ((read.toInt() and 0x80) != 0)
        return result
    }
}

public suspend fun BytesBuffer.writeVarInt(value: Int): Unit = VarIntCodec.encode(this, value)
public suspend fun BytesBuffer.readVarInt(): Int = VarIntCodec.decode(this)

public suspend fun BytesBuffer.writeVarLong(value: Long): Unit = VarLongCodec.encode(this, value)
public suspend fun BytesBuffer.readVarLong(): Long = VarLongCodec.decode(this)
