/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundSystemChatMessagePacket(val content: NBTCompound, val overlay: Boolean) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSystemChatMessagePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSystemChatMessagePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSystemChatMessagePacket {
            val content = buffer.readNetworkNBTCompound()
            val overlay = buffer.readBoolean()
            return ClientboundSystemChatMessagePacket(content, overlay)
        }
    }
}