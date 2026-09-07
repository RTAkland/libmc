/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket

public data class ClientboundLinkEntitiesPacket(val attachedEntityId: Int, val holdingEntityId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundLinkEntitiesPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundLinkEntitiesPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundLinkEntitiesPacket {
            val attachedEntityId = buffer.readInt()
            val holdingEntityId = buffer.readInt()
            return ClientboundLinkEntitiesPacket(attachedEntityId, holdingEntityId)
        }
    }
}