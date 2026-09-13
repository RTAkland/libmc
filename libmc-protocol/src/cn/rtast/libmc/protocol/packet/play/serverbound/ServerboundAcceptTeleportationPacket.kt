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

public data class ServerboundAcceptTeleportationPacket(val teleportId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundAcceptTeleportationPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundAcceptTeleportationPacket) {
            buffer.writeVarInt(value.teleportId)
        }

        override fun decode(buffer: BytesBuffer): ServerboundAcceptTeleportationPacket =
            throw UnsupportedOperationException()
    }
}