/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.util

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.nbt.readNetworkCompound
import cn.rtast.libmc.nbt.readRootCompound
import cn.rtast.libmc.nbt.toNBTInput

internal fun BytesBuffer.readNBTCompound(): NBTCompound =
    this.toNBTInput().readRootCompound()

internal fun BytesBuffer.readNetworkNBTCompound(): NBTCompound =
    this.toNBTInput().readNetworkCompound()