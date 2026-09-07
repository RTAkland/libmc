/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundConfigurationShowDialogPacket(val dialog: NBTCompound) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundConfigurationShowDialogPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundConfigurationShowDialogPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundConfigurationShowDialogPacket {
            return ClientboundConfigurationShowDialogPacket(buffer.readNetworkNBTCompound())
        }
    }
}