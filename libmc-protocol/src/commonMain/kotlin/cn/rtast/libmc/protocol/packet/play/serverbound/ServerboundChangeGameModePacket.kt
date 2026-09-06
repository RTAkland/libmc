/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.GameMode

public data class ServerboundChangeGameModePacket(val gameMode: GameMode) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundChangeGameModePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundChangeGameModePacket) {
            require(value.gameMode != GameMode.Unknown && value.gameMode != GameMode.Undefined)
            buffer.writeVarInt(value.gameMode.id.toInt())
        }

        override fun decode(buffer: BytesBuffer): ServerboundChangeGameModePacket =
            throw UnsupportedOperationException()
    }
}