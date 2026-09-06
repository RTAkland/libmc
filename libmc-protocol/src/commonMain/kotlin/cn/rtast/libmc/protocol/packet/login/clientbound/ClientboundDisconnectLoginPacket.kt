/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.login.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundDisconnectLoginPacket(val reason: NBTCompound) : ClientboundLoginPacket {
    internal companion object Codec : PacketCodec<ClientboundDisconnectLoginPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundDisconnectLoginPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundDisconnectLoginPacket {
            return ClientboundDisconnectLoginPacket(reason = buffer.readNetworkNBTCompound())
        }
    }
}