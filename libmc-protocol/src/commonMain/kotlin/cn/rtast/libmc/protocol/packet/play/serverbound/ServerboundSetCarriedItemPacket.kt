/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ServerboundSetCarriedItemPacket(val slot: Short) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSetCarriedItemPacket> {
        private const val MIN_SLOT: Short = 0
        private const val MAX_SLOT: Short = 8
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundSetCarriedItemPacket) {
            require(value.slot in MIN_SLOT..MAX_SLOT) { "Carried item slot ${value.slot} must be between $MIN_SLOT and $MAX_SLOT" }
            buffer.writeShort(value.slot)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundSetCarriedItemPacket =
            throw UnsupportedOperationException()
    }
}