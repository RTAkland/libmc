/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.item.slot.ItemStack
import cn.rtast.libmc.protocol.protocol.game.item.slot.writeItemStack

public data class ServerboundSetCreativeModeSlotPacket(val slot: Short, val clickedItem: ItemStack) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSetCreativeModeSlotPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSetCreativeModeSlotPacket) {
            buffer.writeShort(value.slot)
            buffer.writeItemStack(value.clickedItem)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetCreativeModeSlotPacket =
            throw UnsupportedOperationException()
    }
}