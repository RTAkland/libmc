/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.item.slot.ItemStack
import cn.rtast.libmc.protocol.protocol.game.item.slot.readItemStack

public data class ClientboundContainerSetContentPacket(
    val windowId: Int,
    val stateId: Int,
    val itemStackData: List<ItemStack>,
    val carriedItem: ItemStack?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundContainerSetContentPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundContainerSetContentPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundContainerSetContentPacket {
            val windowId = buffer.readVarInt()
            val stateId = buffer.readVarInt()
            val slotData = buffer.readPrefixed { readItemStack() }
//            val carriedItem = buffer.readSlot()  // TODO FIX ME
            return ClientboundContainerSetContentPacket(windowId, stateId, slotData, null)
        }
    }
}