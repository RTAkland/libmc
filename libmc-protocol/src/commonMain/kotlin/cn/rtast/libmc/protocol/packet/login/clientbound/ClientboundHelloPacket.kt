/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.login.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readMcString
import cn.rtast.libmc.common.readPrefixedByteArray

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Encryption_Request
 */
public data class ClientboundHelloPacket(
    val serverId: String,
    val publicKey: ByteArray,
    val verifyToken: ByteArray,
    /**
     * Whether the client should attempt to authenticate through mojang servers.
     * ref: https://minecraft.wiki/w/Java_Edition_protocol/Encryption#Authentication
     */
    val shouldAuthenticate: Boolean,
) : ClientboundLoginPacket {
    internal companion object Codec : PacketCodec<ClientboundHelloPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundHelloPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundHelloPacket {
            val serverId = buffer.readMcString()
            val publicKey = buffer.readPrefixedByteArray()
            val verifyToken = buffer.readPrefixedByteArray()
            val shouldAuthenticate = buffer.readBoolean()
            return ClientboundHelloPacket(serverId, publicKey, verifyToken, shouldAuthenticate)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ClientboundHelloPacket

        if (shouldAuthenticate != other.shouldAuthenticate) return false
        if (serverId != other.serverId) return false
        if (!publicKey.contentEquals(other.publicKey)) return false
        if (!verifyToken.contentEquals(other.verifyToken)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = shouldAuthenticate.hashCode()
        result = 31 * result + serverId.hashCode()
        result = 31 * result + publicKey.contentHashCode()
        result = 31 * result + verifyToken.contentHashCode()
        return result
    }
}