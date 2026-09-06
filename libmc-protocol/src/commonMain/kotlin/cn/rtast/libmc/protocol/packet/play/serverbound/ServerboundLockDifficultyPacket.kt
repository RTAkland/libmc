/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

/**
 * Must have at least op level 2 to use.
 * Appears to only be used on singleplayer; the difficulty buttons are still disabled in multiplayer.
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Lock_Difficulty
 */
public data class ServerboundLockDifficultyPacket(val locked: Boolean) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundLockDifficultyPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundLockDifficultyPacket) {
            buffer.writeBoolean(value.locked)
        }

        override fun decode(buffer: BytesBuffer): ServerboundLockDifficultyPacket =
            throw UnsupportedOperationException()
    }
}