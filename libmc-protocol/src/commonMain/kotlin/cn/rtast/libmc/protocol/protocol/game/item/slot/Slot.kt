/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.item.slot

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.data.component.DataComponent
import cn.rtast.libmc.protocol.registry.readDataComponent
import cn.rtast.libmc.protocol.registry.writeDataComponent

public typealias ItemStack = Slot

public data class Slot(
    val count: Int,
    val itemId: Int?,
    val componentsToAdd: List<DataComponent>,
    val componentsToRemove: List<Int>,
) {
    val isEmpty: Boolean get() = count <= 0

    public companion object {
        public val EMPTY: Slot = Slot(0, null, emptyList(), emptyList())
    }
}

internal fun BytesBuffer.readSlot(): Slot {
    val count = readVarInt()
    if (count <= 0) return Slot.EMPTY
    val itemId = readVarInt()
    val addCount = readVarInt()
    val removeCount = readVarInt()
    val componentsToAdd = ArrayList<DataComponent>(addCount)
    repeat(addCount) {
        val componentId = readVarInt()
        componentsToAdd.add(readDataComponent(componentId))
    }
    val componentsToRemove = ArrayList<Int>(removeCount)
    repeat(removeCount) { componentsToRemove.add(readVarInt()) }
    return Slot(count, itemId, componentsToAdd, componentsToRemove)
}

internal fun BytesBuffer.writeSlot(slot: Slot) {
    if (slot.isEmpty || slot.itemId == null) {
        writeVarInt(0)
        return
    }
    writeVarInt(slot.count)
    writeVarInt(slot.itemId)
    writeVarInt(slot.componentsToAdd.size)
    writeVarInt(slot.componentsToRemove.size)
    for (component in slot.componentsToAdd) writeDataComponent(component)
    for (typeId in slot.componentsToRemove) writeVarInt(typeId)
}