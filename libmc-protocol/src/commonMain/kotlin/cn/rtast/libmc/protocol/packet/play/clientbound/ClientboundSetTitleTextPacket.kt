/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundSetTitleTextPacket(val text: NBTCompound) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetTitleTextPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetTitleTextPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetTitleTextPacket {
            return ClientboundSetTitleTextPacket(buffer.readNetworkNBTCompound())
        }
    }
}