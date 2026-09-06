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

public data class ClientboundSetSubtitleTextPacket(val subTitleText: NBTCompound) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetSubtitleTextPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetSubtitleTextPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetSubtitleTextPacket {
            return ClientboundSetSubtitleTextPacket(buffer.readNetworkNBTCompound())
        }
    }
}