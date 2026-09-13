/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.item.slot.Slot
import cn.rtast.libmc.protocol.protocol.game.item.slot.readSlot

public data class ClientboundSetCursorItemPacket(val carriedItem: Slot) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetCursorItemPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetCursorItemPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetCursorItemPacket =
            ClientboundSetCursorItemPacket(buffer.readSlot())
    }
}