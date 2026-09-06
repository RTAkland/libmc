/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.inventory.Slot
import cn.rtast.libmc.protocol.protocol.game.inventory.writeSlot

public data class ServerboundSetCreativeModeSlotPacket(val slot: Short, val clickedItem: Slot) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSetCreativeModeSlotPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSetCreativeModeSlotPacket) {
            buffer.writeShort(value.slot)
            buffer.writeSlot(value.clickedItem)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetCreativeModeSlotPacket =
            throw UnsupportedOperationException()
    }
}