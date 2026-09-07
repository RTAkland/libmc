/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket

public data class ClientboundSetTickingStatePacket(val tickRate: Float, val isFrozen: Boolean) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetTickingStatePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetTickingStatePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetTickingStatePacket {
            val tickRate = buffer.readFloat()
            val isFrozen = buffer.readBoolean()
            return ClientboundSetTickingStatePacket(tickRate, isFrozen)
        }
    }
}