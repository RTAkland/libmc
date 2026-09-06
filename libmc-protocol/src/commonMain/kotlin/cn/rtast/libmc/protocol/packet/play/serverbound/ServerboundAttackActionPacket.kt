/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.writeVarInt

public data class ServerboundAttackActionPacket(val entityId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundAttackActionPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundAttackActionPacket) {
            buffer.writeVarInt(value.entityId)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundAttackActionPacket =
            throw UnsupportedOperationException()
    }
}