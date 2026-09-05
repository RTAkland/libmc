/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/25
 */


package cn.rtast.libmc.nbt

public fun NBTInput.readStringTag(): String {
    val length = readShort().toInt() and 0xFFFF
    val bytes = readBytes(length)
    return bytes.decodeToString()
}

public fun NBTInput.readListTag(): NBTTag.ListTag {
    val elementTypeId = readByte().toInt() and 0xFF
    val elementType = NBTType.fromId(elementTypeId)
    val length = readInt()
    val list = ArrayList<NBTTag>(length)
    repeat(length) { list += readTagPayload(elementType) }
    return NBTTag.ListTag(elementType, list)
}

public fun NBTInput.readCompoundTag(): NBTTag.CompoundTag {
    val map = LinkedHashMap<String, NBTTag>()
    while (true) {
        val typeId = readByte().toInt() and 0xFF
        if (typeId == 0) break // TAG_End
        val name = readStringTag()
        val type = NBTType.fromId(typeId)
        val payload = readTagPayload(type)
        map[name] = payload
    }
    return NBTTag.CompoundTag(map)
}

public fun NBTInput.readTagPayload(type: NBTType): NBTTag =
    when (type) {
        NBTType.Byte -> NBTTag.ByteTag(readByte())
        NBTType.Short -> NBTTag.ShortTag(readShort())
        NBTType.Int -> NBTTag.IntTag(readInt())
        NBTType.Long -> NBTTag.LongTag(readLong())
        NBTType.Float -> NBTTag.FloatTag(readFloat())
        NBTType.Double -> NBTTag.DoubleTag(readDouble())
        NBTType.String -> NBTTag.StringTag(readStringTag())
        NBTType.ByteArray -> NBTTag.ByteArrayTag(readBytes(readInt()))
        NBTType.IntArray -> NBTTag.IntArrayTag(IntArray(readInt()) { readInt() })
        NBTType.LongArray -> NBTTag.LongArrayTag(LongArray(readInt()) { readLong() })
        NBTType.List -> readListTag()
        NBTType.Compound -> readCompoundTag()
        NBTType.End -> error("TAG_End no payload")
    }

public fun NBTInput.readCompound(): NBTTag {
    val map = LinkedHashMap<String, NBTTag>()
    while (true) {
        val typeId = readByte().toInt()
        val type = NBTType.fromId(typeId)
        if (type == NBTType.End) break
        val nameLen = readShort().toInt() and 0xFFFF
        val nameBytes = readBytes(nameLen)
        val name = nameBytes.decodeToString()
        val value = readTagPayload(type)
        map[name] = value
    }
    return NBTTag.ListTag(NBTType.Compound, map.values.toMutableList())
}

public fun NBTInput.readRootCompound(): NBTCompound {
    val rootType = NBTType.fromId(readByte().toInt())
    require(rootType == NBTType.Compound) { "Root tag must be TAG_Compound" }
    val nameLen = readShort().toInt() and 0xFFFF
    val name = readBytes(nameLen).decodeToString()
    val root = readCompound()
    return NBTCompound(name, root)
}

public fun NBTInput.readNetworkCompound(): NBTCompound {
    val root = readCompound()
    return NBTCompound("", root)
}