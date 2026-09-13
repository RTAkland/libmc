/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/25
 */


package cn.rtast.libmc.nbt

@NbtDsl
public class NBTListBuilder(
    private val elementType: NBTType,
) {
    private val elements = mutableListOf<NBTTag>()

    private fun checkType(expected: NBTType) =
        require(elementType == expected) { "TAG_List element type must same: expected $elementType, got $expected" }

    public fun byte(value: Byte) {
        checkType(NBTType.Byte)
        elements += NBTTag.ByteTag(value)
    }

    public fun short(value: Short) {
        checkType(NBTType.Short)
        elements += NBTTag.ShortTag(value)
    }

    public fun int(value: Int) {
        checkType(NBTType.Int)
        elements += NBTTag.IntTag(value)
    }

    public fun long(value: Long) {
        checkType(NBTType.Long)
        elements += NBTTag.LongTag(value)
    }

    public fun float(value: Float) {
        checkType(NBTType.Float)
        elements += NBTTag.FloatTag(value)
    }

    public fun double(value: Double) {
        checkType(NBTType.Double)
        elements += NBTTag.DoubleTag(value)
    }

    public fun string(value: String) {
        checkType(NBTType.String)
        elements += NBTTag.StringTag(value)
    }

    public fun compound(block: CompoundBuilder.() -> Unit) {
        checkType(NBTType.Compound)
        elements += buildNBT(block)
    }

    public fun list(elementType: NBTType, block: NBTListBuilder.() -> Unit) {
        checkType(NBTType.List)
        val builder = NBTListBuilder(elementType)
        builder.block()
        elements += builder.build()
    }

    public fun build(): NBTTag.ListTag = NBTTag.ListTag(elementType, elements)
}