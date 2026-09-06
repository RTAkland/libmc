/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.login.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writePrefixedByteArray

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Encryption
 */
public data class ServerboundKeyPacket(val sharedSecret: ByteArray, val verifyToken: ByteArray) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundKeyPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundKeyPacket) {
            buffer.writePrefixedByteArray(value.sharedSecret)
            buffer.writePrefixedByteArray(value.verifyToken)
        }

        override fun decode(buffer: BytesBuffer): ServerboundKeyPacket =
            throw UnsupportedOperationException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ServerboundKeyPacket

        if (!sharedSecret.contentEquals(other.sharedSecret)) return false
        if (!verifyToken.contentEquals(other.verifyToken)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sharedSecret.contentHashCode()
        result = 31 * result + verifyToken.contentHashCode()
        return result
    }
}