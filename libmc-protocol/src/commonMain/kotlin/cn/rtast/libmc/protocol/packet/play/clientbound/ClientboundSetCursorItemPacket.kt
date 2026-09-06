/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.inventory.Slot
import cn.rtast.libmc.protocol.protocol.game.inventory.readSlot

public data class ClientboundSetCursorItemPacket(val carriedItem: Slot) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetCursorItemPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetCursorItemPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetCursorItemPacket {
            return ClientboundSetCursorItemPacket(buffer.readSlot())
        }
    }
}