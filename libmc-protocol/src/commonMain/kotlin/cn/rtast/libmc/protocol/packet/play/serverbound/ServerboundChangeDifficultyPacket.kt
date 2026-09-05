/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.registry.GameDifficulty

public data class ServerboundChangeDifficultyPacket(val newDifficulty: GameDifficulty) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundChangeDifficultyPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundChangeDifficultyPacket) {
            buffer.writeByte(value.newDifficulty.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundChangeDifficultyPacket =
            throw UnsupportedOperationException()
    }
}