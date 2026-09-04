/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.nbt

public sealed interface NBTElement {
    public val typeId: NBTTypeID
}

public data class NbtByte(val value: Byte) : NBTElement {
    override val typeId: NBTTypeID = NBTType.BYTE
}

public data class NbtShort(val value: Short) : NBTElement {
    override val typeId: NBTTypeID = NBTType.SHORT
}

public data class NbtInt(val value: Int) : NBTElement {
    override val typeId: NBTTypeID = NBTType.INT
}

public data class NbtLong(val value: Long) : NBTElement {
    override val typeId: NBTTypeID = NBTType.LONG
}

public data class NbtFloat(val value: Float) : NBTElement {
    override val typeId: NBTTypeID = NBTType.FLOAT
}

public data class NbtDouble(val value: Double) : NBTElement {
    override val typeId: NBTTypeID = NBTType.DOUBLE
}

public data class NbtByteArray(val value: ByteArray) : NBTElement {
    override val typeId: NBTTypeID = NBTType.BYTE_ARRAY
    override fun equals(other: Any?): Boolean = other is NbtByteArray && value.contentEquals(other.value)
    override fun hashCode(): Int = value.contentHashCode()
}

public data class NbtString(val value: String) : NBTElement {
    override val typeId: NBTTypeID = NBTType.STRING
}

public data class NbtList(val elementType: Byte, val elements: List<NBTElement>) : NBTElement {
    override val typeId: NBTTypeID = NBTType.LIST
}

public data class NbtCompound(val map: Map<String, NBTElement>) : NBTElement {
    override val typeId: NBTTypeID = NBTType.COMPOUND
}

public data class NbtIntArray(val value: IntArray) : NBTElement {
    override val typeId: NBTTypeID = NBTType.INT_ARRAY
    override fun equals(other: Any?): Boolean = other is NbtIntArray && value.contentEquals(other.value)
    override fun hashCode(): Int = value.contentHashCode()
}

public data class NbtLongArray(val value: LongArray) : NBTElement {
    override val typeId: NBTTypeID = NBTType.LONG_ARRAY
    override fun equals(other: Any?): Boolean = other is NbtLongArray && value.contentEquals(other.value)
    override fun hashCode(): Int = value.contentHashCode()
}