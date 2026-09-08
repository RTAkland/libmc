/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/25
 */


package cn.rtast.libmc.nbt

public sealed class NBTTag(public val type: NBTType) {
    public data class ByteTag(val value: Byte) : NBTTag(NBTType.Byte)
    public data class ShortTag(val value: Short) : NBTTag(NBTType.Short)
    public data class IntTag(val value: Int) : NBTTag(NBTType.Int)
    public data class LongTag(val value: Long) : NBTTag(NBTType.Long)
    public data class FloatTag(val value: Float) : NBTTag(NBTType.Float)
    public data class DoubleTag(val value: Double) : NBTTag(NBTType.Double)
    public data class StringTag(val value: String) : NBTTag(NBTType.String)
    public data class CompoundTag(val value: Map<String, NBTTag>) : NBTTag(NBTType.Compound)
    public data class ListTag(val elementType: NBTType, val value: MutableList<NBTTag>) : NBTTag(NBTType.List) {
        public val length: Int get() = value.size
    }

    public data class IntArrayTag(val value: IntArray) : NBTTag(NBTType.IntArray) {
        public val length: Int get() = value.size
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            other as IntArrayTag
            if (!value.contentEquals(other.value)) return false
            if (length != other.length) return false
            return true
        }

        override fun hashCode(): Int {
            var result = value.contentHashCode()
            result = 31 * result + length
            return result
        }
    }

    public data class LongArrayTag(val value: LongArray) : NBTTag(NBTType.LongArray) {
        public val length: Int get() = value.size
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            other as LongArrayTag
            if (!value.contentEquals(other.value)) return false
            if (length != other.length) return false
            return true
        }

        override fun hashCode(): Int {
            var result = value.contentHashCode()
            result = 31 * result + length
            return result
        }

    }

    public data class ByteArrayTag(val value: ByteArray) : NBTTag(NBTType.ByteArray) {
        public val length: Int get() = value.size
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            other as ByteArrayTag
            if (!value.contentEquals(other.value)) return false
            if (length != other.length) return false
            return true
        }

        override fun hashCode(): Int {
            var result = value.contentHashCode()
            result = 31 * result + length
            return result
        }
    }
}