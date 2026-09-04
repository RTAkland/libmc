/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.nbt

public typealias NBTTypeID = Byte

public object NBTType {
    public const val END: NBTTypeID = 0
    public const val BYTE: NBTTypeID = 1
    public const val SHORT: NBTTypeID = 2
    public const val INT: NBTTypeID = 3
    public const val LONG: NBTTypeID = 4
    public const val FLOAT: NBTTypeID = 5
    public const val DOUBLE: NBTTypeID = 6
    public const val BYTE_ARRAY: NBTTypeID = 7
    public const val STRING: NBTTypeID = 8
    public const val LIST: NBTTypeID = 9
    public const val COMPOUND: NBTTypeID = 10
    public const val INT_ARRAY: NBTTypeID = 11
    public const val LONG_ARRAY: NBTTypeID = 12
}