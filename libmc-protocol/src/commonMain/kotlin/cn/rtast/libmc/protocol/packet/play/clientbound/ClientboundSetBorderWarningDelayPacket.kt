/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.readVarInt

public data class ClientboundSetBorderWarningDelayPacket(
    /**
     * In seconds as set by /worldborder warning time
     */
    val warningTime: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetBorderWarningDelayPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetBorderWarningDelayPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetBorderWarningDelayPacket {
            return ClientboundSetBorderWarningDelayPacket(buffer.readVarInt())
        }
    }
}