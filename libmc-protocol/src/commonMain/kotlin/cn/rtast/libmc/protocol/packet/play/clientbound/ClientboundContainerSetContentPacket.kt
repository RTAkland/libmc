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

public data class ClientboundContainerSetContentPacket(
    val windowId: Int,
    /**
     * A server-managed sequence number used to avoid desynchronization
     * see https://minecraft.wiki/w/Java_Edition_protocol/Packets#Click_Container
     */
    val stateId: Int,
    val slotData: List<Slot>,
    val carriedItem: Slot,
) : ClientboundPlayPacket {
    internal companion object Codec : PacketCodec<ClientboundContainerSetContentPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundContainerSetContentPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundContainerSetContentPacket {
            val windowId = buffer.readVarInt()
            val stateId = buffer.readVarInt()
            val slotDataCount = buffer.readVarInt()
            val slotData = ArrayList<Slot>(slotDataCount)
            repeat(slotDataCount) { slotData.add(buffer.readSlot()) }
            val carriedItem = buffer.readSlot()
            return ClientboundContainerSetContentPacket(windowId, stateId, slotData, carriedItem)
        }
    }
}