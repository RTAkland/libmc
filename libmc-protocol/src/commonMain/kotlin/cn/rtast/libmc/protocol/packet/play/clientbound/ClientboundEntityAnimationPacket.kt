/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.Animations

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Entity_Animation
 */
public data class ClientboundEntityAnimationPacket(val entityId: Int, val animation: Animations) :
    ClientboundPlayPacket {
    public companion object Codec : PacketCodec<ClientboundEntityAnimationPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundEntityAnimationPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundEntityAnimationPacket {
            val entityId = buffer.readVarInt()
            val animation = Animations.fromID(buffer.readByte())
            return ClientboundEntityAnimationPacket(entityId, animation)
        }
    }
}