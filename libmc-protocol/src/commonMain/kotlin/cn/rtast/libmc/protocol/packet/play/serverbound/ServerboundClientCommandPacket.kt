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
import cn.rtast.libmc.protocol.protocol.game.player.PlayerActionStatus

public data class ServerboundClientCommandPacket(val action: PlayerActionStatus) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundClientCommandPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundClientCommandPacket) {
            buffer.writeVarInt(value.action.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundClientCommandPacket =
            throw UnsupportedOperationException()
    }
}