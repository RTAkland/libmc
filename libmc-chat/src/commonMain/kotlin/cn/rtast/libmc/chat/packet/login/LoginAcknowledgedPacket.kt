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

internal data class LoginAcknowledgedPacket(
    override val packetId: Int = 0x03,
) : MinecraftPacket, PacketDirection.ServerboundPacket {

    companion object Codec : PacketCodec<LoginAcknowledgedPacket> {
        override fun encode(buffer: _Buffer, value: LoginAcknowledgedPacket) {}
        override fun decode(buffer: _Buffer): LoginAcknowledgedPacket = throw UnsupportedOperationException()
    }
}