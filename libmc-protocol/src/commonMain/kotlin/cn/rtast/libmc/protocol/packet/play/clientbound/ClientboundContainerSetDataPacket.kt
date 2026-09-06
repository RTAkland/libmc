/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readVarInt

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Container_Property
 */
public data class ClientboundContainerSetDataPacket(
    val windowId: Int,
    val property: Short,
    val value: Short,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundContainerSetDataPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundContainerSetDataPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundContainerSetDataPacket {
            val windowId = buffer.readVarInt()
            val property = buffer.readShort()
            val value = buffer.readShort()
            return ClientboundContainerSetDataPacket(windowId, property, value)
        }
    }
}