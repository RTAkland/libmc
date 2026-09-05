/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.player.PlayerPositionFlag

public data class ServerboundSetPlayerMovementFlagPacket(val flags: PlayerPositionFlag) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundSetPlayerMovementFlagPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSetPlayerMovementFlagPacket) {
            buffer.writeByte(value.flags.flag)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetPlayerMovementFlagPacket =
            throw UnsupportedOperationException()
    }
}