/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.block

import cn.rtast.libmc.nbt.NBTCompound

public data class BlockEntityInfo(
    val packedXz: Byte,
    val y: Short,
    val type: Int,
    val data: NBTCompound,
) {
    val localX: Int get() = (packedXz.toInt() and 0xFF) shr 4
    val localZ: Int get() = (packedXz.toInt() and 0xFF) and 15
}