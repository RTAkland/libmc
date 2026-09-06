/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.login.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.writeMcString
import cn.rtast.libmc.common.primitives.writeUuid
import kotlin.uuid.Uuid

public data class ServerboundLoginStartPacket(val username: String, val playerUuid: Uuid) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundLoginStartPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundLoginStartPacket) {
            buffer.writeMcString(value.username)
            buffer.writeUuid(value.playerUuid)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundLoginStartPacket =
            throw UnsupportedOperationException()
    }
}