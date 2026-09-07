/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.network.BytesBuffer

public data class ClientboundServerDataPacket(val motd: TextComponent, val icon: ByteArray?) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundServerDataPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundServerDataPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundServerDataPacket {
            val motd = buffer.readTextComponent()
            val icon = buffer.readOptional { val length = readVarInt(); readBytes(length) }
            return ClientboundServerDataPacket(motd, icon)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ClientboundServerDataPacket
        if (motd != other.motd) return false
        if (!icon.contentEquals(other.icon)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = motd.hashCode()
        result = 31 * result + (icon?.contentHashCode() ?: 0)
        return result
    }
}