/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.IdOrX
import cn.rtast.libmc.primitives.readIdOrX
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.protocol.protocol.game.chat.InlineChatType
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readInlineChatType
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.network.BytesBuffer

public data class ClientboundDisguisedChatMessagePacket(
    val message: TextComponent,
    val chatType: IdOrX<InlineChatType>,
    val senderName: TextComponent,
    val targetName: TextComponent?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundDisguisedChatMessagePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundDisguisedChatMessagePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundDisguisedChatMessagePacket {
            val message = buffer.readTextComponent()
            val chatType = buffer.readIdOrX { readInlineChatType() }
            val senderName = buffer.readTextComponent()
            val targetName = buffer.readOptional { readTextComponent() }
            return ClientboundDisguisedChatMessagePacket(message, chatType, senderName, targetName)
        }
    }
}