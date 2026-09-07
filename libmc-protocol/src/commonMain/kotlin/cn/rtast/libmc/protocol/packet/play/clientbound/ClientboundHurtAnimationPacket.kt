/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readVarInt

public data class ClientboundHurtAnimationPacket(val entityId: Int, val yaw: Float) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundHurtAnimationPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundHurtAnimationPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundHurtAnimationPacket {
            val entityId = buffer.readVarInt()
            val yaw = buffer.readFloat()
            return ClientboundHurtAnimationPacket(entityId, yaw)
        }
    }
}