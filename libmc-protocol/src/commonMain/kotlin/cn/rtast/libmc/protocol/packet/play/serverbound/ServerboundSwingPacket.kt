/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.player.Hand

public data class ServerboundSwingPacket(val hand: Hand) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSwingPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSwingPacket) {
            buffer.writeVarInt(value.hand.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSwingPacket =
            throw UnsupportedOperationException()
    }
}