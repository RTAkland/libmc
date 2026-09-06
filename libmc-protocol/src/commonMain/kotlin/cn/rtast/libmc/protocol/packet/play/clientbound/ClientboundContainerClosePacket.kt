/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Close_Container
 * This is the ID of the window that was closed. 0 for inventory.
 */
public data class ClientboundContainerClosePacket(val windowId: Int) : ClientboundPlayPacket {
    internal companion object Codec : PacketCodec<ClientboundContainerClosePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundContainerClosePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundContainerClosePacket {
            return ClientboundContainerClosePacket(buffer.readVarInt())
        }
    }
}