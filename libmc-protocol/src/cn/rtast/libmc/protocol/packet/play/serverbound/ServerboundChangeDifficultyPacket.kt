/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.registry.GameDifficulty

public data class ServerboundChangeDifficultyPacket(val newDifficulty: GameDifficulty) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundChangeDifficultyPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundChangeDifficultyPacket) {
            buffer.writeByte(value.newDifficulty.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundChangeDifficultyPacket =
            throw UnsupportedOperationException()
    }
}