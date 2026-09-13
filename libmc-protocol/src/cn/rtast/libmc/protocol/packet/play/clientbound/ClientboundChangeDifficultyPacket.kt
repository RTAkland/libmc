/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.registry.GameDifficulty

public data class ClientboundChangeDifficultyPacket(val difficulty: GameDifficulty, val locked: Boolean) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundChangeDifficultyPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundChangeDifficultyPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundChangeDifficultyPacket {
            val difficulty = GameDifficulty.fromID(buffer.readByte())
            val locked = buffer.readBoolean()
            return ClientboundChangeDifficultyPacket(difficulty, locked)
        }
    }
}