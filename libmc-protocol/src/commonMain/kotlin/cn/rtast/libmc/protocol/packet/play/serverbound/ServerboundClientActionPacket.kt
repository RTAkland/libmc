/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.registry.ClientAction

public data class ServerboundClientActionPacket(val action: ClientAction) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundClientActionPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundClientActionPacket) {
            buffer.writeVarInt(value.action.actionID)
        }

        override fun decode(buffer: BytesBuffer): ServerboundClientActionPacket =
            throw UnsupportedOperationException()
    }
}