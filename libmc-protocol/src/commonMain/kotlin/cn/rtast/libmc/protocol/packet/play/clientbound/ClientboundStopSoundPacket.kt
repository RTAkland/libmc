/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.sound.SoundCategory

public data class ClientboundStopSoundPacket(
    val flags: Byte,
    val source: SoundCategory?,
    val sound: Identifier?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundStopSoundPacket> {
        private const val MASK_HAS_SOURCE = 0x01
        private const val MASK_HAS_SOUND = 0x02

        override fun encode(buffer: BytesBuffer, value: ClientboundStopSoundPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundStopSoundPacket {
            val flags = buffer.readByte()
            val category =
                if ((flags.toInt() and MASK_HAS_SOURCE) != 0) SoundCategory.fromID(buffer.readVarInt()) else null
            val sound = if ((flags.toInt() and MASK_HAS_SOUND) != 0) buffer.readIdentifier() else null
            return ClientboundStopSoundPacket(flags, category, sound)
        }
    }
}