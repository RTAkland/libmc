/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ClientboundSetBorderSizePacket(
    /**
     * Length of a single side of the world border, in meters
     */
    val diameter: Double,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetBorderSizePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetBorderSizePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetBorderSizePacket {
            return ClientboundSetBorderSizePacket(buffer.readDouble())
        }
    }
}