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
import cn.rtast.libmc.protocol.protocol.game.inventory.equipment.EntityEquipment
import cn.rtast.libmc.protocol.protocol.game.inventory.equipment.EntityEquipmentSlot
import cn.rtast.libmc.protocol.protocol.game.item.slot.readSlot

public data class ClientboundSetEquipmentPacket(val entityId: Int, val equipments: List<EntityEquipment>) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetEquipmentPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetEquipmentPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetEquipmentPacket {
            val entityId = buffer.readVarInt()
            val equipments = ArrayList<EntityEquipment>()
            while (true) {
                val rawByte = buffer.readByte().toInt()
                val hasNext = (rawByte and 0x80) != 0
                val slotId = rawByte and 0x7F
                val slot = EntityEquipmentSlot.fromID(slotId)
                val item = buffer.readSlot()
                equipments.add(EntityEquipment(slot, item))
                if (!hasNext) break
            }
            return ClientboundSetEquipmentPacket(entityId, equipments)
        }
    }
}