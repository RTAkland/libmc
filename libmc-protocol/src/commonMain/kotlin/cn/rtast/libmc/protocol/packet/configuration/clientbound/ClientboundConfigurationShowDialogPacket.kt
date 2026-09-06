/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundConfigurationShowDialogPacket(val dialog: NBTCompound) : ClientboundConfigurationPacket {
    public companion object Codec : PacketCodec<ClientboundConfigurationShowDialogPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundConfigurationShowDialogPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundConfigurationShowDialogPacket {
            return ClientboundConfigurationShowDialogPacket(buffer.readNetworkNBTCompound())
        }
    }
}