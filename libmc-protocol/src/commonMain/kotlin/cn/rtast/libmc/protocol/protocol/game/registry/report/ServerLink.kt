/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.registry.report

import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeMcString
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.writeTextComponent
import cn.rtast.libmc.network.BytesBuffer

public data class ServerLink(val label: ServerLinkLabel, val url: String) {
    internal companion object Codec : PacketCodec<ServerLink> {
        override fun encode(buffer: BytesBuffer, value: ServerLink) {
            when (value.label) {
                is ServerLinkLabel.Builtin -> {
                    buffer.writeBoolean(true)
                    buffer.writeVarInt(value.label.type.id)
                }

                is ServerLinkLabel.Custom -> {
                    buffer.writeBoolean(false)
                    buffer.writeTextComponent(value.label.text)
                }
            }
            buffer.writeMcString(value.url)
        }

        override fun decode(buffer: BytesBuffer): ServerLink {
            val isBuiltin = buffer.readBoolean()
            val label = if (isBuiltin) ServerLinkLabel.Builtin(BuiltinServerLinkType.fromID(buffer.readVarInt()))
            else ServerLinkLabel.Custom(buffer.readTextComponent())
            val url = buffer.readMcString()
            return ServerLink(label, url)
        }
    }
}

public sealed interface ServerLinkLabel {
    public data class Builtin(val type: BuiltinServerLinkType) : ServerLinkLabel
    public data class Custom(val text: TextComponent) : ServerLinkLabel
}