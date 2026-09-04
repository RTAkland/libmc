/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.play

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.chat.util.readMinimalTextNbt
import cn.rtast.libmc.common.MinecraftPacket
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer

internal data class ClientboundDisconnectPlayPacket(
    val reason: String,
) : MinecraftPacket, PacketDirection.ClientboundPacket {
    override val packetId: Int = 0x28

    companion object Codec : PacketCodec<ClientboundDisconnectPlayPacket> {
        override fun encode(buffer: _Buffer, value: ClientboundDisconnectPlayPacket) {}
        override fun decode(buffer: _Buffer): ClientboundDisconnectPlayPacket {
            val reasonText = buffer.readMinimalTextNbt()
            return ClientboundDisconnectPlayPacket(reason = reasonText)
        }
    }
}