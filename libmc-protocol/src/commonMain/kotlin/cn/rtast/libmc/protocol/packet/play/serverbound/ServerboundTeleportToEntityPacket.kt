/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.writeUuid
import kotlin.uuid.Uuid

public data class ServerboundTeleportToEntityPacket(val targetPlayer: Uuid) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundTeleportToEntityPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundTeleportToEntityPacket) {
            buffer.writeUuid(value.targetPlayer)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundTeleportToEntityPacket =
            throw UnsupportedOperationException()
    }
}