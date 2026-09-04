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
import cn.rtast.libmc.common.readMcString

internal data class ClientboundDisconnectLoginPacket(
    val reason: String,
) : MinecraftPacket, PacketDirection.ClientboundPacket {
    override val packetId: Int = 0x00

    companion object Codec : PacketCodec<ClientboundDisconnectLoginPacket> {
        override fun encode(buffer: _Buffer, value: ClientboundDisconnectLoginPacket) {}
        override fun decode(buffer: _Buffer): ClientboundDisconnectLoginPacket {
            val reasonJson = buffer.readMcString()
            return ClientboundDisconnectLoginPacket(reason = reasonJson)
        }
    }
}