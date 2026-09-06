/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ClientboundSetBorderCenterPacket(val x: Double, val z: Double) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetBorderCenterPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetBorderCenterPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetBorderCenterPacket {
            val x = buffer.readDouble()
            val z = buffer.readDouble()
            return ClientboundSetBorderCenterPacket(x, z)
        }
    }
}