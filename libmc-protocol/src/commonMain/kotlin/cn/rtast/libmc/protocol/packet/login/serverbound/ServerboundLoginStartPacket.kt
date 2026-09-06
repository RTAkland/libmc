/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.login.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeMcString
import cn.rtast.libmc.common.writeUuid
import kotlin.uuid.Uuid

public data class ServerboundLoginStartPacket(val username: String, val playerUuid: Uuid) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundLoginStartPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundLoginStartPacket) {
            buffer.writeMcString(value.username)
            buffer.writeUuid(value.playerUuid)
        }

        override fun decode(buffer: BytesBuffer): ServerboundLoginStartPacket = throw UnsupportedOperationException()
    }
}