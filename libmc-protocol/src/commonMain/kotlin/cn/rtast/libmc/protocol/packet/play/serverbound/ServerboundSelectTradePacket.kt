/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt

public data class ServerboundSelectTradePacket(val selectedSlot: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSelectTradePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSelectTradePacket) {
            buffer.writeVarInt(value.selectedSlot)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSelectTradePacket =
            throw UnsupportedOperationException()
    }
}