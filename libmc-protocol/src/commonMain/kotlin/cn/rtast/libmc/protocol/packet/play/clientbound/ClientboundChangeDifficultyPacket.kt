/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.protocol.registry.GameDifficulty

public data class ClientboundChangeDifficultyPacket(val difficulty: GameDifficulty, val locked: Boolean) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundChangeDifficultyPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundChangeDifficultyPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundChangeDifficultyPacket {
            val difficulty = GameDifficulty.fromID(buffer.readByte())
            val locked = buffer.readBoolean()
            return ClientboundChangeDifficultyPacket(difficulty, locked)
        }
    }
}