/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.inventory

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.readPrefixedByteArray
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.common.writeVarInt

public data class Slot(
    val count: Int,
    val itemId: Int?,
    val componentsToAdd: List<DataComponentToAdd>,
    val componentsToRemove: List<Int>,
) {
    public data class DataComponentToAdd(val typeId: Int, val data: ByteArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as DataComponentToAdd

            if (typeId != other.typeId) return false
            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = typeId
            result = 31 * result + data.contentHashCode()
            return result
        }
    }

    public val isEmpty: Boolean get() = count <= 0

    public companion object {
        /**
         * air
         */
        public val EMPTY: Slot = Slot(
            count = 0,
            itemId = null,
            componentsToAdd = emptyList(),
            componentsToRemove = emptyList()
        )
    }
}

internal fun BytesBuffer.writeSlot(value: Slot) {
    if (value.isEmpty) {
        writeVarInt(0)
        return
    }
    val itemId = requireNotNull(value.itemId) { "itemId must not be null when slot is not empty" }
    writeVarInt(value.count)
    writeVarInt(itemId)
    writeVarInt(value.componentsToAdd.size)
    writeVarInt(value.componentsToRemove.size)
    for ((typeId, data) in value.componentsToAdd) {
        writeVarInt(typeId)
        writeBytes(data)
    }
    for (typeId in value.componentsToRemove) writeVarInt(typeId)
}

internal fun BytesBuffer.readSlot(): Slot {
    val count = readVarInt()
    if (count <= 0) return Slot.EMPTY
    val itemId = readVarInt()
    val componentsToAddCount = readVarInt()
    val componentsToRemoveCount = readVarInt()
    val componentsToAdd = ArrayList<Slot.DataComponentToAdd>(componentsToAddCount)
    repeat(componentsToAddCount) {
        val typeId = readVarInt()
        val data = readPrefixedByteArray()
        componentsToAdd.add(Slot.DataComponentToAdd(typeId, data))
    }
    val componentsToRemove = ArrayList<Int>(componentsToRemoveCount)
    repeat(componentsToRemoveCount) { componentsToRemove.add(readVarInt()) }
    return Slot(
        count = count,
        itemId = itemId,
        componentsToAdd = componentsToAdd,
        componentsToRemove = componentsToRemove
    )
}