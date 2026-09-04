/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.nbt

import cn.rtast.libmc.common.ByteOrder

public enum class NbtVariant(
    public val order: ByteOrder,
    public val isNetwork: Boolean,
) {
    JAVA(ByteOrder.BIG_ENDIAN, isNetwork = false),
    BEDROCK_DISK(ByteOrder.LITTLE_ENDIAN, isNetwork = false),
    BEDROCK_NETWORK(ByteOrder.LITTLE_ENDIAN, isNetwork = true)
}