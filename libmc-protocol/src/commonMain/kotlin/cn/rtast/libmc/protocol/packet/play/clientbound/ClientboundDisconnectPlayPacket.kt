/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundDisconnectPlayPacket(val reason: NBTCompound) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundDisconnectPlayPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundDisconnectPlayPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundDisconnectPlayPacket {
            return ClientboundDisconnectPlayPacket(reason = buffer.readNetworkNBTCompound())
        }
    }
}