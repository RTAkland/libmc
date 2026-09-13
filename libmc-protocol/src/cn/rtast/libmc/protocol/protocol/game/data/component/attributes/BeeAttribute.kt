/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

import cn.rtast.libmc.nbt.NBTCompound

public data class BeeAttribute(
    val entityType: Int,
    val entityData: NBTCompound,
    val ticksInHive: Int,
    val minTicksInHive: Int,
)