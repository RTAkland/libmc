/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.item.slot

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt

public data class HashedSlot(
    val hasItem: Boolean,
    val itemId: Int?,
    val itemCount: Int?,
    val componentsToAdd: List<HashedSlotComponent>?,
    val componentsToRemove: List<Int>?,
) {
    public data class HashedSlotComponent(
        val componentId: Int,
        /**
         * CRC32C
         */
        val hash: Int,
    )

    public companion object {
        public val EMPTY: HashedSlot = HashedSlot(false, null, null, emptyList(), emptyList())
    }
}

internal fun BytesBuffer.readHashedSlot(): HashedSlot {
    val hasItem = readBoolean()
    val itemId = readOptional(hasItem) { readVarInt() }
    val itemCount = readOptional(hasItem) { readVarInt() }
    val componentsToAdd = readOptional(hasItem) {
        readPrefixed {
            val componentId = readVarInt()
            val hash = readInt()
            HashedSlot.HashedSlotComponent(componentId, hash)
        }
    }
    val componentsToRemove = readOptional(hasItem) { readPrefixed { readVarInt() } }
    return HashedSlot(hasItem, itemId, itemCount, componentsToAdd, componentsToRemove)
}