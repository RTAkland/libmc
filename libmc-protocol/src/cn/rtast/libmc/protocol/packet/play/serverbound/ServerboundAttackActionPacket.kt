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

public data class ServerboundAttackActionPacket(val entityId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundAttackActionPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundAttackActionPacket) {
            buffer.writeVarInt(value.entityId)
        }

        override fun decode(buffer: BytesBuffer): ServerboundAttackActionPacket =
            throw UnsupportedOperationException()
    }
}