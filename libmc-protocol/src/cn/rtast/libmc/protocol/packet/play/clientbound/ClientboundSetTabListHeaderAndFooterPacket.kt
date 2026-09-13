/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.network.BytesBuffer

public data class ClientboundSetTabListHeaderAndFooterPacket(val header: TextComponent, val footer: TextComponent) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetTabListHeaderAndFooterPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetTabListHeaderAndFooterPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetTabListHeaderAndFooterPacket {
            val header = buffer.readTextComponent()
            val footer = buffer.readTextComponent()
            return ClientboundSetTabListHeaderAndFooterPacket(header, footer)
        }
    }
}