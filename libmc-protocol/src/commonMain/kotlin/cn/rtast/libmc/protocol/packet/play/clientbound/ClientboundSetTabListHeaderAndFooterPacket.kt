/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundSetTabListHeaderAndFooterPacket(val header: NBTCompound, val footer: NBTCompound) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetTabListHeaderAndFooterPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetTabListHeaderAndFooterPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetTabListHeaderAndFooterPacket {
            val header = buffer.readNetworkNBTCompound()
            val footer = buffer.readNetworkNBTCompound()
            return ClientboundSetTabListHeaderAndFooterPacket(header, footer)
        }
    }
}