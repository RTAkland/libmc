/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.*
import cn.rtast.libmc.protocol.protocol.game.data.component.DataComponent
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound
import cn.rtast.libmc.protocol.protocol.util.writeNetworkNBTCompound
import cn.rtast.libmc.protocol.registry.readDataComponent
import cn.rtast.libmc.protocol.registry.writeDataComponent

public data class BlockPredicate(
    val blocks: IdSet?,
    val properties: List<PropertyMatcher>?,
    val nbt: NBTCompound?,
    val components: List<ExactDataComponentMatcher>,
    val partialComponents: List<PartialDataComponentMatcher>,
)

public sealed interface PropertyMatcher {
    public val name: String

    public data class Exact(override val name: String, val value: String) :
        PropertyMatcher

    public data class Ranged(override val name: String, val minValue: String?, val maxValue: String?) :
        PropertyMatcher
}

public data class ExactDataComponentMatcher(val typeId: Int, val value: DataComponent)

public enum class PartialComponentPredicateType(public val id: Int) {
    DAMAGE(0),
    ENCHANTMENTS(1),
    STORED_ENCHANTMENTS(2),
    POTION_CONTENTS(3),
    CUSTOM_DATA(4),
    CONTAINER(5),
    BUNDLE_CONTENTS(6),
    FIREWORK_EXPLOSION(7),
    FIREWORKS(8),
    WRITABLE_BOOK_CONTENT(9),
    WRITTEN_BOOK_CONTENT(10),
    ATTRIBUTE_MODIFIERS(11),
    TRIM(12),
    JUKEBOX_PLAYABLE(13);

    public companion object {
        public fun fromID(id: Int): PartialComponentPredicateType = entries.first { it.id == id }
    }
}

public data class PartialDataComponentMatcher(val type: PartialComponentPredicateType, val nbt: NBTCompound)


internal fun BytesBuffer.readBlockPredicate(): BlockPredicate {
    val blocks = readPrefixOptional { readIdSet() }
    val properties = readPrefixOptional { readPrefixed { readPropertyMatcher() } }
    val nbt = readPrefixOptional { readNetworkNBTCompound() }
    val components = readPrefixed { readExactDataComponentMatcher() }
    val partialComponents = readPrefixed { readPartialDataComponentMatcher() }

    return BlockPredicate(
        blocks = blocks,
        properties = properties,
        nbt = nbt,
        components = components,
        partialComponents = partialComponents
    )
}

internal fun BytesBuffer.writeBlockPredicate(value: BlockPredicate) {
    writePrefixedOptional(value.blocks) { writeIdSet(it) }
    writePrefixedOptional(value.properties) { list ->
        writePrefixed(list) { writePropertyMatcher(it) }
    }
    writePrefixedOptional(value.nbt) { writeNetworkNBTCompound(it) }
    writePrefixed(value.components) { writeExactDataComponentMatcher(it) }
    writePrefixed(value.partialComponents) { writePartialDataComponentMatcher(it) }
}

internal fun BytesBuffer.readPropertyMatcher(): PropertyMatcher {
    val name = readMcString()
    val isExactMatch = readBoolean()
    return if (isExactMatch) {
        PropertyMatcher.Exact(name, readMcString())
    } else {
        val minValue = readPrefixOptional { readMcString() }
        val maxValue = readPrefixOptional { readMcString() }
        PropertyMatcher.Ranged(name, minValue, maxValue)
    }
}

internal fun BytesBuffer.writePropertyMatcher(value: PropertyMatcher) {
    writeMcString(value.name)
    when (value) {
        is PropertyMatcher.Exact -> {
            writeBoolean(true)
            writeMcString(value.value)
        }

        is PropertyMatcher.Ranged -> {
            writeBoolean(false)
            writePrefixedOptional(value.minValue) { writeMcString(it) }
            writePrefixedOptional(value.maxValue) { writeMcString(it) }
        }
    }
}

internal fun BytesBuffer.readExactDataComponentMatcher(): ExactDataComponentMatcher {
    val typeId = readVarInt()
    val component = readDataComponent(typeId)
    return ExactDataComponentMatcher(typeId, component)
}

internal fun BytesBuffer.writeExactDataComponentMatcher(value: ExactDataComponentMatcher) {
    writeVarInt(value.typeId)
    writeDataComponent(value.value)
}

internal fun BytesBuffer.readPartialDataComponentMatcher(): PartialDataComponentMatcher {
    val typeId = readVarInt()
    val predicateNbt = readNetworkNBTCompound()
    return PartialDataComponentMatcher(PartialComponentPredicateType.fromID(typeId), predicateNbt)
}

internal fun BytesBuffer.writePartialDataComponentMatcher(value: PartialDataComponentMatcher) {
    writeVarInt(value.type.id)
    writeNetworkNBTCompound(value.nbt)
}