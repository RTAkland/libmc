/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.IdOrX
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readIdOrX
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundShowDialogPacket(val dialog: IdOrX<NBTCompound>) : ClientboundPlayPacket {
    internal companion object Codec : PacketCodec<ClientboundShowDialogPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundShowDialogPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundShowDialogPacket {
            return ClientboundShowDialogPacket(buffer.readIdOrX { this.readNetworkNBTCompound() })
        }
    }
}