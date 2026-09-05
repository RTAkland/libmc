/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/25
 */


package cn.rtast.libmc.nbt


public fun NBTOutput.writeStringTag(value: String) {
    val bytes = value.encodeToByteArray()
    writeShort(bytes.size.toShort())
    writeBytes(bytes)
}

public fun NBTOutput.writeTagPayload(tag: NBTTag) {
    when (tag) {
        is NBTTag.ByteTag -> writeByte(tag.value)
        is NBTTag.ShortTag -> writeShort(tag.value)
        is NBTTag.IntTag -> writeInt(tag.value)
        is NBTTag.LongTag -> writeLong(tag.value)
        is NBTTag.FloatTag -> writeInt(tag.value.toBits())
        is NBTTag.DoubleTag -> writeLong(tag.value.toBits())
        is NBTTag.StringTag -> writeStringTag(tag.value)

        is NBTTag.ByteArrayTag -> {
            writeInt(tag.length)
            writeBytes(tag.value)
        }

        is NBTTag.IntArrayTag -> {
            writeInt(tag.length)
            tag.value.forEach { writeInt(it) }
        }

        is NBTTag.LongArrayTag -> {
            writeInt(tag.length)
            tag.value.forEach { writeLong(it) }
        }

        is NBTTag.ListTag -> {
            writeByte(tag.elementType.id)
            writeInt(tag.length)
            tag.value.forEach { writeTagPayload(it) }
        }

        is NBTTag.CompoundTag -> {
            for ((name, value) in tag.value) {
                writeByte(value.type.id)
                writeStringTag(name)
                writeTagPayload(value)
            }
            writeByte(NBTType.End.id)
        }
    }
}

public fun NBTOutput.writeRootNBTCompound(name: String = ""): ByteArray {
    writeByte(NBTType.Compound.id)
    writeStringTag(name)
    writeTagPayload(root)
    return toByteArray()
}
