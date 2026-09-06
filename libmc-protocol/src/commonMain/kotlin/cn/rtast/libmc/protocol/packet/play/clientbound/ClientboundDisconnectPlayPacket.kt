/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundDisconnectPlayPacket(val reason: NBTCompound) : ClientboundPlayPacket {
    internal companion object Codec : PacketCodec<ClientboundDisconnectPlayPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundDisconnectPlayPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundDisconnectPlayPacket {
            return ClientboundDisconnectPlayPacket(reason = buffer.readNetworkNBTCompound())
        }
    }
}