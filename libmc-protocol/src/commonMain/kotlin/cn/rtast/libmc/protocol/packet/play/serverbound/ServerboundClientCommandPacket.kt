/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.player.action.PlayerActionStatus

public data class ServerboundClientCommandPacket(val action: PlayerActionStatus) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundClientCommandPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundClientCommandPacket) {
            buffer.writeVarInt(value.action.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundClientCommandPacket =
            throw UnsupportedOperationException()
    }
}