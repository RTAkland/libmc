/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/25
 */


package cn.rtast.libmc.nbt

public enum class NBTType(public val id: Byte) {
    End(0), Byte(1), Short(2), Int(3),
    Long(4), Float(5), Double(6), ByteArray(7),
    String(8), List(9), Compound(10),
    IntArray(11), LongArray(12);

    public companion object {
        public fun fromId(id: Int): NBTType =
            entries.firstOrNull { it.id.toInt() == id } ?: error("Unknown NBT type id: $id")
    }
}