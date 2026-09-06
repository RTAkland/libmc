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

public data class ClientboundSetBorderWarningDelayPacket(
    /**
     * In seconds as set by /worldborder warning time
     */
    val warningTime: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetBorderWarningDelayPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetBorderWarningDelayPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetBorderWarningDelayPacket {
            return ClientboundSetBorderWarningDelayPacket(buffer.readVarInt())
        }
    }
}