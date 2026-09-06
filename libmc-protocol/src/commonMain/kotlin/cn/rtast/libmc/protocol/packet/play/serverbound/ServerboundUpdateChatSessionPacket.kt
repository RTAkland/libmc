/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.writePrefixedByteArray
import cn.rtast.libmc.common.primitives.writeUuid
import kotlin.uuid.Uuid

public data class ServerboundUpdateChatSessionPacket(
    val sessionId: Uuid,
    /**
     * The time the play session key expires in epoch milliseconds.
     */
    val expiresAt: Long,
    /**
     * A byte array of an X.509-encoded public key. Get this from Mojang API
     * ref: https://minecraft.wiki/w/Mojang_API#Get_keypair_for_signature
     */
    val publicKey: ByteArray,
    /**
     * The signature consists of the player UUID, the key expiration timestamp,
     * and the public key data. These values are hashed using SHA-1 and signed using Mojang's private RSA key.
     */
    val keySignature: ByteArray,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundUpdateChatSessionPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundUpdateChatSessionPacket) {
            buffer.writeUuid(value.sessionId)
            buffer.writeLong(value.expiresAt)
            buffer.writePrefixedByteArray(value.publicKey)
            buffer.writePrefixedByteArray(value.keySignature)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundUpdateChatSessionPacket =
            throw UnsupportedOperationException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ServerboundUpdateChatSessionPacket

        if (expiresAt != other.expiresAt) return false
        if (sessionId != other.sessionId) return false
        if (!publicKey.contentEquals(other.publicKey)) return false
        if (!keySignature.contentEquals(other.keySignature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = expiresAt.hashCode()
        result = 31 * result + sessionId.hashCode()
        result = 31 * result + publicKey.contentHashCode()
        result = 31 * result + keySignature.contentHashCode()
        return result
    }
}