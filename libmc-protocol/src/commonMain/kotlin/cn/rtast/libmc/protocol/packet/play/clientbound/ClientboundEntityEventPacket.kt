/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket

public data class ClientboundEntityEventPacket(
    val entityId: Int,
    /**
     * to get status id,
     * see https://minecraft.wiki/w/Java_Edition_protocol/Entity_statuses#Entity_statuses
     */
    val status: Byte,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundEntityEventPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundEntityEventPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundEntityEventPacket {
            val entityId = buffer.readInt()
            val status = buffer.readByte()
            return ClientboundEntityEventPacket(entityId, status)
        }
    }
}