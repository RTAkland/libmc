/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Container_Property
 */
public data class ClientboundContainerSetDataPacket(
    val windowId: Int,
    val property: Short,
    val value: Short,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundContainerSetDataPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundContainerSetDataPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundContainerSetDataPacket {
            val windowId = buffer.readVarInt()
            val property = buffer.readShort()
            val value = buffer.readShort()
            return ClientboundContainerSetDataPacket(windowId, property, value)
        }
    }
}