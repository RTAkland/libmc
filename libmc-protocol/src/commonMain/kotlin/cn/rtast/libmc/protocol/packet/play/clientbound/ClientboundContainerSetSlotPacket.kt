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
import cn.rtast.libmc.protocol.protocol.game.item.slot.ItemStack
import cn.rtast.libmc.protocol.protocol.game.item.slot.readItemStack

public data class ClientboundContainerSetSlotPacket(
    val windowId: Int,
    val stateId: Int,
    val slot: Short,
    val itemStackData: ItemStack,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundContainerSetSlotPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundContainerSetSlotPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundContainerSetSlotPacket {
            val windowId = buffer.readVarInt()
            val stateId = buffer.readVarInt()
            val slot = buffer.readShort()
            val slotData = buffer.readItemStack()
            return ClientboundContainerSetSlotPacket(windowId, stateId, slot, slotData)
        }
    }
}