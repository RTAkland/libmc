/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.util

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.nbt.*

internal fun BytesBuffer.readNBTCompound(): NBTCompound =
    this.toNBTInput().readRootCompound()

internal fun BytesBuffer.readNetworkNBTCompound(): NBTCompound =
    this.toNBTInput().readNetworkCompound()

internal fun BytesBuffer.writeNetworkNBTCompound(value: NBTCompound) =
    this.toNBTOutput(NBTTag.CompoundTag(mutableMapOf("" to NBTTag.StringTag("")))).writeNetworkCompound(value)