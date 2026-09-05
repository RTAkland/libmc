/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/25
 */


package cn.rtast.libmc.nbt

@DslMarker
public annotation class NbtDsl

@NbtDsl
public class CompoundBuilder {
    private val tags = mutableMapOf<String, NBTTag>()


    public infix fun String.byte(value: Byte) {
        tags[this] = NBTTag.ByteTag(value)
    }

    public infix fun String.short(value: Short) {
        tags[this] = NBTTag.ShortTag(value)
    }

    public infix fun String.int(value: Int) {
        tags[this] = NBTTag.IntTag(value)
    }

    public infix fun String.long(value: Long) {
        tags[this] = NBTTag.LongTag(value)
    }

    public infix fun String.float(value: Float) {
        tags[this] = NBTTag.FloatTag(value)
    }

    public infix fun String.double(value: Double) {
        tags[this] = NBTTag.DoubleTag(value)
    }

    public infix fun String.byteArray(value: ByteArray) {
        tags[this] = NBTTag.ByteArrayTag(value)
    }

    public infix fun String.intArray(value: IntArray) {
        tags[this] = NBTTag.IntArrayTag(value)
    }

    public infix fun String.longArray(value: LongArray) {
        tags[this] = NBTTag.LongArrayTag(value)
    }

    public infix fun String.string(value: String) {
        tags[this] = NBTTag.StringTag(value)
    }

    public infix fun String.compound(block: CompoundBuilder.() -> Unit) {
        tags[this] = nbtCompound(block)
    }

    @Suppress("FunctionName")
    private fun _list(
        elementType: NBTType,
        block: NBTListBuilder.() -> Unit,
    ): NBTTag.ListTag {
        val builder = NBTListBuilder(elementType)
        builder.block()
        return builder.build()
    }

    public fun String.list(elementType: NBTType, block: NBTListBuilder.() -> Unit) {
        tags[this] = _list(elementType, block)
    }

    public fun build(): NBTTag.CompoundTag = NBTTag.CompoundTag(tags)
}

public fun nbtCompound(block: CompoundBuilder.() -> Unit): NBTTag.CompoundTag {
    val builder = CompoundBuilder()
    builder.block()
    return builder.build()
}