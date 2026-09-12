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
    val elementType = NBTType.fromID(elementTypeId)
    val length = readInt()
    val list = ArrayList<NBTTag>(length)
    repeat(length) { list += readTagPayload(elementType) }
    return NBTTag.ListTag(elementType, list)
}

public fun NBTInput.readCompoundTag(): NBTTag.CompoundTag {
    val map = LinkedHashMap<String, NBTTag>()
    while (true) {
        val typeId = readByte().toInt() and 0xFF
        if (typeId == 0) break // TAG_End (0x00)
        val name = readStringTag()
        val type = NBTType.fromID(typeId)
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
        NBTType.End -> error("TAG_End has no payload")
    }

public fun NBTInput.readCompound(): NBTTag.CompoundTag {
    return readCompoundTag()
}

public fun NBTInput.readNBTRootCompound(): NBTCompound {
    val rootTypeId = readByte().toInt() and 0xFF
    val rootType = NBTType.fromID(rootTypeId)
    require(rootType == NBTType.Compound) { "Root tag must be TAG_Compound (0x0A), got: 0x${rootTypeId.toString(16).uppercase()}" }
    val name = readStringTag()
    val root = readCompoundTag()
    return NBTCompound(name, root)
}

public fun NBTInput.readNetworkCompound(): NBTCompound {
    val typeId = readByte().toInt() and 0xFF
    return when (NBTType.fromID(typeId)) {
        NBTType.Compound -> NBTCompound("", readCompoundTag())
        NBTType.String -> NBTCompound("", NBTTag.CompoundTag(linkedMapOf("text" to NBTTag.StringTag(readStringTag()))))
        NBTType.End -> NBTCompound("", NBTTag.CompoundTag(linkedMapOf()))
        else -> throw UnsupportedOperationException("Unsupported network NBT tag 0x${typeId.toString(16).uppercase()}")
    }
}