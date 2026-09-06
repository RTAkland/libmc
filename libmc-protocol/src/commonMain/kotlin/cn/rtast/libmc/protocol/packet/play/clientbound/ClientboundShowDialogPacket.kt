/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.primitives.IdOrX
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readIdOrX
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundShowDialogPacket(val dialog: IdOrX<NBTCompound>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundShowDialogPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundShowDialogPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundShowDialogPacket {
            return ClientboundShowDialogPacket(buffer.readIdOrX { this.readNetworkNBTCompound() })
        }
    }
}