/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.nbt

import cn.rtast.libmc.common._Buffer
import cn.rtast.libmc.common.readVarInt

public class NbtReader(
    private val buffer: _Buffer,
    private val variant: NbtVariant = NbtVariant.JAVA,
) {
    private fun readShort(): Short = buffer.readShort(variant.order)
    private fun readInt(): Int = buffer.readInt(variant.order)
    private fun readLong(): Long = buffer.readLong(variant.order)
    private fun readFloat(): Float = Float.fromBits(readInt())
    private fun readDouble(): Double = Double.fromBits(readLong())

    private fun readString(): String {
        val length = if (variant.isNetwork) buffer.readVarInt() else readShort().toInt() and 0xFFFF
        if (length == 0) return ""
        return buffer.readBytes(length).decodeToString()
    }

    private fun readTagType(): Byte = if (variant.isNetwork) buffer.readVarInt().toByte() else buffer.readByte()

    public fun readRoot(): Pair<String, NbtCompound> {
        val type = readTagType()
        require(type == NBTType.COMPOUND) { "Expected Compound Tag (10), got $type" }
        val name = readString()
        val root = readCompound()
        return name to root
    }

    private fun readCompound(): NbtCompound {
        val map = mutableMapOf<String, NBTElement>()
        while (true) {
            val type = readTagType()
            if (type == NBTType.END) break

            val name = readString()
            map[name] = readPayload(type)
        }
        return NbtCompound(map)
    }

    private fun readList(): NbtList {
        val elementType = readTagType()
        val size = if (variant.isNetwork) buffer.readVarInt() else readInt()
        if (size <= 0) return NbtList(elementType, emptyList())
        val list = mutableListOf<NBTElement>()
        (0 until size).forEach { _ -> list.add(readPayload(elementType)) }
        return NbtList(elementType, list)
    }

    private fun readPayload(type: Byte): NBTElement {
        return when (type) {
            NBTType.BYTE -> NbtByte(buffer.readByte())
            NBTType.SHORT -> NbtShort(readShort())
            NBTType.INT -> {
                val value = if (variant.isNetwork) buffer.readVarInt().decodeZigZag() else readInt()
                NbtInt(value)
            }

            NBTType.LONG -> NbtLong(readLong())
            NBTType.FLOAT -> NbtFloat(readFloat())
            NBTType.DOUBLE -> NbtDouble(readDouble())
            NBTType.BYTE_ARRAY -> {
                val len = if (variant.isNetwork) buffer.readVarInt() else readInt()
                NbtByteArray(buffer.readBytes(len))
            }

            NBTType.STRING -> NbtString(readString())
            NBTType.LIST -> readList()
            NBTType.COMPOUND -> readCompound()
            NBTType.INT_ARRAY -> {
                val len = if (variant.isNetwork) buffer.readVarInt() else readInt()
                val array = IntArray(len) { readInt() }
                NbtIntArray(array)
            }

            NBTType.LONG_ARRAY -> {
                val len = if (variant.isNetwork) buffer.readVarInt() else readInt()
                val array = LongArray(len) { readLong() }
                NbtLongArray(array)
            }

            else -> throw IllegalArgumentException("Unknown Tag type: $type")
        }
    }
}