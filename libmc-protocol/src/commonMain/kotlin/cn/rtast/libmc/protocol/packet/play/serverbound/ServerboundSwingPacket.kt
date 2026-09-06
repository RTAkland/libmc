/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.player.Hand

public data class ServerboundSwingPacket(val hand: Hand) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSwingPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundSwingPacket) {
            buffer.writeVarInt(value.hand.id)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundSwingPacket =
            throw UnsupportedOperationException()
    }
}