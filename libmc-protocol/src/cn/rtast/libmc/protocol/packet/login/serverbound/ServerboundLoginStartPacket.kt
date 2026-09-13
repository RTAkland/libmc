/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.login.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.writeMcString
import cn.rtast.libmc.primitives.writeUuid
import kotlin.uuid.Uuid

public data class ServerboundLoginStartPacket(val username: String, val playerUuid: Uuid) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundLoginStartPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundLoginStartPacket) {
            buffer.writeMcString(value.username)
            buffer.writeUuid(value.playerUuid)
        }

        override fun decode(buffer: BytesBuffer): ServerboundLoginStartPacket =
            throw UnsupportedOperationException()
    }
}