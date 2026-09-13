/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.item.slot.Slot
import cn.rtast.libmc.protocol.protocol.game.item.slot.readSlot

public data class ClientboundSetPlayerInventorySlotPacket(val slot: Int, val slotData: Slot) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetPlayerInventorySlotPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetPlayerInventorySlotPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetPlayerInventorySlotPacket {
            val slot = buffer.readVarInt()
            val slotData = buffer.readSlot()
            return ClientboundSetPlayerInventorySlotPacket(slot, slotData)
        }
    }
}