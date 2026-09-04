/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.nbt

internal fun Int.decodeZigZag(): Int = (this ushr 1) xor -(this and 1)
internal fun Int.encodeZigZag(): Int = (this shl 1) xor (this shr 31)
