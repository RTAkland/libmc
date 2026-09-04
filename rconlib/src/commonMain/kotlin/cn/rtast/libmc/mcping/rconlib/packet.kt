/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.mcping.rconlib

import cn.rtast.libmc.common.ByteOrder
import cn.rtast.libmc.common._Buffer
import cn.rtast.libmc.common._ReadChannel
import cn.rtast.libmc.common._WriteChannel

internal abstract class Packet {
    abstract val requestId: Int
    abstract val type: Int
    abstract val payload: String

    protected val payloadBytes: ByteArray by lazy { payload.encodeToByteArray() }

    val payloadLength: Int
        get() = payloadBytes.size

    open val length: Int
        get() = 4 + 4 + payloadLength + 2

    fun writePayload(buf: _Buffer) {
        buf.writeInt(length, ByteOrder.LITTLE_ENDIAN)
        buf.writeInt(requestId, ByteOrder.LITTLE_ENDIAN)
        buf.writeInt(type, ByteOrder.LITTLE_ENDIAN)
        buf.writeBytes(payloadBytes)
        buf.writeNull()
        buf.writeNull()
    }

    companion object Codec {
        fun decode(channel: _ReadChannel): ResponsePacket {
            val length = channel.readInt(ByteOrder.LITTLE_ENDIAN)
            val requestId = channel.readInt(ByteOrder.LITTLE_ENDIAN)
            val type = channel.readInt(ByteOrder.LITTLE_ENDIAN)
            val payloadBuffer = _Buffer()
            while (true) {
                val b = channel.readByte()
                if (b == 0x00.toByte()) break
                payloadBuffer.writeByte(b)
            }
            channel.readByte()  // consume last null byte
            return ResponsePacket(length, requestId, type, payloadBuffer.toByteArray().decodeToString())
        }
    }
}

internal data class AuthPacket(
    override val payload: String,
    override val requestId: Int = 1,
) : Packet() {
    override val type: Int = PacketType.AUTH
}

internal data class ExecCommandPacket(
    override val payload: String,
    override val requestId: Int,
) : Packet() {
    override val type: Int = PacketType.EXEC_COMMAND
}

internal data class ResponsePacket(
    override val length: Int,
    override val requestId: Int,
    override val type: Int,
    override val payload: String,
) : Packet()


internal fun _WriteChannel.sendPacket(packet: Packet) {
    val buf = _Buffer()
    packet.writePayload(buf)
    writeFully(buf.toByteArray())
    flush()
}

internal fun _ReadChannel.readPacket(): ResponsePacket = Packet.decode(this)