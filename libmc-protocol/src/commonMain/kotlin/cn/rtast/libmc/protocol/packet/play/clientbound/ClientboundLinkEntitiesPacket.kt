/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ClientboundLinkEntitiesPacket(val attachedEntityId: Int, val holdingEntityId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundLinkEntitiesPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundLinkEntitiesPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundLinkEntitiesPacket {
            val attachedEntityId = buffer.readInt()
            val holdingEntityId = buffer.readInt()
            return ClientboundLinkEntitiesPacket(attachedEntityId, holdingEntityId)
        }
    }
}