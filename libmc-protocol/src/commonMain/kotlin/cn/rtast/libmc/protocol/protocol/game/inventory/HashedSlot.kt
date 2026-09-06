/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.inventory

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.common.writeVarInt

public data class HashedSlot(
    val hasItem: Boolean,
    val itemId: Int?,
    val itemCount: Int?,
    val componentsToAdd: List<ComponentAddEntry>?,
    val componentsToRemove: List<Int>?,
) {
    public data class ComponentAddEntry(val typeId: Int, val dataHash: Int) {
        internal companion object Codec : PacketCodec<ComponentAddEntry> {
            override fun encode(buffer: BytesBuffer, value: ComponentAddEntry) {
                buffer.writeVarInt(value.typeId)
                buffer.writeVarInt(value.dataHash)
            }

            override fun decode(buffer: BytesBuffer): ComponentAddEntry {
                val typeId = buffer.readVarInt()
                val dataHash = buffer.readVarInt()
                return ComponentAddEntry(typeId, dataHash)
            }
        }
    }

    public companion object Codec : PacketCodec<HashedSlot> {
        public val EMPTY: HashedSlot = HashedSlot(false, null, null, null, null)

        override fun encode(buffer: BytesBuffer, value: HashedSlot) {
            buffer.writeBoolean(value.hasItem)
            if (!value.hasItem) return
            buffer.writeVarInt(requireNotNull(value.itemId) { "itemId must not be null when hasItem is true" })
            buffer.writeVarInt(requireNotNull(value.itemCount) { "itemCount must not be null when hasItem is true" })
            val addList = value.componentsToAdd ?: emptyList()
            buffer.writeVarInt(addList.size)
            addList.forEach { ComponentAddEntry.encode(buffer, it) }
            val removeList = value.componentsToRemove ?: emptyList()
            buffer.writeVarInt(removeList.size)
            removeList.forEach { buffer.writeVarInt(it) }
        }

        override fun decode(buffer: BytesBuffer): HashedSlot {
            val hasItem = buffer.readBoolean()
            if (!hasItem) return EMPTY
            val itemId = buffer.readVarInt()
            val itemCount = buffer.readVarInt()
            val addCount = buffer.readVarInt()
            val componentsToAdd = List(addCount) { ComponentAddEntry.decode(buffer) }
            val removeCount = buffer.readVarInt()
            val componentsToRemove = List(removeCount) { buffer.readVarInt() }
            return HashedSlot(hasItem, itemId, itemCount, componentsToAdd, componentsToRemove)
        }
    }
}