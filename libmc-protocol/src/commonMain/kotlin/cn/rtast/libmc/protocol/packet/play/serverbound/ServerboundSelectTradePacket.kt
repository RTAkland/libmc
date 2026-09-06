/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.writeVarInt

public data class ServerboundSelectTradePacket(val selectedSlot: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSelectTradePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundSelectTradePacket) {
            buffer.writeVarInt(value.selectedSlot)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundSelectTradePacket =
            throw UnsupportedOperationException()
    }
}