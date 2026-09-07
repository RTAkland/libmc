/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readVarLong

public data class ClientboundSetBorderLeapSizePacket(
    val oldDiameter: Double,
    val newDiameter: Double,
    val speed: Long,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetBorderLeapSizePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetBorderLeapSizePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetBorderLeapSizePacket {
            val oldDiameter = buffer.readDouble()
            val newDiameter = buffer.readDouble()
            val speed = buffer.readVarLong()
            return ClientboundSetBorderLeapSizePacket(oldDiameter, newDiameter, speed)
        }
    }
}