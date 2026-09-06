/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readVarInt

public data class ClientboundSetRenderDistancePacket(
    /**
     * 2-32
     */
    val viewDistance: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetRenderDistancePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetRenderDistancePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetRenderDistancePacket {
            return ClientboundSetRenderDistancePacket(buffer.readVarInt())
        }
    }
}