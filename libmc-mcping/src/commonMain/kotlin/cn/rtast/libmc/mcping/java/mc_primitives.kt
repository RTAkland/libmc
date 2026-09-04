/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

package cn.rtast.libmc.mcping.java

import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer

internal object VarIntCodec : PacketCodec<Int> {
    override fun encode(buffer: _Buffer, value: Int) {
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

    override fun decode(buffer: _Buffer): Int {
        var value = 0
        var position = 0
        while (true) {
            val currentByte = buffer.readByte().toInt() and 0xFF
            value = value or ((currentByte and 0x7F) shl position)
            if ((currentByte and 0x80) == 0) break
            position += 7
            if (position >= 35) throw IllegalArgumentException("VarInt too long")
        }
        return value
    }
}

internal object McStringCodec : PacketCodec<String> {
    override fun encode(buffer: _Buffer, value: String) {
        val bytes = value.encodeToByteArray()
        VarIntCodec.encode(buffer, bytes.size)
        buffer.writeBytes(bytes)
    }

    override fun decode(buffer: _Buffer): String {
        val length = VarIntCodec.decode(buffer)
        val bytes = buffer.readBytes(length)
        return bytes.decodeToString()
    }
}

internal fun _Buffer.writeVarInt(value: Int) = VarIntCodec.encode(this, value)
internal fun _Buffer.readVarInt(): Int = VarIntCodec.decode(this)