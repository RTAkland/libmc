/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.inventory.Slot
import cn.rtast.libmc.protocol.protocol.game.inventory.readSlot

public data class ClientboundContainerSetSlotPacket(
    val windowId: Int,
    val stateId: Int,
    val slot: Short,
    val slotData: Slot,
) : ClientboundPlayPacket {
    internal companion object Codec : PacketCodec<ClientboundContainerSetSlotPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundContainerSetSlotPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundContainerSetSlotPacket {
            val windowId = buffer.readVarInt()
            val stateId = buffer.readVarInt()
            val slot = buffer.readShort()
            val slotData = buffer.readSlot()
            return ClientboundContainerSetSlotPacket(windowId, stateId, slot, slotData)
        }
    }
}