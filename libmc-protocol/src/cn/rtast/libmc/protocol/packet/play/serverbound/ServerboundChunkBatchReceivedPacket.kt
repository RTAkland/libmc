/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket

public data class ServerboundChunkBatchReceivedPacket(val chunksPerTick: Float) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundChunkBatchReceivedPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundChunkBatchReceivedPacket) {
            buffer.writeFloat(value.chunksPerTick)
        }

        override fun decode(buffer: BytesBuffer): ServerboundChunkBatchReceivedPacket =
            throw UnsupportedOperationException()
    }
}