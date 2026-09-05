/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundSystemChatMessagePacket(val content: NBTCompound, val overlay: Boolean) :
    ClientboundPlayPacket {
    public companion object Codec : PacketCodec<ClientboundSystemChatMessagePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSystemChatMessagePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSystemChatMessagePacket {
            val content = buffer.readNetworkNBTCompound()
            val overlay = buffer.readBoolean()
            return ClientboundSystemChatMessagePacket(content, overlay)
        }
    }
}