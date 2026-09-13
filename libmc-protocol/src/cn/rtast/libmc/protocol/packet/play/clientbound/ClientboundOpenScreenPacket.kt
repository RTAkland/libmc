/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.network.BytesBuffer

public data class ClientboundOpenScreenPacket(
    val windowId: Int,
    val windowType: Int,
    val title: TextComponent,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundOpenScreenPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundOpenScreenPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundOpenScreenPacket {
            val windowId = buffer.readVarInt()
            val windowType = buffer.readVarInt()
            val title = buffer.readTextComponent()
            return ClientboundOpenScreenPacket(windowId, windowType, title)
        }
    }
}