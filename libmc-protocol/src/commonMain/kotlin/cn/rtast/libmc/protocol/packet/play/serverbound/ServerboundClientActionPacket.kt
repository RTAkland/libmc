/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.registry.ClientAction

public data class ServerboundClientActionPacket(val action: ClientAction) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundClientActionPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundClientActionPacket) {
            buffer.writeVarInt(value.action.actionID)
        }

        override fun decode(buffer: BytesBuffer): ServerboundClientActionPacket =
            throw UnsupportedOperationException()
    }
}