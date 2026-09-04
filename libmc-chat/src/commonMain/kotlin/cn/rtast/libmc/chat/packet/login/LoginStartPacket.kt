/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.login

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.common.MinecraftPacket
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer
import cn.rtast.libmc.common.writeMcString
import cn.rtast.libmc.common.writeUuid
import kotlin.uuid.Uuid

internal data class LoginStartPacket(
    val username: String,
    val playerUuid: Uuid,
) : MinecraftPacket, PacketDirection.ServerboundPacket {
    override val packetId: Int = 0x00

    companion object Codec : PacketCodec<LoginStartPacket> {
        override fun encode(buffer: _Buffer, value: LoginStartPacket) {
            buffer.writeMcString(value.username)
            buffer.writeUuid(value.playerUuid)
        }

        override fun decode(buffer: _Buffer): LoginStartPacket = throw UnsupportedOperationException()
    }
}