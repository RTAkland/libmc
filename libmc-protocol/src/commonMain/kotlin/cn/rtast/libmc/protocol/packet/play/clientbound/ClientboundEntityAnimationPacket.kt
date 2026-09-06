/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.Animations

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Entity_Animation
 */
public data class ClientboundEntityAnimationPacket(val entityId: Int, val animation: Animations) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundEntityAnimationPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundEntityAnimationPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundEntityAnimationPacket {
            val entityId = buffer.readVarInt()
            val animation = Animations.fromID(buffer.readByte())
            return ClientboundEntityAnimationPacket(entityId, animation)
        }
    }
}