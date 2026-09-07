/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeUuid
import kotlin.uuid.Uuid

public data class ServerboundTeleportToEntityPacket(val targetPlayer: Uuid) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundTeleportToEntityPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundTeleportToEntityPacket) {
            buffer.writeUuid(value.targetPlayer)
        }

        override fun decode(buffer: BytesBuffer): ServerboundTeleportToEntityPacket =
            throw UnsupportedOperationException()
    }
}