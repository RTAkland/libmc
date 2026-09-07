/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundSetSubtitleTextPacket(val subTitleText: NBTCompound) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetSubtitleTextPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetSubtitleTextPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetSubtitleTextPacket {
            return ClientboundSetSubtitleTextPacket(buffer.readNetworkNBTCompound())
        }
    }
}