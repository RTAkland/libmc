/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.inventory.EquipmentEntry
import cn.rtast.libmc.protocol.protocol.game.inventory.EquipmentSlot
import cn.rtast.libmc.protocol.protocol.game.inventory.readSlot

public data class ClientboundSetEquipmentPacket(val entityId: Int, val equipment: List<EquipmentEntry>) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetEquipmentPacket> {
        private const val MASK_HAS_NEXT: Int = 0x80
        private const val MASK_SLOT_ID: Int = 0x7F

        override fun encode(buffer: BytesBuffer, value: ClientboundSetEquipmentPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetEquipmentPacket {
            val entityId = buffer.readVarInt()
            val equipment = ArrayList<EquipmentEntry>()
            while (true) {
                val rawSlot = buffer.readByte().toInt()
                val slotId = rawSlot and MASK_SLOT_ID
                val slot = EquipmentSlot.fromID(slotId)
                val item = buffer.readSlot()
                equipment.add(EquipmentEntry(slot, item))
                if ((rawSlot and MASK_HAS_NEXT) == 0) break
            }
            return ClientboundSetEquipmentPacket(entityId, equipment)
        }
    }
}