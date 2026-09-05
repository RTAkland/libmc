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

public data class ServerboundAttackActionPacket(val entityId: Int) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundAttackActionPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundAttackActionPacket) {
            buffer.writeVarInt(value.entityId)
        }

        override fun decode(buffer: BytesBuffer): ServerboundAttackActionPacket =
            throw UnsupportedOperationException()
    }
}