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

public data class ServerboundAcceptTeleportationPacket(val teleportId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundAcceptTeleportationPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundAcceptTeleportationPacket) {
            buffer.writeVarInt(value.teleportId)
        }

        override fun decode(buffer: BytesBuffer): ServerboundAcceptTeleportationPacket =
            throw UnsupportedOperationException()
    }
}