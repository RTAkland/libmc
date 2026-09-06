/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.container.ContainerButton
import cn.rtast.libmc.protocol.protocol.game.inventory.ChangedSlot
import cn.rtast.libmc.protocol.protocol.game.inventory.HashedSlot
import cn.rtast.libmc.protocol.protocol.game.inventory.InventoryClickType

public data class ServerboundContainerClickPacket(
    val windowId: Int,
    val stateId: Int,
    val slot: Short,
    val button: Byte,
    val mode: Int,
    val changedSlots: List<ChangedSlot>,
    val carriedItem: HashedSlot,
) : MinecraftPacket {
    public constructor(
        windowId: Int,
        stateId: Int,
        slot: Short,
        mouseButton: ContainerButton.Mouse,
        changedSlots: List<ChangedSlot>,
        carriedItem: HashedSlot,
    ) : this(
        windowId = windowId,
        stateId = stateId,
        slot = slot,
        button = mouseButton.id,
        mode = InventoryClickType.PICKUP.id,
        changedSlots = changedSlots,
        carriedItem = carriedItem
    )

    public constructor(
        windowId: Int,
        stateId: Int,
        slot: Short,
        swapButton: ContainerButton.Swap,
        changedSlots: List<ChangedSlot>,
        carriedItem: HashedSlot,
    ) : this(
        windowId = windowId,
        stateId = stateId,
        slot = slot,
        button = swapButton.id,
        mode = InventoryClickType.SWAP.id,
        changedSlots = changedSlots,
        carriedItem = carriedItem
    )

    internal companion object Codec : PacketCodec<ServerboundContainerClickPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundContainerClickPacket) {
            buffer.writeVarInt(value.windowId)
            buffer.writeVarInt(value.stateId)
            buffer.writeShort(value.slot)
            buffer.writeByte(value.button)
        }

        override fun decode(buffer: BytesBuffer): ServerboundContainerClickPacket =
            throw UnsupportedOperationException()
    }
}