/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundServerDataPacket(val motd: NBTCompound, val icon: ByteArray?) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundServerDataPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundServerDataPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundServerDataPacket {
            val motd = buffer.readNetworkNBTCompound()
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