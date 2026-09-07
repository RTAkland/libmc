/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.GameMode

public data class ServerboundChangeGameModePacket(val gameMode: GameMode) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundChangeGameModePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundChangeGameModePacket) {
            require(value.gameMode != GameMode.Unknown && value.gameMode != GameMode.Undefined)
            buffer.writeVarInt(value.gameMode.id.toInt())
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundChangeGameModePacket =
            throw UnsupportedOperationException()
    }
}