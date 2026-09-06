/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.registry.report

import cn.rtast.libmc.common.*
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound
import cn.rtast.libmc.protocol.protocol.util.writeNetworkNBTCompound

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
                    buffer.writeNetworkNBTCompound(value.label.text)
                }
            }
            buffer.writeMcString(value.url)
        }

        override fun decode(buffer: BytesBuffer): ServerLink {
            val isBuiltin = buffer.readBoolean()
            val label = if (isBuiltin) ServerLinkLabel.Builtin(BuiltinServerLinkType.fromID(buffer.readVarInt()))
            else ServerLinkLabel.Custom(buffer.readNetworkNBTCompound())
            val url = buffer.readMcString()
            return ServerLink(label, url)
        }
    }
}

public sealed interface ServerLinkLabel {
    public data class Builtin(val type: BuiltinServerLinkType) : ServerLinkLabel
    public data class Custom(val text: NBTCompound) : ServerLinkLabel
}